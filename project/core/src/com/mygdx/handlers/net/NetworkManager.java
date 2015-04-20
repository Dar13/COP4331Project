package com.mygdx.handlers.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.entities.Enemy;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.QueueCallback;
import com.mygdx.handlers.ThreadSafeList;
import com.mygdx.handlers.action.Action;
import com.mygdx.handlers.action.ActionCreateWave;
import com.mygdx.handlers.action.ActionEnemyCreate;
import com.mygdx.handlers.action.ActionEnemyDestroy;
import com.mygdx.handlers.action.ActionEnemyEnd;
import com.mygdx.handlers.action.ActionHealthChanged;
import com.mygdx.handlers.action.ActionHostPause;
import com.mygdx.handlers.action.ActionPlayerWaveReady;
import com.mygdx.handlers.action.ActionPlayersReady;
import com.mygdx.handlers.action.ActionTowerPlaced;
import com.mygdx.handlers.action.ActionTowerUpgraded;
import com.mygdx.handlers.action.ActionWaitForReady;
import com.mygdx.handlers.action.ActionWaveEnd;
import com.mygdx.handlers.action.ActionWaveStart;
import com.mygdx.net.ConnectionMode;
import com.mygdx.net.EnemyStatus;
import com.mygdx.net.EntityStatus;
import com.mygdx.net.GameConnection;
import com.mygdx.net.NetworkInterface;
import com.mygdx.net.PlayerStatus;
import com.mygdx.net.TowerStatus;
import com.mygdx.states.GameState;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstraction away from low-level networking for use in the game states.
 */
public class NetworkManager implements Runnable, QueueCallback
{
    public static final int SERVER_TCP_PORT = 0xDDD;
    public static final int SERVER_UDP_PORT = 0xDDE;
    public static final int MAX_CLIENTS = 4;
    public static final int ENTITY_ID_START = 0;
    private static final String LOG_TAG = "NetworkManager";

    protected AtomicBoolean initialized;
    protected Boolean ready;
    protected boolean initializeManager;
    protected boolean singleplayer = false;

    // server
    protected Boolean isServer;
    protected Server server;
    protected int uidCounter = 1; // All connections have UID >= 1
    protected int expectedAmountClients;

    protected int lastEntityID = ENTITY_ID_START;
    protected Map<Integer, EntityStatus> entityStatus;
    protected int currentWave = 1;
    protected Map<Integer, PlayerStatus> playerStatus;
    protected int lastPlayerID;
    protected boolean gameStarted;
    protected boolean waitingForLobby;
    protected boolean serverWaveReady = false;

    protected AtomicInteger outputQueueStatus;
    protected AtomicInteger inputQueueStatus;

    // client
    protected Client client;

    protected boolean validated = false;
    protected int playerID = -1;

    // threading stuff
    protected ReentrantReadWriteLock mutex;

    // serverstate that will eventually be moved to a new class
    protected int health = 10; // should be in MyGame maybe, so client can also load?
    protected int numEnemies = 0;
    protected boolean waveRunning = false;
    protected int lastUID = 0; // this represents the connectionID of the 'last' screen

    // ConnectionManager now facilitates details of Connection
    private ConnectionManager connectionManager;

    protected QueueManager queueManager;

    protected AtomicInteger queueStatus;

    protected boolean remoteChangesReady;

    public NetworkManager(HashMap<ConnectionMode, NetworkInterface> modes)
    {
        initialized = new AtomicBoolean(false);
        ready = false;
        initializeManager = false;
        expectedAmountClients = MAX_CLIENTS;
        connectionManager = new ConnectionManager(modes, MAX_CLIENTS, this);

        lastPlayerID = 1;

        playerStatus = new HashMap<>();
        playerStatus.put(0, PlayerStatus.SELF);
        gameStarted = false;

        // this is the closest to a traditional mutex I could find.
        // allows multiple reads at one time while only allowing one write lock.
        mutex = new ReentrantReadWriteLock(true);
        queueManager = new QueueManager(this);

        inputQueueStatus = new AtomicInteger(QueueCallbackStatus.CALLBACK_STATUS_NO_REQUEST.ordinal());
        outputQueueStatus = new AtomicInteger(QueueCallbackStatus.CALLBACK_STATUS_NO_OUTPUT.ordinal());
    }

    public void setSingleplayer(boolean value)
    {
        singleplayer = value;
    }

    public boolean getSingleplayer()
    {
        return singleplayer;
    }

    public void initialize()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("NET: Initializing NetworkManager");

                if(isServer == null)
                {
                    initialized.set(false);
                    System.out.println("NET: Initialization failed, isServer is null");
                }

                // this can't be null so ignore any warnings that it can be.
                if (isServer)
                {
                    server = new Server();
                    server.addListener(connectionManager);

                    entityStatus = new HashMap<>();
                }

                else
                {
                    client = new Client();
                    client.addListener(connectionManager);
                }

                connectionManager.prepare(true,
                        ConnectionMode.WIFI_LAN,
                        ConnectionMode.NONE,
                        true);

                connectionManager.initConnection(client, server);

                initialized.set(true);
            }
        }).start();
    }

    public boolean isInitialized()
    {
        return initialized.get();
    }

    public QueueManager getQueueManager()
    {
        return queueManager;
    }

    public void setExpectedAmountClients(int amount)
    {
        expectedAmountClients = amount;
    }

    public int getExpectedAmountClients()
    {
        return expectedAmountClients;
    }

    public int getPlayerID()
    {
        return playerID;
    }

    public void setPlayerID(int playerID)
    {
        this.playerID = playerID;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        while(!isInitialized())
        {
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        boolean run = true;

        while (run)
        {
            if (!ready)
            {
                // register network packet classes with KryoNet
                registerPackets();

                if (isServer)
                {
                    mutex.writeLock().lock();
                    try
                    {
                        run = setupServer();
                        if (!run)
                        {
                            continue;
                        }
                    }
                    finally
                    {
                        mutex.writeLock().unlock();
                    }
                }
                else
                {
                    mutex.writeLock().lock();
                    try
                    {
                        run = setupClient();
                        if (!run)
                        {
                            continue;
                        }
                    }
                    finally
                    {
                        mutex.writeLock().unlock();
                    }
                }

                // if it's made it this far, everything should be ready.
                ready = true;
            }

            if(outputQueueStatus.get() == QueueCallbackStatus.CALLBACK_STATUS_NO_REQUEST.ordinal())
            {
                outputQueueStatus.set(QueueCallbackStatus.CALLBACK_STATUS_PENDING_REQUEST.ordinal());
                queueManager.fetchWhenNotEmpty(this, QueueManager.QueueID.QUEUE_ID_SEND);
            }

            if (isServer)
            {
                // server is sending packets out and waiting for incoming connections.
                runServer();
            }
            else
            {
                // client is waiting for packets to come in.
                runClient();
            }
        }
    }

    public void setIsServer(boolean isServer)
    {
        this.isServer = isServer;
    }

    public int getFirstClientID()
    {
        for(Integer player : playerStatus.keySet())
        {
            if(playerStatus.get(player) == PlayerStatus.PLAYER)
            {
                return player;
            }
        }

        // special case for single player
        return 0;
    }

    public void registerPackets()
    {
        Kryo kryo = null;

        if(isServer)
        {
            kryo = server.getKryo();
        }
        else
        {
            kryo = client.getKryo();
        }

        kryo.register(GameConnection.ServerAuth.class);
        kryo.register(GameConnection.ClientAuth.class);
        kryo.register(GameConnection.PlayerID.class);

        kryo.register(Action.ActionClass.class);
        kryo.register(Entity.Type.class);
        kryo.register(ActionCreateWave.class);
        kryo.register(ActionEnemyCreate.class);
        kryo.register(ActionEnemyDestroy.class);
        kryo.register(ActionEnemyEnd.class);
        kryo.register(ActionHealthChanged.class);
        kryo.register(ActionHostPause.class);
        kryo.register(ActionPlayersReady.class);
        kryo.register(ActionTowerPlaced.class);
        kryo.register(ActionTowerUpgraded.class);
        kryo.register(ActionPlayerWaveReady.class);
        kryo.register(ActionWaitForReady.class);
        kryo.register(ActionWaveStart.class);
        kryo.register(ActionWaveEnd.class);
    }

    private boolean setupServer()
    {
        // bind server socket to the port and do other initialization as needed.
        try
        {
            server.start();
            server.bind(SERVER_TCP_PORT, SERVER_UDP_PORT);
            //serverSocket.bind(new InetSocketAddress(SERVER_PORT));
        }
        catch (IOException e)
        {
            System.out.println("NET: Server failed to bind to TCP port " + SERVER_TCP_PORT +
                               " and UDP port " + SERVER_UDP_PORT);
            e.printStackTrace();
            return false; // non-recoverable error so terminate the thread
        }

        return true;
    }


    /**
     * Runs the server logic while maintaining thread-safety.
     */
    private void runServer()
    {
        ArrayList<GameConnection> connections = connectionManager.getConnections();

        if(!gameStarted)
        {
            System.out.println("NET: Server waiting to start game");
            if(!waitingForLobby)
            {
                System.out.println("Server waiting for lobby");
                ActionWaitForReady waitForReady = new ActionWaitForReady();
                waitForReady.region = 0;
                addToSendQueue(waitForReady);
                waitingForLobby = true;
            }
        }

        if(connections.size() == 1 && !gameStarted)
        {
            boolean allWaiting = true;
            for(GameConnection conn : connections)
            {
                if(conn.isValidated() && !conn.waiting && conn.connection.isConnected() && serverWaveReady)
                {
                    ActionWaitForReady waitForReady = new ActionWaitForReady();
                    waitForReady.region = conn.playerID;
                    addToSendQueue(waitForReady);
                    conn.waiting = true;
                    allWaiting = false;
                }
            }

            if(allWaiting)
            {
                System.out.println("Players are ready!");
                for(GameConnection conn : connections)
                {
                    ActionPlayersReady playersReady = new ActionPlayersReady();
                    playersReady.region = conn.playerID;
                    addToSendQueue(playersReady);
                }

                ActionPlayersReady playersReady = new ActionPlayersReady();
                playersReady.region = 0;
                addToSendQueue(playersReady);

                gameStarted = true;
            }
        }

        boolean isAllReady = true;
        if(connections.isEmpty())
        {
            isAllReady = serverWaveReady;
        }
        else
        {
            for (GameConnection connection : connections)
            {
                if (!connection.waveReady)
                {
                    isAllReady = false;
                }
            }
        }

        if(isAllReady)
        {
            ActionCreateWave createWave = new ActionCreateWave(currentWave);
            numEnemies = createWave.amountTotalEnemies;
            createWave.region = getFirstClientID();
            currentWave++;

            if(singleplayer)
            {
                createWave.region = 0;
                System.out.println("Sending wave (singleplayer?)");
                queueManager.addToReceivedQueue(createWave, null);
            }
            else
            {
                System.out.println("Sending wave!");
                addToSendQueue(createWave);
            }

            if(singleplayer)
            {
                queueManager.addToReceivedQueue(new ActionWaveStart(), null);
            }
            else
            {
                for (GameConnection connection : connections)
                {
                    ActionWaveStart waveStart = new ActionWaveStart();
                    waveStart.region = connection.playerID;
                    addToSendQueue(waveStart);
                }

                ActionWaveStart waveStart = new ActionWaveStart();
                waveStart.region = 0;
                addToSendQueue(waveStart);
            }

            waveRunning = true;

            serverWaveReady = false;
            for(GameConnection connection : connections)
            {
                connection.waveReady = false;
            }
        }

        if(numEnemies == 0 && waveRunning)
        {
            waveRunning = false;
            if(singleplayer)
            {
                ActionWaveEnd waveEnd = new ActionWaveEnd();
                waveEnd.region = 0;
                queueManager.addToReceivedQueue(waveEnd, null);
            }
            else
            {
                for (GameConnection conn : connections)
                {
                    ActionWaveEnd waveEnd = new ActionWaveEnd();
                    waveEnd.region = conn.playerID;
                    addToSendQueue(waveEnd);
                }

                ActionWaveEnd waveEnd = new ActionWaveEnd();
                waveEnd.region = 0;
                addToSendQueue(waveEnd);
            }
        }

    }

    /**
     * Sets up the client and attempts to connect it to the server.
     *
     * NOTE: This method will return false in the case of getHostAddress() not being set or
     * an exception while connecting.
     * @return
     */
    private boolean setupClient()
    {
        Client tempClient;
        if((tempClient = connectionManager.setupClient(client)) == null)
            return false;
        client = tempClient;
        return true;
    }

    /**
     * Runs the client network synchronization while maintaining thread safety.
     */
    private void runClient()
    {
        if(client.isConnected())
        {
            // handled collected messages and pass actions to server via client.sendTCP()
        }
    }

    public void reset()
    {
        if(entityStatus != null)
            entityStatus.clear();

        connectionManager.reset();
        currentWave = 1;
    }

    /**
     * This method is to be called whenever an action happens; changes are queued locally so we
     * can easily alter sending and receiving timings
     */
    public void addToSendQueue(Action action)
    {
        queueManager.addToSendQueue(action, null);
    }


    /**
     * This method is to be called from within the Game State to request the updates that were
     * received by the network manager
     */
    public void fetchChanges(QueueCallback callback)
    {
        queueManager.fetchWhenNotEmpty(callback, QueueManager.QueueID.QUEUE_ID_RECEIVE);
    }

    /**
     * This should be called locally by our sync function, whatever form that takes
     */
    private void sendLocalChanges(ArrayList<Action> queuedLocalChanges)
    {

        if(!isServer)
        {
                for(Action action : queuedLocalChanges)
                {
                    client.sendTCP(action);
                }
        }

        else
        {
                if(connectionManager.getConnections().isEmpty() || singleplayer)
                {
                    queueManager.addAllSendToReceived();
                }

                else
                {
                    for (Action action : queuedLocalChanges)
                    {
                        for (GameConnection connection : connectionManager.getConnections())
                        {
                            if (connection.playerID == action.region)
                            {
                                System.out.format("Sending: %s\n", action.actionClass);
                                server.sendToTCP(connection.connection.getID(), action);
                            }

                            // Server is always playerID = 0
                            if(action.region == 0)
                            {
                                queueManager.addToReceivedQueue(action, null);
                            }
                        }
                    }
                }
            }

        queueManager.clear(QueueManager.QueueID.QUEUE_ID_SEND);
        outputQueueStatus.set(QueueCallbackStatus.CALLBACK_STATUS_NO_REQUEST.ordinal());
    }

    /**
     * This method should be called from whatever callback we use when changes come in from the
     * network
     */
    /*private synchronized void receiveChanges(List<Action> changes)
    {
        if(changes == null)
        {
            return;
        }

        List<Action> actions = new ArrayList<>(changes);

        // if we are a client, we just take the new changes and leave
        if(!isServer)
        {
            queuedRemoteChanges.addAll(actions.get);
            return;
        }

        for(Action change : actions)
        {
            receiveChange(change);
        }
    }*/

    public void receiveChange(Action change)
    {
        if(change == null)
        {
            return;
        }

        System.out.println("Packet type: " + change.actionClass);

        switch(change.actionClass)
        {
            case ACTION_PLAYER_WAVE_READY:
                ActionPlayerWaveReady playerReady = (ActionPlayerWaveReady)change;
                if(connectionManager.getConnections().size() > 0)
                    connectionManager.getConnections().get(playerReady.region - 1).waveReady = true;
                else
                {
                    serverWaveReady = true;
                }
                break;

            case ACTION_ENEMY_CREATE:
                ActionEnemyCreate actionCreate = (ActionEnemyCreate)change;
                actionCreate.entityID = lastEntityID + 1;
                lastEntityID++;

                entityStatus.put(actionCreate.entityID, new EnemyStatus(actionCreate));

                if(singleplayer)
                {
                    queueManager.addToReceivedQueue(actionCreate, null);
                }

                else
                {
                    addToSendQueue(actionCreate);
                }

                break;

            case ACTION_ENEMY_END:
                ActionEnemyEnd actionEnd = (ActionEnemyEnd)change;


                if(entityStatus.containsKey(actionEnd.entityID))
                {
                    // if the enemy is at region 0, then its at the end
                    if(entityStatus.get(actionEnd.entityID).region == 0)
                    {
                        health--;
                        numEnemies--;
                        ActionHealthChanged actionHealth = new ActionHealthChanged(health);

                        if(singleplayer)
                        {
                            queueManager.addToReceivedQueue(actionEnd, null);
                        }
                        else
                        {
                            addToSendQueue(actionHealth);
                        }
                    }

                    else
                    {
                        // e.g., if we have 2 connections, there are 3 screens. if we are at region 2,
                        // we need to send the enemy to server, or region 0. (2+1) % (2+1) = 0;
                        entityStatus.get(actionEnd.entityID).region += 1;
                        entityStatus.get(actionEnd.entityID).region %= (connectionManager.getConnections().size() + 1);

                        EnemyStatus transfer = (EnemyStatus) entityStatus.get(actionEnd.entityID);
                        transfer.velocity = actionEnd.velocity;

                        ActionEnemyCreate actionEndCreate = new ActionEnemyCreate(transfer);

                        //add actionEndCreate to the queue that's going back out to the clients.
                        addToSendQueue(actionEndCreate);
                    }
                }
                break;

            case ACTION_ENEMY_DESTROY:
                ActionEnemyDestroy actionDestroy = (ActionEnemyDestroy)change;


                if (entityStatus.containsKey(actionDestroy.entityID))
                {
                    entityStatus.remove(actionDestroy.entityID);
                    numEnemies--;
                }
                break;

            case ACTION_ENEMY_DAMAGE:
                // ignore for now
                break;

            case ACTION_TOWER_PLACED:
                ActionTowerPlaced actionTowerPlaced = (ActionTowerPlaced)change;

                actionTowerPlaced.entityID = lastEntityID + 1;
                lastEntityID++;

                entityStatus.put(actionTowerPlaced.entityID, new TowerStatus(actionTowerPlaced));

                if(singleplayer)
                {
                    queueManager.addToReceivedQueue(actionTowerPlaced, null);
                }
                else
                {
                    addToSendQueue(actionTowerPlaced);
                }
                break;

            case ACTION_TOWER_UPGRADED:
                ActionTowerUpgraded actionTowerUpgraded = (ActionTowerUpgraded)change;

                if(entityStatus.containsKey(actionTowerUpgraded.entityID))
                {
                    TowerStatus towerStatus = (TowerStatus)entityStatus.get(actionTowerUpgraded.entityID);
                    towerStatus.level++;

                    if(towerStatus.level > Tower.MAX_LEVEL)
                    {
                        towerStatus.level = Tower.MAX_LEVEL;
                        entityStatus.put(actionTowerUpgraded.level, towerStatus);

                        actionTowerUpgraded.level = towerStatus.level;
                        if(singleplayer)
                        {
                            queueManager.addToReceivedQueue(actionTowerUpgraded, null);
                        }
                        else
                        {
                            addToSendQueue(actionTowerUpgraded);
                        }
                    }
                }

                break;
        }


    }

    protected boolean handleValidation(GameConnection gameConnection, Connection connection, Object object)
    {
        // check for validation message.
        // otherwise close connection
        // validation message should be first thing sent on the connection from the client.
        if(object instanceof GameConnection.ClientAuth)
        {
            GameConnection.ClientAuth authPacket = (GameConnection.ClientAuth)object;
            if(authPacket.key == MyGame.VERSION)
            {
                gameConnection.connection.sendTCP(new GameConnection.ServerAuth());
                gameConnection.assignUID(uidCounter);
                uidCounter++;

                gameConnection.playerID = lastPlayerID;
                GameConnection.PlayerID idPacket = new GameConnection.PlayerID();
                gameConnection.connection.sendTCP(idPacket);

                idPacket.playerID = gameConnection.playerID;

                lastPlayerID++;

                playerStatus.put(gameConnection.playerID, PlayerStatus.PLAYER);

                ActionWaitForReady actionWaitReady = new ActionWaitForReady();
                addToSendQueue(actionWaitReady);

                System.out.println("NET: Client auth key = " + authPacket.key);
                return true;
            }
            else
            {
                System.out.println("NET: Client failed authentication. Allowing retries...");
                return false;
            }
        }

        // client attempting to validate.
        if(object instanceof GameConnection.ServerAuth)
        {
            GameConnection.ServerAuth authPacket = (GameConnection.ServerAuth)object;
            if(authPacket.key != MyGame.VERSION)
            {
                // ERROR, invalid server
                System.out.println("NET: Incompatible server detected. Key = " + authPacket.key);
                gameConnection.connection.close();
                return false;
            }
            else
            {
                return true;
            }
        }

        return false;
    }


    public ConnectionManager getConnectionManager()
    {
        return connectionManager;
    }

    @Override
    public void addCompleted()
    {

    }

    @Override
    public void retrieved(Object o, QueueManager.QueueID queueID)
    {
        switch(queueID)
        {
            case QUEUE_ID_SEND:
                sendLocalChanges((ArrayList<Action>) o);
                break;
        }
    }
}

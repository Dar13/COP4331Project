package com.mygdx.handlers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.entities.Tower;
import com.mygdx.game.MyGame;
import com.mygdx.handlers.action.Action;
import com.mygdx.handlers.action.ActionCreateWave;
import com.mygdx.handlers.action.ActionEnemyCreate;
import com.mygdx.handlers.action.ActionEnemyDestroy;
import com.mygdx.handlers.action.ActionEnemyEnd;
import com.mygdx.handlers.action.ActionHealthChanged;
import com.mygdx.handlers.action.ActionPlayerWaveReady;
import com.mygdx.handlers.action.ActionTowerPlaced;
import com.mygdx.handlers.action.ActionTowerUpgraded;
import com.mygdx.net.ConnectionMode;
import com.mygdx.net.EnemyStatus;
import com.mygdx.net.EntityStatus;
import com.mygdx.net.GameConnection;
import com.mygdx.net.NetworkInterface;
import com.mygdx.net.TowerStatus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstraction away from low-level networking for use in the game states.
 */
public class NetworkManager extends Listener implements Runnable
{
    public static final int SERVER_TCP_PORT = 0xDDD;
    public static final int SERVER_UDP_PORT = 0xDDE;
    public static final int MAX_CLIENTS = 4;
    public static final int ENTITY_ID_START = 0;

    //These lists will hold changes temporarily until they are either sent or applied
    private List<Action> queuedLocalChanges;
    private List<Action> queuedRemoteChanges;

    protected AtomicBoolean initialized;
    protected Boolean ready;
    protected boolean initializeManager;
    protected boolean singleplayer = false;

    // server
    protected Boolean isServer;
    protected Server server;
    protected int uidCounter = 1; // All connections have UID >= 1
    protected ArrayList<GameConnection> connections;
    protected int expectedAmountClients;

    protected int lastEntityID = ENTITY_ID_START;
    protected Map<Integer, EntityStatus> entityStatus;
    protected int currentWave = 1;

    // client
    protected Client client;
    protected ArrayList<InetAddress> serverAddresses;
    protected InetAddress hostAddress;
    protected boolean validated = false;

    // techniques of connecting
    protected ConnectionMode preferredMode;
    protected ConnectionMode fallbackMode;
    protected HashMap<ConnectionMode, NetworkInterface> networkInterfaces;
    protected NetworkInterface networkInterface;

    // threading stuff
    protected ReentrantReadWriteLock mutex;

    // serverstate that will eventually be moved to a new class
    protected int health = 10; // should be in MyGame maybe, so client can also load?
    protected int lastUID = 0; // this represents the connectionID of the 'last' screen

    public NetworkManager(HashMap<ConnectionMode, NetworkInterface> modes)
    {
        networkInterfaces = modes;
        initialized = new AtomicBoolean(false);
        ready = false;
        initializeManager = false;
        expectedAmountClients = MAX_CLIENTS;

        // this is the closest to a traditional mutex I could find.
        // allows multiple reads at one time while only allowing one write lock.
        mutex = new ReentrantReadWriteLock(true);
        queuedLocalChanges = new ArrayList<Action>();
        queuedRemoteChanges = new ArrayList<Action>();
    }

    public HashMap<ConnectionMode, NetworkInterface> getNetworkImpls()
    {
        mutex.readLock().lock();
        try
        {
            return networkInterfaces;
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public synchronized void setSingleplayer(boolean value)
    {
        singleplayer = value;
    }

    public boolean getSingleplayer()
    {
        return singleplayer;
    }

    public void prepInitialize(boolean isServer,
                               ConnectionMode primaryMode,
                               ConnectionMode fallbackMode,
                               boolean initializeNow)
    {
        mutex.writeLock().lock();
        try
        {
            this.isServer = isServer;
            this.preferredMode = primaryMode;
            this.fallbackMode = fallbackMode;
            this.initializeManager = initializeNow;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public void setInitializeFlag(boolean flag)
    {
        mutex.writeLock().lock();
        try
        {
            initializeManager = flag;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public boolean initialize()
    {
        System.out.println("NET: Initializing NetworkManager");
        mutex.writeLock().lock();
        try
        {
            // make sure primary mode isn't null, secondary mode is nullable.
            if (isServer == null || this.preferredMode == null)
            {
                initialized.set(false);
                return false;
            }

            // this can't be null so ignore any warnings that it can be.
            if (isServer)
            {
                // setup class members for server stuff.
                connections = new ArrayList<>();

                server = new Server();
                server.addListener(this);

                entityStatus = new HashMap<>();
            }
            else
            {
                client = new Client();
                client.addListener(this);
            }

            networkInterface = networkInterfaces.get(this.preferredMode);

            if (networkInterface != null)
            {
                serverAddresses = networkInterface.setup(client, server);

                // check to see if primary mode failed to setup properly
                if (!networkInterface.isReady())
                {
                    // switch to fallback connection mode
                    networkInterface = networkInterfaces.get(fallbackMode);

                    if (networkInterface == null)
                    {
                        // invalid fallback
                        System.out.println("NET: Selected fallback connection mode is not supported");
                        return false;
                    }
                    else
                    {
                        serverAddresses = networkInterface.setup(client, server);
                        if (!networkInterface.isReady())
                        {
                            // fallback failed
                            System.out.println("NET: Fallback mode failed to initialize");
                            initialized.set(false);
                            return false;
                        }
                    }
                }
            }
            else
            {
                System.out.println("NET: Selected connection mode is not supported.");
                initialized.set(false);
                return false;
            }

            initialized.set(true);
            return true;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public boolean isInitialized()
    {
        mutex.readLock().lock();
        try
        {
            return initialized.get();
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public void setExpectedAmountClients(int amount)
    {
        expectedAmountClients = amount;
    }

    public int getExpectedAmountClients()
    {
        mutex.readLock().lock();
        try
        {
            return expectedAmountClients;
        }
        finally
        {
            mutex.readLock().unlock();
        }
    }

    public synchronized void setHostAddress(InetAddress addr)
    {
        mutex.writeLock().lock();
        try
        {
            hostAddress = addr;
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }

    public synchronized InetAddress getHostAddress()
    {
        mutex.readLock().lock();
        try
        {
            return hostAddress;
        }
        finally
        {
            mutex.readLock().unlock();
        }
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
        // wait for initialization
        while (!isInitialized())
        {
            if(initializeManager)
            {
                initialize();
            }

            // sleep for 5 milliseconds
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                // not really sure how to handle this.
                // terminate?
                System.out.println("NET: Interrupt caught while waiting for initialization.");
                return;
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

            sendLocalChanges();

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

    public void registerPackets()
    {
        if(isServer)
        {
            Kryo kryo = server.getKryo();
            kryo.register(GameConnection.ServerAuth.class);
            kryo.register(GameConnection.ClientAuth.class);
        }
        else
        {
            Kryo kryo = client.getKryo();
            kryo.register(GameConnection.ServerAuth.class);
            kryo.register(GameConnection.ClientAuth.class);
        }
    }

    public boolean syncReady()
    {
        return true;
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
        mutex.writeLock().lock();
        try
        {
            boolean isAllReady = true;
            for (GameConnection connection : connections)
            {
                if(!connection.waveReady)
                {
                    isAllReady = false;
                }
            }

            if(isAllReady)
            {
                ActionCreateWave createWave = new ActionCreateWave(currentWave);
                currentWave++;

                addToSendQueue(createWave);

                for(GameConnection connection : connections)
                {
                    connection.waveReady = false;
                }
            }
        }
        finally
        {
            mutex.writeLock().unlock();
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
        // if hostAddress isn't set and servers aren't found,
        // return false to force it to wait until it's set.
        if(getHostAddress() == null && serverAddresses == null)
        {
            return false;
        }

        // if the client isn't connected, attempt to connect. Will repeat infinitely at the moment.
        if (!client.isConnected())
        {
            try
            {
                client.start();
                if(getHostAddress() != null)
                {
                    client.connect(5000, hostAddress, SERVER_TCP_PORT, SERVER_UDP_PORT);
                }

                if(!client.isConnected() && serverAddresses != null)
                {
                    for(InetAddress address : serverAddresses)
                    {
                        try
                        {
                            client.connect(5000, address, SERVER_TCP_PORT, SERVER_UDP_PORT);
                        }
                        catch(SocketTimeoutException e)
                        {
                            System.out.println("NET: Client timed out trying to connect to host.");
                            System.out.println("NET: Attempting next host.");
                        }
                    }
                }
            }
            catch (SocketTimeoutException et)
            {
                System.out.println("NET: Client timed out trying to connect to host. Retrying...");
                return false;
            }
            catch (IOException e)
            {
                System.out.println("NET: Exception during client connection. Not retrying. Maybe.");
                e.printStackTrace();
                return false; // non-recoverable error so terminate the thread
            }
        }

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

    @Override
    public void connected(Connection connection)
    {
        if(isServer)
        {
            if(connections.size() < expectedAmountClients)
            {
                GameConnection gameConnection = new GameConnection();
                gameConnection.connection = connection;
                connections.add(gameConnection);
            }
            else
            {
                // don't need anymore connections. (rethink if we ever do chromecast stuff)
                connection.close();
                System.out.println("NET: Closing connection. ID = " + connection.getID());
            }
        }
        else
        {
            // send validation packet
            GameConnection.ClientAuth authPacket = new GameConnection.ClientAuth();
            client.sendTCP(authPacket);

            System.out.println("NET: Connected to server! Connection ID = " + connection.getID());
        }
    }

    @Override
    public void received(Connection connection, Object object)
    {
        System.out.println("NET: Packet received!");
        boolean handled = false;

        if(isServer)
        {
            // Might want to change to hashmap<id, connection> and have the connection ID or some other UID
            // embedded in the packet itself. Not sure if its worth it, as the connections list should
            // fairly small (n < [4,8,12,16])
            for (GameConnection gameConnection : connections)
            {
                if (gameConnection.connection.getID() == connection.getID())
                {
                    handled = true;

                    if (!gameConnection.isValidated())
                    {
                        handleValidation(gameConnection, null, object);
                        System.out.println("NET: TCP Packet received from Connection! ID = " + gameConnection.connection.getID());
                    }
                    else
                    {
                        // handle normally.
                        // decode to List<BaseChange> and then call receiveChanges()

                        if(object instanceof Action)
                        {
                            Action action = (Action)object;
                            action.region = gameConnection.getUID();
                            receiveChange(action);
                        }
                    }
                }
            }
        }
        else // client packet handling
        {
            System.out.println("NET: Packet received from server!");
            if(!validated)
            {
                validated = handleValidation(null, connection, object);
            }

            // assume authenticated, handle packet normally
        }

        // mystery packet!
        if(!handled)
        {
            System.out.println("NET: Unknown packet received. Connection ID = " + connection.getID());
            System.out.println("NET: Unknown packet source = " + connection.getRemoteAddressTCP());
        }
    }

    /**
     * This method is to be called whenever an action happens; changes are queued locally so we
     * can easily alter sending and receiving timings
     */
    public void addToSendQueue(Action action)
    {
        /*
        if(isServer && action.needsID)
            action.entity.entityID = entityID.getAndIncrement();
            */
        mutex.writeLock().lock();
        try
        {
            queuedLocalChanges.add(action);
        }
        finally
        {
            mutex.writeLock().unlock();
        }
    }


    /**
     * This method is to be called from within the Game State to request the updates that were
     * received by the network manager
     */
    public List<Action> fetchChanges()
    {
        if(syncReady())
        {
            List<Action> tmp = queuedRemoteChanges;
            queuedRemoteChanges = new ArrayList<Action>();
            return tmp;
        }

        else
            return null;
    }

    /**
     * This should be called locally by our sync function, whatever form that takes
     */
    private void sendLocalChanges()
    {
        if(!isServer)
        {
            mutex.readLock().lock();
            try
            {
                for(Action action : queuedLocalChanges)
                {
                    client.sendTCP(action);
                }
            }
            finally
            {
                mutex.readLock().unlock();
            }

            mutex.writeLock().lock();
            try
            {
                queuedLocalChanges.clear();
            }
            finally
            {
                mutex.writeLock().unlock();
            }
        }
        else
        {
            List<Action> tmp;
            mutex.writeLock().lock();
            try
            {
                tmp = queuedLocalChanges;
                queuedLocalChanges = new ArrayList<Action>();
            }
            finally
            {
                mutex.writeLock().unlock();
            }

            receiveChanges(tmp);
        }
        /**
         * TODO: implement this method with Kryonet, Pseudocode follows:
         *  if (local changes are not empty or being written)
         *      send the local change queue to Kryonet
         */
    }

    /**
     * This method should be called from whatever callback we use when changes come in from the
     * network
     */
    private void receiveChanges(List<Action> changes)
    {
        /**
         * TODO: this method is implementation dependent, but should look roughly like the following:
         * if (isServer)
         *     check received changes for coherency with master state
         *     push valid changes to send queue (so they can be sent back out to other clients)
         * add all changes to update queue, so game can read them in when needed
         */
        if(changes == null)
        {
            return;
        }

        // if we are a client, we just take the new changes and leave
        if(!isServer)
        {
            queuedRemoteChanges.addAll(changes);
            return;
        }

        for(Action change : changes)
        {
            receiveChange(change);
        }
    }

    private void receiveChange(Action change)
    {
        if(change == null)
        {
            return;
        }

        switch(change.actionClass)
        {
        case ACTION_PLAYER_WAVE_READY:
            ActionPlayerWaveReady playerReady = (ActionPlayerWaveReady)change;

            connections.get(playerReady.region).waveReady = true;
            break;
        case ACTION_ENEMY_CREATE:
            ActionEnemyCreate actionCreate = (ActionEnemyCreate)change;
            actionCreate.entityID = lastEntityID + 1;
            lastEntityID++;

            mutex.writeLock().lock();
            try
            {
                entityStatus.put(actionCreate.entityID, new EnemyStatus(actionCreate));

                if(singleplayer)
                {
                    queuedRemoteChanges.add(actionCreate);
                }
                else
                {
                    addToSendQueue(actionCreate);
                }
            }
            finally
            {
                mutex.writeLock().unlock();
            }

            break;
        case ACTION_ENEMY_END:
            ActionEnemyEnd actionEnd = (ActionEnemyEnd)change;

            mutex.writeLock().lock();
            try
            {
                if(entityStatus.containsKey(actionEnd.entityID))
                {
                    // Move enemy to next region.
                    entityStatus.get(actionEnd.entityID).region += 1;

                    if(entityStatus.get(actionEnd.entityID).region >= connections.size())
                    {
                        health--;
                        ActionHealthChanged actionHealth = new ActionHealthChanged(health);
                        if(singleplayer)
                        {
                            queuedRemoteChanges.add(actionHealth);
                        }
                        else
                        {
                            addToSendQueue(actionHealth);
                        }
                    }

                    ActionEnemyCreate actionEndCreate = new ActionEnemyCreate((EnemyStatus) entityStatus.get(actionEnd.entityID));

                    //add actionEndCreate to the queue that's going back out to the clients.
                    addToSendQueue(actionEndCreate);
                }
            }
            finally
            {
                mutex.writeLock().unlock();
            }
            break;
        case ACTION_ENEMY_DESTROY:
            ActionEnemyDestroy actionDestroy = (ActionEnemyDestroy)change;

            mutex.writeLock().lock();
            try
            {
                if (entityStatus.containsKey(actionDestroy.entityID))
                {
                    entityStatus.remove(actionDestroy.entityID);
                }
            }
            finally
            {
                mutex.writeLock().unlock();
            }
            break;
        case ACTION_ENEMY_DAMAGE:
            // ignore for now
            break;
        case ACTION_TOWER_PLACED:
            ActionTowerPlaced actionTowerPlaced = (ActionTowerPlaced)change;

            actionTowerPlaced.entityID = lastEntityID + 1;
            lastEntityID++;

            mutex.writeLock().lock();
            try
            {
                entityStatus.put(actionTowerPlaced.entityID, new TowerStatus(actionTowerPlaced));
                if(singleplayer)
                {
                    queuedRemoteChanges.add(actionTowerPlaced);
                }
                else
                {
                    addToSendQueue(actionTowerPlaced);
                }
            }
            finally
            {
                mutex.writeLock().unlock();
            }
            break;
        case ACTION_TOWER_UPGRADED:
            ActionTowerUpgraded actionTowerUpgraded = (ActionTowerUpgraded)change;

            mutex.writeLock().lock();
            try
            {
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
                            queuedRemoteChanges.add(actionTowerUpgraded);
                        }
                        else
                        {
                            addToSendQueue(actionTowerUpgraded);
                        }
                    }
                }
            }
            finally
            {
                mutex.writeLock().unlock();
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
                System.out.println("NET: Client auth key = " + authPacket.key);
                return true;
            }
            else
            {
                System.out.println("NET: Client failed authentication. Allowing retries...");
                return false;
            }
        }

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


}

package com.mygdx.game;

/**
 * Created by Tanner Foster on 2/8/2015.
 * Abstract enemy class, contains the very basic movement concepts for the enemies as well as their
 * all their statuses at present.
 */

public abstract class AbstractEnemy {
        private String type;
        private int XCoord;
        private int YCoord;
        private int Health = 100;
        private int Armor;
        private int BaseSpeed;
        private char Direction;
        private boolean IsAlive = true;
        private int DistTravel = 0;

        public AbstractEnemy(String type, int XCoord, int YCoord, char Direction){
            this.type = type;
            this.XCoord = XCoord;
            this.YCoord = YCoord;
            this.Direction = Direction;
            SetType(type);
        }


        public String GetType(){
            return type;
        }

        public int GetXCoords(){
            return XCoord;
        }

        public int GetYCoords(){
            return YCoord;
        }

        public int GetHealth(){
            return Health;
        }

        public int GetArmor(){
            return Armor;
        }

        public int GetBaseSpeed(){
            return BaseSpeed;
        }

        public char GetDirection(){
            return Direction;
        }

        public void SetType(String type){
            String normal = new String("Normal");
            String fast = new String("Fast");
            if (type.equals(normal)){
                Armor = 1;
                BaseSpeed = 1;
            }
            else if (type.equals(fast)){
                Armor = 1;
                BaseSpeed = 2;
            }
        }

        public void MoveEnemy(){
            if(Direction == 'n'){
                YCoord++;
                DistTravel++;
            }

            else if(Direction == 's'){
                YCoord--;
                DistTravel++;
            }

            else if(Direction == 'e'){
                XCoord++;
                DistTravel++;
            }

            else if(Direction == 'w'){
                XCoord--;
                DistTravel++;
            }
        }

        public void DecrimentHealth(int damage){
            Health = Health - (damage / Armor);

            if (Health <= 0){
                IsAlive = false;
            }
        }

        public boolean IsAlive(){
            return IsAlive;
        }
}

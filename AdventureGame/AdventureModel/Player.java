package AdventureModel;

import AdventureModel.NPCs.NPC;
import AdventureModel.Strategies.*;
import javafx.scene.Node;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player implements Serializable {

    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;
    /**
     * The amount of coins that the player currently has.
     */
    private double coins;
    /**
     * The amount of health points that the player currently has.
     */
    private double health;
    /**
     * The amount of reputation points that the player currently has.
     */
    private double reputation;
    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;
    /**
     * The player's fighting strategy.
     */
    public FightStrategy fightStrategy = new FightWithoutPowerUp();
    /**
     * The player's moving strategy.
     */
    public MoveStrategy moveStrategy = new MoveWithoutPowerUp();


    /**
     * Adventure Game Player Constructor
     */
    public Player(Room currentRoom) {
        this.inventory = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
        this.coins = 15;
        this.health = 100;
        this.reputation = 100;
    }


    /**
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.
     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method returns an object given its name, if it is in the inventory.
     *
     * @param s The name of the object.
     * @return The required object from the inventory, if present.
     */
    public AdventureObject getObject(String s) {
        for (int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return this.inventory.get(i);
        }
        return null;
    }


    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }


    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * Reduce the player's health by the damage dealt by an NPC
     * @param npc the NPC to fight
     * @return the amount of damage taken
     */
    public String fight(NPC npc){
        String text = this.fightStrategy.fight(npc, this);
        if (this.fightStrategy instanceof FightWithSP){
            this.fightStrategy = new FightWithoutPowerUp();
        }
        return text;
    }

    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }


    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }


    /**
     * Getter method for the player's remaining coins.
     *
     * @return Amount of coins the player currently has.
     */
    public double getCoins(){
        return this.coins;
    }


    /**
     * Add or remove the given amount of coins to the player's wallet.
     *
     * @param amount Amount of coins the player loses/gains.
     */
    public void setBalance(double amount){
        this.coins += amount;
    }


    /**
     * Getter method for the player's remaining health.
     *
     * @return Current health of the player.
     */
    public double getHealth() {
        return this.health;
    }


    /**
     * Add the given amount of health points to the player.
     *
     * @param hp Amount of health points to gain.
     */
    public void healPlayer(double hp) {
        this.health += hp;
    }


    /**
     * Getter method for the player's current reputation.
     *
     * @return Current reputation of the player.
     */
    public double getRep() {
        return this.reputation;
    }


    /**
     * Add the given amount of reputation points to the player.
     *
     * @param rep Amount of reputation points to gain.
     */
    public void gainRep(double rep) {
        this.reputation += rep;
    }


    /**
     * Returns a text in the format: You have [health] hp remaining!
     *
     * @return Text containing the player's health.
     */
    public String getHealthText() {
        return "You have " + this.health + " hp remaining!";
    }
}


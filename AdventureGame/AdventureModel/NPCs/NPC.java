package AdventureModel.NPCs;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

/**
 * NPC class, the abstract superclass for all NPCs in the game.
 */
public abstract class NPC {
    /**
     * The name of the NPC
     */
    public String name;

    /**
     * The health points of the NPC
     */
    public double hp;

    /**
     * The Powerup carried by the NPC
     */
    public PowerUp item;

    /**
     * The room the NPC is in
     */
    Room room;

    /**
     * The text that the NPC says when the player encounters it
     */
    private final String text;

    /**
     * The text shown when the NPC dies
     */
    private final String deathText;

    /**
     *
     * @param name the name of the NPC
     * @param hp the health points of the NPC
     * @param item the Powerup carried by the NPC
     * @param room the room the NPC is in
     * @param text the text that the NPC says when the player encounters it
     * @param deathText the text shown when the NPC dies
     */
    public NPC(String name, double hp, PowerUp item, Room room, String text, String deathText){
        this.name = name;
        this.hp = hp;
        this.item = item;
        this.room = room;
        this.text = text;
        this.deathText = deathText;
    }

    /**
     * Returns true if NPC is hostile, false if NPC is friendly
     * @return true if NPC is hostile, false if NPC is friendly
     */
    public abstract boolean isHostile();

    /**
     * Returns a text that indicates the health of the NPC.
     * @return a text in the format: [name of NPC] has [hp] hp remaining!
     */
    public String getHealthText(){
        String[] fullName = name.split(": ");
        String firstName = fullName[0];
        return firstName + " has " + hp + " hp remaining!";
    }

    /**
     * Updates the health of the NPC after being attacked by player. Returns a text
     * with the amount of damage the player has done to the NPC.
     * @param player Player
     * @return Text indicating the damage the player has done to the NPC.
     */
    public abstract String fight(Player player);

    /**
     * Returns the text attribute of the NPC
     * @return description of the NPC
     */
    public String getText(){
        return this.text;
    }

    /**
     * Returns the deathText attribute of the NPC
     * @return text when the NPC dies
     */
    public String getDeathText(){
        return this.deathText;
    }

    /**
     * Returns the damage point of the NPC
     * @return the damage the NPC deals
     */
    public abstract double getDamage();

}

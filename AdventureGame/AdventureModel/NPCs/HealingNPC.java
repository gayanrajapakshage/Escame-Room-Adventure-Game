package AdventureModel.NPCs;

import AdventureModel.AdventureObject;
import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public abstract class HealingNPC extends friendlyNPC {
    /**
     * The amount the NPC can heal the player
     */
    double healAmount;

    /**
     *
     * @param name the name of the HealingNPC
     * @param hp the health points of the HealingNPC
     * @param item the Powerup carried by the HealingNPC
     * @param room the room the HealingNPC is in
     * @param text the text that the HealingNPC says when the player encounters it
     * @param deathText the text shown when the HealingNPC dies
     * @param healAmount healing amount
     */
    public HealingNPC(String name, double hp, PowerUp item, Room room, String text, String deathText, double healAmount){
        super(name, hp, item, room, text, deathText);
        this.healAmount = healAmount;
    }

    /**
     * Heals the player based on healAmount
     * @param player the Player
     */
    public abstract String healPlayer(Player player);





}


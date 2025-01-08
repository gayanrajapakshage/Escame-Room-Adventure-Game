package AdventureModel.NPCs;

import AdventureModel.AdventureObject;
import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public abstract class friendlyNPC extends NPC {
    /**
     *
     * @param name the name of the friendlyNPC
     * @param hp the health points of the friendlyNPC
     * @param item the Powerup carried by the friendlyNPC
     * @param room the room the friendlyNPC is in
     * @param text the text that the friendlyNPC says when the player encounters it
     * @param deathText the text shown when the friendlyNPC dies
     */
    public friendlyNPC(String name, double hp, PowerUp item, Room room, String text, String deathText){
        super(name, hp, item, room, text, deathText);
    }

    /**
     * Updates the health of the friendlyNPC to 0.0 after being attacked by player. Returns a text
     * with the amount of damage the player has done to the friendlyNPC.
     * @param player Player
     * @return Text indicating the damage the player has done to the friendlyNPC.
     */
    @Override
    public String fight(Player player) {
        String text = "You have dealt "+ hp + " damage.\n";
        this.hp -= this.hp;
        return text;
    }

    /**
     * Returns false since the friendlyNPC is friendly
     * @return false
     */
    @Override
    public boolean isHostile() {
        return false;
    }

    /**
     * Returns the damage point of the friendlyNPC, which is 0.0
     * @return 0.0
     */
    @Override
    public double getDamage() {
        return 0;
    }
}

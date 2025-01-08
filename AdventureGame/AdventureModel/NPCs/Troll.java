package AdventureModel.NPCs;

import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public class Troll extends hostileNPC{
    /**
     *
     * @param name the name of the Troll
     * @param hp the health points of the Troll
     * @param item the Powerup carried by the Troll
     * @param room the room the Troll is in
     * @param text the text that the Troll says when the player encounters it
     * @param deathText the text shown when the Troll dies
     * @param damage the damage points of the Troll
     */
    public Troll(String name, double hp, PowerUp item, Room room, String text, String deathText, double damage){
        super(name, hp, item, room, text, deathText, damage);
    }

    /**
     * Returns the damage the Troll deals
     * @return damage
     */
    @Override
    public double getDamage() {
        return damage;
    }
}

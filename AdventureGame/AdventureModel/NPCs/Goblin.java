package AdventureModel.NPCs;

import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public class Goblin extends hostileNPC{
    /**
     *
     * @param name the name of the Goblin
     * @param hp the health points of the Goblin
     * @param item the Powerup carried by the Goblin
     * @param room the room the Goblin is in
     * @param text the text that the Goblin says when the player encounters it
     * @param deathText the text shown when the Goblin dies
     * @param damage the damage points of the Goblin
     */
    public Goblin(String name, double hp, PowerUp item, Room room, String text, String deathText, double damage){
        super(name, hp, item, room, text, deathText, damage);
    }

    /**
     * Returns the damage the Goblin deals
     * @return damage * 1.25
     */
    @Override
    public double getDamage() {
        return damage*1.25;
    }
}

package AdventureModel.NPCs;

import AdventureModel.AdventureObject;
import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;
import AdventureModel.Strategies.FightWithoutPowerUp;
import AdventureModel.Strategies.FightWithSP;

public abstract class hostileNPC extends NPC {
    /**
     * The damage this hostile NPC can make
     */
    double damage;

    /**
     *
     * @param name the name of the hostileNPC
     * @param hp the health points of the hostileNPC
     * @param item the Powerup carried by the hostileNPC
     * @param room the room the hostileNPC is in
     * @param text the text that the hostileNPC says when the player encounters it
     * @param deathText the text shown when the hostileNPC dies
     * @param damage the damage points of the hostileNPC
     */
    public hostileNPC(String name, double hp, PowerUp item, Room room, String text, String deathText, double damage){
        super(name, hp, item, room, text, deathText);
        this.damage = damage;
    }


    /**
     * Returns true because hostileNPC is hostile
     * @return true
     */
    @Override
    public boolean isHostile() {
        return true;
    }

    /**
     * Reduce the health of the hostileNPC by hostileNPC.damage after being attacked by player.
     * Returns a text with the amount of damage the player has done to the NPC.
     * @param player Player
     * @return Text indicating the damage the player has done to the NPC.
     */
    @Override
    public String fight(Player player) {
        String text = "You have dealt "+ this.damage + " damage points.\n";
        if (player.fightStrategy instanceof FightWithoutPowerUp){
            this.hp -= Double.min(hp, damage);
        } else if (player.fightStrategy instanceof FightWithSP){
            this.hp -= Double.min(hp, this.damage*2);
            text += "Due to your strength potion, you dealt twice this damage!\n";
        }
        return text;
    }
}

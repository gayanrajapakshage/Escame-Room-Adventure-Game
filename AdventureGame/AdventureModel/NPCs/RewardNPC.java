package AdventureModel.NPCs;

import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public abstract class RewardNPC extends friendlyNPC{
    /**
     *
     * @param name the name of the RewardNPC
     * @param hp the health points of the RewardNPC
     * @param item the Powerup carried by the RewardNPC
     * @param room the room the RewardNPC is in
     * @param text the text that the RewardNPC says when the player encounters it
     * @param deathText the text shown when the RewardNPC dies
     */
    public RewardNPC(String name, double hp, PowerUp item, Room room, String text, String deathText){
        super(name, hp, item, room, text, deathText);
    }

    /**
     * Rewards player with a Powerup if they have sufficient reputation points.
     * @param player Player
     */
    public String reward(Player player){
        String name = this.name.split(":")[0];
        if (player.getRep()>=50 && item != null){
            item.activate(player);
            String text = item.getClass().getName().replace("AdventureModel.PowerUps.","") + ".";
            item = null;
            return name + " has rewarded you with " + text;
        }
        return "Your Reputation is too low, you will not receive an Powerup!";
    }
}

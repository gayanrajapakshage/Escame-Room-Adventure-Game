package AdventureModel.NPCs;

import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public class Saint extends HealingNPC{
    /**
     *
     * @param name the name of the Saint
     * @param hp the health points of the Saint
     * @param item the Powerup carried by the Saint
     * @param room the room the Saint is in
     * @param text the text that the Saint says when the player encounters it
     * @param deathText the text shown when the Saint dies
     * @param healAmount healing amount
     */
    public Saint(String name, double hp, PowerUp item, Room room, String text, String deathText, double healAmount){
        super(name, hp, item, room, text, deathText, healAmount);
    }

    /**
     * Heals the player with healAmount regardless of their reputation
     * Returns the text that indicates player's health after healing
     * @param player the Player
     */
    @Override
    public String healPlayer(Player player) {
        double healed;
        if (healAmount + player.getHealth() >= 100) {
            healed = 100 - player.getHealth();
        } else {
            healed = healAmount;
        }
        player.healPlayer(healed);
        return "Since I am SOOO nice!! I will heal you!\n" + "Your health is now "+ player.getHealth() + "HP.";
    }
}

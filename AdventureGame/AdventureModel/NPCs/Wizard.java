package AdventureModel.NPCs;

import AdventureModel.Player;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public class Wizard extends HealingNPC {
    /**
     *
     * @param name the name of the Wizard
     * @param hp the health points of the Wizard
     * @param item the Powerup carried by the Wizard
     * @param room the room the Wizard is in
     * @param text the text that the Wizard says when the player encounters it
     * @param deathText the text shown when the Wizard dies
     * @param healAmount healing amount
     */
    public Wizard(String name, double hp, PowerUp item, Room room, String text, String deathText, double healAmount){
        super(name, hp, item, room, text, deathText, healAmount);
    }

    /**
     * Heals the player with two times the healAmount if their reputation >= 70
     * Heals the player with healAmount if their reputation >= 50
     * Does not heal player if their reputation is below 50
     * The player can only be healed up to 100 hp
     * Returns the text that indicates player's health after healing
     * @param player the Player
     */
    @Override
    public String healPlayer(Player player) {
        double healed;
        if (player.getRep()>=70){
            if (healAmount*2 + player.getHealth() >= 100){
                healed = 100 - player.getHealth();
            } else {
                healed = healAmount * 2;
            }
            player.healPlayer(healed);
            return "Well done, you have maintained a good reputation!\n" + "You get double healing!\n"+ "Your health is now "+ player.getHealth() + " HP." ;
        } else if(player.getRep()>=50){
            if (healAmount + player.getHealth() >= 100){
                healed = 100 - player.getHealth();
            } else {
                healed = healAmount;
            }
            player.healPlayer(healed);
            return "You have barely enough reputation to be healed, bozo! I'll give you the standard healing!\n"+ "Your health is now "+ player.getHealth() + " HP.";
        } else {
            return "Your reputation is too low. BEGONE, BOZO!";
        }
    }
}

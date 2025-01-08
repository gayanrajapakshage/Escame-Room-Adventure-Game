package AdventureModel.PowerUps;

import AdventureModel.Player;
import AdventureModel.Strategies.FightWithSP;

public class StrengthPotion implements PowerUp{

    /**
     * The amount of coins required to purchase this potion.
     */
    private double cost = 15;

    /**
     * Indicates whether the PowerUp has been completely consumed.
     */
    private boolean used = false;

    /**
     * Gets the amount of coins required to purchase this potion.
     * @return Cost of the potion.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Consumes the potion to increase the damage done by the player's attack.
     * @param player The ongoing game's player
     */
    public void activate(Player player) {
        player.fightStrategy = new FightWithSP();
    }


    /**
     * Describes the PowerUp that has been activated.
     * @return Description of the potion's effect.
     */
    public String getText() {
        return "Paid " + this.cost + " coins.\n" + "You have consumed a Strength Potion. You will deal more damage to your opponents on your next attack!";
    }

    /**
     * Indicate whether the PowerUp can still be used.
     * @return True if the PowerUp has been completely consumed.
     */
    public boolean isUsed() {
        return this.used;
    }
}

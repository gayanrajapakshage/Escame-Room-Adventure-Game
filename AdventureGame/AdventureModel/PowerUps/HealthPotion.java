package AdventureModel.PowerUps;

import AdventureModel.Player;

public class HealthPotion implements PowerUp{

    /**
     * The amount of coins required to purchase this potion.
     */
    private double cost = 5;

    /**
     * The amount of health points this potion adds to the player's health.
     */
    private double hp = 10;


    /**
     * Gets the amount of coins required to purchase this potion.
     * @return Cost of the potion.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Consumes the potion to increase the player's health.
     * @param player The ongoing game's player
     */
    public void activate(Player player) {
        double withGain = Double.min((player.getHealth()+this.hp), 100);
        player.healPlayer(withGain - player.getHealth());
    }

    /**
     * Describes the PowerUp that has been consumed.
     * @return Description of the potion's effect.
     */
    public String getText() {
        return "Paid " + this.cost + " coins.\n" + "You have gained " + this.hp + " health points by consuming a Health Potion!";
    }
}

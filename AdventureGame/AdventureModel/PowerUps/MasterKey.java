package AdventureModel.PowerUps;

import AdventureModel.Player;
import AdventureModel.Strategies.MoveWithMK;

public class MasterKey implements PowerUp{

    /**
     * The amount of coins required to purchase this key.
     */
    private double cost = 20;

    /**
     * Indicates whether the PowerUp has been completely consumed.
     */
    private boolean used = false;


    /**
     * Gets the amount of coins required to purchase this key.
     * @return Cost of the key.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Activates the key to allow the player access to any blocked room for a single move.
     * @param player The ongoing game's player.
     */
    public void activate(Player player) {
        player.moveStrategy = new MoveWithMK();
    }

    /**
     * Describes the PowerUp that has been activated.
     * @return Description of the key's effect.
     */
    public String getText() {
        return "Paid " + this.cost + " coins.\n" + "You have purchased the Master Key. You can now enter any blocked room on your next move!";
    }


    /**
     * Indicate whether the PowerUp can still be used.
     * @return True if the PowerUp has been completely consumed.
     */
    public boolean isUsed() {
        return this.used;
    }
}

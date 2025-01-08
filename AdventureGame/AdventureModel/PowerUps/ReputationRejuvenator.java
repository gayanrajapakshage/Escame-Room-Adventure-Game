package AdventureModel.PowerUps;

import AdventureModel.Player;

public class ReputationRejuvenator implements PowerUp{

    /**
     * The amount of coins required to purchase this substance.
     */
    private double cost = 10;
    /**
     * The amount of reputation points this substance adds to the player's health.
     */
    private double rep = 5;

    /**
     * Gets the amount of coins required to purchase this substance.
     * @return Cost of the substance.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Consumes the substance to increase the player's reputation.
     * @param player The ongoing game's player.
     */
    public void activate(Player player) {
        double withGain = Double.min((player.getRep()+this.rep), 100);
        player.gainRep(withGain - player.getRep());
    }

    /**
     * Describes the PowerUp that has been consumed.
     * @return Description of the substance's effect.
     */
    public String getText() {
        return "Paid " + this.cost + " coins.\n" + "You have gained " + this.rep + " reputation points by consuming a Reputation Rejuvenator!";
    }
}

package AdventureModel.GameShop;

import AdventureModel.Player;
import AdventureModel.AdventureObject;
import AdventureModel.PowerUps.PowerUp;

/**
 * The GameShop class allows the player to purchase PowerUps, or sell objects from their inventory.
 */
public class GameShop {

    /**
     * The player of the game.
     */
    private Player player;

    /**
     * Used to handle creation of sold PowerUp objects.
     */
    private PowerUpFactory factory = new PowerUpFactory();

    /**
     * Text used to greet player at the shop.
     */
    private String welcomeText;


    /**
     * Allow shop to access the game's player.
     * @param player The player of the game.
     */
    public void addPlayer(Player player){
        this.player = player;
    }


    /**
     * Get details about selling or purchasing objects to the shop.
     * @return Instructions for the player on how to interact with the shop.
     */
    public String welcomePlayer(){
        return welcomeText;
    }


    /**
     * Buy an AdventureObject from the player, and pay them its worth.
     */
    public String buyObject(AdventureObject object){
        player.dropObject(object.getName());
        player.getCurrentRoom().removeGameObject(object);
        player.setBalance(object.getWorth());
        return object.getName() + " sold for " + object.getWorth() + " coins.";
    }


    /**
     * Sell the requested PowerUp to the player, immediately activating it.
     * @return The description of the purchase.
     */
    public String sellPowerUp(String type) {
        if (!this.factory.getPowerUpTypes().containsKey(type)) {
            return "That product was not found. Please type 'SHOP' to look at the list of available power-ups.";
        } else if (this.player.getCoins() >= this.factory.getPowerUpTypes().get(type)) {
            PowerUp bought = this.factory.createPowerUp(type);
            this.player.setBalance(-bought.getCost());
            bought.activate(this.player);
            return bought.getText();
        }
        return "Purchase unsuccessful.\nYou do not have enough coins to purchase this power-up!";
    }



    /**
     * Setter method for the GameShop's welcome text.
     * @param text Text to set.
     */
    public void setWelcomeText(String text){
        this.welcomeText = text;
    }
}

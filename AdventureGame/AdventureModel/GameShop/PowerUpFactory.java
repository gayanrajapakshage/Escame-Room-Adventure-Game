package AdventureModel.GameShop;

import AdventureModel.PowerUps.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the creation of PowerUp objects purchased in the GameShop.
 */
public class PowerUpFactory {

    /**
     * The list of available PowerUps in the shop, mapped to their respective prices.
     * */
    HashMap<String, Double> powerUpTypes = new HashMap<>() {{
        put("MK", 20.0);
        put("SP", 15.0);
        put("RR", 10.0);
        put("HP", 5.0);
    }};

    /**
     * Return an object of the requested PowerUp type.
     * @return PowerUp object of the requested type.
     */
    public PowerUp createPowerUp(String type){
        return switch (type) {
            case "MK" -> new MasterKey();
            case "SP" -> new StrengthPotion();
            case "RR" -> new ReputationRejuvenator();
            case "HP" -> new HealthPotion();
            default -> null;
        };
    }


    /**
     * Return a list of the types of PowerUp objects that can be produced along with their prices.
     * @return HashMap of names and prices of available power-up types.
     */
    public HashMap<String, Double> getPowerUpTypes(){
        return this.powerUpTypes;
    }
}

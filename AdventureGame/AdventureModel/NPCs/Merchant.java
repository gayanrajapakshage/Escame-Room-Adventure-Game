package AdventureModel.NPCs;

import AdventureModel.PowerUps.MasterKey;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;

public class Merchant extends RewardNPC{
    /**
     *
     * @param name Name of Merchant
     * @param hp health points of Merchant
     * @param item MasterKey of Merchant
     * @param room Room of Merchant
     * @param text Text said by Merchant
     * @param deathText Text if Merchant dies
     */
    public Merchant(String name, double hp, MasterKey item, Room room, String text, String deathText){
        super(name, hp, item, room, text, deathText);
    }

}

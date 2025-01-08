package AdventureModel.NPCs;

import AdventureModel.PowerUps.MasterKey;
import AdventureModel.PowerUps.StrengthPotion;
import AdventureModel.Room;

public class Blacksmith extends RewardNPC{
    /**
     *
     * @param name Name of Blacksmith
     * @param hp health points of Blacksmith
     * @param item Strength potion of Blacksmith
     * @param room Room of Blacksmith
     * @param text Text said by Blacksmith
     * @param deathText Text if Blacksmith dies
     */
    public Blacksmith(String name, double hp, StrengthPotion item, Room room, String text, String deathText){
        super(name, hp, item, room, text, deathText);
    }
}

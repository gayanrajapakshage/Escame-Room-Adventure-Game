package AdventureModel.Strategies;

import AdventureModel.NPCs.NPC;
import AdventureModel.Player;

public class FightWithoutPowerUp implements FightStrategy{

    /**
     * Reduce the player's health by the amount of damage dealt by the NPC.
     *
     * @param npc the NPC to fight
     * @return the amount of damage taken
     */
    public String fight(NPC npc, Player player) {
        player.healPlayer(-npc.getDamage());
        return npc.name.split(": ")[0] + " has dealt " + npc.getDamage() + " damage points!";
    }
}

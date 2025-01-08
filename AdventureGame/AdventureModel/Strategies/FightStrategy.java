package AdventureModel.Strategies;

import AdventureModel.NPCs.NPC;
import AdventureModel.Player;

public interface FightStrategy {
    public String fight(NPC npc, Player player);
}

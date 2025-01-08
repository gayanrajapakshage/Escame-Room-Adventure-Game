package AdventureModel.Strategies;

import AdventureModel.Player;
import AdventureModel.Room;

import java.util.HashMap;

public interface MoveStrategy {
    public boolean move(String direction, Player player, HashMap<Integer, Room> rooms);
}

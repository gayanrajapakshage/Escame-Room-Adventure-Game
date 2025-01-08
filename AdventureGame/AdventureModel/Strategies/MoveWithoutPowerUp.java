package AdventureModel.Strategies;

import AdventureModel.Passage;
import AdventureModel.PassageTable;
import AdventureModel.Player;
import AdventureModel.Room;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveWithoutPowerUp implements MoveStrategy{

    /**
     * Move the player in the given direction, if possible. Blocked passages can only be accessed if the player has the required object.
     * Return false if the player wins or dies as a result of the move.
     *
     * @param direction the move command
     * @param player the player of the game
     * @param rooms the rooms in the game
     * @return false, if move results in death or a win (and game is over).  Else, true.
     */
    public boolean move(String direction, Player player, HashMap<Integer, Room> rooms) {
        direction = direction.toUpperCase();
        PassageTable motionTable = player.getCurrentRoom().getMotionTable(); //where can we move?
        if (!motionTable.optionExists(direction)) return true; //no move

        ArrayList<Passage> possibilities = new ArrayList<>();
        for (Passage entry : motionTable.getDirection()) {
            if (entry.getDirection().equals(direction)) { //this is the right direction
                possibilities.add(entry); // are there possibilities?
            }
        }

        //try the blocked passages first
        Passage chosen = null;
        for (Passage entry : possibilities) {

            if (chosen == null && entry.getIsBlocked()) {
                if (player.getInventory().contains(entry.getKeyName())) {
                    chosen = entry; //we can make it through, given our stuff
                    break;
                }
            } else { chosen = entry; } //the passage is unlocked
        }

        if (chosen == null) return true; //doh, we just can't move.

        int roomNumber = chosen.getDestinationRoom();
        Room room = rooms.get(roomNumber);
        player.setCurrentRoom(room);

        return !player.getCurrentRoom().getMotionTable().getDirection().get(0).getDirection().equals("FORCED");
    }
}

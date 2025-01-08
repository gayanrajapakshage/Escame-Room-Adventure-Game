package AdventureModel.Settings.Commands;

import views.AdventureGameView;

/**
 * The VolumeCommand class implements the Command interface
 * to represent a command that changes the volume of the game.
 */
public class VolumeCommand implements Command {
    private AdventureGameView gameView; //The AdventureGameView class associated with the game.

    /**
     * Volume Command Constructor
     * __________________________
     * Initializes attribute
     *
     * @param gameView The AdventureGameView class associated with the game.
     */
    public VolumeCommand(AdventureGameView gameView){
        this.gameView = gameView;
    }

    /**
     * Executes the command by invoking the changeVolume method to change
     * the volume of the game within the AdventureGameView class.
     */
    public void execute() {
        gameView.changeVolume();
    }

}

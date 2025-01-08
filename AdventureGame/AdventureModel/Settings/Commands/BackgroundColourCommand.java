package AdventureModel.Settings.Commands;

import views.AdventureGameView;

/**
 * The BackgroundColourCommand class implements the Command interface
 * to represent a command that changes the background colour of the game.
 */
public class BackgroundColourCommand implements Command {
    private AdventureGameView gameView; //The AdventureGameView class associated with the game.

    /**
     * Background Colour Command Constructor
     * __________________________
     * Initializes attribute
     *
     * @param gameView The AdventureGameView class associated with the game.
     */
    public BackgroundColourCommand(AdventureGameView gameView){
        this.gameView = gameView;
    }

    /**
     * Executes the command by invoking the changeBackgroundColour method to change
     * the background colour of the game within the AdventureGameView class.
     */
    public void execute() {
        gameView.changeBackgroundColour();
    }

}

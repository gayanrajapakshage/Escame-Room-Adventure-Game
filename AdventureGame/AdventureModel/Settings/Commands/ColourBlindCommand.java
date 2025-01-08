package AdventureModel.Settings.Commands;

import views.AdventureGameView;

/**
 * The ColourBlindCommand class implements the Command interface
 * to represent a command that changes the colour-blind mode in the game.
 */
public class ColourBlindCommand implements Command {
    private AdventureGameView gameView; //The AdventureGameView class associated with the game.

    /**
     * Colour-Blind Command Constructor
     * __________________________
     * Initializes attribute
     *
     * @param gameView The AdventureGameView class associated with the game.
     */
    public ColourBlindCommand(AdventureGameView gameView){
        this.gameView = gameView;
    }

    /**
     * Executes the command by invoking the changeColourBlindMode method to change
     * the colour-blind mode within the AdventureGameView class.
     */
    @Override
    public void execute() {
        gameView.changeColourBlindMode();
    }

}
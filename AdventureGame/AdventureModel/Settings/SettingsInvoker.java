package AdventureModel.Settings;

import AdventureModel.Settings.Commands.Command;

/**
 * The SettingsInvoker class is responsible for initiating
 * command requests related to the settings of the game.
 */
public class SettingsInvoker{
    private Command command; //The current stored command to be executed.

    /**
     * Stores the command to be executed by the SettingsInvoker.
     *
     * @param command The command to be set and later executed.
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Executes the current command stored in the command attribute. If command is null, nothing happens.
     * The execution of the command is delegated to the corresponding command object.
     */
    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }


}
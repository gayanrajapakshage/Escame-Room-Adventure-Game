package AdventureModel.Settings.Commands;

/**
 * The Command interface represents a command that can be executed.
 */
public interface Command {
    /**
     * Executes the command.
     */
    public default void execute(){
    }
}
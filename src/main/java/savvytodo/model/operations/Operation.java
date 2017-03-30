package savvytodo.model.operations;

import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.TaskManager;

//author @@A0124863A
/**
 * @author A0124863A
 * Represents undo command with hidden internal logic and the ability to be executed.
 */
public abstract class Operation {
    protected TaskManager taskManager;

    /**
     * Executes the command
     */
    public abstract void execute() throws CommandException;

    /**
     * @return the reverse operation just performed
     */
    public abstract Operation reverse();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

}

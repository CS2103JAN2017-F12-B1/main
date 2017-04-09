package savvytodo.logic.commands;

import savvytodo.commons.core.Messages;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.Model;
import savvytodo.storage.Storage;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected Storage storage;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASK_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute() throws CommandException;

    //@@author A0140036X
    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setDependencies(Model model, Storage storage) {
        setData(model);
        setStorage(storage);
    }

    public void setData(Model model) {
        this.model = model;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}

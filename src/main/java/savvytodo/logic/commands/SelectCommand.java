package savvytodo.logic.commands;

import savvytodo.commons.core.EventsCenter;
import savvytodo.commons.core.Messages;
import savvytodo.commons.core.UnmodifiableObservableList;
import savvytodo.commons.events.ui.JumpToListRequestEvent;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.logic.parser.TaskIndex;
import savvytodo.model.task.ReadOnlyTask;

/**
 * Selects a task identified using it's last displayed index from the task manager.
 */
public class SelectCommand extends Command {

    public final TaskIndex targetIndex;

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected task: %1$s";

    public SelectCommand(TaskIndex targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList(targetIndex.getTaskType());

        if (lastShownList.size() < targetIndex.getIndex()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex.getTaskType(),
                targetIndex.getIndex() - 1));

        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex.getIndex()));

    }

}

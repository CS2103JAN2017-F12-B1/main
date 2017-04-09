package savvytodo.logic.commands;

import java.util.LinkedList;
import java.util.List;

import savvytodo.commons.core.Messages;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.logic.parser.TaskIndex;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Status;
import savvytodo.model.task.Task;
import savvytodo.model.task.TaskType;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;

//@@author A0140016B
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";
    public static final String COMMAND_FORMAT = "unmark INDEX [MORE_INDEX]";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the marked tasks identified by the index number used in the last task listing as done.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Unmarked Task: %1$s\n";
    public static final String MESSAGE_UNMARK_TASK_FAIL = "Task %1$s is already marked!\n";

    public final List<TaskIndex> targetIndices;

    private List<ReadOnlyTask> lastShownEventList;
    private List<ReadOnlyTask> lastShownFloatingList;
    private final LinkedList<Integer> targettedEventIndices;
    private final LinkedList<Integer> targettedTaskIndices;
    private final LinkedList<Task> eventsToUnmark;
    private final LinkedList<Task> tasksToUnmark;

    private final StringBuilder resultSb = new StringBuilder();

    public UnmarkCommand(List<TaskIndex> indicesList) {
        this.targetIndices = indicesList;
        this.targettedEventIndices = new LinkedList<Integer>();
        this.targettedTaskIndices = new LinkedList<Integer>();
        this.eventsToUnmark = new LinkedList<Task>();
        this.tasksToUnmark = new LinkedList<Task>();
    }

    @Override
    public CommandResult execute()  throws CommandException {
        for (TaskIndex targetIndex : targetIndices) {
            lastShownEventList = model.getFilteredTaskList(TaskType.EVENT);
            lastShownFloatingList = model.getFilteredTaskList(TaskType.FLOATING_DEADLINE);
            int filteredTaskListIndex = targetIndex.getIndex() - 1;
            if (targetIndex.getTaskType().equals(TaskType.EVENT)) {
                if (filteredTaskListIndex >= lastShownEventList.size() || filteredTaskListIndex < 0) {
                    return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
                }
                targettedEventIndices.add(targetIndex.getIndex());
                eventsToUnmark.add((Task) lastShownEventList.get(filteredTaskListIndex));
            } else {
                if (filteredTaskListIndex >= lastShownFloatingList.size() || filteredTaskListIndex < 0) {
                    return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
                }
                targettedTaskIndices.add(targetIndex.getIndex());
                tasksToUnmark.add((Task) lastShownFloatingList.get(filteredTaskListIndex));
            }
        }

        this.unmarkTasks();

        return new CommandResult(resultSb.toString());
    }

    /**
     * unmark tasks according to tasks or events
     */
    private void unmarkTasks() {
        int numOfSuccessfulUnmark = 0;
        numOfSuccessfulUnmark += unmarkTasks(tasksToUnmark, targettedTaskIndices);
        numOfSuccessfulUnmark += unmarkTasks(eventsToUnmark, targettedEventIndices);

        if (numOfSuccessfulUnmark > 0) {
            model.recordMark(numOfSuccessfulUnmark);
        }
    }

    /**
     * @return number of successful unmark count
     */
    private int unmarkTasks(LinkedList<Task> tasksToUnmark, LinkedList<Integer> targettedIndices) {
        int numOfSuccessfulUnmark = 0;
        try {
            for (Task taskToUnmark : tasksToUnmark) {
                if (!taskToUnmark.isCompleted().value) {
                    resultSb.append(String.format(MESSAGE_UNMARK_TASK_FAIL, targettedIndices.peekFirst()));
                } else {
                    numOfSuccessfulUnmark++;
                    taskToUnmark = new Task(taskToUnmark);
                    taskToUnmark.setStatus(new Status(false));
                    model.updateTask(targettedIndices.peekFirst() - 1, taskToUnmark);
                    resultSb.append(String.format(MESSAGE_UNMARK_TASK_SUCCESS, targettedIndices.peekFirst()));
                }
                targettedIndices.removeFirst();
            }
        } catch (DuplicateTaskException e) {
            //ignore for completed
        }
        return numOfSuccessfulUnmark;
    }

}

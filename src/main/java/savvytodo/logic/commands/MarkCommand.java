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
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";
    public static final String COMMAND_FORMAT = "mark INDEX [MORE_INDEX]";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the tasks identified by the index number used in the last task listing as done.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Marked Task: %1$s\n";
    public static final String MESSAGE_MARK_TASK_FAIL = "Task %1$s is already marked!\n";

    public final List<TaskIndex> targetIndices;

    private List<ReadOnlyTask> lastShownEventList;
    private List<ReadOnlyTask> lastShownFloatingList;
    private final LinkedList<Integer> targettedEventIndices;
    private final LinkedList<Integer> targettedTaskIndices;
    private final LinkedList<Task> eventsToMark;
    private final LinkedList<Task> tasksToMark;

    private final StringBuilder resultSb = new StringBuilder();

    public MarkCommand(List<TaskIndex> indiceslist) {
        this.targetIndices = indiceslist;
        this.targettedEventIndices = new LinkedList<Integer>();
        this.targettedTaskIndices = new LinkedList<Integer>();
        this.eventsToMark = new LinkedList<Task>();
        this.tasksToMark = new LinkedList<Task>();
    }

    @Override
    public CommandResult execute() throws CommandException {
        lastShownEventList = model.getFilteredTaskList(TaskType.EVENT);
        lastShownFloatingList = model.getFilteredTaskList(TaskType.FLOATING_DEADLINE);
        for (TaskIndex targetIndex : targetIndices) {
            int filteredTaskListIndex = targetIndex.getIndex() - 1;
            if (targetIndex.getTaskType().equals(TaskType.EVENT)) {
                if (filteredTaskListIndex >= lastShownEventList.size() || filteredTaskListIndex < 0) {
                    return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
                }
                targettedEventIndices.add(targetIndex.getIndex());
                eventsToMark.add((Task) lastShownEventList.get(filteredTaskListIndex));
            } else {
                if (filteredTaskListIndex >= lastShownFloatingList.size() || filteredTaskListIndex < 0) {
                    return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
                }
                targettedTaskIndices.add(targetIndex.getIndex());
                tasksToMark.add((Task) lastShownFloatingList.get(filteredTaskListIndex));
            }
        }

        this.markTasks();

        return new CommandResult(resultSb.toString());
    }

    /**
     * mark tasks according to tasks or events
     */
    private void markTasks() {
        int numOfSuccessfulMark = 0;
        System.out.println(targettedTaskIndices.size() + " " + targettedEventIndices.size());
        numOfSuccessfulMark += markTasks(tasksToMark, targettedTaskIndices);
        numOfSuccessfulMark += markTasks(eventsToMark, targettedEventIndices);
        System.out.println(numOfSuccessfulMark);
        if (numOfSuccessfulMark > 0) {
            model.recordMark(numOfSuccessfulMark);
        }
    }

    /**
     * @return number of successful mark count
     */
    private int markTasks(LinkedList<Task> tasksToMark, LinkedList<Integer> targettedIndices) {
        int numOfSuccessfulMark = 0;
        try {
            for (Task taskToMark : tasksToMark) {
                if (taskToMark.isCompleted().value) {
                    resultSb.append(String.format(MESSAGE_MARK_TASK_FAIL, targettedIndices.peekFirst()));
                } else {
                    numOfSuccessfulMark++;
                    taskToMark = new Task(taskToMark);
                    taskToMark.setStatus(new Status(true));
                    model.updateTask(targettedIndices.peekFirst() - 1, taskToMark);
                    resultSb.append(String.format(MESSAGE_MARK_TASK_SUCCESS, targettedIndices.peekFirst()));
                }
                targettedIndices.removeFirst();
            }
        } catch (DuplicateTaskException e) {
            //ignore for completed
            System.out.println(numOfSuccessfulMark);
        }
        return numOfSuccessfulMark;
    }

}

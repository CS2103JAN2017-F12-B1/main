package savvytodo.logic.commands;

import java.util.LinkedList;
import java.util.List;

import savvytodo.commons.core.Messages;
import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.commons.util.StringUtil;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.logic.parser.TaskIndex;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Recurrence;
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
    private String addMessage = StringUtil.EMPTY_STRING;
    private int numOfSuccessfulMark = 0;

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
        lastShownFloatingList = model.getFilteredTaskList(TaskType.FLOATING);
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
    private void markTasks() throws CommandException {
        int numOfSuccessfulMark = 0;
        numOfSuccessfulMark += markTasks(tasksToMark, targettedTaskIndices);
        numOfSuccessfulMark += markTasks(eventsToMark, targettedEventIndices);
        if (numOfSuccessfulMark > 0) {
            model.recordMark(numOfSuccessfulMark);
        }
    }

    /**
     * @return number of successful mark count
     */
    private int markTasks(LinkedList<Task> tasksToMark, LinkedList<Integer> targettedIndices) throws CommandException {
        try {
            for (Task taskToMark : tasksToMark) {
                if (taskToMark.isCompleted().value) {
                    resultSb.append(String.format(MESSAGE_MARK_TASK_FAIL, targettedIndices.peekFirst()));
                } else {
                    numOfSuccessfulMark++;
                    taskToMark = new Task(taskToMark);
                    taskToMark.setStatus(new Status(true));
                    Recurrence recur = taskToMark.getRecurrence();
                    taskToMark.setRecurrence(new Recurrence(Recurrence.DEFAULT_VALUES));
                    model.updateTask(targettedIndices.peekFirst() - 1, taskToMark);
                    taskToMark = addNewRecurTask(taskToMark, recur);
                    resultSb.append(String.format(MESSAGE_MARK_TASK_SUCCESS, targettedIndices.peekFirst()));
                }
                targettedIndices.removeFirst();
            }

            resultSb.append(addMessage);
        } catch (DuplicateTaskException e) {
            //ignore for completed
        } catch (IllegalValueException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        return numOfSuccessfulMark;
    }

    /**
     * add new Recurring Task
     */
    private Task addNewRecurTask(Task taskToMark, Recurrence recurrence) throws IllegalValueException {
        Task toAdd;
        int times = recurrence.occurences;

        if (recurrence.type != Recurrence.Type.None && (times >= 0)) {
            numOfSuccessfulMark++;
            String start = DateTimeUtil.getRecurDate(taskToMark.getDateTime().startValue, recurrence.type.name());
            String end = DateTimeUtil.getRecurDate(taskToMark.getDateTime().endValue, recurrence.type.name());

            toAdd = new Task(
                    taskToMark.getName(),
                    taskToMark.getPriority(),
                    taskToMark.getDescription(),
                    taskToMark.getLocation(),
                    taskToMark.getCategories(),
                    new DateTime(start, end),
                    new Recurrence(Recurrence.DEFAULT_VALUES));

            toAdd.setStatus(new Status());

            if (times == 1) {
                toAdd.setRecurrence(new Recurrence(Recurrence.DEFAULT_VALUES));
            } else if (times > 1) {
                String[] newRecurValues = { recurrence.type.name(), Integer.toString(times - 1) };
                toAdd.setRecurrence(new Recurrence(newRecurValues));
            } else {
                toAdd.setRecurrence(taskToMark.getRecurrence());
            }

            model.addTask(toAdd);

            String conflictingTaskList = model.getTaskConflictingDateTimeWarningMessage(toAdd.getDateTime());
            addMessage = String.format(messageSummary(conflictingTaskList), toAdd);
        }

        return taskToMark;
    }

    /**
     * Method for conflicting tasks
     * @param conflictingTaskList
     * @return messageSummary
     */
    private String messageSummary(String conflictingTaskList) {
        String summary = StringUtil.EMPTY_STRING;
        if (!conflictingTaskList.isEmpty()) {
            summary += StringUtil.SYSTEM_NEWLINE
                    + Messages.MESSAGE_CONFLICTING_TASKS_WARNING
                    + conflictingTaskList;
        }
        return summary;
    }
}

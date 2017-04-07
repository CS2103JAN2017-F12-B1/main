package savvytodo.logic.commands;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import savvytodo.commons.core.Messages;
import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.commons.util.StringUtil;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.category.Category;
import savvytodo.model.category.UniqueCategoryList;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.Description;
import savvytodo.model.task.Location;
import savvytodo.model.task.Name;
import savvytodo.model.task.Priority;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Status;
import savvytodo.model.task.Task;
import savvytodo.model.task.UniqueTaskList;

//@@author A0140016B
/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: NAME [dt/START_DATE = END_DATE] [l/LOCATION] [p/PRIORITY_LEVEL] "
            + "[r/RECURRING_TYPE NUMBER_OF_RECURRENCE] [c/CATEGORY] [d/DESCRIPTION]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "Project Meeting dt/05/10/2016 1400 = 05/10/2016 1800 r/weekly 2 c/CS2103 "
            + "d/Discuss about roles and milestones";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final ArrayList<Task> toAddList;

    String conflictingTaskList = StringUtil.EMPTY_STRING;

    /**
     * Creates an AddCommand using raw values.
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String priority, String description, String location, String[] dateTime,
            String[] recurrence, Set<String> categories) throws IllegalValueException {
        final Set<Category> categorySet = new HashSet<>();
        toAddList = new ArrayList<Task>();
        for (String categoryName : categories) {
            categorySet.add(new Category(categoryName));
        }

        this.addToTaskList(name, priority, description, location, dateTime, recurrence, categorySet);
    }

    /**
     * Creates list of toAdd tasks using raw values.
     * @throws IllegalValueException if any of the raw values are invalid
     */
    private void addToTaskList(String name, String priority, String description, String location, String[] dateTime,
            String[] recurrence, Set<Category> categorySet) throws IllegalValueException {
        Task toAdd;

        int numTasks = 1;
        if (recurrence != null
                && Integer.parseInt(recurrence[1]) > Integer.parseInt(Recurrence.DEFAULT_VALUES[1])) {
            numTasks += Integer.parseInt(recurrence[1]);
        }

        for (int i = 0; i < numTasks; i++) {
            if (i != 0) {
                modifyDateTime(dateTime, recurrence, 0);
                modifyDateTime(dateTime, recurrence, 1);
            }

            toAdd = new Task(
                    new Name(name),
                    new Priority(priority),
                    new Description(description),
                    new Location(location),
                    new UniqueCategoryList(categorySet),
                    new DateTime(dateTime),
                    new Recurrence(Recurrence.DEFAULT_VALUES)
            );

            toAdd.setStatus(new Status());
            toAdd.getDateTime().setAdd(LocalDateTime.now());

            toAddList.add(toAdd);
        }
    }

    /**
     * change recurring date Time;
     */
    private void modifyDateTime(String[] dateTime, String[] recurrence,
            int dateTimeIndex) {
        if (dateTime[dateTimeIndex] != null
                && dateTime[dateTimeIndex].length() > 0) {
            dateTime[dateTimeIndex] = DateTimeUtil.getRecurDate(dateTime[dateTimeIndex], recurrence[0]);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            for (Task toAdd : toAddList) {
                conflictingTaskList += model.getTaskConflictingDateTimeWarningMessage(toAdd.getDateTime());
                model.addTask(toAdd);
            }
            return new CommandResult(messageSummary());

        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        } catch (DateTimeException e) {
            throw new CommandException(DateTimeUtil.MESSAGE_INCORRECT_SYNTAX);
        } catch (IllegalValueException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }

    /**
     * Method for conflicting tasks
     * @param conflictingTaskList
     * @return messageSummary
     */
    private String messageSummary() {
        String summary = StringUtil.EMPTY_STRING;

        for (Task toAdd : toAddList) {
            summary += String.format(MESSAGE_SUCCESS, toAdd);
            if (toAddList.size() > 1) {
                summary += "\n";
            }
        }
        if (!conflictingTaskList.isEmpty()) {
            summary += StringUtil.SYSTEM_NEWLINE
                    + Messages.MESSAGE_CONFLICTING_TASKS_WARNING
                    + conflictingTaskList;
        }
        return summary;
    }
}

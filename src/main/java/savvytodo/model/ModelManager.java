package savvytodo.model;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import savvytodo.commons.core.ComponentManager;
import savvytodo.commons.core.LogsCenter;
import savvytodo.commons.core.UnmodifiableObservableList;
import savvytodo.commons.events.model.TaskManagerChangedEvent;
import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.commons.util.CollectionUtil;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.commons.util.StringUtil;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.operations.Operation;
import savvytodo.model.operations.RedoMarkOperation;
import savvytodo.model.operations.UndoAddOperation;
import savvytodo.model.operations.UndoClearOperation;
import savvytodo.model.operations.UndoDeleteOperation;
import savvytodo.model.operations.UndoEditOperation;
import savvytodo.model.operations.UndoMarkOperation;
import savvytodo.model.operations.UndoRedoOperationCentre;
import savvytodo.model.operations.exceptions.RedoFailureException;
import savvytodo.model.operations.exceptions.UndoFailureException;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Status;
import savvytodo.model.task.Task;
import savvytodo.model.task.TaskType;
import savvytodo.model.task.Type;
import savvytodo.model.task.UniqueTaskList;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;
import savvytodo.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the task manager data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private static final String TASK_CONFLICTED = "\nTask %1$s: %2$s";

    private final TaskManager taskManager;
    private final UndoRedoOperationCentre undoRedoOpCentre;

    //@@author A0147827U
    private final FilteredList<ReadOnlyTask> filteredFloatingTasks;
    private final FilteredList<ReadOnlyTask> filteredEventTasks;

    /**
     * Initializes a ModelManager with the given taskManager and userPrefs.
     */
    public ModelManager(ReadOnlyTaskManager taskManager, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(taskManager, userPrefs);

        logger.fine("Initializing with task manager: " + taskManager + " and user prefs " + userPrefs);

        this.taskManager = new TaskManager(taskManager);
        this.undoRedoOpCentre = new UndoRedoOperationCentre();
        filteredEventTasks = new FilteredList<>(this.taskManager.getTaskList());
        filteredEventTasks.setPredicate(Type.getEventType().getPredicate());
        filteredFloatingTasks = new FilteredList<>(this.taskManager.getTaskList());
        filteredFloatingTasks.setPredicate(Type.getFloatingType().getPredicate()
                .or(Type.getDeadlineType().getPredicate()));

    }
    //@@author

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    //@@author A0124863A
    @Override
    public void resetData(ReadOnlyTaskManager newData) {
        UndoClearOperation undoClear = new UndoClearOperation(taskManager, newData);
        undoRedoOpCentre.storeUndoOperation(undoClear);
        undoRedoOpCentre.resetRedo();

        taskManager.resetData(newData);

        indicateTaskManagerChanged();
    }
    //@@author

    @Override
    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskManagerChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }

    //@@author A0124863A
    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        try {
            taskManager.removeTask(target);
        } catch (DuplicateTaskException e) {
            e.printStackTrace();
        }

        UndoDeleteOperation undoDelete = new UndoDeleteOperation(target);
        undoRedoOpCentre.storeUndoOperation(undoDelete);
        undoRedoOpCentre.resetRedo();

        indicateTaskManagerChanged();
    }

    //@@author A0124863A
    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(task);
        UndoAddOperation undoAdd = new UndoAddOperation(task);
        undoRedoOpCentre.storeUndoOperation(undoAdd);
        undoRedoOpCentre.resetRedo();

        updateFilteredListToShowAll();
        indicateTaskManagerChanged();
    }

    //@@author A0124863A
    @Override
    public void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedTask != null;

        int taskManagerIndex = getFilteredTasks(editedTask.getType()).getSourceIndex(filteredTaskListIndex);
        Task originalTask = new Task(getFilteredTasks(editedTask.getType()).get(filteredTaskListIndex));
        UndoEditOperation undoEdit = new UndoEditOperation(filteredTaskListIndex, originalTask, editedTask);
        undoRedoOpCentre.storeUndoOperation(undoEdit);
        undoRedoOpCentre.resetRedo();

        taskManager.updateTask(taskManagerIndex, editedTask);
        indicateTaskManagerChanged();
    }

    //@@author A0124863A
    @Override
    public void recordMark(int numToUnmark) {
        UndoMarkOperation undoMark = new UndoMarkOperation(numToUnmark);
        undoRedoOpCentre.storeUndoOperation(undoMark);
    }


    //@@author A0124863A
    @Override
    public void undo() throws UndoFailureException {
        try {
            Operation undo = undoRedoOpCentre.getUndoOperation();
            if (undo.getClass().isAssignableFrom(UndoMarkOperation.class)) {
                UndoMarkOperation undoMark = (UndoMarkOperation) undo;
                undoMark.setTaskManager(taskManager);
                undoMark.setUndoRedoOperationCentre(undoRedoOpCentre);
                undoMark.execute();
                indicateTaskManagerChanged();

            } else {
                undo.setTaskManager(taskManager);
                undo.execute();
                indicateTaskManagerChanged();
            }
        } catch (EmptyStackException e) {
            throw new UndoFailureException(e.getMessage());
        } catch (CommandException e) {
            throw new UndoFailureException(e.getMessage());
        }
    }

    //@@author A0124863A
    @Override
    public void redo() throws RedoFailureException {
        try {
            Operation redo = undoRedoOpCentre.getRedoOperation();
            if (redo.getClass().isAssignableFrom(RedoMarkOperation.class)) {
                RedoMarkOperation redoMark = (RedoMarkOperation) redo;
                redoMark.setTaskManager(taskManager);
                redoMark.setUndoRedoOperationCentre(undoRedoOpCentre);
                redoMark.execute();
                indicateTaskManagerChanged();

            } else {
                redo.setTaskManager(taskManager);
                redo.execute();
                indicateTaskManagerChanged();
            }
        } catch (EmptyStackException e) {
            throw new RedoFailureException(e.getMessage());
        } catch (CommandException e) {
            throw new RedoFailureException(e.getMessage());
        }
    }

    //@@author A0140016B
    /**
     * @author A0140016B
     * Returns a string of conflicting datetimes within a specified datetime
     * @throws IllegalValueException
     * @throws DateTimeException
     */
    public String getTaskConflictingDateTimeWarningMessage(DateTime dateTimeToCheck)
            throws DateTimeException, IllegalValueException {
        StringBuilder conflictingTasksStringBuilder = new StringBuilder(StringUtil.EMPTY_STRING);

        if (dateTimeToCheck.endValue == StringUtil.EMPTY_STRING) {
            return StringUtil.EMPTY_STRING;
        }

        appendConflictingTasks(conflictingTasksStringBuilder, dateTimeToCheck);

        return conflictingTasksStringBuilder.toString();
    }

    //@@author A0140016B
    /**
     * @param conflictingTasksStringBuilder to generate conflicting tasks in String
     * @return number of task conflicted
     */
    private int appendConflictingTasks(
            StringBuilder conflictingTasksStringBuilder,
            DateTime dateTimeToCheck) throws DateTimeException, IllegalValueException {
        int conflictCount = 0;
        int conflictPosition = 1;
        for (ReadOnlyTask task : taskManager.getTaskList()) {
            if (task.isCompleted().value == Status.ONGOING
                    && DateTimeUtil.isDateTimeConflict(task.getDateTime(), dateTimeToCheck)) {
                conflictingTasksStringBuilder
                        .append(String.format(TASK_CONFLICTED, conflictPosition, task.getAsText()));
                conflictCount++;
            }
            conflictPosition++;
        }
        return conflictCount;
    }

    //@@author A0147827U
    /**
     * Returns the corresponding filter list view based on Task type
     * @param type
     * @return FilterList with predicate for the type already set.
     */
    private FilteredList<ReadOnlyTask> getFilteredTasks(Type type) {
        switch (type.getType()) {
        case FLOATING:
            return filteredFloatingTasks;
        case EVENT:
        default:
            return filteredEventTasks;
        }
    }

    //=========== Filtered Task List Accessors =============================================================
    //@@author A0140016B
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return taskManager.getTaskList();
    }
    //@@author
    //@@author A0147827U
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList(TaskType taskType) {
        switch(taskType) {
        case FLOATING:
            return new UnmodifiableObservableList<>(getFilteredFloatingTaskList());
        case EVENT:
        default:
            return new UnmodifiableObservableList<>(getFilteredEventTaskList());
        }

    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredEventTaskList() {
        return filteredEventTasks;
    }


    @Override
    public ObservableList<ReadOnlyTask> getFilteredFloatingTaskList() {
        return filteredFloatingTasks;
    }

    /**
     *  Reset the filters for all the lists. Adds the default filter (based on task type)
     */
    @Override
    public void updateFilteredListToShowAll() {
        filteredFloatingTasks.setPredicate(Type.getFloatingType().getPredicate()
                .or(Type.getDeadlineType().getPredicate()));
        filteredEventTasks.setPredicate(Type.getEventType().getPredicate());

    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    public void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate) {
        filteredFloatingTasks.setPredicate(predicate.and(Type.getFloatingType().getPredicate()
                .or(Type.getDeadlineType().getPredicate())));
        filteredEventTasks.setPredicate(predicate.and(Type.getEventType().getPredicate()));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredFloatingTasks.setPredicate(Type.getFloatingType().getPredicate()
                .or(Type.getDeadlineType().getPredicate()).and(expression::satisfies));
        filteredEventTasks.setPredicate(Type.getEventType().getPredicate().and(expression::satisfies));
    }
    //@@author
    //@@author A0140016B
    public void updateFilteredTaskListByDateTime(DateTime dateTime) {
        updateFilteredTaskList(new PredicateExpression(new DateTimeQualifier(dateTime)));
    }
    //@@author

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);

        @Override
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);

        @Override
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getName().name, keyword)).findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    @Override
    public int getTotalFilteredListSize() {
        return getFilteredEventTaskList().size() + getFilteredFloatingTaskList().size();
    }

    //@@author A0140016B
    /**
     * Qualifier to sort by DateTime specified
     * Task type must be event
     */
    private class DateTimeQualifier implements Qualifier {

        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private DateTime dateTimeQuery;

        public DateTimeQualifier (DateTime dateTime) {
            if (dateTime.getStartDate() != null) {
                startDateTime = DateTimeUtil.setLocalTime(dateTime.getStartDate(),
                        DateTimeUtil.FIRST_HOUR_OF_DAY,
                        DateTimeUtil.FIRST_MINUTE_OF_DAY,
                        DateTimeUtil.FIRST_SECOND_OF_DAY);
                endDateTime = DateTimeUtil.setLocalTime(dateTime.getEndDate(),
                        DateTimeUtil.LAST_HOUR_OF_DAY,
                        DateTimeUtil.LAST_MINUTE_OF_DAY,
                        DateTimeUtil.LAST_SECOND_OF_DAY);
            } else {
                startDateTime = DateTimeUtil.setLocalTime(dateTime.getEndDate(),
                        DateTimeUtil.FIRST_HOUR_OF_DAY,
                        DateTimeUtil.FIRST_MINUTE_OF_DAY,
                        DateTimeUtil.FIRST_SECOND_OF_DAY);
                endDateTime = DateTimeUtil.setLocalTime(dateTime.getEndDate(),
                        DateTimeUtil.LAST_HOUR_OF_DAY,
                        DateTimeUtil.LAST_MINUTE_OF_DAY,
                        DateTimeUtil.LAST_SECOND_OF_DAY);
            }

            try {
                dateTimeQuery = new DateTime();
                dateTimeQuery.setStart(startDateTime);
                dateTimeQuery.setEnd(endDateTime);
            } catch (IllegalValueException e) {
                dateTimeQuery = new DateTime(LocalDateTime.now(), LocalDateTime.now());
                e.printStackTrace();
            }
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            try {
                return DateTimeUtil.isDateTimeWithinRange(task.getDateTime(), dateTimeQuery);
            } catch (DateTimeException e) {
                e.printStackTrace();
            } catch (IllegalValueException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}

package savvytodo.testutil;

import savvytodo.model.category.UniqueCategoryList;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.Description;
import savvytodo.model.task.Location;
import savvytodo.model.task.Name;
import savvytodo.model.task.Priority;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Status;
import savvytodo.model.task.TaskType;
import savvytodo.model.task.TimeStamp;
import savvytodo.model.task.Type;

//@@author A0140016B
/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private Name name;
    private Description description;
    private Location location;
    private Priority priority;
    private DateTime dateTime;
    private Recurrence recurrence;
    private UniqueCategoryList categories;
    private Status isCompleted;
    private TimeStamp timeStamp;
    private Type type;

    public TestTask() {
        categories = new UniqueCategoryList();
    }

    //@@author A0140036X
    /**
     * Creates a TestTask from ReadOnlyTask
     */
    public TestTask(ReadOnlyTask taskToCopy) {
        copyFromReadOnlyTask(taskToCopy);
    }

    //@@author A0140036X
    /**
     * Creates a copy of {@code taskToCopy}.
     */
    public TestTask(TestTask taskToCopy) {
        copyFromReadOnlyTask(taskToCopy);
    }

    //@@author A0140036X
    private void copyFromReadOnlyTask(ReadOnlyTask taskToCopy) {
        this.name = taskToCopy.getName();
        this.priority = taskToCopy.getPriority();
        this.dateTime = taskToCopy.getDateTime();
        this.description = taskToCopy.getDescription();
        this.location = taskToCopy.getLocation();
        this.categories = taskToCopy.getCategories();
        this.isCompleted = taskToCopy.isCompleted();
        this.timeStamp = taskToCopy.getTimeStamp();
        this.type = taskToCopy.getType();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setCategories(UniqueCategoryList categories) {
        this.categories = categories;
    }

    public void setTimeStamp() {
        this.timeStamp = new TimeStamp();
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setDateTime(DateTime dateTime) {
        assert dateTime != null;
        this.dateTime = dateTime;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setRecurrence(Recurrence recurrence) {
        assert recurrence != null;
        this.recurrence = recurrence;
    }

    @Override
    public Recurrence getRecurrence() {
        return recurrence;
    }

    @Override
    public Status isCompleted() {
        return isCompleted;
    }

    public void setCompleted(Status isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public UniqueCategoryList getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName().name + " ");
        sb.append("l/" + this.getLocation().value + " ");
        sb.append("p/" + this.getPriority().value + " ");
        //@@author A0147827U
        if (!isFloating()) {
            sb.append("dt/" + this.getDateTime().startValue + DateTime.DATETIME_STRING_TO_CONNECTOR
                    + this.getDateTime().endValue + " ");
        }
        //@@author
        sb.append("d/" + this.getDescription().value + " ");
        this.getCategories().asObservableList().stream()
                .forEach(s -> sb.append("c/" + s.categoryName + " "));
        return sb.toString();
    }
    //@@author A0147827U
    private boolean isFloating() {
        return getDateTime().getStartDate() == null && getDateTime().getEndDate() == null;
    }

    private boolean isEvent() {
        return !(getDateTime().getStartDate() == null && getDateTime().getEndDate() == null);
    }

    private boolean isDeadline() {
        return getDateTime().getStartDate() == null && !(getDateTime().getEndDate() == null);
    }

    @Override
    public Type getType() {
        updateType();
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void updateType() {
        if (isEvent()) {
            type.setType(TaskType.EVENT);
        } else if (isFloating()) {
            type.setType(TaskType.FLOATING);
        } else if (isDeadline()) {
            type.setType(TaskType.DEADLINE);
        }
    }
  //@@author


    //@@author A0140036X
    /**
     * Converts a list of ReadOnlyTask to list of TestTask
     */
    public static TestTask[] listFromReadOnlyTask(ReadOnlyTask[] tasks) {
        TestTask[] testTasks = new TestTask[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            testTasks[i] = new TestTask(tasks[i]);
        }
        return testTasks;
    }

}

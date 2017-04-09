package savvytodo.model.task;

import java.util.Arrays;
import java.util.Objects;

import savvytodo.commons.util.CollectionUtil;
import savvytodo.model.category.UniqueCategoryList;
import savvytodo.testutil.TestUtil;

//@@author A0140016B
/**
 * @author A0140016B
 * Represents a Task in the task manager.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask, Comparable<Task> {

    private Name name;
    private Priority priority;
    private Description description;
    private Location location;
    private DateTime dateTime;
    private Recurrence recurrence;
    private Status isCompleted;

    private UniqueCategoryList categories;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Priority priority, Description description, Location location, UniqueCategoryList categories,
            DateTime dateTime, Recurrence recurrence) {
        assert !CollectionUtil.isAnyNull(name, priority, description, location, categories, dateTime, recurrence);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.location = location;
        this.categories = new UniqueCategoryList(categories); //protect internal categories from changes in the arg list
        this.dateTime = dateTime;
        this.recurrence = recurrence;
        this.isCompleted = new Status();
    }

    public Task(Name name, Priority priority, Description description, Location location, UniqueCategoryList categories,
            DateTime dateTime, Recurrence recurrence, Status status) {
        assert !CollectionUtil.isAnyNull(name, priority, description, location, categories, dateTime, recurrence,
                status);
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.location = location;
        this.categories = new UniqueCategoryList(categories); //protect internal categories from changes in the arg list
        this.dateTime = dateTime;
        this.recurrence = recurrence;
        this.isCompleted = status;
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getPriority(), source.getDescription(), source.getLocation(),
                source.getCategories(), source.getDateTime(), source.getRecurrence(), source.isCompleted());
    }

    public void setName(Name name) {
        assert name != null;
        this.name = name;
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setPriority(Priority priority) {
        assert priority != null;
        this.priority = priority;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setDescription(Description description) {
        assert description != null;
        this.description = description;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    public void setLocation(Location location) {
        assert location != null;
        this.location = location;
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
        assert isCompleted != null;
        return isCompleted;
    }

    public void setStatus(Status isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public UniqueCategoryList getCategories() {
        return new UniqueCategoryList(categories);
    }

    /**
     * Replaces this task's categories with the categories in the argument category list.
     */
    public void setCategories(UniqueCategoryList replacement) {
        categories.setCategories(replacement);
    }

    /**
     * Updates this task with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        assert replacement != null;

        this.setName(replacement.getName());
        this.setPriority(replacement.getPriority());
        this.setDescription(replacement.getDescription());
        this.setLocation(replacement.getLocation());
        this.setCategories(replacement.getCategories());
        this.setDateTime(replacement.getDateTime());
        this.setRecurrence(replacement.getRecurrence());
        this.setStatus(replacement.isCompleted());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                        && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(attributes());
    }

    @Override
    public String toString() {
        return getAsText();
    }

    //@@author A0140036X
    /**
     * Array of string representation of all the attributes of a Task.
     */
    @SuppressWarnings("rawtypes")
    private Comparable[] attributes() {
        return new Comparable[] { name, priority, description, location, categories, dateTime, recurrence,
                isCompleted };
    }

    //@@author A0140036X
    /**
     * Generic comparison to another task using attributes of a Task.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public int compareTo(Task o) {
        Comparable[] thisAttributes = attributes();
        Comparable[] thatAttributes = o.attributes();
        int compareVal;
        for (int i = 0; i < thisAttributes.length; i++) {
            compareVal = thisAttributes[i].compareTo(thatAttributes[i]);
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }

    //@@author A0140036X
    /**
     * Checks if two lists of tasks are the same.
     */
    public static boolean areTasksSame(Task[] t1, Task[] t2) {
        if (t1.length != t2.length) {
            return false;
        }
        Arrays.sort(t1);
        Arrays.sort(t2);

        for (int i = 0; i < t1.length; i++) {
            if (!t1[i].equals(t2[i])) {
                System.out.println(t1[i].hashCode());
                System.out.println("false");

                TestUtil.printTasks(new Task[] { t1[i], t2[i] });
                return false;
            }
        }
        return true;
    }
}

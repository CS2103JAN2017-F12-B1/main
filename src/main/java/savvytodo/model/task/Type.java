package savvytodo.model.task;

import java.util.function.Predicate;
//@@author A0147827U
/**
 * Represents a Task's type in the task manager
 * @author jingloon
 */
public class Type {

    private TaskType type;

    public Type(TaskType type) {
        this.type = type;
    }

    public Type(DateTime dateTime) {
        if (dateTime.getStartDate() == null && dateTime.getEndDate() == null) {
            type = TaskType.FLOATING;
        } else if (dateTime.getStartDate() != null && dateTime.getEndDate() != null) {
            type = TaskType.EVENT;
        } else {
            type = TaskType.DEADLINE;
        }
    }
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * Return predicate of the priority to filter tasks
     * @return predicate expression to help filter tasks
     */
    public Predicate<ReadOnlyTask> getPredicate() {
        return (ReadOnlyTask task) -> task.getType().equals(this);
    }

    /**
     * Static methods to return fixed Types
     */
    public static Type getFloatingType() {
        return new Type (TaskType.FLOATING);
    }
    public static Type getEventType() {
        return new Type (TaskType.EVENT);
    }
    public static Type getDeadlineType() {
        return new Type (TaskType.DEADLINE);
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Type // instanceof handles nulls
                        && this.type.equals(((Type) other).type)); // state check
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

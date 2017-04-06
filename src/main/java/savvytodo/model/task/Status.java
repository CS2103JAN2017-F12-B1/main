package savvytodo.model.task;

import java.util.function.Predicate;

import savvytodo.commons.exceptions.IllegalValueException;

/**
 * @author A0140016B
 *
 * Represents Task's Status of Completion in the task manager
 * Guarantees: immutable; Defaults to false
 */
public class Status {

    public final boolean value;

    public static final String MESSAGE_STATUS_CONSTRAINTS = "Task status should be 'Completed' or 'Ongoing'";

    public static final boolean COMPLETED = true;
    public static final boolean ONGOING = false;
    private static final String MESSAGE_STATUS_COMPLETED = "Completed";
    private static final String MESSAGE_STATUS_ONGOING = "Ongoing";

    /**
     * Defaults to Not Completed
     */
    public Status() {
        value = ONGOING;
    }

    /**
     * Changing the status
     */
    public Status(boolean newStatus) {
        this.value = newStatus;
    }

    @Override
    public String toString() {
        if (value) {
            return MESSAGE_STATUS_COMPLETED;
        } else {
            return MESSAGE_STATUS_ONGOING;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Status
                        && this.toString().equals(((Status) other).toString()));
    }


    //@@author A0124863A
    public Status(String status) throws IllegalValueException {

        if (status.equalsIgnoreCase(MESSAGE_STATUS_COMPLETED)) {
            value = COMPLETED;
        } else if (status.equalsIgnoreCase(MESSAGE_STATUS_ONGOING)) {
            value = ONGOING;
        } else {
            throw new IllegalValueException(MESSAGE_STATUS_CONSTRAINTS);

        }

    }

    //@@author A0124863A
    public Predicate<ReadOnlyTask> getPredicate() {
        return (ReadOnlyTask task) -> task.isCompleted().equals(this);
    }
}

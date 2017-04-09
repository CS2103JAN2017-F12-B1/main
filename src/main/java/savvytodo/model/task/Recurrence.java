package savvytodo.model.task;

import savvytodo.commons.exceptions.IllegalValueException;

//@@author A0140016B
/**
 * @author A0140016B
 *
 * Represents Task's Recurrence in the task manager Guarantees: immutable;
 * is valid as declared in {@link #isValidRecurrence(String, int)} *
 */
public class Recurrence implements Comparable<Recurrence> {

    /**
     * @author A0140016B
     *
     * Specifies the type of recurrence for the task
     * Defaults to none if it is a one-time task
     */
    public enum Type {
        None, Daily, Weekly, Monthly, Yearly;

        /**
         * Get type enum object from it's name, ignoring cases
         * @param String recurrence type
         * @return Corresponding enum object
         * @throws IllegalArgumentException if invalid input
         */
        public static Type valueOfIgnoreCase(String reType) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(reType)
                        || type.toString().substring(0, 1).equalsIgnoreCase(reType)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(MESSAGE_RECURR_NOT_MATCH);
        }

        /**
         * Compare enum object with String to see if it matches whole string or first char
         * @param String retype cannot be null
         * @return boolean of whether string matches enum object
         */
        public static boolean matches(String reType) {
            for (Type type : Type.values()) {
                if (type.toString().equalsIgnoreCase(reType)
                        || type.toString().substring(0, 1).equalsIgnoreCase(reType)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Compare enum object with String to see if it matches
         * @param String retype cannot be null
         * @return boolean of wheather string matches enum object
         */
        public static boolean isNone(String reType) {
            if (Type.None.toString().equalsIgnoreCase(reType)
                    || Type.None.toString().substring(0, 1).equalsIgnoreCase(reType)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public int occurences;
    public Type type;

    public static final String MESSAGE_RECURR_CONSTRAINTS = "If recurrence type is NONE, occurences can only be 0";
    public static final String MESSAGE_RECURR_NOT_MATCH = "Task recurrence type should be "
            + "'none', 'daily', 'weekly', 'monthly' or 'yearly'";
    public static final String[] DEFAULT_VALUES = { Type.None.toString(), "0" };

    /**
     * Default constructor
     * @throws IllegalValueException
     * @throws NumberFormatException
     */
    public Recurrence() throws NumberFormatException, IllegalValueException {
        this(DEFAULT_VALUES);
    }

    /**
     * Validates given Recurrence.
     * @throws IllegalValueException if given Recurrence is invalid.
     */
    public Recurrence(String type, int occurences) throws IllegalValueException {
        assert type != null;
        String trimmedType = type.trim();
        if (!Type.matches(type)) {
            throw new IllegalValueException(MESSAGE_RECURR_NOT_MATCH);
        }
        if (!isValidRecurrence(trimmedType, occurences)) {
            throw new IllegalValueException(MESSAGE_RECURR_CONSTRAINTS);
        }
        this.type = Type.valueOfIgnoreCase(trimmedType);
        this.occurences = occurences;
    }

    /**
     * Constructor when given input as String array
     * @param recurrence string array
     * @throws IllegalValueException
     * @throws NumberFormatException
     */
    public Recurrence(String[] recurrence) throws NumberFormatException, IllegalValueException {
        this(recurrence[0], Integer.parseInt(recurrence[1]));
    }

    /**
     * Returns true if a given string is a valid task Recurrence and num is more than -1.
     */
    public static boolean isValidRecurrence(String type, int numOfTimes) {
        if (numOfTimes >= 0 && Type.matches(type)) {
            if (Type.isNone(type) && numOfTimes != 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.type.toString() + "(" + this.occurences + ")";
    }

    //@@author A0140036X
    @Override
    public int compareTo(Recurrence o) {
        return toString().compareTo(o.toString());
    }

}

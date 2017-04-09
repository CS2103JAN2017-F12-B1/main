package savvytodo.model.task;

import java.time.LocalDateTime;

//@@author A0124863A
/**
 * Represents a time stamp of when the task is added
 */
public class TimeStamp {
    private LocalDateTime dateTimeAdded;
    public static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.of(2017, 04, 15, 11, 30);

    public TimeStamp() {
        dateTimeAdded = LocalDateTime.now();
    }

    public TimeStamp(TimeStamp timeStamp) {
        dateTimeAdded = timeStamp.getDateTimeAdded();
    }

    public TimeStamp(LocalDateTime dateTime) {
        dateTimeAdded = dateTime;
    }

    public LocalDateTime getDateTimeAdded() {
        return dateTimeAdded;
    }

    @Override
    public String toString() {
        return dateTimeAdded.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof TimeStamp)) {
            return false;
        }

        TimeStamp otherTimeStamp = (TimeStamp) other;
        if (!dateTimeAdded.equals(otherTimeStamp.getDateTimeAdded())) {
            return false;
        }

        return true;

    }
}
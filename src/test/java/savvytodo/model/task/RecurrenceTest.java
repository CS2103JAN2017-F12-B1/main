package savvytodo.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

//@@author A0140016B
public class RecurrenceTest {
    @Test
    public void isValidRecurrence() {
        // valid priority
        assertTrue(Recurrence.isValidRecurrence("n", 0));
        assertTrue(Recurrence.isValidRecurrence("d", 1));
        assertTrue(Recurrence.isValidRecurrence("w", 3));
        assertTrue(Recurrence.isValidRecurrence("m", 5));
        assertTrue(Recurrence.isValidRecurrence("y", 7));
        assertTrue(Recurrence.isValidRecurrence("none", 0));
        assertTrue(Recurrence.isValidRecurrence("daily", 2));
        assertTrue(Recurrence.isValidRecurrence("weekly", 4));
        assertTrue(Recurrence.isValidRecurrence("monthly", 6));
        assertTrue(Recurrence.isValidRecurrence("yearly", 8));
    }

    @Test
    public void isInvalidRecurrence() {
        assertFalse(Recurrence.isValidRecurrence("n", 1));
        // invalid priority
        assertFalse(Recurrence.isValidRecurrence("", 0)); // empty string
        assertFalse(Recurrence.isValidRecurrence(" ", 0)); // spaces only
        assertFalse(Recurrence.isValidRecurrence("-1", 0));
        assertFalse(Recurrence.isValidRecurrence("123", 0));
        assertFalse(Recurrence.isValidRecurrence("x", 0)); //random alphabet
        assertFalse(Recurrence.isValidRecurrence("recurrence", 0)); // non-numeric
        assertFalse(Recurrence.isValidRecurrence("9011p041", 0)); // alphabets within digits
        assertFalse(Recurrence.isValidRecurrence("9312 1534", 0)); // spaces within digits
    }
}

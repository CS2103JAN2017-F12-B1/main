package savvytodo.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

//@@author A0140016B
public class PriorityTest {

    @Test
    public void isValidPriority() {
        // valid priority
        assertTrue(Priority.isValidPriority("low"));
        assertTrue(Priority.isValidPriority("medium"));
        assertTrue(Priority.isValidPriority("high"));
        assertTrue(Priority.isValidPriority("1"));
        assertTrue(Priority.isValidPriority("2"));
        assertTrue(Priority.isValidPriority("3"));
        assertTrue(Priority.isValidPriority("l"));
        assertTrue(Priority.isValidPriority("m"));
        assertTrue(Priority.isValidPriority("h"));
    }

    @Test
    public void isInvalidPriority() {
        // invalid priority
        assertFalse(Priority.isValidPriority("")); // empty string
        assertFalse(Priority.isValidPriority(" ")); // spaces only
        assertFalse(Priority.isValidPriority("0"));
        assertFalse(Priority.isValidPriority("4"));
        assertFalse(Priority.isValidPriority("priority")); // non-numeric
        assertFalse(Priority.isValidPriority("9011p041")); // alphabets within digits
        assertFalse(Priority.isValidPriority("9312 1534")); // spaces within digits
    }
}

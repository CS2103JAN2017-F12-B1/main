package savvytodo.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import savvytodo.commons.exceptions.IllegalValueException;
//@@author A0147827U
public class TypeTest {

    @Test
    public void isValidType() throws IllegalValueException {
            Type floating = new Type(new DateTime("", ""));
            Type event = new Type(new DateTime("10/03/2017 1000", "10/03/2017 1600"));
            Type deadline = new Type(new DateTime("", "10/03/2017 1600"));
            
            assertEquals(floating, Type.getFloatingType());
            assertEquals(deadline, Type.getDeadlineType());
            assertEquals(event, Type.getEventType());

            //types to be different
            assertFalse(floating.equals(event));
            assertFalse(floating.equals(deadline));
            assertFalse(event.equals(deadline));
    }
    

}

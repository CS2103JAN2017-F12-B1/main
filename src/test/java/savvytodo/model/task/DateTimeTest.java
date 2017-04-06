package savvytodo.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import savvytodo.commons.util.DateTimeUtil;

//@@author A0140016B
public class DateTimeTest {
    @Test
    public void isValidEvent() {
        LocalDateTime startDate = DateTimeUtil.setLocalTime(LocalDateTime.now(), DateTimeUtil.FIRST_HOUR_OF_DAY,
                DateTimeUtil.FIRST_MINUTE_OF_DAY, DateTimeUtil.FIRST_SECOND_OF_DAY);
        LocalDateTime endDate = DateTimeUtil.setLocalTime(LocalDateTime.now(), DateTimeUtil.LAST_HOUR_OF_DAY,
                DateTimeUtil.LAST_MINUTE_OF_DAY, DateTimeUtil.LAST_SECOND_OF_DAY);

        assertTrue(DateTime.isValidEvent(startDate, endDate));
    }

    @Test
    public void isInvalidEvent() {
        LocalDateTime startDate = DateTimeUtil.setLocalTime(LocalDateTime.now(), DateTimeUtil.FIRST_HOUR_OF_DAY,
                DateTimeUtil.FIRST_MINUTE_OF_DAY, DateTimeUtil.FIRST_SECOND_OF_DAY);
        LocalDateTime endDate = DateTimeUtil.setLocalTime(LocalDateTime.now(), DateTimeUtil.LAST_HOUR_OF_DAY,
                DateTimeUtil.LAST_MINUTE_OF_DAY, DateTimeUtil.LAST_SECOND_OF_DAY);

        assertFalse(DateTime.isValidEvent(endDate, startDate));
        assertFalse(DateTime.isValidEvent(startDate, startDate));
    }
}

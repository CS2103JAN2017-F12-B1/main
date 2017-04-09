package savvytodo.commons.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import savvytodo.commons.core.Messages;
import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.Recurrence;

//@@author A0140016B
/**
 * Date time utility test
 */
public class DateTimeUtilTest {

    @Test
    public void parseStringToDateTime_emptyArgs() throws IllegalValueException {
        String[] expected = new String[] { StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING };
        String[] actual = DateTimeUtil.parseStringToDateTime(StringUtil.WHITESPACE);

        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);

        actual = DateTimeUtil.parseStringToDateTime(StringUtil.EMPTY_STRING);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseStringToDateTime_invalidDate() throws IllegalValueException {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_DATETIME);

        DateTimeUtil.parseStringToDateTime("abc");
        DateTimeUtil.parseStringToDateTime("hello world");

        DateTimeUtil.parseStringToDateTime("+1");
        DateTimeUtil.parseStringToDateTime("-1");
    }

    @Test
    public void parseStringToDateTime_dateRangeSuccessful() throws IllegalValueException {
        String[] expectedDateTime = { "01/04/2017 1500", "02/04/2017 1600" };
        String[] actualDateTime = DateTimeUtil.parseStringToDateTime("1/4/2017 1500 to 2/4/2017 1600");

        assertArrayEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void parseStringToDateTime_singleDateSuccessful() throws IllegalValueException {
        String[] expectedDateTime = { StringUtil.EMPTY_STRING, "01/04/2017 1500" };
        String[] actualDateTime = DateTimeUtil.parseStringToDateTime("1/4/2017 1500");

        assertArrayEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void recurDate() {
        String dateToRecur = "06/09/2017 2200";

        String expectedDay = "07/09/2017 2200";
        String expectedWeek = "13/09/2017 2200";
        String expectedMonth = "06/10/2017 2200";
        String expectedYear = "06/09/2018 2200";

        String modifiedDay = DateTimeUtil.getRecurDate(dateToRecur, Recurrence.Type.Daily.name());
        String modifiedWeek = DateTimeUtil.getRecurDate(dateToRecur, Recurrence.Type.Weekly.name());
        String modifiedMonth = DateTimeUtil.getRecurDate(dateToRecur, Recurrence.Type.Monthly.name());
        String modifiedYear = DateTimeUtil.getRecurDate(dateToRecur, Recurrence.Type.Yearly.name());

        assertEquals(expectedDay, modifiedDay);
        assertEquals(expectedWeek, modifiedWeek);
        assertEquals(expectedMonth, modifiedMonth);
        assertEquals(expectedYear, modifiedYear);
    }

    @Test
    public void isOverDue_dateTimeNullValue_returnFalse() {
        LocalDateTime nullDateTime = null;
        assertFalse(DateTimeUtil.isOverDue(nullDateTime));
    }

    @Test
    public void isOverDue_dateTimeOverDue_returnTrue() {
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        assertTrue(DateTimeUtil.isOverDue(yesterday));
    }

    @Test
    public void isOverDue_dateTimeNotOverDue_returnFalse() {
        LocalDateTime tomorrow = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        assertFalse(DateTimeUtil.isOverDue(tomorrow));
    }

    @Test
    public void isWithinWeek_dateTimeNullValue_returnFalse() {
        LocalDateTime nullDateTime = null;
        assertFalse(DateTimeUtil.isWithinWeek(nullDateTime));
    }

    @Test
    public void isWithinWeek_dateTimeNotWithinWeek_returnFalse() {
        LocalDateTime nextMonth = LocalDateTime.now().plus(1, ChronoUnit.MONTHS);
        LocalDateTime lastMonth = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        assertFalse(DateTimeUtil.isWithinWeek(nextMonth));
        assertFalse(DateTimeUtil.isWithinWeek(lastMonth));
    }

    @Test
    public void isDateTimeWithinRange_emptydateTimeSrc() throws Exception {
        DateTime dateTimeSrc = new DateTime(StringUtil.EMPTY_STRING, StringUtil.EMPTY_STRING);
        DateTime dateTimeQuery = new DateTime("17/04/2017 1200", "18/04/2017 1200");
        assertFalse(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, dateTimeQuery));
    }

    @Test
    public void isDateTimeWithinRange_dateTimeOutOfRange() throws Exception {
        DateTime dateTimeSrc = new DateTime("15/04/2017 1200", "16/04/2017 1200");
        DateTime dateTimeSrc2 = new DateTime("19/04/2017 1200", "20/04/2017 1200");
        DateTime dateTimeQuery = new DateTime("17/04/2017 1200", "18/04/2017 1200");

        assertFalse(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, dateTimeQuery));
        assertFalse(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc2, dateTimeQuery));
    }

    @Test
    public void isDateTimeWithinRange_dateTimeWithinRange() throws Exception {
        DateTime dateTimeSrc = new DateTime("14/04/2017 1200", "16/04/2017 1200");
        DateTime dateTimeQueryFullyInRange = new DateTime("14/04/2017 2000", "15/04/2017 1200");
        DateTime dateTimeQueryPartiallyInRange = new DateTime("13/04/2017 1000", "15/04/2017 1200");

        assertTrue(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, dateTimeQueryFullyInRange));
        assertTrue(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, dateTimeQueryPartiallyInRange));
    }

    @Test
    public void isDateTimeWithinRange_dateTimeWithoutStartDate() throws Exception {
        DateTime dateTimeSrc = new DateTime("15/04/2017 1200", "17/04/2017 1100");
        DateTime deadline = new DateTime("", "16/04/2017 1200");
        DateTime event1 = new DateTime("14/04/2017 2000", "17/04/2017 1200");
        DateTime deadline2 = new DateTime("", "16/04/2017 1200");
        DateTime deadline3 = new DateTime("", "18/04/2017 1200");

        assertTrue(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, event1));
        assertFalse(DateTimeUtil.isDateTimeWithinRange(dateTimeSrc, deadline3));
        assertFalse(DateTimeUtil.isDateTimeWithinRange(deadline, event1));
        assertFalse(DateTimeUtil.isDateTimeWithinRange(deadline, deadline2));
        assertFalse(DateTimeUtil.isDateTimeWithinRange(deadline, deadline3));
    }

    @Test
    public void isDateTimeConflicting_dateTimeConflicts() throws Exception {
        DateTime dateTimeSrc = new DateTime("14/04/2017 1200", "16/04/2017 1200");
        DateTime conflictingDateTimeQuery = new DateTime("14/04/2017 2000", "15/04/2017 1200");
        DateTime conflictingDateTimeQuery2 = new DateTime("13/04/2017 1000", "15/04/2017 1200");

        assertTrue(DateTimeUtil.isDateTimeConflict(dateTimeSrc, conflictingDateTimeQuery));
        assertTrue(DateTimeUtil.isDateTimeConflict(dateTimeSrc, conflictingDateTimeQuery2));
    }

    @Test
    public void isDateTimeConflicting_dateTimeNotConflicting() throws Exception {
        DateTime dateTimeSrc = new DateTime("14/04/2017 1200", "16/04/2017 1200");
        DateTime dateTimeQueryOutOfRange = new DateTime("18/04/2017 2000", "19/04/2017 1200");
        DateTime dateTimeAdjacent = new DateTime("13/04/2017 1000", "14/04/2017 1200");

        assertFalse(DateTimeUtil.isDateTimeConflict(dateTimeSrc, dateTimeQueryOutOfRange));
        assertFalse(DateTimeUtil.isDateTimeConflict(dateTimeSrc, dateTimeAdjacent));
    }

    @Test
    public void getListOfFreeTimeSlotsInDate_success() throws IllegalValueException {
        ArrayList<DateTime> listOfFilledTimeSlots = new ArrayList<DateTime>();
        DateTime dateToCheck = new DateTime("29/10/2017 0000", "29/10/2017 2359");
        ArrayList<DateTime> currentList = new ArrayList<DateTime>();
        ArrayList<DateTime> expectedList = new ArrayList<DateTime>();

        // Initialize listOfFilledTimeSlots
        listOfFilledTimeSlots.add(new DateTime("27/10/2017 1200", "29/10/2017 0830"));
        listOfFilledTimeSlots.add(new DateTime("29/10/2017 0500", "29/10/2017 0630"));
        listOfFilledTimeSlots.add(new DateTime("29/10/2017 0730", "29/10/2017 0900"));
        listOfFilledTimeSlots.add(new DateTime("", "29/10/2017 1300"));
        listOfFilledTimeSlots.add(new DateTime("29/10/2017 1400", "29/10/2017 1500"));
        listOfFilledTimeSlots.add(new DateTime("29/10/2017 2330", "30/10/2017 0100"));

        // Initialize expectedList
        expectedList.add(new DateTime("29/10/2017 0900", "29/10/2017 1400"));
        expectedList.add(new DateTime("29/10/2017 1500", "29/10/2017 2330"));

        currentList = DateTimeUtil.getListOfFreeTimeSlotsInDate(dateToCheck, listOfFilledTimeSlots);

        assertEquals(expectedList, currentList);
    }

    @Test
    public void getDurationInMinutesBetweenTwoLocalDateTime_success() {
        LocalDateTime ldt1 = LocalDateTime.of(2017, 11, 24, 9, 36);
        LocalDateTime ldt2 = LocalDateTime.of(2017, 11, 24, 14, 28);

        assertEquals(DateTimeUtil.getDurationBetweenTwoLocalDateTime(ldt1, ldt2), "4 hr 52 min");
    }

}

package savvytodo.commons.util;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.model.task.DateTime;

//@@author A0140016B
/**
 * @author A0140016B
 * Utility methods related to DateTime
 */
public class DateTimeUtil {

    private static final String TIME_ONLY_FORMAT = "HHmm";
    private static final String DATE_ONLY_FORMAT = "dd/MM/uuuu";
    private static final String DATE_FORMAT = "d/M/uuuu HHmm";
    private static final String DATE_STRING_FORMAT = "dd/MM/uuuu HHmm";

    public static final int FIRST_HOUR_OF_DAY = 0;
    public static final int FIRST_MINUTE_OF_DAY = 0;
    public static final int FIRST_SECOND_OF_DAY = 0;
    public static final int LAST_HOUR_OF_DAY = 23;
    public static final int LAST_MINUTE_OF_DAY = 59;
    public static final int LAST_SECOND_OF_DAY = 59;

    private static final String DAILY = "daily";
    private static final String WEEKLY = "weekly";
    private static final String MONTHLY = "monthly";
    private static final String YEARLY = "yearly";
    private static final int INCREMENT_FREQ = 1;

    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern(DATE_ONLY_FORMAT);
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern(TIME_ONLY_FORMAT);

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
            .withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter DATE_STRING_FORMATTER = DateTimeFormatter.ofPattern(DATE_STRING_FORMAT);

    public static final String MESSAGE_INCORRECT_SYNTAX = "It must be a valid date";

    private static final String MESSAGE_DURATION = "%1$s hr %2$s min";

    public static final String MESSAGE_FREE_TIME_SLOT = StringUtil.SYSTEM_NEWLINE + "%1$s. %2$shrs to %3$shrs (%4$s)";


    /**
     * Extracts the new task's dateTime from the string arguments.
     * @return String[] with first index being the start DateTime and second index being the end
     *         date Time
     */
    public static String[] parseStringToDateTime(String dateTimeArgs) {
        return NattyDateTimeParserUtil.parseStringToDateTime(dateTimeArgs);
    }

    /**
     * Extracts the new task's dateTime from the string arguments.
     * @return LocalDateTime
     */
    private static LocalDateTime parseStringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_FORMATTER);
    }

    /**
     * Checks if given endDateTime is within today and the end of this week
     */
    public static boolean isWithinWeek(LocalDateTime endDateTime) {
        if (endDateTime == null) {
            return false;
        } else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endThisWeek = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(0).withMinute(0)
                    .withSecond(0);
            return endDateTime.isAfter(now) && endDateTime.isBefore(endThisWeek);
        }
    }

    /**
     * Checks if given event's endDateTime is before the end of today
     */
    public static boolean isOverDue(LocalDateTime endDateTime) {
        if (endDateTime == null) {
            return false;
        } else {
            LocalDateTime now = LocalDateTime.now();
            return endDateTime.isBefore(now);
        }
    }

    /**
     * Checks whether the dateTimeQuery falls within the range of the dateTimeSource
     * i.e. dateTimeQuery start is equals to or before the dateTimeSource end
     * && dateTimeQuery end is equals to or after the dateTimeSource start
     * Return false if task is a deadline or floating task (i.e. no start or end)
     * @param dateTimeSource
     * @param dateTimeQuery
     * @throws IllegalValueException
     * @throws DateTimeException
     */
    public static boolean isDateTimeWithinRange(DateTime dateTimeSource, DateTime dateTimeQuery)
            throws DateTimeException, IllegalValueException {
        if (!areEvents(dateTimeSource, dateTimeQuery)) {
            return false;
        }

        DateTime dateTimeOriginal = fillDateTime(dateTimeSource);
        DateTime dateTimeCompare = fillDateTime(dateTimeQuery);

        return !dateTimeOriginal.getEndDate().isBefore(dateTimeCompare.getStartDate())
                && !dateTimeOriginal.getStartDate().isAfter(dateTimeCompare.getEndDate());
    }

    /**
     * Checks whether the dateTimeQuery conflicts with the dateTimeSource
     * i.e. dateTimeQuery end should occur after the dateTimeSource start
     * and dateTimeQuery start should occur before the dateTimeSource end
     * Return false if task is a deadline or floating task (i.e. no start or end)
     * @throws IllegalValueException
     * @throws DateTimeException
     */
    public static boolean isDateTimeConflict(DateTime dateTimeSource, DateTime dateTimeQuery)
            throws DateTimeException, IllegalValueException {
        if (!areEvents(dateTimeSource, dateTimeQuery)) {
            return false;
        }

        DateTime dateTimeOriginal = fillDateTime(dateTimeSource);
        DateTime dateTimeCompare = fillDateTime(dateTimeQuery);

        return dateTimeOriginal.getEndDate().isAfter(dateTimeCompare.getStartDate())
                && dateTimeOriginal.getStartDate().isBefore(dateTimeCompare.getEndDate());
    }

    private static DateTime fillDateTime(DateTime filledDateTime) throws IllegalValueException {

        filledDateTime.setEnd(parseStringToLocalDateTime(filledDateTime.endValue));
        filledDateTime.setStart(parseStringToLocalDateTime(filledDateTime.startValue));

        return filledDateTime;
    }

    /**
     * Check whether eventDateTime is an event
     * @param eventDateTime
     * @return whether task is an event
     */
    private static boolean isEvent(DateTime eventDateTime) {
        if (eventDateTime.getStartDate() == null
                || eventDateTime.getEndDate() == null) {
            return false;
        }

        return true;
    }

    /**
     * Check whether dateTimeSource and dateTimeQuery are events before they can be compared
     * @param dateTimeSource
     * @param dateTimeQuery
     * @return whether the task compared to and with are events
     */
    private static boolean areEvents(DateTime dateTimeSource, DateTime dateTimeQuery) {
        if (!isEvent(dateTimeSource)) {
            return false;
        }

        if (!isEvent(dateTimeQuery)) {
            return false;
        }

        return true;
    }

    /**
     * @param dateToCheck cannot be null and it must be an event
     * @return an ArrayList<DateTime> of free slots in a specified date
     * else return an empty ArrayList
     */
    public static ArrayList<DateTime> getListOfFreeTimeSlotsInDate(
            DateTime dateToCheck,
            ArrayList<DateTime> listOfFilledTimeSlotsInDate) {
        ArrayList<DateTime> listOfFreeTimeSlots = new ArrayList<DateTime>();
        if (isEvent(dateToCheck)) {
            LocalDateTime startDateTime = dateToCheck.getStartDate();
            LocalDateTime endDateTime = dateToCheck.getEndDate();

            for (DateTime dateTime : listOfFilledTimeSlotsInDate) {
                if (dateTime.getStartDate() == null) {
                    continue;
                } else {
                    endDateTime = dateTime.getStartDate();
                }

                if (startDateTime.isBefore(endDateTime)) {
                    listOfFreeTimeSlots
                            .add(new DateTime(startDateTime, endDateTime));
                }

                if (startDateTime.isBefore(dateTime.getEndDate())) {
                    startDateTime = dateTime.getEndDate();
                }
            }

            if (startDateTime.isBefore(dateToCheck.getEndDate())) {
                listOfFreeTimeSlots.add(new DateTime(startDateTime, dateToCheck.getEndDate()));
            }
        }

        return listOfFreeTimeSlots;
    }


    public static String getDayAndDateString(DateTime dateTime) {
        StringBuilder sb = new StringBuilder();

        sb.append(dateTime.getEndDate().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(",")
                .append(dateTime.getEndDate().format(DATE_ONLY_FORMATTER));

        return sb.toString();
    }

    /**
     * @return String of Free DateTime In Date
     */
    public static String getStringOfFreeDateTimeInDate(DateTime dateToCheck,
            ArrayList<DateTime> listOfFreeTimeSlotsInDate) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDayAndDateString(dateToCheck))
                .append(":");

        int counter = 1;

        for (DateTime dateTime : listOfFreeTimeSlotsInDate) {
            sb.append(String.format(MESSAGE_FREE_TIME_SLOT, counter,
                    dateTime.getStartDate().format(TIME_ONLY_FORMATTER),
                    dateTime.getEndDate().format(TIME_ONLY_FORMATTER),
                    getDurationBetweenTwoLocalDateTime(dateTime.getStartDate(), dateTime.getEndDate())));
            counter++;
        }

        return sb.toString();
    }

    /**
     * Calculate the duration between 2 dates
     * @param startDateTime is not null
     * @param endDateTime is not null
     * @return String duration between 2 dates
     */
    public static String getDurationBetweenTwoLocalDateTime(
            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        return String.format(MESSAGE_DURATION, hours, minutes);
    }

    /**
     * Modify the date based on the new hour, min and sec
     * @return Date
     */
    public static Date setDateTime(Date toBeEdit, int hour, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toBeEdit);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        toBeEdit = calendar.getTime();

        return toBeEdit;
    }

    /**
     * @param recurDate usually is the start date of an event
     * @param freqType is the frequency based on recurrance
     * Modifies the recurDate based on the frequency for recurring tasks.
     * freqType cannot be null or None
     */
    private static String getRecurDate(String recurDate, String freqType) {
        LocalDateTime date = LocalDateTime.parse(recurDate, DATE_FORMATTER);

        switch (freqType.toLowerCase()) {
        case DAILY:
            date = date.plusDays(INCREMENT_FREQ);
            break;
        case WEEKLY:
            date = date.plusWeeks(INCREMENT_FREQ);
            break;
        case MONTHLY:
            date = date.plusMonths(INCREMENT_FREQ);
            break;
        case YEARLY:
            date = date.plusYears(INCREMENT_FREQ);
            break;
        }

        recurDate = date.format(DATE_STRING_FORMATTER);
        return recurDate;
    }

    /**
     * @param recurDates usually is the start date of an event
     * @param freqType is the frequency based on recurrance
     * @param noOfRecurr is the number of recurrance
     * Modifies the recurDates based on the frequency for recurring tasks.
     * freqType cannot be null or None
     */
    public static ArrayList<String> getRecurDates(String recurDate, String freqType, int noOfRecurr) {
        ArrayList<String> recurrDates = new ArrayList<String>();

        for (int i = 0; i < noOfRecurr; i++) {
            recurrDates.add(getRecurDate(recurDate, freqType));
        }

        return recurrDates;
    }

    public static LocalDateTime setLocalTime(LocalDateTime dateTime, int hour, int min, int sec) {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), hour, min, sec);
    }
}

package savvytodo.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.commons.util.StringUtil;
import savvytodo.model.category.Category;
import savvytodo.model.category.UniqueCategoryList;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.Description;
import savvytodo.model.task.Location;
import savvytodo.model.task.Name;
import savvytodo.model.task.Priority;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.TaskType;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes
 */
public class ParserUtil {

    private static final int CHAR_POS_0 = 0;
    private static final int CHAR_POS_1 = 1;
    private static final int SIZE_OF_DATE_TIME_INPUT = 1;
    private static final int ARRAY_FIELD_2 = 1;
    private static final int ARRAY_FIELD_1 = 0;

    private static final Pattern INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    //@@author A0147827U
    /**
     * Returns the specified TaskIndex in the {@code command} if it matches the required syntax (eg "1", "f1")
     * Returns an {@code Optional.empty()} otherwise.
     */
    public static Optional<TaskIndex> parseIndex(String command) {
        final Matcher matcher = INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        Optional<TaskIndex> parsedIndex;

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            String listIdentifier = listIdentifier(index);
            if (isValidTaskType(listIdentifier)) {
                index = index.substring(1);
                if (!StringUtil.isUnsignedInteger(index)) {
                    parsedIndex = Optional.empty();
                } else {
                    parsedIndex = Optional.of(new TaskIndex(TaskType.FLOATING, Integer.parseInt(index)));
                }
            } else {
                parsedIndex = Optional.empty();
            }
        } else {
            parsedIndex = Optional.of(new TaskIndex(TaskType.EVENT, Integer.parseInt(index))); //defaults to event type
        }

        return parsedIndex;
    }

    //@@author A0140016B
    /**
     * @param listIdentifier
     * @return true is it has valid floating task type
     */
    private static boolean isValidTaskType(String listIdentifier) {
        return listIdentifier.equalsIgnoreCase(CliSyntax.INDEX_FLOATING);
    }

    /**
     * @param unfilteredIndexString that is not a integer, it cannot be null
     * @return position character of the String is there is any
     */
    private static String listIdentifier(String unfilteredIndexString) {
        String listIdentifier = StringUtil.EMPTY_STRING + unfilteredIndexString.charAt(CHAR_POS_0);
        return listIdentifier;
    }

    /**
     * Returns List<TaskIndex> if the String is parsed
     * Returns a List<TaskIndex> populated by all elements in the given string
     * Returns a List<TaskIndex> if the given {@code Optional} is empty,
     * or if the List<TaskIndex> contained in the {@code Optional} is empty
     */
    public static Optional<List<TaskIndex>> parseMultipleInteger(String indicesString) {
        boolean parseError = false;

        String trimmedIndicesString = indicesString.trim();
        String[] indicesArray = trimmedIndicesString.split(StringUtil.WHITESPACE_REGEX);
        List<TaskIndex> indicesList = new ArrayList<TaskIndex>();

        if (!trimmedIndicesString.isEmpty()) {
            for (String index : indicesArray) {
                String listIdentifier = listIdentifier(index);
                if (!StringUtil.isUnsignedInteger(index)) {
                    if (isValidTaskType(listIdentifier)) {
                        index = index.substring(1);
                        if (!StringUtil.isUnsignedInteger(index)) {
                            parseError = true;
                            break;
                        } else {
                            indicesList.add(new TaskIndex(TaskType.FLOATING, Integer.parseInt(index)));
                        }
                    } else {
                        parseError = true;
                        break;
                    }
                } else {
                    indicesList.add(new TaskIndex(TaskType.EVENT, Integer.parseInt(index)));
                }
            }
        }

        if (indicesList.isEmpty() || parseError) {
            return Optional.empty();
        }
        return Optional.of(indicesList);
    }
    //@@author

    /**
     * Returns a new Set populated by all elements in the given list of strings
     * Returns an empty set if the given {@code Optional} is empty,
     * or if the list contained in the {@code Optional} is empty
     */
    public static Set<String> toSet(Optional<List<String>> list) {
        List<String> elements = list.orElse(Collections.emptyList());
        return new HashSet<>(elements);
    }

    /**
    * Splits a preamble string into ordered fields.
    * @return A list of size {@code numFields} where the ith element is the ith field value if specified in
    *         the input, {@code Optional.empty()} otherwise.
    */
    public static List<Optional<String>> splitPreamble(String preamble, int numFields) {
        return Arrays.stream(Arrays.copyOf(preamble.split(StringUtil.WHITESPACE_REGEX, numFields), numFields))
                .map(Optional::ofNullable)
                .collect(Collectors.toList());
    }

    //@@author A0140016B
    /**
     * Extract a {@code Optional<String> dateTime} into an {@code String[]} if {@code dateTime} is present.
     * @throws IllegalValueException for invalid dateTime
     */
    public static String[] getDateTimeFromArgs(Optional<String> dateTime) throws IllegalValueException {
        assert dateTime != null;
        if (dateTime.isPresent()) {
            String [] dateTimeValues = DateTimeUtil.parseStringToDateTime(dateTime.get());
            if (dateTimeValues.length == SIZE_OF_DATE_TIME_INPUT) {
                return new String[] {StringUtil.EMPTY_STRING, dateTimeValues[ARRAY_FIELD_1]};
            } else {
                return dateTimeValues;
            }
        } else {
            return DateTime.DEFAULT_VALUES;
        }
    }

    /**
     * Extract a {@code Optional<String> recurrence} into an {@code String[]} if {@code recurrence} is present.
     */
    public static String[] getRecurrenceFromArgs(Optional<String> recurrence) {
        assert recurrence != null;
        return recurrence.isPresent() ? recurrence.get().split(StringUtil.WHITESPACE_REGEX) : Recurrence.DEFAULT_VALUES;
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        assert name != null;
        return name.isPresent() ? Optional.of(new Name(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> priority} into an {@code Optional<Priority>} if {@code priority} is present.
     */
    public static Optional<Priority> parsePriority(Optional<String> priority) throws IllegalValueException {
        assert priority != null;
        return priority.isPresent() ? Optional.of(new Priority(priority.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> location} into an {@code Optional<Location>} if {@code location} is present.
     */
    public static Optional<Location> parseLocation(Optional<String> location) throws IllegalValueException {
        assert location != null;
        return location.isPresent() ? Optional.of(new Location(location.get())) : Optional.empty();
    }

    //@@author A0140016B
    /**
     * Parses a {@code Optional<String> dateTime} into an {@code Optional<DateTime>}
     * if {@code dateTime} is present.
     */
    public static Optional<DateTime> parseDateTime(Optional<String> dateTime) throws IllegalValueException {
        assert dateTime != null;
        if (dateTime.isPresent()) {
            String [] dateTimeValues = DateTimeUtil.parseStringToDateTime(dateTime.get());
            if (dateTimeValues.length == SIZE_OF_DATE_TIME_INPUT) {
                return Optional.of(new DateTime(StringUtil.EMPTY_STRING, dateTimeValues[ARRAY_FIELD_1]));
            } else {
                return Optional.of(new DateTime(dateTimeValues[ARRAY_FIELD_1], dateTimeValues[ARRAY_FIELD_2]));
            }
        } else {
            return Optional.empty();
        }
    }

    //@@author A0140016B
    /**
     * Parses a {@code Optional<String> recurrence} into an {@code Optional<Recurrence>}
     * if {@code recurrence} is present.
     */
    public static Optional<Recurrence> parseRecurrence(Optional<String> recurrence) throws IllegalValueException {
        assert recurrence != null;
        if (recurrence.isPresent()) {
            String [] recurValues = recurrence.get().split(StringUtil.WHITESPACE_REGEX);
            return Optional
                    .of(new Recurrence(recurValues[ARRAY_FIELD_1], Integer.parseInt(recurValues[ARRAY_FIELD_2])));
        } else {
            return Optional.empty();
        }
    }
    //@@author

    /**
     * Parses a {@code Optional<String> description} into an {@code Optional<Description>}
     * if {@code description} is present.
     */
    public static Optional<Description> parseDescription(Optional<String> description) throws IllegalValueException {
        assert description != null;
        return description.isPresent() ? Optional.of(new Description(description.get())) : Optional.empty();
    }

    /**
     * Parses {@code Collection<String> categories} into an {@code UniqueCategoryList}.
     */
    public static UniqueCategoryList parseCategories(Collection<String> categories) throws IllegalValueException {
        assert categories != null;
        final Set<Category> categorySet = new HashSet<>();
        for (String categoryName : categories) {
            categorySet.add(new Category(categoryName));
        }
        return new UniqueCategoryList(categorySet);
    }
}

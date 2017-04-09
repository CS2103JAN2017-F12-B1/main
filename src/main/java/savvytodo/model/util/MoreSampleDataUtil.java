package savvytodo.model.util;

import java.util.Calendar;
import java.util.Date;

import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.model.category.UniqueCategoryList;
import savvytodo.model.category.UniqueCategoryList.DuplicateCategoryException;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.Description;
import savvytodo.model.task.Location;
import savvytodo.model.task.Name;
import savvytodo.model.task.Priority;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Task;

//@@author A0140036X
/**
 * Get more tasks than MoreSampleDataUtil
 */
public class MoreSampleDataUtil extends SampleDataUtil {

    //@@author A0140036X
    @Override
    public Task[] getSampleTasks() {
        Calendar calendar = Calendar.getInstance();

        Task[] sampleData = new Task[50];
        int i = 0;
        Task[] lessSampleData = super.getSampleTasks();
        for (; i < lessSampleData.length; i++) {
            sampleData[i] = lessSampleData[i];
        }
        try {
            sampleData[i++] = new Task(new Name("Project Security 2"), new Priority("low"),
                    new Description("milestone 3"), new Location("Blk 30 NUS Street 29, #06-40"),
                    new UniqueCategoryList("project"), new DateTime(DateTime.DEFAULT_VALUES),
                    new Recurrence(Recurrence.DEFAULT_VALUES));
            sampleData[i++] = new Task(new Name("Dinner at Nandos"), new Priority("medium"),
                    new Description("makan"), new Location("White Sands"),
                    new UniqueCategoryList("meal"), new DateTime(DateTime.DEFAULT_VALUES),

                    new Recurrence(Recurrence.DEFAULT_VALUES));
            sampleData[i++] = new Task(new Name("Meet up with aunty Jane"), new Priority("low"),
                    new Description("2pm, Rmb to bring gifts"),
                    new Location("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new UniqueCategoryList("family", "meetup"),
                    new DateTime(DateTime.DEFAULT_VALUES),
                    new Recurrence(Recurrence.DEFAULT_VALUES));
            sampleData[i++] = new Task(new Name("Skype Meeting 4"), new Priority("high"),
                    new Description("Skype meeting with project team 2"), new Location("None"),
                    new UniqueCategoryList("project"), new DateTime(),
                    new Recurrence(Recurrence.DEFAULT_VALUES));
            sampleData[i++] = new Task(new Name("David Li Brother BDay"), new Priority("low"),
                    new Description("6pm, rmb to buy present"),
                    new Location("Blk 436 Botanic Gardens 26, #16-43"),
                    new UniqueCategoryList("family"), new DateTime(DateTime.DEFAULT_VALUES),
                    new Recurrence(Recurrence.DEFAULT_VALUES));

            calendar.setTime(new Date());
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            calendar.set(Calendar.HOUR, 19);
            DateTime romanticDate = DateTimeUtil.generateDateTimeFromDuration(calendar.getTime(),
                    Calendar.DAY_OF_YEAR, 4);

            sampleData[i++] = new Task(new Name("Date with Jerome"), new Priority("high"),
                    new Description("Smart Casual"), new Location("Botanic Gardens"),
                    new UniqueCategoryList("romance"), romanticDate,
                    new Recurrence(Recurrence.DEFAULT_VALUES));

            romanticDate = DateTimeUtil.generateDateTimeFromDuration(calendar.getTime(),
                    Calendar.DAY_OF_YEAR, 14);
            sampleData[i++] = new Task(new Name("Date with Jerome 2"), new Priority("high"),
                    new Description("Smart Casual"), new Location("Botanic Gardens"),
                    new UniqueCategoryList("romance"), romanticDate,
                    new Recurrence(Recurrence.DEFAULT_VALUES));

            romanticDate = DateTimeUtil.generateDateTimeFromDuration(calendar.getTime(),
                    Calendar.DAY_OF_YEAR, 20);
            sampleData[i++] = new Task(new Name("Date with Jerome 3"), new Priority("high"),
                    new Description("Smart Casual"), new Location("Botanic Gardens"),
                    new UniqueCategoryList("romance"), romanticDate,
                    new Recurrence(Recurrence.DEFAULT_VALUES));

            int weeksToGenerate = 6;

            Date drivingDate;
            Date drivingEndDate;

            for (int w = 1; w <= weeksToGenerate; w++) {
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 0);
                drivingDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 3);
                drivingEndDate = calendar.getTime();

                sampleData[i++] = taskGenerator("Driving Lesson " + w, "high", "WEW", "Bukit Batok",
                        "driving", new DateTime(drivingDate, drivingEndDate),
                        new Recurrence(Recurrence.DEFAULT_VALUES));
            }

            // CS8888
            weeksToGenerate = 15;

            calendar.setTime(new Date());
            Date tutorialDate;
            Date tutorialEndDate;
            Date lectureDate;
            Date lectureEndDate;

            for (int w = 1; w <= weeksToGenerate; w++) {
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 0);
                tutorialDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 3);
                tutorialEndDate = calendar.getTime();

                sampleData[i++] = taskGenerator("CS8888 Tutorial " + w, "high", "Need to do", "SR1",
                        "lesson", new DateTime(tutorialDate, tutorialEndDate),
                        new Recurrence(Recurrence.DEFAULT_VALUES));

                calendar.add(Calendar.DAY_OF_YEAR, 3);
                lectureDate = calendar.getTime();
                calendar.add(Calendar.HOUR, 3);
                lectureEndDate = calendar.getTime();
                sampleData[i++] = taskGenerator("CS8888 Lecture " + w, "medium", "webcasted", "SR1",
                        "lesson", new DateTime(lectureDate, lectureEndDate),
                        new Recurrence(Recurrence.DEFAULT_VALUES));
                calendar.add(Calendar.DAY_OF_YEAR, 4);
            }

            for (Task t : sampleData) {
                assert t != null;
            }
        } catch (

        IllegalValueException e) {
            e.printStackTrace();
            throw new AssertionError("sample data cannot be invalid", e);
        }
        return sampleData;
    }

    public static Task taskGenerator(String name, String priority, String description,
            String location, String category, DateTime dateTime, Recurrence recurrence)
            throws DuplicateCategoryException, NumberFormatException, IllegalValueException {
        return new Task(new Name(name), new Priority(priority), new Description(description),
                new Location(location), new UniqueCategoryList(category), dateTime, recurrence);
    }

    public static void main(String[] args) {
        for (Task t : new MoreSampleDataUtil().getSampleTasks()) {
            System.out.println(t);
        }
    }
}

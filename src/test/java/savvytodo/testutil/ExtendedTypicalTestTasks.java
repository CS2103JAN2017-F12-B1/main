package savvytodo.testutil;

import java.io.IOException;
import java.util.ArrayList;

import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.model.TaskManager;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Task;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;
import savvytodo.storage.StorageManager;

//@@author A0140036X
/**
 * Creates more typical tests than TypicalTestTasks.java
 */
public class ExtendedTypicalTestTasks extends TypicalTestTasks {
    public ExtendedTypicalTestTasks() {
        super();
    }

    //@@author A0140036X
    /**
     * For testing purposes
     */
    public static void main(String[] args) {
        saveExtendedTypicalTasksToFile("data/sampleData.xml");
    }

    //@@author A0140036X
    /**
     * Creates test tasks and saves to file
     */
    public static void saveExtendedTypicalTasksToFile(String targetFilePath) {
        StorageManager storage = new StorageManager(targetFilePath, "preferences.json");
        TaskManager taskManager = new TaskManager();
        TypicalTestTasks typicals = new ExtendedTypicalTestTasks();
        for (TestTask t : typicals.getTypicalTasks()) {
            Task toAdd = new Task(t);
            try {
                taskManager.addTask(toAdd);
            } catch (DuplicateTaskException e) {
                e.printStackTrace();
                assert false : "not possible";
            }
        }
        try {
            storage.saveTaskManager(taskManager);
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    //@@author A0140036X
    /**
     *
     * @return a set of tasks of size 50
     */
    @Override
    public TestTask[] getTypicalTasks() {
        ArrayList<TestTask> sampleTasks = new ArrayList<TestTask>();
        for (TestTask t : super.getTypicalTasks()) {
            sampleTasks.add(t);
        }
        try {
            sampleTasks.add(new TaskBuilder().withName("99 co Internship Interview").withPriority("high")
                    .withDescription("Face to face interview attire casual").withLocation("One North")
                    .withDateTime("11/06/2017 1000", "11/06/2017 1600").withRecurrence(Recurrence.DEFAULT_VALUES)
                    .withStatus(false).build());
            sampleTasks
                    .add(new TaskBuilder().withName("SoC games").withPriority("medium").withDescription("Annual games")
                            .withLocation("School of Computing").withDateTime("11/03/2017 0800", "11/03/2017 1600")
                            .withRecurrence(Recurrence.DEFAULT_VALUES).withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Meet up with Jane").withPriority("medium")
                    .withDescription("Call interview").withLocation("Arts Canteen")
                    .withDateTime("10/03/2017 1000", "10/03/2017 1600").withRecurrence(Recurrence.DEFAULT_VALUES)
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Study Session").withPriority("low").withDescription("b1 gang")
                    .withLocation("Marina Bay").withDateTime("10/04/2017 1000", "10/04/2017 1600")
                    .withRecurrence(Recurrence.DEFAULT_VALUES).withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 1 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("7/1/2017 1000", "7/1/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 2 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("14/1/2017 1000", "14/1/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 3 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("21/1/2017 1000", "21/1/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 4 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("28/1/2017 1000", "28/1/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 5 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("5/2/2017 1000", "5/2/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 6 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("12/2/2017 1000", "12/2/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 7 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("19/2/2017 1000", "19/2/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 8 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("26/2/2017 1000", "26/2/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 9 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("3/3/2017 1000", "3/3/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 10 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("10/3/2017 1000", "10/3/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 11 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("17/3/2017 1000", "17/3/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 12 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("24/3/2017 1000", "24/3/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 13 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("1/4/2017 1000", "1/4/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS2103 Lecture 14 ").withPriority("low")
                    .withDescription("Webcast").withLocation("iCube").withDateTime("8/4/2017 1000", "8/4/2017 1100")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 1").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("9/4/2017 1000", "9/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 2").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("10/4/2017 1000", "10/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 3").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("11/4/2017 1000", "11/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 4").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("12/4/2017 1000", "12/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 5").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("13/4/2017 1000", "13/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Dance Class 6").withPriority("high").withDescription("WuStudio")
                    .withLocation("Studio").withDateTime("14/4/2017 1000", "14/4/2017 1100").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 10").withPriority("high")
                    .withDescription("Propositions").withLocation("Anywhere")
                    .withDateTime("14/4/2017 1200", "14/4/2017 1300").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 12").withPriority("high")
                    .withDescription("Definition").withLocation("Anywhere")
                    .withDateTime("15/4/2017 1200", "15/4/2017 1300").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 13").withPriority("high")
                    .withDescription("Quantifiers").withLocation("Anywhere")
                    .withDateTime("16/4/2017 1200", "16/4/2017 1300").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 14").withPriority("high")
                    .withDescription("Stats").withLocation("Anywhere").withDateTime("17/4/2017 1200", "17/4/2017 1300")
                    .withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 15").withPriority("high").withDescription("")
                    .withLocation("Anywhere").withDateTime("18/4/2017 1200", "18/4/2017 1300").withStatus(false)
                    .build());
            sampleTasks.add(new TaskBuilder().withName("CS1231 Tutorial 16").withPriority("high")
                    .withDescription("Truth Tables").withLocation("Anywhere")
                    .withDateTime("19/4/2017 1200", "19/4/2017 1300").withStatus(false).build());
            sampleTasks.add(new TaskBuilder().withName("Date with Abigail").withPriority("high").withDescription("")
                    .withLocation("Orchard").withDateTime("19/5/2017 1700", "19/5/2017 2300").withStatus(false)
                    .build());
            sampleTasks.add(new TaskBuilder().withName("Date with Adyln").withPriority("high").withDescription("")
                    .withLocation("Orchard").withDateTime("20/5/2017 1700", "20/5/2017 2300").withStatus(false)
                    .build());
            sampleTasks.add(new TaskBuilder().withName("Date with Adele").withPriority("high").withDescription("")
                    .withLocation("Orchard").withDateTime("21/5/2017 1700", "21/5/2017 2300").withStatus(false)
                    .build());
            sampleTasks.add(new TaskBuilder().withName("Date with Adrienne").withPriority("high").withDescription("")
                    .withLocation("Orchard").withDateTime("22/5/2017 1700", "22/5/2017 2300").withStatus(false)
                    .build());
            sampleTasks.add(new TaskBuilder().withName("Buy korea trip tickets").withPriority("high")
                    .withDescription("$$").withLocation("Gordon's").withDateTime("08/04/2017 2200", "08/04/2017 2300")
                    .withStatus(false).build());

        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
        return sampleTasks.toArray(new TestTask[] {});
    }
}

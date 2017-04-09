package guitests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import savvytodo.commons.exceptions.DataConversionException;
import savvytodo.logic.commands.ClearCommand;
import savvytodo.logic.commands.LoadCommand;
import savvytodo.model.ReadOnlyTaskManager;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Task;
import savvytodo.storage.TaskManagerStorage;
import savvytodo.storage.XmlTaskManagerStorage;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;
import savvytodo.testutil.UiState;

//@@author A0140036X
/**
 * Testing storage related commands.
 */

public class StorageTest extends TaskManagerGuiTest {

    //@@author A0140036X
    /**
     * Manages the state of UI for Save command tests.
     *
     * Save command requires changes in data used in UI and jUnit does not guarantee
     * these tests to run after all the other tests so a mechanism must be in place to resume
     * state of app.
     *
     */
    public static class SaveAppLocation extends UiState {

        private String savedLocation;

        public SaveAppLocation(StorageTest test) {
            super(test);
        }

        //@@author A0140036X
        /**
         * @see UiState#onSaveState()
         * Records file path location from UI.
         */
        @Override
        public void onSaveState() {
            this.savedLocation = getGuiTest().statusBarHandle.getSaveLocationText();
        }

        //@@author A0140036X
        /**
         * @see UiState#onSaveState()
         * Loads savedLocation
         */
        @Override
        public void onResumeState() {
            ((StorageTest) getGuiTest()).loadFromFilePath(savedLocation);
        }

        public String getSavedLocation() {
            return savedLocation;
        }

        public void setSavedLocation(String savedLocation) {
            this.savedLocation = savedLocation;
        }

    }

    //@@author A0140036X
    /**
     * returns a valid file path for storage file that does not exist.
     */
    protected String getNewTestStorageFileName() {
        String testTaskManagerFileName = new Date().getTime() + "_taskmanager.xml";
        return TestUtil.getFilePathInSandboxFolder(testTaskManagerFileName);
    }

    //@@author A0140036X
    /**
     * Tests Load Command.
     * <p>
     * Loads task manager from file path
     * </p>
     * @param testTaskManagerFilePath path of task manager file to load
     * @param tasks tasks to check against those loaded from file
     */
    protected void assertLoad(String testTaskManagerFilePath, ReadOnlyTask[] tasks) {
        loadFromFilePath(testTaskManagerFilePath);
        TestUtil.printTasks(eventTaskListPanel.getListView().getItems().toArray(new Task[0]));
        assertTrue(eventTaskListPanel.isListMatchingIgnoreOrder(tasks));
        assertResultMessage(LoadCommand.getSuccessMessage(testTaskManagerFilePath));
    }

    //@@author A0140036X
    /**
     * Tests Load Command.
     * <p>
     * Loads task manager from file path
     * </p>
     * @param testTaskManagerFilePath path of task manager file to load
     */
    protected void assertLoad(String testTaskManagerFilePath) {
        loadFromFilePath(testTaskManagerFilePath);
        TaskManagerStorage tmStorage = new XmlTaskManagerStorage(testTaskManagerFilePath);
        Optional<ReadOnlyTaskManager> read;
        try {
            read = tmStorage.readTaskManager();
            assertTrue(read.isPresent());
            Task[] readTasks = read.get().getTaskList().toArray(new Task[0]);
            assertTrue(eventTaskListPanel.isListMatchingIgnoreOrder(readTasks));
            assertResultMessage(LoadCommand.getSuccessMessage(testTaskManagerFilePath));
        } catch (DataConversionException | IOException e) {
            assertTrue(false);
        }
    }

    //@@author A0140036X
    /** Tests clear command. */
    protected void assertClear() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
    }

    //@@author A0140036X
    /**
     * Tests add command.
     * @param currentList list to add task to
     * @return new list with added task
     */
    protected TestTask[] assertAdd(TestTask[] currentList) {
        TestTask taskToAdd = td.getTypicalTasks()[0];
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertTrue(eventTaskListPanel.isListMatching(expectedList));
        return expectedList;
    }

    //@@author A0140036X
    /**
     * Tests add command by adding many test tasks.
     * @param currentList list to add task to
     * @return new list with added task
     */
    protected TestTask[] assertAddMany(TestTask[] currentList) {
        TestTask[] toAdd = td.getTypicalTasks();
        TestTask[] expectedList = currentList;
        for (TestTask testTask : toAdd) {
            expectedList = TestUtil.addTasksToList(expectedList, testTask);
            commandBox.runCommand(testTask.getAddCommand());
        }
        assertTrue(eventTaskListPanel.isListMatching(expectedList));
        return expectedList;
    }

    //@@author A0140036X
    /**
     * Enters load command into ui
     */
    protected void loadFromFilePath(String filePath) {
        String cmd = "load " + filePath;
        commandBox.runCommand(cmd);
    }

    //@@author A0140036X
    /**
     * Runs clear commad
     */
    protected void runClearCommand() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        sleep(100);
    }
}

package guitests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import savvytodo.logic.commands.ClearCommand;
import savvytodo.logic.commands.LoadCommand;
import savvytodo.model.TaskManager;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;
import savvytodo.storage.StorageManager;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

/**
 * LoadCommandTest tests the load command which changes the storage file used
 *
 * @@author A0140036X
 *
 */

public class LoadCommandTest extends TaskManagerGuiTest {

    // @@author A0140036X
    /**
     * Tests loading of new file that doesn't exist
     * 1. Save data location
     * 2. Generate tasks
     * 3. Create new storage file
     * 4. Save tasks into storage file
     * 5. Assert load tasks
     * 6. Assert clear tasks
     * 7. Assert add task
     * 
     * This test assumes if 5, 6, 7 is successful task manager has been loaded and linked to UI
     * Resumes with saved location at the end
     * @author A0140036X
     */
    @Test
    public void createNewTemporaryTaskManagerAndLoad() {
        String savedLocation = this.statusBarHandle.getSaveLocationText();
        String resumeCmd = "load " + savedLocation;

        TaskManager tempTaskManager = new TaskManager();
        TestTask[] testTasks = td.getGeneratedTasks(10);
        String testTaskManagerFilePath = TestUtil.getFilePathInSandboxFolder(new Date().getTime() + "_taskmanager.xml");

        try {
            tempTaskManager.setTasks(TestUtil.asList(testTasks));
        } catch (DuplicateTaskException e1) {
            assertTrue(false);
        }

        StorageManager storage = new StorageManager(testTaskManagerFilePath, "");
        try {
            storage.saveTaskManager(tempTaskManager);
        } catch (IOException e) {
            assertTrue(false);
        }
        
        assertLoad(testTaskManagerFilePath, testTasks);
        assertClear();
        assertAdd(testTasks);

        commandBox.runCommand(resumeCmd);
    }

    //@@author A0140036X
    /**
     * Loads task manager from file path
     * @param testTaskManagerFilePath path of task manager file to load
     * @param tasks 
     */
    private void assertLoad(String testTaskManagerFilePath, ReadOnlyTask[] tasks) {
        String cmd = "load " + testTaskManagerFilePath;

        commandBox.runCommand(cmd);

        assertTrue(this.taskListPanel.isListMatching(tasks));
        assertResultMessage(LoadCommand.getSuccessMessage(testTaskManagerFilePath));
        
        sleep(1000000);
    }

    //@@author A0140036X
    /**
     * Test clear command
     */
    private void assertClear() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
    }

    //@@author A0140036X
    /**
     * Test add command
     * @param currentList 
     */
    private void assertAdd(TestTask[] currentList) {
        TestTask taskToAdd = td.getTypicalTasks()[0];
        commandBox.runCommand(taskToAdd.getAddCommand());
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
}

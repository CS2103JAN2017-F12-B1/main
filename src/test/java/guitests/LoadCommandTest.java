package guitests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import savvytodo.model.TaskManager;
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
     * 5. Load tasks in UI using command
     * 6. Compare generated tasks with tasks in list
     *
     * Resumes with saved location at the end
     * @author A0140036X
     */
    @Test
    public void createNewTemporaryTaskManager() {
        String savedLocation = this.statusBarHandle.getText();
        String resumeCmd = "load " + savedLocation;
        
        TaskManager tempTaskManager = new TaskManager();
        TestTask[] tasks = td.getGeneratedTasks(10);
        String testTaskManagerFilePath = TestUtil.getFilePathInSandboxFolder(new Date().getTime() + "_taskmanager.xml");

        try {
            tempTaskManager.setTasks(TestUtil.asList(tasks));
        } catch (DuplicateTaskException e1) {
            assertTrue(false);
        }

        StorageManager storage = new StorageManager(testTaskManagerFilePath, "");
        try {
            storage.saveTaskManager(tempTaskManager);
        } catch (IOException e) {
            assertTrue(false);
        }
        String cmd = "load " + testTaskManagerFilePath;

        commandBox.runCommand(cmd);

        assertTrue(this.taskListPanel.isListMatching(tasks));

        commandBox.runCommand(resumeCmd);
    }
}

package guitests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import savvytodo.model.TaskManager;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;
import savvytodo.storage.StorageManager;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

//@@author A0140036X
/**
 * LoadCommandTest tests the load command which changes the storage file used
 */

public class LoadCommandTest extends StorageTest {

    //@@author A0140036X
    /**
     * Tests loading of non existent file.
     */
    @Test
    public void load_nonExistentFile() {
        SaveAppLocation state = new SaveAppLocation(this);
        state.saveState();
        assertLoad(getNewTestStorageFileName(), new ReadOnlyTask[0]);
        state.resumeState();
    }

    //@@author A0140036X
    /**
     * Tests loading of file.
     * 1. Save current data location
     * 2. Generate tasks
     * 3. Create new storage file
     * 4. Save tasks into storage file
     * 5. Assert load tasks
     * 6. Assert clear tasks
     * 7. Assert add task
     * Resumes with saved location.
     * This test assumes if 5, 6, 7 is successful task manager has been loaded and linked to UI
     * and commands will work on the updated data.
     * @author A0140036X
     */
    @Test
    public void load_createTemporaryTaskManagerAndLoad() {
        SaveAppLocation state = new SaveAppLocation(this);
        state.saveState();

        TaskManager tempTaskManager = new TaskManager();
        TestTask[] testTasks = td.getGeneratedTasks(10);
        String testTaskManagerFilePath = getNewTestStorageFileName();

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
        assertAdd(testTasks);
        assertClear();

        state.resumeState();
    }

}

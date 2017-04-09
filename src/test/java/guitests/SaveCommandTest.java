package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

import savvytodo.commons.exceptions.DataConversionException;
import savvytodo.logic.commands.SaveCommand;
import savvytodo.model.ReadOnlyTaskManager;
import savvytodo.model.task.Task;
import savvytodo.storage.TaskManagerStorage;
import savvytodo.storage.XmlTaskManagerStorage;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

//@@author A0140036X
/**
 * Save command tests the save command which changes the storage file used
 */

public class SaveCommandTest extends StorageTest {

    //@@author A0140036X
    /**
     * Tests saving of new file
     */
    @Test
    public void save_nonExistentFile() {
        Task[] originalTasks = eventTaskListPanel.getListView().getItems().toArray(new Task[0]);
        if (originalTasks.length == 0) {
            assertAddMany(new TestTask[] {});
            originalTasks = eventTaskListPanel.getListView().getItems().toArray(new Task[0]);
        }
        assertTrue(originalTasks.length > 0);

        SaveAppLocation state = new SaveAppLocation(this);
        state.saveState();

        String targetFilePath = getNewTestStorageFileName();

        commandBox.runCommand(SaveCommand.getSaveCommand(targetFilePath));

        // test ui logic update
        assertEquals(statusBarHandle.getSaveLocationText(), targetFilePath);
        assertResultMessage(SaveCommand.getSaveSuccessMessage(targetFilePath));

        TaskManagerStorage tmStorage = new XmlTaskManagerStorage(targetFilePath);
        Optional<ReadOnlyTaskManager> read;
        try {
            read = tmStorage.readTaskManager();
            assertTrue(read.isPresent());
            Task[] savedTasks = read.get().getTaskList().toArray(new Task[0]);

            assertTrue(Task.areTasksSame(savedTasks, originalTasks));
            runClearCommand();
            assertAdd(new TestTask[] {});
            TestUtil.printTasks(originalTasks);
            assertLoad(state.getSavedLocation(), originalTasks);
        } catch (DataConversionException | IOException e) {
            assertTrue(false);
        }

        state.resumeState();
    }
}

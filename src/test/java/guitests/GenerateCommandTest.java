package guitests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import savvytodo.commons.exceptions.DataConversionException;
import savvytodo.logic.commands.GenerateCommand;
import savvytodo.model.ReadOnlyTaskManager;
import savvytodo.model.task.Task;
import savvytodo.model.util.MoreSampleDataUtil;
import savvytodo.storage.TaskManagerStorage;
import savvytodo.storage.XmlTaskManagerStorage;

//@@author A0140036X
/**
 * GenerateCommandTest tests the generate command which creates a task manager with many data.
 */

public class GenerateCommandTest extends StorageTest {
    //@@author A0140036X
    /**
     * Tests generation of file.
     * @author A0140036X
     */
    @Test
    public void generate_createTemporaryTaskManagerAndCheck() {
        SaveAppLocation state = new SaveAppLocation(this);
        state.saveState();

        ReadOnlyTaskManager taskManager = new MoreSampleDataUtil().getSampleTaskManager();

        Task[] generatedTasks = taskManager.getTaskList().toArray(new Task[]{});

        String testTaskManagerFilePath = getNewTestStorageFileName();

        commandBox.enterCommand(GenerateCommand.getCommand(testTaskManagerFilePath));

        sleep(600);
        commandBox.pressEnter();

        TaskManagerStorage storage = new XmlTaskManagerStorage(testTaskManagerFilePath);

        Task[] storedTasks = null;
        try {
            storedTasks = storage.readTaskManager().get().getTaskList().toArray(new Task[]{});
        } catch (DataConversionException | IOException e) {
            assertTrue(false);
        }

        assertTrue(Task.areTasksSame(generatedTasks, storedTasks));

        state.resumeState();
    }

}

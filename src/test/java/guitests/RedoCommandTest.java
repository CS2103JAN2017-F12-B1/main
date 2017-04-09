package guitests;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import savvytodo.model.task.Recurrence;
import savvytodo.testutil.TaskBuilder;
import savvytodo.testutil.TestTask;

//@@author A0124863A
public class RedoCommandTest extends TaskManagerGuiTest {

    @Test
    public void redo() throws Exception {

        String detailsToEdit = "Project 1 p/high d/2pm l/NUS mall c/Meeting";
        int taskManagerIndex = 2;

        TestTask editedTask = new TaskBuilder().withName("Project 1").withPriority("high").withDescription("2pm")
                .withLocation("NUS mall").withCategories("Meeting").withDateTime("02/03/2017 1400", "03/03/2017 1400")
                .withRecurrence(Recurrence.DEFAULT_VALUES).withStatus(false).build();

        commandBox.runCommand("edit " + taskManagerIndex + " " + detailsToEdit);
        commandBox.runCommand("undo");

        assertRedoSuccess(editedTask);

    }

    private void assertRedoSuccess(TestTask editedTask) {
        commandBox.runCommand("redo");

        TaskCardHandle editedTaskCard = eventTaskListPanel.navigateToTask(editedTask.getName().name);
        assertMatching(editedTask, editedTaskCard);

    }
}

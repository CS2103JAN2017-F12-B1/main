package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import savvytodo.model.task.Status;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

//@@author A0124863A
public class UndoCommandTest extends TaskManagerGuiTest {

    private TestTask[] currentList = td.getTypicalTasks();

    @Test
    public void undoAfterAdd() {

        TestTask taskToAdd = td.discussion;
        commandBox.runCommand(taskToAdd.getAddCommand());
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        int index = currentList.length;
        assertUndoAfterAddSuccess(index, currentList);

    }

    private void assertUndoAfterAddSuccess(int index, TestTask... currentList) {

        commandBox.runCommand("undo");
        TestTask[] expectedList = TestUtil.removeTaskFromList(currentList, index);
        assertTrue(taskListPanel.isListMatching(expectedList));

    }

    @Test
    public void undoAfterEdit() throws Exception {

        String detailsToEdit = "Project 1 p/high d/2pm l/NUS mall c/Meeting";
        int taskManagerIndex = 2;

        TestTask task = currentList[taskManagerIndex - 1];
        commandBox.runCommand("edit " + taskManagerIndex + " " + detailsToEdit);
        assertUndoAfterEditSuccess(task);

    }

    private void assertUndoAfterEditSuccess(TestTask task) {

        commandBox.runCommand("undo");
        TaskCardHandle taskCard = taskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);

    }

    @Test
    public void undoAfterMark() {

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        commandBox.runCommand("mark " + targetIndex);

        assertUndoAfterMarkSuccess(task);
    }

    private void assertUndoAfterMarkSuccess(TestTask task) {

        commandBox.runCommand("undo");
        TaskCardHandle taskCard = taskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);

    }

    @Test
    public void undoAfterUnmark() {

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        task.setCompleted(new Status(true));
        commandBox.runCommand("mark " + targetIndex);
        commandBox.runCommand("unmark " + targetIndex);

        assertUndoAfterUnmarkSuccess(task);
    }

    private void assertUndoAfterUnmarkSuccess(TestTask task) {

        commandBox.runCommand("undo");
        TaskCardHandle taskCard = taskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);

    }


}

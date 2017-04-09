

package guitests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import savvytodo.model.task.ReadOnlyTask;
//@@author A0147827U
public class SelectCommandTest extends TaskManagerGuiTest {

    @Test
    public void selectEventTask_nonEmptyList() {

        assertEventSelectionInvalid(10); // invalid index
        assertNoEventTaskSelected();

        assertEventSelectionSuccess(1); // first task in the list
        int taskCount = td.getTypicalTasks().length;
        assertEventSelectionSuccess(taskCount); // last task in the list
        int middleIndex = taskCount / 2;
        assertEventSelectionSuccess(middleIndex); // a task in the middle of the list

        assertEventSelectionInvalid(taskCount + 1); // invalid index
        assertEventTaskSelected(middleIndex); // assert previous selection remains

    }

    @Test
    public void selectFloatingTask_nonEmptyList() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertFloatingSelectionInvalid(5);
        assertNoFloatingTaskSelected();
        //add some floating tasks for selection
        commandBox.runCommand(td.floating1.getAddCommand());
        commandBox.runCommand(td.floating2.getAddCommand());
        assertFloatingListSize(2);
        assertFloatingSelectionSuccess(1);
        assertFloatingSelectionInvalid(5);
    }

    @Test
    public void selectTask_emptyList() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertEventSelectionInvalid(1); //invalid index
    }

    private void assertEventSelectionInvalid(int index) {
        commandBox.runCommand("select " + index);
        assertResultMessage("The task index provided is invalid");
    }
    private void assertFloatingSelectionInvalid(int index) {
        commandBox.runCommand("select f" + index);
        assertResultMessage("The task index provided is invalid");
    }
    private void assertEventSelectionSuccess(int index) {
        commandBox.runCommand("select " + index);
        assertResultMessage("Selected task: " + index);
        assertEventTaskSelected(index);
    }
    private void assertFloatingSelectionSuccess(int index) {
        commandBox.runCommand("select f" + index);
        assertResultMessage("Selected task: " + index);
        assertFloatingTaskSelected(index);
    }

    private void assertEventTaskSelected(int index) {
        assertEquals(eventTaskListPanel.getSelectedTasks().size(), 1);
        ReadOnlyTask selectedTask = eventTaskListPanel.getSelectedTasks().get(0);
        assertEquals(eventTaskListPanel.getTask(index - 1), selectedTask);
    }
    private void assertFloatingTaskSelected(int index) {
        assertEquals(floatingTaskListPanel.getSelectedTasks().size(), 1);
        ReadOnlyTask selectedTask = floatingTaskListPanel.getSelectedTasks().get(0);
        assertEquals(floatingTaskListPanel.getTask(index - 1), selectedTask);
    }
    private void assertNoEventTaskSelected() {
        assertEquals(eventTaskListPanel.getSelectedTasks().size(), 0);
    }
    
    private void assertNoFloatingTaskSelected() {
        assertEquals(floatingTaskListPanel.getSelectedTasks().size(), 0);
    }

}

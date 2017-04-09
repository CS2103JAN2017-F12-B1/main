package guitests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import savvytodo.logic.commands.UndoCommand;
import savvytodo.model.task.Status;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

//@@author A0124863A
public class UndoCommandTest extends TaskManagerGuiTest {

    @Test
    public void undoAfterDelete() {
        TestTask[] currentList = td.getTypicalTasks();

        int targetIndex = 2;
        commandBox.runCommand("delete " + targetIndex);
        assertUndoAfterDeleteSuccess(currentList);

    }


    private void assertUndoAfterDeleteSuccess(TestTask... currentList) {
        commandBox.runCommand("undo");

        assertTrue(eventTaskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
    }
    

    @Test
    public void undoAfterAdd() {
        TestTask[] currentList = td.getTypicalTasks();

        TestTask taskToAdd = td.discussion;
        commandBox.runCommand(taskToAdd.getAddCommand());
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        int index = currentList.length;
        assertUndoAfterAddSuccess(index, currentList);

    }

    private void assertUndoAfterAddSuccess(int index, TestTask... currentList) {
        commandBox.runCommand("undo");
        TestTask[] expectedList = TestUtil.removeTaskFromList(currentList, index);
        assertTrue(eventTaskListPanel.isListMatching(expectedList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);


    }

    @Test
    public void undoAfterEdit() throws Exception {
        TestTask[] currentList = td.getTypicalTasks();

        String detailsToEdit = "Project 1 p/high d/2pm l/NUS mall c/Meeting";
        int taskManagerIndex = 2;

        TestTask task = currentList[taskManagerIndex - 1];
        commandBox.runCommand("edit " + taskManagerIndex + " " + detailsToEdit);
        assertUndoAfterEditSuccess(task);

    }

    private void assertUndoAfterEditSuccess(TestTask task) {
        commandBox.runCommand("undo");
        TaskCardHandle taskCard = eventTaskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

    }

    @Test
    public void undoAfterMark() {
        TestTask[] currentList = td.getTypicalTasks();

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        commandBox.runCommand("mark " + targetIndex);

        assertUndoAfterMarkSuccess(task);
    }

    private void assertUndoAfterMarkSuccess(TestTask task) {

        commandBox.runCommand("undo");
        TaskCardHandle taskCard = eventTaskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

    }

    @Test
    public void undoAfterUnmark() {
        TestTask[] currentList = td.getTypicalTasks();

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        task.setCompleted(new Status(true));
        commandBox.runCommand("mark " + targetIndex);
        commandBox.runCommand("unmark " + targetIndex);

        assertUndoAfterUnmarkSuccess(task);
    }

    private void assertUndoAfterUnmarkSuccess(TestTask task) {

        commandBox.runCommand("undo");
        TaskCardHandle taskCard = eventTaskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

    }

    @Test
    public void undoAfterClear() {
        TestTask[] currentList = td.getTypicalTasks();

        commandBox.runCommand("clear");
        assertUndoAfterClearSuccess(currentList);
    }

    private void assertUndoAfterClearSuccess(TestTask... currentList) {
        commandBox.runCommand("undo");
        assertTrue(eventTaskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

    }
    
    

    @Test
    public void undoMarkMultiple() {
        TestTask[] currentList = td.getTypicalTasks();

        LinkedList<Integer> targetIndices = new LinkedList<Integer>();
        LinkedList<TestTask> markedTasks = new LinkedList<TestTask>();

        //mark the first in the list
        int targetIndex = 1;
        TestTask markedTask = currentList[targetIndex - 1];
        targetIndices.add(targetIndex);
        markedTasks.add(markedTask);

        //mark the last in the list
        targetIndex = currentList.length;
        TestTask markedTask2 = currentList[targetIndex - 1];
        targetIndices.add(targetIndex);
        markedTasks.add(markedTask2);

        //mark from the middle of the list
        targetIndex = currentList.length / 2;
        TestTask markedTask3 = currentList[targetIndex - 1];
        targetIndices.add(targetIndex);
        markedTasks.add(markedTask3);
        
        StringBuilder indices = new StringBuilder();

        for (Integer markedTaskIndex : targetIndices) {
            indices.append(markedTaskIndex  + " ");
        }
        commandBox.runCommand("mark " + indices);


        assertUndoMarkMultipleSuccess(markedTasks);

    }


    private void assertUndoMarkMultipleSuccess(LinkedList<TestTask> markedTasks) {

        commandBox.runCommand("undo");


        for (TestTask markedTask : markedTasks) {
            // confirm the new card contains the right data
            TaskCardHandle editedCard = eventTaskListPanel.navigateToTask(markedTask.getName().name);
            assertMatching(markedTask, editedCard);

        }
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);

    }
    
    @Test
    public void undoUnmarkMultiple() {
        TestTask[] currentList = td.getTypicalTasks();

        LinkedList<Integer> targetIndices = new LinkedList<Integer>();
        LinkedList<TestTask> unmarkedTasks = new LinkedList<TestTask>();

        //mark the first in the list
        int targetIndex = 1;
        TestTask unmarkedTask = currentList[targetIndex - 1];
        unmarkedTask.setCompleted(new Status(true));
        targetIndices.add(targetIndex);
        unmarkedTasks.add(unmarkedTask);

        //mark the last in the list
        targetIndex = currentList.length;
        TestTask unmarkedTask2 = currentList[targetIndex - 1];
        unmarkedTask2.setCompleted(new Status(true));
        targetIndices.add(targetIndex);
        unmarkedTasks.add(unmarkedTask2);

        //mark from the middle of the list
        targetIndex = currentList.length / 2;
        TestTask unmarkedTask3 = currentList[targetIndex - 1];
        unmarkedTask3.setCompleted(new Status(true));
        targetIndices.add(targetIndex);
        unmarkedTasks.add(unmarkedTask3);
        
        StringBuilder indices = new StringBuilder();

        for (Integer unmarkedTaskIndex : targetIndices) {
            indices.append(unmarkedTaskIndex  + " ");
        }
        commandBox.runCommand("mark " + indices);
        commandBox.runCommand("unmark " + indices);

        assertUndoUnmarkMultipleSuccess(unmarkedTasks);

    }


    private void assertUndoUnmarkMultipleSuccess(LinkedList<TestTask> unmarkedTasks) {

        commandBox.runCommand("undo");

        for (TestTask unmarkedTask : unmarkedTasks) {
            // confirm the new card contains the right data
            TaskCardHandle editedCard = eventTaskListPanel.navigateToTask(unmarkedTask.getName().name);
            assertMatching(unmarkedTask, editedCard);


        }
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);


    }
    
    @Test
    public void undoFailure() {
        
        commandBox.runCommand("undo");
        assertUndoFailure();

    }

    private void assertUndoFailure() {
        assertResultMessage(UndoCommand.MESSAGE_FAILURE + ", empty stack");

    }


}

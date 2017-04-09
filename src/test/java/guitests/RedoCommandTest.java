package guitests;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import savvytodo.logic.commands.RedoCommand;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Status;
import savvytodo.testutil.TaskBuilder;
import savvytodo.testutil.TestTask;
import savvytodo.testutil.TestUtil;

//@@author A0124863A
public class RedoCommandTest extends TaskManagerGuiTest {

    @Test
    public void redoAfterUndoAfterEdit() throws Exception {

        String detailsToEdit = "Project 1 p/high d/2pm l/NUS mall c/Meeting";
        int taskManagerIndex = 2;

        TestTask editedTask = new TaskBuilder().withName("Project 1").withPriority("high").withDescription("2pm")
                .withLocation("NUS mall").withCategories("Meeting").withDateTime("02/03/2017 1400", "03/03/2017 1400")
                .withRecurrence(Recurrence.DEFAULT_VALUES).withStatus(false).build();

        commandBox.runCommand("edit " + taskManagerIndex + " " + detailsToEdit);
        commandBox.runCommand("undo");

        assertRedoAfterUndoAfterEditSuccess(editedTask);

    }

    private void assertRedoAfterUndoAfterEditSuccess(TestTask editedTask) {
        commandBox.runCommand("redo");

        TaskCardHandle editedTaskCard = eventTaskListPanel.navigateToTask(editedTask.getName().name);
        assertMatching(editedTask, editedTaskCard);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);


    }
    
    @Test
    public void redoAfterUndoAfterDelete() {
        TestTask[] currentList = td.getTypicalTasks();
        
        int targetIndex = 2;
        commandBox.runCommand("delete " + targetIndex);
        commandBox.runCommand("undo");

        assertRedoAfterUndoAfterDeleteSuccess(targetIndex, currentList);
        


    }


    private void assertRedoAfterUndoAfterDeleteSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        TestTask taskToDelete = currentList[targetIndexOneIndexed - 1]; // -1 as array uses zero indexing
        TestTask[] expectedRemainder = TestUtil.removeTaskFromList(currentList, targetIndexOneIndexed);
        
        commandBox.runCommand("redo");

        assertTrue(eventTaskListPanel.isListMatching(expectedRemainder));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }
    
    @Test
    public void redoAfterUndoAfterAdd() {
        TestTask[] currentList = td.getTypicalTasks();

        TestTask taskToAdd = td.discussion;
        commandBox.runCommand(taskToAdd.getAddCommand());
        commandBox.runCommand("undo");
        
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertRedoAfterUndoAfterAddSuccess(currentList);

    }

    private void assertRedoAfterUndoAfterAddSuccess(TestTask... currentList) {

        commandBox.runCommand("redo");
        //TestTask[] expectedList = TestUtil.removeTaskFromList(currentList, index);
        assertTrue(eventTaskListPanel.isListMatching(currentList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }


    @Test
    public void redoAfterUndoAfterMark() {
        TestTask[] currentList = td.getTypicalTasks();

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        task.setCompleted(new Status(true));
        commandBox.runCommand("mark " + targetIndex);
        commandBox.runCommand("undo");


        assertRedoAfterUndoAfterMarkSuccess(task);
    }

    private void assertRedoAfterUndoAfterMarkSuccess(TestTask task) {

        commandBox.runCommand("redo");
        TaskCardHandle taskCard = eventTaskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }

    @Test
    public void redoAfterUndoAfterUnmark() {
        TestTask[] currentList = td.getTypicalTasks();

        int targetIndex = 1;
        TestTask task = currentList[targetIndex - 1];
        task.setCompleted(new Status(false));
        commandBox.runCommand("mark " + targetIndex);
        commandBox.runCommand("unmark " + targetIndex);
        commandBox.runCommand("undo");

        assertRedoAfterUndoAfterUnmarkSuccess(task);
    }

    private void assertRedoAfterUndoAfterUnmarkSuccess(TestTask task) {

        commandBox.runCommand("redo");
        TaskCardHandle taskCard = eventTaskListPanel.navigateToTask(task.getName().name);
        assertMatching(task, taskCard);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }

    @Test
    public void redoAfterUndoAfterClear() {

        commandBox.runCommand("clear");
        commandBox.runCommand("undo");

        assertRedoUndoAfterClearSuccess();
    }

    private void assertRedoUndoAfterClearSuccess() {
        commandBox.runCommand("redo");
        assertListSize(0);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }
    
    @Test
    public void redoAfterUndoAfterMarkMultiple() {
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
        commandBox.runCommand("undo");

        assertRedoAfterUndoAfterMarkMultipleSuccess(unmarkedTasks);

    }


    private void assertRedoAfterUndoAfterMarkMultipleSuccess(LinkedList<TestTask> unmarkedTasks) {
       
        commandBox.runCommand("redo");

        for (TestTask unmarkedTask : unmarkedTasks) {
            // confirm the new card contains the right data
            TaskCardHandle editedCard = eventTaskListPanel.navigateToTask(unmarkedTask.getName().name);
            assertMatching(unmarkedTask, editedCard);


        }
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);


    }

    
    
    

    @Test
    public void redoAfterUndoAfterUnmarkMultiple() {
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
        commandBox.runCommand("unmark " + indices);
        commandBox.runCommand("undo");


        assertRedoAfterUndoAfterUnmarkMultipleSuccess(markedTasks);

    }


    private void assertRedoAfterUndoAfterUnmarkMultipleSuccess(LinkedList<TestTask> markedTasks) {

        commandBox.runCommand("redo");


        for (TestTask markedTask : markedTasks) {
            // confirm the new card contains the right data
            TaskCardHandle editedCard = eventTaskListPanel.navigateToTask(markedTask.getName().name);
            assertMatching(markedTask, editedCard);

        }
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

    }
    
    @Test
    public void redoFailure() {

        TestTask taskToAdd = td.discussion;
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertRedoFailure();

        commandBox.runCommand("clear");
        assertRedoFailure();
        
        commandBox.runCommand("redo");
        assertRedoFailure();
        

    }

    private void assertRedoFailure() {
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_FAILURE + ", empty stack");

    }
}

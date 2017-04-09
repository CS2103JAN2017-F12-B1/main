package savvytodo.model.operations;

import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.task.Task;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;
import savvytodo.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0124863A
/**
 * @author A0124863A
 * Undo an add operation by deleting the added task
 */
public class UndoAddOperation extends Operation {
    private Task task;

    public UndoAddOperation(Task task) {
        this.task = task;
    }
    @Override
    public void execute() throws CommandException {
        assert taskManager != null;
        try {
            taskManager.removeTask(task);
        } catch (TaskNotFoundException e) {
            assert false : "The target task cannot be missing";
        } catch (DuplicateTaskException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Operation reverse() {
        return new UndoDeleteOperation(task);
    }



}

package savvytodo.model.operations;

import savvytodo.model.ReadOnlyTaskManager;
import savvytodo.model.TaskManager;

//@@author A0124863A
/**
 * @author A0124863A
 * Undo an clear operation by restoring the original
 */

public class UndoClearOperation extends Operation {
    private TaskManager currTaskManager;
    private TaskManager newTaskManager;
    public UndoClearOperation(ReadOnlyTaskManager currTaskManager, ReadOnlyTaskManager newTaskManager) {
        this.currTaskManager = new TaskManager(currTaskManager);
        this.newTaskManager = new TaskManager(newTaskManager);
    }


    @Override
    public void execute() {
        assert taskManager != null;
        taskManager.resetData(currTaskManager);
    }

    @Override
    public Operation reverse() {
        return new UndoClearOperation(newTaskManager, currTaskManager);
    }

}

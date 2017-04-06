package savvytodo.commons.events.ui;

import savvytodo.commons.events.BaseEvent;
import savvytodo.model.task.TaskType;

/**
 * Indicates a request to jump to the list of tasks
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;
    public final TaskType targetTaskList;

    public JumpToListRequestEvent(TaskType taskType, int targetIndex) {
        this.targetIndex = targetIndex;
        this.targetTaskList = taskType;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}

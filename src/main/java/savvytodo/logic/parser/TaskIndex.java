package savvytodo.logic.parser;

import savvytodo.model.task.TaskType;

//@@author A0147827U
/**
 * Stores the index of the task in its corresponding list view and its type (FLOATING, EVENT, etc)
 * @author jingloon
 *
 */
public class TaskIndex {

    private TaskType taskType;
    private int index;

    public TaskIndex(TaskType type, int index) {
        this.taskType = type;
        this.index = index;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getIndex() {
        return index;
    }
}

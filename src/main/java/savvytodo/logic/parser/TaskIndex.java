package savvytodo.logic.parser;
//@@author A0147827U
/**
 * Stores the index of the task in its corresponding list view and its type (FLOATING, EVENT, etc)
 * @author jingloon
 *
 */
public class TaskIndex {
    public enum TaskType {
        FLOATING, EVENT
    }
    
    private TaskType taskType;
    private int index;
    
    public TaskIndex(TaskType type, int index){
        this.taskType = type;
        this.index = index;
    }
    
    public TaskType getTaskType(){
        return taskType;
    }
    
    public int getIndex(){
        return index;
    }
}

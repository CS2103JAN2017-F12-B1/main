package savvytodo.model.operations;

import java.util.EmptyStackException;
import java.util.Stack;


//author @@A0124863A
/**
 * @author A0124863A
 * Class that stores the undo and redo commands
 */
public class UndoRedoOperationCentre {
    private Stack<Operation> undoStack = new Stack<Operation>();
    private Stack<Operation> redoStack = new Stack<Operation>();

    public void storeUndoOperation(Operation undoOperation) {
        undoStack.push(undoOperation);
    }

    public void storeRedoOperation(Operation redoOperation) {
        redoStack.push(redoOperation);
    }

    public void resetRedo() {
        redoStack.clear();
    }

    public Operation getUndoOperation() throws EmptyStackException {
        Operation undo = undoStack.pop();
        if (undo.getClass().isAssignableFrom(UndoMarkOperation.class)) {
            return undo;
        } else {
            Operation redo = undo.reverse();
            redoStack.push(redo);
            return undo;
        }
    }

    public Operation getRedoOperation() throws EmptyStackException {
        Operation redo = redoStack.pop();
        if (redo.getClass().isAssignableFrom(UndoMarkOperation.class)) {
            return redo;
        } else {
            Operation undo = redo.reverse();
            undoStack.push(undo);
            return redo;
        }
    }

}

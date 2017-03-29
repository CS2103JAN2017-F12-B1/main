package savvytodo.model;

import java.util.EmptyStackException;
import java.util.Stack;

import savvytodo.model.operations.UndoOperation;

//author @@A0124863A
/**
 * @author A0124863A
 * Class that stores the undo and redo commands
 */
public class UndoRedoManager {
    private Stack<UndoOperation> undoStack = new Stack<UndoOperation>();
    private Stack<UndoOperation> redoStack = new Stack<UndoOperation>();

    public void storeUndoCommand(UndoOperation undoCommand) {
        undoStack.push(undoCommand);
    }

    public void resetRedo() {
        redoStack.clear();
    }

    public UndoOperation getUndoCommand() throws EmptyStackException {
        UndoOperation undo = undoStack.pop();
        UndoOperation redo = undo.reverseUndo();
        redoStack.push(redo);
        return undo;
    }

    public UndoOperation getRedoCommand() throws EmptyStackException {
        UndoOperation redo = redoStack.pop();
        UndoOperation undo = redo.reverseUndo();
        undoStack.push(undo);
        return redo;
    }

}

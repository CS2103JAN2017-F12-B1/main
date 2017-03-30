package savvytodo.model.operations;

import java.util.EmptyStackException;

import savvytodo.logic.commands.exceptions.CommandException;

//@@author A0124863A
/**
* @author A0124863A
* Undo a mark operation by restoring the marked or unmarked task to its original
*/
public class UndoMarkOperation extends Operation {
    private int numToUnmark;
    private UndoRedoOperationCentre undoRedoOpCentre;


    public UndoMarkOperation(int numToUnmark) {
        this.numToUnmark = numToUnmark;
        this.setTaskManager(taskManager);
    }

    public void setUndoRedoOperationCentre(UndoRedoOperationCentre undoRedoOpCentre) {
        this.undoRedoOpCentre = undoRedoOpCentre;
    }

    @Override
    public void execute() throws CommandException {
        assert taskManager != null;
        assert undoRedoOpCentre != null;

        try {
            for (int i = 0; i < numToUnmark; i++) {
                Operation undo = undoRedoOpCentre.getUndoOperation();
                assert undo.getClass().isAssignableFrom(UndoEditOperation.class);
                undo.setTaskManager(taskManager);
                undo.execute();
            }
        } catch (EmptyStackException e) {
            throw new CommandException(e.getMessage());
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }

        undoRedoOpCentre.storeRedoOperation(this.reverse());

    }

    @Override
    public Operation reverse() {
        return new RedoMarkOperation(numToUnmark);
    }

}

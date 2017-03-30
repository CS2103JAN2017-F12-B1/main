package savvytodo.model.operations;

import java.util.EmptyStackException;

import savvytodo.logic.commands.exceptions.CommandException;

//@@author A0124863A
/**
* @author A0124863A
* Redo a mark operation by restoring the marked or unmarked task to its original
*/
public class RedoMarkOperation extends Operation {
    private int numToUnmark;
    private UndoRedoOperationCentre undoRedoOpCentre;


    public RedoMarkOperation(int numToUnmark) {
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
                Operation redo = undoRedoOpCentre.getRedoOperation();
                assert redo.getClass().isAssignableFrom(UndoEditOperation.class) == true;
                redo.setTaskManager(taskManager);
                redo.execute();
            }
        } catch (EmptyStackException e) {
            throw new CommandException(e.getMessage());
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }

        undoRedoOpCentre.storeUndoOperation(this.reverse());

    }

    @Override
    public Operation reverse() {
        return new UndoMarkOperation(numToUnmark);
    }

}

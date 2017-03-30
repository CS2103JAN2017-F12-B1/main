package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCodeCombination;
import savvytodo.logic.commands.ClearCommand;
import savvytodo.logic.commands.ListCommand;
import savvytodo.logic.commands.RedoCommand;
import savvytodo.logic.commands.UndoCommand;
import savvytodo.ui.hotkeys.HotKeysCombinations;

//@@author A0147827U
/**
 * HotKeysTest simulates the pressing of keyboard combinations. Test cases are passed if results are identical to
 * the result by pressing the actual keyboard keys.
 * Assertions of success cases do not concern with regards to handling of actual tasks.
 *
 * @author jingloon
 *
 */
public class HotKeyTest extends TaskManagerGuiTest {
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#helpWindowRoot";

    @Test
    public void triggerHelpShortcut() {
        KeyCodeCombination helpKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_HELP;
        new GuiRobot().push(helpKeys);
        assertHelpWindowOpen();
    }

    @Test
    public void triggerListShortcut() {
        KeyCodeCombination listKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_LIST;
        new GuiRobot().push(listKeys);
        assertListSuccess();
    }

    @Test
    public void triggerClearShortcut() {
        KeyCodeCombination clearKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_CLEAR;
        new GuiRobot().push(clearKeys);
        assertClearSuccess();
    }

    @Test
    public void triggerUndoShortcut() {
        KeyCodeCombination undoKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_UNDO;
        new GuiRobot().push(undoKeys);
        assertUndoSuccess();
    }

    @Test
    public void triggerRedoShortcut() {
        KeyCodeCombination redoKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_REDO;
        new GuiRobot().push(redoKeys);
        assertRedoSuccess();
    }


    private void assertHelpWindowOpen() {
        assertTrue(new GuiRobot().lookup(HELP_WINDOW_ROOT_FIELD_ID).tryQuery().isPresent());
    }

    private void assertListSuccess() {
        //listed successfully (displayed on result box)
        assertResultMessage(ListCommand.LIST_ALL_SUCCESS);
    }

    private void assertClearSuccess() {
        assertListSize(0);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
    }

    private void assertUndoSuccess() {
        //nothing to undo in test
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);
    }

    private void assertRedoSuccess() {
        //nothing to redo in test
        assertResultMessage(RedoCommand.MESSAGE_FAILURE);
    }

}

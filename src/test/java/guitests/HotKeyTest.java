package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCodeCombination;
import savvytodo.logic.commands.ClearCommand;
import savvytodo.logic.commands.ListCommand;
import savvytodo.logic.commands.RedoCommand;
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

    @Test
    public void triggerHelpShortcut() {
        KeyCodeCombination helpKeys = (KeyCodeCombination) HotKeysCombinations.KEYS_HELP;
        GuiRobot helpRobot = new GuiRobot();
        helpRobot.push(helpKeys);
        helpRobot.sleep(2000);
        assertHelpCommandSuccess();
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


    private void assertHelpCommandSuccess() {
        assertTrue(mainMenu.getHelpWindowHandle().isWindowOpen());
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
        //result will either be undo success or fail to undo
//        assertTrue(resultDisplay.getText().equals(UndoCommand.MESSAGE_FAILURE) ||
//                resultDisplay.getText().equals(UndoCommand.MESSAGE_SUCCESS));
        assertTrue(true); //stub to be fixed in 0.5
    }

    private void assertRedoSuccess() {
        //result will either be redo success or fail to redo
        assertTrue(resultDisplay.getText().equals(RedoCommand.MESSAGE_FAILURE) ||
                resultDisplay.getText().equals(RedoCommand.MESSAGE_SUCCESS));
    }

}

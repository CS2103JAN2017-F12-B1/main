package savvytodo.testutil;

import guitests.TaskManagerGuiTest;

//@@author A0140036X
/**
 * UI state manager for gui tests.
 *
 * Some GUI tests are independent from the main flow (eg. LoadCommandTest).
 * This class provides a mechanism for such tests
 * to save relevant state information that is to be resumed at a later time.
 */
public abstract class UiState {
    private TaskManagerGuiTest guiTest;
    private boolean stateSaved = false;

    //@@author A0140036X
    /**
     * Sets gui test to manage.
     */
    public UiState(TaskManagerGuiTest test) {
        this.setGuiTest(test);
    }

    //@@author A0140036X
    /**
     * Will be executed when {@link #saveState()} is called.
     */
    public abstract void onSaveState();

    //@@author A0140036X
    /**
     * Will be executed when {@link #resumeState()} is called.
     */
    public abstract void onResumeState();

    //@@author A0140036X
    /**
     * Saves state of UI.
     */
    public void saveState() {
        onSaveState();
        stateSaved = true;
    }

    //@@author A0140036X
    /**
     * Resumes state if previous saved state exists.
     */
    public void resumeState() {
        if (stateSaved) {
            onResumeState();
        }
    }

    //@@author A0140036X
    /**
     * Returns gui test that state is managed
     */
    public TaskManagerGuiTest getGuiTest() {
        return guiTest;
    }

    //@@author A0140036X
    /**
     * Sets gui test that state is to be managed
     */
    public void setGuiTest(TaskManagerGuiTest guiTest) {
        this.guiTest = guiTest;
    }
}

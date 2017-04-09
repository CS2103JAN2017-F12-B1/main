package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import savvytodo.TestApp;

/**
 * Provides a handle for the main GUI.
 */
public class MainGuiHandle extends GuiHandle {

    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public TaskListPanelHandle getTaskListPanel() {
        return new TaskListPanelHandle(guiRobot, primaryStage, TaskListPanelHandle.FLOATING_TASK_LIST_VIEW_ID);
    }
    
    public TaskListPanelHandle getEventTaskListPanel() {
        return new TaskListPanelHandle(guiRobot, primaryStage, TaskListPanelHandle.EVENT_TASK_LIST_VIEW_ID);
    }

    public ResultDisplayHandle getResultDisplay() {
        return new ResultDisplayHandle(guiRobot, primaryStage);
    }

    public CommandBoxHandle getCommandBox() {
        return new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public MainMenuHandle getMainMenu() {
        return new MainMenuHandle(guiRobot, primaryStage);
    }

    public BrowserPanelHandle getBrowserPanel() {
        return new BrowserPanelHandle(guiRobot, primaryStage);
    }

    public AlertDialogHandle getAlertDialog(String title) {
        guiRobot.sleep(300);
        return new AlertDialogHandle(guiRobot, primaryStage, title);
    }

    public StatusBarFooterHandle getStatusBar() {
        return new StatusBarFooterHandle(guiRobot, primaryStage, stageTitle);
    }
}

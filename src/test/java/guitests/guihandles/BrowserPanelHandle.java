package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import savvytodo.TestApp;

/**
 * A handler for the BrowserPanel of the UI
 */
public class BrowserPanelHandle extends GuiHandle {

    private static final String BROWSER_ID = "#browserDisplay";

    public BrowserPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    /**
     * Clicks on the WebView.
     */
    public void clickOnTextView() {
        guiRobot.clickOn(BROWSER_ID);
    }

}

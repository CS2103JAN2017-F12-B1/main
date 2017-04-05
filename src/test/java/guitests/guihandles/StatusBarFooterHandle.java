package guitests.guihandles;

import org.controlsfx.control.StatusBar;

import guitests.GuiRobot;
import javafx.scene.Scene;
import javafx.stage.Stage;

//@@author A0140036X
/**
 * A handle for the StatusBarFooter ui.
 */
public class StatusBarFooterHandle extends GuiHandle {
    private static final String STATUS_BAR_SAVE_LOCATION_ID = "#saveLocationStatus";

    public StatusBarFooterHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    /**
     * Returns location of current save location show in UI.
     * @return location of current task manager file
     */
    public String getSaveLocationText() {
        Scene primaryScene = primaryStage.getScene();
        StatusBar saveLocation = (StatusBar) primaryScene.lookup(STATUS_BAR_SAVE_LOCATION_ID);
        return saveLocation.getText();
    }
}

package guitests.guihandles;

import org.controlsfx.control.StatusBar;

import guitests.GuiRobot;
import javafx.stage.Stage;

//@@author A0140036X
/**
 * A handle for the StatusBarFooter ui
 */
public class StatusBarFooterHandle extends GuiHandle {
    private static final String STATUS_BAR_SAVE_LOCATION_ID = "#saveLocationStatus";

    public StatusBarFooterHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    /**
     * 
     * @return location of current taskmanager file
     */
    public String getSaveLocationText(){
        StatusBar saveLocation = (StatusBar) primaryStage.getScene().lookup(STATUS_BAR_SAVE_LOCATION_ID);
        return saveLocation.getText();
    }
}
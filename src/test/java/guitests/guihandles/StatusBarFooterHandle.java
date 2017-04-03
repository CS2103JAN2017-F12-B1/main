package guitests;

import org.controlsfx.control.StatusBar;

import guitests.guihandles.GuiHandle;
import javafx.stage.Stage;

public class StatusBarHandle extends GuiHandle {
    private static final String STATUS_BAR_SAVE_LOCATION_ID = "#saveLocationStatus";

    public StatusBarHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public String getText(){
        StatusBar saveLocation = (StatusBar) primaryStage.getScene().lookup(STATUS_BAR_SAVE_LOCATION_ID);
        return saveLocation.getText();
    }
}
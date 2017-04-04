package savvytodo.ui;

import javafx.stage.Stage;
import savvytodo.commons.core.Config;
import savvytodo.logic.Logic;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

    //@@author A0140036X
    /** Set UI with new logic Note: Does not refresh the UI */
    void setLogic(Logic logic);

    //@@author A0140036X
    /** Set UI with new config. Note: Does not refresh the UI */
    void setConfig(Config config);
    
    //@@author A0140036X
    /** Links UI to underlying logic and config and updates UI */
    void refresh();
}

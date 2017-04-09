package savvytodo.ui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import savvytodo.commons.util.FxViewUtil;
import savvytodo.model.task.ReadOnlyTask;

//@@author A0140036X
/**
 * @author A0140036X The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    @FXML
    private GridPane content;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    /**
     * @param placeholder
     *            The AnchorPane where the BrowserPanel must be inserted
     */
    public BrowserPanel(AnchorPane placeholder) {
        super(FXML);
        placeholder.setOnKeyPressed(Event::consume);
        FxViewUtil.applyAnchorBoundaryParameters(content, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(content);
    }

    public void loadTaskPage(ReadOnlyTask task) {
        nameLabel.setText(task.getName().toString().toUpperCase());
        dateLabel.setText(task.getDateTime().toString());
        descriptionLabel.setText(task.getDescription().toString());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        content = null;
    }

}

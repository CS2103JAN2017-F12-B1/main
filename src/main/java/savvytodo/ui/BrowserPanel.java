package savvytodo.ui;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import savvytodo.commons.util.DateTimeUtil;
import savvytodo.commons.util.FxViewUtil;
import savvytodo.commons.util.StringUtil;
import savvytodo.model.task.ReadOnlyTask;

//@@author A0140016B
/**
 * @author A0140016B The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private AnchorPane browser;

    @FXML
    private TextArea browserDisplay;

    /**
     * @param placeholder
     *            The AnchorPane where the BrowserPanel must be inserted
     */
    public BrowserPanel(AnchorPane placeholder) {
        super(FXML);
        placeholder.setOnKeyPressed(Event::consume); // To prevent triggering
                                                     // events for typing inside
                                                     // the
                                                     // loaded Web page.
        browserDisplay.textProperty().bind(displayed);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(browserDisplay, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(browser);
    }

    public void loadTaskPage(ReadOnlyTask task) {
        displayed.setValue(build(task));
    }

    private String build(ReadOnlyTask task) {
        final StringBuilder builder = new StringBuilder();
        builder.append(task.getAsText());

        ArrayList<String> recurDates = DateTimeUtil.getRecurDates(task.getDateTime().startValue,
                task.getRecurrence().type.name(), task.getRecurrence().occurences);

        if (!recurDates.isEmpty()) {
            builder.append(StringUtil.SYSTEM_NEWLINE)
                   .append(StringUtil.SYSTEM_NEWLINE)
                   .append("Recurring dates (Start DateTime) are:")
                   .append(StringUtil.SYSTEM_NEWLINE)
                   .append(recurDates);
        }

        return builder.toString();
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

}
// @@author A0140016B

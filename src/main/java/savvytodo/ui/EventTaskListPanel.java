package savvytodo.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import savvytodo.commons.core.LogsCenter;
import savvytodo.commons.events.ui.TaskPanelSelectionChangedEvent;
import savvytodo.commons.util.FxViewUtil;
import savvytodo.model.task.ReadOnlyTask;
//@@author A0147827U
/**
 * Panel containing the list of event tasks.
 */
public class EventTaskListPanel extends UiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(EventTaskListPanel.class);
    private static final String FXML = "EventTaskListPanel.fxml";

    @FXML
    private ListView<ReadOnlyTask> eventTaskListView;

    public EventTaskListPanel(AnchorPane taskListPlaceholder, ObservableList<ReadOnlyTask> taskList) {
        super(FXML);
        setConnections(taskList);
        addToPlaceholder(taskListPlaceholder);
    }

    public void setConnections(ObservableList<ReadOnlyTask> taskList) {
        eventTaskListView.setItems(taskList);
        eventTaskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        placeHolderPane.getChildren().add(getRoot());
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventTaskListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new TaskPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            eventTaskListView.scrollTo(index);
            eventTaskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TaskCard(task, getIndex() + 1).getRoot());
            }
        }
    }

}

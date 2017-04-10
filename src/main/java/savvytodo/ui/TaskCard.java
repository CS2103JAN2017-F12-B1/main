package savvytodo.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import savvytodo.commons.util.StringUtil;
import savvytodo.logic.parser.CliSyntax;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Recurrence;
import savvytodo.model.task.Status;
import savvytodo.model.task.TaskType;

//@@author A0140016B
public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";

    private static final String LABEL_LOW = "L";
    private static final String LABEL_MEDIUM = "M";
    private static final String LABEL_HIGH = "H";
    private static final String LABEL_DONE = "✔ ";

    private static final String PRIORITY_LOW = "Low";
    private static final String PRIORITY_MEDIUM = "Medium";
    private static final String PRIORITY_HIGH = "High";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label status;
    @FXML
    private Label priority;
    @FXML
    private Label dateTime;
    @FXML
    private Label description;
    @FXML
    private FlowPane categories;
    @FXML
    private Circle circle;


    public TaskCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        name.setText(task.getName().name);
        setId(task, displayedIndex);
        description.setText(task.getDescription().value);
        dateTime.setText(getDateTimeRecur(task));
        setStatus(task);
        initCategories(task);
        setCircle(task);
    }

    //@@author A0147827U
    private void setId(ReadOnlyTask task, int displayedIndex) {
        if (task.getType().getType() == TaskType.FLOATING) {
            id.setText(CliSyntax.INDEX_FLOATING.toUpperCase() + displayedIndex + ". ");
        } else {
            id.setText(displayedIndex + ". ");
        }
    }
    //@@author
    //@@author A0140016B
    private void setStatus(ReadOnlyTask task) {
        if (task.isCompleted().value == Status.COMPLETED) {
            status.setText(LABEL_DONE + task.isCompleted().toString());
        }
    }

    private void setCircle(ReadOnlyTask task) {
        switch (task.getPriority().value) {
        case PRIORITY_HIGH:
            priority.setText(LABEL_HIGH);
            circle.setFill(Color.RED);
            break;
        case PRIORITY_MEDIUM:
            priority.setText(LABEL_MEDIUM);
            circle.setFill(Color.ORANGE);
            break;
        case PRIORITY_LOW:
            priority.setText(LABEL_LOW);
            circle.setFill(Color.GREENYELLOW);
            break;
        default:
            break;
        }
    }

    private String getDateTimeRecur(ReadOnlyTask task) {
        StringBuilder sb = new StringBuilder();
        if (task.getType().getType() == TaskType.FLOATING) {
            sb.append("-");
        } else if (task.getType().getType() == TaskType.DEADLINE) {
            sb.append("Due By: " + task.getDateTime().toString());
        } else {
            sb.append(task.getDateTime().toString());
            if (!task.getRecurrence().type.name().equalsIgnoreCase(Recurrence.Type.None.name())) {
                if (task.getRecurrence().occurences > 0) {
                    sb.append(StringUtil.WHITESPACE + "[" + task.getRecurrence().toString() + "]");
                } else {
                    sb.append(StringUtil.WHITESPACE + "[" + task.getRecurrence().type.name() + "]");
                }
            }
        }

        return sb.toString();
    }

    private void initCategories(ReadOnlyTask task) {
        task.getCategories().forEach(category -> categories.getChildren().add(new Label(category.categoryName)));
    }
}

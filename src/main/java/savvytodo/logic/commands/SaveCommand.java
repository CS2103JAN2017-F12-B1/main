package savvytodo.logic.commands;

import java.io.IOException;

import savvytodo.MainApp;
import savvytodo.commons.exceptions.DataConversionException;

//@@author A0140036X
/**
 * Saves the task manager in specified location.
 */
public class SaveCommand extends Command {

    public static final String COMMAND_FORMAT = "save %s";
    public static final String MESSAGE_SUCCESS_FORMAT = "Task manager has been saved at %s!";
    public static final String MESSAGE_FAILED_FORMAT = "Failed to save task manager at %s!";
    public static final String COMMAND_WORD = "save";

    private String filepath;

    public SaveCommand(String filepath) {
        this.filepath = filepath;
    }

    //@@author A0140036X
    /**
     * Saves file in specified filepath from command.
     */
    @Override
    public CommandResult execute() {
        boolean success = true;
        try {
            MainApp.getRunningInstance().saveTaskManagerToFile(filepath);
        } catch (IOException | DataConversionException e) {
            success = false;
        }
        String message = success ? getSaveSuccessMessage(filepath) : getSaveFailureMessage(filepath);
        return new CommandResult(message);
    }

    //@@author A0140036X
    /**
     * Returns user command for saving task manager to filepath.
     */
    public static String getSaveCommand(String filePath) {
        return String.format(COMMAND_FORMAT, filePath);
    }

    //@@author A0140036X
    /**
     * Returns message if operation was successful.
     */
    public static String getSaveSuccessMessage(String filePath) {
        return String.format(MESSAGE_SUCCESS_FORMAT, filePath);
    }

    //@@author A0140036X
    /**
     * Returns message if operation failed.
     */
    public static String getSaveFailureMessage(String filePath) {
        return String.format(MESSAGE_SUCCESS_FORMAT, filePath);
    }
}

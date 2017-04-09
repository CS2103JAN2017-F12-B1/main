package savvytodo.logic.commands;

import java.io.IOException;

import savvytodo.MainApp;
import savvytodo.commons.exceptions.DataConversionException;
import savvytodo.logic.commands.exceptions.CommandException;
import savvytodo.model.ReadOnlyTaskManager;
import savvytodo.model.util.MoreSampleDataUtil;

//@@author A0140036X
/**
 * Saves and loads a big data file
 * @author A0140036X
 */
public class GenerateCommand extends Command {

    public static final String COMMAND_WORD = "generate";
    public static final String COMMAND = "generate %s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + "Generates big data file. 'generate <filepath>'";

    public static final String MESSAGE_SUCCESS = "Generated file at %1$s";
    private static final String MESSAGE_FILE_NOT_FOUND = "Unable to generate file at %s";

    private String filePath;

    public GenerateCommand(String filePath) {
        this.filePath = filePath;
    }

    //@@author A0140036X
    /**
     * Generates big data file.
     */
    @Override
    public CommandResult execute() throws CommandException {
        try {
            ReadOnlyTaskManager manager = new MoreSampleDataUtil().getSampleTaskManager();
            storage.setTaskManagerStorageFilePath(filePath);
            storage.saveTaskManager(manager);
            MainApp.getRunningInstance().loadTaskManagerFile(filePath);
            return new CommandResult(getSuccessMessage(filePath));
        } catch (DataConversionException | IOException e) {
            return new CommandResult(getFailureMessage(filePath));
        }
    }

    //@@author A0140036X
    /**
     * Returns success message if file path was generated successfully
     * @param filePath file path of file
     */
    public static String getSuccessMessage(String filePath) {
        return String.format(MESSAGE_SUCCESS, filePath);
    }

    //@@author A0140036X
    /**
     * Returns failure message if file path was generated unsuccessfully
     * @param filePath file path of file
     */
    public static String getFailureMessage(String filePath) {
        return String.format(MESSAGE_FILE_NOT_FOUND, filePath);
    }

    //@@author A0140036X
    /**
     * Get command string
     */
    public static String getCommand(String testTaskManagerFilePath) {
        return String.format(COMMAND, testTaskManagerFilePath);
    }
}

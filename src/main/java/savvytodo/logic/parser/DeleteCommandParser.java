package savvytodo.logic.parser;

import static savvytodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import savvytodo.logic.commands.Command;
import savvytodo.logic.commands.DeleteCommand;
import savvytodo.logic.commands.IncorrectCommand;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand,
     * converts the relevant list view index to the source list index,
     * and then returns an DeleteCommand object for execution.
     */
    public Command parse(String args) {

        Optional<TaskIndex> taskIndex = ParserUtil.parseIndex(args);

        if (!taskIndex.isPresent()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        return new DeleteCommand(taskIndex.get());
    }

}

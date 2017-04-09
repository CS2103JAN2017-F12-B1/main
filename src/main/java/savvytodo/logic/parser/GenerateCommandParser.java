package savvytodo.logic.parser;

import static savvytodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.NoSuchElementException;

import savvytodo.logic.commands.Command;
import savvytodo.logic.commands.GenerateCommand;
import savvytodo.logic.commands.IncorrectCommand;

//@@author A0140036X
/**
 * @author A0140036X
 * Parses input arguments and creates a new LoadCommand object
 */
public class GenerateCommandParser {

    //@@author @author A0140036X
    /**
     * Parses the given {@code String} of arguments in the context of the GenerateCommand
     * and returns an LoadCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer();
        argsTokenizer.tokenize(args);
        try {
            return new GenerateCommand(argsTokenizer.getPreamble().get());
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GenerateCommand.MESSAGE_USAGE));
        }
    }

}

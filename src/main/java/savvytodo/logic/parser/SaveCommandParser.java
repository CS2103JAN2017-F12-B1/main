package savvytodo.logic.parser;

import static savvytodo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.NoSuchElementException;

import savvytodo.logic.commands.Command;
import savvytodo.logic.commands.IncorrectCommand;
import savvytodo.logic.commands.LoadCommand;
import savvytodo.logic.commands.SaveCommand;

//@@author A0140036X
/**
 * @author A0140036X
 * Parses input arguments and creates a new SaveCommand object
 */
public class SaveCommandParser {

    //@@author A0140036X
    /**
     * Parses the given {@code String} of arguments in the context of the SaveCommand
     * and returns an SaveCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer();
        argsTokenizer.tokenize(args);
        try {
            return new SaveCommand(argsTokenizer.getPreamble().get());
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));
        }
    }

}

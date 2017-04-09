package savvytodo.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

//@@author A0147827U
/**
 * Generates dictionaries (as Collections<String>) for auto-complete feature in CommandBox
 * @author jingloon
 */
public class AutoCompleteDictionaryFactory {

    public static final String[] COMMAND_WORDS = { "add", "delete", "list", "edit", "clear", "find", "undo", "redo",
        "help", "select", "mark", "unmark" };

    public static Collection<String> getDictionary() {
        ArrayList<String> dictionary = new ArrayList<String>();
        dictionary.addAll(Arrays.asList(COMMAND_WORDS));

        return dictionary;
    }

}

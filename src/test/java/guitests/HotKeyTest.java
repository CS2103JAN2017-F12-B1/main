package guitests;

import org.junit.Test;

import javafx.scene.input.KeyCodeCombination;
import savvytodo.ui.hotkeys.HotKeysCombinations;

public class HotKeyTest extends TaskManagerGuiTest {
    
    @Test
    public void triggerExitShortcut(){
        KeyCodeCombination exitKeys = (KeyCodeCombination)HotKeysCombinations.KEYS_EXIT;
        new GuiRobot().push(exitKeys);  
        
        assertTaskManagerExited();
    }
    
    
    @Test
    public void triggerHelpShortcut(){
        KeyCodeCombination exitKeys = (KeyCodeCombination)HotKeysCombinations.KEYS_EXIT;
        new GuiRobot().push(exitKeys);  
      
    }
    
    private void assertTaskManagerExited(){
        
    }
}

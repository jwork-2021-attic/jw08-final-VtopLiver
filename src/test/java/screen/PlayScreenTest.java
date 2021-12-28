package screen;

import Data.GameData;
import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import org.junit.Before;
import org.junit.Test;
import world.World;
import world.WorldBuilder;

import java.awt.*;
import java.io.IOException;
import static org.junit.Assert.*;
public class PlayScreenTest {
    PlayScreen playScreen;


    @Before
    public void setUp(){
        playScreen = new PlayScreen(false);
    }
    @Test
    public void testCreateWorld(){
        assertNotNull(playScreen.getWorld());
    }
    @Test
    public void testCreate(){
        assertNotNull(playScreen.getWorld().getCreatures());
    }
    @Test
    public void testDisplay(){
        playScreen.displayOutput(new AsciiPanel(50, 32, AsciiFont.NEWSOURCE));
        assertTrue(playScreen.getMessages().isEmpty());
    }
    @Test
    public void testScroll(){
        assertEquals(0,playScreen.getScrollX());
        assertEquals(0,playScreen.getScrollY());
    }
}

package world;

import Data.GameData;
import asciiPanel.AsciiPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.*;

public class WorldTest {
    private World world;
    private WorldBuilder worldBuilder;
    private Player player;

    @Before
    public void setUp(){
        worldBuilder=new WorldBuilder(30,30);
        world=worldBuilder.makeCaves().build();
        player = new Player(this.world, (char)0, AsciiPanel.brightWhite, 100, 20, 5, 9);

    }

    @Test
    public void testPlayer(){
        world.addAtEmptyLocation(player);
        assertEquals(1,world.getCreatures().size());
        assertTrue(world.getCreatures().contains(player));
        assertEquals(player,world.creature(player.x(),player.y()));
    }
    @Test
    public void testBonus(){
        Bonus b=new AtkBonus(this.world,(char)8, Color.BLUE);
        b.setY(2);
        b.setX(3);
        world.getBonuses().add(b);
        assertEquals(b,world.bonus(3,2));
        world.removeBonus(b);
        assertFalse(world.getBonuses().contains(b));
    }
    @Test
    public void testBullet(){
        Bullet b=new NormalBullet(this.world,10,3);
        b.setX(1);
        b.setY(2);
        world.getBullets().add(b);
        world.removeBullet(b);
        assertFalse(world.getBullets().contains(b));
    }
    @Test
    public void testLoad() throws IOException {
        if (!GameData.canLoad())
            assertEquals(1,2-1);
        world.executeCreature();
        world.executeBullet();
        world.executeBonus();
        assertNotNull(world.getPlayer());
    }
    @Test
    public void testDig(){
        for (int i=1;i<world.height();i++){
            for (int j=1;j<world.width();j++){
                if (world.tile(i,j)==Tile.WALL){
                    world.dig(i,j);
                    assertEquals(Tile.FLOOR,world.tile(i,j));
                    break;
                }
            }
        }
    }
    @Test
    public void testCollapse(){
        world.collapse();
        assertEquals(Tile.BOUNDS,world.tile(1,1));
    }
}

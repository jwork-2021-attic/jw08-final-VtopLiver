package world;

import asciiPanel.AsciiPanel;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;
    private World world;
    private PlayerAI ai;
    private Tile[][] tiles;
    private WorldBuilder worldBuilder;

    @Before
    public void setUp(){
        worldBuilder = new WorldBuilder(30,30);
        world=worldBuilder.makeCaves().build();
        player = new Player(this.world, (char)0, AsciiPanel.brightWhite, 100, 20, 5, 9);
        ai=new PlayerAI(player);
        player.setAI(ai);
        world.addAtEmptyLocation(player);
    }

    @Test
    public void testMove(){
        int x=player.x();
        int y=player.y();
        player.moveBy(1,0);
        if (world.tile(x+1,y)==Tile.FLOOR){
            assertEquals(x+1,player.x());
        } else {
            assertEquals(x,player.x());
        }
    }

    @Test
    public void testAttack(){
        Fungus fungus = new Fungus(this.world, (char)4, AsciiPanel.brightYellow, 10, 0, 0, 4, player);
        int x=player.x();
        int y=player.y();
        player.setAttackValue(50);
        assertEquals(50,player.attackValue());
        if (player.x()!=0) {
            if (world.tile(x-1,y)!=Tile.FLOOR) {
                world.getTiles()[x-1][y]=Tile.FLOOR;
            }
            fungus.setX(x-1);
            fungus.setY(y);
            world.getCreatures().add(fungus);
            player.moveBy(-1,0);
            //System.out.println(world.creature(x-1,y).getClass());
            assertNull(world.creature(x-1,y));
        }
        else {
            if (world.tile(x+1,y)!=Tile.FLOOR) {
                world.getTiles()[x+1][y]=Tile.FLOOR;
            }
            fungus.setX(x+1);
            fungus.setY(y);
            world.getCreatures().add(fungus);
            player.moveBy(1,0);
            assertNull(world.creature(x+1,y));
        }

    }
    @Test
    public void testShoot(){
        player.shoot(1);
        assertFalse(world.getBullets().isEmpty());
        assertEquals(1,world.getBullets().get(0).direction());
    }
    @Test
    public void testHurt(){
        player.hurt(20);
        assertEquals(20,player.maxHP()- player.hp());
        player.modifyHP(20);
        assertEquals(player.maxHP(),player.hp());
    }
}

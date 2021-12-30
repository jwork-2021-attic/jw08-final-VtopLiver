package world;

import java.awt.*;

public class Sword extends Bonus{
    @Override
    public int toInt(){return 2;}
    public Sword(World world, char glyph, Color color){
        super(world, glyph, color);
    }
    @Override
    public void benefit(Creature c){
        c.getSword();
    }
}

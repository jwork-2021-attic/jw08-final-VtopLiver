package world;

import java.awt.*;

public class HpBonus extends Bonus{
    HpBonus(World world, char glyph, Color color){
        super(world, glyph, color);
    }
    @Override
    public void benefit(Creature c){
        c.modifyHP(Math.min(20,c.maxHP()-c.hp()));
    }

    @Override
    public int toInt(){
        return 1;
    }
}

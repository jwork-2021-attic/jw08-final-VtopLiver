package world;

import java.awt.*;

public class AtkBonus extends Bonus{
    AtkBonus(World world, char glyph, Color color){
        super(world, glyph, color);
    }
    @Override
    public void benefit(Creature c){
        c.setAttackValue(c.attackValue()+10);
    }

    @Override
    public int toInt(){
        return 0;
    }
}

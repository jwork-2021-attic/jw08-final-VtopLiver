package world;

import java.awt.*;

public class SwordBullet extends Bullet{
    @Override
    public void run() {
        while(true){
            if(!this.world.getBullets().contains(this)){
                break;
            }
            this.moveBy(d[direction][0], d[direction][1]);
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {}
        }
    }
    @Override
    public int toInt(){return 1;}
    public SwordBullet(World world, char glyph, Color color, int attack, int direction){
        super(world, glyph, color, attack, direction);
    }
}

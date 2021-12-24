package world;

import java.awt.*;

public class NormalBullet extends Bullet{
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
    public int toInt(){return 0;}
    public NormalBullet(World world, char glyph, Color color, int attack, int direction){
        super(world, glyph, color, attack, direction);
    }
    public NormalBullet(World world,Color color,int attack, int direction){
        super(world, color, attack, direction);
    }
    public NormalBullet(World world,int attack,int direction){
        super(world, attack, direction);
    }
}

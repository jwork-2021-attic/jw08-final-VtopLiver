package world;

import java.awt.*;

public abstract class Bonus {
    protected World world;

    protected char glyph;
    public char glyph(){ return this.glyph;}

    protected Color color;
    public Color color(){return this.color;}

    private int x;
    public void setX(int x) {
        this.x = x;
    }
    public int x() {
        return x;
    }

    private int y;
    public void setY(int y) {
        this.y = y;
    }
    public int y() {
        return y;
    }

    public Bonus(World world,char glyph,Color color){
        this.world=world;
        this.color=color;
        this.glyph=glyph;
    }
    public abstract int toInt();
    public void benefit(Creature c){}
}

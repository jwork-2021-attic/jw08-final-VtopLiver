package world;

import java.awt.*;

public abstract class Bullet implements Runnable{
    protected final int[][] d = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 }, { -1, 1 } };

    protected World world;

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

    private char glyph;
    public char glyph() {
        return this.glyph;
    }

    protected Color color;
    public Color color() {
        return this.color;
    }

    protected int attack;
    public int attack(){ return this.attack; }
    public void attackOther(Creature c){c.hurt(attack);}
    public void setAttack(int atk){ this.attack=atk; }

    protected int direction;
    public int direction(){ return this.direction; }

    public Bullet(World world, char glyph, Color color, int attack, int direction) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.attack = attack;
        this.direction = direction;

    }
    public Bullet(World world,Color color,int attack, int direction){
        this(world,(char)7,color,attack,direction);
    }
    public Bullet(World world,int attack,int direction){
        this(world,Color.BLUE,attack,direction);
    }

    public void moveBy(int mx,int my){
        Creature other = world.creature(x + mx, y + my);

        if (other == null) {
            onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        } else {
            attackOther(other);
            world.removeBullet(this);
        }
    }
    private void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            this.setX(x);
            this.setY(y);
        } else if (tile.isDiggable()) {
            world.dig(x, y);
            world.removeBullet(this);
        }else{
            world.removeBullet(this);
        }
    }

    public abstract int toInt();

}

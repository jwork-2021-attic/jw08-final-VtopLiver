package world;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fungus extends Creature{
    public Fungus(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius,Player p){
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
        this.player=p;
        dx=new int[] {0,0,-1,1};
        dy=new int[] {-1,1,0,0};
        rand=new Random();
    }
    public Fungus(World world, char glyph, Color color, int maxHP,int hp, int attack, int defense, int visionRadius,int x,int y,Player p) {
        super(world, glyph, color, maxHP,hp, attack, defense, visionRadius,x,y);
        this.player=p;
        dx=new int[] {0,0,-1,1};
        dy=new int[] {-1,1,0,0};
        rand=new Random();
    }
    private Player player;
    private int[] dx;
    private int[] dy;
    private Random rand;
    @Override
    public void run(){
        while(true){
            if(!canSee(player.x(), player.y())){
                int direction=rand.nextInt(4);
                this.moveBy(dx[direction], dy[direction]);
            }
            else{
                if(rand.nextInt(3)==1)
                    dfs(this.x(), this.y());
            }
            try {
            Thread.sleep(rand.nextInt(500));
            } 
            catch (Exception e) {}
        }
        
    }
    private int mht(int x1,int y1,int x2,int y2){
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }
    private void dfs(int x,int y){
        if(x==player.x()&&y==player.y()){
            return;
        }
        int maxi=0;
        int minabs=100000;
        for(int i=0;i<4;i++){
            if(mht(player.x(), player.y(), x+dx[i], y+dy[i])<minabs){
                maxi=i;
                minabs=mht(player.x(), player.y(), x+dx[i], y+dy[i]);
            }//潜在的加锁位置
        }
        this.moveBy(dx[maxi], dy[maxi]);
        try {
            Thread.sleep(500);
            } 
            catch (Exception e) {}
        dfs(x+dx[maxi],y+dy[maxi]);
    }

    @Override
    public int toInt(){
        return 1;
    }
}

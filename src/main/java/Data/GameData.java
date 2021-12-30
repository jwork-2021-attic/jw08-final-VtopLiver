package Data;

import world.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameData {
    private World world;
    private Tile[][] tiles;
    public GameData(World world){
        this.world = world;
        tiles=this.world.getTiles();
    }
    public void save() throws IOException {
        saveTile();
        saveCreature();
        saveBonus();
        saveBullet();
    }
    private void saveTile() throws IOException{
        FileWriter fw=new FileWriter("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\TileCache.txt");
        fw.write("-\n");
        for(int i=0;i<tiles.length;i++){
            for(int j=0;j<tiles[0].length;j++){
                fw.write(String.valueOf(tiles[i][j].toInt()));
            }
            fw.write("\n");
        }
        fw.flush();
        fw.close();
    }
    private void saveCreature() throws IOException{
        FileWriter fw=new FileWriter("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\CreatureCache.txt");
        for(Creature c:this.world.getCreatures()){
            fw.write(c.toInt()+" "+c.hp()+" "+c.attackValue()+" "+c.x()+" "+c.y()+" "+c.isBullet()+"\n");
        }
        fw.flush();
        fw.close();
    }
    private void saveBonus() throws IOException{
        FileWriter fw=new FileWriter("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\BonusCache.txt");
        for (Bonus b:this.world.getBonuses()){
            fw.write(b.toInt()+" "+b.x()+" "+b.y()+"\n");
        }
        fw.flush();
        fw.close();
    }
    private void saveBullet() throws IOException{
        FileWriter fw=new FileWriter("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\BulletCache.txt");
        for (Bullet b:this.world.getBullets()){
            fw.write(b.toInt()+" "+b.attack()+" "+b.direction()+" "+b.x()+" "+b.y()+"\n");
        }
        fw.flush();
        fw.close();
    }
    public static boolean canLoad() throws IOException{
        FileReader fr=new FileReader("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\TileCache.txt");
        int a;
        a=fr.read();
        fr.close();
        return a==(int)'-';
    }
}

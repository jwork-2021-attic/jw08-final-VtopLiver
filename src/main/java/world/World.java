package world;

import Data.GameData;
import asciiPanel.AsciiPanel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 *
 * @author Aeranythe Echosong
 */
public class World {

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Bullet> bullets;
    private List<Bonus> bonuses;
    private final double bonusRate=0.4;
    private Player player;

    public static final int TILE_TYPES = 2;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.bullets =new ArrayList<>();
        this.bonuses = new ArrayList<>();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public Tile[][] getTiles(){
        return this.tiles;
    }

    public char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            if(Math.random()<bonusRate){
                Random rand=new Random();
                if(rand.nextInt(2)==0){
                    Bonus b=new AtkBonus(this,(char)65,Color.BLUE);
                    b.setX(x);
                    b.setY(y);
                    bonuses.add(b);
                }
                else{
                    Bonus b=new HpBonus(this,(char)66,Color.BLUE);
                    b.setX(x);
                    b.setY(y);
                    bonuses.add(b);
                }
            }
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }
    public Bonus bonus(int x,int y) {
        for(Bonus b:this.bonuses) {
            if(b.x()==x && b.y()==y){
                return b;
            }
        }
        return null;
    }
    public List<Bullet> getBullets(){
        return this.bullets;
    }
    public List<Bonus> getBonuses(){
        return this.bonuses;
    }
    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public void remove(Creature target) {
        this.creatures.remove(target);
    }
    public void removeBullet(Bullet target) {this.bullets.remove(target); }
    public void removeBonus(Bonus target) {this.bonuses.remove(target); }
    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }
    }

    public void executeCreature() throws IOException {
        if(!GameData.canLoad())
            return;
        File f=new File("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\CreatureCache.txt");
        Scanner sc=new Scanner(f);
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            String[] tmp=s.split(" ");
            int idx=Integer.parseInt(tmp[0]);
            int hp=Integer.parseInt(tmp[1]);
            int attack=Integer.parseInt(tmp[2]);
            int xx=Integer.parseInt(tmp[3]);
            int yy=Integer.parseInt(tmp[4]);
            if(idx==0){
                player = new Player(this, (char)2, AsciiPanel.brightWhite, 100,hp, 20, 5, 9,xx,yy);
                this.creatures.add(player);
            }
            else if (idx==1){
                if (player!=null) {
                    Fungus fungus = new Fungus(this, (char)3, AsciiPanel.brightYellow, 10,hp, 0, 0, 4,xx,yy, player);
                    this.creatures.add(fungus);
                    new FungusAI(fungus,new CreatureFactory(this));
                    new Thread(fungus).start();
                }
            }
        }
        sc.close();
    }

    public void executeBonus() throws IOException {
        if(!GameData.canLoad())
            return;
        File f=new File("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\BonusCache.txt");
        Scanner sc=new Scanner(f);
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            String[] tmp=s.split(" ");
            int idx=Integer.parseInt(tmp[0]);
            int xx=Integer.parseInt(tmp[1]);
            int yy=Integer.parseInt(tmp[2]);
            Bonus b=null;
            if(idx==0){
                b=new AtkBonus(this,(char)65,Color.BLUE);
                b.setX(xx);
                b.setY(yy);
            }
            else if (idx==1){
                b=new HpBonus(this,(char)66,Color.BLUE);
                b.setX(xx);
                b.setY(yy);
            }
            if (b!=null){
                this.bonuses.add(b);
            }

        }
        sc.close();
    }

    public void executeBullet() throws IOException {
        if(!GameData.canLoad())
            return;
        File f=new File("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\BulletCache.txt");
        Scanner sc=new Scanner(f);
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            String[] tmp=s.split(" ");
            int idx=Integer.parseInt(tmp[0]);
            int atk=Integer.parseInt(tmp[1]);
            int direction=Integer.parseInt(tmp[2]);
            int xx=Integer.parseInt(tmp[3]);
            int yy=Integer.parseInt(tmp[4]);
            Bullet b=new NormalBullet(this,atk,direction);
            b.setX(xx);
            b.setY(yy);
            this.bullets.add(b);
        }
        sc.close();
    }

    public Player getPlayer(){
        return player;
    }
}

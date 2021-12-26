package world;

import Data.GameData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/*
 * Copyright (C) 2015 s-zhouj
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
 * @author s-zhouj
 */
public class WorldBuilder {

    private int width;
    private int height;
    private Tile[][] tiles;

    public WorldBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public World build() {
        return new World(tiles);
    }
    public World load(){
        try {
            executeTile();
        } catch (IOException e){
            e.printStackTrace();
        }
        return new World(tiles);
    }
    private WorldBuilder randomizeTiles() {
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                Random rand = new Random();
                int choice=rand.nextInt(World.TILE_TYPES+2);
                if(choice==0) {
                    tiles[width][height] = Tile.HpBonusWALL;
                }
                else if (choice==1){
                    tiles[width][height] = Tile.WALL;
                }
                else if (choice==2){
                    tiles[width][height] = Tile.AtkBonusWALL;
                }
                else {
                    tiles[width][height] = Tile.FLOOR;
                }

                
            }
        }
        return this;
    }

    private WorldBuilder smooth(int factor) {
        Tile[][] newtemp = new Tile[width][height];
        if (factor > 1) {
            smooth(factor - 1);
        }
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                // Surrounding walls and floor
                int surrwalls = 0;
                int surrfloor = 0;

                // Check the tiles in a 3x3 area around center tile
                for (int dwidth = -1; dwidth < 2; dwidth++) {
                    for (int dheight = -1; dheight < 2; dheight++) {
                        if (width + dwidth < 0 || width + dwidth >= this.width || height + dheight < 0
                                || height + dheight >= this.height) {
                            continue;
                        } else if (tiles[width + dwidth][height + dheight] == Tile.FLOOR) {
                            surrfloor++;
                        } else if (tiles[width + dwidth][height + dheight] != Tile.BOUNDS) {
                            surrwalls++;
                        }
                    }
                }
                Tile replacement;
                if (surrwalls > surrfloor) {
                    Random rand=new Random();
                    int tmp=rand.nextInt(5);
                    if (tmp==0)
                        replacement=Tile.AtkBonusWALL;
                    else if (tmp==1)
                        replacement=Tile.HpBonusWALL;
                    else
                        replacement = Tile.WALL;
                } else {
                    replacement = Tile.FLOOR;
                }
                newtemp[width][height] = replacement;
            }
        }
        tiles = newtemp;
        for (int i=0;i<tiles.length;i++){
            for (int j=0;j<tiles[i].length;j++){
                if (i==0 || j==0 || i==width-1 || j== height -1)
                    tiles[i][j]=Tile.BOUNDS;
            }
        }
        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(8);
    }

    public void executeTile() throws IOException {
        if(!GameData.canLoad())
            return;
        File f=new File("C:\\Users\\lenovo\\Desktop\\Java\\jw05-VtopLiver\\src\\main\\resources\\TileCache.txt");
        Scanner sc=new Scanner(f);
        int i=0;
        while (sc.hasNextLine()){
            String s=sc.nextLine();
            if(s.charAt(0)=='-')
                continue;
            for(int j=0;j<s.length();j++){
                char c=s.charAt(j);
                if(c>='0'&&c<='9'){
                    int intc=c-'0';
                    if(intc==0)
                        tiles[i][j]=Tile.FLOOR;
                    else if (intc==1)
                        tiles[i][j]=Tile.WALL;
                    else if (intc==2)
                        tiles[i][j]=Tile.BOUNDS;
                    else if (intc==3)
                        tiles[i][j]=Tile.AtkBonusWALL;
                    else
                        tiles[i][j]=Tile.HpBonusWALL;
                }
            }
            i++;
        }
        sc.close();
    }
}

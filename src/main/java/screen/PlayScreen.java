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
package screen;

import Data.GameData;
import network.Client;
import network.ClientListener;
import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Player player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private int lastCode;
    private boolean iswaterSki;
    private boolean isfireSki;
    private GameData data;
    private Client client;
    private int playerID;
    private Player p0;
    private Player p1;
    private Player p2;
    private boolean multi;

    public PlayScreen(boolean isLoad){
        this.screenWidth = 30;
        this.screenHeight = 30;
        multi=false;
        if (isLoad){
            loadWorld();
            try {
                world.executeCreature();
                world.executeBonus();
                world.executeBullet();
            } catch (IOException e){
                e.printStackTrace();
            }
            player=world.getPlayer();
            new PlayerAI(player,messages);
        }
        else{
            createWorld();
            CreatureFactory creatureFactory = new CreatureFactory(this.world);
            createCreatures(creatureFactory);
        }
        data=new GameData(this.world);

        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        lastCode=0;
        //System.out.println(this.world.getTiles().length);
        //System.out.println(this.world.getTiles()[0].length);
    }

    public PlayScreen(int playID){
        this.playerID=playID;
        this.screenWidth = 30;
        this.screenHeight = 30;
        multi=true;
        loadWorld();
        //Player[] ptmp={p0,p1,p2};
        //for (int i=0;i<3;i++){
        p0 = new Player(this.world, (char)0, AsciiPanel.brightWhite, 100, 20, 5, 90);
        new PlayerAI(p0, messages);
        p0.setX(3);
        p0.setY(3);
        world.getCreatures().add(p0);
        //world.addAtEmptyLocation(p0);
        new Thread(p0).start();
        p1 = new Player(this.world, (char)1, AsciiPanel.brightWhite, 100, 20, 5, 90);
        new PlayerAI(p1, messages);
        p1.setX(3);
        p1.setY(27);
        world.getCreatures().add(p1);
        //world.addAtEmptyLocation(p1);
        new Thread(p1).start();
        p2 = new Player(this.world, (char)2, AsciiPanel.brightWhite, 100, 20, 5, 90);
        new PlayerAI(p2, messages);
        p2.setX(27);
        p2.setY(3);
        world.getCreatures().add(p2);
        //world.addAtEmptyLocation(p2);
        new Thread(p2).start();
        if (playID==0)
            this.player=p0;
        else if (playID==1)
            this.player=p1;
        else
            this.player=p2;
        //}
        try {
            client=new Client(this,playID);
        }catch (IOException e){
        }
        ClientListener cl=new ClientListener(client);
        new Thread(cl).start();

        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        lastCode=0;

    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages);

        for (int i = 0; i < 8; i++) {
            creatureFactory.newFungus();
        }
        creatureFactory.newBoss();
    }

    private void createWorld() {
        world = new WorldBuilder(30, 30).makeCaves().build();
    }
    private void loadWorld() {world = new WorldBuilder(30,30).load();}
    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }
        //Show Bonuses
        for (Bonus bonus : world.getBonuses()) {
            if (bonus.x() >= left && bonus.x() < left + screenWidth && bonus.y() >= top
                    && bonus.y() < top + screenHeight) {
                if (player.canSee(bonus.x(), bonus.y())) {
                    terminal.write(bonus.glyph(), bonus.x() - left, bonus.y() - top, bonus.color());
                }
            }
        }
        //Show Bullets
        for (Bullet bullet : world.getBullets()) {
            if (bullet.x() >= left && bullet.x() < left + screenWidth && bullet.y() >= top
                    && bullet.y() < top + screenHeight) {
                if (player.canSee(bullet.x(), bullet.y())) {
                    terminal.write(bullet.glyph(), bullet.x() - left, bullet.y() - top, bullet.color());
                }
            }
        }
        // Creatures can choose their next action now
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, 0, 0);
        //displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        String stats = String.format("%3d/%3d hp", player.hp(), player.maxHP());
        terminal.write(stats, 1, 31);
        // Messages
        displayMessages(terminal, this.messages);
        displaywaterSki(terminal);
        displayfireSki(terminal);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                lastCode=2;
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                lastCode=3;
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                lastCode=0;
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                lastCode=1;
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                iswaterSki=true;
                break;
            case KeyEvent.VK_S:
                isfireSki=true;
                break;
            case KeyEvent.VK_D:
                player.shoot(lastCode);
                break;
            case KeyEvent.VK_P:
                try{
                    data.save();
                } catch (IOException e){
                    e.printStackTrace();
                }
                break;
            
        }
        if(player.hp()<1){
            return new LoseScreen();
        }
        if (!multi&&world.getCreatures().size()==1&&player.hp()>0){
            return new WinScreen();
        }
        if (multi) {
            int cnt=0;
            if (p0.hp()>0)
                cnt++;
            if (p1.hp()>0)
                cnt++;
            if (p2.hp()>0)
                cnt++;
            if (cnt==1&&player.hp()>0){
                return new WinScreen();
            }
        }
        if (multi){
            try{
                client.handleWrite(pack(key.getKeyCode(),lastCode));
            }catch (IOException e){
            }
        }
        return this;
        
            
    }
    public void displaywaterSki(AsciiPanel ter){
        if(iswaterSki){
            player.waterSki(lastCode);
            for(int i=0;i<player.Lx.size();i++){
                ter.write("*",player.Lx.get(i)- getScrollX(),player.Ly.get(i)- getScrollY(),Color.BLUE);
                //System.out.println("waterSkiOK");
            }
            iswaterSki=false;
        }
    }
    public void displayfireSki(AsciiPanel ter){
        if(isfireSki){
            player.fireSki(lastCode);
            for(int i=0;i<player.Lx.size();i++){
                ter.write("*",player.Lx.get(i)- getScrollX(),player.Ly.get(i)- getScrollY(),Color.red);
                //System.out.println("waterSkiOK");
            }
            isfireSki=false;
        }
    }
    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

    public String pack(int key,int lastCode){
        return new String(Integer.toString(playerID)+"-"+Integer.toString(key)+"-"+Integer.toString(lastCode));
    }

    public void handleEvent(String info){
        String[] tmp = info.split("-");
        Player p;
        if (tmp.length!=3)
            return;
        if (Integer.parseInt(tmp[0])==playerID)
            return;
        switch (Integer.parseInt(tmp[0])){
            case 0:
                p=p0;
                System.out.println("p0");
                if (p0==null)
                    System.out.println("p0NULL");
                break;
            case 1:
                p=p1;
                System.out.println("p1");
                if (p1==null)
                    System.out.println("p1NULL");
                break;
            case 2:
                p=p2;
                System.out.println("p2");
                if (p2==null)
                    System.out.println("p2NULL");
                break;
            default:
                p=player;
                break;
        }
        if (p==null)
            System.out.println("ERROR???????????????");
        switch (Integer.parseInt(tmp[1])){
            case KeyEvent.VK_LEFT:
                lastCode=2;
                p.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                lastCode=3;
                p.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                lastCode=0;
                p.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                lastCode=1;
                p.moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                iswaterSki=true;
                break;
            case KeyEvent.VK_S:
                isfireSki=true;
                break;
            case KeyEvent.VK_D:
                p.shoot(Integer.parseInt(tmp[2]));
                break;
        }
    }
}

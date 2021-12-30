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
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author Aeranythe Echosong
 */
public class StartScreen extends RestartScreen {
    private boolean positon;
    @Override
    public void displayOutput(AsciiPanel terminal){
        terminal.write("New Game", 5, 10);
        boolean cl=false;
        try{
            cl=GameData.canLoad();
        } catch (IOException e){
            e.printStackTrace();
        }
        if (cl){
            terminal.write("Load Game", 5, 20);
        }
        if (this.positon && cl){
            terminal.write("->", 2, 20);
        }
        else{
            terminal.write("->", 2, 10);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return new PlayScreen(this.positon);
            case KeyEvent.VK_DOWN:
                this.positon=true;
                return this;
            case KeyEvent.VK_UP:
                this.positon=false;
                return this;
            default:
                return this;
        }
    }

    public StartScreen(){
        this.positon=false;
    }

}

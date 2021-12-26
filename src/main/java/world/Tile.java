package world;

import asciiPanel.AsciiPanel;
import java.awt.Color;
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
public enum Tile {

    FLOOR((char) 3, Color.darkGray),

    WALL((char) 178, AsciiPanel.brightBlack),

    BOUNDS((char) 9, AsciiPanel.magenta),

    AtkBonusWALL((char)178,AsciiPanel.brightBlack),

    HpBonusWALL((char)178,AsciiPanel.brightBlack);

    private char glyph;

    public char glyph() {
        return glyph;
    }

    private Color color;

    public Color color() {
        return color;
    }

    public boolean isDiggable() {
        return this != Tile.BOUNDS;
        //return true;
    }//We can set the returnValue = true for test!

    public boolean isGround() {
        return this==Tile.FLOOR;
    }

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }

    public int toInt(){
        if (this==Tile.FLOOR)
            return 0;
        else if (this==Tile.WALL)
            return 1;
        else if (this==Tile.BOUNDS)
            return 2;
        else if (this==Tile.AtkBonusWALL)
            return 3;
        else
            return 4;
    }
}

package screen;

import Data.GameData;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class SelectScreen implements Screen{

    private boolean positon;
    @Override
    public void displayOutput(AsciiPanel terminal){
        terminal.write("Single Player", 15, 10);
        boolean cl=false;
        try{
            cl= GameData.canLoad();
        } catch (IOException e){
            e.printStackTrace();
        }
        if (cl){
            terminal.write("Multi Players", 15, 20);
        }
        if (this.positon && cl){
            terminal.write("->", 12, 20);
        }
        else{
            terminal.write("->", 12, 10);
        }

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (!positon){
                    return new StartScreen();
                }
                else
                    return new MultiScreen();
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

    public SelectScreen(){
        this.positon=false;
    }

}

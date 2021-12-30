package screen;

import asciiPanel.AsciiPanel;
import network.Server;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class ServerScreen implements Screen{
    network.Server server;
    public ServerScreen(){
        server=new Server();
    }
    @Override
    public Screen GameStatus(){
        return this;
    }
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("The Server is Running",5,14);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        try {
            server.startServer();
        }catch (IOException e){
        }

        return this;
    }
}

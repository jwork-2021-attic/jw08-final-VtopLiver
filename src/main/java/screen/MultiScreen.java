package screen;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class MultiScreen implements Screen{
    int state;

    public MultiScreen() {
        state = 0;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Please choose: Server or Client?", 0, 0);
        switch (state) {
            case 0:
                terminal.write((char) (26), 0, 2);
                break;
            case 1:
                terminal.write((char) (26), 0, 6);
                break;
            case 2:
                terminal.write((char) (26), 0, 10);
                break;
            case 3:
                terminal.write((char) (26), 0, 14);
                break;
        }
        terminal.write("Server", 3, 2);
        terminal.write("Client1", 3, 6);
        terminal.write("Client2", 3, 10);
        terminal.write("Client3", 3, 14);

    }

    @Override
    public Screen respondToUserInput(KeyEvent key){
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                switch (state) {
                    case 0:
                        return new ServerScreen();
                    case 1:
                        return new PlayScreen(0);
                    case 2:
                        return new PlayScreen(1);
                    case 3:
                        return new PlayScreen(2);

                }

            case KeyEvent.VK_UP:
                if (state > 0)
                    state--;
                return this;
            case KeyEvent.VK_DOWN:
                if (state < 3)
                    state++;
                return this;
            default:
                return this;
        }
    }
}

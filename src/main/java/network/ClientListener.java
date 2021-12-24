package network;

import java.io.IOException;

public class ClientListener implements Runnable{
    Client client;
    public ClientListener(Client c){
        client = c;
    }
    @Override
    public void run(){
        while (true){
            try {
                client.handleRead();
                Thread.sleep(100);
            }catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

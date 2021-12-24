package network;


import screen.PlayScreen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    private PlayScreen playScreen;
    private int clientID;
    private SocketChannel sc;
    private static final int PORT=7777;
    private static final int BUFFER_SIZE=1024;
    private ByteBuffer buf;

    public Client(PlayScreen playScreen,int ID) throws IOException {
        this.playScreen=playScreen;
        clientID=ID;
        buf=ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress addr=new InetSocketAddress("127.0.0.1",PORT);
        sc=SocketChannel.open(addr);
        sc.configureBlocking(false);
        //sc.connect(new InetSocketAddress("127.0.0.1",PORT));


    }

    public void handleRead() throws IOException {
        int bytesRead = sc.read(buf);
        String info= "";
        while(bytesRead>0){
            buf.flip();
            while(buf.hasRemaining()){
                char c=(char)buf.get();
                System.out.print(c);
                info+=c;
            }
            System.out.println();
            buf.clear();
            bytesRead = sc.read(buf);
        }
        if(bytesRead == -1){
            sc.close();
        }
        if (!info.isEmpty())
            System.out.println(info+"Client Received!");

        handleEvent(info);
    }

    private void handleEvent(String info){
        //操作规范：编号-行走-键码 OR 编号-攻击-键码-lastcode
        this.playScreen.handleEvent(info);
    }

    public void handleWrite(String info) throws IOException{
        byte[] data=info.getBytes();
        buf.put(data);
        buf.flip();
        sc.write(buf);
        buf.clear();
    }
}

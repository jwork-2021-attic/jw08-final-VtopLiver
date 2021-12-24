package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private ServerSocketChannel ssc;
    private Selector selector;
    //private ByteBuffer buf;

    private static final int BUF_SIZE=1024;

    private static final int PORT=7777;

    public void startServer() throws IOException {
        ssc=ServerSocketChannel.open();
        //buf=ByteBuffer.allocate(BUF_SIZE);
        ssc.configureBlocking(false);
        ServerSocket ss=ssc.socket();
        ss.bind(new InetSocketAddress("127.0.0.1",PORT));
        selector=Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.select() == 0){
                System.out.println("==");
                continue;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while(iter.hasNext()){
                SelectionKey key = iter.next();
                if(key.isAcceptable()){
                    handleAccept(key);
                }
                if(key.isReadable()){
                    handleRead(key);
                }
                if(key.isWritable() && key.isValid()){
                    ;//handleWrite(key);
                }
                if(key.isConnectable()){
                    System.out.println("isConnectable = true");
                }
                iter.remove();
            }
        }
    }
    private void handleAccept(SelectionKey key) throws IOException{
        ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocateDirect(BUF_SIZE));
    }
    private void handleRead(SelectionKey key) throws IOException{
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer buf = (ByteBuffer)key.attachment();
        int bytesRead = sc.read(buf);
        String info=new String();
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
            System.out.println(info+"!!!!");
        handleEvent(info);
    }
    private void handleWrite(SelectionKey key) throws IOException{
        ByteBuffer buf = (ByteBuffer)key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel) key.channel();
        while(buf.hasRemaining()){
            sc.write(buf);
        }
        buf.compact();
    }

    private void handleEvent(String info) throws IOException{
        if(!info.isEmpty()){
            Set<SelectionKey> allKeys = selector.keys();
            Iterator iter = allKeys.iterator();
            //Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            SelectionKey key=(SelectionKey) iter.next();
            writeToClient(key,info);

            while (iter.hasNext()){
                key=(SelectionKey) iter.next();
                writeToClient(key,info);
            }
        }
    }

    private void writeToClient(SelectionKey key, String info) throws IOException {
        if (key.channel() instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(info.getBytes());
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
            System.out.println("HAVE WRITETOCLIENT!!!");
        }
    }

}

package xyz.raygetoffer.communication.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author mingruiwu
 * @create 2022/6/28 16:49
 * @description
 */
public class ClientDemo {
    private static final Logger logger = LoggerFactory.getLogger(ClientDemo.class);

    public Object send(Message message, String host, int port) {
        // 1. 创建Socket对象并指定服务器的地址和端口
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            // 2. 通过输出流向服务器端发送请求消息
            objectOutputStream.writeObject(message);
            // 3. 通过输入流读取服务器发来的消息
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("occur exception:", e);
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        ClientDemo clientDemo = new ClientDemo();
        Object message = clientDemo.send(new Message("你好"), "127.0.0.1", 9999);
        System.out.println("Client receive message:" + message);
    }
}

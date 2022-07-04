package xyz.raygetoffer.communication.socket;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.raygetoffer.exception.SerializeException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mingruiwu
 * @create 2022/6/28 15:51
 * @description 模拟socket通信的服务端，采用同步阻塞io的方式
 */
@Slf4j
public class ServerDemo {
    private static final Logger logger = LoggerFactory.getLogger(ServerDemo.class);
    ExecutorService threadPool = new ThreadPoolExecutor(10, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100));


    public void start(int port) {
        // 1. 创建ServerSocket 对象并绑定一个端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket;
            // 2. 通过accept()方法监听客户端请求
            while ((socket = serverSocket.accept()) != null) {
                Runnable communicationTask = new CommunicationTask(socket);
                threadPool.submit(communicationTask);
            }
        } catch (IOException e) {
            logger.error("occur exception:", e);
        }
    }

    public static void main(String[] args) {
        ServerDemo serverDemo = new ServerDemo();
        serverDemo.start(9999);
    }

    // 线程池实现多客户端管理
    static class CommunicationTask implements Runnable {
        private Socket socket;

        public CommunicationTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            logger.info("client connected");
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                // 3. 通过输入流读取客户端发送的请求消息
                Message message = (Message) objectInputStream.readObject();
                logger.info("server receive message:" + message.getContent());
                logger.info(Thread.currentThread().getName());
                message.setContent("new message");
                // 4. 通过输出流向客户端发送响应信息
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            } catch (ClassNotFoundException | IOException e) {
                logger.error("occur exception:", e);
            }
        }
    }
}

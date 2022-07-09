package xyz.raygetoffer.remoting.communication.socket;

import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.utils.concurrent.threadpool.ThreadPoolFactoryUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:24
 * @description
 */
@Slf4j
public class SocketRpcServer {
    // 假设绑定的端口固定为6666
    private final ExecutorService threadPool;

    public SocketRpcServer() {
        this.threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 6666));
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.submit(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}

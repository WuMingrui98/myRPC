package xyz.raygetoffer.communication.socket;

import xyz.raygetoffer.remoting.communication.impl.socket.SocketRpcServer;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:39
 * @description
 */
public class ServerTest {
    public static void main(String[] args) {
        new SocketRpcServer().start();
    }
}

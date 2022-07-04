package xyz.raygetoffer.communication.netty;

/**
 * @author mingruiwu
 * @create 2022/7/1 14:00
 * @description
 */
public class ServerTestMain {
    public static void main(String[] args) {
        new NettyServer(8089).run();
    }
}

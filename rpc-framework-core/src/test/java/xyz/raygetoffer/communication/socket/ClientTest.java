package xyz.raygetoffer.communication.socket;

import xyz.raygetoffer.remoting.communication.impl.socket.SocketRpcClient;
import xyz.raygetoffer.remoting.dto.RpcRequest;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:39
 * @description
 */
public class ClientTest {
    public static void main(String[] args) {
        new SocketRpcClient().sendRpcRequest(new RpcRequest("abc", "test"));
    }
}

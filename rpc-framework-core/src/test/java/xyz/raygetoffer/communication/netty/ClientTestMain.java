package xyz.raygetoffer.communication.netty;

import xyz.raygetoffer.remoting.dto.RpcRequest;

/**
 * @author mingruiwu
 * @create 2022/7/1 13:57
 * @description
 */
public class ClientTestMain {
    public static void main(String[] args) {
        RpcRequest rpcRequest = RpcRequest.builder().interfaceName("interface").methodName("hello").build();
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8089);
        for (int i = 0; i < 3; i++) {
            System.out.println(nettyClient.sendMessage(rpcRequest));
        }
    }
}

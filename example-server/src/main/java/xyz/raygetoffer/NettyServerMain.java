package xyz.raygetoffer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.annotation.RpcScan;
import xyz.raygetoffer.remoting.communication.netty.server.NettyRpcServer;

/**
 * @author mingruiwu
 * @create 2022/8/7 16:47
 * @description
 */
@RpcScan(basePackages = "xyz.raygetoffer")
public class NettyServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        nettyRpcServer.start();
    }
}

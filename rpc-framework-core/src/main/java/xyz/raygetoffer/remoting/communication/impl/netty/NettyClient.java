package xyz.raygetoffer.remoting.communication.impl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.raygetoffer.serialize.ISerializer;
import xyz.raygetoffer.serialize.impl.kryo.KryoSerializer;

/**
 * 使用Netty实现RPC客户端
 *
 * @author mingruiwu
 * @create 2022/6/30 14:32
 * @description
 */
public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

//    static {
//        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//        Bootstrap bootstrap = new Bootstrap();
//        ISerializer serializer = new KryoSerializer();
//        bootstrap.group(eventLoopGroup)
//                .channel(NioSocketChannel.class)
//                .handler()
//    }
}

package xyz.raygetoffer.remoting.communication.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.communication.netty.codec.NettyRpcDecoder;
import xyz.raygetoffer.remoting.communication.netty.codec.NettyRpcEncoder;
import xyz.raygetoffer.remoting.communication.netty.codec.ProtocolFrameDecoder;
import xyz.raygetoffer.remoting.communication.netty.server.handler.NettyRpcServerHandler;
import xyz.raygetoffer.serialize.ISerializer;
import xyz.raygetoffer.serialize.impl.kryo.KryoSerializer;
import xyz.raygetoffer.utils.RuntimeUtil;
import xyz.raygetoffer.utils.concurrent.threadpool.ThreadPoolFactoryUtil;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author mingruiwu
 * @create 2022/7/1 13:31
 * @description
 */
@Slf4j
public class NettyRpcServer {
    public static final int PORT = 9999;

//    public NettyRpcServer(int port) {
//        this.PORT = port;
//    }



    @SneakyThrows
    public void run() {
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.getCPUs(),
                ThreadPoolFactoryUtil.createThreadFactory("server-handler-group", false)
        );
        ISerializer serializer = new KryoSerializer();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 心跳机制，30秒内没有接收到客户端的请求的话就关闭连接
                            ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            // 入站
                            // 解码 ByteBuf -> RpcRequest
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
//                            ch.pipeline().addLast(new NettyRpcDecoder());
                            // 使用额外的线程池来处理NettyRpcServerHandler
                            ch.pipeline().addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                            // 出栈
                            // 编码 RpcResponse -> ByteBuf
//                            ch.pipeline().addLast(new NettyRpcEncoder());

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }
}

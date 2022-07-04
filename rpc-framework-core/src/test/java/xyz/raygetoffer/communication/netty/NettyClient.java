package xyz.raygetoffer.communication.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;
import xyz.raygetoffer.serialize.ISerializer;
import xyz.raygetoffer.serialize.impl.kryo.KryoSerializer;

/**
 * 使用Netty实现RPC客户端
 *
 * @author mingruiwu
 * @create 2022/6/30 14:32
 * @description
 */
@Slf4j
public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private final String host;
    private final int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        ISerializer serializer = new KryoSerializer();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                // 连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        /*
                        自定义序列化编解码器
                         */
                        // 入
                        // ByteBuf -> RpcResponse 解码
                        socketChannel.pipeline().addLast(new NettyDecoder(new KryoSerializer(), RpcResponse.class));
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                        // 出
                        // RpcRequest -> ByteBuf 编码
                        socketChannel.pipeline().addLast(new NettyEncoder(new KryoSerializer(), RpcRequest.class));

                    }
                });
    }

    /**
     * 发送消息到服务器
     *
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    public RpcResponse sendMessage(RpcRequest rpcRequest) {
        try {
            ChannelFuture f = bootstrap.connect(host, port).sync();
            log.info("client connect {}", host + ":" + port);
            Channel channel = f.channel();
            log.info("send message");
            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        logger.info("client send message: [{}]", rpcRequest.toString());
                    } else {
                        logger.error("Send failed:", future.cause());
                    }
                });
                // 阻塞等待，直到Channel关闭
                channel.closeFuture().sync();
                // 将服务端返回的RpcResponse对象取出返回
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                return channel.attr(key).get();
            }
        } catch (InterruptedException e) {
            log.error("occur exception when connect server:", e);
        }
        return null;
    }
}

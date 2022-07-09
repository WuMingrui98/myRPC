package xyz.raygetoffer.remoting.communication.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.enums.RpcErrorMessageEnum;
import xyz.raygetoffer.exception.RpcException;
import xyz.raygetoffer.remoting.communication.IRpcRequestCommunication;
import xyz.raygetoffer.remoting.communication.netty.client.handler.NettyRpcClientHandler;
import xyz.raygetoffer.remoting.communication.netty.codec.NettyRpcDecoder;
import xyz.raygetoffer.remoting.communication.netty.codec.NettyRpcEncoder;
import xyz.raygetoffer.remoting.communication.netty.codec.ProtocolFrameDecoder;
import xyz.raygetoffer.remoting.dto.RpcMessage;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用Netty实现RPC客户端
 *
 * @author mingruiwu
 * @create 2022/6/30 14:32
 * @description
 */
@Slf4j
public class NettyRpcClient implements IRpcRequestCommunication {
    private final String host;
    private final int port;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelContainer channelContainer;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.unprocessedRequests = new UnprocessedRequests();
        this.channelContainer = new ChannelContainer();

        // 初始化bootstrap和eventLoopGroup
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //  The timeout period of the connection.
                //  If this time is exceeded or the connection cannot be established, the connection fails.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 开启心跳机制，设置写空闲时间为5s
                        ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new ProtocolFrameDecoder());
//                        ch.pipeline().addLast(new NettyRpcEncoder());
//                        ch.pipeline().addLast(new NettyRpcDecoder());
//                        ch.pipeline().addLast(new NettyRpcClientHandler());
                    }
                });
    }

    /**
     * 用于连接服务端（目标方法所在的服务器）并返回对应的 Channel。当我们知道了服务端的地址之后，我们就可以通过 NettyClient 成功连接服务端了
     *
     * @param inetSocketAddress 服务端地址
     * @return 和服务端通信的channel
     */
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("The client has connected [{}] successfully", inetSocketAddress.toString());
                    completableFuture.complete(((ChannelFuture) future).channel());
                } else {
                    throw new IllegalStateException();
                }
            }
        });
        // 同步等待结果
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RpcException(RpcErrorMessageEnum.CLIENT_CONNECT_SERVER_FAILURE);
        }
    }


    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 构造返回值
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 假设服务端地址固定
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        // 获得服务端对应的channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // 放入服务器端未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            // 构造RpcMessage
            RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                    .build();
            channel.writeAndFlush(rpcRequest).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("client send message: [{}] successfully", rpcMessage);
                    } else {
                        // 出错，则关闭channel，并将future设置为completeExceptionally
                        future.channel().close();
                        resultFuture.completeExceptionally(future.cause());
                        log.error("send failed: {}", future.cause().toString());
                    }
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }

    /**
     * 根据ip+port获得对应的channel，先从channelContainer中获取，如果不存在，则进行连接，再放入容器中
     * @param inetSocketAddress
     * @return
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelContainer.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelContainer.setChannel(inetSocketAddress, channel);
        }
        return channel;
    }
}

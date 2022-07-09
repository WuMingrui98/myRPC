package xyz.raygetoffer.remoting.communication.netty.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.communication.netty.client.NettyRpcClient;
import xyz.raygetoffer.remoting.communication.netty.client.UnprocessedRequests;
import xyz.raygetoffer.remoting.constants.RpcConstants;
import xyz.raygetoffer.remoting.dto.RpcMessage;
import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;

/**
 * @author mingruiwu
 * @create 2022/7/1 10:52
 * @description
 */
@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final UnprocessedRequests unprocessedRequests;
    private final NettyRpcClient nettyRpcClient;

    public NettyRpcClientHandler(UnprocessedRequests unprocessedRequests, NettyRpcClient nettyRpcClient) {
        this.unprocessedRequests = unprocessedRequests;
        this.nettyRpcClient = nettyRpcClient;
    }

    // TODO client channelRead0
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        log.info("client receive msg: [{}]", msg.toString());
        // 声明一个 AttributeKey 对象
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        // 将服务端的返回结果保存到 AttributeMap 上，AttributeMap 可以看作是一个Channel的共享数据源
        // AttributeMap的key是AttributeKey，value是Attribute
        ctx.channel().attr(key).set(msg);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }

    // Netty心跳机制，保证客户端和服务端之间的长连接, 自动发送ping消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO 心跳机制完善
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 触发写空闲事件
            if(idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
//                rpcMessage.setCodec();
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
//                rpcMessage.setCompress();
//                rpcMessage.setData();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

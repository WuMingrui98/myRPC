package xyz.raygetoffer.remoting.communication.netty.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.config.DefaultConfig;
import xyz.raygetoffer.factory.SingletonFactory;
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
@Component
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private final UnprocessedRequests unprocessedRequests;
    private final NettyRpcClient nettyRpcClient;


    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    /**
    SimpleChannelInboundHandler内部自动释放了ByteBuf
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        log.info("client receive msg: [{}]", msg.toString());
        //根据msg的类型进行对应的处理
        byte messageType = msg.getMessageType();
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            log.info("heart [{}]", msg.getData());
        } else if (messageType == RpcConstants.RESPONSE_TYPE) {
            RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg.getData();
            unprocessedRequests.complete(rpcResponse);
        }
    }

    /**
     * 当处理消息出现异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }

    /**
     * Netty心跳机制，保证客户端和服务端之间的长连接, 自动发送ping消息
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 触发写空闲事件
            if(idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setCodec(DefaultConfig.getDefaultCodecCode());
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setCompress(DefaultConfig.getDefaultCompressCode());
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

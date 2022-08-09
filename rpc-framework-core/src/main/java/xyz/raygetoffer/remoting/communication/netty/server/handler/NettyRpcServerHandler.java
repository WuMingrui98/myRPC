package xyz.raygetoffer.remoting.communication.netty.server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.config.DefaultConfig;
import xyz.raygetoffer.factory.SingletonFactory;
import xyz.raygetoffer.remoting.constants.RpcConstants;
import xyz.raygetoffer.remoting.dto.RpcMessage;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;
import xyz.raygetoffer.remoting.handler.RpcRequestHandler;

/**
 * @author mingruiwu
 * @create 2022/7/1 09:56
 * @description
 */
@Slf4j
@Component
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcMessage rpcMessage = (RpcMessage) msg;
            log.info("server receive msg: [{}]", rpcMessage);
            // 判断rpcRequest的类型
            byte messageType = ((RpcMessage) msg).getMessageType();
            RpcMessage responseMsg = new RpcMessage();
            responseMsg.setCompress(DefaultConfig.getDefaultCompressCode());
            responseMsg.setCodec(DefaultConfig.getDefaultCodecCode());
            if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                responseMsg.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                responseMsg.setData(RpcConstants.PONG);
            } else {
                RpcRequest rpcRequest = (RpcRequest) rpcMessage.getData();
                responseMsg.setMessageType(RpcConstants.RESPONSE_TYPE);
                Object service = rpcRequestHandler.handle(rpcRequest);
                // 判断channel的存活状况并且是否可写
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    if (service != null) {
                        log.info(String.format("server get service: %s successfully", service.toString()));
                        RpcResponse<Object> success = RpcResponse.success(service, rpcRequest.getRequestId());
                        responseMsg.setData(success);
                    } else {
                        log.info("server failed to get service");
                        RpcResponse<Object> fail = RpcResponse.fail(rpcRequest.getRequestId());
                        responseMsg.setData(fail);
                    }
                }
            }

            ChannelFuture channelFuture = ctx.channel().writeAndFlush(responseMsg);
            channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } finally {
            // 释放byteBuf的资源
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }


    // 心跳机制
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 触发读空闲事件
            if(idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("Idle check happen, so close the connection");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

package xyz.raygetoffer.communication.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mingruiwu
 * @create 2022/7/1 09:56
 * @description
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final AtomicInteger atomicInteger = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            log.info("server receive msg: [{}] ,times:[{}]", rpcRequest, atomicInteger.getAndIncrement());
            RpcResponse<Object> messageFromServer = RpcResponse.builder().message("message from server").build();
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(messageFromServer);
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}

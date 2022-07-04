package xyz.raygetoffer.communication.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.dto.RpcResponse;

/**
 * @author mingruiwu
 * @create 2022/7/1 10:52
 * @description
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
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
}

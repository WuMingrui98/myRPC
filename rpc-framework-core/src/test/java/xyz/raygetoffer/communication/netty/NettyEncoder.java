package xyz.raygetoffer.communication.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import xyz.raygetoffer.serialize.ISerializer;

/**
 * 自定义编码器
 * <p>
 * 网络传输需要通过字节流来实现，ByteBuf 可以看作是 Netty 提供的字节数据的容器，使用它会让我们更加方便地处理字节数据。
 * <p>
 * RpcRequest -> ByteBuf
 *
 *
 * @author mingruiwu
 * @create 2022/6/30 16:38
 * @description
 */
@AllArgsConstructor
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private final ISerializer serializer;
    private final Class<?> genericClass;
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            // 1. 将对象转为byte
            byte[] body = serializer.serialize(o);
            // 2. 读取body长度
            int length = body.length;
            // 3. 写入消息对应
            byteBuf.writeInt(length);
            // 4. 将字节数组写入ByteBuf
            byteBuf.writeBytes(body);
        }
    }
}

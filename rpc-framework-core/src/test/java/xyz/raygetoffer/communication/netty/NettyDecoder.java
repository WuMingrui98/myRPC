package xyz.raygetoffer.communication.netty;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.serialize.ISerializer;

import java.util.List;

/**
 * @author mingruiwu
 * @create 2022/6/30 18:27
 * @description
 */
@AllArgsConstructor
@Slf4j
public class NettyDecoder extends ByteToMessageDecoder {
    private final ISerializer serializer;
    private final Class<?> genericClass;

    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小,存储在 ByteBuf 头部
     */
    private static final int BODY_LENGTH = 4;


    /**
     * 解码 ByteBuf 对象
     *
     * @param channelHandlerContext 解码器关联的 ChannelHandlerContext 对象
     * @param byteBuf "入站"数据，也就是 ByteBuf 对象
     * @param list "入站"数据，也就是 ByteBuf 对象
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 必须超过4个字节，因为前4个字节是表示消息的body长度的
        if(byteBuf.readableBytes() >= BODY_LENGTH) {
            byteBuf.markReaderIndex();
            int dataLength = byteBuf.readInt();
            if (dataLength < 0 || byteBuf.readableBytes() < 0) {
                log.error("data length or byteBuf readableBytes is not valid");
                return;
            }
            /*
            markReaderIndex() 用于保存 readerIndex 的位置，resetReaderIndex() 则将当前 readerIndex 重置为之前保存的位置。

            这对 API 在实现协议解码时最为常用，在读取协议内容长度字段之前，先使用 markReaderIndex() 保存了 readerIndex 的位置，
            如果 ByteBuf 中可读字节数小于长度字段的值，则表示 ByteBuf 还没有一个完整的数据包，此时直接使用 resetReaderIndex() 重置 readerIndex 的位置。
             */
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                return;
            }
            // 可以读取消息体
            byte[] body = new byte[dataLength];
            byteBuf.readBytes(body);
            // 将bytes数组反序列化为类对象
            Object obj = serializer.deserialize(genericClass, body);
            list.add(obj);
            log.info("successful decode ByteBuf to Object");
        }

    }
}

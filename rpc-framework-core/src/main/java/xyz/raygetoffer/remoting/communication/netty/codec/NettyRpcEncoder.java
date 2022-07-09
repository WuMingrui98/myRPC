package xyz.raygetoffer.remoting.communication.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import xyz.raygetoffer.remoting.constants.RpcConstants;
import xyz.raygetoffer.remoting.dto.RpcMessage;
import xyz.raygetoffer.serialize.ISerializer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义编码器
 * <pre>
 * +---------------------------------------------------------------------------+
 * | 0  1  2  3      4      5      6      7      8   9   a   b   c   d   e   f |
 * +---------------------------------------------------------------------------+
 * |  magicCode  |  ver |msgType|codec|compress|   requestID   |   dataLength  |
 * +---------------------------------------------------------------------------+
 * |                                                                           |
 * |                                 data                                      |
 * |                                 ...                                       |
 * +---------------------------------------------------------------------------+
 * </pre>
 * </p>
 * <pre>
 * 4B magicCode(magic code, 魔数)         1B ver(version, 版本)
 * 1B msgType(message type, 消息类型)      1B codec(codec type, 序列化编解码器类型)
 * 1B compress(compress type, 压缩类型)    4B requestID(request ID, 请求id)
 * 4B dataLength(data length, 数据长度)
 * </pre>
 *
 * <p>
 * 网络传输需要通过字节流来实现，ByteBuf 可以看作是 Netty 提供的字节数据的容器，使用它会让我们更加方便地处理字节数据。
 * <p>
 * RpcMessage -> ByteBuf
 *
 *
 * @author mingruiwu
 * @create 2022/6/30 16:38
 * @description
 */
@AllArgsConstructor
public class NettyRpcEncoder extends MessageToByteEncoder<RpcMessage> {
    // 计算requestId
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    private final ISerializer serializer;


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(RpcConstants.MAGIC_CODE);
        out.writeByte(RpcConstants.VERSION);
        byte msgType = msg.getMessageType();
        out.writeByte(msgType);
        out.writeByte(msg.getCodec());
        out.writeByte(msg.getCompress());
        int requestId = ATOMIC_INTEGER.getAndIncrement();
        out.writeInt(requestId);
        int dataLength = 0;
        byte[] data = null;
        // 如果msgType为heart message的话，data不需要设置
        if (msgType != RpcConstants.HEARTBEAT_REQUEST_TYPE && msgType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            // TODO 序列化和压缩拓展
            // 序列化
            data = serializer.serialize(msg.getData());

            // 压缩
            dataLength += data.length;
        }
        out.writeInt(dataLength);
        if (data != null) {
            out.writeBytes(data);
        }
    }
}

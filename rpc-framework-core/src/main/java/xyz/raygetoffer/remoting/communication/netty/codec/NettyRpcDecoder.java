package xyz.raygetoffer.remoting.communication.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.raygetoffer.remoting.constants.RpcConstants;
import xyz.raygetoffer.remoting.dto.RpcMessage;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;
import xyz.raygetoffer.serialize.ISerializer;

import java.util.Arrays;
import java.util.List;

/**
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
 * 处理ProtocolFrameDecoder处理后传来的协议数据帧
 *
 * @author mingruiwu
 * @create 2022/6/30 18:27
 * @description
 */
@AllArgsConstructor
@Slf4j
public class NettyRpcDecoder extends ByteToMessageDecoder {
    private final ISerializer serializer;

    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小,存储在 ByteBuf 头部
     */
    private static final int BODY_LENGTH = 4;


    /**
     * 解码 ByteBuf 对象
     *
     * @param channelHandlerContext 解码器关联的 ChannelHandlerContext 对象
     * @param byteBuf "入站"数据，也就是 ByteBuf 对象
     * @param list 传给下一个handler的对象
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (byteBuf.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
            Object obj = decodeFrame(byteBuf);
            list.add(obj);
            byteBuf.release();
        }
    }

    private Object decodeFrame(ByteBuf in) {
        // 检查魔数和版本
        checkMagicCode(in);
        checkVersion(in);
        byte msgType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        int dataLength = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(msgType)
                .codec(codecType)
                .requestId(requestId)
                .compress(compressType).build();
        if (msgType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (msgType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        if (dataLength > 0) {
            byte[] bytes = new byte[dataLength];
            in.readBytes(bytes);
            // TODO 解压数据和反序列拓展
            // 解压数据

            // 反序列化数据
            if (msgType == RpcConstants.REQUEST_TYPE) {
                RpcRequest rpcRequest = serializer.deserialize(RpcRequest.class, bytes);
                rpcMessage.setData(rpcRequest);
            } else {
                RpcResponse rpcResponse = serializer.deserialize(RpcResponse.class, bytes);
                rpcMessage.setData(rpcResponse);
            }
        }
        return rpcMessage;
    }


    /**
     * 检查版本是否符合要求
     * @param in 传入的ByteBuf对象
     */
    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("Version is not compatible with" + version);
        }
    }

    /**
     * 检查数据帧的魔数是否是规定的魔数
     * @param in 传入的ByteBuf对象
     */
    private void checkMagicCode(ByteBuf in) {
        int len = RpcConstants.MAGIC_CODE.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for(int i = 0; i < len; i++) {
            if (RpcConstants.MAGIC_CODE[i] != tmp[i]) {
                throw new RuntimeException("Unknown magic code:" + Arrays.toString(tmp));
            }
        }
    }
}

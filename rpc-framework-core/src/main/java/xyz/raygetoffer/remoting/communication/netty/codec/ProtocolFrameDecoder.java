package xyz.raygetoffer.remoting.communication.netty.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import xyz.raygetoffer.remoting.constants.RpcConstants;

/**
 *
 * 协议格式：
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
 * LengthFieldBasedFrameDecoder是基于长度的解码器，确保接到的 ByteBuf 消息是完整的
 * Netty从TCP缓冲区中读取字节, 把这些字节交给LengthFieldBasedFrameDecoder进行解码, 解码的操作是根据设定的规则, 根据规则, 从字节中解码出来有意义的数据, 然后把数据再交给后续的Handler处理.
 * @author mingruiwu
 * @create 2022/7/5 15:17
 * @description
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength
     *        the maximum length of the frame.  If the length of the frame is
     *        greater than this value, {@link TooLongFrameException} will be
     *        thrown.
     * @param lengthFieldOffset
     *        the offset of the length field
     * @param lengthFieldLength
     *        the length of the length field
     * @param lengthAdjustment
     *        the compensation value to add to the value of the length field
     * @param initialBytesToStrip
     *        the number of first bytes to strip out from the decoded frame
     */
    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    /**
     * 参考例子
     * <pre>
     * <b>lengthFieldOffset</b>   = <b>2</b> (= the length of Header 1)
     * <b>lengthFieldLength</b>   = <b>3</b>
     * lengthAdjustment    = 0
     * initialBytesToStrip = 0
     *
     * BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
     * +----------+----------+----------------+      +----------+----------+----------------+
     * | Header 1 |  Length  | Actual Content |----->| Header 1 |  Length  | Actual Content |
     * |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      |  0xCAFE  | 0x00000C | "HELLO, WORLD" |
     * +----------+----------+----------------+      +----------+----------+----------------+
     * </pre>
     *
     * lengthFieldOffset: 4 + 1 + 1 + 1 + 1 + 4 = 12
     * lengthFieldLength: 数据长度为4B
     * lengthAdjustment: dataLength后面直接跟着data，所以不需要调整
     * initialBytesToStrip: 因为后续需要对数据帧进行校验，所以需要保证数据帧的完整，不需要跳过任何字节
     *
     */
    public ProtocolFrameDecoder() {
        super(RpcConstants.MAX_FRAME_LENGTH, 12, 4, 0, 0);
    }
}

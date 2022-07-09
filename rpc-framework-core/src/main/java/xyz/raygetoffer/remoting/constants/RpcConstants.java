package xyz.raygetoffer.remoting.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author mingruiwu
 * @create 2022/7/5 13:59
 * @description
 */
public interface RpcConstants {
    byte[]  MAGIC_CODE = {(byte) 'm', (byte) 'r', (byte) 'p', (byte) 'c'};
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    // 版本
    byte VERSION = 1;

    byte TOTAL_LENGTH = 16;

    // message type
    byte REQUEST_TYPE = 1;
    byte RESPONSE_TYPE = 2;
    byte HEARTBEAT_REQUEST_TYPE = 3;
    byte HEARTBEAT_RESPONSE_TYPE = 4;

    int HEAD_LENGTH = 16;
    String PING = "ping";
    String PONG = "pong";

    // 数据的最大长度
    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}

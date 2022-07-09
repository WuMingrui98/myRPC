package xyz.raygetoffer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mingruiwu
 * @create 2022/7/4 10:07
 * @description
 */
@Getter
@AllArgsConstructor
public enum RpcErrorMessageEnum {
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_CAN_NOT_BE_FOUND("没有找到指定服务"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误，请求和返回的响应不匹配");

    private final String message;
}

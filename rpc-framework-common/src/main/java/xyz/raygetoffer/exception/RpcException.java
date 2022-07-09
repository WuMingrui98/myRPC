package xyz.raygetoffer.exception;

import xyz.raygetoffer.enums.RpcErrorMessageEnum;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:15
 * @description
 */
public class RpcException extends RuntimeException{
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }
}

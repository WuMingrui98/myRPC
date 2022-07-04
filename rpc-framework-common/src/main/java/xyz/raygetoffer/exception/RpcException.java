package xyz.raygetoffer.exception;

/**
 * @author mingruiwu
 * @create 2022/6/29 17:15
 * @description
 */
public class RpcException extends RuntimeException{
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}

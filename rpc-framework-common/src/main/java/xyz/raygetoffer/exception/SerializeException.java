package xyz.raygetoffer.exception;

/**
 * @author mingruiwu
 * @create 2022/6/28 11:12
 * @description 序列化的异常
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String message) {
        super(message);
    }
}

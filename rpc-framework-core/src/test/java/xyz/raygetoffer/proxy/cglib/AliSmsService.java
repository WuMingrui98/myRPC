package xyz.raygetoffer.proxy.cglib;

/**
 * @author mingruiwu
 * @create 2022/7/1 16:19
 * @description
 */
public class AliSmsService {
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}

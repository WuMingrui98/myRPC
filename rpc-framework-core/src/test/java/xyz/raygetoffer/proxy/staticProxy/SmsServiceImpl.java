package xyz.raygetoffer.proxy.staticProxy;

/**
 * @author mingruiwu
 * @create 2022/7/1 15:17
 * @description
 */
public class SmsServiceImpl implements SmsService{
    @Override
    public void send(String message) {
        System.out.println("send message:" + message);
    }
}

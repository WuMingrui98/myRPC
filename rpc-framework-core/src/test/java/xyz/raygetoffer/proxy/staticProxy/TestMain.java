package xyz.raygetoffer.proxy.staticProxy;

/**
 * @author mingruiwu
 * @create 2022/7/1 15:20
 * @description
 */
public class TestMain {
    public static void main(String[] args) {
        SmsService smsService = new SmsServiceImpl();
        SmsProxy smsProxy = new SmsProxy(smsService);
        smsProxy.send("java");
    }
}

package xyz.raygetoffer.proxy.jdk;

/**
 * @author mingruiwu
 * @create 2022/7/1 16:02
 * @description
 */
public class TestMain {
    public static void main(String[] args) {
        SmsService smsService = new SmsServiceImpl();
        SmsService smsProxy = (SmsService) JdkProxyFactory.getProxy(smsService);
        smsProxy.send("java");
    }
}

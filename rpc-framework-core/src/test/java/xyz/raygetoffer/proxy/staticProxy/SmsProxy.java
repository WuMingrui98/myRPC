package xyz.raygetoffer.proxy.staticProxy;

/**
 * @author mingruiwu
 * @create 2022/7/1 15:18
 * @description
 */
public class SmsProxy implements SmsService{
    private final SmsService smsService;

    public SmsProxy(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(String message) {
        System.out.println("before method send()");
        smsService.send(message);
        System.out.println("after method send()");
    }
}

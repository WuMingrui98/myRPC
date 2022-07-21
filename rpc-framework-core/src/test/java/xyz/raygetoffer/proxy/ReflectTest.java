package xyz.raygetoffer.proxy;

import xyz.raygetoffer.proxy.jdk.SmsService;
import xyz.raygetoffer.proxy.jdk.SmsServiceImpl;

import java.util.Arrays;
import java.util.Random;

/**
 * @author mingruiwu
 * @create 2022/7/9 17:23
 * @description
 */
public class ReflectTest {
    public static void main(String[] args) {
//        System.out.println(Random.class.getName());
//        System.out.println(Random.class.getCanonicalName());
//        System.out.println(Random.class);
        SmsService smsService = new SmsServiceImpl();
        System.out.println(SmsService.class.cast(smsService));
    }
}

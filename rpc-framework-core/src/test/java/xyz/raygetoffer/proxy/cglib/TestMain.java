package xyz.raygetoffer.proxy.cglib;

/**
 * @author mingruiwu
 * @create 2022/7/1 16:44
 * @description
 */
public class TestMain {
    public static void main(String[] args) {
        AliSmsService proxy = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
        proxy.send("java");
    }
}

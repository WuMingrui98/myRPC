package xyz.raygetoffer;

import org.springframework.stereotype.Component;
import xyz.raygetoffer.annotation.RpcReference;

/**
 * @author mingruiwu
 * @create 2022/8/7 16:56
 * @description
 */
@Component
public class HelloController {
    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111")));
        }
    }
}

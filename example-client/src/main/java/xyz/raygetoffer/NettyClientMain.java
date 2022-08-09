package xyz.raygetoffer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import xyz.raygetoffer.annotation.RpcScan;

/**
 * @author mingruiwu
 * @create 2022/8/7 16:47
 * @description
 */
@RpcScan(basePackages = {"xyz.raygetoffer"})
public class NettyClientMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}

package xyz.raygetoffer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.annotation.RpcService;

/**
 * @author mingruiwu
 * @create 2022/8/7 17:03
 * @description
 */
@Slf4j
@RpcService(group = "test1", version = "version1")
public class HelloServiceImp implements HelloService{
    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        return hello.getMessage();
    }
}

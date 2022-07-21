package xyz.raygetoffer.annotation;

import org.springframework.context.annotation.Import;
import xyz.raygetoffer.spring.RpcScanScannerRegistrar;

import java.lang.annotation.*;

/**
 * @author mingruiwu
 * @create 2022/7/14 15:58
 * @description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScanScannerRegistrar.class)
@Documented
public @interface RpcScan {
    String[] basePackages();
}

package xyz.raygetoffer.extension;

import java.lang.annotation.*;

/**
 * @author mingruiwu
 * @create 2022/7/15 16:26
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SPI {
    String value() default "";
}

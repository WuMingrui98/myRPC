package xyz.raygetoffer.annotation;

import java.lang.annotation.*;

/**
 * 消费服务
 *
 * @author mingruiwu
 * @create 2022/7/9 17:58
 * @description
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcReference {
    /**
     * 分组，默认为空
     */
    String group() default "";

    /**
     * 版本，默认为空
     */
    String version() default "";
}

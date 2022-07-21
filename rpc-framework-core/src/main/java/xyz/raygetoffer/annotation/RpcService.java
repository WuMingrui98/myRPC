package xyz.raygetoffer.annotation;

import java.lang.annotation.*;

/**
 * 注册服务
 *
 * @author mingruiwu
 * @create 2022/7/9 17:57
 * @description
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {
    /**
     * 分组，默认为空
     */
    String group() default "";

    /**
     * 版本，默认为空
     */
    String version() default "";
}

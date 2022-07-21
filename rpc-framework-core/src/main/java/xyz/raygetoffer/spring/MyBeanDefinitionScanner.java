package xyz.raygetoffer.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * 自定义scanner，将指定包路径下的添加了指定注解的类扫描到IOC容器中
 *
 * @author mingruiwu
 * @create 2022/7/14 14:51
 * @description
 */
public class MyBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    public MyBeanDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotationType) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annotationType));
    }
}

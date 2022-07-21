package xyz.raygetoffer.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import xyz.raygetoffer.annotation.RpcScan;
import xyz.raygetoffer.annotation.RpcService;

/**
 * 通过@Import引⼊ImportBeanDefinitionRegistrar的实现类
 * 实现扫描@RpcScan注解下指定的包中被@RpcService注解的类，并加入到IOC容器中
 * @author mingruiwu
 * @create 2022/7/14 15:36
 * @description
 */
@Slf4j
public class RpcScanScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackages";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获得@RpcScan注解的属性
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // 获得@RpcScan注解中设置的backPackages的值
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        // @RpcScan注解中设置的backPackages的值为空时，默认获得@RpcScan注解的类所在包
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[] {((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName()};
        }
        // 扫描包下有RpcService注解的类
        MyBeanDefinitionScanner rpcServiceScanner = new MyBeanDefinitionScanner(registry, RpcService.class);
        if(resourceLoader != null) {
            rpcServiceScanner.setResourceLoader(resourceLoader);
        }
        int rpcServiceCount = rpcServiceScanner.scan(rpcScanBasePackages);
        log.info("rpcServiceScanner扫描的数量 [{}]", rpcServiceCount);
    }
}

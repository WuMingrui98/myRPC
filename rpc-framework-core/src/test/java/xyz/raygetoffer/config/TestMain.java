package xyz.raygetoffer.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

/**
 * @author mingruiwu
 * @create 2022/7/6 13:54
 * @description
 */
@SpringBootApplication
public class TestMain{
    @Autowired
    private MyConfig myConfig;

    @Test
    public void propsTest() {
        PropertiesLoaderUtils
        System.out.println(myConfig);
    }
}

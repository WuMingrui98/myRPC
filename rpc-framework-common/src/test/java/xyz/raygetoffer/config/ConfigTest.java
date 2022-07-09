package xyz.raygetoffer.config;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import xyz.raygetoffer.utils.PropertiesFileUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * @author mingruiwu
 * @create 2022/7/6 15:20
 * @description
 */
public class ConfigTest {
    public static void main(String[] args) throws IOException {
//        Properties properties = PropertiesLoaderUtils.loadAllProperties(("test.properties"));
        Properties properties = PropertiesFileUtil.readProperties(("test.properties"));
        System.out.println(properties.getProperty("test1"));
        System.out.println(properties.getProperty("test2"));
    }
}

package xyz.raygetoffer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author mingruiwu
 * @create 2022/7/6 15:02
 * @description
 */
@Slf4j
public class PropertiesFileUtil {
    private PropertiesFileUtil() {
    }

    public static Properties readProperties(String fileName) {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(fileName);
        } catch (IOException e) {
            log.error("Occur exception when read properties file [{}]", fileName);
        }
        return properties;
    }
}

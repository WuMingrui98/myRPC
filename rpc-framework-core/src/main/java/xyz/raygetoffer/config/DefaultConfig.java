package xyz.raygetoffer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.enums.CompressTypeEnum;
import xyz.raygetoffer.enums.SerializationTypeEnum;

/**
 * 获得默认配置
 * @author mingruiwu
 * @create 2022/8/6 18:52
 * @description
 */
@Component
public class DefaultConfig {

    private static String serializationName;


    private static String compressName;

    @Value("hessian")
    public void setSerializationName(String serializationName) {
        DefaultConfig.serializationName = serializationName;
    }

    @Value("gzip")
    public void setCompressName(String compressName) {
        DefaultConfig.compressName = compressName;
    }

    public static byte getDefaultCodecCode() {
        return SerializationTypeEnum.getCode(serializationName);
    }

    public static byte getDefaultCompressCode() {
        return CompressTypeEnum.getCode(compressName);
    }

}

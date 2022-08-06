package xyz.raygetoffer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.raygetoffer.enums.SerializationTypeEnum;

/**
 * 获得默认配置
 * @author mingruiwu
 * @create 2022/8/6 18:52
 * @description
 */
@Component
public class DefaultConfig {
    @Value("${myconfig.serializationName}")
    private String serializationName;

    @Value("${myconfig.compressName}")
    private String compressName;

    public byte getDefaultCodecCode() {
        return SerializationTypeEnum.getCode(serializationName);
    }

    public byte getDefaultCompressCode() {
        return SerializationTypeEnum.getCode(compressName);
    }

}

package xyz.raygetoffer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mingruiwu
 * @create 2022/8/6 15:44
 * @description
 */
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {
    GZIP((byte) 0x01, "gzip"),
    LZ4((byte) 0x02, "lz4");

    private final byte code;
    private final String name;

    /**
     * 根据对应的code获得压缩方式的名称
     * @param code
     * @return 压缩方式的名称
     */
    public static String getName(byte code) {
        CompressTypeEnum[] values = CompressTypeEnum.values();
        for (CompressTypeEnum value : values) {
            if (code == value.code) {
                return value.name;
            }
        }
        throw new RuntimeException("Can not get correct compress name!");
    }

    /**
     * 根据对应的压缩方法名称获取压缩方式的code
     * @param name 压缩名称
     * @return 压缩名对应的code
     */
    public static byte getCode(String name) {
        CompressTypeEnum[] values = CompressTypeEnum.values();
        for (CompressTypeEnum value : values) {
            if (value.name.equals(name)) {
                return value.code;
            }
        }
        throw new RuntimeException("Can not get correct compress code!");
    }
}

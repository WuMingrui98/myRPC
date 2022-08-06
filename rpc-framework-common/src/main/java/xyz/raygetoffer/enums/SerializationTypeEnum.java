package xyz.raygetoffer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author mingruiwu
 * @create 2022/8/6 15:52
 * @description
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {
    HESSIAN((byte) 0x01, "hessian"),
    KRYO((byte) 0x02, "kyro"),
    PROTOSTUFF((byte) 0x03, "protostuff"),
    JDK((byte) 0x04, "jdk");
    private final byte code;
    private final String name;

    /**
     * 根据对应的code获得序列化方式的名称
     * @param code
     * @return 压缩方式的名称
     */
    public static String getName(byte code) {
        SerializationTypeEnum[] values = SerializationTypeEnum.values();
        for (SerializationTypeEnum value : values) {
            if (code == value.code) {
                return value.name;
            }
        }
        throw new RuntimeException("Can not get correct serialization name");
    }


    /**
     * 根据对应的序列化方法名称获取压缩方式的code
     * @param name 序列化名称
     * @return 序列化名称对应的code
     */
    public static byte getCode(String name) {
        SerializationTypeEnum[] values = SerializationTypeEnum.values();
        for (SerializationTypeEnum value : values) {
            if (value.name.equals(name)) {
                return value.code;
            }
        }
        throw new RuntimeException("Can not get correct serialization code!");
    }
}

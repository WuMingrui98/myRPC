package xyz.raygetoffer.serialize;

/**
 * @author mingruiwu
 * @create 2022/6/28 10:56
 * @description 序列化接口，完成对象的序列化和反序列化
 */
public interface ISerializer {

    /**
     * 序列化
     * @param obj 序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param tClass 目标类
     * @param bytes 字节数组
     * @param <T> 目标类的泛型
     * @return 反序列化后生成的对象
     */
    <T> T deserialize(Class<T> tClass, byte[] bytes);
}

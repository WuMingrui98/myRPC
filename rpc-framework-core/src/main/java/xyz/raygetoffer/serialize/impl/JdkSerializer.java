package xyz.raygetoffer.serialize.impl;

import xyz.raygetoffer.exception.SerializeException;
import xyz.raygetoffer.serialize.ISerializer;

import java.io.*;

/**
 * @author mingruiwu
 * @create 2022/6/28 11:02
 * @description 用jdk原始的序列化方式，实现序列化和反序列化
 */
public class JdkSerializer implements ISerializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream =  new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(Class<T> tClass, byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object o = objectInputStream.readObject();
            return tClass.cast(o);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}

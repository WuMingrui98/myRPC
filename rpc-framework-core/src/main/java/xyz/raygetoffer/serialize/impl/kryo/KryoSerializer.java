package xyz.raygetoffer.serialize.impl.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import xyz.raygetoffer.exception.SerializeException;
import xyz.raygetoffer.remoting.dto.RpcRequest;
import xyz.raygetoffer.remoting.dto.RpcResponse;
import xyz.raygetoffer.serialize.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author mingruiwu
 * @create 2022/6/28 11:23
 * @description 使用kryo实现序列化和反序列
 */
public class KryoSerializer implements ISerializer {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        /* 显示地为类注册id
        Class IDs -1 and -2 are reserved.
        Class IDs 0-8 are used by default for primitive types and String, though these IDs can be repurposed.
         */
        kryo.register(RpcRequest.class, 9);
        kryo.register(RpcResponse.class, 10);
        return kryo;
    });
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            // 需要调用remove方法防止内存泄露
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(Class<T> tClass, byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            T t = kryo.readObject(input, tClass);
            // 需要调用remove方法防止内存泄露
            kryoThreadLocal.remove();
            return t;
        } catch (IOException e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}

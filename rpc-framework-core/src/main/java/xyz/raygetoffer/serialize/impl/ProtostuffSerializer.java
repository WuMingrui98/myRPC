package xyz.raygetoffer.serialize.impl;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import xyz.raygetoffer.serialize.ISerializer;

/**
 * @author mingruiwu
 * @create 2022/6/28 15:09
 * @description 使用protostuff完成序列化和反序列化
 */
public class ProtostuffSerializer implements ISerializer {
    /**
     * 避免每次序列化的时候都重新分配buffer
     */
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object obj) {
        Class<?> aClass = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(aClass);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(Class<T> tClass, byte[] bytes) {
        Schema<T> schema = RuntimeSchema.getSchema(tClass);
        T t = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }
}

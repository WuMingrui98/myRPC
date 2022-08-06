package xyz.raygetoffer.serialize.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import xyz.raygetoffer.exception.SerializeException;
import xyz.raygetoffer.serialize.ISerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author mingruiwu
 * @create 2022/6/28 15:32
 * @description
 */
public class HessianSerializer implements ISerializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
            hessian2Output.startMessage();
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            hessian2Output.completeMessage();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializeException("Serialization failed");
        }
    }

    @Override
    public <T> T deserialize(Class<T> tClass, byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
            hessian2Input.startMessage();
            T t = tClass.cast(hessian2Input.readObject());
            hessian2Input.completeMessage();
            return t;
        } catch (IOException e) {
            throw new SerializeException("Deserialization failed");
        }
    }
}

package xyz.raygetoffer.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import xyz.raygetoffer.serialize.Student;

import java.io.*;

/**
 * @author mingruiwu
 * @create 2022/6/28 09:31
 * @description
 */
public class ProtostuffSerializeTest {
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    public static void main(String[] args) throws IOException {
        Student student = Student.builder().age(10).name("wmr").score(100).build();
        Class<Student> aClass = (Class<Student>) student.getClass();
        Schema<Student> schema = RuntimeSchema.getSchema(aClass);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(student, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }

        File file = new File(ProtostuffSerializeTest.class.getClassLoader().getResource("").getPath() + "/file1.bin");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.close();
        FileInputStream fileInputStream = new FileInputStream(file);
        long length = file.length();
        byte[] newBytes = new byte[(int) length];
        fileInputStream.read(newBytes);
        Student student1 = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(newBytes, student1, schema);
        System.out.println(student1);
    }
}

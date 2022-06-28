package xyz.raygetoffer.serialize.jdk;

import org.junit.Test;
import xyz.raygetoffer.serialize.Student;

import java.io.*;

/**
 * @author mingruiwu
 * @create 2022/6/27 21:57
 * @description
 */
public class JDKSerializeTest {
    @Test
    public void testJDKSerialize() throws IOException, ClassNotFoundException {
        Student student = Student.builder().age(12).name("wmr").score(100).build();
        serialize(student);
        Student studentDeSerialize = (Student) deserialize();
        System.out.println(studentDeSerialize);

    }

    public void serialize(Object o) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("").getPath() + "/student");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
    }

    public Object deserialize() throws IOException, ClassNotFoundException {
        File file = new File(this.getClass().getClassLoader().getResource("").getPath() + "/student");
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        return o;
    }
}

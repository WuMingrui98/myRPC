package xyz.raygetoffer.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import xyz.raygetoffer.serialize.Student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author mingruiwu
 * @create 2022/6/27 23:21
 * @description
 */
public class KryoSerializeTest {
    public static void main(String[] args) throws FileNotFoundException {
        Kryo kryo = new Kryo();
        kryo.register(Student.class);
        Student student = Student.builder().age(5).name("WMR").score(100).build();

        Output output = new Output(new FileOutputStream(KryoSerializeTest.class.getClassLoader().getResource("").getPath() + "/file.bin"));
        kryo.writeObject(output, student);
        output.close();

        Input input = new Input(new FileInputStream(KryoSerializeTest.class.getClassLoader().getResource("").getPath() + "/file.bin"));
        Student student1 = kryo.readObject(input, Student.class);
        System.out.println(student1);
        input.close();
    }
}

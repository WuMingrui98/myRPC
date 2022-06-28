package xyz.raygetoffer.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import xyz.raygetoffer.serialize.Student;

import java.io.*;

/**
 * @author mingruiwu
 * @create 2022/6/28 10:12
 * @description
 */
public class HessianSerializeTest {
    public static void main(String[] args) throws IOException {
        Student student = Student.builder().age(10).name("wmr").score(100).build();
        File file = new File(HessianSerializeTest.class.getClassLoader().getResource("").getPath() + "/file3.bin");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
            hessian2Output.startMessage();
            hessian2Output.writeObject(student);
            hessian2Output.flush();
            hessian2Output.completeMessage();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long length = file.length();
        byte[] newBytes = new byte[(int) length];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(newBytes);


        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(newBytes)) {
            Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
            hessian2Input.startMessage();
            Student student1 = (Student) hessian2Input.readObject();
            hessian2Input.completeMessage();
            System.out.println(student1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

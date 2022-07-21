package xyz.raygetoffer.extension;

import xyz.raygetoffer.serialize.ISerializer;

/**
 * @author mingruiwu
 * @create 2022/7/21 15:24
 * @description
 */
public class TestExtensionLoader {
    public static void main(String[] args) {
        ISerializer extension = ExtensionLoader.getExtensionLoader(ISerializer.class).getExtension("hessian");
        System.out.println(extension);
    }
}

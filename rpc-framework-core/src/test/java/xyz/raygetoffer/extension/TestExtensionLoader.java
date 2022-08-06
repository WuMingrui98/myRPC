package xyz.raygetoffer.extension;

import xyz.raygetoffer.compress.ICompress;
import xyz.raygetoffer.serialize.ISerializer;

/**
 * @author mingruiwu
 * @create 2022/7/21 15:24
 * @description
 */
public class TestExtensionLoader {
    public static void main(String[] args) {
//        ISerializer extension = ExtensionLoader.getExtensionLoader(ISerializer.class).getExtension();
        ICompress extension = ExtensionLoader.getExtensionLoader(ICompress.class).getExtension();
        ICompress extension2 = ExtensionLoader.getExtensionLoader(ICompress.class).getExtension();
        System.out.println(extension);
        System.out.println(extension2);
    }
}

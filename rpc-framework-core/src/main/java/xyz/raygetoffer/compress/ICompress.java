package xyz.raygetoffer.compress;

/**
 * @author mingruiwu
 * @create 2022/7/10 15:10
 * @description
 */
public interface ICompress {
    /**
     * 压缩
     * @param bytes
     * @return
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压缩
     * @param bytes
     * @return
     */
    byte[] decompress(byte[] bytes);
}

package xyz.raygetoffer.compress;

import xyz.raygetoffer.extension.SPI;

/**
 * @author mingruiwu
 * @create 2022/7/10 15:10
 * @description
 */
@SPI("gzip")
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

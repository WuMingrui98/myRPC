package xyz.raygetoffer.compress;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.junit.Test;
import xyz.raygetoffer.compress.impl.Lz4Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

/**
 * @author mingruiwu
 * @create 2022/7/10 15:18
 * @description
 */
public class CompressTest {
    private static final int BUFFER_SIZE = 1024 * 4;


    @Test
    public void testGzip() {
        byte[] bytes = new byte[1024];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) i;
        }
        Lz4Compress lz4Compress = new Lz4Compress();
        byte[] compress = lz4Compress.compress(bytes);
        byte[] decompress = lz4Compress.decompress(compress);
        System.out.println(compress.length);
        System.out.println(decompress.length);
    }




}

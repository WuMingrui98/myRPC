package xyz.raygetoffer.compress.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import xyz.raygetoffer.compress.ICompress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 使用gzip实现压缩
 * @author mingruiwu
 * @create 2022/7/10 16:01
 * @description
 */
public class GzipCompress implements ICompress {
    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GzipCompressorOutputStream gzipOut = new GzipCompressorOutputStream(byteArrayOutputStream)) {
            gzipOut.write(bytes, 0, bytes.length);
            gzipOut.flush();
            gzipOut.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(byteArrayInputStream)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gzipIn.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }
}

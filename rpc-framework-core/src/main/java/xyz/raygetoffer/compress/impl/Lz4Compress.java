package xyz.raygetoffer.compress.impl;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorInputStream;
import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorOutputStream;
import xyz.raygetoffer.compress.ICompress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 使用LZ4实现压缩
 * @author mingruiwu
 * @create 2022/7/10 16:05
 * @description
 */
public class Lz4Compress implements ICompress {
    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FramedLZ4CompressorOutputStream lz4Out = new FramedLZ4CompressorOutputStream(byteArrayOutputStream)) {
            lz4Out.write(bytes, 0, bytes.length);
            lz4Out.flush();
            lz4Out.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("lz4 compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             FramedLZ4CompressorInputStream lz4In = new FramedLZ4CompressorInputStream(byteArrayInputStream)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = lz4In.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }
}

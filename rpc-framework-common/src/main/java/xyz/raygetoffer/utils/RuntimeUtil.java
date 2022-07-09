package xyz.raygetoffer.utils;

/**
 * @author mingruiwu
 * @create 2022/7/5 09:58
 * @description
 */
public class RuntimeUtil {
    /**
     * 获得机器的CPU核心数
     * @return CPU核心数
     */
    public static int getCPUs() {
        return Runtime.getRuntime().availableProcessors();
    }
}

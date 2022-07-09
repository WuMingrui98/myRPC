package xyz.raygetoffer.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mingruiwu
 * @create 2022/7/4 10:35
 * @description
 */
@Slf4j
public class TestLog {
    private static final Logger logger = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) {
        log.info("你好");
        logger.info("你好");
    }
}

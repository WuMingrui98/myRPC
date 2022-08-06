package xyz.raygetoffer.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.raygetoffer.MyConfig;


/**
 * @author mingruiwu
 * @create 2022/7/6 13:54
 * @description
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MyConfig.class})
public class TestMain {
    @Autowired
    private DefaultConfig defaultConfig;

    @Test
    public void propsTest() {
        System.out.println(defaultConfig.getDefaultCodecCode());
    }
}

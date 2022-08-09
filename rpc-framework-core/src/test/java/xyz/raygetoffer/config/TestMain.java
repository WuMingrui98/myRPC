package xyz.raygetoffer.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author mingruiwu
 * @create 2022/7/6 13:54
 * @description
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestMain {
    @Autowired
    private DefaultConfig defaultConfig;

    @Test
    public void propsTest() {
        System.out.println(DefaultConfig.getDefaultCodecCode());
    }
}

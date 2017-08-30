package com.moninfotech;

import com.moninfotech.commons.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "com.moninfotech")
public class HrsApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void dateDiffTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR,1);
        assertEquals(1, DateUtils.getDateDiff(new Date(), calendar.getTime(), TimeUnit.HOURS));
    }

}

package com.wlkg.test;

import com.wlkg.ssm.WlkgSmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgSmsService.class)
public class SmsTest {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendTest()
    {
        Map<String,String> map = new HashMap<>();
        map.put("phone", "15554417361");
        map.put("code","4567888");
        amqpTemplate.convertAndSend("wlkg.sms.exchange","sms.verify.code",map);
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

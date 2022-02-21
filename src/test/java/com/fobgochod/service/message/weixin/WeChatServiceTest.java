package com.fobgochod.service.message.weixin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WeChatServiceTest {

    private final String accessToken = "53_e9m3_3b5x4hg4De8vCLqVPb1aq4IgeWD8_Vy_ng5dfSDp37vOKDhfPs3PpHimGQkbYTsrzmxdhlzxY8W8MMi3YiWdzibHxIpYMV6Gxn6sTqMRqnA0GmBcXm3NRlSy-B7-Jn8g0q3jSAgl3wzWWHeADAIIT";

    @Autowired
    private WeChatService weChatService;

    @Test
    void getAccessToken() {
        String accessToken = weChatService.getAccessToken();
        System.out.println("accessToken = " + accessToken);
    }
}

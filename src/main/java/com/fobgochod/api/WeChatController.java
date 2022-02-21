package com.fobgochod.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping(value = "/wechat")
public class WeChatController {

    @GetMapping(value = "/connect")
    public void connect(@RequestParam String signature,
                        @RequestParam String timestamp,
                        @RequestParam String nonce,
                        @RequestParam String echostr,
                        HttpServletResponse response) throws Exception {

        System.out.println("signature = " + signature);
        System.out.println("timestamp = " + timestamp);
        System.out.println("nonce = " + nonce);
        System.out.println("echostr = " + echostr);

        try (PrintWriter out = response.getWriter()) {
            out.write(echostr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

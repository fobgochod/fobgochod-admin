package com.fobgochod.util;

import java.util.Random;

/**
 * 验证码
 *
 * @author Xiao
 * @date 2022/3/1 0:01
 */
public class CaptchaUtils {

    private static int codeCount = 4;
    private static char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String generateCaptcha() {
        Random random = new Random();
        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < codeCount; i++) {
            String code = String.valueOf(codeSequence[random.nextInt(10)]);
            randomCode.append(code);
        }
        return randomCode.toString();
    }
}

package com.fobgochod.util;

import com.fobgochod.exception.BusinessException;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;

public final class AESCipher {

    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            messageDigest.update(bytes);
            encodeStr = Base64.encodeBase64String(messageDigest.digest());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return encodeStr;
    }
}

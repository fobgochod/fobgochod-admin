package com.fobgochod.util;

import com.fobgochod.exception.SystemException;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class AESCipher {

    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            messageDigest.update(bytes);
            encodeStr = Base64.encodeBase64String(messageDigest.digest());
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
        return encodeStr;
    }
}

package com.fobgochod.support.security;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.crypto.SecureUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {

    public final static boolean ENABLE_AES = true;
    public final static String DEFAULT_KEY = "Cyrus·Smith____";
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static void main(String[] args) {
        String data = "vivi229229";
        String s = SecureUtil.aes(DEFAULT_KEY.getBytes()).encryptBase64(data);
        System.out.println("s = " + s);
        byte[] decode = Base64Decoder.decode(s);
        String decrypt = SecureUtil.aes(DEFAULT_KEY.getBytes()).decryptStr(decode);
        System.out.println("decrypt = " + decrypt);
    }

    // 获取 cipher
    private static Cipher getCipher(byte[] key, int model) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model, secretKeySpec);
        return cipher;
    }

    // AES加密
    public static String encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }


    // AES解密
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }

    public static String encrypt(String data) {
        return SecureUtil.aes(DEFAULT_KEY.getBytes()).encryptBase64(data);
    }

    public static byte[] decrypt(String data) {
        return SecureUtil.aes(DEFAULT_KEY.getBytes()).decrypt(data);
    }

    public static byte[] decrypt(byte[] data) {
        return SecureUtil.aes(DEFAULT_KEY.getBytes()).decrypt(Base64Decoder.decode(data));
    }
}

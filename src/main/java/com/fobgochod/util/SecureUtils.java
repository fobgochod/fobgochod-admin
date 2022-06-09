package com.fobgochod.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 加解密
 *
 * @author Xiao
 * @date 2022/6/6 21:48
 */
public class SecureUtils {

    public static final String AES_SECRET_KEY;
    public static final String RSA_PUBLIC_KEY;
    public static final String RSA_PRIVATE_KEY;

    private static final Logger logger = LoggerFactory.getLogger(SecureUtils.class);
    private static final AES aes;
    private static final RSA rsa;

    static {
        Base64.encode("Cyrus Smith_____");

        aes = SecureUtil.aes();
        AES_SECRET_KEY = Base64.encode(aes.getSecretKey().getEncoded());
        logger.info("aes secret key: {}", AES_SECRET_KEY);

        rsa = SecureUtil.rsa();
        RSA_PUBLIC_KEY = rsa.getPublicKeyBase64();
        RSA_PRIVATE_KEY = rsa.getPrivateKeyBase64();
    }

    private static void aesTest() {
        String test = "AES_DATA_TEST";
        String encrypt = aesEncrypt(test, AES_SECRET_KEY);
        System.out.println("aes encrypt = " + encrypt);
        String decrypt = aesDecrypt(encrypt, AES_SECRET_KEY);
        System.out.println("aes decrypt = " + decrypt);
    }

    private static void rsaTest() {
        String test = "RSA_DATA_TEST";
        String encrypt = rsaEncrypt(test, RSA_PUBLIC_KEY);
        System.out.println("rsa encrypt = " + encrypt);
        String decrypt = rsaDecrypt(encrypt, RSA_PRIVATE_KEY);
        System.out.println("rsa decrypt = " + decrypt);
    }

    public static String rsaEncrypt(String data, String publicKeyBase64) {
        PublicKey publicKey = KeyUtil.generatePublicKey(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(), Base64.decode(publicKeyBase64));
        byte[] encrypt = rsa.setPublicKey(publicKey).encrypt(data, KeyType.PublicKey);
        return Base64.encode(encrypt);
    }

    public static String rsaDecrypt(String data, String privateKeyBase64) {
        PrivateKey privateKey = KeyUtil.generatePrivateKey(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(), Base64.decode(privateKeyBase64));
        byte[] decrypt = rsa.setPrivateKey(privateKey).decrypt(data, KeyType.PrivateKey);
        return new String(decrypt);
    }

    public static String aesEncrypt(String data, String secretKeyBase64) {
        byte[] bytes = SecureUtil.aes(Base64.decode(secretKeyBase64)).encrypt(data);
        return Base64.encode(bytes);
    }

    public static String aesDecrypt(String data, String secretKeyBase64) {
        byte[] bytes = Base64.decode(data);
        byte[] decrypt = SecureUtil.aes(Base64.decode(secretKeyBase64)).decrypt(bytes);
        return new String(decrypt);
    }

    public static String rsaEncrypt(String data) {
        byte[] encrypt = rsa.encrypt(data, KeyType.PublicKey);
        return Base64.encode(encrypt);
    }

    public static String rsaDecrypt(String data) {
        byte[] decrypt = rsa.decrypt(data, KeyType.PrivateKey);
        return new String(decrypt);
    }

    public static String sha256(String data) {
        byte[] bytes = SecureUtil.sha256().digest(data);
        byte[] bytes2 = SecureUtil.sha256().digest(bytes);
        return Base64Encoder.encode(bytes2);
    }

    public static String aesEncrypt(String data) {
        byte[] bytes = aes.encrypt(data);
        return Base64.encode(bytes);
    }

    public static byte[] aesDecrypt(byte[] data) {
        byte[] bytes = Base64.decode(data);
        return aes.decrypt(bytes);
    }

    public static String aesDecrypt(String data) {
        byte[] bytes = Base64.decode(data);
        byte[] decrypt = aes.decrypt(bytes);
        return new String(decrypt);
    }
}

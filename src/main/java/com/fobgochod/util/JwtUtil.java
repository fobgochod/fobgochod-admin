package com.fobgochod.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author chenxsa
 * @date: 2018-6-6 15:27
 * @Description:
 */
public class JwtUtil {

    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    private static final String KEY = "docSFCy76*h%(Hil";
    private static final String SUB = "admin";
    private static final String ISSUER = "issuer";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String issuer) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier =
                    JWT.require(algorithm)
                            .withIssuer(ISSUER + issuer)
                            .withSubject(SUB)
                            .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (JWTVerificationException exception) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getData(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("data").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param json json
     * @return 加密的token
     */
    public static String sign(String json, String key, String issuer, long expire) {
        if (StringUtils.isEmpty(json)) {
            throw new IllegalArgumentException("json 不能为null");
        }
        try {
            Date date = new Date(System.currentTimeMillis() + expire);
            Algorithm algorithm = Algorithm.HMAC256(key);
            // 附带username信息
            return JWT.create()
                    .withIssuer(ISSUER + issuer)
                    .withSubject(SUB)
                    .withClaim("data", json)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 生成签名,30min后过期
     *
     * @param json json
     * @return 加密的token
     */
    public static String sign(String json, String issuer) {
        return sign(json, KEY, issuer, EXPIRE_TIME);
    }
}

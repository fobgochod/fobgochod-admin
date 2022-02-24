package com.fobgochod.service.login.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.config.CacheManagerConfig;
import com.fobgochod.exception.SystemException;
import com.fobgochod.util.JsonUtils;
import com.fobgochod.util.JwtUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * DMC userToken处理
 *
 * @author zhouxiao
 * @date 2021/3/8
 */
@Service
@CacheConfig(cacheNames = CacheManagerConfig.JCacheNames.LOGIN_TOKEN)
public class UserTokenService {

    private final static ObjectMapper objectMapper = JsonUtils.createObjectMapper();

    @Cacheable(key = "#loginUser.uniqueKey()")
    public String getToken(LoginUser loginUser) {
        String json;
        try {
            json = objectMapper.writeValueAsString(loginUser);
        } catch (JsonProcessingException e) {
            throw new SystemException("Json序列化异常：" + loginUser);
        }
        return JwtUtil.sign(json, getIssuer());
    }

    private String getIssuer() {
        return "UserToken";
    }

    public boolean validate(String token) {
        return JwtUtil.verify(token, getIssuer());
    }

    /**
     * 解析 JWT Token
     *
     * @param token
     * @return
     */
    public LoginUser getData(String token) {
        if (!validate(token)) {
            throw new SystemException("不是有效的Token：" + token);
        }
        try {
            return objectMapper.readValue(JwtUtil.getData(token), LoginUser.class);
        } catch (IOException e) {
            throw new SystemException("Token解析异常：" + e.getMessage());
        }
    }
}

package com.fobgochod.service.login.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fobgochod.auth.holder.UserDetails;
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

    @Cacheable(key = "#userDetails.uniqueKey()")
    public String getToken(UserDetails userDetails) {
        String json;
        try {
            json = objectMapper.writeValueAsString(userDetails);
        } catch (JsonProcessingException e) {
            throw new SystemException("Json序列化异常：" + userDetails);
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
    public UserDetails getData(String token) {
        if (!validate(token)) {
            throw new SystemException("不是有效的Token：" + token);
        }
        try {
            return objectMapper.readValue(JwtUtil.getData(token), UserDetails.class);
        } catch (IOException e) {
            throw new SystemException("Token解析异常：" + e.getMessage());
        }
    }
}

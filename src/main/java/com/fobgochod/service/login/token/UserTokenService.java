package com.fobgochod.service.login.token;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.util.JsonUtils;
import com.fobgochod.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@CacheConfig(cacheNames = "login.token")
public class UserTokenService {

    private final static ObjectMapper objectMapper = JsonUtils.createObjectMapper();

    @Cacheable(key = "#jwtUser.uniqueKey()")
    public String getToken(JwtUser jwtUser) {
        String json;
        try {
            json = objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Json序列化异常：" + jwtUser);
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
    public JwtUser getData(String token) {
        if (!validate(token)) {
            throw new BusinessException("不是有效的Token：" + token);
        }
        try {
            return objectMapper.readValue(JwtUtil.getData(token), JwtUser.class);
        } catch (IOException e) {
            throw new BusinessException("Token解析异常：" + e.getMessage());
        }
    }
}

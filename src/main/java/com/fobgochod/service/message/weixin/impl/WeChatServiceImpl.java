package com.fobgochod.service.message.weixin.impl;

import com.fobgochod.config.AppProperties;
import com.fobgochod.service.message.SecretConfig;
import com.fobgochod.service.message.weixin.WeChatService;
import com.fobgochod.service.message.weixin.domain.ResponseWx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * WeChatServiceImpl.java
 *
 * @author Xiao
 * @date 2022/2/9 22:13
 */
@CacheConfig(cacheNames = "login.token")
@Service
public class WeChatServiceImpl implements WeChatService {

    private static final String WX_ACCESS_TOKEN = "/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";
    @Autowired
    private SecretConfig secretConfig;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Cacheable
    @Override
    public String getAccessToken() {
        String uri = appProperties.getWechatUri() + WX_ACCESS_TOKEN;
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("appId", secretConfig.getAppId());
        uriVariables.put("appSecret", secretConfig.getAppSecret());
        ResponseWx response = restTemplate.getForObject(uri, ResponseWx.class, uriVariables);
        return response.getAccess_token();
    }
}

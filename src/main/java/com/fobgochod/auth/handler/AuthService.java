package com.fobgochod.auth.handler;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.DmcConstants;
import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.repository.BucketRepository;
import com.fobgochod.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private LoginService loginService;
    @Autowired
    private BucketRepository bucketRepository;

    public void getAuth(HttpServletRequest request) {
        String userToken = request.getHeader(DmcConstants.HTTP_HEADER_USER_TOKEN_KEY);
        JwtUser jwtUser = this.analysisUserToken(userToken);
        if (jwtUser != null) {
            // 设置用户持有的bucket
            List<Bucket> buckets = bucketRepository.findByOwner(jwtUser.getUsername());
            for (Bucket bucket : buckets) {
                jwtUser.getBuckets().add(bucket.getCode());
            }
            request.setAttribute(DmcConstants.HTTP_HEADER_USER_INFO_KEY, jwtUser);
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * 解析 userToken
     */
    private JwtUser analysisUserToken(String userToken) {
        if (!StringUtils.isEmpty(userToken)) {
            try {
                return loginService.analysis(userToken);
            } catch (Exception e) {
                throw new BusinessException("userToken已经过期或者无效");
            }
        }
        return null;
    }

}

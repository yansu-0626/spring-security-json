package com.su.springsecurityjson.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.su.springsecurityjson.config.model.LoginDto;
import com.su.springsecurityjson.domain.AuthorityUser;
import com.su.springsecurityjson.domain.Token;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName JsonAuthenticationFilter
 * @Description 自定义登录过滤器
 * @Author yansu
 * @Date 2020/10/25 下午 3:02
 * @Version 1.0
 **/
@Data
public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    public JsonAuthenticationFilter(String defaultFilterProcessesUrl, String httpMethod) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, httpMethod));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 从输入流中获取到登录的信息
        try {
            LoginDto loginUser;
            loginUser = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
            token.setDetails(loginUser);
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("入参错误: " + request.getServletPath());
        }
    }
}

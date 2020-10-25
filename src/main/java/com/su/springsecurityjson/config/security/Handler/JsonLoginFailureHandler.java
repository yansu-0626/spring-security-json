package com.su.springsecurityjson.config.security.Handler;

import com.su.springsecurityjson.config.model.UrlResponse;
import com.su.springsecurityjson.util.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JsonLoginFailureHandler
 * @Description 自定义登陆失败后处理402
 * @Author yansu
 * @Date 2020/10/25 下午 3:07
 * @Version 1.0
 **/
public class JsonLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException {
        UrlResponse response = new UrlResponse();
        response.setSuccess(false);
        response.setCode("402");
        if (exception instanceof LockedException) {
            response.setMessage("账户被锁定");
        } else if (exception instanceof CredentialsExpiredException) {
            response.setMessage("密码过期");
        } else if (exception instanceof AccountExpiredException) {
            response.setMessage("账户过期");
        } else if (exception instanceof DisabledException) {
            response.setMessage("账户被禁用");
        } else if (exception instanceof BadCredentialsException) {
            response.setMessage("用户名或者密码输入错误");
        } else if (exception instanceof UsernameNotFoundException) {
            response.setMessage("用户名不存在");
        }
        response.setData(exception.getMessage());

        httpServletResponse.setStatus(402);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(JacksonUtil.objectMapper.writeValueAsString(response));
    }

}
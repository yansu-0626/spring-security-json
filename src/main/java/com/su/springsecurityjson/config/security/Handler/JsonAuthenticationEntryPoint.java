package com.su.springsecurityjson.config.security.Handler;

import com.su.springsecurityjson.config.model.UrlResponse;
import com.su.springsecurityjson.util.JacksonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JsonAuthenticationEntryPoint
 * @Description 自定义未登录后处理401
 * @Author yansu
 * @Date 2020/10/25 下午 3:08
 * @Version 1.0
 **/
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        //当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
        UrlResponse response = new UrlResponse();
        response.setSuccess(false);
        response.setCode("401");
        response.setMessage(e.getMessage());
        response.setData(null);

        httpServletResponse.setStatus(401);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(JacksonUtil.objectMapper.writeValueAsString(response));
    }

}
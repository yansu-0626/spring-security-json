package com.su.springsecurityjson.config.security.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.su.springsecurityjson.config.model.UrlResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JsonAuthenticationEntryPoint
 * @Description 自定义未登录后处理
 * @Author yansu
 * @Date 2020/10/25 下午 3:08
 * @Version 1.0
 **/
@AllArgsConstructor
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    //当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
        UrlResponse response = new UrlResponse();
        response.setSuccess(false);
        response.setCode("401");
        response.setMessage(e.getMessage());
        response.setData(null);

        httpServletResponse.setStatus(401);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }

}
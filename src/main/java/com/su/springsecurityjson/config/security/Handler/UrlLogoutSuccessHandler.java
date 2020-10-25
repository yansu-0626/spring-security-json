package com.su.springsecurityjson.config.security.Handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.su.springsecurityjson.config.model.UrlResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义注销成功处理器：返回状态码200
 */
public class UrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper;

    public UrlLogoutSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //token响应头Key
    @Value("${jwt.token-header-key}")
    private String tokenHeaderKey;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        UrlResponse response = new UrlResponse();
        response.setSuccess(true);
        response.setCode("200");
        response.setMessage("Logout Success!!");
        response.setData(null);
        //清除请求头部token信息
        httpServletResponse.addHeader(tokenHeaderKey, "logout");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }
}

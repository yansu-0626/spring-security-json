package com.su.springsecurityjson.config.security.Handler;


import com.su.springsecurityjson.config.model.UrlResponse;
import com.su.springsecurityjson.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author yansu
 * @Description 自定义注销成功处理器：返回状态码200
 * @Date 下午 10:52 2020/11/8
 * @Param
 * @return
 **/
@Component
public class UrlLogoutSuccessHandler implements LogoutSuccessHandler {

    //token响应头Key
    @Value("${jwt.token-header-key}")
    private String tokenHeaderKey;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        UrlResponse response = new UrlResponse();
        response.setSuccess(true);
        response.setCode("200");
        response.setMessage("Logout Success!!");
        response.setData(null);
        //清除请求头部token信息
        httpServletResponse.addHeader(tokenHeaderKey, "logout");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(JacksonUtil.objectMapper.writeValueAsString(response));
    }
}

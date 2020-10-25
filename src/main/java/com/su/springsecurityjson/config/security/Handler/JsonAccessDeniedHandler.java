package com.su.springsecurityjson.config.security.Handler;

import com.su.springsecurityjson.config.model.UrlResponse;
import com.su.springsecurityjson.util.JacksonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName JsonAccessDeniedHandler
 * @Description 自定义权限校验失败\权限不足后处理403
 * @Author yansu
 * @Date 2020/10/25 下午 3:08
 * @Version 1.0
 **/
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
        UrlResponse response = new UrlResponse();
        response.setSuccess(false);
        response.setCode("403");
        response.setMessage(e.getMessage());
        response.setData(null);

        httpServletResponse.setStatus(403);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(JacksonUtil.objectMapper.writeValueAsString(response));
    }

}
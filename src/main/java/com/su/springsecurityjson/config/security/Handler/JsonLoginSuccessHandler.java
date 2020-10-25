package com.su.springsecurityjson.config.security.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.su.springsecurityjson.config.model.LoginDto;
import com.su.springsecurityjson.config.model.UrlResponse;
import com.su.springsecurityjson.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName JsonLoginSuccessHandler
 * @Description 自定义登陆成功后处理
 * @Author yansu
 * @Date 2020/10/25 下午 3:06
 * @Version 1.0
 **/
public class JsonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper;
    //token响应头Key
    @Value("${jwt.token-header-key}")
    private String tokenHeaderKey;
    //token前缀
    @Value("${jwt.token-prefix}")
    private String tokenPrefix;
    //token秘钥
    @Value("${jwt.token-secret}")
    private String tokenSecret;
    //token过期时间
    @Value("${jwt.token-expiration}")
    private Long tokenExpiration;
    @Autowired
    private UserService userService;

    public JsonLoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        UrlResponse response = new UrlResponse();
        response.setSuccess(true);
        response.setCode("200");
        response.setMessage("Login Success!");
        //表单输入的用户名
        String username = ((LoginDto) authentication.getPrincipal()).getUsername();
        //用户可访问的菜单信息
        Map<String, Object> userInfo = userService.findMenuInfoByUsername(username, response);

        // 生成token并设置响应头
        Claims claims = Jwts.claims();
        claims.put("role", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        String token = Jwts.builder()
                .setClaims(claims)
                //设置用户名
                .setSubject(username)
                //设置token过期时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                //设置token签名算法及秘钥
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
        //设置token响应头
        httpServletResponse.addHeader(tokenHeaderKey, tokenPrefix + " " + token);
        userInfo.put(tokenHeaderKey, tokenPrefix + " " + token);
        response.setData(userInfo);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
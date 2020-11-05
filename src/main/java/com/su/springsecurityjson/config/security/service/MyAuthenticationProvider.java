package com.su.springsecurityjson.config.security.service;

import com.su.springsecurityjson.config.model.LoginDto;
import com.su.springsecurityjson.util.JacksonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName MyAuthenticationProvider
 * @Description 自定义登录认证
 * @Author yansu
 * @Date 2020/10/25 下午 10:27
 * @Version 1.0
 **/
@Slf4j
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Resource
    MyUserDetailsService myUserDetailsService;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication：[{}]", JacksonUtil.objectMapper.writeValueAsString(authentication));
        LoginDto userDetails = (LoginDto) authentication.getDetails();
        log.info("Authentication 详情：[{}]" + JacksonUtil.objectMapper.writeValueAsString(userDetails));
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userInfo = myUserDetailsService.loadUserByUsername(username);
        //校验用户名密码
        boolean matches = new BCryptPasswordEncoder().matches(password, userInfo.getPassword());
        if (!matches) {
            throw new BadCredentialsException("The password is incorrect!!");
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                userInfo, authentication.getCredentials(),
                userInfo.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
        //org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#createSuccessAuthentication

//        return super.createSuccessAuthentication(principalToReturn, authentication, user);
//        return new UsernamePasswordAuthenticationToken(username, userInfo.getPassword(), userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

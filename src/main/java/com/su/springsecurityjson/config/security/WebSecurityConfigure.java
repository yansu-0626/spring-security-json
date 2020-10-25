package com.su.springsecurityjson.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.su.springsecurityjson.config.security.Handler.*;
import com.su.springsecurityjson.config.security.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @ClassName WebSecurityConfigure
 * @Description 注册
 * @Author yansu
 * @Date 2020/10/25 下午 2:51
 * @Version 1.0
 **/
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImp).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 公共资源无需权限
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .addFilterAfter(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint(this.objectMapper))
                .accessDeniedHandler(new JsonAccessDeniedHandler(this.objectMapper));
        // 将session策略设置为无状态的,通过token进行登录认证
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 开启自动配置的注销功能
        http.logout()
                //自定义注销请求路径
                .logoutUrl("/logout")
                .logoutSuccessHandler(urlLogoutSuccessHandler());
    }

    @Bean
    public UrlLogoutSuccessHandler urlLogoutSuccessHandler() {
        UrlLogoutSuccessHandler jwtAuthorizationTokenFilter = new UrlLogoutSuccessHandler(this.objectMapper);
        return jwtAuthorizationTokenFilter;
    }

    @Bean
    public JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter() {
        JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter = new JwtAuthorizationTokenFilter();
        return jwtAuthorizationTokenFilter;
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter("/login", "POST");
        filter.setAuthenticationManager(authenticationManager());
        filter.setObjectMapper(this.objectMapper);
        filter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new JsonLoginFailureHandler(this.objectMapper));
        return filter;
    }

    @Bean
    public JsonLoginSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler(this.objectMapper);
    }
}

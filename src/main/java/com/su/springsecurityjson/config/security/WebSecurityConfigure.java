package com.su.springsecurityjson.config.security;

import com.su.springsecurityjson.config.security.Handler.*;
import com.su.springsecurityjson.config.security.autoManage.MyAccessDecisionManager;
import com.su.springsecurityjson.config.security.autoManage.MyFilterInvocationSecurityMetadataSource;
import com.su.springsecurityjson.config.security.service.MyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

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

    @Resource
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;
    @Resource
    private UrlLogoutSuccessHandler urlLogoutSuccessHandler;
    @Resource
    private JsonLoginSuccessHandler jsonLoginSuccessHandler;
    @Resource
    private MyAuthenticationProvider myAuthenticationProvider;
    @Resource
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
    @Resource
    private MyAccessDecisionManager myAccessDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        //自定义登录认证
        auth.authenticationProvider(myAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 对请求进行认证
        http.authorizeRequests()
                // 配置公共资源无需权限
                .antMatchers("/public/**").permitAll()
                // /login 的POST请求 放行
//                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 此方式设置需后台数据库中的角色名为"ROLE_"开头或登录认证存权限时代码写死 （ SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + roleName);）
//                .antMatchers("/security-manage/user-manage/findAll1").hasRole("user")
                .antMatchers("/security-manage/user-manage/findAll1").hasAuthority("user")
                // OPTIONS 方便前后端分离的时候前端过来的第一次验证请求
                .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
                // 其他请求权限验证
                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        // 动态获取url权限配置
                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        // 权限判断
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        return o;
                    }
                })
                // 关闭掉csrf保护
                .and().csrf().disable()
                //开启跨域访问
                .cors().disable()
                // 在**过滤器之前添加自定义登录过滤器
                .addFilterAfter(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 在**过滤器之前添加验证身份信息过滤器
                .addFilterBefore(jwtAuthorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // 自定义未登录后处理
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                // 自定义权限校验失败\权限不足后处理
                .accessDeniedHandler(new JsonAccessDeniedHandler());
        // 将session策略设置为无状态的,通过token进行登录认证
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 开启自动配置的注销功能
        http.logout()
                //自定义注销请求路径
                .logoutUrl("/logout")
                .logoutSuccessHandler(urlLogoutSuccessHandler);
    }

    /**
     * @return org.springframework.security.access.hierarchicalroles.RoleHierarchy
     * @Author yansu
     * @Description 设置角色包含关系
     * @Date 下午 9:29 2020/10/25
     * @Param []
     **/
    @Bean
    public RoleHierarchy roleHierarchy() {
        String separator = System.lineSeparator();
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER " + separator + " ROLE_USER > ROLE_TOURISTS";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    /**
     * @return com.su.springsecurityjson.config.security.JsonAuthenticationFilter
     * @Author yansu
     * @Description 创建自定义登录过滤器对象并交IOC容器管理
     * @Date 下午 9:14 2020/10/25
     * @Param []
     **/
    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter("/login", "POST");
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler);
        filter.setAuthenticationFailureHandler(new JsonLoginFailureHandler());
        return filter;
    }

}

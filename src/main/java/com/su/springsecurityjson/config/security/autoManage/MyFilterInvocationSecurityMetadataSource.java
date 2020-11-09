package com.su.springsecurityjson.config.security.autoManage;

import com.su.springsecurityjson.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author yansu
 * @Description 从数据库动态取出权限数据源(本次url可被什么role访问)
 * @Date 下午 10:49 2020/11/8
 * @Param
 * @return
 **/
@Slf4j
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private UserService userService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * @return java.util.Collection<org.springframework.security.access.ConfigAttribute>
     * @Author yansu
     * @Description 在我们初始化的权限数据中找到对应当前url的权限数据
     * @Date 下午 10:05 2020/11/8
     * @Param [o]
     **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        Set<ConfigAttribute> set = new HashSet<>();
        // 获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        log.info("requestUrl >> {}", requestUrl);
        List<String> menuUrl = userService.findAllMenuUrl();
        for (String url : menuUrl) {
            if (antPathMatcher.match(url, requestUrl)) {
                //当前请求需要的权限
                List<String> roleNames = userService.findRoleNameByMenuUrl(url);
                roleNames.forEach(roleName -> {
                    SecurityConfig securityConfig = new SecurityConfig(roleName);
                    set.add(securityConfig);
                });
            }
        }
        if (ObjectUtils.isEmpty(set)) {
            // 该资源未在数据库维护或此资源未配置可访问权限默认将分配“ROLE_WRONG”后续进行处理
            return SecurityConfig.createList("ROLE_WRONG");
        }
        return set;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}

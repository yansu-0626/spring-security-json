package com.su.springsecurityjson.config.security.autoManage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义权限判断管理器
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    /**
     * @return void
     * @Author yansu
     * @Description decide 方法是判定是否拥有权限的决策方法
     * @Date 下午 10:17 2020/11/8
     * @Param [authentication, o, collection]
     **/
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if (null == collection || collection.size() <= 0) {
            return;
        }
        // 当前请求需要的权限
        log.info("当前请求需要的权限collection:{}", collection);
        // 当前用户所具有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("用户principal:{} ；权限为authorities:{}", authentication.getPrincipal().toString(), authorities);

        for (ConfigAttribute configAttribute : collection) {
            // 当前请求需要的权限
            String needRole = configAttribute.getAttribute();
            if ("ROLE_WRONG".equals(needRole)) {
                // 该资源未在数据库维护或此资源未配置可访问权限
                throw new BadCredentialsException("Wrong path!无该资源访问配置");
            }
            // 当前用户所具有的权限
            for (GrantedAuthority grantedAuthority : authorities) {
                // 包含其中一个角色即可访问
                if (grantedAuthority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
//        throw new AccessDeniedException("权限不足，请联系管理员!");
        throw new AccessDeniedException(messages.getMessage(
                "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}

package com.su.springsecurityjson.config.security.service;

import com.su.springsecurityjson.config.model.LoginDto;
import com.su.springsecurityjson.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author yansu
 * @Date 2020/10/25 下午 3:17
 * @Version 1.0
 **/
@AllArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        LoginDto userInfo = new LoginDto();
        //任意登录用户名
        userInfo.setUsername(username);

        String password = userService.findPasswordByUsernameAfterValidTime(username);
        if (ObjectUtils.isEmpty(password)) {
            throw new UsernameNotFoundException("User: " + username + " not find!!");
        }
        //从数据库获取密码
        userInfo.setPassword(password);

        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        List<String> roles = userService.findRoleNameByUsername(username);
        for (String roleName : roles) {
            //用户拥有的角色
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authoritiesSet.add(simpleGrantedAuthority);
        }
        userInfo.setAuthorities(authoritiesSet);

        return userInfo;
    }

}
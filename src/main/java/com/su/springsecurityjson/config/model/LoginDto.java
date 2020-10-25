package com.su.springsecurityjson.config.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义UserDetails
 */
public class LoginDto implements UserDetails, Serializable {
    private String id;
    @NotBlank(message = "用户名字段值不能为空")
    private String username;
    @NotBlank(message = "密码字段值不能为空")
    private String password;
    private Set<? extends GrantedAuthority> authorities;
    private String macAddress;
    private String verifyCode;

    public LoginDto() {
    }

    public LoginDto(String username, String password, Set<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getMacAddress() {
        return macAddress;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                ", macAddress='" + macAddress + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}

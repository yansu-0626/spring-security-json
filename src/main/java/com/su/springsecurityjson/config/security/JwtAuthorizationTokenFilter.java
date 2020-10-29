package com.su.springsecurityjson.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andon
 * @date 2019/12/11
 * <p>
 * JwtToken解析并生成authentication:身份信息过滤器
 */
@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    //token请求头Key
    @Value("${jwt.token-header-key}")
    private String tokenHeaderKey;
    //token前缀
    @Value("${jwt.token-prefix}")
    private String tokenPrefix;
    //token秘钥
    @Value("${jwt.token-secret}")
    private String tokenSecret;

    /**
     * 解析token并生成authentication身份信息
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(tokenHeaderKey);
        log.info("JwtAuthorizationTokenFilter >> token:{}", token);
        //header没带token的，直接放过 ，然后交给SpringSecurity去处理，因为部分url匿名用户也可以访问
        //如果需要不支持匿名用户的请求没带token，这里放过也没问题，因为SecurityContext中没有认证信息，后面会被权限控制模块拦截
        if (null == token || !token.startsWith(tokenPrefix + " ")) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，告诉SpringSecurity有哪些角色信息。然后交给它去处理
        Claims claims;
        try {
            // 解析token
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token.replace(tokenPrefix + " ", "")).getBody();
        } catch (ExpiredJwtException e) {
            log.error("JwtToken validity!! 令牌{}过期;error={}", token, e.getMessage());
            chain.doFilter(request, response);
            return;
        } catch (SignatureException e) {
            log.error("JwtToken validity!! 令牌{}被篡改;error={}", token, e.getMessage());
            chain.doFilter(request, response);
            return;
        } catch (Exception e) {
            log.error("JwtToken validity!! 令牌{};error={}", token, e.getMessage());
            chain.doFilter(request, response);
            return;
        }
        String username = claims.getSubject();
        @SuppressWarnings("all")
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        if (null != username) {
            // 生成authentication身份信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}

 # spring-security-json 
 ## 项目简述
     该项目是本人联系spring security 整合JWT所用。

        github上标签版本说明：
           - [V1.0.0是基础版，简单整合]
           - [V1.0.1是整合spring security默认权限控制，配置类写死权限规则]
           - [V1.0.2是整合了自定义从数据库取权限数据进行认证]
## 实现思路
 - 实现 `AbstractAuthenticationProcessingFilter` 接口，接收JSON格式登录表单数据，执行登录校验
 - 实现 `AuthenticationSuccessHandler` 接口，登录成功，返回JSON格式信息
 - 实现 `AuthenticationFailureHandler` 接口，登录失败，返回JSON格式错误信息
 - 实现 `AccessDeniedHandler` 接口，登录成功，但无资源访问权限时，返回JSON格式错误信息
 - 实现 `AuthenticationEntryPoint` 接口，未登录访问资源时，返回JSON格式错误信息
    
## [数据库脚本](./src/main/resources/other/block.sql) （注意更改数据库账密）






   

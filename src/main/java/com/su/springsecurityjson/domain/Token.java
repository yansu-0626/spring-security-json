package com.su.springsecurityjson.domain;

import lombok.Data;

/**
 * @ClassName Token
 * @Description TODO
 * @Author yansu
 * @Date 2020/10/25 下午 4:02
 * @Version 1.0
 **/
@Data
public class Token {
    private Long id;
    private String token;
    private Long userId;
}
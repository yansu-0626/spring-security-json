package com.su.springsecurityjson.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName publicController
 * @Description 测试私有权限接口
 * @Author yansu
 * @Date 2020/10/23 下午 4:19
 * @Version 1.0
 **/
@RequestMapping(value = "/private")
@RestController
public class PrivateController {
    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/hello1")
    public String hello1() {
        return "hello1";
    }
}

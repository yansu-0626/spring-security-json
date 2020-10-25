package com.su.springsecurityjson.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName yansu
 * @Description JacksonUtil
 * @Author yansu
 * @Date 2020/8/2 下午 10:22
 * @Version 1.0
 **/
@Slf4j
public class JacksonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @return java.lang.String
     * @Author yansu
     * @Description 对象转json字符串
     * @Date 下午 2:24 2020/8/24
     * @Param [object]
     **/
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JacksonUtil ： object to String Error", e);
        }
        return null;
    }

    /**
     * @return java.lang.Object
     * @Author yansu
     * @Description 字符串转对象
     * @Date 下午 2:25 2020/8/24
     * @Param [string, classType]
     **/
    public static Object fromJson(String string, Class classType) {
        try {
            return objectMapper.readValue(string, classType);
        } catch (IOException e) {
            log.error("JacksonUtil ： object fromJson Error", e);
        }
        return null;
    }

    /**
     * <b>Description:</b> 将json数据转换成Map<br>
     * <b>Title:</b> jsonToMap<br>
     *
     * @param json - 转换的数据
     * @return
     * <b>Date:</b> 2018年5月24日 下午3:29:37 <br>
     * <b>Version:</b> 1.0
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("JacksonUtil ： json to map Error", e);
        }
        return map;
    }

    /**
     * <b>Description:</b> 将json数据转换成list <br>
     * <b>Title:</b> jsonToList<br>
     *
     * @param json - 转换的数据
     * @return
     * <b>Date:</b> 2018年5月24日 下午3:28:35 <br>
     * <b>Version:</b> 1.0
     */
    public static <T> List<T> jsonToList(String json, Class<T> beanType) {
        List<T> list = null;
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
            list = objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JacksonUtil ： json to list Error", e);
        }
        return list;
    }

    /**
     * <b>Description:</b> 获取json对象数据的属性<br>
     * <b>Title:</b> findValue<br>
     *
     * @param resData - 请求的数据
     * @param resPro  - 请求的属性
     * @return 返回String类型数据
     * <b>Date:</b> 2018年5月31日 上午10:00:09 <br>
     * <b>Version:</b> 1.0
     */
    public static String findValue(String resData, String resPro) {
        String result = null;
        try {
            JsonNode node = objectMapper.readTree(resData);
            JsonNode resProNode = node.get(resPro);
            result = JacksonUtil.toJson(resProNode);
        } catch (Exception e) {
            log.error("JacksonUtil ： find value Error", e);
        }
        return result;
    }

}

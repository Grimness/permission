package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * 对象转换工具类
 * @author liliang
 * @date 2017/11/16.
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 对象转字符串
     *
     * @param src
     * @param <T>
     * @return
     */
    public static <T> String objToString(T src) {
        if (src == null) {
            return null;
        }
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("对象转换字符串发生异常!error:{}", e);
            return null;
        }
    }

    /**
     * 字符串转对象
     * @param src
     * @param tTypeReference
     * @param <T>
     * @return
     */
    public static <T> T stringToObject(String src, TypeReference<T> tTypeReference) {
        if (src == null || tTypeReference == null) {
            return null;
        }
        try {
            return (T) (tTypeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, tTypeReference));
        } catch (Exception e) {
            log.warn("字符串转换对象发生异常!String:{},TypeReference<T>:{},error:{}",src,tTypeReference.getType(),e);
            return null;
        }
    }
}

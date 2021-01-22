package cn.bracerframework.core.util;

import cn.bracerframework.core.exception.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * JSON 操作工具<br/>
 * 使用了 jackson-databind、commons-pool2 依赖包
 *
 * @author Dracula
 */
public class JsonUtil {

    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /**
     * json转bean对象
     *
     * @param json  JSON字符串
     * @param clazz bean类型
     * @param <T>   返回对象类型
     * @return 转换后的对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return StrUtil.isEmpty(json) ? null : objectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new SystemException("JSON 解析对象失败", e);
        }
    }

    /**
     * JSON 转复杂对象
     *
     * @param json          JSON字符串
     * @param typeReference 用于支持复杂类型转换，例如new TypeReference<SysConfig>() { }、new TypeReference<List<SysConfig>>() { }、new TypeReference<Map<String, SysConfig>>() { }
     * @return 复杂对象
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        try {
            if (StrUtil.isEmpty(json)) {
                return null;
            }
            return objectMapper().readValue(json, typeReference);
        } catch (IOException e) {
            throw new SystemException("JSON 解析对象失败", e);
        }
    }

    /**
     * JSON 字符串转简单的泛型对象
     *
     * @param json           JSON字符串
     * @param clazz          对象类型
     * @param genericityType 泛型类型，按泛型参数顺序传入
     * @return 转化后的对象
     */
    public static <T, K> T parseGenericObj(String json, Class<T> clazz, Class<?>... genericityType) {
        try {
            JavaType javaType = objectMapper().getTypeFactory().constructParametricType(clazz, genericityType);
            return objectMapper().readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new SystemException("json 解析异常！", e);
        }
    }

    /**
     * 对象转 Json 字符串，不会忽略 null 字段
     *
     * @param obj 待转换对象
     * @param <T> 对象类型
     * @return Json 字符串
     */
    public static <T> String toStrContainNull(T obj) {
        try {
            return objectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SystemException("json 解析异常！", e);
        }
    }

    /**
     * json 转 List
     *
     * @param json  json 字符串
     * @param clazz 目标对象类型
     * @param <T>   对象类型
     * @return List 列表
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return parseGenericObj(json, List.class, clazz);
    }

}
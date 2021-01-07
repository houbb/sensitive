package com.github.houbb.sensitive.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * bean 工具类
 *
 * 如果需要属性拷贝，可以参考：
 * [bean-mapping](https://github.com/houbb/bean-mapping)
 * @author binbin.hou
 * date 2018/12/29
 * @since 0.0.2
 */
public final class JacksonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JacksonUtil(){}

    /**
     * 深度复制
     * 1. 为了避免深拷贝要求用户实现 clone 和 序列化的相关接口
     * 2. 为了避免使用 dozer 这种比较重的工具
     * 3. 自己实现暂时工作量比较大
     *
     * 暂时使用 jackson 作为实现深度拷贝的方式
     * @since 0.0.2
     * @param object 对象
     * @return 深拷贝后的对象
     *
     * TODO: 这里的对象拷贝应该使用 bean-mapping 这种对象深度复制来处理，避免序列化的性能损失。
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T deepCopy(T object) {
        try {
            JsonNode tree = OBJECT_MAPPER.valueToTree(object);
            return (T) OBJECT_MAPPER.treeToValue(tree, object.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

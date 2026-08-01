package com.github.houbb.sensitive.core.support.deepcopy;

import com.github.houbb.heaven.support.cache.impl.ClassFieldListCache;
import com.github.houbb.heaven.util.lang.reflect.ClassTypeUtil;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 基于对象身份的循环引用检测器。
 *
 * 使用一个 IdentityHashMap 同时记录当前递归路径和已完成节点，避免将共享引用误判为环。
 *
 * @since 1.9.1
 */
final class ObjectGraphCycleDetector {

    /** 当前递归路径中的节点。 */
    private static final Boolean VISITING = Boolean.FALSE;

    /** 已完成遍历且不存在环的节点。 */
    private static final Boolean VISITED = Boolean.TRUE;

    private ObjectGraphCycleDetector() {
    }

    /**
     * 判断对象图中是否存在循环引用。
     *
     * @param root 根对象
     * @return 是否存在循环引用
     */
    static boolean hasCycle(Object root) {
        if (!mayContainReferences(root)) {
            return false;
        }

        IdentityHashMap<Object, Boolean> states = new IdentityHashMap<>();
        return hasCycle(root, states);
    }

    private static boolean hasCycle(Object object, IdentityHashMap<Object, Boolean> states) {
        if (!mayContainReferences(object)) {
            return false;
        }

        Boolean state = states.get(object);
        if (state != null) {
            return VISITING.equals(state);
        }

        states.put(object, VISITING);
        if (hasCycleInChildren(object, states)) {
            return true;
        }

        states.put(object, VISITED);
        return false;
    }

    private static boolean hasCycleInChildren(Object object, IdentityHashMap<Object, Boolean> states) {
        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                if (hasCycle(Array.get(object, i), states)) {
                    return true;
                }
            }
            return false;
        }

        if (object instanceof Map) {
            for (Object entryObject : ((Map<?, ?>) object).entrySet()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObject;
                if (hasCycle(entry.getKey(), states) || hasCycle(entry.getValue(), states)) {
                    return true;
                }
            }
            return false;
        }

        if (object instanceof Iterable) {
            for (Object entry : (Iterable<?>) object) {
                if (hasCycle(entry, states)) {
                    return true;
                }
            }
            return false;
        }

        // Fastjson2 对其他 JDK 类型有专用序列化器，无需反射其内部字段。
        if (ClassTypeUtil.isJdk(clazz)) {
            return false;
        }

        try {
            for (Field field : ClassFieldListCache.getInstance().get(clazz)) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)
                        || Modifier.isTransient(modifiers)
                        || field.isSynthetic()) {
                    continue;
                }

                if (hasCycle(field.get(object), states)) {
                    return true;
                }
            }
            return false;
        } catch (IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    private static boolean isLeaf(Object object) {
        if (object == null) {
            return true;
        }

        Class<?> clazz = object.getClass();
        return ClassTypeUtil.isBase(clazz) || clazz.isEnum();
    }

    private static boolean mayContainReferences(Object object) {
        if (isLeaf(object)) {
            return false;
        }

        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            if (Array.getLength(object) == 0) {
                return false;
            }

            Class<?> componentType = clazz.getComponentType();
            return !componentType.isPrimitive()
                    && !ClassTypeUtil.isBase(componentType)
                    && !componentType.isEnum();
        }

        if (object instanceof Map) {
            return !((Map<?, ?>) object).isEmpty();
        }
        if (object instanceof Collection) {
            return !((Collection<?>) object).isEmpty();
        }
        if (object instanceof Iterable) {
            return true;
        }
        if (ClassTypeUtil.isJdk(clazz)) {
            return false;
        }

        return computeMayContainReferences(clazz);
    }

    private static boolean computeMayContainReferences(Class<?> clazz) {
        boolean result = false;
        for (Field field : ClassFieldListCache.getInstance().get(clazz)) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers)
                    || Modifier.isTransient(modifiers)
                    || field.isSynthetic()) {
                continue;
            }

            Class<?> fieldType = field.getType();
            if (!fieldType.isPrimitive()
                    && !ClassTypeUtil.isBase(fieldType)
                    && !fieldType.isEnum()) {
                result = true;
                break;
            }
        }
        return result;
    }
}

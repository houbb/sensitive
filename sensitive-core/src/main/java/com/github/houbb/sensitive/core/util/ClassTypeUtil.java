package com.github.houbb.sensitive.core.util;

import com.github.houbb.heaven.util.common.ArgUtil;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassTypeUtil {
    private static final Class[] BASE_TYPE_CLASS = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, Object.class, Class.class};

    private ClassTypeUtil() {
    }

    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isIterable(Class<?> clazz) {
        return Iterable.class.isAssignableFrom(clazz);
    }

    public static boolean isBase(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        } else {
            Class[] var1 = BASE_TYPE_CLASS;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Class baseClazz = var1[var3];
                if (baseClazz.equals(clazz)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isEnum(Class<?> clazz) {
        ArgUtil.notNull(clazz, "clazz");
        return clazz.isEnum();
    }

    public static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isAbstractOrInterface(Class<?> clazz) {
        return isAbstract(clazz) || clazz.isInterface();
    }

    public static boolean isJavaBean(Class<?> clazz) {
        return null != clazz && !clazz.isInterface() && !isAbstract(clazz) && !clazz.isEnum() && !clazz.isArray() && !clazz.isAnnotation() && !clazz.isSynthetic() && !clazz.isPrimitive() && !isIterable(clazz) && !isMap(clazz);
    }

    public static boolean isJdk(Class<?> clazz) {
        return clazz != null && clazz.getClassLoader() == null;
    }

    public static boolean isBean(Class<?> clazz) {
        if (isJavaBean(clazz)) {
            Method[] methods = clazz.getMethods();
            Method[] var2 = methods;
            int var3 = methods.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Method method = var2[var4];
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Class getListType(Field field) {
        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        return (Class) listActualTypeArguments[0];
    }

    public static boolean isWildcardGenericType(Type type) {
        Class clazz = type.getClass();
        return WildcardTypeImpl.class.equals(clazz);
    }

    public static boolean isList(Class clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    public static boolean isSet(Class clazz) {
        return Set.class.isAssignableFrom(clazz);
    }

    public static boolean isPrimitive(Class clazz) {
        return clazz.isPrimitive();
    }

    public static boolean isPrimitive(Object object) {
        return null != object && isPrimitive(object.getClass());
    }
}

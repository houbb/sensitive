package com.github.houbb.sensitive.core.api;

import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveNest;
import com.github.houbb.sensitive.api.ICondition;
import com.github.houbb.sensitive.api.ISensitive;
import com.github.houbb.sensitive.api.IStrategy;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.exception.SenstiveRuntimeException;
import com.github.houbb.sensitive.core.util.BeanUtil;
import com.github.houbb.sensitive.core.util.ClassUtil;
import com.github.houbb.sensitive.core.util.ObjectUtil;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * 脱敏服务实现类
 *
 * @author binbin.hou
 * @since 0.0.1
 * date 2018/12/29
 */
public class SensitiveService<T> implements ISensitive<T> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T desCopy(T object) {
        try {
            //1. 初始化对象
            final Class clazz = object.getClass();
            final T copyObject = (T) clazz.newInstance();

            //2. 对象的信息处理
            Mapper mapper = new DozerBeanMapper();
            mapper.map(object, copyObject);

            //2.1 上下文的构造
            final SensitiveContext context = new SensitiveContext();
            context.setCurrentObject(copyObject);

            //3. 处理
            handleClassField(context, clazz, copyObject);
            return copyObject;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SenstiveRuntimeException(e);
        }
    }

    /**
     * 处理脱敏相关信息
     * TODO: 嵌套的时候，context 中的字段列表使用起来将很不方便。
     * @param context 执行上下文
     * @param clazz class 类型
     * @param copyObject 拷贝的新对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleClassField(final SensitiveContext context,
                                       final Class clazz,
                                       final Object copyObject) {

        List<Field> fieldList = ClassUtil.getAllFieldList(clazz);
        context.addFieldList(fieldList);

        try {
            for (Field field : fieldList) {
                // 设置当前处理的字段
                context.setCurrentField(field);

                final Class fieldTypeClass = field.getType();

                // 处理 @SensitiveNest 注解
                SensitiveNest sensitiveNest = field.getAnnotation(SensitiveNest.class);
                if(ObjectUtil.isNotNull(sensitiveNest)) {
                    // 为普通 javabean 或者 Iterable/Array 对象，则做特殊处理。
                    if(ClassUtil.isNormalClass(fieldTypeClass)) {
                        final Object fieldNewObject = field.get(copyObject);
                        handleClassField(context, fieldTypeClass, fieldNewObject);
                    }

                    //TODO: 这里当为数组或者列表时 处理是存在问题的。
                    if(fieldTypeClass.isArray()) {
                        Object[] arrays = (Object[]) field.get(copyObject);
                        for(Object array : arrays) {
                            handleClassField(context, array.getClass(), array);
                        }
                    }

                    if(Iterable.class.isAssignableFrom(fieldTypeClass)) {
                        final Iterable<Object> fieldIterable = (Iterable<Object>) field.get(copyObject);
                        Iterator<Object> fieldIterator = fieldIterable.iterator();
                        while(fieldIterator.hasNext()) {
                            Object fieldIterableItem = fieldIterator.next();
                            handleClassField(context, fieldIterableItem.getClass(), fieldIterableItem);
                        }
                    }
                }

                //处理 @Sensitive
                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                if (sensitive != null) {
                    Class<? extends ICondition> conditionClass = sensitive.condition();
                    ICondition condition = conditionClass.newInstance();
                    if (condition.valid(context)) {
                        Class<? extends IStrategy> strategyClass = sensitive.strategy();
                        IStrategy strategy = strategyClass.newInstance();
                        final Object originalFieldVal = field.get(copyObject);
                        final Object result = strategy.des(originalFieldVal, context);
                        field.set(copyObject, result);
                    }
                }

                // 系统内置自定义注解的处理

                // 其他用户自定义注解的处理
            }

        } catch (IllegalAccessException | InstantiationException e) {
            throw new SenstiveRuntimeException(e);
        }
    }

}

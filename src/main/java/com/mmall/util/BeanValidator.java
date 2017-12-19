package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 检验参数工具类
 * @author liliang
 * @date 2017/11/16.
 */
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 类对象验证
     *
     * @param t
     * @param grups
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> validate(T t, Class... grups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, grups);
        if (violationSet.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap<String, String> errors = Maps.newLinkedHashMap();
            Iterator<ConstraintViolation<T>> iterator = violationSet.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> violation = iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * 验证集合
     *
     * @param collection
     * @return
     */
    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    /**
     * 检验对象是否合法
     *
     * @param firstObject
     * @param objects
     * @return
     */
    public static Map<String, String> validateObject(Object firstObject, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(firstObject, objects));
        } else {
            return validate(firstObject, new Class[0]);
        }
    }

    /**
     * 校验参数是否合法
     * @param param
     * @throws ParamException
     */
    public static void check(Object param) throws ParamException {
        Map<String, String> map = validateObject(param);
        if (MapUtils.isNotEmpty(map)){
            throw new ParamException(map.toString());
        }
    }
}

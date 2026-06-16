package com.iljin.apiServer.core.util;

import jakarta.persistence.Query;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * qlrm(JpaResultMapper) 대체 유틸.
 *
 * Hibernate 6 는 native query 결과 타입을 Hibernate 5 와 다르게 반환한다.
 * 1) CHAR(1) 컬럼 / 단일문자 리터럴('Y') → java.lang.Character (5는 String)
 * 2) NUMBER(스케일 없음) 컬럼 → java.lang.Long / java.math.BigInteger (5는 보통 BigDecimal)
 * 이로 인해 결과를 생성자로 매핑할 때 DTO 의 String/BigDecimal 파라미터와 타입이 맞지 않아
 * "No constructor taking ..." 가 발생한다.
 *
 * 이 유틸은 native query 결과(Object[])의 Character 값을 String 으로 보정한 뒤,
 * 1차로 타입이 그대로 맞는 생성자를(기존 동작 보존) 찾고, 없으면 2차로 숫자 타입 간 변환(coercion)을
 * 허용하여 생성자를 찾는다. Hibernate 의 타입 해석/Dialect 동작과 무관하게 동작이 보장된다.
 */
public final class ResultMapperUtil {

    private ResultMapperUtil() {
    }

    public static <T> List<T> list(Query query, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            result.add(map(clazz, normalize(row)));
        }
        return result;
    }

    public static <T> T uniqueResult(Query query, Class<T> clazz) {
        return map(clazz, normalize(query.getSingleResult()));
    }

    private static Object[] normalize(Object row) {
        Object[] values = (row instanceof Object[]) ? (Object[]) row : new Object[]{row};
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Character) {
                values[i] = String.valueOf(values[i]);
            }
        }
        return values;
    }

    private static <T> T map(Class<T> clazz, Object[] args) {
        // 1차: 타입이 그대로 호환되는 생성자(기존 동작 보존)
        T exact = tryMatch(clazz, args, false);
        if (exact != null) {
            return exact;
        }
        // 2차: 숫자 타입 간 변환을 허용하여 매칭(Hibernate 6 native NUMBER → Long/BigInteger 등)
        T coerced = tryMatch(clazz, args, true);
        if (coerced != null) {
            return coerced;
        }

        StringBuilder sb = new StringBuilder("No constructor taking: ");
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.getClass().getName()).append(' ');
        }
        throw new RuntimeException(sb.toString().trim());
    }

    @SuppressWarnings("unchecked")
    private static <T> T tryMatch(Class<T> clazz, Object[] args, boolean allowNumericCoercion) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] params = constructor.getParameterTypes();
            if (params.length != args.length) {
                continue;
            }
            Object[] converted = new Object[args.length];
            boolean matches = true;
            for (int i = 0; i < params.length; i++) {
                Object value = args[i];
                if (value == null) {
                    converted[i] = null;
                    continue;
                }
                Class<?> target = box(params[i]);
                if (target.isAssignableFrom(value.getClass())) {
                    converted[i] = value;
                } else if (allowNumericCoercion && value instanceof Number && Number.class.isAssignableFrom(target)) {
                    Object num = coerceNumber(target, (Number) value);
                    if (num == null) {
                        matches = false;
                        break;
                    }
                    converted[i] = num;
                } else {
                    matches = false;
                    break;
                }
            }
            if (!matches) {
                continue;
            }
            try {
                return (T) constructor.newInstance(converted);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
            }
        }
        return null;
    }

    private static Object coerceNumber(Class<?> target, Number value) {
        if (target == BigDecimal.class) {
            return (value instanceof BigInteger) ? new BigDecimal((BigInteger) value) : new BigDecimal(value.toString());
        }
        if (target == BigInteger.class) {
            return new BigDecimal(value.toString()).toBigInteger();
        }
        if (target == Integer.class) return value.intValue();
        if (target == Long.class) return value.longValue();
        if (target == Double.class) return value.doubleValue();
        if (target == Float.class) return value.floatValue();
        if (target == Short.class) return value.shortValue();
        if (target == Byte.class) return value.byteValue();
        return null;
    }

    private static Class<?> box(Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == char.class) return Character.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        return type;
    }
}

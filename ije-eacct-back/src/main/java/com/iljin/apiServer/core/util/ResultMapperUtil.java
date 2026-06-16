package com.iljin.apiServer.core.util;

import jakarta.persistence.Query;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * qlrm(JpaResultMapper) 대체 유틸.
 *
 * Hibernate 6 는 native query 의 CHAR(1) 컬럼 / 단일문자 리터럴('Y')을 java.lang.Character 로 반환한다(5는 String).
 * 이로 인해 결과를 생성자로 매핑할 때 DTO 의 String 파라미터와 타입이 맞지 않아 "No constructor taking ..." 가 발생한다.
 *
 * 이 유틸은 native query 결과(Object[])의 Character 값을 String 으로 보정한 뒤, 인자 개수/타입이 맞는 생성자를 찾아
 * 인스턴스를 생성한다. Hibernate 의 타입 해석/Dialect 동작과 무관하게 동작이 보장된다.
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

    @SuppressWarnings("unchecked")
    private static <T> T map(Class<T> clazz, Object[] args) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] params = constructor.getParameterTypes();
            if (params.length != args.length) {
                continue;
            }
            boolean matches = true;
            for (int i = 0; i < params.length; i++) {
                if (args[i] == null) {
                    continue;
                }
                if (!box(params[i]).isAssignableFrom(args[i].getClass())) {
                    matches = false;
                    break;
                }
            }
            if (!matches) {
                continue;
            }
            try {
                return (T) constructor.newInstance(args);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
            }
        }

        StringBuilder sb = new StringBuilder("No constructor taking: ");
        for (Object arg : args) {
            sb.append(arg == null ? "null" : arg.getClass().getName()).append(' ');
        }
        throw new RuntimeException(sb.toString().trim());
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

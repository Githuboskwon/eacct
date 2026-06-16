package com.iljin.apiServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/** 모든 @RequestBody DTO 중 Jackson 이 역직렬화 못 하는(canRead=false) 것을 전수 조사. */
public class JacksonCanReadTest {

    static final String BASE = "com.iljin.apiServer";

    @Test
    public void scanAllRequestBodyTypes() throws Exception {
        ObjectMapper om = Jackson2ObjectMapperBuilder.json().build();
        MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter(om);
        MediaType json = MediaType.parseMediaType("application/json;charset=UTF-8");

        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        Set<Class<?>> bodyTypes = new LinkedHashSet<>();
        for (var bd : scanner.findCandidateComponents(BASE)) {
            Class<?> ctrl = Class.forName(bd.getBeanClassName());
            for (Method m : ctrl.getDeclaredMethods()) {
                for (Parameter p : m.getParameters()) {
                    for (Annotation a : p.getAnnotations()) {
                        if (a instanceof RequestBody) {
                            Class<?> t = p.getType();
                            if (!t.getName().startsWith("java.")) { // List/Map 등 표준은 제외(요소는 별도)
                                bodyTypes.add(t);
                            }
                        }
                    }
                }
            }
        }

        Set<String> bad = new TreeSet<>();
        for (Class<?> c : bodyTypes) {
            if (!conv.canRead(c, json)) {
                String cause;
                try {
                    om.readValue("{}", c);
                    cause = "(readValue OK -- canRead false reason unclear)";
                } catch (Exception e) {
                    Throwable t = e;
                    while (t.getCause() != null) t = t.getCause();
                    cause = t.getClass().getSimpleName() + ": " + t.getMessage();
                }
                bad.add(c.getName() + "\n      CAUSE: " + cause);
            }
        }

        System.out.println("[CANREAD-SCAN] @RequestBody 타입 " + bodyTypes.size() + "개 중 실패 " + bad.size() + "개");
        for (String b : bad) {
            System.out.println("  - " + b);
        }
    }
}

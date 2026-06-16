package com.iljin.apiServer;

import jakarta.persistence.Entity;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.hql.HqlTranslator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DB 연결 없이 모든 비-native @Query(JPQL/HQL)를 Hibernate 6 의미분석기로 검증한다.
 * 깨지는 쿼리를 한 번에 모두 출력한다. (Hibernate 6 엄격화 대응용 마이그레이션 검증 도구)
 */
public class HqlQueryValidationTest {

    static final String BASE = "com.iljin.apiServer";

    @Test
    public void validateAllJpqlQueries() throws Exception {
        // 1) @Entity 스캔
        var entityScanner = new ClassPathScanningCandidateComponentProvider(false);
        entityScanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        List<Class<?>> entities = new ArrayList<>();
        for (var bd : entityScanner.findCandidateComponents(BASE)) {
            entities.add(Class.forName(bd.getBeanClassName()));
        }
        System.out.println("[HQL-VALIDATE] entities=" + entities.size());

        // 2) DB 없이 오프라인 SessionFactory 빌드
        var reg = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.OracleDialect")
                .applySetting("hibernate.boot.allow_jdbc_metadata_access", "false")
                .applySetting("hibernate.temp.use_jdbc_metadata_defaults", "false")
                .build();
        var ms = new MetadataSources(reg);
        entities.forEach(ms::addAnnotatedClass);
        var sf = ms.buildMetadata().buildSessionFactory();
        HqlTranslator translator = ((SessionFactoryImplementor) sf).getQueryEngine().getHqlTranslator();

        // 3) Repository 인터페이스 스캔 (인터페이스 포함)
        var repoScanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface();
            }
        };
        repoScanner.addIncludeFilter((TypeFilter) (mr, f) -> true);

        List<String> errors = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        int checked = 0;
        for (var bd : repoScanner.findCandidateComponents(BASE)) {
            Class<?> c = Class.forName(bd.getBeanClassName());
            if (!Repository.class.isAssignableFrom(c)) continue;
            for (Method m : c.getMethods()) {
                Query q = m.getAnnotation(Query.class);
                if (q == null || q.nativeQuery()) continue;
                String hql = q.value();
                if (hql == null || hql.isBlank()) continue;
                if (!seen.add(c.getName() + "#" + m.getName() + "(" + m.getParameterCount() + ")")) continue;
                checked++;
                try {
                    translator.translate(hql, null);
                } catch (Exception e) {
                    errors.add(c.getName() + "#" + m.getName()
                            + "\n    HQL: " + hql
                            + "\n    ERR: " + rootMsg(e));
                }
            }
        }
        sf.close();

        System.out.println("[HQL-VALIDATE] checked=" + checked + ", invalid=" + errors.size());
        if (!errors.isEmpty()) {
            System.out.println("\n===== INVALID JPQL QUERIES (" + errors.size() + ") =====");
            for (String s : errors) System.out.println("- " + s + "\n");
        } else {
            System.out.println("[HQL-VALIDATE] ALL JPQL @Query OK");
        }
    }

    static String rootMsg(Throwable t) {
        while (t.getCause() != null) t = t.getCause();
        return t.getClass().getSimpleName() + ": " + t.getMessage();
    }
}

package com.iljin.apiServer.support;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 테스트에서 사용하는 Gson 인스턴스 팩토리.
 *
 * Java 17+ 의 모듈 캡슐화로 인해 기본 {@code new Gson()} 은 {@code java.time.LocalDateTime}
 * 등의 private 필드에 리플렉션 접근을 할 수 없어
 * {@code JsonIOException: Failed making field 'java.time.LocalDateTime#date' accessible}
 * 가 발생한다. 직렬화용 어댑터를 등록해 이를 회피한다.
 */
public final class TestGson {

    private TestGson() {
    }

    // 서버측 DTO 들이 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 로 역직렬화하므로
    // 동일 포맷으로 직렬화해야 @RequestBody 바인딩(Jackson)이 성공한다.
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Gson create() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                                new JsonPrimitive(src.format(DATE_TIME)))
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, ctx) ->
                                new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .create();
    }
}

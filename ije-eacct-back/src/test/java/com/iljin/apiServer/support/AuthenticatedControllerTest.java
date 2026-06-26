package com.iljin.apiServer.support;

import com.iljin.apiServer.core.security.user.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * 컨트롤러 통합 테스트 공용 부모.
 *
 * MockMvc 세팅이 Spring Security 필터 체인을 적용하지 않기 때문에, 컨트롤러/서비스가
 * {@code Util.getLoginId()} 등으로 직접 읽는 {@link SecurityContextHolder} 의 인증 정보가
 * 비어 있어 NullPointerException 이 발생한다. (Java21/Spring Boot 3 전환 이후 표면화)
 *
 * 각 테스트 실행 전에 TEST DB 에 존재하는 'admin' 사용자를 주체(principal)로 하는 인증을
 * 스레드 로컬에 주입해 로그인 컨텍스트를 흉내 낸다.
 */
public abstract class AuthenticatedControllerTest {

    protected static final String TEST_LOGIN_ID = "admin";

    @BeforeEach
    public void setUpAuthentication() {
        Member member = new Member();
        member.setUsername(TEST_LOGIN_ID);
        member.setAuthorities(List.of(new SimpleGrantedAuthority("ROLE_USER")));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}

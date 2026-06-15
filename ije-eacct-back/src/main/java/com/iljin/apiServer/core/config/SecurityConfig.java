package com.iljin.apiServer.core.config;

import com.iljin.apiServer.core.security.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import static com.rathontech.sso.sp.config.Env.IDPM_DOMAIN_CONTEXT;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 31536000)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("com.iljin.apiServer.core.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final Environment environment;

    @Autowired
    private MemberService memberService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new CookieHttpSessionIdResolver();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/login", "/logout").permitAll()
                //.antMatchers("/login/sso", "/logout/sso").permitAll()
                .antMatchers("/login/sso").permitAll()
                .antMatchers("/login/ssoRathon").permitAll()
                .antMatchers("/dispatch/*").permitAll()
                .antMatchers("/dispatch/**").permitAll()
                .antMatchers(IDPM_DOMAIN_CONTEXT).permitAll()
                .antMatchers("/api/v1/user*/**").permitAll()
                .antMatchers("/api/v1/download2*/**", "/home", "/login/**").permitAll()
                .antMatchers("/swagger-**/**", "/webjars/**", "/v2/**", "/").permitAll()
                .antMatchers("/docs/api-guide.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .and()
                .headers().frameOptions().disable().addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW-FROM http://" + environment.getProperty("server.domain-name") + "/"));
        ;
    }

}

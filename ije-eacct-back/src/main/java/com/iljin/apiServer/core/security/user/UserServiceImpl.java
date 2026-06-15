package com.iljin.apiServer.core.security.user;

import com.iljin.apiServer.core.mail.MailDto;
import com.iljin.apiServer.core.mail.MailService;
import com.iljin.apiServer.core.security.role.Role;
import com.iljin.apiServer.core.security.AuthToken;
import com.iljin.apiServer.core.security.loginHistory.LoginHistory;
import com.iljin.apiServer.core.security.loginHistory.LoginHistoryRepository;
import com.iljin.apiServer.core.security.role.RoleRepository;
import com.iljin.apiServer.core.security.role.UserRole;
import com.iljin.apiServer.core.security.role.UserRoleRepository;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final Environment environment;
    private final MailService mailService;
    private final LoggersEndpoint loggersEndpoint;

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDto(
                        user.id,
                        user.loginId,
                        "******",
                        user.userName,
                        user.enableFlag,
                        "",
                        user.getRoles().stream().map(
                                roles -> roles.getRole().toString()
                        ).collect(toList())
                )).collect(toList());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    @Override
    public List<User> getSearchUser(String loginId) {
        return userRepository.findAllByLoginIdContains(loginId);
    }

    @Override
    public ResponseEntity<Object> addUser(UserDto userDto) {
        try {
            userDto.setLoginPw(passwordEncoder.encode(userDto.getLoginPw()));
            User newUser = new User();
            newUser.loginId = userDto.loginId;
            newUser.loginPw = userDto.loginPw;
            newUser.userName = userDto.userName;
            newUser.enableFlag = userDto.enableFlag;
            newUser.compCd = userDto.compCd;
            newUser.deptCd = userDto.deptCd;
            userRepository.save(newUser);

            userRepository.findByLoginId(userDto.loginId).ifPresent(c -> {
                List<UserRole> newRoles = new ArrayList<>();
                UserRole newRole = new UserRole(
                        c.getId(),
                        c.getCompCd(),
                        userDto.role,
                        c);
                userRoleRepository.save(newRole);

                newRoles.add(newRole);
                c.updateUserRoles(newRoles);
                userRepository.save(c);
            });

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            throw new UserCreateException();
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(String loginId) {
        userRepository.deleteByLoginId(loginId);
        return new ResponseEntity<>("사용자가 삭제되었습니다", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> updateUser(UserDto userDto) {
        String loginId = userDto.getLoginId();
        Optional<User> userData = userRepository.findByLoginId(loginId);

        if (userData.isPresent()) {
            User modifiedUser = userData.get();

            // 비밀번호가 가려진 거 아닌 경우에만 변경.
            if (!userDto.loginPw.equals("******")) {
                modifiedUser.loginPw = passwordEncoder.encode(userDto.getLoginPw());
            }

            modifiedUser.userName = userDto.userName;
            modifiedUser.enableFlag = userDto.enableFlag;

            if(!userDto.getRole().isEmpty()) {
                List<UserRole> modifiedRoles = new ArrayList<>();

                // TODO 복수 권한 부여는 차후 필요시 구현 예정
                Optional<UserRole> modifiedRole = userRoleRepository.findRoleByUser_LoginId(userDto.loginId);
                if (!modifiedRole.isPresent()) {
                    modifiedRole = Optional.of(new UserRole());
                }
                modifiedRole.ifPresent(c -> {
                    c.updateRole(userDto.role);
                    userRoleRepository.save(c);

                    modifiedRoles.add(c);
                    modifiedUser.updateUserRoles(modifiedRoles);
                });
            }

            userRepository.save(modifiedUser);

            return new ResponseEntity<>(userRepository.save(modifiedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<AuthToken> login(UserDto userDto, HttpSession session, HttpServletRequest request) {
        try {
            String loginId = userDto.loginId;
            String loginPw = userDto.loginPw;
            String compCd = userDto.compCd;
            String loginToken = userDto.token;

            Optional<User> user = userRepository.findByCompCdAndLoginId(compCd, loginId);

            Optional<AuthToken> result =
                    user.map(obj -> {
                        // 1. username, password를 조합하여 UsernamePasswordAuthenticationToken 생성
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId, loginPw);
                        if(loginToken != null && !loginToken.equals("")) {
                            // Create Granted Authority Rules
                            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                            token = new UsernamePasswordAuthenticationToken(loginId, null, grantedAuthorities);
                        } else {
                            // Form login
                            // 2. Form 로그인 검증을 위해 UsernamePasswordAuthenticationToken 을 authenticationManager 의 인스턴스로 전달
                            authenticationManager.authenticate(token);// 3. 인증에 성공하면 Authentication 인스턴스 리턴
                        }

                        /*
                         * added on 26.08.2019
                         * Login history
                         * */
                        String clientIp = this.getClientIp(request);

                        /* login success */
                        LoginHistory loginHistory = new LoginHistory(
                                loginId,
                                clientIp,
                                userDto.getConnectMthd(),
                                "",
                                request.getRequestURI(),
                                LocalDateTime.now()
                        );
                        loginHistoryRepository.save(loginHistory);

                        if (!StringUtils.isEmpty(userDto.getAttribute1())) {
                            /* from mobile */
                            userRepository.findByLoginId(loginId).ifPresent(c -> {
                                c.updateAttribute1(userDto.getAttribute1());

                                // save Mobile token (String value)
                                userRepository.save(c);
                            });
                        }
                        return getAuthToken(session, compCd, loginId, obj, token);
                    });

            return result.map(authToken -> new ResponseEntity<>(authToken, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(new AuthToken(
                            null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null), HttpStatus.UNAUTHORIZED));
        } catch (AuthenticationException e) {
            LoginHistory loginHistory = new LoginHistory(
                    userDto.loginId,
                    this.getClientIp(request),
                    userDto.getConnectMthd(),
                    e.getMessage(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );
            loginHistoryRepository.save(loginHistory);

            return new ResponseEntity<>(new AuthToken(
                    null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<AuthToken> ssoLogin(UserDto userDto, HttpSession session, HttpServletRequest request) {
        try{
            String loginId = userDto.loginId + "%";
            String compCd = userDto.compCd;

            //Optional<User> user = userRepository.findByLoginId(loginId);
            //List<UserRole> roles = userRoleRepository.findRolesByUser_LoginId(loginId);
            Optional<User> user = userRepository.findByCompCdAndLoginIdLike(compCd, loginId);
            List<UserRole> roles = userRoleRepository.findByCompCdAndUser_LoginIdLike(compCd, loginId);
    //        List<String> r = roles.stream().map(Role::getRole).map(RoleType::toString).collect(Collectors.toList());
            List<String> r = roles.stream().map(x -> x.getRole()).collect(Collectors.toList());

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(r.get(0));

            String setLoginId = user.get().loginId;

            Optional<AuthToken> result =
                    user.map(obj -> {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(setLoginId, null, grantedAuthorities);
                        return getAuthToken(session, compCd, setLoginId, obj, token/*, token*/);
                    });

            /*
            URI uri = null;
            try {
                uri = new URI("http://localhost:8080");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);
            headers.set("Set-cookie", "sessionid=" + session.getId());
    */
            return result.map(authToken -> new ResponseEntity<>(authToken, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(new AuthToken(
                            null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null
                            , null), HttpStatus.UNAUTHORIZED));

        } catch(AuthenticationException e)  {
            System.out.println("!!!!!error");

            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setConnectId(userDto.loginId);
            loginHistory.setConnectIp(this.getClientIp(request));
            loginHistory.setConnectMthd(userDto.connectMthd);
            loginHistory.setConnectError(e.getMessage());
            loginHistory.setConnectUrl(request.getRequestURI());
            loginHistory.setCreationDate(LocalDateTime.now());

            loginHistoryRepository.save(loginHistory);

            return new ResponseEntity<>(new AuthToken(
                    null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null
                    , null), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @NotNull
    private AuthToken getAuthToken(final HttpSession session, final String compCd, final String loginId, final User obj, final UsernamePasswordAuthenticationToken token/*, final Authentication authentication*/) {
        // 4. Authentication 인스턴스를 SecurityContextHolder의 SecurityContext에 설정
        SecurityContextHolder.getContext().setAuthentication(token);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        Optional<User> user = userRepository.findByCompCdAndLoginId(compCd, loginId);

        List<Role> authList = roleRepository.findByCompCdAndRoleCdIn(compCd, user.get().roles.stream().map(UserRole::getRole).collect(Collectors.toList()));

        return new AuthToken(obj.userName,
                loginId,
                user.get().getCompCd(),
                user.get().getEmployee().getCompNm(),
                user.get().getEmployee().getDeptCd(),
                user.get().getEmployee().getDeptNm(),
                user.get().getEmployee().getCctrCd(),
                user.get().getEmployee().getCctrNm(),
                user.get().getEmployee().getJobDutCd(),
                user.get().getEmployee().getJobDutNm(),
                user.get().getEmployee().getJobGradeCd(),
                user.get().getEmployee().getJobGradeNm(),
                session.getId(),
                authList);
    }

    @Override
    public ResponseEntity<Object> resetPassword(String empNo) {
        User user = userRepository.findByLoginId(empNo)
                .orElseThrow(() -> new UserException("해당 사번(" + empNo + ")의 사용자를 찾을 수 없습니다."));

        String tempPassword = Util.getRandomPassword();
        String mailTo = empNo + "@iljin.co.kr";
        String mailFrom = "system@iljin.co.kr";
        String mailSubject = "[전기 전자전표] 비밀번호 재설정 안내";
        String mailText = String.format(
                "비밀번호 초기화 요청 <br><br>초기화된 비밀번호 : <strong>%s</strong><br><br>" +
                "<a href='http://%s' target='_blank'>전기 전자전표</a> <br><br>" +
                "초기화된 비밀번호는 로그인 후 반드시 변경해 주세요.<br><br>" +
                "우측 상단 열쇠 아이콘 클릭 → 비밀번호 변경 메뉴를 이용해주세요.<br><br>감사합니다.",
                tempPassword, environment.getProperty("server.domain-name"));

        MailDto mailDto = new MailDto(mailFrom, mailTo, mailSubject, mailText);

        try {
            mailService.sendSimpleMessage(mailDto);
            user.loginPw = passwordEncoder.encode(tempPassword);
            userRepository.save(user);
            return ResponseEntity.ok("초기화된 비밀번호를 메일로 전송하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메일 발송 또는 비밀번호 저장에 실패하였습니다.");
        }
    }

    @Override
    public ResponseEntity<Object> changePassword(UserDto dto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(new Date());

        if (dto.loginNewPw.length() != StringUtils.trimAllWhitespace(dto.loginNewPw).length()) {
            return new ResponseEntity<>("비밀번호에 공백을 사용할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        AtomicBoolean bUpdate = new AtomicBoolean(false);
        userRepository.findByLoginId(dto.getLoginId()).ifPresent(c -> {
            if (passwordEncoder.matches(dto.getLoginPw(), c.getLoginPw())) {
                userRepository.save(c);
                userRepository.updatePw(dto.getLoginId(), passwordEncoder.encode(dto.getLoginNewPw()));
                bUpdate.set(true);
            }
        });

        if (bUpdate.get()) {
            return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("비밀번호 변경에 실패했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> initAllPasswords() {
        List<User> users = userRepository.findAllByIsAdminFalse();
        List<String> failedUsers = new ArrayList<>();

        for (User user : users) {
            try {
                if (user.getLoginId().matches("\\d+")) {
                    String empNo = user.getEmployee().getEmpNo();
                    String tempPassword = Util.getRandomPassword();
                    String mailTo = empNo + "@iljin.co.kr";
                    String mailFrom = "system@iljin.co.kr";
                    String mailSubject = "[전기 전자전표] 비밀번호 재설정 안내";
                    String mailText = String.format(
                            "비밀번호 초기화 요청 <br><br>초기화된 비밀번호 : <strong>%s</strong><br><br>" +
                            "<a href='http://%s' target='_blank'>전기 전자전표</a> <br><br>" +
                            "초기화된 비밀번호는 로그인 후 반드시 변경해 주세요.<br><br>" +
                            "우측 상단 열쇠 아이콘 클릭 → 비밀번호 변경 메뉴를 이용해주세요.<br><br>감사합니다.",
                            tempPassword, environment.getProperty("server.domain-name"));

                    MailDto mailDto = new MailDto(mailFrom, mailTo, mailSubject, mailText);
                    mailService.sendSimpleMessage(mailDto);
                    user.loginPw = passwordEncoder.encode(tempPassword);
                    userRepository.save(user);
                } else {
                    System.out.println("잘못된 사번으로 메일발송 취소 : {}" + user.getLoginId());
                    failedUsers.add(user.getLoginId());
                }
            } catch (Exception e) {
                failedUsers.add(user.getLoginId());
                System.out.println(e.getMessage());
            }
        }

        if (failedUsers.isEmpty()) {
            return ResponseEntity.ok("전체 사용자 비밀번호 초기화 완료");
        }
        return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                .body("일부 실패: " + String.join(", ", failedUsers));
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

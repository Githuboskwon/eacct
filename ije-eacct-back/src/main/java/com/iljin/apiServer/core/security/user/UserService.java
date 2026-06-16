package com.iljin.apiServer.core.security.user;

import com.iljin.apiServer.core.security.AuthToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByLoginId(String loginId);
    List<User> getSearchUser(String loginId);
    @Modifying
    @Transactional
    ResponseEntity<Object> addUser(UserDto dto);
    @Modifying
    @Transactional
    ResponseEntity<String> deleteUser(String loginId);
    @Modifying
    @Transactional
    ResponseEntity<User> updateUser(UserDto userDto);
    ResponseEntity<AuthToken> login(UserDto userDto, HttpSession session, HttpServletRequest request);
    void logout(HttpSession session);

    @Modifying
    @Transactional
    ResponseEntity<Object> resetPassword(String empNo);

    @Modifying
    @Transactional
    ResponseEntity<Object> changePassword(UserDto dto);

    @Modifying
    @Transactional
    ResponseEntity<Object> initAllPasswords();
}

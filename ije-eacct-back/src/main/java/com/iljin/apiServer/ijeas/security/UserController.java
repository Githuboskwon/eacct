package com.iljin.apiServer.ijeas.security;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.security.user.UserDto;
import com.iljin.apiServer.core.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(Long.parseLong(id));
    }

    @GetMapping("/login-id/{loginId}")
    public Optional<User> getUserByLoginId(@PathVariable String loginId) {
        return userService.getUserByLoginId(loginId);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody UserDto dto) {
        return userService.addUser(dto);
    }

    @DeleteMapping("/{loginId}")
    public ResponseEntity<String> deleteUser(@PathVariable("loginId") String loginId) {
        return userService.deleteUser(loginId);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @PostMapping("/reset-pw")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> param) {
        String empNo = param.get("empNo");
        if (empNo == null || empNo.isEmpty()) {
            return ResponseEntity.ok("사번이 올바르지 않습니다.");
        }
        return userService.resetPassword(empNo);
    }

    @PostMapping("/change-pw")
    public ResponseEntity<Object> changePassword(@RequestBody UserDto dto) {
        return userService.changePassword(dto);
    }

    @GetMapping("/init-all-passwords")
    public ResponseEntity<Object> initAllPasswords() {
        return userService.initAllPasswords();
    }
}

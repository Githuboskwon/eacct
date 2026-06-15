package com.iljin.apiServer.core.util;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class Util {
    final UserRepository userRepository;

    @Autowired
    public Util(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoginId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication().getPrincipal();

        String loginId = "";
        //if (principal instanceof org.springframework.security.core.userdetails.User) {
        if (principal instanceof com.iljin.apiServer.core.security.user.Member) {
            //loginId = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            loginId = ((com.iljin.apiServer.core.security.user.Member) principal).getUsername();
        } else {
            loginId = String.valueOf(principal);
        }
        return loginId;
    }

    public String getLoginUserId() {
        Optional<User> user = userRepository.findByLoginId(getLoginId());
        return user.map(User::getLoginId).orElse("");
    }

    public String getLoginCompCd() {
        String loginId = Optional.ofNullable(getLoginId()).orElse("");
        Optional<User> user = userRepository.findByLoginId(loginId);
        return user.map(User::getCompCd).orElse("");
    }

    public User getLoginUser() {
        Optional<User> user = userRepository.findByLoginId(getLoginId());

        return user.orElse(null);
    }

    public String getStartDate(String yearMonth) {
        yearMonth = yearMonth.replaceAll("-", "");

        if(yearMonth.length() != 6) {
            throw new RuntimeException("년월의 형태가 잘못되었습니다. (YYYY-MM 또는 YYYYMM 가 아닙니다.) \n value : " + yearMonth);
        }
        String startDate = yearMonth + "01";

        return startDate;
    }

    public String getEndDate(String yearMonth) {
        yearMonth = yearMonth.replaceAll("-", "");

        if(yearMonth.length() != 6) {
            throw new RuntimeException("년월의 형태가 잘못되었습니다. (YYYY-MM 또는 YYYYMM 가 아닙니다.) \n value : " + yearMonth);
        }

        int lastDayOfMonth = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM")).lengthOfMonth();

        return yearMonth + lastDayOfMonth;
    }

    public static String getRandomPassword() {
        int minLength = 8;
        int maxLength = 10;
        if (minLength < 8 || maxLength > 10 || minLength > maxLength) {
            throw new IllegalArgumentException("비밀번호 길이는 8자리 이상, 20자리 이하로 설정해야 합니다.");
        }
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+<>?";
        String all = upper + lower + digits + special;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        int remainingLength = random.nextInt(maxLength - minLength + 1) + minLength - 4;
        for (int i = 0; i < remainingLength; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }
        char[] characters = password.toString().toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }
}

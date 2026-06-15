package com.iljin.apiServer.core.security;

import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@Getter
@Data
public class AuthToken {
    private String userName;
    private String loginId;
    private String loginCompCd;
    private String loginCompNm;
    private String loginDeptCd;
    private String loginDeptNm;
    private String loginCctrCd;
    private String loginCctrNm;
    private String loginJobDutCd;
    private String loginJobDutNm;
    private String loginJobGradeCd;
    private String loginJobGradeNm;
    private String token;
    private Collection authorities;

    @Builder
    public AuthToken(String userName,
                     String loginId,
                     String loginCompCd,
                     String loginCompNm,
                     String loginDeptCd,
                     String loginDeptNm,
                     String loginCctrCd,
                     String loginCctrNm,
                     String loginJobDutCd,
                     String loginJobDutNm,
                     String loginJobGradeCd,
                     String loginJobGradeNm,
                     String token,
                     Collection authorities) {
        this.userName = userName;
        this.loginId = loginId;
        this.loginCompCd = loginCompCd;
        this.loginCompNm = loginCompNm;
        this.loginDeptCd = loginDeptCd;
        this.loginDeptNm = loginDeptNm;
        this.loginCctrCd = loginCctrCd;
        this.loginCctrNm = loginCctrNm;
        this.loginJobDutCd = loginJobDutCd;
        this.loginJobDutNm = loginJobDutNm;
        this.loginJobGradeCd = loginJobGradeCd;
        this.loginJobGradeNm = loginJobGradeNm;
        this.token = token;
        this.authorities = authorities;
    }
}

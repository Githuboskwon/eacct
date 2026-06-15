package com.iljin.apiServer.ijeas.system.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class SettingsDto {
    BigDecimal cnt;

    // 등록여부
    String regYn;
    // 메뉴 번호
    String menuNo;
    // 메뉴명
    String menuName;
    // 메뉴 아이콘 코드
    String menuIconCd;

    // 회사코드
    String compCd;
    // 사용자 아이디
    String loginId;
    //메뉴 순서
    String menuOrdr;

    public SettingsDto(String regYn, String menuNo, String menuName, String menuIconCd, String menuOrdr) {
        this.regYn = regYn;
        this.menuNo = menuNo;
        this.menuName = menuName;
        this.menuIconCd = menuIconCd;
        this.menuOrdr = menuOrdr;
    }

    public SettingsDto(BigDecimal cnt) {
        this.cnt = cnt;
    }
}

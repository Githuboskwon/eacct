package com.iljin.apiServer.ijeas.system.authority;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuAuthDto {
    //권한체크
    String roleCk;
    //권한코드
    String roleCd;
    //메뉴순번
    String menuNo;
    //메뉴명
    String menuNm;
    //메뉴설명
    String menuDc;
    //회사코드
    String compCd;
    //메뉴레벨
    Integer menuLv;
    //메뉴정렬순서
    Integer menuOrder;
    //사우이 메뉴순번
    String upperMenuNo;

    public MenuAuthDto () {

    }

    /* 권한별 메뉴 List */
    public MenuAuthDto(String roleCk, String roleCd, String menuNo, String menuNm, String menuDc, String compCd, Integer menuLv, Integer menuOrder, String upperMenuNo) {
        this.roleCk = roleCk;
        this.roleCd = roleCd;
        this.menuNo = menuNo;
        this.menuNm = menuNm;
        this.menuDc = menuDc;
        this.compCd = compCd;
        this.menuLv = menuLv;
        this.menuOrder = menuOrder;
        this.upperMenuNo = upperMenuNo;
    }

    /* IJD eAcct 권한별 매뉴 조회 */
    public MenuAuthDto(Character roleCk, String roleCd, String menuNo, String menuNm, String menuDc, String compCd, BigDecimal menuLv, BigDecimal menuOrder, String upperMenuNo) {
        this.roleCk = String.valueOf(roleCk);
        this.roleCd = roleCd;
        this.menuNo = menuNo;
        this.menuNm = menuNm;
        this.menuDc = menuDc;
        this.compCd = compCd;
        this.menuLv = menuLv.intValue();
        this.menuOrder = menuOrder.intValue();
        this.upperMenuNo = upperMenuNo;
    }

}

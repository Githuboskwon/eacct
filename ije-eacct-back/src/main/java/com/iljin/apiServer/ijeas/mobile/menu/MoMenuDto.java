package com.iljin.apiServer.ijeas.mobile.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoMenuDto {
    //사용여부
    String regYn;
    //메뉴코드
    String menuNo;
    //메뉴명(상위-메뉴)
    String menuNm;
    //메뉴명
    String oMenuNm;
    //이미지(아이콘)
    String rImagePath;

    public MoMenuDto(String regYn, String menuNo, String menuNm, String oMenuNm, String rImagePath) {
        this.regYn = regYn;
        this.menuNo = menuNo;
        this.menuNm = menuNm;
        this.oMenuNm = oMenuNm;
        this.rImagePath = rImagePath;
    }
}

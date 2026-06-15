package com.iljin.apiServer.ijeas.mobile.menu;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MoMenuService {

    /** 모바일 - 메인화면 - 사용자 커스텀 메뉴*/
    List<MoMenu> getCustMenu(String lgoinId);

    /** 모바일 - 메인화면 - 사용자 커스텀 메뉴 저장*/
    @Modifying
    @Transactional
    ResponseEntity<String> saveCustMenu(List<MoMenuDto> moMenuDto);

    /** 모바일 - 메뉴리스트*/
    List<MoMenuDto> getMenuList(String loginId);
}

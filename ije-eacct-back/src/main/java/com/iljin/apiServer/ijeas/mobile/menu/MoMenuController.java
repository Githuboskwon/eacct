package com.iljin.apiServer.ijeas.mobile.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/mobile")
public class MoMenuController {
    private  final MoMenuService moMenuService;

    /**
     * MA-MP-001
     * 모바일 - 메인화면 - 사용자 커스텀 메뉴
     */
    @GetMapping("/custMenu")
    public ResponseEntity<List<MoMenu>> getCustMenu(@RequestParam String loginId) {
        List<MoMenu> list = moMenuService.getCustMenu(loginId);
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    /**
     * MA-MP-001_01
     * 모바일 - 메인화면 - 사용자 커스텀 메뉴 저장
     */
    @PutMapping("/saveCustMenu")
    public ResponseEntity<String> saveCustMenu(@RequestBody List<MoMenuDto> moMenuDto) {
        return moMenuService.saveCustMenu(moMenuDto);
    }

    /**
     * MA-MP-002
     * 모바일 - 메뉴리스트
     */
    @GetMapping("/menuList")
    public ResponseEntity<List<MoMenuDto>> getMenuList(@RequestParam String loginId) {
        List<MoMenuDto> list = moMenuService.getMenuList(loginId);
        return new ResponseEntity<>(list, HttpStatus.OK);

    }
}

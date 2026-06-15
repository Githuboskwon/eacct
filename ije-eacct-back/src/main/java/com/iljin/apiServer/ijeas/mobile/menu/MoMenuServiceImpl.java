package com.iljin.apiServer.ijeas.mobile.menu;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.security.user.UserRepository;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoMenuServiceImpl implements MoMenuService{
    private final MoMenuRepository moMenuRepository;
    private final UserRepository userRepository;
    private final Util util;

    @Override
    public List<MoMenu> getCustMenu(String loginId){
        return moMenuRepository.findByLoginId(loginId);
    }

    @Override
    public ResponseEntity<String> saveCustMenu(List<MoMenuDto> list){

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();
        //String compCd = "101600";
        //String loginId = "admin";
        
        moMenuRepository.deleteByLoginIdAndCompCd(loginId, compCd);
        if(list.size() > 0) {
            for(MoMenuDto moMenuDto : list){
                MoMenu moMenu = new MoMenu();
                moMenu.setCompCd(compCd);
                moMenu.setLoginId(loginId);
                moMenu.setMenuNo(moMenuDto.getMenuNo());
                moMenu.setMenuNm(moMenuDto.getOMenuNm());
                moMenu.setMenuIcon(moMenuDto.getRImagePath());

                moMenuRepository.save(moMenu);
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public List<MoMenuDto> getMenuList(String loginId){
        //return moMenuRepository.getMenuList(loginId);
        List<MoMenuDto> menuList = new ArrayList<>();
        AtomicReference<String> userRole = new AtomicReference<>("");

        //1. 사용자 권한 확인
        userRepository.findByLoginId(loginId)
                .ifPresent(c -> {
                    userRole.set(c.getRoles().get(0).getRole());
                });

        //2. Logged in User's quick menus & set Dto
        menuList = moMenuRepository.getMenuList(loginId,String.valueOf(userRole))
                .stream()
                .map(s -> new MoMenuDto(
                        String.valueOf(Optional.ofNullable(s[0]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[1]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[2]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[3]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[4]).orElse(""))
                ))
                .collect(Collectors.toList());

        return menuList;
    }
}

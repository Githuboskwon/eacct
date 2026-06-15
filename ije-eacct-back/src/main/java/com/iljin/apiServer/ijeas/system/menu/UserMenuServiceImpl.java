package com.iljin.apiServer.ijeas.system.menu;

import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMenuServiceImpl implements UserMenuService {
    private final UserMenuRepository userMenuRepository;
    private final Util util;

    @Override
    public List<UserMenuDto> getCustomQuickMenuList(String loginId) {
        List<UserMenuDto> quickMenus = new ArrayList<>();
        String compCd = util.getLoginCompCd();

        //1. Logged in User's quick menus & set Dto
        quickMenus = userMenuRepository.getCustomQuickMenuList(compCd, loginId)
                .stream()
                .map(s -> new UserMenuDto(
                        String.valueOf(Optional.ofNullable(s[0]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[1]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[2]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[3]).orElse(""))
                ))
                .collect(Collectors.toList());

        return quickMenus;
    }
}

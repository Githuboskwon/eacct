package com.iljin.apiServer.ijeas.system.menu;

import java.util.List;

public interface UserMenuService {
    List<UserMenuDto> getCustomQuickMenuList(String loginId);
}

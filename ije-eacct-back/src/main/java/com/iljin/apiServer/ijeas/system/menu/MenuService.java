package com.iljin.apiServer.ijeas.system.menu;

import java.util.List;

public interface MenuService {
    List<MenuDto> getMenuListByLoginId(String loginId);
}

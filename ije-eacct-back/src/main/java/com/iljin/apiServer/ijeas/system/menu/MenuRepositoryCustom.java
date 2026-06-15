package com.iljin.apiServer.ijeas.system.menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<MenuDto> getMenuList(MenuDto menuDto);
}

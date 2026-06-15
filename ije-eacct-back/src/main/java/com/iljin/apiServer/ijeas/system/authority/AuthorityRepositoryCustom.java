package com.iljin.apiServer.ijeas.system.authority;

import java.util.List;

public interface AuthorityRepositoryCustom {

    List<MenuAuthDto> getMenuByAuthority(String roleCd, String compCd);

}

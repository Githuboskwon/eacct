package com.iljin.apiServer.ijeas.bond;

import java.util.List;

public interface BondRepositoryCustom {

    List<BondExpendDto> getBondExpendList(BondExpendDto bondExpendDto);

    List<BondExpendDto> getBondRefNoList(BondMstDto bondMstDto);

}

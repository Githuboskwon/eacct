package com.iljin.apiServer.ijeas.bond;


import java.util.List;

public interface BondMstQdslRepository {

    List<BondMstDto> getBondMstList(BondMstDto bondMstDto);

    List<BondMstDto> getBondMstRefNoList(BondMstDto bondMstDto);

}

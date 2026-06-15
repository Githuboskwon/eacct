package com.iljin.apiServer.ijeas.bond;

import com.iljin.apiServer.ijeas.es.erpViews.ErpPaProjectsAllDto;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface BondService {

    @Transactional(readOnly = true)
    List<BondMstDto> getBondMstList(BondMstDto bondMstDto);

    @Modifying
    @Transactional
    String saveBondMstList(List<BondMstDto> bondMstDtos);

    @Modifying
    @Transactional
    String deleteBondMstList(List<BondMstDto> bondMstDto);

    @Transactional(readOnly = true)
    List<BondExpendDto> getBondExpendList(BondExpendDto bondExpendDto);

    @Transactional
    List<ErpPaProjectsAllDto> getProjectList(ErpPaProjectsAllDto erpPaProjectsAllDto);

}

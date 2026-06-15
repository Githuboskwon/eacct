package com.iljin.apiServer.ijeas.system.cctr;


import java.util.List;

public interface CostCenterQdslRepository {

    List<CostCenterDto> getByCctrCdOrCctrNmContaining(String compCd, String value);

    List<CostCenterDto> getSlipCctr(CostCenterDto costCenterDto);

    List<CostCenterDto> getCctrsDeptRole(CostCenterDto costCenterDto);

    List<CostCenterDto> getCctrsBetween(String compCd, String startDept, String endDept);
}

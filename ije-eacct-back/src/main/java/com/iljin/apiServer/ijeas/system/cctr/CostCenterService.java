package com.iljin.apiServer.ijeas.system.cctr;

import java.util.List;

public interface CostCenterService {

	List<CostCenterDto> getByCctrCdOrCctrNm(String value);

	List<CostCenterDto> getSlipCctr(CostCenterDto costCenterDto);

	List<CostCenterDto> getCctrsDeptRole(CostCenterDto costCenterDto);
}

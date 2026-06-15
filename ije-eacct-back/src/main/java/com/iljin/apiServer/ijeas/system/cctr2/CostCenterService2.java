package com.iljin.apiServer.ijeas.system.cctr2;

import com.iljin.apiServer.ijeas.system.cctr.CostCenterDto;
import com.iljin.apiServer.ijeas.system.delegate.DelegateDto;

import java.util.List;

public interface CostCenterService2 {

	List<CostCenterDto2> getByCctrCdOrCctrNm(String value);

	List<CostCenterDto2> getSlipCctr(CostCenterDto2 costCenterDto);

    List<CostCenterDto2> getSlipCctrAdmin(CostCenterDto2 costCenterDto);

    List<CostCenterDto2> getCctrsDelegate(DelegateDto delegateDto);
}

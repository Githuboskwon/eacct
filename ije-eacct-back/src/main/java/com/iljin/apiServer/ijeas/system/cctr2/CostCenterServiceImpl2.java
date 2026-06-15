package com.iljin.apiServer.ijeas.system.cctr2;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.delegate.DelegateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CostCenterServiceImpl2 implements CostCenterService2 {

    private final CostCenterQdslRepository2 costCenterQdslRepository2;
    private final Util util;

	@Override
	public List<CostCenterDto2> getByCctrCdOrCctrNm(String value) {
		return costCenterQdslRepository2.getByCctrCdOrCctrNmContaining(util.getLoginCompCd(), value);
	}

	@Override
	public List<CostCenterDto2> getSlipCctr(CostCenterDto2 costCenterDto){
		return costCenterQdslRepository2.getSlipCctr(costCenterDto);
	}

	@Override
	public List<CostCenterDto2> getSlipCctrAdmin(CostCenterDto2 costCenterDto){
		return costCenterQdslRepository2.getSlipCctrAdmin(costCenterDto);
	}

	@Override
	public List<CostCenterDto2> getCctrsDelegate(DelegateDto delegateDto){
		return costCenterQdslRepository2.getCctrsDelegate(delegateDto);
	}

}

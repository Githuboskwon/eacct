package com.iljin.apiServer.ijeas.system.cctr;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.code.CodeQdslRepository;
import com.iljin.apiServer.ijeas.system.dept.DeptAuthDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CostCenterServiceImpl implements CostCenterService {

    private final CostCenterQdslRepository costCenterQdslRepository;
	private final CodeQdslRepository codeQdslRepository;
    private final Util util;

	@Override
	public List<CostCenterDto> getByCctrCdOrCctrNm(String value) {
		return costCenterQdslRepository.getByCctrCdOrCctrNmContaining(util.getLoginCompCd(), value);
	}

	@Override
	public List<CostCenterDto> getSlipCctr(CostCenterDto costCenterDto){
		return costCenterQdslRepository.getSlipCctr(costCenterDto);
	}

	@Override
	public List<CostCenterDto> getCctrsDeptRole(CostCenterDto costCenterDto){
		List<CostCenterDto> list = costCenterQdslRepository.getCctrsDeptRole(costCenterDto);

		List<Map> deptRole = codeQdslRepository.findByGroupCd(costCenterDto.compCd, "DEPT_ROLE_RANGE_CD");

		for(Map c : deptRole) {
			String detailCd = c.get("detailCd").toString();

			if(detailCd.equals(costCenterDto.attribute1)){
				List<CostCenterDto> cctrList = costCenterQdslRepository.getCctrsBetween(costCenterDto.compCd, c.get("remark1").toString(), c.get("remark2").toString());

				for(CostCenterDto cctr : cctrList){
					list.add(cctr);
				}

				break;
			}
		}

		return list;
	}

}

package com.iljin.apiServer.ijeas.ims.pjtLaborCost;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PjtLaborCostServiceImpl implements PjtLaborCostService {

    private final Util util;
    private final PjtLaborCostRepository pjtLaborCostRepository;
    private final PjtLaborCostQdslRepository pjtLaborCostQdslRepository;

    @Override
    public List<PjtLaborCostDto> getLaborCostList(PjtLaborCostDto pjtLaborCostDto) {
        return pjtLaborCostQdslRepository.getLaborCostList(pjtLaborCostDto);
    }

    @Override
    public String saveLaborCostList(List<PjtLaborCostDto> pjtLaborCostDtos) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        for (PjtLaborCostDto pjtLaborCostDto : pjtLaborCostDtos) {

            PjtLaborCostKey pjtLaborCostKey = new PjtLaborCostKey();

            pjtLaborCostKey.setOrgId(compCd);
            pjtLaborCostKey.setProjectMngNo(pjtLaborCostDto.getProjectMngNo());

            Optional<PjtLaborCost> result = pjtLaborCostRepository.findById(pjtLaborCostKey);

            if (result.isPresent()) {
                //update
                result.ifPresent(s -> {
                    s.update(pjtLaborCostDto);
                });
            } else {
                //insert
                PjtLaborCost pjtLaborCost = PjtLaborCost.builder()
                        .orgId(compCd)
                        .projectMngNo(pjtLaborCostDto.getProjectMngNo())
                        .positionCd(pjtLaborCostDto.getPositionCd())
                        .amt(pjtLaborCostDto.getAmt())
                        .amtType(pjtLaborCostDto.getAmtType())
                        .acctCode(pjtLaborCostDto.getAcctCode())
                        .acctName(pjtLaborCostDto.getAcctName())
                        .addDate(pjtLaborCostDto.getAddDate())
                        .remark(pjtLaborCostDto.getRemark())
                        .changeDate(pjtLaborCostDto.getChangeDate())
                        .changeTime(pjtLaborCostDto.getChangeTime())
                        .changeUserId(pjtLaborCostDto.getChangeUserId())
                        .build();
                pjtLaborCostRepository.save(pjtLaborCost);
            }
        }
        return "저장되었습니다.";
    }

    @Override
    public String deleteLaborCostList(PjtLaborCostDto pjtLaborCostDto) {
        {
            String positionCd = pjtLaborCostDto.getPositionCd();
            String orgId = pjtLaborCostDto.getOrgId();
            String projectMngNo = pjtLaborCostDto.getProjectMngNo();

            PjtLaborCostKey pjtLaborCostKey = new PjtLaborCostKey();
            pjtLaborCostKey.setOrgId(orgId);
            pjtLaborCostKey.setPositionCd(positionCd);
            pjtLaborCostKey.setProjectMngNo(projectMngNo);

            pjtLaborCostRepository.deleteById(pjtLaborCostKey);

            return ("삭제되었습니다.");
        }

    }
}

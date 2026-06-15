package com.iljin.apiServer.ijeas.ims.pjtPersonnelPlan;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PjtPersonnelPlanServiceImpl implements PjtPersonnelPlanService {

    private final PjtProcessRatePlanQdslRepository pjtProcessRatePlanQdslRepository;

    private final PjtPersonnelPlanRepositoryCustom pjtPersonnelPlanRepositoryCustom;

    private Util util;

    @Override
    public PersonnelResult getPersonnelList(PjtPersonnelPlanDto pjtPersonnelPlanDto) {

        PersonnelResult personnelResult = new PersonnelResult();

        PjtProcessRatePlanDto intopjtPersonnelPlanDto = new PjtProcessRatePlanDto();
        intopjtPersonnelPlanDto.setProjectManageNo(pjtPersonnelPlanDto.getProjectManageNo());

        PjtProcessRatePlanDto personnelInfo = pjtProcessRatePlanQdslRepository.getProcessInfo(intopjtPersonnelPlanDto);


        if(personnelInfo == null){
            return null;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(personnelInfo.getStartDate(), formatter);
        LocalDate end = LocalDate.parse(personnelInfo.getEndDate(), formatter);

        List<String> param = new ArrayList<>();

        formatter = DateTimeFormatter.ofPattern("yyyyMM");

        while (!start.isAfter(end)) {
            param.add(start.format(formatter));
            start = start.plusMonths(1);
        }

        pjtPersonnelPlanDto.setParam(param);
        pjtPersonnelPlanDto.setDegree(personnelInfo.getDegree());

        // 상단 데이터
        personnelResult.setInfo(personnelInfo);
        // 리스트
        personnelResult.setList(pjtPersonnelPlanRepositoryCustom.getPersonnelList(pjtPersonnelPlanDto));


        return personnelResult;
    }

    @Override
    public String savePersonnelList(PersonnelResult personnelResult) throws Exception {
        return pjtPersonnelPlanRepositoryCustom.savePersonnelList(personnelResult);
    }

}

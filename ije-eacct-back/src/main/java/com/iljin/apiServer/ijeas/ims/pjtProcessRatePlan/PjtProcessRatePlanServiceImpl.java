package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PjtProcessRatePlanServiceImpl implements PjtProcessRatePlanService {

    private final PjtProcessRatePlanQdslRepository pjtProcessRatePlanQdslRepository;

    private final PjtProcessRatePlanRepositoryCustom pjtProcessRatePlanRepositoryCustom;

    private Util util;

    @Override
    public ProcessResult getProcessList(PjtProcessRatePlanDto pjtProcessRatePlanDto) {

        ProcessResult processResult = new ProcessResult();

        PjtProcessRatePlanDto processInfo = pjtProcessRatePlanQdslRepository.getProcessInfo(pjtProcessRatePlanDto);

        if(processInfo == null){
            return null;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(processInfo.getStartDate(), formatter);
        LocalDate end = LocalDate.parse(processInfo.getEndDate(), formatter);

        List<String> param = new ArrayList<>();

        formatter = DateTimeFormatter.ofPattern("yyyyMM");

        while (!start.isAfter(end)) {
            param.add(start.format(formatter));
            start = start.plusMonths(1);
        }

        pjtProcessRatePlanDto.setParam(param);
        pjtProcessRatePlanDto.setDegree(processInfo.getDegree());

        // 상단 데이터
        processResult.setInfo(processInfo);
        // 리스트
        processResult.setList(pjtProcessRatePlanRepositoryCustom.getProcessList(pjtProcessRatePlanDto));

        return processResult;
    }

    @Override
    public String saveProcessList(ProcessResult pjtProcessRatePlanDtoList) {
        return pjtProcessRatePlanRepositoryCustom.saveProcessList(pjtProcessRatePlanDtoList);
    }

}

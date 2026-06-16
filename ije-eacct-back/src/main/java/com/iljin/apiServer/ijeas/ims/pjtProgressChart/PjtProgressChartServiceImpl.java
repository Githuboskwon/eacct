package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Service
public class PjtProgressChartServiceImpl implements PjtProgressChartService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PjtProgressChartRepositoryCustom pjtProgressChartRepositoryCustom;

    private final PjtRegistInfoRepository pjtRegistInfoRepository;

    private Util util;

    @Override
    public List<Map<String,Object>> getProgressChartList(PjtProgressChartDto pjtProgressChartDto) {
          return pjtProgressChartRepositoryCustom.getProgressChartList(pjtProgressChartDto);
    }

    @Override
    public String saveProgressChartList(List<PjtProgressChartDto> pjtProgressChartDto) {

        String compCd = util.getLoginCompCd();
        String userId = util.getLoginUserId();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDate = currentDate.format(dateFormatter);

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String nowTime = currentTime.format(timeFormatter);

        for(PjtProgressChartDto pjtRegistInfoDto : pjtProgressChartDto){

            String projectManageNo = pjtRegistInfoDto.getProjectManageNo();
            String issue = pjtRegistInfoDto.getIssue();
            String issueMeasure = pjtRegistInfoDto.getIssueMeasure();
            String weeklyIssue = pjtRegistInfoDto.getWeeklyIssue();

            List<Map> sf = pjtRegistInfoRepository.getSaveProgressChartList(compCd,projectManageNo);

            String sfIssue = String.valueOf(sf.get(0).get(0));
            String sfIssueMeasure = String.valueOf(sf.get(0).get(1));
            String sfWeeklyIssue = String.valueOf(sf.get(0).get(2));

            boolean issueNull = issue == null && sfIssue == null;
            boolean issueMeasureNull = issueMeasure == null && sfIssueMeasure == null;
            boolean weeklyIssueNull = weeklyIssue == null && sfWeeklyIssue == null;

            if ((issueNull || !Objects.equals(issue, sfIssue)) ||
                    (issueMeasureNull || !Objects.equals(issueMeasure, sfIssueMeasure)) ||
                    (weeklyIssueNull || !Objects.equals(weeklyIssue, sfWeeklyIssue))) {
                pjtRegistInfoRepository.updateProgressChartList(compCd, projectManageNo, issue, issueMeasure, weeklyIssue, userId, nowDate, nowTime);
            }
        }
        return "저장되었습니다.";
    }

}

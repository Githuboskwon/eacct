package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import com.iljin.apiServer.ijeas.ims.pjtProgressChart.PjtProgressChartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PjtRegistInfoRepository extends JpaRepository<PjtRegistInfo, PjtRegistInfoKey> {


    @Query(value =
            " SELECT ISSUE AS issue "
            +"   , ISSUE_MEASURE AS issueMeasure  "
            +"   , WEEKLY_ISSUE AS weeklyIssue  "
            +"   FROM SPJTM  "
            +"  WHERE ORG_ID = :compCd  "
            +"    AND PROJECT_MANAGE_NO = :projectManageNo  "
            , nativeQuery = true)
    List<Map> getSaveProgressChartList(@Param("compCd")String compCd , @Param("projectManageNo")String projectManageNo);

    @Modifying
    @Query(value =
            " UPDATE SPJTM SET "
            +"   ISSUE = :issue   "
            +"   ,ISSUE_MEASURE = :issueMeasure   "
            +"   ,WEEKLY_ISSUE = :weeklyIssue  "
            +"   ,ISSUE_CHANGE_DATE =  :nowDate   "
            +"   ,ISSUE_CHANGE_TIME = :nowTime    "
            +"   ,ISSUE_CHANGE_USER_ID =  :userId   "
            +"  WHERE ORG_ID = :compCd  "
            +"    AND PROJECT_MANAGE_NO = :projectManageNo  "
            , nativeQuery = true)
    void updateProgressChartList(@Param("compCd")String compCd , @Param("projectManageNo")String projectManageNo,
                                 @Param("issue")String issue ,@Param("issueMeasure")String issueMeasure ,
                                 @Param("weeklyIssue")String weeklyIssue, @Param("userId")String userId ,
                                 @Param("nowDate")String nowDate, @Param("nowTime")String nowTime);

}
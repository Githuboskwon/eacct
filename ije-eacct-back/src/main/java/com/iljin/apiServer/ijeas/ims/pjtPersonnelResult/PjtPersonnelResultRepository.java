package com.iljin.apiServer.ijeas.ims.pjtPersonnelResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PjtPersonnelResultRepository extends JpaRepository<PjtPersonnelResult, PjtPersonnelResultKey> {

    @Modifying
    @Query(" UPDATE PjtPersonnelResult A SET " +
            "   A.orgId = :compCd " +
            "   , A.projectMngNo = :projectManageNo" +
            "   , A.periodYear = :periodYear" +
            "   , A.periodMonth = :periodMonth" +
            "   , A.periodMonth = :periodMonth" +
            "   , A.yyyyMm = :yyyyMm" +
            "   , A.mText = :mText" +
            "   , A.positionCd = :positionCd" +
            "   , A.remark = :remark" +
            "   , A.d1 = :d1" +
            "   , A.changeDate = :saveDate" +
            "   , A.changeTime = :saveTime" +
            "   , A.changeUserId = :saveUserId" +
            " WHERE A.orgId = :compCd" +
            " AND A.projectMngNo = :projectManageNo" +
            " AND A.degree = :degree" +
            " AND A.positionCd = :positionCd" +
            " AND A.yyyyMm = :yyyymm")
    void updatePjtPersonnelResult(@Param("compCd") String compCd,
                                  @Param("projectManageNo") String projectManageNo,
                                  @Param("periodYear") String periodYear,
                                  @Param("periodMonth") String periodMonth,
                                  @Param("yyyyMm") String yyyyMm,
                                  @Param("mText") String mText,
                                  @Param("positionCd") String positionCd,
                                  @Param("remark") String remark,
                                  @Param("d1") String d1,
                                  @Param("saveDate") String saveDate,
                                  @Param("saveTime") String saveTime,
                                  @Param("saveUserId") String saveUserId,
                                  @Param("degree") String degree
    );
}
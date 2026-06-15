package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PjtProcessRatePlanRepository extends JpaRepository<PjtProcessRatePlan, PjtProcessRatePlanKey> {

    @Modifying
    @Query(" UPDATE PjtProcessRatePlan A SET " +
            "   A.objectiveMon = :objectiveMon" +
            "   , A.objectiveAll = :objectiveAll" +
            "   , A.changeDate = :saveDate" +
            "   , A.changeTime = :saveTime" +
            "   , A.changeUserId = :saveUserId" +
            " WHERE A.orgId = :compCd" +
            " AND A.projectMngNo = :projectManageNo" +
            " AND A.degree = :degree" +
            " AND A.itemCode = :itemCode" +
            " AND A.yyyyMm = :yyyymm")
    void updatePjtProcessRatePlan(@Param("objectiveMon") String objectiveMon,
                                  @Param("objectiveAll") String objectiveAll,
                                  @Param("saveDate") String saveDate,
                                  @Param("saveTime") String saveTime,
                                  @Param("saveUserId") String saveUserId,
                                  @Param("compCd") String compCd,
                                  @Param("projectManageNo") String projectManageNo,
                                  @Param("degree") String degree,
                                  @Param("itemCode") String itemCode,
                                  @Param("yyyymm") String yyyymm
    );
}
package com.iljin.apiServer.ijeas.approval;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApprovalHeaderRepository extends JpaRepository<ApprovalHeader, ApprovalHeaderKey> {

//    Optional<ApprovalHeader> findByApprNo(String apprNo);

    @Query(value = "SELECT FN_CMN_GET_MNG_NO(:compCd, 'AP', TO_CHAR(SYSDATE, 'YYYYMMDD')) FROM DUAL", nativeQuery = true)
    String getApprNo(@Param("compCd") String compCd);

    Boolean existsByCompCdAndApprGroupIdAndSlipNo(String compCd, BigDecimal apprGroupId, String slipNo);

    Optional<ApprovalHeader> findByCompCdAndApprGroupId(String compCd, BigDecimal apprGroupId);

    Optional<ApprovalHeader> findByCompCdAndSlipTypeAndApprGroupId(String compCd, String slipType, BigDecimal apprGroupId);

    @Transactional
    @Modifying
    @Query("update ApprovalHeader a set a.nextAppUserId = ?1 where a.apprGroupId = ?2")
    int changeApprHd(String nextAppUserId, BigDecimal apprGroupId);

    @Transactional
    @Modifying
    void deleteByCompCdAndApprGroupId(String compCd, BigDecimal apprGroupId);

    @Modifying
    @Query("UPDATE ApprovalHeader a set a.slipStatus = :status WHERE a.compCd = :compCd AND a.apprGroupId = :apprGroupId")
    void updateStatusByCompCdAndApprGroupId(@Param("status") String status, @Param("compCd") String compCd, @Param("apprGroupId") BigDecimal apprGroupId);

    @Modifying
    @Query("UPDATE ApprovalHeader a set a.slipStatus = :status WHERE a.compCd = :compCd AND a.apprGroupId IN (SELECT b.slipHeaderId FROM SlipHeader b WHERE b.compCd = :compCd AND b.approvalGroupId = :apprGroupId)")
    void updateStatusByCompCdAndApprGroupIdInSlipHeader(@Param("status")String status, @Param("compCd") String compCd, @Param("apprGroupId") BigDecimal apprGroupId);

}

package com.iljin.apiServer.ijeas.es.erpViews;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ErpSlipHeaderRepository extends JpaRepository<ErpSlipHeader, ErpSlipHeaderKey> {

    Optional<ErpSlipHeader> findByOrgIdAndSlipNumberAndSlipHeaderId(Integer orgId, String slipNumber, BigDecimal slipHeaderId);

    List<ErpSlipHeader> findByOrgIdAndApprovalGroupId(Integer orgId, BigDecimal approvalGroupId);

    Optional<ErpSlipHeader> findByOrgIdAndSlipHeaderId(Integer orgId, BigDecimal slipHeaderId);

    List<ErpSlipHeader> findAllByOrgIdAndSlipHeaderId(Integer orgId, BigDecimal slipHeaderId);

    List<ErpSlipHeader> findByOrgIdAndSourceSlipHeaderId(Integer orgId, BigDecimal sourceSlipHeaderId);

    Optional<ErpSlipHeader> findByOrgIdAndSlipNumberAndTrxTypeCodeAndSlipHeaderId(Integer orgId, String slipNumber, String trxTypeCd, BigDecimal slipHeaderId);

    @Query("SELECT DISTINCT e.slipNumber " +
            "FROM ErpSlipHeader e " +
            "WHERE e.orgId = :orgId " +
            "AND e.slipNumber != :slipNumber " +
            "AND e.taxSmartbillNo = :taxSmartbillNo " +
            "AND e.slipStatus NOT IN ('AR', 'CR', 'SC', 'FR')")
    String existsBySmartBillNoCheck(
            @Param("orgId") Integer orgId,
            @Param("slipNumber") String slipNumber,
            @Param("taxSmartbillNo") String taxSmartbillNo);

    @Transactional
    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipDataFixFlag = :flag WHERE e.orgId = :orgId AND e.sourceSlipHeaderId = :sourceSlipHeaderId")
    void updateSlipDataFixFlagByOrgIdAndSourceSlipHeaderId(@Param("flag") String flag, @Param("orgId") Integer orgId, @Param("sourceSlipHeaderId") BigDecimal sourceSlipHeaderId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipForcedIfFlag = :flag, e.approvalCompleteFlag = :flag WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateSlipForcedIfFlagAndApprovalCompleteFlagByOrgIdAndApprovalGroupId(@Param("flag") String flag, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipIfFlag = :flag WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateSlipIfFlagByOrgIdAndApprovalGroupId(@Param("flag") String flag, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipStatus = :status WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateSlipStatusByOrgIdAndApprovalGroupId(@Param("status") String status, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    List<ErpSlipHeader> findAllByOrgIdAndApprovalGroupIdAndSlipDisplayFlagEquals(Integer orgId, BigDecimal approvarGroupId, String slipDisplayFlag);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.validationFlag = 'Y', e.slipDeleteFlag = 'Y',  e.slipDataFixFlag = 'Y', e.createValidationFlag = 'Y', e.approvalCompleteFlag = 'Y', e.slipIfFlag = 'ING' WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateSlipFlags(@Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipStatus = :status, e.validationFlag = :validationFlag, e.slipDataFixFlag = :slipDataFixFlag WHERE e.orgId = :orgId AND e.slipHeaderId = :slipHeaderId")
    void updateSlipStatusAndValidationFlagAndSlipDataFixFlag(@Param("status") String status, @Param("validationFlag") String validationFlag, @Param("slipDataFixFlag") String slipDataFixFlag, @Param("orgId") Integer orgId, @Param("slipHeaderId") BigDecimal slipHeaderId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipStatus = :status, e.validationFlag = :validationFlag, e.slipDataFixFlag = :slipDataFixFlag WHERE e.orgId = :orgId AND e.sourceSlipHeaderId = :sourceSlipHeaderId")
    void updateSlipStatusAndValidationFlagAndSlipDataFixFlagBySourceSlipId(@Param("status") String status, @Param("validationFlag") String validationFlag, @Param("slipDataFixFlag") String slipDataFixFlag, @Param("orgId") Integer orgId, @Param("sourceSlipHeaderId") BigDecimal sourceSlipHeaderId);
    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.slipStatus = :status, e.slipDataFixFlag = :slipDataFixFlag WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateSlipStatusAndSlipDataFixFlagByOrgIdAndApprovalGroupId(@Param("status") String status, @Param("slipDataFixFlag") String slipDataFixFlag, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.taxSmartbillNo = :taxSmartbillNo, e.globalAttribute14 = :globalAttribute14 WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateTaxSmartbillNoAndGlobalAttribute14ByOrgIdAndApprovalGroupId(@Param("taxSmartbillNo") String taxSmartbillNo, @Param("globalAttribute14") String globalAttribute14,  @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.approvalCompleteFlag = :flag WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateApprovalCompleteFlagByOrgIdAndApprovalGroupId(@Param("flag") String flag, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE ErpSlipHeader e SET e.validationFlag = :validationFlag, e.slipStatus = :slipStatus, e.lastUpdateDate = current_timestamp WHERE e.orgId = :orgId AND e.approvalGroupId = :approvalGroupId")
    void updateValidationFlagAndSlipStatusByOrgIdAndApprovalGroupId(@Param("validationFlag") String validationFlag, @Param("slipStatus") String slipStatus, @Param("orgId") Integer orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);
}

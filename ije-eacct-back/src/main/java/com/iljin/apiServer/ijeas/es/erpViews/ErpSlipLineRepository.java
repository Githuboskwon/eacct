package com.iljin.apiServer.ijeas.es.erpViews;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ErpSlipLineRepository extends JpaRepository<ErpSlipLine, BigDecimal> {

    void deleteByOrgIdAndSlipHeaderId(String orgId, BigDecimal slipHeaderId);

    void deleteAllByOrgIdAndSlipHeaderId(String orgId, BigDecimal slipHeaderId);

    List<ErpSlipLine> findAllByOrgIdAndSlipHeaderId(String orgId, BigDecimal slipHeaderId);

    @Modifying
    @Query("UPDATE ErpSlipLine e SET e.validationFlag = :validationFlag, e.lastUpdateDate = current_timestamp WHERE e.orgId = :orgId AND e.slipHeaderId IN (SELECT e.slipHeaderId FROM ErpSlipHeader s WHERE s.approvalGroupId = :approvalGroupId)")
    void updateValidationFlagByOrgIdAndSlipHeaderId(@Param("validationFlag") String validationFlag, @Param("orgId") String orgId, @Param("approvalGroupId") BigDecimal approvalGroupId);

}

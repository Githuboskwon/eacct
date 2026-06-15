package com.iljin.apiServer.ijeas.slip.prepayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrepaymentApplyRepository extends JpaRepository<PrepaymentApply, PrepaymentApplyKey> {

    @Query(value = "SELECT APPS.CBO_SP_PREPAYMENT_APPLY_S.NEXTVAL FROM DUAL", nativeQuery = true)
    BigDecimal getPrepaymentApplyId();

    List<PrepaymentApply> findAllByOrgIdAndSlipHeaderIdAndLedgerId(Integer orgId, BigDecimal slipHeaderId, BigDecimal ledgerId);

    Optional<PrepaymentApply> findByPrepaymentApplyIdAndSlipHeaderIdAndOrgIdAndLedgerIdAndPrepayInvoiceIdAndPrepayLineNumber
            (BigDecimal prepaymentApplyId, BigDecimal slipHeaderId, Integer orgId, BigDecimal ledgerId, BigDecimal prepayInvoiceId, Integer prepayLineNumber);

    @Modifying
    @Query("UPDATE PrepaymentApply p SET p.prepayCancelledFlag = :prepayCancelledFlag, p.prepayCancelledAmount = p.amountToApply, p.prepayCancelledDate = current_timestamp, "
        + "p.prepayCancelledPersonId = :personId, p.lastUpdatedPersonId = :personId, p.lastUpdateDate = current_timestamp WHERE p.slipHeaderId IN (SELECT s.slipHeaderId FROM "
        + "SlipHeader s WHERE s.compCd = :compCd AND s.approvalGroupId = :approvalGroupId) AND p.orgId = :orgId AND p.ledgerId = :ledgerId")
    void updateByOrgIdAndSlipHeaderIdAndLedgerId(@Param("prepayCancelledFlag") String prepayCancelledFlag, @Param("personId") BigDecimal personId, @Param("compCd") String compCd,
        @Param("approvalGroupId") BigDecimal approvalGroupId, @Param("orgId")Integer orgId, @Param("ledgerId") BigDecimal ledgerId);
}

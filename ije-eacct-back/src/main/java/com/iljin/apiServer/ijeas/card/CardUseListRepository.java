package com.iljin.apiServer.ijeas.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardUseListRepository extends JpaRepository<CardUseList, CardUseListKey> {

    List<CardUseList> findAllByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    Optional<CardUseList> findByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    Optional<CardUseList> findByCompCdAndUsedNo(String compCd, String usedNo);

    @Query("SELECT c FROM CardUseList c WHERE c.compCd = :compCd AND c.slipHeaderId = :slipHeaderId AND c.status != :status")
    List<CardUseList> findAllByCompCdAndSlipHeaderIdAndStatusNotEqual(@Param("compCd")String compCd, @Param("slipHeaderId")BigDecimal slipHeaderId, @Param("status")String status);

    @Transactional
    @Modifying
    @Query("UPDATE CardUseList c SET c.status = :status WHERE c.compCd = :compCd AND c.slipHeaderId = :slipHeaderId AND c.status <> :flag")
    void updateStatusByCompCdAndSlipHeaderIdAndFlag(@Param("status") String status, @Param("compCd") String compCd, @Param("slipHeaderId") BigDecimal slipHeaderId,  @Param("flag") String flag);

    @Modifying
    @Query("UPDATE CardUseList c SET c.status = :status WHERE c.slipHeaderId IN (SELECT s.slipHeaderId FROM SlipHeader s WHERE s.compCd = :compCd AND s.approvalGroupId = :approvalGroupId)")
    void updateStatusByCompCdAndApprovalGroupId(@Param("status") String status, @Param("compCd") String compCd, @Param("approvalGroupId") BigDecimal approvalGroupId);

    @Modifying
    @Query("UPDATE CardUseList c SET c.slipNo = '', c.status = '01', c.slipHeaderId = '', c.slipLineId = '' WHERE c.slipHeaderId IN (SELECT s.slipHeaderId FROM SlipHeader s WHERE s.compCd = :compCd AND s.approvalGroupId = :approvalGroupId) AND c.compCd = :compCd")
    void resetByCompCdAndApprovalGroupId(@Param("compCd") String compCd, @Param("approvalGroupId") BigDecimal approvalGroupId);
}

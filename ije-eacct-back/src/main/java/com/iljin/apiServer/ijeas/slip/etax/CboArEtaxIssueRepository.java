package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CboArEtaxIssueRepository extends JpaRepository<CboArEtaxIssue, CboArEtaxIssueKey> {
    List<CboArEtaxIssue> findByEtaxIssueId(BigDecimal etaxIssueId);

    @Query(value = "SELECT FN_CMN_GET_MNG_NO(:compCd, 'TAX', TO_CHAR(SYSDATE, 'YYYYMM') || '01') FROM DUAL", nativeQuery = true)
    String getTaxSeq(@Param("compCd") String compCd);

    @Transactional
    @Modifying
    @Query("update CboArEtaxIssue c set c.interfaceBatchId = ?1, c.lastUpdatedPersonId = ?2, c.lastUpdateDate = ?3 " +
            "where c.etaxIssueId = ?4 and c.conversationId = ?5")
    int updateInterfaceBatchIdAndLastUpdatedPersonIdAndLastUpdateDateByEtaxIssueIdAndConversationId(BigDecimal interfaceBatchId, BigDecimal lastUpdatedPersonId, LocalDateTime lastUpdateDate, BigDecimal etaxIssueId, String conversationId);

    @Transactional
    @Modifying
    @Query("update CboArEtaxIssue c set c.byrEmail = ?1, c.lastUpdatedPersonId = ?2, c.lastUpdateDate = ?3 " +
            "where c.etaxIssueId = ?4 and c.conversationId = ?5")
    int updateForEmailReSend(String byrEmail, BigDecimal lastUpdatedPersonId, LocalDateTime lastUpdateDate, BigDecimal etaxIssueId, String conversationId);

    @Transactional
    @Modifying
    @Query("delete from CboArEtaxIssue c where c.etaxIssueId = ?1")
    int deleteByEtaxIssueId(BigDecimal etaxIssueId);

}

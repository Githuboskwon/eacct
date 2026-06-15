package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CboArTrxMergeRepository extends JpaRepository<CboArTrxMerge, CboArTrxMergeKey> {
    @Query("select c from CboArTrxMerge c where c.etaxIssueId = ?1")
    List<CboArTrxMerge> findByEtaxIssueId(BigDecimal etaxIssueId);

    @Transactional
    @Modifying
    @Query("update CboArTrxMerge c set c.deleteFlag = 'Y' where c.etaxIssueId = ?1")
    int updateDeleteFlagByEtaxIssueId(BigDecimal etaxIssueId);


}

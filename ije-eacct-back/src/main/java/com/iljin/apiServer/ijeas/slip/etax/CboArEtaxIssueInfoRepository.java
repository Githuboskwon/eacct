package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CboArEtaxIssueInfoRepository extends JpaRepository<CboArEtaxIssueInfo, CboArEtaxIssueInfoKey> {
    List<CboArEtaxIssueInfo> findByEtaxIssueId(BigDecimal etaxIssueId);



}

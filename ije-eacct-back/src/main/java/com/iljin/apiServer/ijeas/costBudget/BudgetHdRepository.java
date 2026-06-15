package com.iljin.apiServer.ijeas.costBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


public interface BudgetHdRepository extends JpaRepository<BudgetHd, BudgetHdKey> {

    @Transactional
    @Modifying
    void deleteBySlipNoAndSlipHeaderId(String slip_no, BigDecimal slipHeaderId);

}

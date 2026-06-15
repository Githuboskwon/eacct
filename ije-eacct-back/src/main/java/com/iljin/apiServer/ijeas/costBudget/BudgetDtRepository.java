package com.iljin.apiServer.ijeas.costBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


public interface BudgetDtRepository extends JpaRepository<BudgetDt, BudgetDtKey> {


    Integer countBySlipNoAndSlipHeaderId(String slip_no, BigDecimal slipHeaderId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BudgetDt b WHERE b.slipNo = ?1 AND b.slipHeaderId = ?2 ")
    void deleteBySlipNoAndSlipHeaderId(String slip_no, BigDecimal slipHeaderId);

}

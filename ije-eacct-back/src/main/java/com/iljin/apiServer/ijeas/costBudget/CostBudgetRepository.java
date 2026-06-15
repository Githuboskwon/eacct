package com.iljin.apiServer.ijeas.costBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CostBudgetRepository extends JpaRepository<CostBudget, CostBudgetKey> {


    @Modifying
    @Query("UPDATE CostBudget c SET c.budgetApprYn = :flag WHERE c.compCd = :compCd AND c.ledgerId = :ledgerId AND c.cctrCd = :cctrCd AND c.periodYear = :periodYear AND c.periodMonth  = :periodMonth")
    void updateBudgetApprYn(@Param("flag") String flag,@Param("compCd") String compCd, @Param("ledgerId") BigDecimal ledgerId, @Param("cctrCd") String cctrCd, @Param("periodYear") String periodYear, @Param("periodMonth") String periodMonth);


    @Modifying
    @Query("UPDATE CostBudget c SET c.budgetAmt = :budgetAmt , c.remark = :remark WHERE c.compCd = :compCd AND c.ledgerId = :ledgerId AND c.cctrCd = :cctrCd AND c.periodYear = :periodYear AND c.periodMonth  = :periodMonth AND c.acctCd = :acctCd AND c.pjtCd = :pjtCd AND c.itemGroupCd = :itemGroupCd AND c.degree = :degree")
    void updateBudgetAmt(@Param("budgetAmt") BigDecimal budgetAmt,  @Param("remark") String remark, @Param("compCd") String compCd, @Param("ledgerId") BigDecimal ledgerId, @Param("cctrCd") String cctrCd, @Param("periodYear") String periodYear, @Param("periodMonth") String periodMonth,
                                                                                   @Param("acctCd") String acctCd, @Param("pjtCd") String pjtCd, @Param("itemGroupCd") String itemGroupCd, @Param("degree") String degree);

}

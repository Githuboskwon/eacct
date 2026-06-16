package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(CostBudgetKey.class)
@Table(name = "TB_COST_BUDGET")
@Entity
public class CostBudget extends BaseEntity {

    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "LEDGER_ID")
    BigDecimal ledgerId;

    @Id
    @Column(name = "PERIOD_YEAR")
    String periodYear;

    @Id
    @Column(name = "PERIOD_MONTH")
    String periodMonth;

    @Id
    @Column(name = "CCTR_CD")
    String cctrCd;

    @Column(name = "CCTR_NM")
    String cctrNm;

    @Id
    @Column(name = "ACCT_CD")
    String acctCd;

    @Column(name = "ACCT_NM")
    String acctNm;

    @Id
    @Column(name = "PJT_CD")
    String pjtCd;

    @Column(name = "PJT_NM")
    String pjtNm;

    @Id
    @Column(name = "ITEM_GROUP_CD")
    String itemGroupCd;

    @Column(name = "ITEM_GROUP_NM")
    String itemGroupNm;

    @Column(name = "BUDGET_AMT")
    BigDecimal budgetAmt;

    @Column(name = "REMARK")
    String remark;

    @Id
    @Column(name = "DEGREE")
    String degree;

    @Column(name = "BUDGET_APPR_YN")
    String budgetApprYn;

    @Column(name = "ACTUAL_APPR_YN")
    String actualApprYn;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

    @Column(name = "ATTRIBUTE4")
    String attribute4;

    @Column(name = "ATTRIBUTE5")
    String attribute5;


    @Builder
    public CostBudget(String compCd,BigDecimal ledgerId,String periodYear,String periodMonth,String cctrCd,
                      String cctrNm,String acctCd,String acctNm,String pjtCd,String pjtNm,String itemGroupCd,
                      String itemGroupNm,BigDecimal budgetAmt,String remark,String degree,String budgetApprYn,
                      String actualApprYn,String attribute1){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.budgetAmt = budgetAmt;
        this.remark = remark;
        this.degree = degree;
        this.budgetApprYn = budgetApprYn;
        this.actualApprYn = actualApprYn;
        this.attribute1 = attribute1;
    }

}

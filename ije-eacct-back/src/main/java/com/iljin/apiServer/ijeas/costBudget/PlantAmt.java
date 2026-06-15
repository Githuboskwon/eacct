package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.budget.BudgetAmountsKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(PlantAmtKey.class)
@Table(name = "TB_PLAN_AMT")
@Entity
public class PlantAmt extends BaseEntity {

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

    @Column(name = "ITEM_GROUP_CD")
    String itemGroupCd;

    @Column(name = "ITEM_GROUP_NM")
    String itemGroupNm;

    @Column(name = "PLAN_AMT")
    BigDecimal planAmt;

    @Column(name = "EMERGENCY_PLAN_AMT")
    BigDecimal emergencyPlanAmt;

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



}

package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(BudgetDtKey.class)
@Table(name = "TB_BUDGET_DT")
@Entity
public class BudgetDt extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "LEDGER_ID", nullable = false)
    BigDecimal ledgerId;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_HEADER_ID", nullable = false)
    BigDecimal slipHeaderId;

    @Column(name = "PERIOD_YEAR")
    String periodYear;

    @Column(name = "PERIOD_MONTH")
    String periodMonth;

    @Column(name = "DEPT_CD")
    String deptCd;

    @Column(name = "CCTR_CD")
    String cctrCd;

    @Column(name = "CCTR_NM")
    String cctrNm;

    @Column(name = "ACCT_DIV_NM")
    String acctDivNm;

    @Column(name = "ACCT_CD")
    String acctCd;

    @Id
    @Column(name = "ACCT_NM")
    String acctNm;

    @Column(name = "PJT_CD")
    String pjtCd;

    @Column(name = "PJT_NM")
    String pjtNm;

    @Column(name = "ITEM_GROUP_CD")
    String itemGroupCd;

    @Column(name = "ITEM_GROUP_NM")
    String itemGroupNm;

    @Id
    @Column(name = "PAST_OVER_AMT")
    String pastOverAmt;

    @Id
    @Column(name = "PAST_OVER_REASON")
    String pastOverReason;

    @Id
    @Column(name = "CUR_OVER_AMT")
    String curOverAmt;

    @Id
    @Column(name = "CUR_OVER_REASON")
    String curOverReason;

    @Builder
    public BudgetDt(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId, String periodYear, String periodMonth,
                    String deptCd, String cctrCd, String cctrNm, String acctDivNm, String acctCd, String acctNm,  String pjtCd, String pjtNm,
                    String itemGroupCd, String itemGroupNm, String pastOverAmt, String pastOverReason, String curOverAmt, String curOverReason){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.deptCd = deptCd;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.pastOverAmt = pastOverAmt;
        this.pastOverReason = pastOverReason;
        this.curOverAmt = curOverAmt;
        this.curOverReason = curOverReason;
    }

}

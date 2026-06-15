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
@IdClass(BudgetHdKey.class)
@Table(name = "TB_BUDGET_HD")
@Entity
public class BudgetHd extends BaseEntity {

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

    @Column(name = "PERIOD_YEAR", nullable = false)
    String periodYear;

    @Column(name = "PERIOD_MONTH", nullable = false)
    String periodMonth;

    @Column(name = "CCTR_CD", nullable = false)
    String cctrCd;

    @Column(name = "CCTR_NM", nullable = false)
    String cctrNm;

    @Column(name = "ACCT_DIV_NM", nullable = false)
    String acctDivNm;

    @Id
    @Column(name = "ACCT_CD", nullable = false)
    String acctCd;

    @Column(name = "ACCT_NM", nullable = false)
    String acctNm;

    @Id
    @Column(name = "PJT_CD", nullable = false)
    String pjtCd;

    @Column(name = "PJT_NM", nullable = false)
    String pjtNm;

    @Id
    @Column(name = "ITEM_GROUP_CD", nullable = false)
    String itemGroupCd;

    @Column(name = "ITEM_GROUP_NM", nullable = false)
    String itemGroupNm;

    @Column(name = "PREV_PLAN_AMT")
    BigDecimal prevPlanAmt;

    @Column(name = "PREV_EMERGENCY_PLAN_AMT")
    BigDecimal prevEmergencyPlanAmt;

    @Column(name = "PREV_BD_AMT")
    BigDecimal prevBdAmt;

    @Column(name = "PTD_ACTUAL_AMT")
    BigDecimal ptdActualAmt;

    @Column(name = "PREV_PTD_GAP_AMT")
    BigDecimal prevPtdGapAmt;

    @Column(name = "PREV_YTD_PLAN_AMT")
    BigDecimal prevYtdPlanAmt;

    @Column(name = "PREV_YTD_EMERGENCY_PLAN_AMT")
    BigDecimal prevYtdEmergencyPlanAmt;

    @Column(name = "PREV_YTD_BD_AMT")
    BigDecimal prevYtdBdAmt;

    @Column(name = "YTD_ACTUAL_AMT")
    BigDecimal ytdActualAmt;

    @Column(name = "PREV_YTD_GAP_AMT")
    BigDecimal prevYtdGapAmt;

    @Column(name = "PLAN_AMT")
    BigDecimal planAmt;

    @Column(name = "EMERGENCY_PLAN_AMT")
    BigDecimal emergencyPlanAmt;

    @Column(name = "BD_AMT")
    BigDecimal bdAmt;

    @Column(name = "NEXT_PLAN_AMT")
    BigDecimal nextPlanAmt;

    @Column(name = "NEXT_EMERGENCY_PLAN_AMT")
    BigDecimal nextEmergencyPlanAmt;

    @Column(name = "NEXT_BD_AMT")
    BigDecimal nextBdAmt;

    @Column(name = "NEXT2_PLAN_AMT")
    BigDecimal next2PlanAmt;

    @Column(name = "NEXT2_EMERGENCY_PLAN_AMT")
    BigDecimal next2EmergencyPlanAmt;

    @Column(name = "NEXT2_BD_AMT")
    BigDecimal next2BdAmt;


    @Builder
    public BudgetHd(String compCd,BigDecimal ledgerId,String slipNo,BigDecimal slipHeaderId,String periodYear,String periodMonth,String cctrCd,
                    String cctrNm,String acctDivNm,String acctCd,String acctNm,String pjtCd,String pjtNm,String itemGroupCd,String itemGroupNm,
                    BigDecimal prevPlanAmt,BigDecimal prevEmergencyPlanAmt,BigDecimal prevBdAmt,BigDecimal ptdActualAmt,BigDecimal prevPtdGapAmt,
                    BigDecimal prevYtdPlanAmt,BigDecimal prevYtdEmergencyPlanAmt,BigDecimal prevYtdBdAmt,BigDecimal ytdActualAmt,
                    BigDecimal prevYtdGapAmt,BigDecimal planAmt,BigDecimal emergencyPlanAmt,BigDecimal bdAmt,BigDecimal nextPlanAmt,BigDecimal nextEmergencyPlanAmt,
                    BigDecimal nextBdAmt,BigDecimal next2PlanAmt,BigDecimal next2EmergencyPlanAmt,BigDecimal next2BdAmt){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.prevPlanAmt = prevPlanAmt;
        this.prevEmergencyPlanAmt = prevEmergencyPlanAmt;
        this.prevBdAmt = prevBdAmt;
        this.ptdActualAmt = ptdActualAmt;
        this.prevPtdGapAmt = prevPtdGapAmt;
        this.prevYtdPlanAmt = prevYtdPlanAmt;
        this.prevYtdEmergencyPlanAmt = prevYtdEmergencyPlanAmt;
        this.prevYtdBdAmt = prevYtdBdAmt;
        this.ytdActualAmt = ytdActualAmt;
        this.prevYtdGapAmt = prevYtdGapAmt;
        this.planAmt = planAmt;
        this.emergencyPlanAmt = emergencyPlanAmt;
        this.bdAmt = bdAmt;
        this.nextPlanAmt = nextPlanAmt;
        this.nextEmergencyPlanAmt = nextEmergencyPlanAmt;
        this.nextBdAmt = nextBdAmt;
        this.next2PlanAmt = next2PlanAmt;
        this.next2EmergencyPlanAmt = next2EmergencyPlanAmt;
        this.next2BdAmt = next2BdAmt;
    }

}

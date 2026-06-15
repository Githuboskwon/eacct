package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CostBudgetDto implements Serializable {

    private static final long serialVersionUID = 5729842227739508470L;

    String compCd;

    BigDecimal ledgerId;

    String periodYear;

    String periodMonth;

    String prePeriodYear;

    String prePeriodMonth;

    String cctrCd;

    String cctrNm;

    String acctCd;

    String acctNm;

    String pjtCd;

    String pjtNm;

    String itemGroupCd;

    String itemGroupNm;

    BigDecimal budgetAmt;

    String remark;

    String degree;

    String budgetApprYn;

    String actualApprYn;

    BigDecimal planAmt;

    BigDecimal emergencyPlanAmt;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String searchDeptCd;

    String searchDeptNm;

    String searchDegree;

    String cctrDivCd;

    String cctrDivNm;

    String acctDivCd;

    String acctDivNm;

    String userYn;

    BigDecimal m01PlanAmt;

    BigDecimal m01EmergencyPlanAmt;

    BigDecimal m01BudgetAmt;

    String m01Remark;

    BigDecimal m02PlanAmt;

    BigDecimal m02EmergencyPlanAmt;

    BigDecimal m02BudgetAmt;

    String m02Remark;


    BigDecimal m03PlanAmt;

    BigDecimal m03EmergencyPlanAmt;

    BigDecimal m03BudgetAmt;

    String m03Remark;


    BigDecimal m04PlanAmt;

    BigDecimal m04EmergencyPlanAmt;

    BigDecimal m04BudgetAmt;

    String m04Remark;


    BigDecimal m05PlanAmt;

    BigDecimal m05EmergencyPlanAmt;

    BigDecimal m05BudgetAmt;

    String m05Remark;


    BigDecimal m06PlanAmt;

    BigDecimal m06EmergencyPlanAmt;

    BigDecimal m06BudgetAmt;

    String m06Remark;


    BigDecimal m07PlanAmt;

    BigDecimal m07EmergencyPlanAmt;

    BigDecimal m07BudgetAmt;

    String m07Remark;


    BigDecimal m08PlanAmt;

    BigDecimal m08EmergencyPlanAmt;

    BigDecimal m08BudgetAmt;

    String m08Remark;


    BigDecimal m09PlanAmt;

    BigDecimal m09EmergencyPlanAmt;

    BigDecimal m09BudgetAmt;

    String m09Remark;


    BigDecimal m10PlanAmt;

    BigDecimal m10EmergencyPlanAmt;

    BigDecimal m10BudgetAmt;

    String m10Remark;


    BigDecimal m11PlanAmt;

    BigDecimal m11EmergencyPlanAmt;

    BigDecimal m11BudgetAmt;

    String m11Remark;


    BigDecimal m12PlanAmt;

    BigDecimal m12EmergencyPlanAmt;

    BigDecimal m12BudgetAmt;

    String m12Remark;

    String actual01ApprYn;

    String actual02ApprYn;

    String actual03ApprYn;

    String actual04ApprYn;

    String actual05ApprYn;

    String actual06ApprYn;

    String actual07ApprYn;

    String actual08ApprYn;

    String actual09ApprYn;

    String actual10ApprYn;

    String actual11ApprYn;

    String actual12ApprYn;

    String budget01ApprYn;

    String budget02ApprYn;

    String budget03ApprYn;

    String budget04ApprYn;

    String budget05ApprYn;

    String budget06ApprYn;

    String budget07ApprYn;

    String budget08ApprYn;

    String budget09ApprYn;

    String budget10ApprYn;

    String budget11ApprYn;

    String budget12ApprYn;

    String flag;

    String insertYn;

    String 	pastOverAmt;

    String 	pastOverReason;

    String 	curOverAmt;

    String 	curOverReason;

    String empNo;

    String empNm;

    String result;

    String msg;

    Boolean chkFlag;


    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String periodMonth,
                         String cctrDivCd, String cctrDivNm, String cctrCd, String cctrNm,
                         String acctDivCd, String acctDivNm, String acctCd, String acctNm,
                         String itemGroupCd, String itemGroupNm, String pjtCd, String pjtNm, String userYn,
                         BigDecimal m01PlanAmt, BigDecimal m01EmergencyPlanAmt, BigDecimal m01BudgetAmt, String m01Remark){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.userYn = userYn;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
        this.m01Remark = m01Remark;
    }


    public CostBudgetDto(String compCd,
                         BigDecimal ledgerId,
                         String periodYear,
                         String periodMonth,
                         String cctrDivCd,
                         String cctrDivNm,
                         String cctrCd,
                         String cctrNm,
                         String acctDivCd,
                         String acctDivNm,
                         String acctCd,
                         String acctNm,
                         String itemGroupCd,
                         String itemGroupNm,
                         String pjtCd,
                         String pjtNm,
                         String userYn,
                         BigDecimal m01PlanAmt,
                         BigDecimal m01EmergencyPlanAmt,
                         BigDecimal m01BudgetAmt,
                         String m01Remark,
                         BigDecimal m02PlanAmt,
                         BigDecimal m02EmergencyPlanAmt,
                         BigDecimal m02BudgetAmt,
                         String m02Remark){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.userYn = userYn;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
        this.m01Remark = m01Remark;
        this.m02PlanAmt = m02PlanAmt;
        this.m02EmergencyPlanAmt = m02EmergencyPlanAmt;
        this.m02BudgetAmt = m02BudgetAmt;
        this.m02Remark = m02Remark;
    }

    public CostBudgetDto(String compCd,
                         BigDecimal ledgerId,
                         String periodYear,
                         String periodMonth,
                         String cctrDivCd,
                         String cctrDivNm,
                         String cctrCd,
                         String cctrNm,
                         String acctDivCd,
                         String acctDivNm,
                         String acctCd,
                         String acctNm,
                         String itemGroupCd,
                         String itemGroupNm,
                         String pjtCd,
                         String pjtNm,
                         String userYn,
                         BigDecimal m01PlanAmt,
                         BigDecimal m01EmergencyPlanAmt,
                         BigDecimal m01BudgetAmt,
                         String m01Remark,
                         BigDecimal m02PlanAmt,
                         BigDecimal m02EmergencyPlanAmt,
                         BigDecimal m02BudgetAmt,
                         String m02Remark,
                         BigDecimal m03PlanAmt,
                         BigDecimal m03EmergencyPlanAmt,
                         BigDecimal m03BudgetAmt,
                         String m03Remark){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.userYn = userYn;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
        this.m01Remark = m01Remark;
        this.m02PlanAmt = m02PlanAmt;
        this.m02EmergencyPlanAmt = m02EmergencyPlanAmt;
        this.m02BudgetAmt = m02BudgetAmt;
        this.m02Remark = m02Remark;
        this.m03PlanAmt = m03PlanAmt;
        this.m03EmergencyPlanAmt = m03EmergencyPlanAmt;
        this.m03BudgetAmt = m03BudgetAmt;
        this.m03Remark = m03Remark;
    }

    public CostBudgetDto(BigDecimal ledgerId,
                         String periodYear,
                         String cctrCd,
                         String cctrNm,
                         String actual01ApprYn,
                         String actual02ApprYn,
                         String actual03ApprYn,
                         String actual04ApprYn,
                         String actual05ApprYn,
                         String actual06ApprYn,
                         String actual07ApprYn,
                         String actual08ApprYn,
                         String actual09ApprYn,
                         String actual10ApprYn,
                         String actual11ApprYn,
                         String actual12ApprYn){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.actual01ApprYn = actual01ApprYn;
        this.actual02ApprYn = actual02ApprYn;
        this.actual03ApprYn = actual03ApprYn;
        this.actual04ApprYn = actual04ApprYn;
        this.actual05ApprYn = actual05ApprYn;
        this.actual06ApprYn = actual06ApprYn;
        this.actual07ApprYn = actual07ApprYn;
        this.actual08ApprYn = actual08ApprYn;
        this.actual09ApprYn = actual09ApprYn;
        this.actual10ApprYn = actual10ApprYn;
        this.actual11ApprYn = actual11ApprYn;
        this.actual12ApprYn = actual12ApprYn;
    }

    public CostBudgetDto(BigDecimal ledgerId,
                         String flag,
                         String periodYear,
                         String cctrCd,
                         String cctrNm,
                         String budget01ApprYn,
                         String budget02ApprYn,
                         String budget03ApprYn,
                         String budget04ApprYn,
                         String budget05ApprYn,
                         String budget06ApprYn,
                         String budget07ApprYn,
                         String budget08ApprYn,
                         String budget09ApprYn,
                         String budget10ApprYn,
                         String budget11ApprYn,
                         String budget12ApprYn){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.budget01ApprYn = budget01ApprYn;
        this.budget02ApprYn = budget02ApprYn;
        this.budget03ApprYn = budget03ApprYn;
        this.budget04ApprYn = budget04ApprYn;
        this.budget05ApprYn = budget05ApprYn;
        this.budget06ApprYn = budget06ApprYn;
        this.budget07ApprYn = budget07ApprYn;
        this.budget08ApprYn = budget08ApprYn;
        this.budget09ApprYn = budget09ApprYn;
        this.budget10ApprYn = budget10ApprYn;
        this.budget11ApprYn = budget11ApprYn;
        this.budget12ApprYn = budget12ApprYn;
    }

    BigDecimal totalPlanAmt;

    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String cctrCd, String cctrNm, String acctCd, String acctNm,
                         String pjtCd, String pjtNm, String itemGroupCd, String itemGroupNm, BigDecimal m01PlanAmt, BigDecimal m02PlanAmt,
                         BigDecimal m03PlanAmt, BigDecimal m04PlanAmt, BigDecimal m05PlanAmt, BigDecimal m06PlanAmt, BigDecimal m07PlanAmt,
                         BigDecimal m08PlanAmt, BigDecimal m09PlanAmt, BigDecimal m10PlanAmt, BigDecimal m11PlanAmt, BigDecimal m12PlanAmt, BigDecimal totalPlanAmt){

        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.m01PlanAmt = m01PlanAmt;
        this.m02PlanAmt = m02PlanAmt;
        this.m03PlanAmt = m03PlanAmt;
        this.m04PlanAmt = m04PlanAmt;
        this.m05PlanAmt = m05PlanAmt;
        this.m06PlanAmt = m06PlanAmt;
        this.m07PlanAmt = m07PlanAmt;
        this.m08PlanAmt = m08PlanAmt;
        this.m09PlanAmt = m09PlanAmt;
        this.m10PlanAmt = m10PlanAmt;
        this.m11PlanAmt = m11PlanAmt;
        this.m12PlanAmt = m12PlanAmt;
        this.totalPlanAmt = totalPlanAmt;
    }

    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String cctrDivCd, String cctrDivNm,
                         String cctrCd, String cctrNm, String acctDivCd, String acctDivNm,
                         BigDecimal m01PlanAmt, BigDecimal m01EmergencyPlanAmt, BigDecimal m01BudgetAmt,
                         BigDecimal m02PlanAmt, BigDecimal m02EmergencyPlanAmt, BigDecimal m02BudgetAmt,
                         BigDecimal m03PlanAmt, BigDecimal m03EmergencyPlanAmt, BigDecimal m03BudgetAmt,
                         String acctCd, String acctNm){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
        this.m02PlanAmt = m02PlanAmt;
        this.m02EmergencyPlanAmt = m02EmergencyPlanAmt;
        this.m02BudgetAmt = m02BudgetAmt;
        this.m03PlanAmt = m03PlanAmt;
        this.m03EmergencyPlanAmt = m03EmergencyPlanAmt;
        this.m03BudgetAmt = m03BudgetAmt;
    }

    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String cctrDivCd, String cctrDivNm,
                         String cctrCd, String cctrNm, String acctDivCd, String acctDivNm,
                         BigDecimal m01PlanAmt, BigDecimal m01EmergencyPlanAmt, BigDecimal m01BudgetAmt,
                         BigDecimal m02PlanAmt, BigDecimal m02EmergencyPlanAmt, BigDecimal m02BudgetAmt,
                         String acctCd, String acctNm){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
        this.m02PlanAmt = m02PlanAmt;
        this.m02EmergencyPlanAmt = m02EmergencyPlanAmt;
        this.m02BudgetAmt = m02BudgetAmt;
    }

    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String cctrDivCd, String cctrDivNm,
                         String cctrCd, String cctrNm, String acctDivCd, String acctDivNm,
                         BigDecimal m01PlanAmt, BigDecimal m01EmergencyPlanAmt, BigDecimal m01BudgetAmt,
                         String acctCd, String acctNm){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrDivCd = cctrDivCd;
        this.cctrDivNm = cctrDivNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivCd = acctDivCd;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.m01PlanAmt = m01PlanAmt;
        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
        this.m01BudgetAmt = m01BudgetAmt;
    }


    BigDecimal prevPlanAmt;

    BigDecimal prevEmergencyPlanAmt;

    BigDecimal prevBdAmt;

    BigDecimal ptdActualAmt;

    BigDecimal prevPtdGapAmt;

    BigDecimal prevYtdPlanAmt;

    BigDecimal prevYtdEmergencyPlanAmt;

    BigDecimal prevYtdBdAmt;

    BigDecimal ytdActualAmt;

    BigDecimal prevYtdGapAmt;

    BigDecimal bdAmt;

    BigDecimal nextPlanAmt;

    BigDecimal nextEmergencyPlanAmt;

    BigDecimal nextBdAmt;

    BigDecimal next2PlanAmt;

    BigDecimal next2EmergencyPlanAmt;

    BigDecimal next2BdAmt;

    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String periodMonth, String prePeriodYear, String prePeriodMonth,
                         String cctrCd, String cctrNm, String acctDivNm, String acctCd, String acctNm, String itemGroupCd,
                         String itemGroupNm, String pjtCd, String pjtNm, BigDecimal prevPlanAmt,BigDecimal prevEmergencyPlanAmt,
                         BigDecimal prevBdAmt,BigDecimal ptdActualAmt,BigDecimal prevPtdGapAmt,BigDecimal prevYtdPlanAmt,
                         BigDecimal prevYtdEmergencyPlanAmt,BigDecimal prevYtdBdAmt,BigDecimal ytdActualAmt,BigDecimal prevYtdGapAmt,
                         BigDecimal planAmt, BigDecimal emergencyPlanAmt, BigDecimal bdAmt,BigDecimal nextPlanAmt,
                         BigDecimal nextEmergencyPlanAmt,BigDecimal nextBdAmt, BigDecimal next2PlanAmt,
                         BigDecimal next2EmergencyPlanAmt,BigDecimal next2BdAmt){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.prePeriodYear = prePeriodYear;
        this.prePeriodMonth = prePeriodMonth;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivNm = acctDivNm;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
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

    String type;

    String  teamCd;

    String  teamNm;


    BigDecimal  quarterNum;

    BigDecimal  periodNum;

    BigDecimal  eventId;

    BigDecimal  journalLineId;

    String  slipNo;

    BigDecimal  lineNo;

    String  description;

    BigDecimal  vendorId;

    BigDecimal  vendorSiteId;

    String  vendorName;

    String  integrationVendorNum;

    String  currencyCode;

    BigDecimal  exchangeRate;

    BigDecimal  amount;

    Timestamp glDate;

    String  deptCode;

    String  deptName;

    String  segment5;

    BigDecimal  codeCombinationId;

    String  projectNumber;

    String  taskNumber;

    String  createdBy;

    String  createdName;

    BigDecimal  orgId;

    String  slipType;

    String scanNo;

    String postDt;

    BigDecimal userId;

    String nextAppUserId;

    String userNm;

    String nextAppUserNm;

    String deptCd;

    String deptNm;

    String headerRemark;

    String refUserId;

    String docTitle;

    String docUrl;

    String docNo;
    BigDecimal  slipHeaderId;

    BigDecimal  approvalGroupId;

    String  slipGroupNumber;

    String  slipGroupYn;

    String  slipStatus;

    String  slipForm;

    String  ttypeInputModule;

    public CostBudgetDto(String type, String teamCd, String teamNm, BigDecimal periodYear, BigDecimal quarterNum, BigDecimal periodNum,
                         String acctCd, String acctNm, BigDecimal eventId, BigDecimal journalLineId, String slipNo, BigDecimal lineNo,
                         String description, BigDecimal vendorId, BigDecimal vendorSiteId, String vendorName, String  integrationVendorNum,
                         String  currencyCode, BigDecimal  exchangeRate, BigDecimal  amount, Timestamp glDate, String  deptCode, String  deptName,
                         String  segment5, BigDecimal  codeCombinationId, String  projectNumber, String  taskNumber, String  createdBy, String  createdName,
                         BigDecimal  orgId, String  slipType, BigDecimal  slipHeaderId, BigDecimal  approvalGroupId, String  slipGroupNumber,
                         Character slipGroupYn,  String  slipStatus, String  slipForm, String  ttypeInputModule){
        this.type = type;
        this.teamCd = teamCd;
        this.teamNm = teamNm;
        this.periodYear = String.valueOf(periodYear);
        this.quarterNum = quarterNum;
        this.periodNum = periodNum;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.eventId = eventId;
        this.journalLineId = journalLineId;
        this.slipNo = slipNo;
        this.lineNo = lineNo;
        this.description = description;
        this.vendorId = vendorId;
        this.vendorSiteId = vendorSiteId;
        this.vendorName = vendorName;
        this.integrationVendorNum = integrationVendorNum;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.amount = amount;
        this.glDate = glDate;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.segment5 = segment5;
        this.codeCombinationId = codeCombinationId;
        this.projectNumber = projectNumber;
        this.taskNumber = taskNumber;
        this.createdBy = createdBy;
        this.createdName = createdName;
        this.orgId = orgId;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.slipGroupNumber = slipGroupNumber;
        this.slipGroupYn = String.valueOf(slipGroupYn);
        this.slipStatus = slipStatus;
        this.slipForm = slipForm;
        this.ttypeInputModule = ttypeInputModule;
    }


    BigDecimal count;

    public CostBudgetDto(BigDecimal count){
        this.count = count;
    }

    public CostBudgetDto(String attribute2){
        this.attribute2 = attribute2;
    }

    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String cctrCd, String cctrNm, String budget01ApprYn , String degree){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.budget01ApprYn = budget01ApprYn;
        this.degree = degree;
    };

    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String cctrCd, String cctrNm, String budget01ApprYn , String budget02ApprYn , String degree){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.budget01ApprYn = budget01ApprYn;
        this.budget02ApprYn =  budget02ApprYn;
        this.degree = degree;
    };

    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String cctrCd, String cctrNm, String budget01ApprYn, String budget02ApprYn , String budget03ApprYn, String degree){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.budget01ApprYn = budget01ApprYn;
        this.budget02ApprYn = budget02ApprYn;
        this.budget03ApprYn = budget03ApprYn;
        this.degree = degree;
    };


    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String cctrCd, String cctrNm, String actualApprYn){
        this.ledgerId = ledgerId;
        this.periodYear = periodYear;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.actualApprYn = actualApprYn;
    }


    BigDecimal planPreAmt;

    BigDecimal emcyPlanPreAmt;

    BigDecimal budgetPreAmt;

    BigDecimal actualPreAmt;

    BigDecimal variAmt;

    BigDecimal planAccmAmt;

    BigDecimal emcyPlanAccmAmt;

    BigDecimal budgetAccmAmt;

    BigDecimal actualAccmAmt;

    BigDecimal accmVariAmt;

    BigDecimal emcyPlanAmt;

    BigDecimal planNextAmt;

    BigDecimal emcyPlanNextAmt;

    BigDecimal budgetNextAmt;

    BigDecimal planNext2Amt;

    BigDecimal emcyPlanNext2Amt;

    BigDecimal budgetNext2Amt;

    BigDecimal uFileCnt;

    BigDecimal jiniCnt;


    public CostBudgetDto(BigDecimal ledgerId, String periodYear, String periodMonth, String cctrCd, String cctrNm, String acctDivNm,
                         String acctCd, String acctNm, String pjtCd, String pjtNm, String itemGroupCd, String itemGroupNm,
                         BigDecimal prevPlanAmt,BigDecimal prevEmergencyPlanAmt,
                         BigDecimal prevBdAmt,BigDecimal ptdActualAmt,BigDecimal prevPtdGapAmt,BigDecimal prevYtdPlanAmt,
                         BigDecimal prevYtdEmergencyPlanAmt,BigDecimal prevYtdBdAmt,BigDecimal ytdActualAmt,BigDecimal prevYtdGapAmt,
                         BigDecimal planAmt, BigDecimal emergencyPlanAmt, BigDecimal bdAmt,BigDecimal nextPlanAmt,
                         BigDecimal nextEmergencyPlanAmt,BigDecimal nextBdAmt, BigDecimal next2PlanAmt,
                         BigDecimal next2EmergencyPlanAmt,BigDecimal next2BdAmt){
        this.ledgerId = ledgerId;
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


    public CostBudgetDto(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId, String prePeriodYear, String periodMonth,
                         String cctrCd, String cctrNm, String acctDivNm, String acctCd, String acctNm, String pjtCd, String pjtNm, String itemGroupCd,
                         String itemGroupNm, BigDecimal prevPlanAmt, BigDecimal prevEmergencyPlanAmt, BigDecimal prevBdAmt, BigDecimal ptdActualAmt,
                         BigDecimal prevPtdGapAmt, BigDecimal prevYtdPlanAmt, BigDecimal prevYtdEmergencyPlanAmt, BigDecimal prevYtdBdAmt,
                         BigDecimal ytdActualAmt, BigDecimal prevYtdGapAmt, BigDecimal planAmt, BigDecimal emergencyPlanAmt, BigDecimal bdAmt,
                         BigDecimal nextPlanAmt, BigDecimal nextEmergencyPlanAmt, BigDecimal nextBdAmt, BigDecimal next2PlanAmt, BigDecimal next2EmergencyPlanAmt, BigDecimal next2BdAmt){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.prePeriodYear = prePeriodYear;
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

    public CostBudgetDto(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId, String periodYear, String periodMonth,
                         String cctrCd, String cctrNm, String acctDivNm, String acctCd , String acctNm, String pastOverAmt, String pastOverReason,
                         String curOverAmt, String curOverReason){
        this.compCd = compCd;
        this.ledgerId = ledgerId;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.acctDivNm = acctDivNm;
        this.acctCd  = acctCd ;
        this.acctNm = acctNm;
        this.pastOverAmt = pastOverAmt;
        this.pastOverReason = pastOverReason;
        this.curOverAmt = curOverAmt;
        this.curOverReason = curOverReason;
    }

    public CostBudgetDto(String slipNo, BigDecimal slipHeaderId, BigDecimal approvalGroupId, String headerRemark, String userId, String userNm, String postDt,
                         String scanNo, String remark, String nextAppUserId, String nextAppUserNm, String docTitle, String docUrl){
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.headerRemark = headerRemark;
        this.userId = BigDecimal.valueOf(Long.parseLong(userId));
        this.userNm = userNm;
        this.postDt = postDt;
        this.scanNo = scanNo;
        this.remark = remark;
        this.nextAppUserId = nextAppUserId;
        this.nextAppUserNm = nextAppUserNm;
        this.docTitle = docTitle;
        this.docUrl = docUrl;
    }

    public CostBudgetDto(String compCd, String slipNo, BigDecimal slipHeaderId, String empNo, String empNm, String deptCd, String deptNm,
                         String cctrCd, String cctrNm, String periodYear, String periodMonth, String headerRemark,
                         String remark, String postDt, BigDecimal uFileCnt, BigDecimal jiniCnt){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.empNo = empNo;
        this.empNm = empNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.periodYear = periodYear;
        this.periodMonth = periodMonth;
        this.headerRemark = headerRemark;
        this.remark = remark;
        this.postDt = postDt;
        this.uFileCnt = uFileCnt;
        this.jiniCnt = jiniCnt;
    }

//    public CostBudgetDto(String compCd, BigDecimal ledgerId, String slipNo, BigDecimal slipHeaderId, String periodYear, String periodMonth,
//                         String cctrCd, String cctrNm, String acctDivNm, String acctCd, String acctNm, String itemGroupCd,
//                         String itemGroupNm, String pjtCd, String pjtNm, BigDecimal prevPlanAmt,BigDecimal prevEmergencyPlanAmt,
//                         BigDecimal prevBdAmt,BigDecimal ptdActualAmt,BigDecimal prevPtdGapAmt,BigDecimal prevYtdPlanAmt,
//                         BigDecimal prevYtdEmergencyPlanAmt,BigDecimal prevYtdBdAmt,BigDecimal ytdActualAmt,BigDecimal prevYtdGapAmt,
//                         BigDecimal planAmt, BigDecimal emergencyPlanAmt, BigDecimal bdAmt,BigDecimal nextPlanAmt,
//                         BigDecimal nextEmergencyPlanAmt,BigDecimal nextBdAmt, BigDecimal next2PlanAmt,
//                         BigDecimal next2EmergencyPlanAmt,BigDecimal next2BdAmt){
//        this.compCd = compCd;
//        this.ledgerId = ledgerId;
//        this.slipNo = slipNo;
//        this.slipHeaderId = slipHeaderId;
//        this.periodYear = periodYear;
//        this.periodMonth = periodMonth;
//        this.cctrCd = cctrCd;
//        this.cctrNm = cctrNm;
//        this.acctDivNm = acctDivNm;
//        this.acctCd = acctCd;
//        this.acctNm = acctNm;
//        this.itemGroupCd = itemGroupCd;
//        this.itemGroupNm = itemGroupNm;
//        this.pjtCd = pjtCd;
//        this.pjtNm = pjtNm;
//        this.prevPlanAmt = prevPlanAmt;
//        this.prevEmergencyPlanAmt = prevEmergencyPlanAmt;
//        this.prevBdAmt = prevBdAmt;
//        this.ptdActualAmt = ptdActualAmt;
//        this.prevPtdGapAmt = prevPtdGapAmt;
//        this.prevYtdPlanAmt = prevYtdPlanAmt;
//        this.prevYtdEmergencyPlanAmt = prevYtdEmergencyPlanAmt;
//        this.prevYtdBdAmt = prevYtdBdAmt;
//        this.ytdActualAmt = ytdActualAmt;
//        this.prevYtdGapAmt = prevYtdGapAmt;
//        this.planAmt = planAmt;
//        this.emergencyPlanAmt = emergencyPlanAmt;
//        this.bdAmt = bdAmt;
//        this.nextPlanAmt = nextPlanAmt;
//        this.nextEmergencyPlanAmt = nextEmergencyPlanAmt;
//        this.nextBdAmt = nextBdAmt;
//        this.next2PlanAmt = next2PlanAmt;
//        this.next2EmergencyPlanAmt = next2EmergencyPlanAmt;
//        this.next2BdAmt = next2BdAmt;
//    }

//    public CostBudgetDto(String compCd, BigDecimal ledgerId, String periodYear, String cctrDivCd, String cctrDivNm,
//                         String cctrCd, String cctrNm, String acctDivCd, String acctDivNm, String acctCd, String acctNm,
//                         String itemGroupCd, String itemGroupNm, String pjtCd, String pjtNm, String userYn,
//                         BigDecimal m01PlanAmt, BigDecimal m01EmergencyPlanAmt, BigDecimal m01BudgetAmt, String m01Remark,
//                         BigDecimal m02PlanAmt, BigDecimal m02EmergencyPlanAmt, BigDecimal m02BudgetAmt, String m02Remark,
//                         BigDecimal m03PlanAmt, BigDecimal m03EmergencyPlanAmt, BigDecimal m03BudgetAmt, String m03Remark,
//                         BigDecimal m04PlanAmt, BigDecimal m04EmergencyPlanAmt, BigDecimal m04BudgetAmt, String m04Remark,
//                         BigDecimal m05PlanAmt, BigDecimal m05EmergencyPlanAmt, BigDecimal m05BudgetAmt, String m05Remark,
//                         BigDecimal m06PlanAmt, BigDecimal m06EmergencyPlanAmt, BigDecimal m06BudgetAmt, String m06Remark,
//                         BigDecimal m07PlanAmt, BigDecimal m07EmergencyPlanAmt, BigDecimal m07BudgetAmt, String m07Remark,
//                         BigDecimal m08PlanAmt, BigDecimal m08EmergencyPlanAmt, BigDecimal m08BudgetAmt, String m08Remark,
//                         BigDecimal m09PlanAmt, BigDecimal m09EmergencyPlanAmt, BigDecimal m09BudgetAmt, String m09Remark,
//                         BigDecimal m10PlanAmt, BigDecimal m10EmergencyPlanAmt, BigDecimal m10BudgetAmt, String m10Remark,
//                         BigDecimal m11PlanAmt, BigDecimal m11EmergencyPlanAmt, BigDecimal m11BudgetAmt, String m11Remark,
//                         BigDecimal m12PlanAmt, BigDecimal m12EmergencyPlanAmt, BigDecimal m12BudgetAmt, String m12Remark){
//        this.compCd = compCd;
//        this.ledgerId = ledgerId;
//        this.periodYear = periodYear;
//        this.cctrDivCd = cctrDivCd;
//        this.cctrDivNm = cctrDivNm;
//        this.cctrCd = cctrCd;
//        this.cctrNm = cctrNm;
//        this.acctDivCd = acctDivCd;
//        this.acctDivNm = acctDivNm;
//        this.acctCd = acctCd;
//        this.acctNm = acctNm;
//        this.itemGroupCd = itemGroupCd;
//        this.itemGroupNm = itemGroupNm;
//        this.pjtCd = pjtCd;
//        this.pjtNm = pjtNm;
//        this.userYn = userYn;
//        this.m01PlanAmt = m01PlanAmt;
//        this.m01EmergencyPlanAmt = m01EmergencyPlanAmt;
//        this.m01BudgetAmt = m01BudgetAmt;
//        this.m01Remark = m01Remark;
//        this.m02PlanAmt = m02PlanAmt;
//        this.m02EmergencyPlanAmt = m02EmergencyPlanAmt;
//        this.m02BudgetAmt = m02BudgetAmt;
//        this.m02Remark = m02Remark;
//        this.m03PlanAmt = m03PlanAmt;
//        this.m03EmergencyPlanAmt = m03EmergencyPlanAmt;
//        this.m03BudgetAmt = m03BudgetAmt;
//        this.m03Remark = m03Remark;
//        this.m04PlanAmt = m04PlanAmt;
//        this.m04EmergencyPlanAmt = m04EmergencyPlanAmt;
//        this.m04BudgetAmt = m04BudgetAmt;
//        this.m04Remark = m04Remark;
//        this.m05PlanAmt = m05PlanAmt;
//        this.m05EmergencyPlanAmt = m05EmergencyPlanAmt;
//        this.m05BudgetAmt = m05BudgetAmt;
//        this.m05Remark = m05Remark;
//        this.m06PlanAmt = m06PlanAmt;
//        this.m06EmergencyPlanAmt = m06EmergencyPlanAmt;
//        this.m06BudgetAmt = m06BudgetAmt;
//        this.m06Remark = m06Remark;
//        this.m07PlanAmt = m07PlanAmt;
//        this.m07EmergencyPlanAmt = m07EmergencyPlanAmt;
//        this.m07BudgetAmt = m07BudgetAmt;
//        this.m07Remark = m07Remark;
//        this.m08PlanAmt = m08PlanAmt;
//        this.m08EmergencyPlanAmt = m08EmergencyPlanAmt;
//        this.m08BudgetAmt = m08BudgetAmt;
//        this.m08Remark = m08Remark;
//        this.m09PlanAmt = m09PlanAmt;
//        this.m09EmergencyPlanAmt = m09EmergencyPlanAmt;
//        this.m09BudgetAmt = m09BudgetAmt;
//        this.m09Remark = m09Remark;
//        this.m10PlanAmt = m10PlanAmt;
//        this.m10EmergencyPlanAmt = m10EmergencyPlanAmt;
//        this.m10BudgetAmt = m10BudgetAmt;
//        this.m10Remark = m10Remark;
//        this.m11PlanAmt = m11PlanAmt;
//        this.m11EmergencyPlanAmt = m11EmergencyPlanAmt;
//        this.m11BudgetAmt = m11BudgetAmt;
//        this.m11Remark = m11Remark;
//        this.m12PlanAmt = m12PlanAmt;
//        this.m12EmergencyPlanAmt = m12EmergencyPlanAmt;
//        this.m12BudgetAmt = m12BudgetAmt;
//        this.m12Remark = m12Remark;
//    }


}
@Data
class CostBudgetResult {
    List<CostBudgetDto> list;
    List<CostBudgetDto> planConfirm;
    List<CostBudgetDto> savePerformance;
}

@Data
class CostBudgetSaveList{
    CostBudgetDto main;
    List<CostBudgetDto> list;
    CostBudgetDto planConfirm;
}


@Data
class AccountDetailResult {
    List<CostBudgetDto> list;
    BigDecimal total;
}

@Data
class DraftListResult {
    List<CostBudgetDto> list;
    List<CostBudgetDto> data;
    List<CostBudgetDto> dtList;
}


@Data
class DraftResult {
    CostBudgetDto data;
    List<CostBudgetDto> subData;
//    List<CostBudgetDto> apprLine;
}
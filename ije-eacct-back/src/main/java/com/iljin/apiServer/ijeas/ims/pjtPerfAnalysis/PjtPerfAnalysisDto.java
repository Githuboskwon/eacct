package com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PjtPerfAnalysisDto implements Serializable {

    String orgId;

    //PJT ID
    String projectId;

    //PJT 코드
    String projectCode;

    //PJT 구분
    String projectGubun;


    //PJT 명
    String projectName;

    //타스크 ID
    String taskId;

    //타스크 번호
    String taskNumber;

    //타스크 명
    String taskName;

    //PJT 관리번호
    String projectManageNo;

    //PJT 시작일
    String startDate;

    //PJT 종료일
    String endDate;

    //PJT 기간(개월수)
    String totalMonth;

    //수주금액
    String ctAmount;

    //수주금액 외화
    String ctAmountFor;

    //상태
    String projectStatus;

    //소장/담당자 사번
    String pjtSiteManagerUserId;

    //소장/담당자 명
    String pjtSiteManagerUserName;

    //소장/담당자 연락처
    String pjtSiteManagerPhone;

    //영업담당 사번
    String pjtBusinessUserId;

    //영업담당 명
    String pjtBusinessUserName;

    //지역
    String pjtLocation;

    //A:공사국내, B:공사해외
    String pjtType;

    //전표번호
    String slipNo;

    //첨부파일 KEY
    String docNo;

    //비고
    String remark;

    //부서코드
    String deptCode;

    //부서명
    String deptName;

    //팀ID - 사용안함
    String jobId;

    //팀명 - 사용안함
    String jobName;

    //작성일
    String addDate;

    //작성시간
    String addTime;

    //작성자
    String addUserId;

    //수정일
    String changeDate;

    //수정시간
    String changeTime;

    //수정자
    String changeUserId;

    //고객 ID
    String customerId;

    //고객명
    String customerName;

    //최종수요처코드
    String mainContractCode;

    //최종수요처명
    String mainContractName;

    //수주일
    String ctDate;

    //통화
    String ctCurrency;

    //환율
    String ctExchangeRate;

    //환율일자
    String ctExchangeRateDate;

    String pjtJobId;

    String pjtJobName;

    String countryCode;

    String countryName;

    String issue;

    String issueMeasure;

    String issueChangeDate;

    String issueChangeTime;

    String issueChangeUserId;

    String degree;

    String weeklyIssue;

    String attachNo;

    //예산부서 - 팀
    String bdeptCode;

    //예산부서 - 이름
    String bdeptName;

    String pjtBdeptCode;

    String pjtBdeptName;

    String ctOriAmount;

    String slipType;

    String saveStartDate;

    String saveEndDate;

    String inAmtPerFlag;

    String addUserNameUse;

    BigDecimal contractOriAmount;

    String salesPer;

    String processPer;

    String claimPer;

    String collectionPer;

    BigDecimal fTam;

    BigDecimal planAmt;

    BigDecimal planAmtPer;

    BigDecimal resultAmt;

    BigDecimal resultAmt1;

    BigDecimal resultAmtPer;

    BigDecimal resultAmtPer1;

    BigDecimal revenueAmount;

    BigDecimal revenueAmount1;

    BigDecimal slipHeaderId;

    BigDecimal totalOfferUnitCost;

    BigDecimal totalSalesProfitBudget;

    BigDecimal totalSalesProfitBudget1;

    BigDecimal totalSalesProfitPlan;

    BigDecimal version;

    String projectNumber;

    /* 실적 분석 조회 */
    public PjtPerfAnalysisDto(BigDecimal contractOriAmount,String salesPer,String processPer,String claimPer,String collectionPer,
                                String ctAmount,String ctAmountFor,String degree,String endDate,BigDecimal fTam,String orgId,
                                BigDecimal planAmt,BigDecimal planAmtPer,String projectCode,String projectManageNo,String projectName, String projectGubun,
                                BigDecimal resultAmt,BigDecimal resultAmt1,BigDecimal resultAmtPer,BigDecimal resultAmtPer1,BigDecimal revenueAmount,
                                BigDecimal revenueAmount1,BigDecimal slipHeaderId,String slipNo,String startDate,String taskNumber,String totalMonth,
                                BigDecimal totalOfferUnitCost,BigDecimal totalSalesProfitBudget,BigDecimal totalSalesProfitBudget1,
                                BigDecimal totalSalesProfitPlan,BigDecimal version, String countryCode, String countryName, String addUserId) {
        this.contractOriAmount = contractOriAmount;
        this.salesPer = salesPer;
        this.processPer = processPer;
        this.claimPer = claimPer;
        this.collectionPer = collectionPer;
        this.ctAmount = ctAmount;
        this.ctAmountFor = ctAmountFor;
        this.degree = degree;
        this.endDate = endDate;
        this.fTam = fTam;
        this.orgId = orgId;
        this.planAmt = planAmt;
        this.planAmtPer = planAmtPer;
        this.projectCode = projectCode;
        this.projectManageNo = projectManageNo;
        this.projectName = projectName;
        this.projectGubun = projectGubun;
        this.resultAmt = resultAmt;
        this.resultAmt1 = resultAmt1;
        this.resultAmtPer = resultAmtPer;
        this.resultAmtPer1 = resultAmtPer1;
        this.revenueAmount = revenueAmount;
        this.revenueAmount1 = revenueAmount1;
        this.slipHeaderId = slipHeaderId;
        this.slipNo = slipNo;
        this.startDate = startDate;
        this.taskNumber = taskNumber;
        this.totalMonth = totalMonth;
        this.totalOfferUnitCost = totalOfferUnitCost;
        this.totalSalesProfitBudget = totalSalesProfitBudget;
        this.totalSalesProfitBudget1 = totalSalesProfitBudget1;
        this.totalSalesProfitPlan = totalSalesProfitPlan;
        this.version = version;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.addUserId = addUserId;
    }

    String revenuePeriod;

    String itemGroup;

    String soPaType;

    String soPaNumber;

    String soLinePaEventNumber;

    String itemCode;

    BigDecimal enteredAmount;

    String coaSegment1;

    String coaSegment1Desc;

    /* 매출 상세 조회 */
    public PjtPerfAnalysisDto(String revenuePeriod, String projectNumber, String projectName, String itemGroup,
                              String soPaType, String soPaNumber, String soLinePaEventNumber, String itemCode,
                              BigDecimal enteredAmount, BigDecimal revenueAmount, String coaSegment1, String coaSegment1Desc){
        this.revenuePeriod = revenuePeriod;
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.itemGroup = itemGroup;
        this.soPaType = soPaType;
        this.soPaNumber = soPaNumber;
        this.soLinePaEventNumber = soLinePaEventNumber;
        this.itemCode = itemCode;
        this.enteredAmount = enteredAmount;
        this.revenueAmount = revenueAmount;
        this.coaSegment1 = coaSegment1;
        this.coaSegment1Desc = coaSegment1Desc;
    }

    BigDecimal billAmount;

    /* 청구/수금 상세 조회 */
    public PjtPerfAnalysisDto(String revenuePeriod, String projectNumber, String projectName, String itemGroup, BigDecimal billAmount){
        this.revenuePeriod = revenuePeriod;
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.itemGroup = itemGroup;
        this.billAmount = billAmount;
    }


    BigDecimal periodYear;

    BigDecimal periodNum;

    BigDecimal totalAmount;

    /* 총비용 상세 조회 */
    public PjtPerfAnalysisDto(String projectNumber, String projectName, BigDecimal periodYear, BigDecimal periodNum, BigDecimal totalAmount, String startDate, String endDate){
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.periodYear = periodYear;
        this.periodNum = periodNum;
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}

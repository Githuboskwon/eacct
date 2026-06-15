package com.iljin.apiServer.ijeas.card;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class CardUseListDto implements Serializable {

    private static final long serialVersionUID = 7326659560192153187L;

    String compCd;

    String usedNo;

    String cardNo;

    String apprNo;

    String cardType;

    String slipNo;

    Integer slipHeaderId;

    Integer slipLineId;

    String taxTypeCd;

    String status;

    String cancelFlag;

    String usedOrgNo;

    String employeeNo;

    String usedDt;

    String usedTime;

    String userId;

    String userNm;

    String deptCd;

    String deptNm;

    String usedAmt;

    String originAmt;

    String surtax;

    String serviceCharge;

    String usedForAmt;

    String usedCur;

    String cardComCd;

    String cardComNm;

    String irsNo;

    String storeCd;

    String storeNm;

    String abroadFlag;

    String mccCd;

    String mccNm;

    String taxType;

    String storeOwner;

    String storeAddr;

    String realStoreCd;

    String realStoreNm;

    String amtAmountOri;

    String vatAmountOri;

    String projectNo;

    String desc1;

    String desc2;

    String desc3;

    String desc4;

    String temp1;

    String temp2;

    String temp3;

    String temp4;

    String temp5;

    String buyDt;

    String remark;

    String jobGradeNm;

    String productNm;

    String deptNmBack;

    String cctrNm;

    String searchDtmFr;

    String searchDtmTo;

    String cctrCd;

    String deptCdBack;

    String trAcctNm;

    String pjtNm;

    String taxCode;

    String taskName;

    String tText;

    String slipStatus;

    String dtCase;

    String blgDeptCd;

    String blgDeptNm;


    @QueryProjection
    public CardUseListDto ( String compCd, String usedNo,String cardNo,String jobGradeNm,String deptNm, String userId,String userNm,String usedDt,String usedTime, String buyDt,
                            String status,String cancelFlag, String apprNo, String irsNo,String usedAmt,String originAmt,String surtax,String serviceCharge,
                            String storeNm,String storeAddr,String slipNo, String slipStatus,String mccNm,String taxType,String abroadFlag, String temp1, String temp3,
                            String productNm,String deptNmBack, String cctrNm ,String trAcctNm , String taxCode, String pjtNm, String taskName, String tText ){
        this.compCd = compCd;
        this.usedNo = usedNo;
        this.cardNo = cardNo;
        this.jobGradeNm = jobGradeNm;
        this.deptNm = deptNm;
        this.userId = userId;
        this.userNm = userNm;
        this.usedDt = usedDt;
        this.usedTime = usedTime;
        this.buyDt = buyDt;
        this.status = status;
        this.cancelFlag = cancelFlag;
        this.apprNo = apprNo;
        this.irsNo = irsNo;
        this.usedAmt = usedAmt;
        this.originAmt = originAmt;
        this.surtax = surtax;
        this.serviceCharge = serviceCharge;
        this.storeNm = storeNm;
        this.storeAddr = storeAddr;
        this.slipNo = slipNo;
        this.slipStatus = slipStatus;
        this.mccNm = mccNm;
        this.taxType = taxType;
        this.abroadFlag = abroadFlag;
        this.temp1 = temp1;
        this.temp3 = temp3;
        this.productNm = productNm;
        this.deptNmBack = deptNmBack;
        this.cctrNm = cctrNm;
        this.trAcctNm = trAcctNm;
        this.taxCode = taxCode;
        this.pjtNm = pjtNm;
        this.taskName = taskName;
        this.tText = tText;
    }


    BigDecimal taxId;

    String taxAcctCode;

    BigDecimal taxPercentageRate;

    String statusText;

    String cancelFlagText;

    String taxFlag;

    String taxFlagText;

    String taxEvidenceType;

    String ledgerId;

    String erpSlipFlag;

    /* 발생전표 팝업 조회*/
    @QueryProjection
    public CardUseListDto ( BigDecimal taxId,String taxAcctCode,BigDecimal taxPercentageRate,String compCd,String usedNo,String cardNo,String slipNo, String abroadFlag,
                            String usedDt,String buyDt, String usedTime, String userId,String userNm,String deptCd,String deptNm,String irsNo,
                            String storeCd,String storeNm, String mccNm, String usedAmt,String usedCur,String usedForAmt,String cardComCd, String cardComNm, String originAmt,
                            String surtax,String serviceCharge, String status ,String statusText , String cancelFlag, String cancelFlagText, String employeeNo, String storeOwner,
                            String storeAddr, String desc1, String desc4, String mccCd, String apprNo, String taxType, String taxFlag, String taxFlagText,
                            String temp1, String cardType, String taxTypeCd){

        this.taxId = taxId;
        this.taxAcctCode = taxAcctCode;
        this.taxPercentageRate = taxPercentageRate;
        this.compCd = compCd;
        this.usedNo = usedNo;
        this.cardNo = cardNo;
        this.slipNo = slipNo;
        this.abroadFlag = abroadFlag;
        this.usedDt = usedDt;
        this.buyDt = buyDt;
        this.usedTime = usedTime;
        this.userId = userId;
        this.userNm = userNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.irsNo = irsNo;
        this.storeCd = storeCd;
        this.storeNm = storeNm;
        this.mccNm = mccNm;
        this.usedAmt = usedAmt;
        this.usedCur = usedCur;
        this.usedForAmt = usedForAmt;
        this.cardComCd = cardComCd;
        this.cardComNm = cardComNm;
        this.originAmt = originAmt;
        this.surtax = surtax;
        this.serviceCharge = serviceCharge;
        this.status = status;
        this.statusText = statusText;
        this.cancelFlag = cancelFlag;
        this.cancelFlagText = cancelFlagText;
        this.employeeNo = employeeNo;
        this.storeOwner = storeOwner;
        this.storeAddr = storeAddr;
        this.desc1 = desc1;
        this.desc4 = desc4;
        this.mccCd = mccCd;
        this.apprNo = apprNo;
        this.taxType = taxType;
        this.taxFlag = taxFlag;
        this.taxFlagText = taxFlagText;
        this.temp1 = temp1;
        this.cardType = cardType;
        this.taxTypeCd = taxTypeCd;
    }

    // 카드사용내역 팝업 (Custom)_
    public CardUseListDto(String compCd, String usedNo, String cardNo, String slipNo, String abroadFlag, String usedDt
            , String buyDt, String usedTime, String userId, String userNm, String deptCd, String deptNm, String irsNo
            , String storeCd, String storeNm, String mccNm, String usedAmt, String usedCur, String usedForAmt
            , String cardComCd, String cardComNm, String originAmt, String surtax, String serviceCharge, String status, String statusText
            , String cancelFlag, String cancelFlagText, String employeeNo, String storeOwner, String storeAddr, String desc1, String desc4
            , String mccCd, String apprNo, String taxType, String taxFlag, String taxFlagText, String temp1, String cardType, String dtCase
            , BigDecimal taxId, String taxCode, String taxAcctCode, BigDecimal taxPercentageRate) {
        this.compCd = compCd;
        this.usedNo = usedNo;
        this.cardNo = cardNo;
        this.slipNo = slipNo;
        this.abroadFlag = abroadFlag;
        this.usedDt = usedDt;
        this.buyDt = buyDt;
        this.usedTime = usedTime;
        this.userId = userId;
        this.userNm = userNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.irsNo = irsNo;
        this.storeCd = storeCd;
        this.storeNm = storeNm;
        this.mccNm = mccNm;
        this.usedAmt = usedAmt;
        this.usedForAmt = usedForAmt;
        this.usedCur = usedCur;
        this.cardComCd = cardComCd;
        this.cardComNm = cardComNm;
        this.originAmt = originAmt;
        this.surtax = surtax;
        this.serviceCharge = serviceCharge;
        this.status = status;
        this.statusText = statusText;
        this.cancelFlag = cancelFlag;
        this.cancelFlagText = cancelFlagText;
        this.employeeNo = employeeNo;
        this.storeOwner = storeOwner;
        this.storeAddr = storeAddr;
        this.desc1 = desc1;
        this.desc4 = desc4;
        this.mccCd = mccCd;
        this.apprNo = apprNo;
        this.taxType = taxType;
        this.taxFlag = taxFlag;
        this.taxFlagText = taxFlagText;
        this.temp1 = temp1;
        this.cardType = cardType;
        this.dtCase = dtCase;
        this.taxId = taxId;
        this.taxCode = taxCode;
        this.taxAcctCode = taxAcctCode;
        this.taxPercentageRate = taxPercentageRate;
    }

    public CardUseListDto(String storeNm, String usedDt, String usedAmt, String status) {
        this.storeNm = storeNm;
        this.usedDt = usedDt;
        this.usedAmt = usedAmt;
        this.status = status;
    }

    /* 법인카드 사용정보  (as-is 신용카드전표) */
    @QueryProjection
    public CardUseListDto (String cardNo, String usedDt, String cardComNm, String originAmt, String surtax,String serviceCharge,
                            String usedAmt, String apprNo, String storeNm, String storeAddr, String storeCd, String mccNm,
                            String irsNo, String storeOwner, String userNm){
        this.cardNo = cardNo;
        this.usedDt = usedDt;
        this.cardComNm = cardComNm;
        this.originAmt = originAmt;
        this.surtax = surtax;
        this.serviceCharge = serviceCharge;
        this.usedAmt = usedAmt;
        this.apprNo = apprNo;
        this.storeNm = storeNm;
        this.storeAddr = storeAddr;
        this.storeCd = storeCd;
        this.mccNm = mccNm;
        this.irsNo = irsNo;
        this.storeOwner = storeOwner;
        this.userNm = userNm;
    }
}
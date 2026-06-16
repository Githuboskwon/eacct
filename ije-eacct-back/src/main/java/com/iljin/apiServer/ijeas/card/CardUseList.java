package com.iljin.apiServer.ijeas.card;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "TB_CARD_USE_LIST")
@IdClass(CardUseListKey.class)
@NoArgsConstructor
public class CardUseList extends BaseEntity {

    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "USED_NO")
    String usedNo;

    @Column(name = "CARD_NO")
    String cardNo;

    @Column(name = "APPR_NO")
    String apprNo;

    @Column(name = "CARD_TYPE")
    String cardType;

    @Column(name = "SLIP_NO")
    String slipNo;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "SLIP_LINE_ID")
    BigDecimal slipLineId;

    @Column(name = "TAX_TYPE_CD")
    String taxTypeCd;

    @Column(name = "STATUS")
    String status;

    @Column(name = "CANCEL_FLAG")
    String cancelFlag;

    @Column(name = "USED_ORG_NO")
    String usedOrgNo;

    @Column(name = "EMPLOYEE_NO")
    String employeeNo;

    @Column(name = "USED_DT")
    String usedDt;

    @Column(name = "USED_TIME")
    String usedTime;

    @Column(name = "USER_ID")
    String userId;

    @Column(name = "USER_NM")
    String userNm;

    @Column(name = "DEPT_CD")
    String deptCd;

    @Column(name = "DEPT_NM")
    String deptNm;

    @Column(name = "USED_AMT")
    String usedAmt;

    @Column(name = "ORIGIN_AMT")
    String originAmt;

    @Column(name = "SURTAX")
    String surtax;

    @Column(name = "SERVICE_CHARGE")
    String serviceCharge;

    @Column(name = "USED_FOR_AMT")
    String usedForAmt;

    @Column(name = "USED_CUR")
    String usedCur;

    @Column(name = "CARD_COM_CD")
    String cardComCd;

    @Column(name = "CARD_COM_NM")
    String cardComNm;

    @Column(name = "IRS_NO")
    String irsNo;

    @Column(name = "STORE_CD")
    String storeCd;

    @Column(name = "STORE_NM")
    String storeNm;

    @Column(name = "ABROAD_FLAG")
    String abroadFlag;

    @Column(name = "MCC_CD")
    String mccCd;

    @Column(name = "MCC_NM")
    String mccNm;

    @Column(name = "TAX_TYPE")
    String taxType;

    @Column(name = "STORE_OWNER")
    String storeOwner;

    @Column(name = "STORE_ADDR")
    String storeAddr;

    @Column(name = "REAL_STORE_CD")
    String realStoreCd;

    @Column(name = "REAL_STORE_NM")
    String realStoreNm;

    @Column(name = "AMT_AMOUNT_ORI")
    String amtAmountOri;

    @Column(name = "VAT_AMOUNT_ORI")
    String vatAmountOri;

    @Column(name = "PROJECT_NO")
    String projectNo;

    @Column(name = "DESC1")
    String desc1;

    @Column(name = "DESC2")
    String desc2;

    @Column(name = "DESC3")
    String desc3;

    @Column(name = "DESC4")
    String desc4;

    @Column(name = "TEMP1")
    String temp1;

    @Column(name = "TEMP2")
    String temp2;

    @Column(name = "TEMP3")
    String temp3;

    @Column(name = "TEMP4")
    String temp4;

    @Column(name = "TEMP5")
    String temp5;

    @Column(name = "BUY_DT")
    String buyDt;

    @Column(name = "REMARK")
    String remark;

    @Builder
    public CardUseList ( String compCd, String usedNo, String cardNo, String apprNo, String cardType, String slipNo, BigDecimal slipHeaderId,
                         BigDecimal slipLineId, String taxTypeCd, String status, String cancelFlag, String usedOrgNo, String employeeNo, String usedDt,
                         String usedTime, String userId, String userNm, String deptCd, String deptNm, String usedAmt, String originAmt, String surtax,
                         String serviceCharge, String usedForAmt, String usedCur, String cardComCd, String cardComNm, String irsNo, String storeCd,
                         String storeNm, String abroadFlag, String mccCd, String mccNm, String taxType, String storeOwner, String storeAddr,String realStoreCd,
                         String realStoreNm, String amtAmountOri, String vatAmountOri, String projectNo, String desc1, String desc2, String desc3,String desc4,
                         String temp1, String temp2, String temp3, String temp4, String temp5, String buyDt, String remark){

        this.compCd = compCd;
        this.usedNo = usedNo;
        this.cardNo = cardNo;
        this.apprNo = apprNo;
        this.cardType = cardType;
        this.slipNo = slipNo;
        this.slipHeaderId = slipHeaderId;
        this.slipLineId = slipLineId;
        this.taxTypeCd = taxTypeCd;
        this.status = status;
        this.cancelFlag = cancelFlag;
        this.usedOrgNo = usedOrgNo;
        this.employeeNo = employeeNo;
        this.usedDt = usedDt;
        this.usedTime = usedTime;
        this.userId = userId;
        this.userNm = userNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.usedAmt = usedAmt;
        this.originAmt = originAmt;
        this.surtax = surtax;
        this.serviceCharge = serviceCharge;
        this.usedForAmt = usedForAmt;
        this.usedCur = usedCur;
        this.cardComCd = cardComCd;
        this.cardComNm = cardComNm;
        this.irsNo = irsNo;
        this.storeCd = storeCd;
        this.storeNm = storeNm;
        this.abroadFlag = abroadFlag;
        this.mccCd = mccCd;
        this.mccNm = mccNm;
        this.taxType = taxType;
        this.storeOwner = storeOwner;
        this.storeAddr = storeAddr;
        this.realStoreCd = realStoreCd;
        this.realStoreNm = realStoreNm;
        this.amtAmountOri = amtAmountOri;
        this.vatAmountOri = vatAmountOri;
        this.projectNo = projectNo;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.desc4 = desc4;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
        this.buyDt = buyDt;
        this.remark = remark;
    }

    public void updateStatus07(){
        this.status =  "07";
        this.slipNo =  "";
    }

    public void updateSlipInfo(String slipNo, String status, BigDecimal slipHeaderId, BigDecimal slipLineId) {
        this.slipNo = slipNo;
        this.status = status;
        this.slipHeaderId = slipHeaderId;
        this.slipLineId = slipLineId;
    }

    public void cancelStatus01(){
        this.status =  "01";
        this.slipNo =  "";
    }

    public void resetSlipInfo() {
        this.slipNo = "";
        this.status = "01";
        this.slipHeaderId = null;
    }

    public void resetStatus(){
        this.slipNo = "";
        this.status =  "01";
        this.slipHeaderId = null;
        this.slipLineId = null;
    }

    public void changeStatus(String status){
        this.status = status;
    }

    public void updateCardEmpInfo(String userId, String userNm, String deptCd, String deptNm) {
        this.userId = userId;
        this.userNm = userNm;
        this.employeeNo = userId;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
    }
}
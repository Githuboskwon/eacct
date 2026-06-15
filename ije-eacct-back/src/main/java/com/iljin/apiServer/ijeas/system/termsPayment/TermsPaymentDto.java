package com.iljin.apiServer.ijeas.system.termsPayment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class TermsPaymentDto {

    String compCd;

    Integer termId;

    String deptCd;

    String deptNm;

    String trxType;

    String name;

    String description;

    String notesFlag;

    String maturityDays;

    String dueDateCalcFlag;

    String currencyType;

    String paymentMethod;

    String vendorAcctCheck;

    String spEnabledFlag;

    String trxTypeCd;

    String trxTypeNm;

    String eaEnabledFlag;

    String ptEnabledFlag;

    String dtChangeFlag;

    String oriDeptCd;

    String oriDeptNm;

    String chgNm;

    String chgId;

    LocalDateTime chgDtm;

    String insertYn;

    /*
     * 조회 클릭 event
     * 결제조건 등록관리 리스트 조회
     * */
    @QueryProjection
    public TermsPaymentDto(String trxType,
                           Integer termId,
                           String name,
                           String description,
                           String compCd,
                           String notesFlag,
                           String maturityDays,
                           String dueDateCalcFlag,
                           String currencyType,
                           String paymentMethod,
                           String vendorAcctCheck,
                           String trxTypeCd,
                           String trxTypeNm,
                           String deptCd,
                           String deptNm,
                           String spEnabledFlag,
                           String eaEnabledFlag,
                           String ptEnabledFlag,
                           String dtChangeFlag,
                           String oriDeptCd,
                           String oriDeptNm,
                           String chgId,
                           String chgNm,
                           LocalDateTime chgDtm)
    {
        this.trxType = trxType;
        this.termId = termId;
        this.name = name;
        this.description = description;
        this.compCd = compCd;
        this.notesFlag = notesFlag;
        this.maturityDays = maturityDays;
        this.dueDateCalcFlag = dueDateCalcFlag;
        this.currencyType = currencyType;
        this.paymentMethod = paymentMethod;
        this.vendorAcctCheck = vendorAcctCheck;
        this.trxTypeCd = trxTypeCd;
        this.trxTypeNm = trxTypeNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.spEnabledFlag = spEnabledFlag;
        this.eaEnabledFlag = eaEnabledFlag;
        this.ptEnabledFlag = ptEnabledFlag;
        this.dtChangeFlag = dtChangeFlag;
        this.oriDeptCd = oriDeptCd;
        this.oriDeptNm = oriDeptNm;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }

}

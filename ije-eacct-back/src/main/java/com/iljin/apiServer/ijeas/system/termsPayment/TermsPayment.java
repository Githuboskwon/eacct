package com.iljin.apiServer.ijeas.system.termsPayment;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "TB_STERMS")
@IdClass(TermsPaymentKey.class)
@NoArgsConstructor
public class TermsPayment extends BaseEntity {

    // 회사코드
    @Id
    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "TERM_ID")
    Integer termId;

    @Id
    @Column(name = "DEPT_CD")
    String deptCd;

    @Column(name = "DEPT_NM")
    String deptNm;

    @Column(name = "TRX_TYPE")
    String trxType;

    @Column(name = "NAME")
    String name;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "NOTES_FLAG")
    String notesFlag;

    @Column(name = "MATURITY_DAYS")
    String maturityDays;

    @Column(name = "DUE_DATE_CALC_FLAG")
    String dueDateCalcFlag;

    @Column(name = "CURRENCY_TYPE")
    String currencyType;

    @Column(name = "PAYMENT_METHOD")
    String paymentMethod;

    @Column(name = "VENDOR_ACCT_CHECK")
    String vendorAcctCheck;

    @Column(name = "SP_ENABLED_FLAG")
    String spEnabledFlag;

    @Column(name = "TRX_TYPE_CD")
    String trxTypeCd;

    @Column(name = "TRX_TYPE_NM")
    String trxTypeNm;

    @Column(name = "EA_ENABLED_FLAG")
    String eaEnabledFlag;

    @Column(name = "PT_ENABLED_FLAG")
    String ptEnabledFlag;

    @Column(name = "DT_CHANGE_FLAG")
    String dtChangeFlag;



    @Builder
    public TermsPayment(String trxType,
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
                        String dtChangeFlag
    ) {
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
    }
//
//    public void update(ConfirmDto confirmDto){
//        this.confirmStartAmt = confirmDto.getConfirmStartAmt();
//        this.confirmEndAmt = confirmDto.getConfirmEndAmt();
//        this.remark = confirmDto.getRemark();
//    }

}

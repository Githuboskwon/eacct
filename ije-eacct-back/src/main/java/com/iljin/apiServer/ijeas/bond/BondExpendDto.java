package com.iljin.apiServer.ijeas.bond;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BondExpendDto implements Serializable {

    private static final long serialVersionUID = 8455478171490500829L;
    
    String amendSeq;
    String deptNm;
    String benCountry;
    BigDecimal budget;
    String bondCurrencyCd;
    String currencyCd;
    String regId;
    String slipType;
    BigDecimal slipHeaderId;
    BigDecimal approvalGroupId;
    String slipStatus;
    String customerNm;
    BigDecimal guaranteeAmt;
    String headerText;
    String intBankNm;
    String localBankNm;
    String maturityDtI;
    String maturityDtF;
    BigDecimal rateI;
    BigDecimal rateF;
    String usedAmtI;
    String usedAmtF;
    String openingDt;
    String projectNm;
    String refNo;
    String slipNo;
    String splitEtcYn;
    String bondCd;
    String type;


    String deptCd;
    String localMaturityDt;
    String overseasMaturityDt;
    String benCountryCd;
    String customerId;
    String projectId;
    String compCd;
    BigDecimal count;
    BigDecimal currencyAmt;


    /**
     * BOND 경비 조회
     * 조회 생성자
     * */
    public BondExpendDto(String amendSeq, String deptNm, String benCountry, BigDecimal budget,
        String bondCurrencyCd, String currencyCd, String regId, String slipType,
        BigDecimal slipHeaderId, BigDecimal approvalGroupId, String slipStatus, String customerNm,
        BigDecimal guaranteeAmt, String headerText, String intBankNm, String localBankNm,
        String maturityDtI, String maturityDtF, BigDecimal rateI, BigDecimal rateF,
        String usedAmtI,
        String usedAmtF, String openingDt, String projectNm, String refNo, String slipNo,
        String splitEtcYn, String bondCd, String type) {
        this.amendSeq = amendSeq;
        this.deptNm = deptNm;
        this.benCountry = benCountry;
        this.budget = budget;
        this.bondCurrencyCd = bondCurrencyCd;
        this.currencyCd = currencyCd;
        this.regId = regId;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.approvalGroupId = approvalGroupId;
        this.slipStatus = slipStatus;
        this.customerNm = customerNm;
        this.guaranteeAmt = guaranteeAmt;
        this.headerText = headerText;
        this.intBankNm = intBankNm;
        this.localBankNm = localBankNm;
        this.maturityDtI = maturityDtI;
        this.maturityDtF = maturityDtF;
        this.rateI = rateI;
        this.rateF = rateF;
        this.usedAmtI = usedAmtI;
        this.usedAmtF = usedAmtF;
        this.openingDt = openingDt;
        this.projectNm = projectNm;
        this.refNo = refNo;
        this.slipNo = slipNo;
        this.splitEtcYn = splitEtcYn;
        this.bondCd = bondCd;
        this.type = type;
    }

    /**
     * BOND RefNo 조회
     * */
    public BondExpendDto(String refNo, String customerNm, String openingDt, String benCountryCd,
        String benCountry, String customerId, String intBankNm, String localBankNm, BigDecimal budget,
        String projectNm, String projectId, String currencyCd, BigDecimal currencyAmt,
        String amendSeq, BigDecimal count, String bondCd) {
        this.refNo = refNo;
        this.customerNm = customerNm;
        this.openingDt = openingDt;
        this.benCountryCd = benCountryCd;
        this.benCountry = benCountry;
        this.customerId = customerId;
        this.intBankNm = intBankNm;
        this.localBankNm = localBankNm;
        this.budget = budget;
        this.projectNm = projectNm;
        this.projectId = projectId;
        this.currencyCd = currencyCd;
        this.currencyAmt = currencyAmt;
        this.amendSeq = amendSeq;
        this.count = count;
        this.bondCd = bondCd;
    }
}

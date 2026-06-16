package com.iljin.apiServer.ijeas.bond;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@IdClass(BondHeaderKey.class)
@Table(name = "TB_BOND_HD")
@Entity
public class BondHeader extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;

    @Id
    @Column(name = "REF_NO")
    String refNo;

    @Id
    @Column(name = "TYPE")
    String type;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "BOND_CD")
    String bondCd;

    @Column(name = "AMEND_SEQ")
    Integer amendSeq;

    @Column(name = "BEN_COUNTRY")
    String benCountry;

    @Column(name = "BEN_COUNTRY_CD")
    String benCountryCd;

    @Column(name = "CUSTOMER_NM")
    String customerNm;

    @Column(name = "CUSTOMER_ID")
    String customerId;

    @Column(name = "PROJECT_NM")
    String projectNm;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "LOCAL_BANK_NM")
    String localBankNm;

    @Column(name = "LOCAL_BANK_ID")
    String localBankId;

    @Column(name = "INT_BANK_NM")
    String intBankNm;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "CURRENCY_AMT")
    BigDecimal currencyAmt;

    @Column(name = "BUDGET")
    BigDecimal budget;

    @Column(name = "OPENING_DT")
    LocalDateTime openingDt;

    @Column(name = "MATURITY_DT")
    LocalDateTime maturityDt;

    @Column(name = "RELEASE_DT")
    LocalDateTime releaseDt;

    @Column(name = "RATE")
    BigDecimal rate;

    @Column(name = "GUARANTEE_AMT")
    BigDecimal guaranteeAmt;

    @Column(name = "SPLIT_ETC_YN")
    String splitEtcYn;

    @Builder
    public BondHeader(String compCd, String slipNo, String refNo, String type, BigDecimal slipHeaderId,
        String bondCd, Integer amendSeq, String benCountry, String benCountryCd, String customerNm,
        String customerId, String projectNm, String projectId, String localBankNm,
        String localBankId,
        String intBankNm, String currencyCd, BigDecimal currencyAmt, BigDecimal budget,
        LocalDateTime openingDt, LocalDateTime maturityDt, LocalDateTime releaseDt, BigDecimal rate,
        BigDecimal guaranteeAmt, String splitEtcYn) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.refNo = refNo;
        this.type = type;
        this.slipHeaderId = slipHeaderId;
        this.bondCd = bondCd;
        this.amendSeq = amendSeq;
        this.benCountry = benCountry;
        this.benCountryCd = benCountryCd;
        this.customerNm = customerNm;
        this.customerId = customerId;
        this.projectNm = projectNm;
        this.projectId = projectId;
        this.localBankNm = localBankNm;
        this.localBankId = localBankId;
        this.intBankNm = intBankNm;
        this.currencyCd = currencyCd;
        this.currencyAmt = currencyAmt;
        this.budget = budget;
        this.openingDt = openingDt;
        this.maturityDt = maturityDt;
        this.releaseDt = releaseDt;
        this.rate = rate;
        this.guaranteeAmt = guaranteeAmt;
        this.splitEtcYn = splitEtcYn;
    }

    public static BondHeader from(SlipDto slipDto, BondMst bondMst) {
        BondHeaderDto bondHeaderDto = slipDto.getBondHeaderDto();
        return BondHeader.builder()
            .slipNo(slipDto.getSlipNo())
            .slipHeaderId(slipDto.getSlipHeaderId())
            .refNo(bondHeaderDto.getRefNo())
            .amendSeq(bondHeaderDto.getAmendSeq())
            .type(bondHeaderDto.getType())
            .maturityDt(bondHeaderDto.getMaturityDt())
            .rate(bondHeaderDto.getRate())
            .guaranteeAmt(bondHeaderDto.getGuaranteeAmt())
            .compCd(slipDto.getCompCd())
            .splitEtcYn(bondHeaderDto.getSplitEtcYn())
            .benCountry(bondMst.getBenCountry())
            .benCountryCd(bondMst.getBenCountryCd())
            .customerId(bondMst.getCustomerId())
            .customerNm(bondMst.getCustomerNm())
            .projectId(bondMst.getProjectId())
            .projectNm(bondMst.getProjectNm())
            .localBankId(bondMst.getLocalBankId())
            .localBankNm(bondMst.getLocalBankNm())
            .intBankNm(bondMst.getIntBankNm())
            .currencyCd(bondMst.getCurrencyCd())
            .currencyAmt(bondMst.getCurrencyAmt())
            .budget(bondMst.getBudget())
            .openingDt(bondMst.getOpeningDt())
            .releaseDt(bondMst.getOpeningDt())
            .bondCd(bondMst.getBondCd())
            .build();
    }

    public void reset() {
        this.bondCd = null;
        this.amendSeq = null;
        this.benCountry = null;
        this.benCountryCd = null;
        this.customerNm = null;
        this.customerId = null;
        this.projectNm = null;
        this.projectId = null;
        this.localBankNm = null;
        this.localBankId = null;
        this.intBankNm = null;
        this.currencyCd = null;
        this.currencyAmt = null;
        this.budget = null;
        this.openingDt = null;
        this.maturityDt = null;
        this.releaseDt = null;
        this.rate = null;
        this.guaranteeAmt = null;
        this.splitEtcYn = null;
    }

    public void addOneToAmendSeq() {
        this.amendSeq++;
    }

    public static BondHeader copy(String slipNo, BigDecimal slipHeaderId, BondHeader bondHeader) {
        return BondHeader.builder()
            .compCd(bondHeader.getCompCd())
            .slipNo(slipNo)
            .slipHeaderId(slipHeaderId)
            .refNo(bondHeader.getRefNo())
            .type(bondHeader.getType())
            .bondCd(bondHeader.getBondCd())
            .amendSeq(bondHeader.getAmendSeq())
            .benCountry(bondHeader.getBenCountry())
            .benCountryCd(bondHeader.getBenCountryCd())
            .customerNm(bondHeader.getCustomerNm())
            .customerId(bondHeader.getCustomerId())
            .projectNm(bondHeader.getProjectNm())
            .projectId(bondHeader.getProjectId())
            .localBankNm(bondHeader.getLocalBankNm())
            .localBankId(bondHeader.getLocalBankId())
            .intBankNm(bondHeader.getIntBankNm())
            .budget(bondHeader.getBudget())
            .openingDt(bondHeader.getOpeningDt())
            .maturityDt(bondHeader.getMaturityDt())
            .releaseDt(bondHeader.getReleaseDt())
            .rate(bondHeader.getRate())
            .guaranteeAmt(bondHeader.getGuaranteeAmt())
            .splitEtcYn(bondHeader.getSplitEtcYn())
            .build();
    }
}

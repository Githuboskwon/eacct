package com.iljin.apiServer.ijeas.bond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iljin.apiServer.core.audit.BaseEntity;
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
@IdClass(BondMstKey.class)
@Table(name = "TB_BOND_MASTER")
@Entity
public class BondMst extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "REF_NO", nullable = false)
    String refNo;

    @Column(name = "BOND_CD")
    String bondCd;

    @Column(name = "BEN_COUNTRY")
    String benCountry;

    @Column(name = "PROJECT_NM")
    String projectNm;

    @Column(name = "INT_BANK_NM")
    String intBankNm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "OPENING_DT")
    LocalDateTime openingDt;

    @Column(name = "BUDGET")
    BigDecimal budget;

    @Column(name = "RELEASE_DT")
    LocalDateTime releaseDt;

    @Column(name = "BEN_COUNTRY_CD")
    String benCountryCd;

    @Column(name = "CUSTOMER_NM")
    String customerNm;

    @Column(name = "CUSTOMER_ID")
    String customerId;

    @Column(name = "PROJECT_ID")
    String projectId;

    @Column(name = "LOCAL_BANK_NM")
    String localBankNm;

    @Column(name = "LOCAL_BANK_ID")
    String localBankId;

    @Column(name = "CURRENCY_AMT")
    BigDecimal currencyAmt;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Builder
    public BondMst(String compCd, String refNo, String bondCd, String benCountry, String projectNm,
        String intBankNm, LocalDateTime openingDt, BigDecimal budget, LocalDateTime releaseDt,
        String benCountryCd, String customerNm, String customerId, String projectId,
        String localBankNm,
        String localBankId, BigDecimal currencyAmt, String currencyCd) {
        this.compCd = compCd;
        this.refNo = refNo;
        this.bondCd = bondCd;
        this.benCountry = benCountry;
        this.projectNm = projectNm;
        this.intBankNm = intBankNm;
        this.openingDt = openingDt;
        this.budget = budget;
        this.releaseDt = releaseDt;
        this.benCountryCd = benCountryCd;
        this.customerNm = customerNm;
        this.customerId = customerId;
        this.projectId = projectId;
        this.localBankNm = localBankNm;
        this.localBankId = localBankId;
        this.currencyAmt = currencyAmt;
        this.currencyCd = currencyCd;
    }

    public BondMst update(BondMstDto dto) {
        this.bondCd = dto.getBondCd();
        this.benCountry = dto.getBenCountry();
        this.projectNm = dto.getProjectNm();
        this.intBankNm = dto.getIntBankNm();
        this.openingDt = dto.getOpeningDt();
        this.budget = dto.getBudget();
        this.releaseDt = dto.getReleaseDt();
        this.benCountryCd = dto.getBenCountryCd();
        this.customerNm = dto.getCustomerNm();
        this.customerId = dto.getCustomerId();
        this.projectId = dto.getProjectId();
        this.localBankNm = dto.getLocalBankNm();
        this.localBankId = dto.getLocalBankId();
        this.currencyAmt = dto.getCurrencyAmt();
        this.currencyCd = dto.getCurrencyCd();
        return this;
    }

    public void changeReleaseDt(LocalDateTime releaseDt) {
        this.releaseDt = releaseDt;
    }
}

package com.iljin.apiServer.ijeas.bond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BondHeaderDto implements Serializable {

    private static final long serialVersionUID = 380785811484870211L;

    String compCd;

    String slipNo;

    // 참조번호
    String refNo;

    // 국내수수료 LOCAL
    String type;

    // 전표헤더ID
    BigDecimal slipHeaderId;

    // BOND 종류
    String bondCd;

    // 개정이력 (시스템 입력)
    Integer amendSeq;

    // 수익자 국가
    String benCountry;

    // 수익자 국가 코드
    String benCountryCd;

    // 고객명
    String customerNm;

    // 고객 ID VENDOR_SITE_ID
    String customerId;

    // 프로젝트명
    String projectNm;

    // 프로젝트ID
    String projectId;

    // 국내은행명
    String localBankNm;

    // 국내은행 ID
    String localBankId;

    // 해외 은행명
    String intBankNm;

    // 통화코드
    String currencyCd;

    // 통화금액
    String currencyNm;

    // 예산 (단위:천원)
    BigDecimal budget;

    // 개설일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime openingDt;

    // 만기일(사용자 입력)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime maturityDt;

    // 릴리즈 일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime releaseDt;

    // 요율 (0~5%)
    BigDecimal rate;

    // 보증금액
    BigDecimal guaranteeAmt;

    // 분할, 기타여부
    String splitEtcYn;


    @QueryProjection
    public BondHeaderDto(String refNo, String type, String splitEtcYn) {
        this.refNo = refNo;
        this.type = type;
        this.splitEtcYn = splitEtcYn;
    }


    String bondGubun;
    BigDecimal amendCount;

    @QueryProjection
    public BondHeaderDto(String splitEtcYn, String bondGubun, String type, String benCountryCd, String benCountry, String refNo, String projectNm,
                         String projectId, String customerNm, String customerId, LocalDateTime openingDt, String currencyCd, String currencyNm, BigDecimal guaranteeAmt,
                         LocalDateTime maturityDt, BigDecimal rate, String localBankNm, String localBankId, String intBankNm, BigDecimal budget, Integer amendSeq,
                         Long amendCount, LocalDateTime releaseDt) {
        this.splitEtcYn = splitEtcYn;
        this.bondGubun = bondGubun;
        this.type = type;
        this.benCountryCd = benCountryCd;
        this.benCountry = benCountry;
        this.refNo = refNo;
        this.projectNm = projectNm;
        this.projectId = projectId;
        this.customerNm = customerNm;
        this.customerId = customerId;
        this.openingDt = openingDt;
        this.currencyCd = currencyCd;
        this.currencyNm = currencyNm;
        this.guaranteeAmt = guaranteeAmt;
        this.maturityDt = maturityDt;
        this.rate = rate;
        this.localBankNm = localBankNm;
        this.localBankId = localBankId;
        this.intBankNm = intBankNm;
        this.budget = budget;
        this.amendSeq = amendSeq;
        this.amendCount = BigDecimal.valueOf(amendCount);
        this.releaseDt = releaseDt;
    }
}

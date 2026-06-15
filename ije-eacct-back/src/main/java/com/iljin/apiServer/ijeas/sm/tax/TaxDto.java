package com.iljin.apiServer.ijeas.sm.tax;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class TaxDto {

    // 세금코드
    String taxCd;
    // 회사코드
    String compCd;
    // 세금코드명
    String taxNm;
    // 세율
    BigDecimal taxRt;
    // 사용여부
    String useYn;
    // 정렬순서
    BigDecimal orderSeq;
    // 증빙유형코드
    String evdTypeCd;
    // 증빙유형명
    String evdTypeNm;
    // 비고1
    String ref1;
    // 비고2
    String ref2;
    // 비고3
    String ref3;

    /*
    * EA-06-03 세금코드관리
    * 리스트 조회
    * */

    public TaxDto(String compCd, String taxCd, String taxNm, BigDecimal taxRt, String useYn, BigDecimal orderSeq, String evdTypeCd, String evdTypeNm, String ref1, String ref2, String ref3) {
        this.compCd = compCd;
        this.taxCd = taxCd;
        this.taxNm = taxNm;
        this.taxRt = taxRt;
        this.useYn = useYn;
        this.orderSeq = orderSeq;
        this.evdTypeCd = evdTypeCd;
        this.evdTypeNm = evdTypeNm;
        this.ref1 = ref1;
        this.ref2 = ref2;
        this.ref3 = ref3;
    }
}

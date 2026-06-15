package com.iljin.apiServer.ijeas.slip.tax;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SearchTaxDtiNtsDto implements Serializable {

    private static final long serialVersionUID = 8952303724191741403L;

    //slipHeaderId
    BigDecimal searchSlipHeaderId;

    //작성시작일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime searchFrom;

    //작성종료일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime searchTo;

    //사업자번호명
    String searchSuId;

    //승인번호
    String searchIssueId;

    //email
    String searchEmail;

    //공급자담당자명
    String searchIpPersname;

    String searchTaxEvidenceType;

    String searchTaxSmartbillNo;

    String searchLineAttribute6;

    String searchVendor;

    String searchStatus;
}

package com.iljin.apiServer.ijeas.es.erpViews;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpSlipRequestDto implements Serializable {

    private static final long serialVersionUID = -6735808355971209735L;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime searchFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime searchTo;

    String compCd;

    String transferredBy;

    String vendorNm;

    String vendorCd;

    String bankAcctNum;

    BigDecimal checkNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime futurePayDueDt;

    String paymentMethod;

    String currencyCd;

    String currencyType;

    String slipNo;

    String slipType;

    BigDecimal checkId;

    String userId;

    String eslipTransferBatchId;

    String erpRegId;

    String dealNum;

    String dealType;

    String productType;

    String trxTypeCd;

    String batchNm;

    String sourceNm;

    String fileYn;

    String jiniYn;

    String searchMonth;

    String searchDate;

    String searchYn;

    String erpReflection;

    String transferType;

    String reportArraign;

    String approvalArraign;

    String sealArraign;

    String vendorSiteId;

    String searchCd;

    String searchNm;

    String taxRateCd;

    BigDecimal slipHeaderId;

    String deptCd;

    String headerNm;

    String glSlipTypeNm;

    String div;

    String remark;

    String certAmendYn;

}

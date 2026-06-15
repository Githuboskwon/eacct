package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionDt;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ErpCollectionSlipDto implements Serializable {

    private static final long serialVersionUID = 5281003613811924734L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal slipHeaderId;

    String xtrSlipType;

    LocalDateTime glDt;

    String currencyCd;

    BigDecimal functionalAmtDr;

    BigDecimal functionalAmtCr;

    String remark;

    String deptCd;

    String deptNm;

    String chgId;

    String chgNm;

    Long uFileCnt;

    Long jiniCnt;


    List<SpTrCollectionDt> dtList;

    String docTitle;

    // list
    @QueryProjection
    public ErpCollectionSlipDto(String compCd, String slipNo, String slipType,
                            BigDecimal slipHeaderId, String xtrSlipType, LocalDateTime glDt,
                            String currencyCd, BigDecimal functionalAmtDr, BigDecimal functionalAmtCr,
                            String remark, Long uFileCnt, String docTitle){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.glDt = glDt;
        this.currencyCd = currencyCd;
        this.functionalAmtDr = functionalAmtDr;
        this.functionalAmtCr = functionalAmtCr;
        this.remark = remark;
        this.uFileCnt = uFileCnt;
        this.docTitle = docTitle;
    }

    String status;

    @QueryProjection
    public ErpCollectionSlipDto(String compCd, String slipNo, String slipType,
                                BigDecimal slipHeaderId, String xtrSlipType, LocalDateTime glDt,
                                String currencyCd, BigDecimal functionalAmtDr, BigDecimal functionalAmtCr, String remark,
                                String chgId ,String chgNm, String deptCd, String deptNm,
                                String status,Long uFileCnt, Long jiniCnt){
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.slipHeaderId = slipHeaderId;
        this.xtrSlipType = xtrSlipType;
        this.glDt = glDt;
        this.currencyCd = currencyCd;
        this.functionalAmtDr = functionalAmtDr;
        this.functionalAmtCr = functionalAmtCr;
        this.remark = remark;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.status = status;
        this.uFileCnt = uFileCnt;
        this.jiniCnt = jiniCnt;
    }

}

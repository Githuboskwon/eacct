package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeaders;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Immutable
@IdClass(SpTrCollectionHdKey.class)
@Table(name = "TB_TR_COLLECTION_HD")
@Entity
public class SpTrCollectionHd extends BaseEntity {

    @Id
    @Column(name = "COMP_CD" , nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO" , nullable = false)
    String slipNo;

    @Id
    @Column(name = "SLIP_TYPE" , nullable = false)
    String slipType;

    @Column(name = "SLIP_HEADER_ID")
    BigDecimal slipHeaderId;

    @Column(name = "XTR_SLIP_TYPE")
    String xtrSlipType;

    @Column(name = "GL_DT")
    LocalDateTime glDt;

    @Column(name = "CURRENCY_CD")
    String currencyCd;

    @Column(name = "FUNCTIONAL_AMT_DR")
    BigDecimal functionalAmtDr;

    @Column(name = "FUNCTIONAL_AMT_CR")
    BigDecimal functionalAmtCr;

    @Column(name = "REMARK")
    String remark;


    @Builder
    public SpTrCollectionHd(String compCd,
                            String slipNo,
                            String slipType,
                            BigDecimal slipHeaderId,
                            String xtrSlipType,
                            LocalDateTime glDt,
                            String currencyCd,
                            BigDecimal functionalAmtDr,
                            BigDecimal functionalAmtCr,
                            String remark){
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
    }

    public static SpTrCollectionHd from(ErpTrFundTrnHeaders erpTrFundTrnHeaders, String slipNo, BigDecimal slipHeaderId){
        return SpTrCollectionHd.builder()
                .compCd(String.valueOf(erpTrFundTrnHeaders.getOrgId()))
                .slipNo(slipNo)
                .slipHeaderId(slipHeaderId)
                .slipType(SlipType.CLCT.getCode())
                .xtrSlipType(erpTrFundTrnHeaders.getXtrSlipType())
                .glDt(erpTrFundTrnHeaders.getGlDate())
                .currencyCd(erpTrFundTrnHeaders.getCurrencyCode())
                .functionalAmtDr(erpTrFundTrnHeaders.getFunctionalAmountDr())
                .functionalAmtCr(erpTrFundTrnHeaders.getFunctionalAmountCr())
                .remark(erpTrFundTrnHeaders.getRemark())
                .build();
    }

}

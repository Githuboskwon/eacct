package com.iljin.apiServer.ijeas.es.item;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApPaymentsItemDto implements Serializable {

    private static final long serialVersionUID = -8134358907021573911L;

    String compCd;

    String slipNo;

    String slipType;

    BigDecimal checkId;

    Long aeHeaderId;

    Long aeLineId;

    Long aeLineNo;

    String segment3;

    String segment4;

    String segment5;

    String segment3Desc;

    String segment4Desc;

    String segment5Desc;

    Double enteredDr;

    Double enteredCr;

    Double accountedDr;

    Double accountedCr;

    String description;

    @Builder
    @QueryProjection
    public ApPaymentsItemDto(String compCd, String slipNo, String slipType, BigDecimal checkId,
        Long aeHeaderId, Long aeLineId, Long aeLineNo, String segment3, String segment4,
        String segment5, String segment3Desc, String segment4Desc, String segment5Desc,
        Double enteredDr, Double enteredCr, Double accountedDr, Double accountedCr,
        String description) {
        this.compCd = compCd;
        this.slipNo = slipNo;
        this.slipType = slipType;
        this.checkId = checkId;
        this.aeHeaderId = aeHeaderId;
        this.aeLineId = aeLineId;
        this.aeLineNo = aeLineNo;
        this.segment3 = segment3;
        this.segment4 = segment4;
        this.segment5 = segment5;
        this.segment3Desc = segment3Desc;
        this.segment4Desc = segment4Desc;
        this.segment5Desc = segment5Desc;
        this.enteredDr = enteredDr;
        this.enteredCr = enteredCr;
        this.accountedDr = accountedDr;
        this.accountedCr = accountedCr;
        this.description = description;
    }

    public static ApPaymentsItemDto from(ApPaymentsItem apPaymentsItem) {
        return ApPaymentsItemDto.builder()
            .compCd(apPaymentsItem.getCompCd())
            .slipNo(apPaymentsItem.getSlipNo())
            .slipType(apPaymentsItem.getSlipType())
            .checkId(apPaymentsItem.getCheckId())
            .aeHeaderId(apPaymentsItem.getAeHeaderId())
            .aeLineId(apPaymentsItem.getAeLineId())
            .aeLineNo(apPaymentsItem.getAeLineNo())
            .segment3(apPaymentsItem.getSegment3())
            .segment4(apPaymentsItem.getSegment4())
            .segment5(apPaymentsItem.getSegment5())
            .segment3Desc(apPaymentsItem.getSegment3Desc())
            .segment4Desc(apPaymentsItem.getSegment4Desc())
            .segment5Desc(apPaymentsItem.getSegment5Desc())
            .enteredDr(apPaymentsItem.getEnteredDr())
            .enteredCr(apPaymentsItem.getEnteredCr())
            .accountedDr(apPaymentsItem.getAccountedDr())
            .accountedCr(apPaymentsItem.getAccountedCr())
            .description(apPaymentsItem.getDescription())
            .build();
    }
}

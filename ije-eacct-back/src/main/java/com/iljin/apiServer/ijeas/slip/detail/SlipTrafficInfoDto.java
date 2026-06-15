package com.iljin.apiServer.ijeas.slip.detail;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SlipTrafficInfoDto {


    BigDecimal slipLineId;

    String slipSeq;

    String bungaeNo;

    String amt;

    String chitGubun;

    String usedNo;

    String remark;

    String etcType;

    public SlipTrafficInfoDto(String slipSeq, BigDecimal slipLineId, String bungaeNo, String amt,
        String cardCashType, String usedNo, String remark, String etcType) {
        this.slipSeq = slipSeq;
        this.slipLineId = slipLineId;
        this.bungaeNo = bungaeNo;
        this.amt = amt;
        this.chitGubun = cardCashType;
        this.usedNo = usedNo;
        this.remark = remark;
        this.etcType = etcType;
    }
}

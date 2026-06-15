package com.iljin.apiServer.ijeas.slip.detail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlipTrafficDtDto implements Serializable {

    private static final long serialVersionUID = -5385200749708339633L;

    String compCd;

    String slipNo;

    BigDecimal slipHeaderId;

    String slipSeq;

    String usedDt;

    String fromArea;

    String fromAreaText;

    String toArea;

    String toAreaText;

    String trafficType;

    String distance;

    String oilPrice;

    String mileage;

    String oilAmt;

    String oilBungae;

    String etcType1;

    String etcAmt1;

    String etcBungae1;

    String etcChitCd1;

    String etcUsedNo1;

    String etcType2;

    String etcAmt2;

    String etcBungae2;

    String etcChitCd2;

    String etcUsedNo2;

    String etcType3;

    String etcAmt3;

    String etcBungae3;

    String etcChitCd3;

    String etcUsedNo3;

    String sumAmt;

    String oilAmtType;

    BigDecimal confDist;

    LocalDateTime paymentDt;

    String remark;
}

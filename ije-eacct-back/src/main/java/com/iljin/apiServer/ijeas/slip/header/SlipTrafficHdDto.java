package com.iljin.apiServer.ijeas.slip.header;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlipTrafficHdDto implements Serializable {

    private static final long serialVersionUID = 8529928677431661959L;

    String compCd;

    String slipNo;

    BigDecimal slipHeaderId;

    // 외근출장지역. 외근출장지역
    String tripPlace;

    // 출장시작일
    String tripFromDt;

    // 출장종료일
    String tripToDt;

    // 출장목적
    String tripObj;

    // 프로젝트번호
    String projectNo;

    // 출장코드
    String tripCd;

    // 임시필드1
    String temp1;

    // 자가운전 보조금 해당여부
    String temp2;

    // 외근신청서 작성여부
    String temp3;

    String temp4;

    String temp5;
}

package com.iljin.apiServer.ijeas.system.expend;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "TB_SLIP_EXPEND_MONEY")
@Getter
@IdClass(SlipExpendKey.class)
public class SlipExpend extends BaseEntity {

    //회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SLIP_NO", nullable = false)
    String slipNo;


    @Id
    @Column(name = "SLIP_HEADER_ID", nullable = false)
    BigDecimal slipHeaderId;

    @Column(name = "EXPEND_CD")
    String expendCd;

    @Column(name = "EXPEND_RECEIVE_NM")
    String expendReceiveNm;

    @Column(name = "EXPEND_RELATION")
    String expendRelation;

    @Column(name = "EXPEND_DT")
    String expendDt;

    //경조금(원)
    @Column(name = "EXPEND_AMT")
    String expendAmt;

    //화환구분
    @Column(name = "WREATH_YN")
    String wreathYn;

    //상조용품구분
    @Column(name = "MUTUAL_YN")
    String mutualYn;

    @Column(name = "HOLIDAY")
    String holiday;

    @Column(name = "TEMP1")
    String temp1;

    @Column(name = "TEMP2")
    String temp2;

    @Column(name = "TEMP3")
    String temp3;

    @Column(name = "TEMP4")
    String temp4;

    @Column(name = "TEMP5")
    String temp5;


    @Builder
    public SlipExpend(String compCd, String slipNo, BigDecimal slipHeaderId, String expendCd,
                      String expendReceiveNm, String expendRelation, String expendDt, String expendAmt,
                      String wreathYn, String mutualYn, String holiday, String temp1,
                      String temp2, String temp3, String temp4, String temp5)
    {
        this.compCd = compCd ;
        this.slipNo = slipNo ;
        this.slipHeaderId = slipHeaderId ;
        this.expendCd = expendCd ;
        this.expendReceiveNm = expendReceiveNm;
        this.expendRelation = expendRelation ;
        this.expendDt = expendDt ;
        this.expendAmt = expendAmt ;
        this.wreathYn = wreathYn ;
        this.mutualYn = mutualYn ;
        this.holiday = holiday ;
        this.temp1 = temp1 ;
        this.temp2 = temp2 ;
        this.temp3 = temp3 ;
        this.temp4 = temp4 ;
        this.temp5 = temp5 ;
    }

    public static SlipExpend from(SlipDto slipDto) {
        SlipExpendDto slipExpendDto = slipDto.getSlipExpendDto();
        return SlipExpend.builder()
            .compCd(slipDto.getCompCd())
            .slipHeaderId(slipDto.getSlipHeaderId())
            .slipNo(slipDto.getSlipNo())
            .expendCd(slipExpendDto.getExpendCd())
            .expendReceiveNm(slipExpendDto.getExpendReceiveNm())
            .expendRelation(slipExpendDto.getExpendRelation())
            .expendDt(slipExpendDto.getExpendDt())
            .expendAmt(slipExpendDto.getExpendAmt())
            .wreathYn(slipExpendDto.getWreathYn())
            .holiday(slipExpendDto.getHoliday())
            .mutualYn(slipExpendDto.getMutualYn())
            .temp1(slipExpendDto.getTemp1())
            .temp2(slipExpendDto.getTemp2())
            .temp3(slipExpendDto.getTemp3())
            .temp4(slipExpendDto.getTemp4())
            .temp5(slipExpendDto.getTemp5())
            .build();
    }

    public void update(SlipExpendDto slipExpendDto) {
        this.expendDt = slipExpendDto.getExpendDt();
        this.expendCd = slipExpendDto.getExpendCd();
        this.expendReceiveNm = slipExpendDto.getExpendReceiveNm();
        this.expendRelation = slipExpendDto.getExpendRelation();
        this.expendAmt = slipExpendDto.getExpendAmt();
        this.wreathYn = slipExpendDto.getWreathYn();
        this.holiday = slipExpendDto.getHoliday();
        this.mutualYn = slipExpendDto.getMutualYn();
        this.temp1 = slipExpendDto.getTemp1();
        this.temp2 = slipExpendDto.getTemp2();
        this.temp3 = slipExpendDto.getTemp3();
        this.temp4 = slipExpendDto.getTemp4();
        this.temp5 = slipExpendDto.getTemp5();
    }
    public void reset() {
        this.expendCd = null ;
        this.expendReceiveNm = null ;
        this.expendRelation = null ;
        this.expendDt = null ;
        this.expendAmt = null ;
        this.wreathYn = null ;
        this.mutualYn = null ;
        this.holiday = null ;
        this.temp1 = null ;
        this.temp2 = null ;
        this.temp3 = null ;
        this.temp4 = null ;
        this.temp5 = null ;
    }

    public static SlipExpend copy(String slipNo, BigDecimal slipHeaderId, SlipExpend slipExpend) {
        return SlipExpend.builder()
            .compCd(slipExpend.getCompCd())
            .slipNo(slipNo)
            .slipHeaderId(slipHeaderId)
            .expendCd(slipExpend.getExpendCd())
            .expendReceiveNm(slipExpend.getExpendReceiveNm())
            .expendRelation(slipExpend.getExpendRelation())
            .expendDt(slipExpend.getExpendDt())
            .expendAmt(slipExpend.getExpendAmt())
            .wreathYn(slipExpend.getWreathYn())
            .holiday(slipExpend.getHoliday())
            .mutualYn(slipExpend.getMutualYn())
            .temp1(slipExpend.getTemp1())
            .temp2(slipExpend.getTemp2())
            .temp3(slipExpend.getTemp3())
            .temp4(slipExpend.getTemp4())
            .temp5(slipExpend.getTemp5())
            .build();
    }




}

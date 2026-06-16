package com.iljin.apiServer.ijeas.slip.detail;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.SlipDetailDto;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_SLIP_TRAFFIC_DT")
@IdClass(SlipTrafficDtKey.class)
public class SlipTrafficDt extends BaseEntity {

	@Id
	@Column(name="COMP_CD" , nullable=false)
	String compCd;

	@Id
	@Column(name = "SLIP_NO" , nullable=false)
	String slipNo;

	@Id
	@Column(name = "SLIP_HEADER_ID" , nullable=false)
	BigDecimal slipHeaderId;

	@Id
	@Column(name = "SLIP_SEQ" , nullable=false)
	String slipSeq;

	@Column(name = "USED_DT")
	String usedDt;

	@Column(name = "FROM_AREA")
	String fromArea;

	@Column(name = "FROM_AREA_TEXT")
	String fromAreaText;

	@Column(name = "TO_AREA")
	String toArea;

	@Column(name = "TO_AREA_TEXT")
	String toAreaText;

	@Column(name = "TRAFFIC_TYPE")
	String trafficType;

	@Column(name = "DISTANCE")
	String distance;

	@Column(name = "OIL_PRICE")
	String oilPrice;

	@Column(name = "MILEAGE")
	String mileage;

	@Column(name = "OIL_AMT")
	String oilAmt;

	@Column(name = "OIL_BUNGAE")
	String oilBungae;

	@Column(name = "ETC_TYPE1")
	String etcType1;

	@Column(name = "ETC_AMT1")
	String etcAmt1;

	@Column(name = "ETC_BUNGAE1")
	String etcBungae1;

	@Column(name = "ETC_CHIT_CD1")
	String etcChitCd1;

	@Column(name = "ETC_USED_NO1")
	String etcUsedNo1;

	@Column(name = "ETC_TYPE2")
	String etcType2;

	@Column(name = "ETC_AMT2")
	String etcAmt2;

	@Column(name = "ETC_BUNGAE2")
	String etcBungae2;

	@Column(name = "ETC_CHIT_CD2")
	String etcChitCd2;

	@Column(name = "ETC_USED_NO2")
	String etcUsedNo2;

	@Column(name = "ETC_TYPE3")
	String etcType3;

	@Column(name = "ETC_AMT3")
	String etcAmt3;

	@Column(name = "ETC_BUNGAE3")
	String etcBungae3;

	@Column(name = "ETC_CHIT_CD3")
	String etcChitCd3;

	@Column(name = "ETC_USED_NO3")
	String etcUsedNo3;

	@Column(name = "SUM_AMT")
	String sumAmt;

	@Column(name = "OIL_AMT_TYPE")
	String oilAmtType;

	@Column(name = "CONF_DIST")
	BigDecimal confDist;

	@Column(name = "PAYMENT_DT")
	LocalDateTime paymentDt;

	@Column(name = "REMARK")
	String remark;

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
	public SlipTrafficDt(String compCd, String slipNo, BigDecimal slipHeaderId, String slipSeq,
		String usedDt, String fromArea, String fromAreaText, String toArea, String toAreaText,
		String trafficType, String distance, String oilPrice, String mileage, String oilAmt,
		String oilBungae, String etcType1, String etcAmt1, String etcBungae1, String etcChitCd1,
		String etcUsedNo1, String etcType2, String etcAmt2, String etcBungae2, String etcChitCd2,
		String etcUsedNo2, String etcType3, String etcAmt3, String etcBungae3, String etcChitCd3,
		String etcUsedNo3, String sumAmt, String oilAmtType, BigDecimal confDist,
		LocalDateTime paymentDt, String remark, String temp1, String temp2, String temp3,
		String temp4,
		String temp5) {
		this.compCd = compCd;
		this.slipNo = slipNo;
		this.slipHeaderId = slipHeaderId;
		this.slipSeq = slipSeq;
		this.usedDt = usedDt;
		this.fromArea = fromArea;
		this.fromAreaText = fromAreaText;
		this.toArea = toArea;
		this.toAreaText = toAreaText;
		this.trafficType = trafficType;
		this.distance = distance;
		this.oilPrice = oilPrice;
		this.mileage = mileage;
		this.oilAmt = oilAmt;
		this.oilBungae = oilBungae;
		this.etcType1 = etcType1;
		this.etcAmt1 = etcAmt1;
		this.etcBungae1 = etcBungae1;
		this.etcChitCd1 = etcChitCd1;
		this.etcUsedNo1 = etcUsedNo1;
		this.etcType2 = etcType2;
		this.etcAmt2 = etcAmt2;
		this.etcBungae2 = etcBungae2;
		this.etcChitCd2 = etcChitCd2;
		this.etcUsedNo2 = etcUsedNo2;
		this.etcType3 = etcType3;
		this.etcAmt3 = etcAmt3;
		this.etcBungae3 = etcBungae3;
		this.etcChitCd3 = etcChitCd3;
		this.etcUsedNo3 = etcUsedNo3;
		this.sumAmt = sumAmt;
		this.oilAmtType = oilAmtType;
		this.confDist = confDist;
		this.paymentDt = paymentDt;
		this.remark = remark;
		this.temp1 = temp1;
		this.temp2 = temp2;
		this.temp3 = temp3;
		this.temp4 = temp4;
		this.temp5 = temp5;
	}

	public static SlipTrafficDt from(SlipDto slipDto, int idx) {
		SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(idx);
		return SlipTrafficDt.builder()
			.compCd(slipDto.getCompCd())
			.slipNo(slipDto.getSlipNo())
			.slipHeaderId(slipDto.getSlipHeaderId())
			.slipSeq(String.valueOf(idx+1))
			.usedDt(slipDetailDto.getUsedDt())
			.fromArea(slipDetailDto.getFromArea())
			.fromAreaText(slipDetailDto.getFromAreaText())
			.toArea(slipDetailDto.getToArea())
			.toAreaText(slipDetailDto.getToAreaText())
			.trafficType(slipDetailDto.getTrafficType())
			.distance(slipDetailDto.getDistance())
			.oilPrice(slipDetailDto.getOilPrice())
			.mileage(slipDetailDto.getMileage())
			.oilAmt(slipDetailDto.getOilAmt())
			.oilBungae(slipDetailDto.getOilBungae())
			.etcType1(slipDetailDto.getEtcType1())
			.etcAmt1(slipDetailDto.getEtcAmt1())
			.etcBungae1(slipDetailDto.getEtcBungae1())
			.etcChitCd1(slipDetailDto.getEtcChitCd1())
			.etcUsedNo1(slipDetailDto.getEtcUsedNo1())
			.etcType2(slipDetailDto.getEtcType2())
			.etcAmt2(slipDetailDto.getEtcAmt2())
			.etcBungae2(slipDetailDto.getEtcBungae2())
			.etcChitCd2(slipDetailDto.getEtcChitCd2())
			.etcUsedNo2(slipDetailDto.getEtcUsedNo2())
			.etcType3(slipDetailDto.getEtcType3())
			.etcAmt3(slipDetailDto.getEtcAmt3())
			.etcBungae3(slipDetailDto.getEtcBungae3())
			.etcChitCd3(slipDetailDto.getEtcChitCd3())
			.etcUsedNo3(slipDetailDto.getEtcUsedNo3())
			.sumAmt(slipDetailDto.getSumAmt())
//			.oilAmtType(trafficDtDto.getOilAmtType())
//			.confDist(trafficDtDto.getConfDist())
//			.paymentDt(trafficDtDto.getPaymentDt())
			.remark(slipDetailDto.getBigo())
			.temp2(slipDetailDto.getTemp2())
			.build();
	}

	public static SlipTrafficDt copy(String slipNo, BigDecimal slipHeaderId, SlipTrafficDt slipTrafficDt) {
		return SlipTrafficDt.builder()
			.compCd(slipTrafficDt.getCompCd())
			.slipNo(slipNo)
			.slipHeaderId(slipHeaderId)
			.slipSeq(slipTrafficDt.getSlipSeq())
			.usedDt(slipTrafficDt.getUsedDt())
			.fromArea(slipTrafficDt.getFromArea())
			.fromAreaText(slipTrafficDt.getFromAreaText())
			.toArea(slipTrafficDt.getToArea())
			.toAreaText(slipTrafficDt.getToAreaText())
			.trafficType(slipTrafficDt.getTrafficType())
			.distance(slipTrafficDt.getDistance())
			.oilPrice(slipTrafficDt.getOilPrice())
			.mileage(slipTrafficDt.getMileage())
			.oilAmt(slipTrafficDt.getOilAmt())
			.oilBungae(slipTrafficDt.getOilBungae())
			.etcType1(slipTrafficDt.getEtcType1())
			.etcAmt1(slipTrafficDt.getEtcAmt1())
			.etcBungae1(slipTrafficDt.getEtcBungae1())
			.etcChitCd1(slipTrafficDt.getEtcChitCd1())
			.etcUsedNo1(slipTrafficDt.getEtcUsedNo1())
			.etcType2(slipTrafficDt.getEtcType2())
			.etcAmt2(slipTrafficDt.getEtcAmt2())
			.etcBungae2(slipTrafficDt.getEtcBungae2())
			.etcChitCd2(slipTrafficDt.getEtcChitCd2())
			.etcUsedNo2(slipTrafficDt.getEtcUsedNo2())
			.etcType3(slipTrafficDt.getEtcType3())
			.etcAmt3(slipTrafficDt.getEtcAmt3())
			.etcBungae3(slipTrafficDt.getEtcBungae3())
			.etcChitCd3(slipTrafficDt.getEtcChitCd3())
			.etcUsedNo3(slipTrafficDt.getEtcUsedNo3())
			.sumAmt(slipTrafficDt.getSumAmt())
			.oilAmtType(slipTrafficDt.getOilAmtType())
			.confDist(slipTrafficDt.getConfDist())
			.paymentDt(slipTrafficDt.getPaymentDt())
			.remark(slipTrafficDt.getRemark())
			.temp1(slipTrafficDt.getTemp1())
			.temp2(slipTrafficDt.getTemp2())
			.temp3(slipTrafficDt.getTemp3())
			.temp4(slipTrafficDt.getTemp4())
			.temp5(slipTrafficDt.getTemp5())
			.build();

	}
}

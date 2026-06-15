package com.iljin.apiServer.ijeas.slip.header;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_SLIP_TRAFFIC_HD")
@IdClass(SlipTrafficHdKey.class)
public class SlipTrafficHd extends BaseEntity {

	@Id
	@Column(name="COMP_CD" , nullable=false)
	String compCd;

	@Id
	@Column(name="SLIP_NO" , nullable=false)
	String slipNo;

	@Column(name="SLIP_HEADER_ID")
	BigDecimal slipHeaderId;

	@Column(name = "TRIP_PLACE")
	String tripPlace;

	@Column(name = "TRIP_FROM_DT")
	String tripFromDt;

	@Column(name = "TRIP_TO_DT")
	String tripToDt;

	@Column(name = "TRIP_OBJ")
	String tripObj;

	@Column(name = "PROJECT_NO")
	String projectNo;

	@Column(name = "TRIP_CD")
	String tripCd;

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
	public SlipTrafficHd(String compCd, String slipNo, BigDecimal slipHeaderId, String tripPlace,
						 String tripFromDt, String tripToDt, String tripObj, String projectNo,
						 String tripCd, String temp1, String temp2, String temp3, String temp4, String temp5) {
		this.compCd = compCd;
		this.slipNo = slipNo;
		this.slipHeaderId = slipHeaderId;
		this.tripPlace = tripPlace;
		this.tripFromDt = tripFromDt;
		this.tripToDt = tripToDt;
		this.tripObj = tripObj;
		this.projectNo = projectNo;
		this.tripCd = tripCd;
		this.temp1 = temp1;
		this.temp2 = temp2;
		this.temp3 = temp3;
		this.temp4 = temp4;
		this.temp5 = temp5;
	}

	public static SlipTrafficHd from(SlipDto slipDto) {
		SlipTrafficHdDto trafficHdDto = slipDto.getSlipTrafficHdDto();
		return SlipTrafficHd.builder()
			.compCd(slipDto.getCompCd())
			.slipNo(slipDto.getSlipNo())
			.slipHeaderId(slipDto.getSlipHeaderId())
			.tripPlace(trafficHdDto.getTripPlace())
			.tripFromDt(trafficHdDto.getTripFromDt())
			.tripToDt(trafficHdDto.getTripToDt())
			.tripObj(trafficHdDto.getTripObj())
			.projectNo(trafficHdDto.getProjectNo())
			.tripCd(trafficHdDto.getTripCd())
			.temp1(trafficHdDto.getTemp1())
			.temp2(trafficHdDto.getTemp2())
			.temp3(trafficHdDto.getTemp3())
			.temp4(trafficHdDto.getTemp4())
			.temp5(trafficHdDto.getTemp5())
			.build();
	}

	public void update(SlipTrafficHdDto trafficHdDto) {
		this.tripObj = trafficHdDto.getTripObj();
		this.tripCd = trafficHdDto.getTripCd();
		this.temp2 = trafficHdDto.getTemp2();
	}

	public void reset() {
		this.tripPlace = null;
		this.tripFromDt = null;
		this.tripToDt = null;
		this.tripObj = null;
		this.projectNo = null;
		this.tripCd = null;
		this.temp1 = null;
		this.temp2 = null;
		this.temp3 = null;
		this.temp4 = null;
		this.temp5 = null;
	}

	public static SlipTrafficHd copy(String slipNo, BigDecimal slipHeaderId, SlipTrafficHd slipTrafficHd) {
		return SlipTrafficHd.builder()
			.compCd(slipTrafficHd.getCompCd())
			.slipNo(slipNo)
			.slipHeaderId(slipHeaderId)
			.tripPlace(slipTrafficHd.getTripPlace())
			.tripFromDt(slipTrafficHd.getTripFromDt())
			.tripToDt(slipTrafficHd.getTripToDt())
			.tripObj(slipTrafficHd.getTripObj())
			.projectNo(slipTrafficHd.getProjectNo())
			.tripCd(slipTrafficHd.getTripCd())
			.temp1(slipTrafficHd.getTemp1())
			.temp2(slipTrafficHd.getTemp2())
			.temp3(slipTrafficHd.getTemp3())
			.temp4(slipTrafficHd.getTemp4())
			.temp5(slipTrafficHd.getTemp5())
			.build();
	}


}

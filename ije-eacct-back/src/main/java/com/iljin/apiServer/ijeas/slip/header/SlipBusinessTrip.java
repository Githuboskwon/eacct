package com.iljin.apiServer.ijeas.slip.header;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iljin.apiServer.core.audit.BaseEntity;
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
@Table(name = "TB_SLIP_BUSINESS_TRIP")
@IdClass(SlipBusinessTripKey.class)
public class SlipBusinessTrip extends BaseEntity {


	@Id
	@Column(name = "COMP_CD", nullable=false)
	String compCd;

	@Id
	@Column(name = "MASTER_SLIP_NO", nullable=false)
	String masterSlipNo;

	@Id
	@Column(name = "MASTER_SLIP_HEADER_ID", nullable=false)
	BigDecimal masterSlipHeaderId;

	@Id
	@Column(name = "SEQ", nullable=false)
	Integer seq;

	@Column(name = "LEDGER_ID")
	Integer ledgerId;

	@Column(name = "DETAIL_SLIP_HEADER_ID")
	BigDecimal detailSlipHeaderId;

	@Column(name = "DETAIL_SLIP_NO")
	String detailSlipNo;

	@Column(name = "DETAIL_GL_DT")
	String detailGlDt;

	@Column(name = "DETAIL_TRX_TYPE")
	String detailTrxType;

	@Column(name = "DETAIL_VENDOR_ID")
	BigDecimal detailVendorId;

	@Column(name = "DETAIL_VENDOR_NM")
	String detailVendorNm;

	@Column(name = "DETAIL_USED_CUR")
	String detailUsedCur;

	@Column(name = "DETAIL_USED_AMT")
	BigDecimal detailUsedAmt;

	@Column(name = "DETAIL_REG_ID")
	String detailRegId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DETAIL_REG_DTM")
	LocalDateTime detailRegDtm;

	@Builder
	public SlipBusinessTrip(String compCd,String masterSlipNo,BigDecimal masterSlipHeaderId,Integer seq,
							Integer ledgerId,BigDecimal detailSlipHeaderId,String detailSlipNo,String detailGlDt,
							String detailTrxType,BigDecimal detailVendorId,String detailVendorNm,String detailUsedCur,
							BigDecimal detailUsedAmt,String detailRegId,LocalDateTime detailRegDtm) {
		this.compCd = compCd;
		this.masterSlipNo = masterSlipNo;
		this.masterSlipHeaderId = masterSlipHeaderId;
		this.seq = seq;
		this.ledgerId = ledgerId;
		this.detailSlipHeaderId = detailSlipHeaderId;
		this.detailSlipNo = detailSlipNo;
		this.detailGlDt = detailGlDt;
		this.detailTrxType = detailTrxType;
		this.detailVendorId = detailVendorId;
		this.detailVendorNm = detailVendorNm;
		this.detailUsedCur = detailUsedCur;
		this.detailUsedAmt = detailUsedAmt;
		this.detailRegId = detailRegId;
		this.detailRegDtm = detailRegDtm;
	}

	public static SlipBusinessTrip from(SlipDto slipDto, int idx) {
		SlipBusinessTripDto businessTripDto = slipDto.getSlipBusinessTripDtoList().get(idx);
		return SlipBusinessTrip.builder()
			.compCd(slipDto.getCompCd())
			.masterSlipNo(slipDto.getSlipNo())
			.masterSlipHeaderId(slipDto.getSlipHeaderId())
			.seq(idx+1)
			.ledgerId(slipDto.getLedgerId().intValue())
			.detailSlipHeaderId(businessTripDto.getDetailSlipHeaderId())
			.detailSlipNo(slipDto.getSlipNo())
			.detailGlDt(businessTripDto.getDetailGlDt())
			.detailTrxType(businessTripDto.getDetailTrxType())
			.detailVendorId(businessTripDto.getDetailVendorId())
			.detailVendorNm(businessTripDto.getDetailVendorNm())
			.detailUsedCur(businessTripDto.getDetailUsedCur())
			.detailUsedAmt(businessTripDto.getDetailUsedAmt())
			.detailRegId(businessTripDto.getDetailRegId())
			.detailRegDtm(businessTripDto.getDetailRegDtm())
			.build();
	}

	public void reset() {
		this.ledgerId = null;
		this.detailSlipHeaderId = null;
		this.detailSlipNo = null;
		this.detailGlDt = null;
		this.detailTrxType = null;
		this.detailVendorId = null;
		this.detailVendorNm = null;
		this.detailUsedCur = null;
		this.detailUsedAmt = null;
		this.detailRegId = null;
		this.detailRegDtm = null;
	}

	public void update(SlipBusinessTripDto businessTripDto) {
		this.detailGlDt = businessTripDto.getDetailGlDt();
		this.detailTrxType = businessTripDto.getDetailTrxType();
		this.detailVendorId = businessTripDto.getDetailVendorId();
		this.detailVendorNm = businessTripDto.getDetailVendorNm();
		this.detailUsedCur = businessTripDto.getDetailUsedCur();
		this.detailUsedAmt = businessTripDto.getDetailUsedAmt();
		this.detailRegId = businessTripDto.getDetailRegId();
		this.detailRegDtm = businessTripDto.getDetailRegDtm();
	}

	public static SlipBusinessTrip copy(String slipNo, BigDecimal slipHeaderId,SlipBusinessTrip slipBusinessTrip) {
		return SlipBusinessTrip.builder()
			.compCd(slipBusinessTrip.getCompCd())
			.masterSlipNo(slipNo)
			.masterSlipHeaderId(slipHeaderId)
			.seq(slipBusinessTrip.getSeq())
			.ledgerId(slipBusinessTrip.getLedgerId())
			.detailSlipNo(slipNo)
			.detailSlipHeaderId(slipBusinessTrip.getDetailSlipHeaderId())
			.detailGlDt(slipBusinessTrip.getDetailGlDt())
			.detailTrxType(slipBusinessTrip.getDetailTrxType())
			.detailVendorId(slipBusinessTrip.getDetailVendorId())
			.detailVendorNm(slipBusinessTrip.getDetailVendorNm())
			.detailUsedCur(slipBusinessTrip.getDetailUsedCur())
			.detailUsedAmt(slipBusinessTrip.getDetailUsedAmt())
			.detailRegId(slipBusinessTrip.getDetailRegId())
			.detailRegDtm(slipBusinessTrip.getDetailRegDtm())
			.build();
	}
}

package com.iljin.apiServer.ijeas.slip;

import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.slip.detail.SlipTrafficInfoDto;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_SLIP_DT")
@IdClass(SlipDetailKey.class)
public class SlipDetail extends BaseEntity {


	@Id
	@Column(name="COMP_CD" , nullable=false)
	String compCd;

	@Id
	@Column(name="SLIP_NO" , nullable=false)
	String slipNo;

	@Id
	@Column(name="SLIP_SEQ" , nullable=false)
	String slipSeq;

	@Id
	@Column(name="BUNGAE_NO" , nullable=false)
	String bungaeNo;

	@Column(name="USED_DT")
	String usedDt;

	@Column(name="SLIP_HEADER_ID")
	BigDecimal slipHeaderId;

	@Column(name="SLIP_LINE_ID")
	BigDecimal slipLineId;

	@Column(name="GL_ACCT_CD")
	String glAcctCd;

	@Column(name="TAX_FLAG")
	String taxFlag;

	@Column(name="USED_CUR")
	String usedCur;

	@Column(name="F_AMT")
	String fAmt;

	@Column(name="T_AMT")
	String tAmt;

	@Column(name="SURTAX")
	String surtax;

	@Column(name="ORIGIN_AMT")
	String originAmt;

	@Column(name="ORIGINAL_TAX_FLAG")
	String originalTaxFlag;

	@Column(name="ORIGINAL_SUPPLY_AMT")
	String originalSupplyAmt;

	@Column(name="ORIGINAL_VAT_AMT")
	String originalVatAmt;

	@Column(name="ORIGINAL_USED_AMT")
	String originalUsedAmt;

	@Column(name="ORIGINAL_TAX_ACCT_CD")
	String originalTaxAcctCd;

	@Column(name="USED_NO")
	String usedNo;

	@Column(name="PROJECT_NO")
	String projectNo;

	@Column(name="DRCR_TYPE")
	String drcrType;

	@Column(name="REMARK")
	String remark;

	@Column(name = "ATTRIBUTE1")
	String attribute1;

	@Column(name = "ATTRIBUTE2")
	String attribute2;

	@Column(name = "ATTRIBUTE3")
	String attribute3;

	@Column(name = "ATTRIBUTE4")
	String attribute4;

	@Column(name = "ATTRIBUTE5")
	String attribute5;

	@Column(name = "ATTRIBUTE6")
	String attribute6;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
	    @JoinColumn(name = "slip_no", referencedColumnName="slip_no", insertable = false, updatable = false),
		@JoinColumn(name = "comp_cd", referencedColumnName="comp_cd", insertable = false, updatable = false)
	})
    SlipHeader slipHeader;

	@Builder
	public SlipDetail(String compCd, String slipNo, String slipSeq, String bungaeNo, String usedDt,
		BigDecimal slipHeaderId, BigDecimal slipLineId, String glAcctCd, String taxFlag,
		String usedCur,
		String fAmt, String tAmt, String surtax, String originAmt, String originalTaxFlag,
		String originalSupplyAmt, String originalVatAmt, String originalUsedAmt,
		String originalTaxAcctCd, String usedNo, String projectNo, String drcrType, String remark,
		String attribute1, String attribute2, String attribute3, String attribute4,
		String attribute5,
		String attribute6) {
		this.compCd = compCd;
		this.slipNo = slipNo;
		this.slipSeq = slipSeq;
		this.bungaeNo = bungaeNo;
		this.usedDt = usedDt;
		this.slipHeaderId = slipHeaderId;
		this.slipLineId = slipLineId;
		this.glAcctCd = glAcctCd;
		this.taxFlag = taxFlag;
		this.usedCur = usedCur;
		this.fAmt = fAmt;
		this.tAmt = tAmt;
		this.surtax = surtax;
		this.originAmt = originAmt;
		this.originalTaxFlag = originalTaxFlag;
		this.originalSupplyAmt = originalSupplyAmt;
		this.originalVatAmt = originalVatAmt;
		this.originalUsedAmt = originalUsedAmt;
		this.originalTaxAcctCd = originalTaxAcctCd;
		this.usedNo = usedNo;
		this.projectNo = projectNo;
		this.drcrType = drcrType;
		this.remark = remark;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.attribute3 = attribute3;
		this.attribute4 = attribute4;
		this.attribute5 = attribute5;
		this.attribute6 = attribute6;
	}

	public void reset() {
		this.usedDt = null;
		this.slipHeaderId = null;
		this.slipLineId = null;
		this.glAcctCd = null;
		this.taxFlag = null;
		this.usedCur = null;
		this.fAmt = null;
		this.tAmt = null;
		this.surtax = null;
		this.originAmt = null;
		this.originalTaxFlag = null;
		this.originalSupplyAmt = null;
		this.originalVatAmt = null;
		this.originalUsedAmt = null;
		this.originalTaxAcctCd = null;
		this.usedNo = null;
		this.projectNo = null;
		this.drcrType = null;
		this.remark = null;
		this.attribute1 = null;
		this.attribute2 = null;
		this.attribute3 = null;
		this.attribute4 = null;
		this.attribute5 = null;
		this.attribute6 = null;
	}

	public static SlipDetail from(SlipDto slipDto, SlipTrafficInfoDto infoDto, int idx) {
		SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(idx);
		return SlipDetail.builder()
			.compCd(slipDto.getCompCd())
			.slipNo(slipDto.getSlipNo())
			.slipSeq(infoDto.getSlipSeq())
			.bungaeNo(infoDto.getBungaeNo())
			.usedDt(slipDetailDto.getUsedDt())
			.fAmt(infoDto.getAmt())
			.tAmt(infoDto.getAmt())
			.originAmt(infoDto.getAmt())
			.surtax("0")
			.usedNo(infoDto.getUsedNo())
			.glAcctCd(slipDetailDto.getAcctCode())
			.projectNo(slipDetailDto.getProjectNo())
			.drcrType(slipDetailDto.getDrCr().trim().equals("Cr") ? "대변" : "차변")
			.remark(infoDto.getRemark())
			.slipHeaderId(slipDto.getSlipHeaderId())
			.slipLineId(infoDto.getSlipLineId())
			.originalSupplyAmt(slipDetailDto.getOriginalSupplyAmt())
			.originalVatAmt(slipDetailDto.getOriginalVatAmt())
			.originalUsedAmt(slipDetailDto.getOriginalUsedAmt())
			.originalTaxFlag(slipDetailDto.getOriginalTaxFlag())
			.taxFlag(slipDetailDto.getTaxFlag())
			.originalTaxAcctCd(slipDetailDto.getOriginalTaxAcctCd())
			.attribute1(slipDetailDto.getAttribute1Code())
			.attribute2(slipDetailDto.getAttribute2Code())
			.attribute3(slipDetailDto.getAttribute3Code())
			.attribute4(slipDetailDto.getAttribute4Code())
			.attribute5(slipDetailDto.getAttribute5Code())
			.attribute6(slipDetailDto.getAttribute6Code())
			.build();
	}

	public static SlipDetail from(SlipDto slipDto, BigDecimal slipLineId, String bungaeNo, int idx) {
		SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(idx);
		return SlipDetail.builder()
			.compCd(slipDto.getCompCd())
			.slipNo(slipDto.getSlipNo())
			.slipSeq(String.valueOf(idx+1))
			.bungaeNo(bungaeNo)
			.usedDt(slipDto.getPostingDt())
			.fAmt(slipDetailDto.getUsedAmt().toString())
			.tAmt(slipDetailDto.getUsedAmt().toString())
			.originAmt(slipDetailDto.getSupplyAmount().toString())
			.surtax(slipDetailDto.getVatAmount().toString())
			.usedNo(slipDetailDto.getCardUsedNo())
			.glAcctCd(slipDetailDto.getAcctCode())
			.projectNo(slipDetailDto.getProjectCode())
			.drcrType(slipDetailDto.getDrCr().trim().equals("Cr") ? "대변" : "차변")
			.remark(slipDetailDto.getRemark())
			.slipHeaderId(slipDto.getSlipHeaderId())
			.slipLineId(slipLineId)
			.originalSupplyAmt(slipDetailDto.getOriginalSupplyAmt())
			.originalVatAmt(slipDetailDto.getOriginalVatAmt())
			.originalTaxFlag(slipDetailDto.getOriginalTaxFlag())
			.taxFlag(slipDetailDto.getTaxFlag())
			.originalTaxAcctCd(slipDetailDto.getOriginalTaxAcctCd())
			.attribute1(slipDetailDto.getAttribute1Code())
			.attribute2(slipDetailDto.getAttribute2Code())
			.attribute3(slipDetailDto.getAttribute3Code())
			.attribute4(slipDetailDto.getAttribute4Code())
			.attribute5(slipDetailDto.getAttribute5Code())
			.attribute6(slipDetailDto.getAttribute6Code())
			.build();
	}

	public static SlipDetail copy(String slipNo, BigDecimal slipHeaderId, String bungaeNo, BigDecimal slipLineId, SlipDetail slipDetail) {
		return SlipDetail.builder()
			.compCd(slipDetail.getCompCd())
			.slipNo(slipNo)
			.slipHeaderId(slipHeaderId)
			.slipLineId(slipLineId)
			.slipSeq(slipDetail.getSlipSeq())
			.bungaeNo(bungaeNo)
			.usedDt(slipDetail.getUsedDt())
			.glAcctCd(slipDetail.getGlAcctCd())
			.taxFlag(slipDetail.getTaxFlag())
			.usedCur(slipDetail.getUsedCur())
			.fAmt(slipDetail.getFAmt())
			.tAmt(slipDetail.getTAmt())
			.surtax(slipDetail.getSurtax())
			.originAmt(slipDetail.getOriginAmt())
			.originalTaxFlag(slipDetail.getOriginalTaxFlag())
			.originalSupplyAmt(slipDetail.getOriginalSupplyAmt())
			.originalVatAmt(slipDetail.getOriginalVatAmt())
			.originalUsedAmt(slipDetail.getOriginalUsedAmt())
			.originalTaxAcctCd(slipDetail.getOriginalTaxAcctCd())
			.usedNo(slipDetail.getUsedNo())
			.projectNo(slipDetail.getProjectNo())
			.drcrType(slipDetail.getDrcrType())
			.remark(slipDetail.getRemark())
			.attribute1(slipDetail.getAttribute1())
			.attribute2(slipDetail.getAttribute2())
			.attribute3(slipDetail.getAttribute3())
			.attribute4(slipDetail.getAttribute4())
			.attribute5(slipDetail.getAttribute5())
			.attribute6(slipDetail.getAttribute6())
			.build();
	}
}

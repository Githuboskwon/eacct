package com.iljin.apiServer.ijeas.slip;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iljin.apiServer.core.audit.BaseEntity;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesBatch;
import com.iljin.apiServer.ijeas.es.erpViews.bulk.ErpApPaymentsBatch;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.frgn.ErpSpOsSlip;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTrxHeadersDto;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsHeader;
import com.iljin.apiServer.ijeas.es.erpViews.sale.ErpArTrxHeaders;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_SLIP_HD")
@IdClass(SlipHeaderKey.class)
public class SlipHeader extends BaseEntity {

	@Id
	@Column(name = "COMP_CD" , nullable=false)
	String compCd;

	@Id
	@Column(name = "SLIP_NO" , nullable=false)
	String slipNo;

	@OneToMany(mappedBy = "slipHeader", fetch = FetchType.LAZY)
	@JsonIgnore
	List<SlipDetail> slipDetails;

	@Column(name = "SLIP_HEADER_ID")
	BigDecimal slipHeaderId;

	@Column(name = "SLIP_GROUP_NO")
	String slipGroupNo;

	@Column(name = "EMP_NO")
	String empNo;

	@Column(name = "DEPT_NO")
	String deptNo;

	@Column(name = "SLIP_TYPE")
	String slipType;

	@Column(name = "SLIP_TYPE_CD")
	String slipTypeCd;

	@Column(name = "SLIP_FORM")
	String slipForm;

	@Column(name = "STATUS")
	String status;

	@Column(name = "POSTING_DT")
	String postingDt;

	@Column(name = "HEADER_REMARK")
	String headerRemark;

	@Column(name = "USED_CUR")
	String usedCur;

	@Column(name = "USED_AMT")
	String usedAmt;

	@Column(name = "USED_FOR_AMT")
	String usedForAmt;

	@Column(name = "SCAN_NO")
	String scanNo;

	@Column(name = "DOC_NO")
	String docNo;

	@Column(name = "EVIDENCE_YN")
	String evidenceYn;

	@Column(name = "APPROVAL_GROUP_ID")
	BigDecimal approvalGroupId;

	@Column(name = "TAXBILL_SUPPLY_AMT")
	BigDecimal taxbillSupplyAmt;

	@Column(name = "TAXBILL_TAX_AMT")
	BigDecimal taxbillTaxAmt;

	@Column(name = "TAXBILL_TOTAL_AMT")
	BigDecimal taxbillTotalAmt;

	@Column(name = "TAX_SMARTBILL_NO")
	String taxSmartbillNo;

	@Column(name = "LEDGER_ID")
	BigDecimal ledgerId;

	@Column(name = "VALIDATION_FLAG")
	String validationFlag;

	@Column(name = "ERROR_MSG")
	String errorMsg;

	@Column(name = "DOC_TITLE")
	String docTitle;

	@Column(name = "DOC_URL")
	String docUrl;

	@Column(name = "ERP_INVOICE_ID")
	String erpInvoiceId;

	@Column(name = "ERP_APP_USER_ID")
	String erpAppUserId;

	@Column(name = "ERP_VENDOR_NM")
	String erpVendorNm;

	@Column(name = "ERP_XTR_SLIP_TYPE")
	String erpXtrSlipType;

	@Column(name = "ERP_TRANSACTION_NO")
	String erpTransactionNo;

	@Column(name = "TAXBILL_AMT_MODIFY_YN")
	String taxbillAmtModifyYn;

	@Column(name = "TAXBILL_SU_ID")
	String taxbillSuId;

	@Column(name = "TRANSFER_TYPE")
	String transferType;

	@Column(name = "REMARK")
	String remark;

	@Column(name = "PREPAYMENT_YN")
	String prepaymentYn;

	@Column(name = "SLIP_REUSE_POSSIBLE_YN")
	String slipReusePossibleYn;

	@Column(name = "SLIP_COPY_YN")
	String slipCopyYn;


	@Builder
	public SlipHeader(String compCd, String slipNo, BigDecimal slipHeaderId, String slipGroupNo,
		String empNo, String deptNo, String slipType, String slipTypeCd, String slipForm,
		String status,
		String postingDt, String headerRemark, String usedCur, String usedAmt, String usedForAmt,
		String scanNo, String docNo, String evidenceYn, BigDecimal approvalGroupId,
		BigDecimal taxbillSupplyAmt, BigDecimal taxbillTaxAmt, BigDecimal taxbillTotalAmt,
		String taxSmartbillNo, BigDecimal ledgerId, String validationFlag, String errorMsg,
		String docTitle, String docUrl, String erpInvoiceId, String erpAppUserId,
		String erpVendorNm,
		String erpXtrSlipType, String erpTransactionNo, String taxbillAmtModifyYn,
		String taxbillSuId,
		String transferType, String remark, String prepaymentYn, String slipReusePossibleYn,
		String slipCopyYn) {
		this.compCd = compCd;
		this.slipNo = slipNo;
		this.slipHeaderId = slipHeaderId;
		this.slipGroupNo = slipGroupNo;
		this.empNo = empNo;
		this.deptNo = deptNo;
		this.slipType = slipType;
		this.slipTypeCd = slipTypeCd;
		this.slipForm = slipForm;
		this.status = status;
		this.postingDt = postingDt;
		this.headerRemark = headerRemark;
		this.usedCur = usedCur;
		this.usedAmt = usedAmt;
		this.usedForAmt = usedForAmt;
		this.scanNo = scanNo;
		this.docNo = docNo;
		this.evidenceYn = evidenceYn;
		this.approvalGroupId = approvalGroupId;
		this.taxbillSupplyAmt = taxbillSupplyAmt;
		this.taxbillTaxAmt = taxbillTaxAmt;
		this.taxbillTotalAmt = taxbillTotalAmt;
		this.taxSmartbillNo = taxSmartbillNo;
		this.ledgerId = ledgerId;
		this.validationFlag = validationFlag;
		this.errorMsg = errorMsg;
		this.docTitle = docTitle;
		this.docUrl = docUrl;
		this.erpInvoiceId = erpInvoiceId;
		this.erpAppUserId = erpAppUserId;
		this.erpVendorNm = erpVendorNm;
		this.erpXtrSlipType = erpXtrSlipType;
		this.erpTransactionNo = erpTransactionNo;
		this.taxbillAmtModifyYn = taxbillAmtModifyYn;
		this.taxbillSuId = taxbillSuId;
		this.transferType = transferType;
		this.remark = remark;
		this.prepaymentYn = prepaymentYn;
		this.slipReusePossibleYn = slipReusePossibleYn;
		this.slipCopyYn = slipCopyYn;
	}

	public static SlipHeader from(ErpApPaymentsHeader erpApPaymentsHeader, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
			.compCd(String.valueOf(erpApPaymentsHeader.getOrgId()))
			.deptNo(emp.getDeptCd())
			.empNo(emp.getEmpNo())
			.slipHeaderId(slipHeaderId)
			.slipNo(slipNo)
			.slipType(SlipType.ITEM.getCode())
			.status("SV")
			.postingDt(erpApPaymentsHeader.getCheckDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
			.headerRemark(erpApPaymentsHeader.getRemark())
			.usedCur(erpApPaymentsHeader.getCurrencyCode())
			.usedAmt(erpApPaymentsHeader.getBaseAmount().toString())
			.usedForAmt(erpApPaymentsHeader.getAmount())
			.erpInvoiceId(String.valueOf(erpApPaymentsHeader.getCheckId()))
			.erpAppUserId(erpApPaymentsHeader.getTransferredBy())
			.erpVendorNm(erpApPaymentsHeader.getVendorName())
			.approvalGroupId(slipHeaderId)
			.build();
	}

	public static SlipHeader from(ErpApPaymentsBatch erpApPaymentsBatch, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
				.compCd(String.valueOf(erpApPaymentsBatch.getOrgId()))
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.BULK.getCode())
				.postingDt(erpApPaymentsBatch.getCheckDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.headerRemark(erpApPaymentsBatch.getRemark())
				.usedCur(erpApPaymentsBatch.getCurrencyCode())
				.usedForAmt(String.valueOf(erpApPaymentsBatch.getSumAmount()))
				.usedAmt(String.valueOf(erpApPaymentsBatch.getSumBaseAmount()))
				.usedForAmt(String.valueOf(erpApPaymentsBatch.getSumAmount()))
				.erpInvoiceId(String.valueOf(erpApPaymentsBatch.getEslipTransferBatchId()))
				.erpAppUserId(erpApPaymentsBatch.getTransferredBy())
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpApFuturesBatch erpApFuturesBatch, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
				.compCd(String.valueOf(erpApFuturesBatch.getOrgId()))
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.BOND.getCode())
				.postingDt(erpApFuturesBatch.getFuturePayDueDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.headerRemark(erpApFuturesBatch.getRemark())
				.usedAmt(String.valueOf(erpApFuturesBatch.getSumAmount()))
				.erpInvoiceId(String.valueOf(erpApFuturesBatch.getEslipTransferBatchId()))
				.erpAppUserId(erpApFuturesBatch.getTransferredBy())
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpTrTrxHeadersDto erpTrTrxHeaders, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
				.compCd(String.valueOf(erpTrTrxHeaders.getOrgId()))
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.FUND.getCode())
				.erpXtrSlipType(erpTrTrxHeaders.getXtrSlipType())
				.erpTransactionNo(erpTrTrxHeaders.getTransactionNumber())
				.erpInvoiceId(String.valueOf(erpTrTrxHeaders.getTrxUniqueId()))
				.erpAppUserId(erpTrTrxHeaders.getCreatedBy())
				.usedCur(erpTrTrxHeaders.getCurrencyCodeHeader())
				.remark(erpTrTrxHeaders.getDescription())
				.postingDt(erpTrTrxHeaders.getJournalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpTrFundTrnHeaders erpTrFundTrnHeaders, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
				.compCd(String.valueOf(erpTrFundTrnHeaders.getOrgId()))
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.CLCT.getCode())
				.erpXtrSlipType(erpTrFundTrnHeaders.getXtrSlipType())
				.postingDt(erpTrFundTrnHeaders.getGlDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.headerRemark(erpTrFundTrnHeaders.getRemark())
				.usedCur(erpTrFundTrnHeaders.getCurrencyCode())
				.status("SV")
				.usedAmt(String.valueOf(erpTrFundTrnHeaders.getFunctionalAmountCr()))
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpArTrxHeaders erpArTrxHeaders, String slipNo, BigDecimal slipHeaderId, Employee emp, String compCd) {
		return SlipHeader.builder()
				.compCd(compCd)
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.SALE.getCode())
				.erpTransactionNo(erpArTrxHeaders.getSlipNumber())
				.erpInvoiceId(String.valueOf(erpArTrxHeaders.getSlipId()))
				.erpXtrSlipType(erpArTrxHeaders.getSlipType())
				.postingDt(erpArTrxHeaders.getGlDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.ledgerId(new BigDecimal(erpArTrxHeaders.getLedgerId()))
				.usedCur(erpArTrxHeaders.getCurrencyCode())
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpSpOsSlip erpSpOsSlips, String slipNo, BigDecimal slipHeaderId, Employee emp, String compCd) {
		return SlipHeader.builder()
				.compCd(compCd)
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.FRGN.getCode())
				.erpTransactionNo(erpSpOsSlips.getSlipNumber())
				.erpInvoiceId(String.valueOf(erpSpOsSlips.getSlipId()))
				.erpXtrSlipType(erpSpOsSlips.getSlipType())
				.postingDt(erpSpOsSlips.getGlDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.ledgerId(new BigDecimal(erpSpOsSlips.getLedgerId()))
				.usedCur(erpSpOsSlips.getCurrencyCode())
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.build();
	}

	public static SlipHeader from(ErpArTrxHeaders erpArTrxHeaders, String slipNo, BigDecimal slipHeaderId, Employee emp, String compCd, String remark) {
		return SlipHeader.builder()
				.compCd(compCd)
				.deptNo(emp.getDeptCd())
				.empNo(emp.getEmpNo())
				.slipHeaderId(slipHeaderId)
				.slipNo(slipNo)
				.slipType(SlipType.EXPT.getCode())
				.erpTransactionNo(erpArTrxHeaders.getSlipNumber())
				.erpInvoiceId(String.valueOf(erpArTrxHeaders.getSlipId()))
				.erpXtrSlipType(erpArTrxHeaders.getSlipType())
				.postingDt(erpArTrxHeaders.getGlDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.ledgerId(new BigDecimal(erpArTrxHeaders.getLedgerId()))
				.usedCur(erpArTrxHeaders.getCurrencyCode())
				.status("SV")
				.approvalGroupId(slipHeaderId)
				.remark(erpArTrxHeaders.getCoaSegment5())
				.build();
	}

	public static SlipHeader from(ErpGlHeaders erpGlHeaders, String slipNo, BigDecimal slipHeaderId, Employee emp) {
		return SlipHeader.builder()
			.compCd(String.valueOf(erpGlHeaders.getOrgId()))
			.slipHeaderId(slipHeaderId)
			.deptNo(emp.getDeptCd())
			.empNo(emp.getEmpNo())
			.slipNo(slipNo)
			.slipType(SlipType.GL.getCode())
			.erpInvoiceId(String.valueOf(erpGlHeaders.getJeHeaderId()))
			.usedCur(erpGlHeaders.getCurrencyCode())
			.ledgerId(erpGlHeaders.getLedgerId())
			.postingDt(erpGlHeaders.getGlDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
			.headerRemark(erpGlHeaders.getHeaderDescription())
			.status("SV")
//			.usedAmt()
			.approvalGroupId(slipHeaderId)
			.build();
	}

	public static SlipHeader from(SlipDto slipDto) {
		return SlipHeader.builder()
			.slipForm(slipDto.getSlipForm())
			.compCd(slipDto.getCompCd())
			.slipNo(slipDto.getSlipNo())
			.slipHeaderId(slipDto.getSlipHeaderId())
			.slipGroupNo(slipDto.getSlipGroupNo())
			.empNo(slipDto.getEmpNo())
			.approvalGroupId(nonNull(slipDto.getApprovalGroupId()) ? slipDto.getApprovalGroupId() : slipDto.getSlipHeaderId())
			.deptNo(slipDto.getActualDeptCode())
			.slipType(slipDto.getTrxTypeCode())
			.slipTypeCd(slipDto.getSlipTypeCd())
			.status("SV")
			.postingDt(slipDto.getPostingDt())
			.headerRemark(slipDto.getDescription())
			.remark(slipDto.getRemark())
			.scanNo(slipDto.getScanNo())
			.taxSmartbillNo(slipDto.getTaxSmartbillNo())
			.taxbillSupplyAmt(slipDto.getTaxbillSupplyAmt())
			.taxbillTaxAmt(slipDto.getTaxbillTaxAmt())
			.taxbillTotalAmt(slipDto.getTaxbillTotalAmt())
			.ledgerId(slipDto.getLedgerId())
			.remark(slipDto.getRemark())
			.docNo(slipDto.getDocNo())
			.docTitle(slipDto.getDocTitle())
			.docUrl(slipDto.getDocUrl())
			.taxbillAmtModifyYn(slipDto.getTaxbillAmtModifyYn())
			.taxbillSuId(slipDto.getTaxbillSuId())
			.usedAmt(nonNull(slipDto.getAccountedAmount()) ? slipDto.getAccountedAmount().toString() : null)
			.usedCur(slipDto.getSlipCurrencyCode())
			.usedForAmt(nonNull(slipDto.getEnteredAmount()) ? slipDto.getEnteredAmount().toString() : null)
			.erpInvoiceId(slipDto.getErpInvoiceId())
			.erpAppUserId(slipDto.getErpAppUserId())
//			.erpVendorNm(slipDto.getErpVendorNm())
			.erpXtrSlipType(slipDto.getErpXtrSlipType())
			.erpTransactionNo(slipDto.getErpTransactionNo())
			.evidenceYn(slipDto.getEvidenceYn())
			.validationFlag("N")
//			.errorMsg(slipDto.getErrorMsg())
			.transferType(slipDto.getTransferType())
			.prepaymentYn(slipDto.getPrepaymentYn())
			.build();
	}

	public void changeStatus(String status) {
		this.status = status;
	}

	public void update(SlipDto slipDto) {
		this.approvalGroupId = nonNull(slipDto.getApprovalGroupId()) ? slipDto.getApprovalGroupId() : slipDto.getSlipHeaderId();
		this.empNo = slipDto.getEmpNo();
		this.deptNo = slipDto.getActualDeptCode();
		this.status = "SV";
		this.slipType = slipDto.getTrxTypeCode();
		this.postingDt = slipDto.getPostingDt();
		this.headerRemark = slipDto.getDescription();
		this.remark = slipDto.getRemark();
		this.usedCur = slipDto.getSlipCurrencyCode();
		this.usedAmt = nonNull(slipDto.getAccountedAmount()) ? slipDto.getAccountedAmount().toString() : null;
		this.usedForAmt = nonNull(slipDto.getEnteredAmount()) ? slipDto.getEnteredAmount().toString() : null;
		this.scanNo = slipDto.getScanNo();
		this.taxSmartbillNo = slipDto.getTaxSmartbillNo();
		this.taxbillSupplyAmt = slipDto.getTaxbillSupplyAmt();
		this.taxbillTaxAmt = slipDto.getTaxbillTaxAmt();
		this.taxbillTotalAmt = slipDto.getTaxbillTotalAmt();
		this.ledgerId = slipDto.getLedgerId();
		this.evidenceYn = slipDto.getEvidenceYn();
		this.docTitle = slipDto.getDocTitle();
		this.docUrl = slipDto.getDocUrl();
		this.taxbillAmtModifyYn = slipDto.getTaxbillAmtModifyYn();
		this.taxbillSuId = slipDto.getTaxbillSuId();
		this.prepaymentYn = slipDto.getPrepaymentYn();
	}

	public void reset() {
		this.deptNo = null;
		this.empNo = null;
		this.slipType = null;
		this.slipTypeCd = null;
		this.slipForm = null;
		this.status = null;
		this.postingDt = null;
		this.headerRemark = null;
		this.usedCur = null;
		this.usedAmt = null;
		this.usedForAmt = null;
		this.scanNo = null;
		this.docNo = null;
		this.evidenceYn = null;
		this.approvalGroupId = null;
		this.taxbillSupplyAmt = null;
		this.taxbillTaxAmt = null;
		this.taxbillTotalAmt = null;
		this.taxSmartbillNo = null;
		this.ledgerId = null;
		this.validationFlag = null;
		this.errorMsg = null;
		this.docTitle = null;
		this.docUrl = null;
		this.erpInvoiceId = null;
		this.erpAppUserId = null;
		this.erpVendorNm = null;
		this.erpXtrSlipType = null;
		this.erpTransactionNo = null;
		this.taxbillAmtModifyYn = null;
		this.taxbillSuId = null;
		this.transferType = null;
		this.remark = null;
		this.prepaymentYn = null;
	}

	public void setTransferType(String status) {
		this.transferType = status;
	}

	public void setDeleteFlag() {
		this.status = "SD";
		this.taxSmartbillNo = null;
		this.taxbillSuId = null;
	}

	public void updateTaxbillInfo(SlipHeaderDto slipHeaderDto) {
		this.taxbillSupplyAmt = slipHeaderDto.getTaxbillSupplyAmt();
		this.taxbillTaxAmt = slipHeaderDto.getTaxbillTaxAmt();
		this.taxbillTotalAmt = slipHeaderDto.getTaxbillTotalAmt();
	}

	public void changeEvidenceYn(String evidenceYn) {
		this.evidenceYn = evidenceYn;
	}

	public void updateBudgetSlip(String deptNo, String slipType, String usedCur, String status, String postingDt, BigDecimal approvalGroupId, String scanNo,
								 String erpInvoiceId, String headerRemark, String remark, String docTitle, String docUrl){
		this.deptNo = deptNo;
		this.slipType = slipType;
		this.usedCur = usedCur;
		this.status = status;
		this.postingDt = postingDt;
		this.approvalGroupId = approvalGroupId;
		this.scanNo = scanNo;
		this.erpInvoiceId = erpInvoiceId;
		this.headerRemark = headerRemark;
		this.remark = remark;
		this.docTitle = docTitle;
		this.docUrl = docUrl;
	}

	public void changeTaxSmartBillNo(String taxSmartbillNo) {
		this.taxSmartbillNo = taxSmartbillNo;
	}

	public void changeTaxbillSuId(String taxbillSuId) {
		this.taxbillSuId = taxbillSuId;
	}

	public void changeSlipReusePossibleYn(String slipReusePossibleYn) {
		this.slipReusePossibleYn = slipReusePossibleYn;
	}

	public void changeSlipCopyYn(String slipCopyYn) {
		this.slipCopyYn = slipCopyYn;
	}

	public static SlipHeader copy(String slipNo, BigDecimal slipHeaderId, String slipGroupNo, SlipHeader slipHeader) {
		return SlipHeader.builder()
			.compCd(slipHeader.getCompCd())
			.slipNo(slipNo)
			.slipHeaderId(slipHeaderId)
			.slipGroupNo(slipGroupNo)
			.empNo(slipHeader.getEmpNo())
			.deptNo(slipHeader.getDeptNo())
			.slipType(slipHeader.getSlipType())
			.slipTypeCd(slipHeader.getSlipTypeCd())
			.slipForm(slipHeader.getSlipForm())
			.status("SV")
			.postingDt(slipHeader.getPostingDt())
			.headerRemark(slipHeader.getHeaderRemark())
			.usedCur(slipHeader.getUsedCur())
			.usedAmt(slipHeader.getUsedAmt())
			.usedForAmt(slipHeader.getUsedForAmt())
			.scanNo(slipHeader.getScanNo())
			.docNo(slipHeader.getDocNo())
			.evidenceYn(slipHeader.getEvidenceYn())
			.approvalGroupId(slipHeaderId)
			.taxbillSupplyAmt(slipHeader.getTaxbillSupplyAmt())
			.taxbillTaxAmt(slipHeader.getTaxbillTaxAmt())
			.taxbillTotalAmt(slipHeader.getTaxbillTotalAmt())
			.taxSmartbillNo(slipHeader.getTaxSmartbillNo())
			.ledgerId(slipHeader.getLedgerId())
			.validationFlag(slipHeader.getValidationFlag())
			.errorMsg(slipHeader.getErrorMsg())
			.docTitle(slipHeader.getDocTitle())
			.docUrl(slipHeader.getDocUrl())
			.erpInvoiceId(slipHeader.getErpInvoiceId())
			.erpAppUserId(slipHeader.getErpAppUserId())
			.erpVendorNm(slipHeader.getErpVendorNm())
			.erpXtrSlipType(slipHeader.getErpXtrSlipType())
			.erpTransactionNo(slipHeader.getErpTransactionNo())
			.taxbillAmtModifyYn(slipHeader.getTaxbillAmtModifyYn())
			.taxbillSuId(slipHeader.getTaxbillSuId())
			.transferType(slipHeader.getTransferType())
			.prepaymentYn(slipHeader.getPrepaymentYn())
			.slipReusePossibleYn("Y")
			.slipCopyYn("N")
			.remark(slipHeader.getRemark())
			.build();
	}

	public void updatetestSlip(String docTitle, String docUrl){
		this.docTitle = docTitle;
		this.docUrl = docUrl;
	}

}

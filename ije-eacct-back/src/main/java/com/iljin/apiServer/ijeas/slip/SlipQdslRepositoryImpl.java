package com.iljin.apiServer.ijeas.slip;

import static com.iljin.apiServer.ijeas.approval.QApprovalDetail.approvalDetail;
import static com.iljin.apiServer.ijeas.approval.QApprovalHeader.approvalHeader;
import static com.iljin.apiServer.ijeas.bond.QBondHeader.bondHeader;
import static com.iljin.apiServer.ijeas.bond.QBondMst.bondMst;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeader.erpSlipHeader;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipLine.erpSlipLine;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.slip.history.QApInvoicesAll.apInvoicesAll;
import static com.iljin.apiServer.ijeas.slip.history.QCboApViewPrepaysV.cboApViewPrepaysV;
import static com.iljin.apiServer.ijeas.slip.history.QPoHeadersAll.poHeadersAll;
import static com.iljin.apiServer.ijeas.slipCommon.hierarchy.QCoaHierarchy.coaHierarchy;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.trx.QTrx.trx;
import static com.iljin.apiServer.ijeas.system.vendor.QVendor.vendor;
import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.bond.QBondHeader;
import com.iljin.apiServer.ijeas.bond.QBondHeaderDto;
import com.iljin.apiServer.ijeas.slip.history.QSlipHistoryDto;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchyDto;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.QCoaHierarchyDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SlipQdslRepositoryImpl implements SlipQdslRepository{

    private final JPAQueryFactory queryFactory;
    private com.querydsl.core.types.dsl.Expressions Expressions;

    private final Util util;

    @Override
    public List<SlipHistoryDto> getSlipHistory(SlipHistoryDto slipHistoryDto){
        Pageable pageable = PageRequest.of(slipHistoryDto.getPage(), slipHistoryDto.getPageSize());
        String loginId = util.getLoginId();

        List<SlipHistoryDto> result = queryFactory
                .select(new QSlipHistoryDto(
                        slipHeader.compCd,
                        slipHeader.slipNo,
                        slipHeader.deptNo,
                        ExpressionUtils.as(JPAExpressions.select(costCenter.deptNm)
                                        .from(costCenter)
                                        .where(costCenter.deptCd.eq(slipHeader.deptNo)
                                                .and(costCenter.compCd.eq(slipHeader.compCd))),
                                "deptNm"),
                        slipHeader.empNo,
                        ExpressionUtils.as(JPAExpressions.select(employee.empNm)
                                        .from(employee)
                                        .where(employee.empNo.eq(slipHeader.empNo)
                                                .and(employee.compCd.eq(slipHeader.compCd))),
                                "empNm"),
                        slipHeader.slipType,
                        ExpressionUtils.as(JPAExpressions.select(trx.trxTypeNm)
                                        .from(trx)
                                        .where(trx.trxTypeCd.eq(slipHeader.slipType)
                                                .and(trx.compCd.eq(slipHeader.compCd))),
                                "slipTypeNm"),
                        slipHeader.slipTypeCd,
                        slipHeader.status,
                        ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                        .from(codeDetail)
                                        .where(codeDetail.groupCd.eq("PROGRESS_STATUS_CD")
                                                .and(codeDetail.compCd.eq(slipHeader.compCd))
                                                .and(codeDetail.detailCd.eq(slipHeader.status))),
                                "statusNm"),
                        slipHeader.postingDt,
                        slipHeader.headerRemark,
                        slipHeader.usedCur,
                        slipHeader.usedAmt,
                        slipHeader.usedForAmt,
                        slipHeader.scanNo,
                        slipHeader.slipHeaderId,
                        slipHeader.slipGroupNo,
                        slipHeader.docNo,
                        slipHeader.regDtm,
                        slipHeader.regId,
                        slipHeader.chgDtm,
                        slipHeader.chgId,
                        slipHeader.slipForm,
                        slipHeader.remark,
                        slipHeader.approvalGroupId,
                        slipHeader.taxbillSupplyAmt,
                        slipHeader.taxbillTaxAmt,
                        slipHeader.taxbillTotalAmt,
                        slipHeader.taxSmartbillNo,
                        slipHeader.ledgerId,
                        slipHeader.validationFlag,
                        slipHeader.errorMsg,
                        slipHeader.docTitle,
                        slipHeader.docUrl,
                        slipHeader.erpInvoiceId,
                        slipHeader.erpAppUserId,
                        slipHeader.erpXtrSlipType,
                        slipHeader.erpTransactionNo,
                        slipHeader.taxbillAmtModifyYn,
                        slipHeader.taxbillSuId,
                        erpSlipHeader.invoiceTypeLookupCode,
                        erpSlipHeader.batchSourceName,
                        erpSlipHeader.categoryName,
                        erpSlipHeader.trxTypeCode,
                        erpSlipHeader.ttypeInputModule,
                        erpSlipHeader.ttypeInterfaceModule,
                        erpSlipHeader.ttypeInterfaceSlipType,
                        erpSlipHeader.ttypePrepaymentFlag,
                        erpSlipHeader.ttypeClearingAcctCode,
                        erpSlipHeader.ttypeAddInfoType,
                        erpSlipHeader.ttypeIntegrationVendorNum,
                        erpSlipHeader.ttypePaymentReceiptTermId,
                        erpSlipHeader.compCode,
                        erpSlipHeader.budgetDeptCode,
                        erpSlipHeader.projectCode,
                        erpSlipHeader.projectId,
                        erpSlipHeader.taskCode,
                        erpSlipHeader.taskId,
                        erpSlipHeader.itemGroupCode,
                        erpSlipHeader.codeCombinationId,
                        erpSlipHeader.drCr,
                        erpSlipHeader.acctCode,
                        erpSlipHeader.slipDate,
                        erpSlipHeader.taxEvidenceType,
                        erpSlipHeader.slipCurrencyCode,
                        erpSlipHeader.exchangeRateType,
                        erpSlipHeader.exchangeDate,
                        erpSlipHeader.exchangeRate,
                        erpSlipHeader.enteredAmount,
                        erpSlipHeader.accountedAmount,
                        erpSlipHeader.referenceSlipModule,
                        erpSlipHeader.referenceSlipNumber,
                        erpSlipHeader.description,
                        erpSlipHeader.integrationVendorNum,
                        ExpressionUtils.as(JPAExpressions.select(vendor.integrationVendorName)
                                        .from(vendor)
                                        .where(vendor.integrationVendorNum.eq(erpSlipHeader.integrationVendorNum)
                                                .and(vendor.compCd.eq(slipHeader.compCd))),
                                "integrationVendorName"),
                        erpSlipHeader.vendorId,
                        erpSlipHeader.vendorSiteId,
                        erpSlipHeader.vendorPartyId,
                        erpSlipHeader.vendorPartySiteId,
                        erpSlipHeader.customerId,
                        erpSlipHeader.customerSiteId,
                        erpSlipHeader.customerPartyId,
                        erpSlipHeader.customerPartySiteId,
                        erpSlipHeader.termName,
                        erpSlipHeader.termId,
                        erpSlipHeader.termDueDate,
                        ExpressionUtils.as(JPAExpressions.select(poHeadersAll.segment1)
                                        .from(poHeadersAll)
                                        .where(poHeadersAll.poHeaderId.eq(
                                                JPAExpressions.select(erpSlipLine.poHeaderId.max())
                                                                .from(erpSlipLine)
                                                                .where(erpSlipLine.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
                                        )),
                                "poNumber"),
                        erpSlipHeader.prepaymentApplyFlag,
                        erpSlipHeader.paymentReceiptMethodCode,
                        erpSlipHeader.payGroupLookupCode,
                        erpSlipHeader.noteFlag,
                        erpSlipHeader.maturityDate,
                        erpSlipHeader.awtGroupId,
                        erpSlipHeader.awtLocationCode,
                        erpSlipHeader.taxLocationCode,
                        erpSlipHeader.taxIssueTypeCode,
                        erpSlipHeader.taxCode,
                        erpSlipHeader.vatTaxId,
                        erpSlipHeader.taxAcctCode,
                        erpSlipHeader.taxCodeCombinationId,
                        erpSlipHeader.evidenceVendorNum,
                        erpSlipHeader.evidenceVendorCustSiteId,
                        erpSlipHeader.evidenceDate,
                        new CaseBuilder()
                                .when(erpSlipHeader.supplyAmount.eq(BigDecimal.valueOf(0)).and(erpSlipHeader.taxAmount.eq(BigDecimal.valueOf(0)))).then(erpSlipHeader.enteredAmount)
                                .otherwise(erpSlipHeader.supplyAmount)
                                .as("supplyAmount"),
                        new CaseBuilder()
                                .when(erpSlipHeader.supplyAmount.eq(BigDecimal.valueOf(0)).and(erpSlipHeader.taxAmount.eq(BigDecimal.valueOf(0)))).then(BigDecimal.valueOf(0))
                                .otherwise(erpSlipHeader.taxAmount)
                                .as("taxAmount"),
                        erpSlipHeader.actualDeptCode,
                        erpSlipHeader.trBankAcctCode,
                        erpSlipHeader.slipTypeCode,
                        erpSlipHeader.segment9Code,
                        erpSlipHeader.segment10Code,
                        erpSlipHeader.bankAccountId,
                        erpSlipHeader.bankAccountName,
                        erpSlipHeader.sourceSlipHeaderId,
                        erpSlipHeader.slipDisplayFlag,
                        erpSlipHeader.slipCreationTargetFlag,
                        erpSlipHeader.slipStatus,
                        erpSlipHeader.slipDataFixFlag,
                        erpSlipHeader.approvalCompleteFlag,
                        erpSlipHeader.createValidationFlag,
                        erpSlipHeader.createValidationErrMsg,
                        erpSlipHeader.slipIfFlag,
                        erpSlipHeader.stdInvoiceTrxId,
                        erpSlipHeader.slipIfDate,
                        erpSlipHeader.slipIfLastApprovalUser,
                        erpSlipHeader.slipInterfaceErrorMsg,
                        erpSlipHeader.postingFlag,
                        erpSlipHeader.dueDateUpdateFlag,
                        erpSlipHeader.slipDeleteFlag,
                        erpSlipHeader.slipDeleteDate,
                        erpSlipHeader.erpSlipCancelDate,
                        erpSlipHeader.erpSlipCancelUser,
                        erpSlipHeader.attributeCategory,
                        erpSlipHeader.attribute1,
                        erpSlipHeader.attribute2,
                        erpSlipHeader.attribute3,
                        erpSlipHeader.attribute4,
                        erpSlipHeader.attribute5,
                        erpSlipHeader.attribute6,
                        erpSlipHeader.attribute7,
                        erpSlipHeader.attribute8,
                        erpSlipHeader.attribute9,
                        erpSlipHeader.attribute10,
                        erpSlipHeader.attribute11,
                        erpSlipHeader.attribute12,
                        erpSlipHeader.attribute13,
                        erpSlipHeader.attribute14,
                        erpSlipHeader.attribute15,
                        erpSlipHeader.globalAttributeCategory,
                        erpSlipHeader.globalAttribute1,
                        erpSlipHeader.globalAttribute2,
                        erpSlipHeader.globalAttribute3,
                        erpSlipHeader.globalAttribute4,
                        erpSlipHeader.globalAttribute5,
                        erpSlipHeader.globalAttribute6,
                        erpSlipHeader.globalAttribute7,
                        erpSlipHeader.globalAttribute8,
                        erpSlipHeader.globalAttribute9,
                        erpSlipHeader.globalAttribute10,
                        erpSlipHeader.globalAttribute11,
                        erpSlipHeader.globalAttribute12,
                        erpSlipHeader.globalAttribute13,
                        erpSlipHeader.globalAttribute14,
                        erpSlipHeader.globalAttribute15,
                        erpSlipHeader.headerAttribute1,
                        erpSlipHeader.headerAttribute2,
                        erpSlipHeader.headerAttribute3,
                        erpSlipHeader.headerAttribute4,
                        erpSlipHeader.headerAttribute5,
                        erpSlipHeader.headerAttribute6,
                        erpSlipHeader.headerAttribute7,
                        erpSlipHeader.headerAttribute8,
                        erpSlipHeader.headerAttribute9,
                        erpSlipHeader.headerAttribute10,
                        erpSlipHeader.createdPersonId,
                        erpSlipHeader.createdEmpNo,
                        erpSlipHeader.creationDate,
                        erpSlipHeader.lastUpdatedPersonId,
                        erpSlipHeader.lastUpdatedEmpNo,
                        erpSlipHeader.lastUpdateDate,
                        erpSlipHeader.lastUpdateLogin,
                        erpSlipHeader.vatAmtExceptionFlag,
                        new CaseBuilder()
                                .when(JPAExpressions.select(count(cboApViewPrepaysV.invoiceId))
                                                .from(cboApViewPrepaysV)
                                                .where(cboApViewPrepaysV.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId)).gt(0L))
                                    .then("C")
                                .otherwise(JPAExpressions.select(apInvoicesAll.paymentStatusFlag)
                                        .from(apInvoicesAll)
                                        .where(apInvoicesAll.orgId.eq(erpSlipHeader.orgId)
                                                .and(apInvoicesAll.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId))
                                                .and(apInvoicesAll.globalAttribute19.eq(erpSlipHeader.approvalGroupId))
                                        ))
                                .as("intStatus"),
                        approvalHeader.nextAppUserId,
                        ExpressionUtils.as(JPAExpressions.select(employee.empNm)
                                        .from(employee)
                                        .where(employee.empNo.eq(approvalHeader.nextAppUserId)
                                        ),
                                "nextAppUserNm"),
                        new CaseBuilder()
                                .when(slipHeader.status.eq("CC")).then("")
                                        /*.then(JPAExpressions.select(approvalDetail.apprTypeCd)
                                                                                        .from(approvalDetail)
                                                                                        .where(approvalDetail.compCd.eq(slipHeader.compCd),
                                                                                                approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                                                                                                approvalDetail.slipStatus.eq(slipHeader.status)))*/
                                .otherwise(JPAExpressions.select(approvalDetail.apprTypeCd)
                                        .from(approvalDetail)
                                        .where(approvalDetail.apprStage.eq(approvalHeader.nextStage),
                                                approvalDetail.compCd.eq(slipHeader.compCd),
                                                approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId)
                                        )
                                )
                                .as("apprTypeCd"),
                        new CaseBuilder()
                                .when(approvalHeader.nextAppUserId.eq("1")).then("")
                                .when(slipHeader.status.eq("CC")).then(JPAExpressions.select(approvalDetail.apprDesc)
                                                                            .from(approvalDetail)
                                                                            .where(approvalDetail.compCd.eq(slipHeader.compCd),
                                                                                    approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                                                                                    approvalDetail.slipStatus.eq(slipHeader.status)))
                                .otherwise(JPAExpressions.select(approvalDetail.apprDesc)
                                        .from(approvalDetail)
                                        .where(approvalDetail.apprStage.eq
                                                    (JPAExpressions.select(approvalDetail.apprStage.max())
                                                                .from(approvalDetail)
                                                                .where(approvalDetail.compCd.eq(slipHeader.compCd),
                                                                        approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                                                                        approvalDetail.apprStage.lt(approvalHeader.nextStage))),
                                                approvalDetail.compCd.eq(slipHeader.compCd),
                                                approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId)
                                                )
                                        )
                                .as("apprDesc"),
                    approvalHeader.draftId
                ))
                .from(slipHeader)
                .leftJoin(erpSlipHeader).on(slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
                .leftJoin(approvalHeader).on(approvalHeader.compCd.eq(slipHeader.compCd), approvalHeader.apprGroupId.eq(slipHeader.approvalGroupId))
                .where(slipHeader.compCd.eq(slipHistoryDto.getCompCd())
                        .and(slipHeader.postingDt.between(slipHistoryDto.getPostDtFrom(), slipHistoryDto.getPostDtTo()))
                        .and(regDtmBetween(slipHistoryDto.getRegDtFrom(), slipHistoryDto.getRegDtTo()))
                        //.and(deptIn(slipHistoryDto.getWrtDeptCd()))
                        // 부서가 달라져도 내 전표면 볼 수 있게 변경 (부서가 있으면서 작성자가 없을때, 혹은 작성자와 로그인 아이디가 다를때 로직 타도록 수정)
                        .and(conditionalDeptIn(slipHistoryDto.getWrtDeptCd(), slipHistoryDto.getWrtId(), loginId))
                        .and(integrationVendorNameLike(slipHistoryDto.getEvdCustNm()))
                        .and(slipTypeEq(slipHistoryDto.getSlipTypeCd()))
                        .and(empNoEq(slipHistoryDto.getWrtId()))
                        .and(statusIn(slipHistoryDto.getSlipStatCd()))
                        .and(slipNoIn(slipHistoryDto.getSlipNo()))
                        .and(headerRemarkLike(slipHistoryDto.getHdSgtxt()))
                        .and(slipHeader.status.notIn("SD","BD9","04","06"))
                        .and(nextAppUserIdEq(slipHistoryDto.getNextAppUserId()))
                        .and(slipHeader.slipType.notIn("28", "29", "30", "91"))
                        .and(returnFlagSearch(slipHistoryDto.getReturnFlag()))
                        .and(currencyEq(slipHistoryDto.getCurrency()))
                        .and(termIdEq(slipHistoryDto.getTermId()))
                        .and(inStatusEq(slipHistoryDto.getIntStatus()))
                )
                .orderBy(slipHeader.postingDt.asc(), slipHeader.regDtm.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return result;
    }

    @Override
    public List<SlipHistoryDto> getSlipHistoryExcel(SlipHistoryDto slipHistoryDto){
        String loginId = util.getLoginId();

        List<SlipHistoryDto> result = queryFactory
            .select(new QSlipHistoryDto(
                slipHeader.compCd,
                slipHeader.slipNo,
                slipHeader.deptNo,
                ExpressionUtils.as(JPAExpressions.select(costCenter.deptNm)
                        .from(costCenter)
                        .where(costCenter.deptCd.eq(slipHeader.deptNo)
                            .and(costCenter.compCd.eq(slipHeader.compCd))),
                    "deptNm"),
                slipHeader.empNo,
                ExpressionUtils.as(JPAExpressions.select(employee.empNm)
                        .from(employee)
                        .where(employee.empNo.eq(slipHeader.empNo)
                            .and(employee.compCd.eq(slipHeader.compCd))),
                    "empNm"),
                slipHeader.slipType,
                ExpressionUtils.as(JPAExpressions.select(trx.trxTypeNm)
                        .from(trx)
                        .where(trx.trxTypeCd.eq(slipHeader.slipType)
                            .and(trx.compCd.eq(slipHeader.compCd))),
                    "slipTypeNm"),
                slipHeader.slipTypeCd,
                slipHeader.status,
                ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                        .from(codeDetail)
                        .where(codeDetail.groupCd.eq("PROGRESS_STATUS_CD")
                            .and(codeDetail.compCd.eq(slipHeader.compCd))
                            .and(codeDetail.detailCd.eq(slipHeader.status))),
                    "statusNm"),
                slipHeader.postingDt,
                slipHeader.headerRemark,
                slipHeader.usedCur,
                slipHeader.usedAmt,
                slipHeader.usedForAmt,
                slipHeader.scanNo,
                slipHeader.slipHeaderId,
                slipHeader.slipGroupNo,
                slipHeader.docNo,
                slipHeader.regDtm,
                slipHeader.regId,
                slipHeader.chgDtm,
                slipHeader.chgId,
                slipHeader.slipForm,
                slipHeader.remark,
                slipHeader.approvalGroupId,
                slipHeader.taxbillSupplyAmt,
                slipHeader.taxbillTaxAmt,
                slipHeader.taxbillTotalAmt,
                slipHeader.taxSmartbillNo,
                slipHeader.ledgerId,
                slipHeader.validationFlag,
                slipHeader.errorMsg,
                slipHeader.docTitle,
                slipHeader.docUrl,
                slipHeader.erpInvoiceId,
                slipHeader.erpAppUserId,
                slipHeader.erpXtrSlipType,
                slipHeader.erpTransactionNo,
                slipHeader.taxbillAmtModifyYn,
                slipHeader.taxbillSuId,
                erpSlipHeader.invoiceTypeLookupCode,
                erpSlipHeader.batchSourceName,
                erpSlipHeader.categoryName,
                erpSlipHeader.trxTypeCode,
                erpSlipHeader.ttypeInputModule,
                erpSlipHeader.ttypeInterfaceModule,
                erpSlipHeader.ttypeInterfaceSlipType,
                erpSlipHeader.ttypePrepaymentFlag,
                erpSlipHeader.ttypeClearingAcctCode,
                erpSlipHeader.ttypeAddInfoType,
                erpSlipHeader.ttypeIntegrationVendorNum,
                erpSlipHeader.ttypePaymentReceiptTermId,
                erpSlipHeader.compCode,
                erpSlipHeader.budgetDeptCode,
                erpSlipHeader.projectCode,
                erpSlipHeader.projectId,
                erpSlipHeader.taskCode,
                erpSlipHeader.taskId,
                erpSlipHeader.itemGroupCode,
                erpSlipHeader.codeCombinationId,
                erpSlipHeader.drCr,
                erpSlipHeader.acctCode,
                erpSlipHeader.slipDate,
                erpSlipHeader.taxEvidenceType,
                erpSlipHeader.slipCurrencyCode,
                erpSlipHeader.exchangeRateType,
                erpSlipHeader.exchangeDate,
                erpSlipHeader.exchangeRate,
                erpSlipHeader.enteredAmount,
                erpSlipHeader.accountedAmount,
                erpSlipHeader.referenceSlipModule,
                erpSlipHeader.referenceSlipNumber,
                erpSlipHeader.description,
                erpSlipHeader.integrationVendorNum,
                ExpressionUtils.as(JPAExpressions.select(vendor.integrationVendorName)
                        .from(vendor)
                        .where(vendor.integrationVendorNum.eq(erpSlipHeader.integrationVendorNum)
                            .and(vendor.compCd.eq(slipHeader.compCd))),
                    "integrationVendorName"),
                erpSlipHeader.vendorId,
                erpSlipHeader.vendorSiteId,
                erpSlipHeader.vendorPartyId,
                erpSlipHeader.vendorPartySiteId,
                erpSlipHeader.customerId,
                erpSlipHeader.customerSiteId,
                erpSlipHeader.customerPartyId,
                erpSlipHeader.customerPartySiteId,
                erpSlipHeader.termName,
                erpSlipHeader.termId,
                erpSlipHeader.termDueDate,
                ExpressionUtils.as(JPAExpressions.select(poHeadersAll.segment1)
                        .from(poHeadersAll)
                        .where(poHeadersAll.poHeaderId.eq(
                            JPAExpressions.select(erpSlipLine.poHeaderId.max())
                                .from(erpSlipLine)
                                .where(erpSlipLine.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
                        )),
                    "poNumber"),
                erpSlipHeader.prepaymentApplyFlag,
                erpSlipHeader.paymentReceiptMethodCode,
                erpSlipHeader.payGroupLookupCode,
                erpSlipHeader.noteFlag,
                erpSlipHeader.maturityDate,
                erpSlipHeader.awtGroupId,
                erpSlipHeader.awtLocationCode,
                erpSlipHeader.taxLocationCode,
                erpSlipHeader.taxIssueTypeCode,
                erpSlipHeader.taxCode,
                erpSlipHeader.vatTaxId,
                erpSlipHeader.taxAcctCode,
                erpSlipHeader.taxCodeCombinationId,
                erpSlipHeader.evidenceVendorNum,
                erpSlipHeader.evidenceVendorCustSiteId,
                erpSlipHeader.evidenceDate,
                new CaseBuilder()
                    .when(erpSlipHeader.supplyAmount.eq(BigDecimal.valueOf(0)).and(erpSlipHeader.taxAmount.eq(BigDecimal.valueOf(0)))).then(erpSlipHeader.enteredAmount)
                    .otherwise(erpSlipHeader.supplyAmount)
                    .as("supplyAmount"),
                new CaseBuilder()
                    .when(erpSlipHeader.supplyAmount.eq(BigDecimal.valueOf(0)).and(erpSlipHeader.taxAmount.eq(BigDecimal.valueOf(0)))).then(BigDecimal.valueOf(0))
                    .otherwise(erpSlipHeader.taxAmount)
                    .as("taxAmount"),
                erpSlipHeader.actualDeptCode,
                erpSlipHeader.trBankAcctCode,
                erpSlipHeader.slipTypeCode,
                erpSlipHeader.segment9Code,
                erpSlipHeader.segment10Code,
                erpSlipHeader.bankAccountId,
                erpSlipHeader.bankAccountName,
                erpSlipHeader.sourceSlipHeaderId,
                erpSlipHeader.slipDisplayFlag,
                erpSlipHeader.slipCreationTargetFlag,
                erpSlipHeader.slipStatus,
                erpSlipHeader.slipDataFixFlag,
                erpSlipHeader.approvalCompleteFlag,
                erpSlipHeader.createValidationFlag,
                erpSlipHeader.createValidationErrMsg,
                erpSlipHeader.slipIfFlag,
                erpSlipHeader.stdInvoiceTrxId,
                erpSlipHeader.slipIfDate,
                erpSlipHeader.slipIfLastApprovalUser,
                erpSlipHeader.slipInterfaceErrorMsg,
                erpSlipHeader.postingFlag,
                erpSlipHeader.dueDateUpdateFlag,
                erpSlipHeader.slipDeleteFlag,
                erpSlipHeader.slipDeleteDate,
                erpSlipHeader.erpSlipCancelDate,
                erpSlipHeader.erpSlipCancelUser,
                erpSlipHeader.attributeCategory,
                erpSlipHeader.attribute1,
                erpSlipHeader.attribute2,
                erpSlipHeader.attribute3,
                erpSlipHeader.attribute4,
                erpSlipHeader.attribute5,
                erpSlipHeader.attribute6,
                erpSlipHeader.attribute7,
                erpSlipHeader.attribute8,
                erpSlipHeader.attribute9,
                erpSlipHeader.attribute10,
                erpSlipHeader.attribute11,
                erpSlipHeader.attribute12,
                erpSlipHeader.attribute13,
                erpSlipHeader.attribute14,
                erpSlipHeader.attribute15,
                erpSlipHeader.globalAttributeCategory,
                erpSlipHeader.globalAttribute1,
                erpSlipHeader.globalAttribute2,
                erpSlipHeader.globalAttribute3,
                erpSlipHeader.globalAttribute4,
                erpSlipHeader.globalAttribute5,
                erpSlipHeader.globalAttribute6,
                erpSlipHeader.globalAttribute7,
                erpSlipHeader.globalAttribute8,
                erpSlipHeader.globalAttribute9,
                erpSlipHeader.globalAttribute10,
                erpSlipHeader.globalAttribute11,
                erpSlipHeader.globalAttribute12,
                erpSlipHeader.globalAttribute13,
                erpSlipHeader.globalAttribute14,
                erpSlipHeader.globalAttribute15,
                erpSlipHeader.headerAttribute1,
                erpSlipHeader.headerAttribute2,
                erpSlipHeader.headerAttribute3,
                erpSlipHeader.headerAttribute4,
                erpSlipHeader.headerAttribute5,
                erpSlipHeader.headerAttribute6,
                erpSlipHeader.headerAttribute7,
                erpSlipHeader.headerAttribute8,
                erpSlipHeader.headerAttribute9,
                erpSlipHeader.headerAttribute10,
                erpSlipHeader.createdPersonId,
                erpSlipHeader.createdEmpNo,
                erpSlipHeader.creationDate,
                erpSlipHeader.lastUpdatedPersonId,
                erpSlipHeader.lastUpdatedEmpNo,
                erpSlipHeader.lastUpdateDate,
                erpSlipHeader.lastUpdateLogin,
                erpSlipHeader.vatAmtExceptionFlag,
                new CaseBuilder()
                    .when(JPAExpressions.select(count(cboApViewPrepaysV.invoiceId))
                        .from(cboApViewPrepaysV)
                        .where(cboApViewPrepaysV.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId)).gt(0L))
                    .then("C")
                    .otherwise(JPAExpressions.select(apInvoicesAll.paymentStatusFlag)
                        .from(apInvoicesAll)
                        .where(apInvoicesAll.orgId.eq(erpSlipHeader.orgId)
                            .and(apInvoicesAll.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId))
                            .and(apInvoicesAll.globalAttribute19.eq(erpSlipHeader.approvalGroupId))
                        ))
                    .as("intStatus"),
                approvalHeader.nextAppUserId,
                ExpressionUtils.as(JPAExpressions.select(employee.empNm)
                        .from(employee)
                        .where(employee.empNo.eq(approvalHeader.nextAppUserId)
                        ),
                    "nextAppUserNm"),
                new CaseBuilder()
                    .when(slipHeader.status.eq("CC")).then("")
                    /*.then(JPAExpressions.select(approvalDetail.apprTypeCd)
                                                                    .from(approvalDetail)
                                                                    .where(approvalDetail.compCd.eq(slipHeader.compCd),
                                                                            approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                                                                            approvalDetail.slipStatus.eq(slipHeader.status)))*/
                    .otherwise(JPAExpressions.select(approvalDetail.apprTypeCd)
                        .from(approvalDetail)
                        .where(approvalDetail.apprStage.eq(approvalHeader.nextStage),
                            approvalDetail.compCd.eq(slipHeader.compCd),
                            approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId)
                        )
                    )
                    .as("apprTypeCd"),
                new CaseBuilder()
                    .when(approvalHeader.nextAppUserId.eq("1")).then("")
                    .when(slipHeader.status.eq("CC")).then(JPAExpressions.select(approvalDetail.apprDesc)
                        .from(approvalDetail)
                        .where(approvalDetail.compCd.eq(slipHeader.compCd),
                            approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                            approvalDetail.slipStatus.eq(slipHeader.status)))
                    .otherwise(JPAExpressions.select(approvalDetail.apprDesc)
                        .from(approvalDetail)
                        .where(approvalDetail.apprStage.eq
                                (JPAExpressions.select(approvalDetail.apprStage.max())
                                    .from(approvalDetail)
                                    .where(approvalDetail.compCd.eq(slipHeader.compCd),
                                        approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId),
                                        approvalDetail.apprStage.lt(approvalHeader.nextStage))),
                            approvalDetail.compCd.eq(slipHeader.compCd),
                            approvalDetail.apprGroupId.eq(slipHeader.approvalGroupId)
                        )
                    )
                    .as("apprDesc"),
                approvalHeader.draftId
            ))
            .from(slipHeader)
            .leftJoin(erpSlipHeader).on(slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
            .leftJoin(approvalHeader).on(approvalHeader.compCd.eq(slipHeader.compCd), approvalHeader.apprGroupId.eq(slipHeader.approvalGroupId))
            .where(slipHeader.compCd.eq(slipHistoryDto.getCompCd())
                .and(slipHeader.postingDt.between(slipHistoryDto.getPostDtFrom(), slipHistoryDto.getPostDtTo()))
                .and(regDtmBetween(slipHistoryDto.getRegDtFrom(), slipHistoryDto.getRegDtTo()))
                //.and(deptIn(slipHistoryDto.getWrtDeptCd()))
                // 부서가 달라져도 내 전표면 볼 수 있게 변경 (부서가 있으면서 작성자가 없을때, 혹은 작성자와 로그인 아이디가 다를때 로직 타도록 수정)
                .and(conditionalDeptIn(slipHistoryDto.getWrtDeptCd(), slipHistoryDto.getWrtId(), loginId))
                .and(integrationVendorNameLike(slipHistoryDto.getEvdCustNm()))
                .and(slipTypeEq(slipHistoryDto.getSlipTypeCd()))
                .and(empNoEq(slipHistoryDto.getWrtId()))
                .and(statusIn(slipHistoryDto.getSlipStatCd()))
                .and(slipNoIn(slipHistoryDto.getSlipNo()))
                .and(headerRemarkLike(slipHistoryDto.getHdSgtxt()))
                .and(slipHeader.status.notIn("SD","BD9","04","06"))
                .and(nextAppUserIdEq(slipHistoryDto.getNextAppUserId()))
                .and(slipHeader.slipType.notIn("28", "29", "30", "91"))
                .and(returnFlagSearch(slipHistoryDto.getReturnFlag()))
                .and(currencyEq(slipHistoryDto.getCurrency()))
                .and(termIdEq(slipHistoryDto.getTermId()))
                .and(inStatusEq(slipHistoryDto.getIntStatus()))
            )
            .orderBy(slipHeader.postingDt.asc(), slipHeader.regDtm.asc())
            .fetch();

        return result;
    }

    @Override
    public List<SlipHeaderDto> getSlipStatusGroup(String compCd, BigDecimal approvalGroupId){
        return queryFactory
                .select(new QSlipHeaderDto(
                        slipHeader.slipHeaderId,
                        slipHeader.approvalGroupId,
                        slipHeader.compCd,
                        slipHeader.slipType,
                        slipHeader.ledgerId,
                        erpSlipHeader.slipIfFlag.coalesce("N").as("slipIfFlag")
                ))
                .from(slipHeader)
                .leftJoin(erpSlipHeader).on(slipHeader.compCd.eq(String.valueOf(erpSlipHeader.orgId)) , slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId) )
                .where(slipHeader.compCd.eq(compCd), slipHeader.approvalGroupId.eq(approvalGroupId))
                .fetch();
    }

    @Override
    public List<SlipHeaderDto> getSlipStatus(String compCd, BigDecimal approvalGroupId){
        return queryFactory
                .select(new QSlipHeaderDto(
                        slipHeader.approvalGroupId,
                        slipHeader.compCd,
                        slipHeader.slipType,
                        slipHeader.ledgerId,
                        slipHeader.status,
                        slipHeader.slipNo,
                        slipHeader.taxSmartbillNo
                ))
                .from(slipHeader)
                .where(slipHeader.compCd.eq(compCd), slipHeader.approvalGroupId.eq(approvalGroupId))
                .fetch();
    }

    @Override
    public List<BondHeaderDto> getBondSlipHeader(String compCd, String slipNo){
        QBondHeader bondHeader1 = new QBondHeader("bondHeader1");
        QSlipHeader slipHeader1 = new QSlipHeader("slipHeader1");

        return queryFactory
                .select(new QBondHeaderDto(
                        bondHeader.splitEtcYn.coalesce("N").as("splitEtcYn"),
                        bondMst.bondCd,
                        bondHeader.type,
                        bondMst.benCountryCd,
                        bondMst.benCountry,
                        bondMst.refNo,
                        bondMst.projectNm,
                        bondMst.projectId,
                        bondMst.customerNm,
                        bondMst.customerId,
                        bondMst.openingDt,
                        bondMst.currencyCd,
                        Expressions.asString("").as("currencyNm"),
                        bondHeader.guaranteeAmt.coalesce(BigDecimal.valueOf(0)).as("guaranteeAmt"),
                        bondHeader.maturityDt,
                        bondHeader.rate.coalesce(BigDecimal.valueOf(0)).as("rate"),
                        bondMst.localBankNm,
                        bondMst.localBankId,
                        bondMst.intBankNm,
                        bondMst.budget.coalesce(BigDecimal.valueOf(0)).as("budget"),
                        bondHeader.amendSeq,
                        ExpressionUtils.as(JPAExpressions.select(bondHeader1.refNo.count())
                                        .from(slipHeader1, bondHeader1)
                                        .where(slipHeader1.compCd.eq(bondHeader1.compCd)
                                                .and(slipHeader1.slipNo.eq(bondHeader1.slipNo))
                                                .and(slipHeader1.status.in("CC", "IC", "IE", "BD9", "04"))
                                                .and(bondHeader1.type.eq("LOCAL"))
                                                .and(bondHeader1.refNo.eq(bondHeader.refNo))
                                                .and(slipHeader1.slipHeaderId.ne(slipHeader.slipHeaderId))
                                        ),
                                "amendCount"),
                        bondMst.releaseDt
                ))
                .from(bondHeader)
                .leftJoin(bondMst).on(bondMst.refNo.eq(bondHeader.refNo), bondMst.compCd.eq(bondHeader.compCd))
                .leftJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipHeaderId.eq(bondHeader.slipHeaderId),
                        slipHeader.slipNo.eq(bondHeader.slipNo))
                .where(bondHeader.compCd.eq(compCd),
                        bondHeader.slipNo.eq(slipNo)
                ).fetch();
    }

    @Override
    public List<CoaHierarchyDto> getControlledAccount(){

        return queryFactory
                .select(new QCoaHierarchyDto(
                        coaHierarchy.flexValue,
                        coaHierarchy.flexValueDesc,
                        coaHierarchy.acctType,
                        coaHierarchy.coaId,
                        coaHierarchy.segmentNum,
                        coaHierarchy.segmentName,
                        coaHierarchy.applicationColumnName,
                        coaHierarchy.startDateActive,
                        coaHierarchy.endDateActive,
                        coaHierarchy.enabledFlag,
                        coaHierarchy.hierarchyCode,
                        coaHierarchy.parentFlexValue,
                        coaHierarchy.hierarchyLevel
                ))
                .from(coaHierarchy)
                .where(coaHierarchy.parentFlexValue.in("B550814", "B550813")
                        .and(coaHierarchy.enabledFlag.eq("Y")))
                .fetch();
    }


    private BooleanExpression regDtmBetween(String from, String to) {
        if(hasText(from) && hasText(to)){
            LocalDateTime localFrom = LocalDateTime.of(Integer.parseInt(from.substring(0, 4)), Integer.parseInt(from.substring(4, 6)), Integer.parseInt(from.substring(6, 8)), 0, 0, 0);
            LocalDateTime localTo = LocalDateTime.of(Integer.parseInt(to.substring(0, 4)), Integer.parseInt(to.substring(4, 6)), Integer.parseInt(to.substring(6, 8)), 0, 0, 0).plusDays(1L);
            return slipHeader.regDtm.between(localFrom, localTo);
        }else{
            return null;
        }
    }

    private BooleanExpression deptIn(String deptNo) {
        if(hasText(deptNo)){
            String [] depts = deptNo.split(",");
            return slipHeader.deptNo.in(depts);
        }else {
            return null;
        }
    }

    private BooleanExpression conditionalDeptIn(String wrtDeptCd, String wrtId, String loginId) { // 본인의 전표는 볼 수 있게 변경
        if (wrtId == null || !wrtId.equals(loginId)) {
            return deptIn(wrtDeptCd);
        }
        return null; // wrtId와 loginId가 같으면 조건을 적용하지 않음
    }

    private BooleanExpression slipTypeCdEq(String slipTypeCd) {
        return hasText(slipTypeCd) ? slipHeader.slipTypeCd.eq(slipTypeCd) : null;
    }

    private BooleanExpression slipTypeEq(String slipType) {
        return hasText(slipType) ? slipHeader.slipType.eq(slipType) : null;
    }

    private BooleanExpression empNoEq(String empNo) {
        return hasText(empNo) ? slipHeader.empNo.eq(empNo) : null;
    }

    private BooleanExpression statusEq(String status) {
        return hasText(status) ? slipHeader.status.eq(status) : null;
    }

    private BooleanExpression statusIn(String status) {
        if(hasText(status)){
            String [] arr = status.split(",");
            return slipHeader.status.in(arr);
        }else {
            return null;
        }
    }

    private BooleanExpression slipNoIn(String slipNo) {
        return hasText(slipNo) ? slipHeader.slipNo.containsIgnoreCase(slipNo) : null;
    }

    private BooleanExpression headerRemarkLike(String headerRemark) {
        return hasText(headerRemark) ? slipHeader.headerRemark.containsIgnoreCase(headerRemark) : null;
    }
    private BooleanExpression integrationVendorNameLike(String integrationVendorName){
        if(hasText(integrationVendorName)){
            return erpSlipHeader.integrationVendorNum.in(JPAExpressions.select(vendor.integrationVendorNum)
                                                .from(vendor)
                                                .where(vendor.integrationVendorName.containsIgnoreCase(integrationVendorName)));
        }else{
            return null;
        }
    }

    private BooleanExpression nextAppUserIdEq(String nextAppUserId){
        return hasText(nextAppUserId) ? approvalHeader.nextAppUserId.eq(nextAppUserId) : null;
    }

    private BooleanExpression returnFlagSearch(Boolean returnFlag){

        if(Objects.nonNull(returnFlag)){
            if(returnFlag){
                return slipHeader.status.in("AR","CR","FR","SC");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    private BooleanExpression currencyEq(String currency){
        return hasText(currency) ? slipHeader.usedCur.eq(currency) : null;
    }

    private BooleanExpression termIdEq(BigDecimal termId){
        if(termId != null) {
            return erpSlipHeader.termId.eq(Integer.valueOf(String.valueOf(termId)));
        }else{
            return null;
        }
    }

    private BooleanExpression inStatusEq(String inStatus){
        if(hasText(inStatus)){
            if(inStatus.equals("C")){
                return (JPAExpressions.select(count(cboApViewPrepaysV.invoiceId))
                        .from(cboApViewPrepaysV)
                        .where(cboApViewPrepaysV.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId)).gt(0L));
            }else if(inStatus.equals("Y") || inStatus.equals("N")){
                return (JPAExpressions.select(count(apInvoicesAll.paymentStatusFlag))
                        .from(apInvoicesAll)
                        .where(apInvoicesAll.orgId.eq(erpSlipHeader.orgId)
                                .and(apInvoicesAll.invoiceId.eq(erpSlipHeader.stdInvoiceTrxId))
                                .and(apInvoicesAll.globalAttribute19.eq(erpSlipHeader.approvalGroupId))
                                .and(apInvoicesAll.paymentStatusFlag.eq(inStatus))
                        ).eq(1L));
            }
        }

        return null;
    }
}

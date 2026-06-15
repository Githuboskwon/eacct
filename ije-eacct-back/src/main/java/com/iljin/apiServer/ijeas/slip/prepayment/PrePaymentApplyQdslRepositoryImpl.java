package com.iljin.apiServer.ijeas.slip.prepayment;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


import static com.iljin.apiServer.ijeas.slip.history.QApInvoicesAll.apInvoicesAll;
import static com.iljin.apiServer.ijeas.slip.history.QApInvoiceLinesAll.apInvoiceLinesAll;
import static com.iljin.apiServer.ijeas.slip.prepayment.QApplyPrepays.applyPrepays;
import static com.iljin.apiServer.ijeas.slip.prepayment.QPrepaymentApply.prepaymentApply;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class PrePaymentApplyQdslRepositoryImpl implements PrePaymentApplyQdslRepository {

    private final Util util;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ApplyPrepaysDto> getAdvancedList(ErpSlipRequestDto erpSlipRequestDto) {
        String compCd = util.getLoginCompCd();

        BigDecimal vendorCd = BigDecimal.valueOf(Integer.parseInt(erpSlipRequestDto.getVendorCd()));
        BigDecimal vendorSiteId = BigDecimal.valueOf(Integer.parseInt(erpSlipRequestDto.getVendorSiteId()));

        return queryFactory
                .select(new QApplyPrepaysDto(
                        applyPrepays.rowId,
                        applyPrepays.invoiceId,
                        applyPrepays.prepayLineNumber,
                        applyPrepays.acctCode,
                        applyPrepays.acctDesc,
                        applyPrepays.prepayAmountRemainingErp,
                        applyPrepays.prepayAmountRemaining,
                        applyPrepays.lineAmount,
                        applyPrepays.accountingDate,
                        applyPrepays.periodName,
                        applyPrepays.setOfBooksId,
                        applyPrepays.description,
                        applyPrepays.poLineLocationId,
                        applyPrepays.poDistributionId,
                        applyPrepays.rcvTransactionId,
                        applyPrepays.orgId,
                        applyPrepays.prepayInvoiceNumber,
                        applyPrepays.invoiceDate,
                        applyPrepays.invoiceAmount,
                        applyPrepays.integrationVendorNum,
                        applyPrepays.vendorId,
                        applyPrepays.vendorSiteId,
                        applyPrepays.invoiceCurrencyCode,
                        applyPrepays.paymentCurrencyCode,
                        applyPrepays.paymentCrossRate,
                        applyPrepays.poHeaderId,
                        applyPrepays.poNumber,
                        applyPrepays.vendorName,
                        applyPrepays.vendorNumber,
                        applyPrepays.vendorSiteCode,
                        applyPrepays.receiptNumber,
                        applyPrepays.earliestSettlementDate,
                        applyPrepays.source,
                        applyPrepays.taxRateCode
                ))
                .from(applyPrepays)
                .where(applyPrepays.orgId.eq(Integer.parseInt(compCd)),
                        applyPrepays.vendorId.eq(vendorCd),
                        applyPrepays.vendorSiteId.eq(vendorSiteId),
                        applyPrepays.paymentCurrencyCode.eq(erpSlipRequestDto.getCurrencyCd())
                )
                .fetch();
    }

    @Override
    public List<PrepaymentApplyDto> getReimbursementList(ErpSlipRequestDto erpSlipRequestDto) {
        String compCd = util.getLoginCompCd();

        BigDecimal vendorCd = BigDecimal.valueOf(Integer.parseInt(erpSlipRequestDto.getVendorCd()));
        BigDecimal vendorSiteId = BigDecimal.valueOf(Integer.parseInt(erpSlipRequestDto.getVendorSiteId()));

        return queryFactory
                .select(new QPrepaymentApplyDto(
                        prepaymentApply.slipHeaderId,
                        prepaymentApply.prepaymentApplyId,
                        prepaymentApply.orgId,
                        prepaymentApply.ledgerId,
                        prepaymentApply.prepayInvoiceId,
                        prepaymentApply.prepayLineNumber,
                        prepaymentApply.prepayAmountRemaining,
                        prepaymentApply.prepayAmount,
                        prepaymentApply.prepayAmountRemaining.subtract(prepaymentApply.amountToApply).as("afterAmt"),
                        prepaymentApply.amountToApply,
                        prepaymentApply.applyGlDate,
                        prepaymentApply.vendorId,
                        prepaymentApply.vendorSiteId,
                        prepaymentApply.invoiceCurrencyCode,
                        prepaymentApply.prepayCancelledFlag,
                        prepaymentApply.prepayCancelledAmount,
                        prepaymentApply.prepayCancelledDate,
                        prepaymentApply.prepayCancelledPersonId,
                        prepaymentApply.applyProcessFlag,
                        prepaymentApply.applyProcessDate,
                        prepaymentApply.applyProcessError,
                        prepaymentApply.attribute1,
                        prepaymentApply.attribute2,
                        prepaymentApply.attribute3,
                        prepaymentApply.attribute4,
                        prepaymentApply.attribute5,
                        prepaymentApply.attribute6,
                        prepaymentApply.attribute7,
                        prepaymentApply.attribute8,
                        prepaymentApply.attribute9,
                        prepaymentApply.attribute10,
                        prepaymentApply.createdPersonId,
                        prepaymentApply.creationDate,
                        prepaymentApply.lastUpdatedPersonId,
                        prepaymentApply.lastUpdateDate,
                        prepaymentApply.lastUpdateLogin,
                        apInvoicesAll.invoiceNum,
                        apInvoiceLinesAll.description
                ))
                .from(prepaymentApply)
                .leftJoin(apInvoicesAll).on(prepaymentApply.prepayInvoiceId.eq(apInvoicesAll.invoiceId))
                .leftJoin(apInvoiceLinesAll).on(prepaymentApply.prepayInvoiceId.eq(apInvoiceLinesAll.invoiceId)
                                                .and(prepaymentApply.prepayLineNumber.eq(apInvoiceLinesAll.lineNumber)))
                .where(prepaymentApply.orgId.eq(Integer.parseInt(compCd)),
                        prepaymentApply.vendorId.eq(vendorCd),
                        prepaymentApply.vendorSiteId.eq(vendorSiteId),
                        prepaymentApply.invoiceCurrencyCode.eq(erpSlipRequestDto.getCurrencyCd()),
                        prepaymentApply.slipHeaderId.eq(erpSlipRequestDto.getSlipHeaderId()),
                        prepaymentApply.prepayCancelledFlag.eq("N")
                )
                .orderBy(prepaymentApply.prepaymentApplyId.asc())
                .fetch();
    }

    private BooleanExpression vendorIdEq(String vendorId) {
        return hasText(vendorId) ? applyPrepays.vendorId.eq(BigDecimal.valueOf(Integer.parseInt(vendorId))) : null;
    }

    private BooleanExpression vendorSiteIdEq(String vendorSiteId) {
        return hasText(vendorSiteId) ? applyPrepays.vendorSiteId.eq(BigDecimal.valueOf(Integer.parseInt(vendorSiteId))) : null;
    }

    private BooleanExpression paymentCurrencyCodeEq(String currencyCd) {
        return hasText(currencyCd) ? applyPrepays.paymentCurrencyCode.eq(currencyCd) : null;
    }

}

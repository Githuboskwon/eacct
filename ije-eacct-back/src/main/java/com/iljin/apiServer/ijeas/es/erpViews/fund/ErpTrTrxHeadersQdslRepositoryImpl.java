package com.iljin.apiServer.ijeas.es.erpViews.fund;

import com.iljin.apiServer.ijeas.es.fund.ErpFundSlipDto;
import com.iljin.apiServer.ijeas.es.fund.QErpFundSlipDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.fund.QSpTrFundHd.spTrFundHd;
import static com.iljin.apiServer.ijeas.es.erpViews.fund.QErpTrTrxHeaders.erpTrTrxHeaders;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ErpTrTrxHeadersQdslRepositoryImpl implements ErpTrTrxHeadersQdslRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpTrTrxHeadersDto> pullErpTrTransactions(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .select(new QErpTrTrxHeadersDto(
                    erpTrTrxHeaders.xtrSlipType,
                    erpTrTrxHeaders.orgId,
                    erpTrTrxHeaders.batchId,
                    erpTrTrxHeaders.dealNumber,
                    erpTrTrxHeaders.transactionNumber,
                    erpTrTrxHeaders.createdBy,
                    erpTrTrxHeaders.dealType,
                    erpTrTrxHeaders.productType,
                    erpTrTrxHeaders.originTrxNumber,
                    erpTrTrxHeaders.dealSubtype,
                    erpTrTrxHeaders.dailyRate,
                    erpTrTrxHeaders.journalDate,
                    erpTrTrxHeaders.transactionRate,
                    erpTrTrxHeaders.transactionType,
                    erpTrTrxHeaders.description,
                    erpTrTrxHeaders.currencyCodeHeader,
                    erpTrTrxHeaders.debitAmount,
                    erpTrTrxHeaders.creditAmount,
                    erpTrTrxHeaders.accountedDr,
                    erpTrTrxHeaders.accountedCr,
                    erpTrTrxHeaders.amountType,
                    erpTrTrxHeaders.trxUniqueId,
                    erpTrTrxHeaders.trxUniqueNumber,
                    erpTrTrxHeaders.buyAmount,
                    erpTrTrxHeaders.sellAmount,
                    erpTrTrxHeaders.profitAmount,
                    erpTrTrxHeaders.slipAmount,
                    erpTrTrxHeaders.attribute1,
                    erpTrTrxHeaders.attribute2,
                    erpTrTrxHeaders.attribute3,
                    erpTrTrxHeaders.attribute4,
                    erpTrTrxHeaders.attribute5
            ))
            .from(erpTrTrxHeaders)
            .where(erpTrTrxHeaders.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                    JPAExpressions.selectFrom(slipHeader).where(slipHeader.slipType.eq(slipTypeCd),
                            slipHeader.erpInvoiceId.eq(String.valueOf(erpTrTrxHeaders.trxUniqueId)),
                            slipHeader.erpXtrSlipType.eq(erpTrTrxHeaders.xtrSlipType)
//                            erpTransactionNoEq(erpTrTrxHeaders.transactionNumber)
                    ).notExists(),
                    erpTrTrxHeaders.journalDate.loe(erpSlipRequestDto.getSearchTo()),
                    erpTrTrxHeaders.journalDate.goe(erpSlipRequestDto.getSearchFrom())
            )
            .fetch();
    }

    @Override
    public List<ErpFundSlipDto> getErpFundSlipList(ErpSlipRequestDto searchDto) {
        List<ErpFundSlipDto> erpFundSlipDtos =
                queryFactory
                .select(new QErpFundSlipDto(
                        spTrFundHd.compCd,
                        spTrFundHd.slipNo,
                        spTrFundHd.slipType,
                        spTrFundHd.slipHeaderId,
                        spTrFundHd.xtrSlipType,
                        spTrFundHd.batchId,
                        spTrFundHd.dealNum,
                        spTrFundHd.transactionNum,
                        spTrFundHd.dealType,
                        spTrFundHd.productType,
                        spTrFundHd.originTrxNum,
                        spTrFundHd.dealSubtype,
                        spTrFundHd.journalDt,
                        spTrFundHd.transactionRate,
                        spTrFundHd.description,
                        spTrFundHd.currencyCdHeader,
                        spTrFundHd.buyAmt,
                        spTrFundHd.sellAmt,
                        spTrFundHd.profitAmt,
                        spTrFundHd.slipAmt,
                        spTrFundHd.amountType,
                        spTrFundHd.transactionType,
                        spTrFundHd.dailyRate,
                        spTrFundHd.attribute1,
                        spTrFundHd.attribute2,
                        spTrFundHd.attribute3,
                        spTrFundHd.attribute4,
                        spTrFundHd.attribute5,
                        employee.empNm.as("chgNm"),
                        JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(spTrFundHd.slipNo)),
                        slipHeader.docTitle
                ))
                .from(spTrFundHd)
                .leftJoin(employee).on(spTrFundHd.compCd.eq(employee.compCd), spTrFundHd.chgId.eq(employee.empNo))
                .leftJoin(slipHeader).on(slipHeader.compCd.eq(spTrFundHd.compCd),slipHeader.slipNo.eq(spTrFundHd.slipNo))
                .where(spTrFundHd.compCd.eq(searchDto.getCompCd()),
                erpRegIdEq(searchDto.getTransferredBy()),
                dealNumEq(searchDto.getDealNum()),
                dealTypeEq(searchDto.getDealType()),
                productTypeEq(searchDto.getProductType()),
                currencyCdEq(searchDto.getCurrencyCd()),
                journalDtFromGoe(searchDto.getSearchFrom()),
                journalDtToLoe(searchDto.getSearchTo()),
                slipHeader.status.eq("SV")
                )
                .fetch();

        for(ErpFundSlipDto erpFundSlipDto: erpFundSlipDtos) {
            Long count = 0L;
            if(hasText(erpFundSlipDto.getDocTitle())) {
                count = Long.valueOf(erpFundSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpFundSlipDto.setJiniCnt(count);
        }
        return erpFundSlipDtos;
    }


    private BooleanExpression erpTransactionNoEq(StringPath erpTransactionNo) {
        return nonNull(erpTransactionNo) ? slipHeader.erpTransactionNo.eq(erpTransactionNo) : null;
    }

    private BooleanExpression journalDtFromGoe(LocalDateTime journalDt) {
        return nonNull(journalDt) ? spTrFundHd.journalDt.goe(journalDt) : null;
    }

    private BooleanExpression journalDtToLoe(LocalDateTime journalDt) {
        return nonNull(journalDt) ? spTrFundHd.journalDt.loe(journalDt) : null;
    }

    private BooleanExpression erpRegIdEq(String erpRegId) {
        return hasText(erpRegId) ? spTrFundHd.erpRegId.eq(erpRegId) : null;
    }

    private BooleanExpression dealNumEq(String dealNum) {
        return hasText(dealNum) ? spTrFundHd.dealNum.eq(dealNum) : null;
    }

    private BooleanExpression dealTypeEq(String dealType) {
        return hasText(dealType) ? spTrFundHd.dealType.eq(dealType) : null;
    }

    private BooleanExpression productTypeEq(String productType) {
        return hasText(productType) ? spTrFundHd.productType.eq(productType) : null;
    }

    private BooleanExpression currencyCdEq(String currencyCd) {
        return hasText(currencyCd) ? spTrFundHd.currencyCdHeader.eq(currencyCd) : null;
    }

}

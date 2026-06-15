package com.iljin.apiServer.ijeas.es.erpViews.fund;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.fund.QErpTrTransactions.erpTrTransactions;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Repository
public class ErpTrTransactionsQdslRepositoryImpl implements ErpTrTransactionsQdslRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpTrTransactionsDto> getErpTrTransactionsList(ErpTrTrxHeadersDto erpTrTrxHeadersDto) {
        return queryFactory
            .select(new QErpTrTransactionsDto(
                    erpTrTransactions.xtrSlipType,
                    erpTrTransactions.orgId,
                    erpTrTransactions.batchId,
                    erpTrTransactions.dealNumber,
                    erpTrTransactions.transactionNumber,
                    erpTrTransactions.createdBy,
                    erpTrTransactions.dealType,
                    erpTrTransactions.originTrxNumber,
                    erpTrTransactions.dealSubtype,
                    erpTrTransactions.productType,
                    erpTrTransactions.amountType,
                    erpTrTransactions.transactionType,
                    erpTrTransactions.currencyCodeHeader,
                    erpTrTransactions.currencyCode,
                    erpTrTransactions.dailyRate,
                    erpTrTransactions.transactionRate,
                    erpTrTransactions.journalDate,
                    erpTrTransactions.segment1_2,
                    erpTrTransactions.description,
                    erpTrTransactions.debitAmount,
                    erpTrTransactions.creditAmount,
                    erpTrTransactions.accountedDr,
                    erpTrTransactions.accountedCr,
                    erpTrTransactions.bankName,
                    erpTrTransactions.accountNo,
                    erpTrTransactions.currencyCodeLine,
                    erpTrTransactions.attribute1,
                    erpTrTransactions.attribute2,
                    erpTrTransactions.attribute3,
                    erpTrTransactions.attribute4,
                    erpTrTransactions.attribute5,
                    erpTrTransactions.trxUniqueId,
                    erpTrTransactions.trxUniqueNumber
            ))
            .from(erpTrTransactions)
            .where(erpTrTransactions.orgId.eq(erpTrTrxHeadersDto.getOrgId()),
                      erpTrTransactions.trxUniqueNumber.eq(erpTrTrxHeadersDto.getTrxUniqueNumber()),
                      erpTrTransactions.xtrSlipType.eq(erpTrTrxHeadersDto.getXtrSlipType()),
                      erpTrTransactions.dealNumber.eq(erpTrTrxHeadersDto.getDealNumber())
//                    erpTrTransactions.journalDate.eq(erpTrTrxHeadersDto.getJournalDate()),
//                    erpTrTransactions.transactionNumber.eq(erpTrTrxHeadersDto.getTransactionNumber())
//                    journalDateEq(erpTrTrxHeadersDto.getJournalDate()),
//                    dealNumberEq(erpTrTrxHeadersDto.getDealNumber()),
//                    transactionNumberEq(erpTrTrxHeadersDto.getTransactionNumber())
//                    descriptionEq(erpTrTrxHeadersDto.getDescription())
            )
            .fetch();
    }

    private BooleanExpression journalDateEq(LocalDateTime journalDate) {
        return nonNull(journalDate) ? erpTrTransactions.journalDate.eq(journalDate) : null;
    }

    private BooleanExpression dealNumberEq(String dealNumber) {
        return nonNull(dealNumber) ? erpTrTransactions.dealNumber.eq(dealNumber) : null;
    }

    private BooleanExpression transactionNumberEq(String transactionNumber) {
        return nonNull(transactionNumber) ? erpTrTransactions.transactionNumber.eq(transactionNumber) : null;
    }

    private BooleanExpression descriptionEq(String description) {
        return nonNull(description) ? erpTrTransactions.description.eq(description) : null;
    }

}

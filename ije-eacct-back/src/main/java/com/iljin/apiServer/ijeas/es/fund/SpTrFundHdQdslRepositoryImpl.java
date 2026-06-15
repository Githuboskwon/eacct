package com.iljin.apiServer.ijeas.es.fund;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.fund.QSpTrFundHd.spTrFundHd;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;

@RequiredArgsConstructor
@Repository
public class SpTrFundHdQdslRepositoryImpl implements SpTrFundHdQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpFundSlipDto> getErpFundSlipDetail(ErpSlipRequestDto requestDto) {
        return queryFactory
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
                spTrFundHd.chgId,
                employee.empNm.as("chgNm"),
                costCenter.deptCd,
                costCenter.deptNm,
                spTrFundHd.erpRegId,
                slipHeader.status,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(spTrFundHd.slipNo)),
                JPAExpressions.select(jini.id.count()).from(jini).where(jini.documentId.eq(spTrFundHd.slipNo))
        ))
        .from(spTrFundHd)
        .leftJoin(employee).on(spTrFundHd.compCd.eq(employee.compCd), spTrFundHd.chgId.eq(employee.empNo))
        .leftJoin(costCenter).on(employee.compCd.eq(costCenter.compCd) , employee.cctrCd.eq(costCenter.deptCd))
        .leftJoin(slipHeader).on(slipHeader.slipNo.eq(spTrFundHd.slipNo) , slipHeader.compCd.eq(spTrFundHd.compCd))
        .where(spTrFundHd.compCd.eq(requestDto.getCompCd()), spTrFundHd.slipNo.eq(requestDto.getSlipNo())
        )
        .fetch();
    }

}

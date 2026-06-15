package com.iljin.apiServer.ijeas.es.bulk;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.bulk.QApPaymentsHd.apPaymentsHd;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ApPaymentsHdQdslRepositoryImpl implements ApPaymentsHdQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final Util util;

    @Override
    public List<ErpBulkSlipDto> getErpBulkSlipList(ErpSlipRequestDto searchDto) {

        String compCd = util.getLoginCompCd();

        List<ErpBulkSlipDto> erpBulkSlipDtos =
             queryFactory
                .select(new QErpBulkSlipDto(
                        apPaymentsHd.compCd,
                        apPaymentsHd.eslipTransferBatchId,
                        apPaymentsHd.bankNm,
                        apPaymentsHd.bankAcctNum,
                        apPaymentsHd.checkDt,
                        slipHeader.usedCur,
                        slipHeader.usedForAmt,
                        slipHeader.usedAmt,
                        apPaymentsHd.paymentMethodLookupCode,
                        apPaymentsHd.slipNo,
                        apPaymentsHd.slipType,
                        apPaymentsHd.slipHeaderId,
                        JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(apPaymentsHd.slipNo)),
                        slipHeader.docTitle
                ))
                .from(apPaymentsHd)
                .leftJoin(slipHeader).on(slipHeader.slipNo.eq(apPaymentsHd.slipNo),slipHeader.compCd.eq(apPaymentsHd.compCd))
                .where(apPaymentsHd.compCd.eq(compCd),
                        checkDtFromGoe(searchDto.getSearchFrom()),
                        checkDtToLoe(searchDto.getSearchTo()),
                        transferredByEq(searchDto.getTransferredBy()),
                        bankAcctNumLk(searchDto.getBankAcctNum()),
                        paymentMethodCdEq(searchDto.getPaymentMethod()),
                        slipHeader.status.eq("SV")
                )
                .orderBy(apPaymentsHd.eslipTransferBatchId.asc(),
                        apPaymentsHd.checkDt.desc())
                .fetch();

        for(ErpBulkSlipDto erpBulkSlipDto: erpBulkSlipDtos) {
            Long count = 0L;
            if(hasText(erpBulkSlipDto.getDocTitle())) {
                count = Long.valueOf(erpBulkSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpBulkSlipDto.setJiniCnt(count);
        }
        return erpBulkSlipDtos;
    }

    @Override
    public List<ErpBulkSlipDto> getErpBulkSlipDetail(ErpSlipRequestDto requestDto) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpBulkSlipDto(
                        apPaymentsHd.compCd,
                        apPaymentsHd.slipNo,
                        apPaymentsHd.slipHeaderId,
                        apPaymentsHd.slipType,
                        apPaymentsHd.checkDt,
                        apPaymentsHd.bankAcctNum,
                        slipHeader.usedCur,
                        slipHeader.usedAmt,
                        slipHeader.usedForAmt,
                        apPaymentsHd.futurePayDueDt,
                        apPaymentsHd.remark,
                        apPaymentsHd.regId,
                        employee.empNm,
                        slipHeader.deptNo,
                        costCenter.deptNm,
                        slipHeader.postingDt,
                        slipHeader.headerRemark,
                        apPaymentsHd.bankNm,
                        apPaymentsHd.bankAcctNm,
                        apPaymentsHd.bankBranchNm,
                        apPaymentsHd.eslipTransferBatchId,
                        apPaymentsHd.sumAmt,
                        slipHeader.status,
                        JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(apPaymentsHd.slipNo)),
                        JPAExpressions.select(jini.id.count()).from(jini).where(jini.documentId.eq(apPaymentsHd.slipNo))
                ))
                .from(apPaymentsHd)
                .leftJoin(slipHeader).on(slipHeader.slipNo.eq(apPaymentsHd.slipNo),slipHeader.compCd.eq(apPaymentsHd.compCd))
                .leftJoin(employee).on(employee.empNo.eq(apPaymentsHd.regId),employee.compCd.eq(apPaymentsHd.compCd))
                .leftJoin(costCenter).on(costCenter.deptCd.eq(slipHeader.deptNo),costCenter.compCd.eq(slipHeader.compCd))
                .where(apPaymentsHd.compCd.eq(compCd),
                        apPaymentsHd.slipNo.eq(requestDto.getSlipNo())
                )
                .fetch();
    }

    @Override
    public List<ErpBondSlipDto> getErpBondSlipList(ErpSlipRequestDto searchDto) {

        String compCd = util.getLoginCompCd();

        List<ErpBondSlipDto> erpBondSlipDtos =
             queryFactory
                .select(new QErpBondSlipDto(
                        apPaymentsHd.compCd,
                        apPaymentsHd.eslipTransferBatchId,
                        apPaymentsHd.eslipTransfer,
                        apPaymentsHd.transferType,
                        apPaymentsHd.transferDt,
                        apPaymentsHd.transferredBy,
                        apPaymentsHd.bankNm,
                        apPaymentsHd.bankBranchNm,
                        apPaymentsHd.bankAcctNm,
                        apPaymentsHd.bankAcctNum,
                        apPaymentsHd.segment3,
                        apPaymentsHd.segment4,
                        apPaymentsHd.segment3Desc,
                        apPaymentsHd.segment4Desc,
                        apPaymentsHd.futurePayDueDt,
                        apPaymentsHd.sumAmt,
                        apPaymentsHd.remark,
                        apPaymentsHd.paymentMethodLookupCode,
                        apPaymentsHd.slipNo,
                        apPaymentsHd.slipHeaderId,
                        apPaymentsHd.slipType,
                        JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(apPaymentsHd.slipNo)),
                        slipHeader.docTitle
                ))
                .from(apPaymentsHd)
                .leftJoin(slipHeader).on(apPaymentsHd.compCd.eq(slipHeader.compCd),apPaymentsHd.slipNo.eq(slipHeader.slipNo))
                .where(apPaymentsHd.compCd.eq(compCd),
                        futurePayDueDtFromGoe(searchDto.getSearchFrom()),
                        futurePayDueDtToLoe(searchDto.getSearchTo()),
                        transferredByEq(searchDto.getTransferredBy()),
                        bankAcctNumLk(searchDto.getBankAcctNum()),
                        paymentMethodCdEq(searchDto.getPaymentMethod()),
                        slipHeader.status.eq("SV")
                )
                .fetch();

        for(ErpBondSlipDto erpBondSlipDto: erpBondSlipDtos) {
            Long count = 0L;
            if(hasText(erpBondSlipDto.getDocTitle())) {
                count = Long.valueOf(erpBondSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpBondSlipDto.setJiniCnt(count);
        }
        return erpBondSlipDtos;
    }


    private BooleanExpression checkDtFromGoe(LocalDateTime checkDtFrom) {
        return nonNull(checkDtFrom) ? apPaymentsHd.checkDt.goe(checkDtFrom) : null;
    }

    private BooleanExpression checkDtToLoe(LocalDateTime checkDtTo) {
        return nonNull(checkDtTo) ? apPaymentsHd.checkDt.loe(checkDtTo) : null;
    }

    private BooleanExpression futurePayDueDtFromGoe(LocalDateTime checkDtFrom) {
        return nonNull(checkDtFrom) ? apPaymentsHd.futurePayDueDt.goe(checkDtFrom) : null;
    }

    private BooleanExpression futurePayDueDtToLoe(LocalDateTime checkDtTo) {
        return nonNull(checkDtTo) ? apPaymentsHd.futurePayDueDt.loe(checkDtTo) : null;
    }

    private BooleanExpression transferredByEq(String transferredBy) {
        return hasText(transferredBy) ? apPaymentsHd.transferredBy.eq(transferredBy) : null;
    }

    private BooleanExpression paymentMethodCdEq(String paymentMethodCd) {
        return hasText(paymentMethodCd) ? apPaymentsHd.paymentMethodLookupCode.eq(paymentMethodCd) : null;
    }

    private BooleanExpression bankAcctNumLk(String bankAcctNum) {
        return hasText(bankAcctNum) ? apPaymentsHd.bankAcctNum.likeIgnoreCase("%" + bankAcctNum.replaceAll("-","") + "%") : null;
    }

}

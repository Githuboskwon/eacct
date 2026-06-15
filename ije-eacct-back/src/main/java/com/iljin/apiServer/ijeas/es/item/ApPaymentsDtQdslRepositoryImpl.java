package com.iljin.apiServer.ijeas.es.item;

import static com.iljin.apiServer.ijeas.es.item.QApPaymentsDt.apPaymentsDt;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ApPaymentsDtQdslRepositoryImpl implements ApPaymentsDtQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpItemSlipDto> getErpItemSlipList(ErpSlipRequestDto searchDto) {
        List<ErpItemSlipDto> erpItemSlipDtos =
             queryFactory
            .select(new QErpItemSlipDto(
                apPaymentsDt.compCd,
                apPaymentsDt.slipNo,
                apPaymentsDt.slipType,
                apPaymentsDt.slipHeaderId,
                apPaymentsDt.checkId,
                apPaymentsDt.checkDt,
                apPaymentsDt.bankAcctNum,
                apPaymentsDt.currencyCd,
                apPaymentsDt.paymentAmt,
                apPaymentsDt.baseAmt,
                apPaymentsDt.vendorNm,
                apPaymentsDt.futurePayDueDt,
                apPaymentsDt.currencyConversionRate,
                apPaymentsDt.currencyConversionDt,
                apPaymentsDt.checkNo,
                apPaymentsDt.paymentFormat,
                apPaymentsDt.paymentMethodCd,
                apPaymentsDt.paymentMethod,
                apPaymentsDt.remark,
                apPaymentsDt.regId,
                employee.empNm,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(apPaymentsDt.slipNo)),
                slipHeader.docTitle
            ))
            .from(apPaymentsDt)
            .leftJoin(employee).on(employee.empNo.eq(apPaymentsDt.regId),employee.compCd.eq(apPaymentsDt.compCd))
            .leftJoin(slipHeader).on(slipHeader.slipNo.eq(apPaymentsDt.slipNo),slipHeader.compCd.eq(apPaymentsDt.compCd))
            .where(apPaymentsDt.compCd.eq(searchDto.getCompCd()),
                apPaymentsDt.slipType.eq(searchDto.getSlipType()),
                slipHeader.status.eq("SV"),
                checkDtFromGoe(searchDto.getSearchFrom()),
                checkDtToLoe(searchDto.getSearchTo()),
                transferredByEq(searchDto.getTransferredBy()),
                vendorNmEq(searchDto.getVendorNm()),
                checkNoEq(searchDto.getCheckNo()),
                futurePayDueDt(searchDto.getFuturePayDueDt()),
                bankAcctNumEq(searchDto.getBankAcctNum()),
                paymentMethodEq(searchDto.getPaymentMethod()),
                currencyCdEq(searchDto.getCurrencyCd())
            )
            .orderBy(apPaymentsDt.checkId.asc(),
                apPaymentsDt.checkDt.desc())
            .fetch();

        for (ErpItemSlipDto erpItemSlipDto : erpItemSlipDtos){
            Long count = 0L;
            if(hasText(erpItemSlipDto.getDocTitle())){
                count = Long.valueOf(erpItemSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpItemSlipDto.setJiniCnt(count);
        }
        return erpItemSlipDtos;
    }

    @Override
    public List<ErpItemSlipDto> getErpItemSlipDetail(ErpSlipRequestDto requestDto) {
        return queryFactory
            .select(new QErpItemSlipDto(
                apPaymentsDt.compCd,
                apPaymentsDt.slipNo,
                apPaymentsDt.slipType,
                apPaymentsDt.slipHeaderId,
                apPaymentsDt.checkId,
                apPaymentsDt.checkDt,
                apPaymentsDt.bankAcctNum,
                apPaymentsDt.currencyCd,
                apPaymentsDt.paymentAmt,
                apPaymentsDt.baseAmt,
                apPaymentsDt.vendorNm,
                apPaymentsDt.futurePayDueDt,
                apPaymentsDt.currencyConversionRate,
                apPaymentsDt.currencyConversionDt,
                apPaymentsDt.checkNo,
                apPaymentsDt.paymentFormat,
                apPaymentsDt.paymentMethodCd,
                apPaymentsDt.paymentMethod,
                apPaymentsDt.remark,
                apPaymentsDt.regId,
                employee.empNm,
                slipHeader.deptNo,
                costCenter.deptNm,
                slipHeader.postingDt,
                slipHeader.headerRemark,
                apPaymentsDt.bankNm,
                apPaymentsDt.bankAcctNm,
                apPaymentsDt.bankBranchNm,
                apPaymentsDt.acctHolderNm,
                apPaymentsDt.externalBankNm,
                apPaymentsDt.externalAcctNum,
                slipHeader.status,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(apPaymentsDt.slipNo)),
                JPAExpressions.select(jini.id.count()).from(jini).where(jini.documentId.eq(apPaymentsDt.slipNo))
            ))
            .from(apPaymentsDt)
            .leftJoin(slipHeader).on(slipHeader.slipNo.eq(apPaymentsDt.slipNo),slipHeader.compCd.eq(apPaymentsDt.compCd))
            .leftJoin(employee).on(employee.empNo.eq(apPaymentsDt.regId),employee.compCd.eq(apPaymentsDt.compCd))
            .leftJoin(costCenter).on(costCenter.deptCd.eq(slipHeader.deptNo),costCenter.compCd.eq(slipHeader.compCd))
            .where(apPaymentsDt.compCd.eq(requestDto.getCompCd()),
                apPaymentsDt.slipNo.eq(requestDto.getSlipNo()))
            .fetch();
    }

    @Override
    public List<ApPaymentsDtDto> pullErpApPaymentsDtList(String compCd, String eSlipTransferBatchId){
        return queryFactory
                .select(new QApPaymentsDtDto(
                        apPaymentsDt.compCd,
                        apPaymentsDt.slipNo,
                        apPaymentsDt.slipType,
                        apPaymentsDt.checkId,
                        apPaymentsDt.slipHeaderId,
                        apPaymentsDt.eslipTransferBatchId,
                        apPaymentsDt.eslipTransfer,
                        apPaymentsDt.eslipStatus,
                        apPaymentsDt.transferType,
                        apPaymentsDt.transferDt,
                        apPaymentsDt.transferredBy,
                        apPaymentsDt.bankNm,
                        apPaymentsDt.bankBranchNm,
                        apPaymentsDt.bankAcctNm,
                        apPaymentsDt.bankAcctNum,
                        apPaymentsDt.checkNo,
                        apPaymentsDt.paymentFormat,
                        apPaymentsDt.checkDt,
                        apPaymentsDt.futurePayDueDt,
                        apPaymentsDt.vendorNm,
                        apPaymentsDt.externalBankNm,
                        apPaymentsDt.externalAcctNm,
                        apPaymentsDt.externalAcctNum,
                        apPaymentsDt.acctHolderNm,
                        apPaymentsDt.paymentMethodCd,
                        apPaymentsDt.paymentMethod,
                        apPaymentsDt.paymentAmt,
                        apPaymentsDt.baseAmt,
                        apPaymentsDt.currencyCd,
                        apPaymentsDt.currencyConversionRate,
                        apPaymentsDt.currencyConversionDt,
                        apPaymentsDt.remark,
                        apPaymentsDt.attribute1,
                        apPaymentsDt.attribute2,
                        apPaymentsDt.attribute3,
                        apPaymentsDt.attribute4,
                        apPaymentsDt.attribute5,
                        slipHeader.usedAmt,
                        slipHeader.usedForAmt
                ))
                .from(apPaymentsDt)
                .leftJoin(slipHeader).on(slipHeader.slipNo.eq(apPaymentsDt.slipNo),slipHeader.compCd.eq(apPaymentsDt.compCd))
                .where(apPaymentsDt.compCd.eq(compCd),
                        apPaymentsDt.eslipTransferBatchId.eq(eSlipTransferBatchId))
                .fetch();
    }


    private BooleanExpression checkDtFromGoe(LocalDateTime checkDtFrom) {
        return nonNull(checkDtFrom) ? apPaymentsDt.checkDt.goe(checkDtFrom) : null;
    }

    private BooleanExpression checkDtToLoe(LocalDateTime checkDtTo) {
        return nonNull(checkDtTo) ? apPaymentsDt.checkDt.loe(checkDtTo) : null;
    }

    private BooleanExpression transferredByEq(String transferredBy) {
        return hasText(transferredBy) ? apPaymentsDt.transferredBy.eq(transferredBy) : null;
    }

    private BooleanExpression vendorNmEq(String vendorNm) {
        return hasText(vendorNm) ? apPaymentsDt.vendorNm.eq(vendorNm) : null;
    }

    private BooleanExpression checkNoEq(BigDecimal checkNo) {
        return nonNull(checkNo) ? apPaymentsDt.checkNo.eq(checkNo) : null;
    }

    private BooleanExpression futurePayDueDt(LocalDateTime futurePayDueDt) {
        return nonNull(futurePayDueDt) ? apPaymentsDt.futurePayDueDt.eq(futurePayDueDt) : null;
    }

    private BooleanExpression paymentMethodEq(String paymentMethod) {
        return hasText(paymentMethod) ? apPaymentsDt.paymentMethod.eq(paymentMethod) : null;
    }

    private BooleanExpression currencyCdEq(String currencyCd) {
        return hasText(currencyCd) ? apPaymentsDt.currencyCd.eq(currencyCd) : null;
    }

    private BooleanExpression bankAcctNumEq(String bankAcctNum) {
        return hasText(bankAcctNum) ? apPaymentsDt.bankAcctNum.eq(bankAcctNum) : null;
    }


}

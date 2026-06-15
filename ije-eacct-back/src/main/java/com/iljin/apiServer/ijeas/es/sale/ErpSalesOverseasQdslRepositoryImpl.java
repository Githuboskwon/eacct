package com.iljin.apiServer.ijeas.es.sale;

import com.iljin.apiServer.ijeas.es.erpViews.sale.ErpArTrxHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.frgn.ErpSpOsSlip;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.bulk.QErpApPaymentsBatch.erpApPaymentsBatch;
import static com.iljin.apiServer.ijeas.es.sale.QErpSalesOverseas.erpSalesOverseas;
import static com.iljin.apiServer.ijeas.es.erpViews.sale.QErpArTrxHeaders.erpArTrxHeaders;
import static com.iljin.apiServer.ijeas.es.erpViews.frgn.QErpSpOsSlip.erpSpOsSlip;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ErpSalesOverseasQdslRepositoryImpl implements ErpSalesOverseasQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpArTrxHeaders> pullErpArTrxHeaders(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
        .selectFrom(erpArTrxHeaders)
                .where(erpArTrxHeaders.glDate.loe(erpSlipRequestDto.getSearchTo()),
                        erpArTrxHeaders.glDate.goe(erpSlipRequestDto.getSearchFrom())
//                erpApPaymentsBatch.transferredBy.eq(erpSlipRequestDto.getUserId()), // 추후 주석 해제
                )
                .fetch();
    }

    @Override
    public List<ErpArTrxHeaders> pullErpArTrxHeaders_export(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {


//        YearMonth yearMonth = YearMonth.parse(erpSlipRequestDto.getSearchMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
//        LocalDate startDate = yearMonth.atDay(1);
//        LocalDate endDate = yearMonth.atEndOfMonth();

        return queryFactory
                .selectFrom(erpArTrxHeaders)
                .where(erpArTrxHeaders.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                        JPAExpressions.selectFrom(slipHeader)
                                .where(slipHeader.slipType.eq(slipTypeCd),
                                        slipHeader.erpInvoiceId.eq(
                                        Expressions.stringTemplate("TO_CHAR({0})", erpArTrxHeaders.slipId)
                                        )).notExists(),
                        divLk2(erpSlipRequestDto.getDiv()),
                        erpArTrxHeaders.glDate.loe(erpSlipRequestDto.getSearchTo()),
                        erpArTrxHeaders.glDate.goe(erpSlipRequestDto.getSearchFrom())
//                .where(erpArTrxHeaders.glDate.between(startDate.atStartOfDay(), endDate.atStartOfDay()),
//                erpApPaymentsBatch.transferredBy.eq(erpSlipRequestDto.getUserId()), // 추후 주석 해제
                )
                .fetch();
    }


    @Override
    public List<ErpSalesOverseasDto> getErpSalesSlipList(ErpSlipRequestDto erpSlipRequestDto){

        List<ErpSalesOverseasDto> erpSalesOverseasDtos;

        if(erpSlipRequestDto.getSlipType().equals("30")){
            erpSalesOverseasDtos = queryFactory
                    .select(new QErpSalesOverseasDto(
                            erpSalesOverseas.compCd,
                            erpSalesOverseas.slipNo,
                            erpSalesOverseas.slipType,
                            erpSalesOverseas.slipHeaderId,
                            erpSalesOverseas.xtrSlipType,
                            erpSalesOverseas.batchId,
                            erpSalesOverseas.transactionNum,
                            erpSalesOverseas.glDt,
                            erpSalesOverseas.integrationVendorNum,
                            erpSalesOverseas.integrationVendorNm,
                            erpSalesOverseas.erpRegId,
                            erpSalesOverseas.erpRegNm,
                            erpSalesOverseas.coaSegment4Name,
                            erpSalesOverseas.coaSegment5,
                            erpSalesOverseas.projectCd,
                            erpSalesOverseas.projectNm,
                            erpSalesOverseas.currencyCd,
                            erpSalesOverseas.enteredAmt,
                            erpSalesOverseas.accountedAmt,
                            erpSalesOverseas.periodNm,
                            erpSalesOverseas.ledgerId,
                            erpSalesOverseas.partitionKey,
                            JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(erpSalesOverseas.slipNo)),
                            slipHeader.docTitle,
                            erpSalesOverseas.remark,
                            erpSalesOverseas.slipDeptName,
                            erpSalesOverseas.taskCode,
                            erpSalesOverseas.taskName,
                            erpSalesOverseas.incoterms,
                            erpSalesOverseas.certAmendYn
                    ))
                    .from(erpSalesOverseas)
                    .leftJoin(slipHeader).on(slipHeader.compCd.eq(erpSalesOverseas.compCd),slipHeader.slipNo.eq(erpSalesOverseas.slipNo))
//                    .leftJoin(costCenter).on(slipHeader.compCd.eq(costCenter.compCd) , slipHeader.deptNo.eq(costCenter.deptCd))
                    .where(erpSalesOverseas.compCd.eq(erpSlipRequestDto.getCompCd()),
                            glDtFromGoe(erpSlipRequestDto.getSearchFrom()),
                            glDtToLoe(erpSlipRequestDto.getSearchTo()),
//                            glMtLk(erpSlipRequestDto.getSearchMonth()),
                            vendorNmEq(erpSlipRequestDto.getVendorCd()),
                            erpRegIdEq(erpSlipRequestDto.getTransferredBy()),
                            divLk(erpSlipRequestDto.getDiv()),
                            deptCdEq(erpSlipRequestDto.getDeptCd()),
                            fileYn(erpSlipRequestDto.getFileYn(),erpSalesOverseas.slipNo),
                            erpSalesOverseas.slipType.eq(erpSlipRequestDto.getSlipType()),
                            slipHeader.status.eq("SV")
                    )
                    .fetch();
        }else{
            erpSalesOverseasDtos = queryFactory
                            .select(new QErpSalesOverseasDto(
                                    erpSalesOverseas.compCd,
                                    erpSalesOverseas.slipNo,
                                    erpSalesOverseas.slipType,
                                    erpSalesOverseas.slipHeaderId,
                                    erpSalesOverseas.xtrSlipType,
                                    erpSalesOverseas.batchId,
                                    erpSalesOverseas.transactionNum,
                                    erpSalesOverseas.glDt,
                                    erpSalesOverseas.integrationVendorNum,
                                    erpSalesOverseas.integrationVendorNm,
                                    erpSalesOverseas.erpRegId,
                                    erpSalesOverseas.erpRegNm,
                                    erpSalesOverseas.coaSegment5,
                                    erpSalesOverseas.projectCd,
                                    erpSalesOverseas.projectNm,
                                    erpSalesOverseas.currencyCd,
                                    erpSalesOverseas.enteredAmt,
                                    erpSalesOverseas.accountedAmt,
                                    erpSalesOverseas.periodNm,
                                    erpSalesOverseas.ledgerId,
                                    erpSalesOverseas.partitionKey,
                                    JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(erpSalesOverseas.slipNo)),
                                    slipHeader.docTitle
                            ))
                            .from(erpSalesOverseas)
                            .leftJoin(slipHeader).on(slipHeader.compCd.eq(erpSalesOverseas.compCd),slipHeader.slipNo.eq(erpSalesOverseas.slipNo))
                            .where(erpSalesOverseas.compCd.eq(erpSlipRequestDto.getCompCd()),
                                    glDtFromGoe(erpSlipRequestDto.getSearchFrom()),
                                    glDtToLoe(erpSlipRequestDto.getSearchTo()),
                                    vendorNmEq(erpSlipRequestDto.getVendorCd()),
                                    erpRegIdEq(erpSlipRequestDto.getTransferredBy()),
                                    fileYn(erpSlipRequestDto.getFileYn(),erpSalesOverseas.slipNo),
                                    erpSalesOverseas.slipType.eq(erpSlipRequestDto.getSlipType()),
                                    slipHeader.status.eq("SV")
                            )
                            .fetch();
        }



        for(ErpSalesOverseasDto erpSalesOverseasDto: erpSalesOverseasDtos) {
            Long count = 0L;
            if(hasText(erpSalesOverseasDto.getDocTitle())) {
                count = Long.valueOf(erpSalesOverseasDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpSalesOverseasDto.setJiniCnt(count);
        }
        return erpSalesOverseasDtos;
    }


    @Override
    public List<ErpSpOsSlip> pullErpSpOsSlip(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
                .selectFrom(erpSpOsSlip)
                .where(erpSpOsSlip.glDate.loe(erpSlipRequestDto.getSearchTo()),
                        erpSpOsSlip.glDate.goe(erpSlipRequestDto.getSearchFrom())
                )
                .fetch();
    }


    private BooleanExpression glDtFromGoe(LocalDateTime glDt) {
        return nonNull(glDt) ? erpSalesOverseas.glDt.goe(glDt) : null;
    }

    private BooleanExpression glDtToLoe(LocalDateTime glDt) {
        return nonNull(glDt) ? erpSalesOverseas.glDt.loe(glDt) : null;
    }

    private BooleanExpression vendorNmEq(String vendorNum) {
        return hasText(vendorNum) ? erpSalesOverseas.integrationVendorNum.eq(vendorNum) : null;
    }

    private BooleanExpression erpRegIdEq(String erpRegId) {
        return hasText(erpRegId) ? erpSalesOverseas.erpRegId.eq(erpRegId) : null;
    }

    private BooleanExpression divLk(String div) {
        return hasText(div) ? erpSalesOverseas.coaSegment5.likeIgnoreCase(div + "%") : null;
    }

    private BooleanExpression divLk2(String div) {
        return hasText(div) ? erpArTrxHeaders.coaSegment5.likeIgnoreCase(div + "%") : null;
    }

    private BooleanExpression deptCdEq(String deptCd) {
        return hasText(deptCd) ? slipHeader.deptNo.eq(deptCd) : null;
    }

    private BooleanExpression glMtLk(String glMt) {
        if (hasText(glMt)) {
            // glMt 예시: "2025-04"
            StringTemplate formattedDate = Expressions.stringTemplate(
                    "TO_CHAR({0}, {1})",
                    erpSalesOverseas.glDt,
                    "YYYY-MM"
            );
            return formattedDate.likeIgnoreCase(glMt + "%");
        } else {
            return null;
        }
    }
    private BooleanExpression glMtLk2(String glMt) {
        return hasText(glMt) ? erpArTrxHeaders.glDate.stringValue().like(glMt + "%") : null;
    }


    private BooleanExpression fileYn(String fileYn, StringPath slipNo) {
        if(hasText(fileYn)){
            return fileYn.equals("Y")
                    ? JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(slipNo)).goe(1L)
                    : JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(slipNo)).eq(0L);
        }else{
            return  null;
        }
    }




}

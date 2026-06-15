package com.iljin.apiServer.ijeas.es.gl;

import static com.iljin.apiServer.ijeas.es.gl.QGlHeader.glHeader;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GlHeaderQdslRepositoryImpl implements GlHeaderQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpGlSlipDto> getErpGlSlipList(ErpSlipRequestDto searchDto) {
        List<ErpGlSlipDto> erpGlSlipDtos =  queryFactory
            .select(new QErpGlSlipDto(
                glHeader.compCd,
                glHeader.slipNo,
                slipHeader.status,
                glHeader.slipType,
                glHeader.slipHeaderId,
                glHeader.jeHeaderId,
                glHeader.headerNm,
                glHeader.ledgerId,
                glHeader.sourceNm,
                glHeader.categoryNm,
                glHeader.glSlipType,
                glHeader.trxTypeCd,
                glHeader.trxTypeNm,
                glHeader.jeBatchId,
                glHeader.batchNm,
                glHeader.batchStatus,
                glHeader.reverseFlag,
                glHeader.reverseHeaderNm,
                glHeader.headerRemark,
                glHeader.headerStatus,
                glHeader.postedDt,
                slipHeader.postingDt,
                glHeader.currencyCd,
                glHeader.currencyRate,
                glHeader.currencyRateType,
                glHeader.currencyDt,
                glHeader.glDt,
                glHeader.forTotalDr,
                glHeader.forTotalCr,
                glHeader.krwTotalDr,
                glHeader.krwTotalCr,
                glHeader.regId,
                employee.empNm,
                employee.deptCd,
                employee.deptNm,
                glHeader.regDtm,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.compCd.eq(searchDto.getCompCd()), uFile.documentHId.eq(glHeader.slipNo)),
                slipHeader.docTitle
            ))
            .from(glHeader)
            .leftJoin(employee).on(employee.empNo.eq(glHeader.regId), employee.compCd.eq(glHeader.compCd))
            .leftJoin(slipHeader).on(slipHeader.slipNo.eq(glHeader.slipNo), slipHeader.compCd.eq(glHeader.compCd))
            .where(glHeader.compCd.eq(searchDto.getCompCd()),
                glHeader.slipType.eq(searchDto.getSlipType()),
                slipHeader.status.eq("SV"),
                glDtFromGoe(searchDto.getSearchFrom()),
                glDtToLoe(searchDto.getSearchTo()),
                batchNmContainsIgnoreCase(searchDto.getBatchNm()),
                headerNmContainsIgnoreCase(searchDto.getHeaderNm()),
                sourceNmEq(searchDto.getSourceNm()),
                fileYn(searchDto.getFileYn(), glHeader.slipNo),
                jiniYn(searchDto.getJiniYn(), glHeader.slipNo),
                glSlipTypeEq(searchDto.getGlSlipTypeNm()),
                trxTypeCdEq(searchDto.getTrxTypeCd()),
                regIdEq(searchDto.getTransferredBy())
                )
            .fetch();

        for(ErpGlSlipDto erpGlSlipDto: erpGlSlipDtos) {
            Long count = 0L;
            if(hasText(erpGlSlipDto.getDocTitle())) {
                count = Long.valueOf(erpGlSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpGlSlipDto.setJiniCnt(count);
        }
        return erpGlSlipDtos;
    }

    @Override
    public List<ErpGlSlipDto> getErpGlSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .select(new QErpGlSlipDto(
                glHeader.compCd,
                glHeader.slipNo,
                slipHeader.status,
                glHeader.slipType,
                glHeader.slipHeaderId,
                glHeader.jeHeaderId,
                glHeader.headerNm,
                glHeader.ledgerId,
                glHeader.sourceNm,
                glHeader.categoryNm,
                glHeader.glSlipType,
                glHeader.trxTypeCd,
                glHeader.trxTypeNm,
                glHeader.jeBatchId,
                glHeader.batchNm,
                glHeader.batchStatus,
                glHeader.reverseFlag,
                glHeader.reverseHeaderNm,
                glHeader.headerRemark,
                glHeader.headerStatus,
                glHeader.postedDt,
                slipHeader.postingDt,
                glHeader.currencyCd,
                glHeader.currencyRate,
                glHeader.currencyRateType,
                glHeader.currencyDt,
                glHeader.glDt,
                glHeader.forTotalDr,
                glHeader.forTotalCr,
                glHeader.krwTotalDr,
                glHeader.krwTotalCr,
                glHeader.regId,
                employee.empNm,
                employee.deptCd,
                employee.deptNm,
                glHeader.regDtm,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(glHeader.slipNo)),
                slipHeader.docTitle
            ))
            .from(glHeader)
            .leftJoin(employee).on(employee.empNo.eq(glHeader.regId), employee.compCd.eq(glHeader.compCd))
            .leftJoin(slipHeader).on(slipHeader.slipNo.eq(glHeader.slipNo), slipHeader.compCd.eq(glHeader.compCd))
            .where(glHeader.compCd.eq(erpSlipRequestDto.getCompCd()),
                glHeader.slipType.eq(erpSlipRequestDto.getSlipType()),
//                slipHeader.status.ne("AP"),
                glHeader.slipNo.eq(erpSlipRequestDto.getSlipNo())
            )
            .fetch();
    }

    private BooleanExpression glDtFromGoe(LocalDateTime glDtFrom) {
        return nonNull(glDtFrom) ? glHeader.glDt.goe(glDtFrom) : null;
    }

    private BooleanExpression glDtToLoe(LocalDateTime glDtTo) {
        return nonNull(glDtTo) ? glHeader.glDt.loe(glDtTo) : null;
    }

    private BooleanExpression batchNmContainsIgnoreCase (String batchNm) {
        return hasText(batchNm) ? glHeader.batchNm.containsIgnoreCase(batchNm) : null;
    }

    private BooleanExpression headerNmContainsIgnoreCase (String headerNm) {
        return hasText(headerNm) ? glHeader.headerNm.containsIgnoreCase(headerNm) : null;
    }

    private BooleanExpression fileYn(String fileYn, StringPath slipNo) {
        if(hasText(fileYn)) {
            return fileYn.equals("Y")
                ? JPAExpressions.select(uFile.id).from(uFile).where(uFile.documentHId.eq(slipNo)).exists()
                : JPAExpressions.select(uFile.id).from(uFile).where(uFile.documentHId.eq(slipNo)).notExists();
        }
        return null;
    }

    private BooleanExpression jiniYn(String jiniYn, StringPath slipNo) {
        if(hasText(jiniYn)) {
            return jiniYn.equals("Y")
                ? JPAExpressions.select(slipHeader.docTitle).from(slipHeader).where(slipHeader.slipNo.eq(slipNo)).isNotNull()
                : JPAExpressions.select(slipHeader.docTitle).from(slipHeader).where(slipHeader.slipNo.eq(slipNo)).isNull();
        }
        return null;
    }

    private BooleanExpression regIdEq(String regId) {
        return hasText(regId) ? glHeader.regId.eq(regId): null;
    }

    private BooleanExpression glSlipTypeEq(String glSlipType) {
        return hasText(glSlipType) ? glHeader.glSlipType.eq(glSlipType) : null;
    }

    private BooleanExpression trxTypeCdEq(String trxTypeCd) {
        return hasText(trxTypeCd) ? glHeader.trxTypeCd.eq(trxTypeCd) : null;
    }

    private BooleanExpression sourceNmEq(String sourceNm) {
        return hasText(sourceNm) ? glHeader.sourceNm.eq(sourceNm) : null;
    }

}

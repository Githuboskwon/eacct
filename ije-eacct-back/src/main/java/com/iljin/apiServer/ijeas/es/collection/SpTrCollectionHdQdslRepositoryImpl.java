package com.iljin.apiServer.ijeas.es.collection;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.collection.QSpTrCollectionHd.spTrCollectionHd;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.sm.evid.QUFile.uFile;
import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class SpTrCollectionHdQdslRepositoryImpl implements SpTrCollectionHdQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpCollectionSlipDto> getErpCollectionSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpCollectionSlipDto> erpCollectionSlipDtos =
        queryFactory
        .select(new QErpCollectionSlipDto(
                spTrCollectionHd.compCd,
                spTrCollectionHd.slipNo,
                spTrCollectionHd.slipType,
                spTrCollectionHd.slipHeaderId,
                spTrCollectionHd.xtrSlipType,
                spTrCollectionHd.glDt,
                spTrCollectionHd.currencyCd,
                spTrCollectionHd.functionalAmtDr,
                spTrCollectionHd.functionalAmtCr,
                spTrCollectionHd.remark,
                JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(spTrCollectionHd.slipNo)),
                slipHeader.docTitle
        ))
        .from(spTrCollectionHd)
        .leftJoin(slipHeader).on(spTrCollectionHd.compCd.eq(slipHeader.compCd),spTrCollectionHd.slipNo.eq(slipHeader.slipNo))
        .where(spTrCollectionHd.compCd.eq(erpSlipRequestDto.getCompCd()),
        glDtFromGoe(erpSlipRequestDto.getSearchFrom()),
        glDtToLoe(erpSlipRequestDto.getSearchTo()),
        slipHeader.status.eq("SV")
        )
        .fetch();

        for(ErpCollectionSlipDto erpCollectionSlipDto: erpCollectionSlipDtos) {
            Long count = 0L;
            if(hasText(erpCollectionSlipDto.getDocTitle())) {
                count = Long.valueOf(erpCollectionSlipDto.getDocTitle().split(",").length);
                if(count == 0L) count = 1L;
            }
            erpCollectionSlipDto.setJiniCnt(count);
        }
        return erpCollectionSlipDtos;
    }

    @Override
    public List<ErpCollectionSlipDto> getErpCollectionSlip(ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
                .select(new QErpCollectionSlipDto(
                        spTrCollectionHd.compCd,
                        spTrCollectionHd.slipNo,
                        spTrCollectionHd.slipType,
                        spTrCollectionHd.slipHeaderId,
                        spTrCollectionHd.xtrSlipType,
                        spTrCollectionHd.glDt,
                        spTrCollectionHd.currencyCd,
                        spTrCollectionHd.functionalAmtDr,
                        spTrCollectionHd.functionalAmtCr,
                        spTrCollectionHd.remark,
                        spTrCollectionHd.chgId,
                        employee.empNm.as("chgNm"),
                        costCenter.deptCd,
                        costCenter.deptNm,
                        slipHeader.status,
                        JPAExpressions.select(uFile.id.count()).from(uFile).where(uFile.documentHId.eq(spTrCollectionHd.slipNo)),
                        JPAExpressions.select(jini.id.count()).from(jini).where(jini.documentId.eq(spTrCollectionHd.slipNo))
                ))
                .from(spTrCollectionHd)
                .leftJoin(employee).on(spTrCollectionHd.compCd.eq(employee.compCd), spTrCollectionHd.chgId.eq(employee.empNo))
                .leftJoin(costCenter).on(employee.compCd.eq(costCenter.compCd) , employee.cctrCd.eq(costCenter.deptCd))
                .leftJoin(slipHeader).on(slipHeader.slipNo.eq(spTrCollectionHd.slipNo), slipHeader.compCd.eq(spTrCollectionHd.compCd))
                .where(spTrCollectionHd.compCd.eq(erpSlipRequestDto.getCompCd()),
                        spTrCollectionHd.slipNo.eq(erpSlipRequestDto.getSlipNo()
                ))
                .fetch();
    }



    private BooleanExpression glDtFromGoe(LocalDateTime glDt) {
        return nonNull(glDt) ? spTrCollectionHd.glDt.goe(glDt) : null;
    }

    private BooleanExpression glDtToLoe(LocalDateTime glDt) {
        return nonNull(glDt) ? spTrCollectionHd.glDt.loe(glDt) : null;
    }

}

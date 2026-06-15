package com.iljin.apiServer.ijeas.es.erpViews.gl;

import static com.iljin.apiServer.ijeas.es.erpViews.gl.QErpGlHeaders.erpGlHeaders;
import static com.iljin.apiServer.ijeas.es.gl.QGlHeader.glHeader;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ErpGlHeadersQdslRepositoryImpl implements ErpGlHeadersQdslRepository {

    private final JPAQueryFactory queryFactory;
    private final Util util;

    @Override
    public List<ErpGlHeaders> pullErpGlHeaders(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        return queryFactory
            .selectFrom(erpGlHeaders)
            .where(erpGlHeaders.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                JPAExpressions.selectFrom(glHeader).where(glHeader.jeHeaderId.eq(erpGlHeaders.jeHeaderId)).notExists(),
                userNameEq(erpSlipRequestDto.getTransferredBy()),
                erpGlHeaders.glDate.loe(erpSlipRequestDto.getSearchTo()),
                erpGlHeaders.glDate.goe(erpSlipRequestDto.getSearchFrom()))
            .fetch();
    }

    private BooleanExpression userNameEq (String empNo) {
        return hasText(empNo) ? erpGlHeaders.userName.eq(empNo) : null;

    }

}

package com.iljin.apiServer.ijeas.sm.jini;

import static com.iljin.apiServer.ijeas.sm.jini.QJini.jini;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JiniQdslRepositoryImpl implements JiniQdslRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Jini> getJiniDocsByPreviousMonth(String vendorNm, String regId) {
        return queryFactory
            .selectFrom(jini)
            .leftJoin(slipHeader).on(slipHeader.slipNo.eq(jini.documentId))
            .where(regDtmEq(),
                vendorNmContainsIgnoreCase(vendorNm),
                slipHeader.regId.eq(regId))
            .fetch();
    }

    private BooleanExpression vendorNmContainsIgnoreCase(String vendorNm) {
        return hasText(vendorNm) ? slipHeader.erpVendorNm.eq(vendorNm) : null;
    }

    private BooleanExpression regDtmEq() {
        String lastMonthDate = DateTimeFormatter.ofPattern("yyyyMM").format(LocalDateTime.now());
        StringExpression formattedDate = Expressions.stringTemplate("TO_CHAR({0}, {1})", jini.regDtm,  "yyyyMM");
        return formattedDate.eq(lastMonthDate);

    }



}

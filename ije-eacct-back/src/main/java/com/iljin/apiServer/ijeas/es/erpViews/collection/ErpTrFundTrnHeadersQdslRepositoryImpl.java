package com.iljin.apiServer.ijeas.es.erpViews.collection;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.collection.QErpTrFundTrnHeaders.erpTrFundTrnHeaders;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;

@RequiredArgsConstructor
@Repository
public class ErpTrFundTrnHeadersQdslRepositoryImpl implements ErpTrFundTrnHeadersQdslRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ErpTrFundTrnHeaders> pullErpTrFundTrnHeaders(String slipTypeCd , ErpSlipRequestDto erpSlipRequestDto) {


        return queryFactory
            .selectFrom(erpTrFundTrnHeaders)
            .where(erpTrFundTrnHeaders.orgId.eq(Integer.parseInt(erpSlipRequestDto.getCompCd())),
                    JPAExpressions.selectFrom(slipHeader).where(slipHeader.slipType.eq(slipTypeCd),
                            slipHeader.postingDt.eq(Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", erpTrFundTrnHeaders.glDate))).notExists(),
                    erpTrFundTrnHeaders.glDate.loe(erpSlipRequestDto.getSearchTo()),
                    erpTrFundTrnHeaders.glDate.goe(erpSlipRequestDto.getSearchFrom())
            )
            .fetch();
    }

    private String strGlDate(DateTimePath<java.time.LocalDateTime> glDate){
//       String result = Expressions.stringTemplate("FUNCTION('FORMATDATETIME', {0}, {1})", glDate, "yyyy-MM-dd HH:mm:ss").as("formattedDateTime");
       LocalDateTime lGldt = LocalDateTime.parse(String.valueOf(glDate));
       return lGldt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    };



}

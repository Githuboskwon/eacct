package com.iljin.apiServer.ijeas.system.benCountry;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.benCountry.QBenCountry.benCountry;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class BenCountryQdslRepositoryImpl implements BenCountryQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BenCountryDto> getBenCountryList(String value) {
        return queryFactory
            .select(new QBenCountryDto(
                benCountry.benCountryCd,
                benCountry.benCountryNm,
                benCountry.alternateBenCountryCd,
                benCountry.description
            ))
            .from(benCountry)
            .where(searchCondition(value)
            )
            .fetch();
    }

    private BooleanExpression benCountryCdContains(String benCountryCd) {
        return hasText(benCountryCd) ? benCountry.benCountryCd.contains(benCountryCd) : null;
    }

    private BooleanExpression benCountryNmContains(String benCountryNm) {
        return hasText(benCountryNm) ? benCountry.benCountryNm.contains(benCountryNm) : null;
    }

    private BooleanBuilder searchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(benCountry.benCountryCd.containsIgnoreCase(value))
                .or(benCountry.benCountryNm.containsIgnoreCase(value));
        }
        return booleanBuilder;
    }


}

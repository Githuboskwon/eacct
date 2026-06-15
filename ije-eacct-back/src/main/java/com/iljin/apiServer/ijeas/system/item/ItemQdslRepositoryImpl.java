package com.iljin.apiServer.ijeas.system.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.iljin.apiServer.ijeas.system.item.QCboApEmpSegment5.cboApEmpSegment5;
import static com.iljin.apiServer.ijeas.system.item.QItem.item;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ItemQdslRepositoryImpl implements ItemQdslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ItemDto> getItemList(ItemDto itemDto){
        List<ItemDto> result = queryFactory
                .select(new QItemDto(
                        item.compCd,
                        item.itemGroupCd,
                        item.itemGroupNm,
                        item.attribute1
                ))
                .from(item)
                .where(searchCondition(itemDto.itemGroupCd).or(searchCondition(itemDto.itemGroupNm))
                        .and(compCdEq(itemDto.compCd))
                )
                .fetch();

        return result;
    }

    @Override
    public List<ItemDto> getSlipItemList(ItemDto itemDto){
        List<ItemDto> result = queryFactory
                .select(new QItemDto(
                        item.compCd,
                        item.itemGroupCd,
                        item.itemGroupNm,
                        item.attribute1
                ))
                .from(item)
                .where(
                        item.itemGroupCd.in(JPAExpressions.select(cboApEmpSegment5.segment5)
                                        .from(cboApEmpSegment5)
                                        .where(cboApEmpSegment5.personId.eq(itemDto.personId)
                                                .and(cboApEmpSegment5.enabledFlag.eq("Y"))
                                                .and(cboApEmpSegment5.orgId.eq(Integer.valueOf(itemDto.compCd))))
                                )
                                .and(compCdEq(itemDto.compCd))
                                //.and(item.startDateActive.loe(LocalDate.parse(itemDto.postingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()))
                                //.and(item.endDateActive.goe(LocalDate.parse(itemDto.postingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()))
                                .and(searchCondition(itemDto.itemGroupCd).or(searchCondition(itemDto.itemGroupNm)))
                )
                .fetch();


        return result;

    }

    private BooleanBuilder searchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(item.itemGroupCd.contains(value))
                    .or(item.itemGroupNm.contains(value));
        }
        return booleanBuilder;
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? item.compCd.eq(compCd) : null;
    }
}

package com.iljin.apiServer.ijeas.costBudget;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlCoaSegment.erpGlCoaSegment;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpPaProjectsAll.erpPaProjectsAll;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.item.ItemDto;
import com.iljin.apiServer.ijeas.system.item.QItemDto;
import com.iljin.apiServer.ijeas.system.pjt.ProjectDto;
import com.iljin.apiServer.ijeas.system.pjt.QProjectDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class CostBudgetQdslRepositoryImpl implements CostBudgetQdslRepository {
    private final JPAQueryFactory queryFactory;

    private final Util util;

    @Override
    public List<ItemDto> getBudgetItemList(CostBudgetDto costBudgetDto){

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QItemDto(
                        erpGlCoaSegment.orgId.stringValue().as("compCd")
                        ,erpGlCoaSegment.valueCode.as("itemGroupName")
                        ,erpGlCoaSegment.valueName.as("itemGroupCode")
                        ,erpGlCoaSegment.attribute1
                ))
                .from(erpGlCoaSegment)
                .where(erpGlCoaSegment.orgId.eq(Integer.parseInt(compCd)),
                        erpGlCoaSegment.segmentNum.eq(BigDecimal.valueOf(5)),
                        erpGlCoaSegment.enabledFlag.eq("Y"),
                        searchCondition(costBudgetDto.itemGroupCd).or(searchCondition(costBudgetDto.itemGroupNm))
                )
                .fetch();
    }

    @Override
    public List<ProjectDto> getBudgetProjectList(CostBudgetDto costBudgetDto){

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QProjectDto(
                        erpGlCoaSegment.orgId.stringValue().as("compCd"),
                        erpGlCoaSegment.valueCode.as("projectCd"),
                        erpGlCoaSegment.valueName.as("projectNm"),
                        erpPaProjectsAll.projectId
                ))
                .from(erpGlCoaSegment)
                .leftJoin(erpPaProjectsAll).on(erpPaProjectsAll.orgId.eq(Integer.parseInt(compCd)),erpPaProjectsAll.segment1.eq(erpGlCoaSegment.valueCode))
                .where(erpGlCoaSegment.orgId.eq(Integer.parseInt(compCd)),
                        erpGlCoaSegment.segmentNum.eq(BigDecimal.valueOf(6)),
                        searchCondition(costBudgetDto.itemGroupCd).or(searchCondition(costBudgetDto.itemGroupNm))
                )
                .orderBy(erpGlCoaSegment.valueCode.asc())
                .fetch();

    }

    private BooleanBuilder searchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(erpGlCoaSegment.valueCode.contains(value))
                    .or(erpGlCoaSegment.valueName.contains(value));
        }
        return booleanBuilder;
    }

}

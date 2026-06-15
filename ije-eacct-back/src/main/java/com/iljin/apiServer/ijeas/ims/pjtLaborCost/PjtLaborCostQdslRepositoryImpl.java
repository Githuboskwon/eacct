package com.iljin.apiServer.ijeas.ims.pjtLaborCost;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.ims.pjtLaborCost.QPjtLaborCost.pjtLaborCost;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class PjtLaborCostQdslRepositoryImpl implements PjtLaborCostQdslRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<PjtLaborCostDto> getLaborCostList(PjtLaborCostDto pjtLaborCostDto) {

        return queryFactory
                .select(new QPjtLaborCostDto(
                        pjtLaborCost.orgId,
                        pjtLaborCost.projectMngNo,
                        codeDetail.detailCd.as("positionCd"),
                        pjtLaborCost.amt,
                        pjtLaborCost.remark,
                        pjtLaborCost.addDate,
                        pjtLaborCost.addTime,
                        pjtLaborCost.addUserId,
                        pjtLaborCost.changeDate.coalesce(pjtLaborCost.addDate).as("changeDate"),
                        pjtLaborCost.changeTime.coalesce(pjtLaborCost.addTime).as("changeTime"),
                        pjtLaborCost.changeUserId.coalesce(pjtLaborCost.addUserId).as("changeUserId"),
                        pjtLaborCost.amtType,
                        pjtLaborCost.acctCode,
                        pjtLaborCost.acctName
                ))
                .from(pjtLaborCost)
                .leftJoin(codeDetail).on(codeDetail.detailCd.eq(pjtLaborCost.positionCd))
                .where(codeDetail.groupCd.eq("SPJ_EM_CD")
                        ,orgIDEq(pjtLaborCostDto.getOrgId())
                )
                .fetch();
    }

    private BooleanExpression orgIDEq(String orgId) {
        return hasText(orgId) ? pjtLaborCost.orgId.eq(orgId) : null;
    }

}

package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.core.security.user.QUser.user;
import static com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfo.pjtRegistInfo;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class PjtRegistInfoQdslRepositoryImpl implements PjtRegistInfoQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PjtRegistInfoDto> getRegistInfoList(PjtRegistInfoDto pjtRegistInfoDto) {

        QPjtRegistInfo pjtRegistInfo1 = new QPjtRegistInfo("pjtRegistInfo");

        return queryFactory
                .select(new QPjtRegistInfoDto(
                        pjtRegistInfo.orgId,
                        pjtRegistInfo.slipNo,
                        slipHeader.slipType,
                        pjtRegistInfo.projectManageNo,
                        pjtRegistInfo.degree,
                        pjtRegistInfo.projectId,
                        pjtRegistInfo.projectCode,
                        pjtRegistInfo.projectName,
                        pjtRegistInfo.startDate,
                        pjtRegistInfo.endDate,
                        pjtRegistInfo.startDate.as("saveStartDate"),
                        pjtRegistInfo.endDate.as("saveEndDate"),
                        pjtRegistInfo.totalMonth,
                        pjtRegistInfo.taskNumber,
                        pjtRegistInfo.taskName,
                        pjtRegistInfo.addUserId,
                        ExpressionUtils.as(JPAExpressions.select(user.userName)
                                .from(user)
                                .where(user.compCd.eq(pjtRegistInfo.orgId), user.loginId.eq(pjtRegistInfo.addUserId)), "addUserNameUse")
                ))
                .from(pjtRegistInfo)
                .leftJoin(slipHeader).on(slipHeader.compCd.eq(pjtRegistInfo.orgId), slipHeader.slipNo.eq(pjtRegistInfo.slipNo))
                .where(pjtRegistInfo.orgId.eq(pjtRegistInfoDto.getOrgId())
                                .and(projectCdEq(pjtRegistInfoDto.getProjectCode()))
                                .and(addUserIdEq(pjtRegistInfoDto.getAddUserId()))
                                .and(bdeptCodeEq(pjtRegistInfoDto.getBdeptCode()))
                                .and(pjtRegistInfo.degree.eq(JPAExpressions.select(pjtRegistInfo1.degree.max())
                                                .from(pjtRegistInfo1)
                                                .where(pjtRegistInfo1.orgId.eq(pjtRegistInfo.orgId), pjtRegistInfo1.projectManageNo.eq(pjtRegistInfo.projectManageNo))))
                )
                .orderBy(pjtRegistInfo.projectManageNo.desc())
                .fetch();

    }

    private BooleanExpression projectCdEq(String projectCode) {
        return hasText(projectCode) ? pjtRegistInfo.projectCode.eq(projectCode) : null;
    }

    private BooleanExpression addUserIdEq(String addUserId) {
        return hasText(addUserId) ? pjtRegistInfo.addUserId.eq(addUserId) : null;
    }

    private BooleanExpression bdeptCodeEq(String bdeptCode) {
        return hasText(bdeptCode) ? pjtRegistInfo.bdeptCode.eq(bdeptCode) : null;
    }


}


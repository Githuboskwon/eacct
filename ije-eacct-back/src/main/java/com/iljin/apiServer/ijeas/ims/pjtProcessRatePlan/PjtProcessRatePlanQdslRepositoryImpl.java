package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import static com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfo.pjtRegistInfo;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;

@RequiredArgsConstructor
@Repository
public class PjtProcessRatePlanQdslRepositoryImpl implements PjtProcessRatePlanQdslRepository {

    private final Util util;

    private final JPAQueryFactory queryFactory;

    @Override
    public PjtProcessRatePlanDto getProcessInfo(PjtProcessRatePlanDto pjtProcessRatePlanDto) {

        String compCd = util.getLoginCompCd();

        QPjtRegistInfo pjtRegistInfo1 = new QPjtRegistInfo("pjtRegistInfo");

        return queryFactory
                .select(new QPjtProcessRatePlanDto(
                        pjtRegistInfo.orgId,
                        pjtRegistInfo.projectId,
                        pjtRegistInfo.projectCode,
                        pjtRegistInfo.projectName,
                        pjtRegistInfo.taskId,
                        pjtRegistInfo.taskNumber,
                        pjtRegistInfo.taskName,
                        pjtRegistInfo.projectManageNo,
                        pjtRegistInfo.degree,
                        pjtRegistInfo.customerName,
                        pjtRegistInfo.startDate,
                        pjtRegistInfo.endDate,
                        pjtRegistInfo.totalMonth,
                        pjtRegistInfo.ctAmount,
                        pjtRegistInfo.ctAmountFor,
                        pjtRegistInfo.docNo,
                        pjtRegistInfo.remark,
                        pjtRegistInfo.projectStatus,
                        pjtRegistInfo.pjtSiteManagerUserId,
                        pjtRegistInfo.pjtSiteManagerUserName,
                        pjtRegistInfo.pjtSiteManagerPhone,
                        pjtRegistInfo.pjtBusinessUserId,
                        pjtRegistInfo.pjtBusinessUserName,
                        pjtRegistInfo.pjtLocation,
                        pjtRegistInfo.pjtType,
                        pjtRegistInfo.slipNo,
                        ExpressionUtils.as(JPAExpressions.select(slipHeader.status).from(slipHeader).where(slipHeader.slipNo.eq(pjtRegistInfo.slipNo)),"status"),
                        pjtRegistInfo.jobId,
                        pjtRegistInfo.addDate,
                        pjtRegistInfo.addTime,
                        pjtRegistInfo.addUserId,
                        pjtRegistInfo.changeDate,
                        pjtRegistInfo.changeTime,
                        pjtRegistInfo.changeUserId
                ))
                .from(pjtRegistInfo)
                .where(pjtRegistInfo.orgId.eq(compCd),
                        pjtRegistInfo.projectManageNo.eq(pjtProcessRatePlanDto.getProjectManageNo()),
                        pjtRegistInfo.degree.eq(JPAExpressions.select(pjtRegistInfo1.degree.max())
                                .from(pjtRegistInfo1)
                                .where(pjtRegistInfo1.orgId.eq(compCd), pjtRegistInfo1.projectManageNo.eq(pjtProcessRatePlanDto.getProjectManageNo())))
                )
                .fetchOne();
    }

}


package com.iljin.apiServer.ijeas.approval;


import static com.iljin.apiServer.ijeas.approval.QApprovalDetail.approvalDetail;
import static com.iljin.apiServer.ijeas.approval.QApprovalHeader.approvalHeader;
import static com.iljin.apiServer.ijeas.card.QCardUseList.cardUseList;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeader.erpSlipHeader;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.trx.QTrx.trx;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.slip.SlipStatusType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ApprovalQdslRepositoryImpl implements ApprovalQdslRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private final Util util;


      @Override
      public List<ApprovalHeaderDto> getApprTodoList(ApprovalHeaderDto approvalHeaderDto) {

          List<String> statusCodes = Arrays.asList(SlipStatusType.AP.getCode());

          return queryFactory
              .select(new QApprovalHeaderDto(
                  approvalHeader.apprGroupId,
                  approvalHeader.slipNo,
                  approvalHeader.slipType,
                  trx.trxTypeNm.as("slipTypeNm"),
                  approvalHeader.docTitleNm,
                  approvalHeader.docContents,
                  approvalHeader.slipStatus,
                  codeDetail.detailNm.as("slipStatusNm"),
                  approvalDetail.apprNo,
                  approvalDetail.aprverId,
                  approvalDetail.aprverNm,
                  approvalHeader.draftId,
                  employee.empNm.as("draftNm"),
                  employee.deptCd.as("draftDeptCd"),
                  employee.deptNm.as("draftDeptNm"),
                  employee.jobDutNm.as("draftUserJob"),
                  approvalHeader.regDtm.as("draftDtm"),
                  slipHeader.taxbillTotalAmt.as("totAmt"),
                  slipHeader.headerRemark,
                  erpSlipHeader.supplyAmount,
                  erpSlipHeader.taxAmount,
                  slipHeader.usedAmt
              ))
              .from(slipHeader)
              .leftJoin(erpSlipHeader)
                  .on(slipHeader.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})",erpSlipHeader.orgId)),
                      slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
              .leftJoin(approvalHeader)
                  .on(slipHeader.compCd.eq(approvalHeader.compCd),
                      slipHeader.approvalGroupId.eq(approvalHeader.apprGroupId))
              .leftJoin(approvalDetail)
                  .on(approvalHeader.compCd.eq(approvalDetail.compCd),
                      approvalHeader.apprGroupId.eq(approvalDetail.apprGroupId),
                      approvalHeader.slipNo.eq(approvalDetail.slipNo),
                      approvalHeader.nextAppUserId.eq(approvalDetail.aprverId),
                      approvalDetail.apprTypeCd.eq("01"))
              .leftJoin(codeDetail)
                  .on(slipHeader.compCd.eq(codeDetail.compCd),
                      approvalHeader.slipStatus.eq(codeDetail.detailCd),
                      codeDetail.groupCd.eq("SLIP_STAT_CD"))
              .leftJoin(employee)
                  .on(approvalHeader.compCd.eq(employee.compCd),
                      approvalHeader.draftId.eq(employee.empNo))
              .leftJoin(trx)
                  .on(approvalHeader.compCd.eq(trx.compCd),
                      approvalHeader.slipType.eq(trx.trxTypeCd))
              .where(
                  draftDtmGoe(approvalHeaderDto.getSearchDtmFr()),
                  draftDtmLoe(approvalHeaderDto.getSearchDtmTo()),
                  docTitleNmContains(approvalHeaderDto.getDocTitleNm()),
                  nextAppUserIdEq(approvalHeaderDto.getLoginId()),
                  draftIdEq(approvalHeaderDto.getDraftId()),
                  deptCdEq(approvalHeaderDto.wrtDeptCd),
                  statusNvlIn(statusCodes),
                  slipNoContains(approvalHeaderDto.slipNo),
                  slipHeader.slipType.notIn("91")
              )
              .orderBy(approvalHeader.apprGroupId.desc())
              .fetch();

      }

    @Override
    public List<ApprovalHeaderDto> getApprDoneList(ApprovalHeaderDto approvalHeaderDto) {

        List<String> statusCodes = Arrays.asList(
                SlipStatusType.AP.getCode(),
                SlipStatusType.AR.getCode(),
                SlipStatusType.CP.getCode(),
                SlipStatusType.CR.getCode(),
                SlipStatusType.CC.getCode(),
                SlipStatusType.IE.getCode(),
                SlipStatusType.IC.getCode(),
                SlipStatusType.SD.getCode(),
                SlipStatusType.FP.getCode(),
                SlipStatusType.FH.getCode(),
                SlipStatusType.FC.getCode(),
                SlipStatusType.FR.getCode(),
                SlipStatusType.SC.getCode()
        );

        return queryFactory
            .select(new QApprovalHeaderDto(
                approvalHeader.apprGroupId,
                approvalHeader.slipNo,
                approvalHeader.slipType,
                trx.trxTypeNm.as("slipTypeNm"),
                approvalHeader.docTitleNm,
                approvalHeader.docContents,
                approvalHeader.slipStatus,
                codeDetail.detailNm.as("slipStatusNm"),
                approvalDetail.apprNo,
                approvalDetail.aprverId,
                approvalDetail.aprverNm,
                approvalHeader.draftId,
                employee.empNm.as("draftNm"),
                employee.deptCd.as("draftDeptCd"),
                employee.deptNm.as("draftDeptNm"),
                employee.jobDutNm.as("draftUserJob"),
                approvalHeader.regDtm.as("draftDtm"),
                slipHeader.taxbillTotalAmt.as("totAmt"),
                slipHeader.headerRemark
            ))
            .from(slipHeader)
            .leftJoin(erpSlipHeader)
                .on(slipHeader.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipHeader.orgId)),
                    slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
            .leftJoin(approvalHeader)
                .on(slipHeader.compCd.eq(approvalHeader.compCd),
                    slipHeader.approvalGroupId.eq(approvalHeader.apprGroupId))
            .leftJoin(approvalDetail)
                .on(approvalHeader.compCd.eq(approvalDetail.compCd),
                    approvalHeader.apprGroupId.eq(approvalDetail.apprGroupId),
                    approvalHeader.slipNo.eq(approvalDetail.slipNo),
                    approvalDetail.apprTypeCd.eq("01"))
            .leftJoin(codeDetail)
                .on(slipHeader.compCd.eq(codeDetail.compCd),
                    approvalHeader.slipStatus.eq(codeDetail.detailCd),
                    codeDetail.groupCd.eq("SLIP_STAT_CD"))
            .leftJoin(employee)
                .on(approvalHeader.compCd.eq(employee.compCd),
                    approvalHeader.draftId.eq(employee.empNo))
            .leftJoin(trx)
                .on(approvalHeader.compCd.eq(trx.compCd),
                    approvalHeader.slipType.eq(trx.trxTypeCd))
            .where(
                draftDtmGoe(approvalHeaderDto.getSearchDtmFr()),
                draftDtmLoe(approvalHeaderDto.getSearchDtmTo()),
                docTitleNmContains(approvalHeaderDto.getDocTitleNm()),
                draftIdEq(approvalHeaderDto.getDraftId()),
                aAprverIdEq(approvalHeaderDto.getLoginId()),
                statusNvlIn(statusCodes),
                slipStatusEq(approvalHeaderDto.slipStatus),
                slipNoContains(approvalHeaderDto.slipNo),
                slipHeader.slipType.notIn("91")
            )
            .orderBy(approvalHeader.apprGroupId.desc())
            .fetch();

    }

    @Override
    public List<ApprovalHeaderDto> getApprReqList(ApprovalHeaderDto approvalHeaderDto) {

        List<String> statusCodes = Arrays.asList(
                SlipStatusType.AP.getCode(),
                SlipStatusType.AR.getCode(),
                SlipStatusType.CP.getCode(),
                SlipStatusType.CR.getCode(),
                SlipStatusType.CC.getCode(),
                SlipStatusType.IE.getCode(),
                SlipStatusType.IC.getCode(),
                SlipStatusType.SD.getCode(),
                SlipStatusType.FP.getCode(),
                SlipStatusType.FH.getCode(),
                SlipStatusType.FC.getCode(),
                SlipStatusType.FR.getCode(),
                SlipStatusType.SC.getCode()
        );

        return queryFactory
            .select(new QApprovalHeaderDto(
                approvalHeader.apprGroupId,
                approvalHeader.slipNo,
                approvalHeader.slipType,
                trx.trxTypeNm.as("slipTypeNm"),
                approvalHeader.docTitleNm,
                approvalHeader.docContents,
                approvalHeader.slipStatus,
                codeDetail.detailNm.as("slipStatusNm"),
                approvalHeader.draftId,
                employee.empNm.as("draftNm"),
                employee.deptCd.as("draftDeptCd"),
                employee.deptNm.as("draftDeptNm"),
                employee.jobDutNm.as("draftUserJob"),
                approvalHeader.regDtm.as("draftDtm"),
                slipHeader.taxbillTotalAmt.as("totAmt"),
                slipHeader.headerRemark
            ))
            .from(slipHeader)
            .leftJoin(erpSlipHeader)
                .on(slipHeader.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipHeader.orgId)),
                    slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
            .leftJoin(approvalHeader)
                .on(slipHeader.compCd.eq(approvalHeader.compCd),
                    slipHeader.approvalGroupId.eq(approvalHeader.apprGroupId))
//            .leftJoin(approvalDetail)
//                .on(slipHeader.compCd.eq(approvalDetail.compCd),
//                    slipHeader.approvalGroupId.eq(approvalDetail.apprGroupId),
//                    approvalDetail.aprverId.eq(approvalHeader.nextAppUserId))
            .leftJoin(codeDetail)
                .on(slipHeader.compCd.eq(codeDetail.compCd),
                    approvalHeader.slipStatus.eq(codeDetail.detailCd),
                    codeDetail.groupCd.eq("SLIP_STAT_CD"))
            .leftJoin(employee)
                .on(approvalHeader.compCd.eq(employee.compCd),
                    approvalHeader.draftId.eq(employee.empNo))
            .leftJoin(trx)
                .on(approvalHeader.compCd.eq(trx.compCd),
                    approvalHeader.slipType.eq(trx.trxTypeCd))
            .where(
                headerDraftDtmGoe(approvalHeaderDto.getSearchDtmFr()),
                headerDraftDtmLoe(approvalHeaderDto.getSearchDtmTo()),
                docTitleNmContains(approvalHeaderDto.getDocTitleNm()),
                draftIdEq(approvalHeaderDto.getLoginId()),
                statusNvlIn(statusCodes),
                slipStatusEq(approvalHeaderDto.slipStatus),
                slipNoContains(approvalHeaderDto.slipNo),
                slipHeader.slipType.notIn("91")
            )
            .orderBy(approvalHeader.apprGroupId.desc())
            .fetch();
    }

    @Override
    public List<ApprovalHeaderDto> getRefList(ApprovalHeaderDto approvalHeaderDto) {

        List<String> statusCodes = Arrays.asList(
                SlipStatusType.AP.getCode(),
                SlipStatusType.AR.getCode(),
                SlipStatusType.CP.getCode(),
                SlipStatusType.CR.getCode(),
                SlipStatusType.CC.getCode(),
                SlipStatusType.IE.getCode(),
                SlipStatusType.IC.getCode(),
                SlipStatusType.SD.getCode(),
                SlipStatusType.FP.getCode(),
                SlipStatusType.FH.getCode(),
                SlipStatusType.FC.getCode(),
                SlipStatusType.FR.getCode(),
                SlipStatusType.SC.getCode()
        );

        return queryFactory
                .select(new QApprovalHeaderDto(
                        approvalHeader.apprGroupId,
                        approvalHeader.slipNo,
                        approvalHeader.slipType,
                        trx.trxTypeNm.as("slipTypeNm"),
                        approvalHeader.docTitleNm,
                        approvalHeader.docContents,
                        approvalHeader.slipStatus,
                        codeDetail.detailNm.as("slipStatusNm"),
                        approvalHeader.draftId,
                        employee.empNm.as("draftNm"),
                        employee.deptCd.as("draftDeptCd"),
                        employee.deptNm.as("draftDeptNm"),
                        employee.jobDutNm.as("draftUserJob"),
                        approvalHeader.regDtm.as("draftDtm"),
                        slipHeader.taxbillTotalAmt.as("totAmt"),
                        slipHeader.headerRemark
                ))
                .from(slipHeader)
                .leftJoin(erpSlipHeader)
                .on(slipHeader.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipHeader.orgId)),
                        slipHeader.slipHeaderId.eq(erpSlipHeader.slipHeaderId))
                .leftJoin(approvalHeader)
                .on(slipHeader.compCd.eq(approvalHeader.compCd),
                        slipHeader.approvalGroupId.eq(approvalHeader.apprGroupId))
//            .leftJoin(approvalDetail)
//                .on(slipHeader.compCd.eq(approvalDetail.compCd),
//                    slipHeader.approvalGroupId.eq(approvalDetail.apprGroupId),
//                    approvalDetail.aprverId.eq(approvalHeader.nextAppUserId))
                .leftJoin(codeDetail)
                .on(slipHeader.compCd.eq(codeDetail.compCd),
                        approvalHeader.slipStatus.eq(codeDetail.detailCd),
                        codeDetail.groupCd.eq("SLIP_STAT_CD"))
                .leftJoin(employee)
                .on(approvalHeader.compCd.eq(employee.compCd),
                        approvalHeader.draftId.eq(employee.empNo))
                .leftJoin(trx)
                .on(approvalHeader.compCd.eq(trx.compCd),
                        approvalHeader.slipType.eq(trx.trxTypeCd))
                .where(
                        headerDraftDtmGoe(approvalHeaderDto.getSearchDtmFr()),
                        headerDraftDtmLoe(approvalHeaderDto.getSearchDtmTo()),
                        docTitleNmContains(approvalHeaderDto.getDocTitleNm()),
                        refUserIdContains(approvalHeaderDto.getLoginId()),
                        statusNvlIn(statusCodes),
                        slipStatusEq(approvalHeaderDto.slipStatus),
                        slipNoContains(approvalHeaderDto.slipNo),
                        slipHeader.slipType.notIn("91")
                )
                .orderBy(approvalHeader.apprGroupId.desc())
                .fetch();
    }

    @Override
    public List<ApprovalHeaderDto> findByCompCdAndSlipHeaderId(String compCd, BigDecimal apprGroupId) {
          return queryFactory
              .select(new QApprovalHeaderDto(
                  approvalDetail.apprTypeCd,
                  approvalDetail.apprStage,
                  approvalDetail.aprverId,
                  approvalDetail.slipStatus
              ))
              .from(approvalHeader)
              .leftJoin(approvalDetail).on(approvalDetail.compCd.eq(approvalHeader.compCd),
                  approvalDetail.apprGroupId.eq(approvalHeader.apprGroupId),
                  approvalDetail.apprStage.eq(approvalHeader.nextStage))
              .join(slipHeader).on(slipHeader.compCd.eq(approvalHeader.compCd),slipHeader.slipHeaderId.eq(approvalDetail.apprGroupId))
              .where(slipHeader.compCd.eq(compCd),
                  slipHeader.slipHeaderId.eq(apprGroupId))
              .fetch();
    }

    private BooleanExpression statusNvlIn(List<String> statusCodes) {
        return erpSlipHeader.slipStatus.in(statusCodes)
                .or(slipHeader.status.in(statusCodes));
    }

    private BooleanExpression slipStatusEq(String slipStatus) {
        return hasText(slipStatus) ? approvalHeader.slipStatus.eq(slipStatus) : null;
    }

    private BooleanExpression draftDtmGoe (String searchDtmFr) {

        return Objects.nonNull(searchDtmFr) ? Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", approvalDetail.regDtm).goe(searchDtmFr) : null;
    }

    private BooleanExpression draftDtmLoe (String searchDtmTo) {
        return Objects.nonNull(searchDtmTo) ? Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", approvalDetail.regDtm).loe(searchDtmTo) : null;
    }

    private BooleanExpression headerDraftDtmGoe (String searchDtmFr) {
        return Objects.nonNull(searchDtmFr) ? Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", approvalHeader.regDtm).goe(searchDtmFr) : null;
    }

    private BooleanExpression headerDraftDtmLoe (String searchDtmTo) {
        return Objects.nonNull(searchDtmTo) ? Expressions.stringTemplate("TO_CHAR({0}, 'YYYYMMDD')", approvalHeader.regDtm).loe(searchDtmTo) : null;
    }

//    private BooleanExpression draftUserIdEq(String draftUserId) {
//        return hasText(draftUserId) ? approvalHeader.draftId.eq(draftUserId) : null;
//    }

    private BooleanExpression docTitleNmContains(String docTitleNm) {
        return hasText(docTitleNm) ? approvalHeader.docTitleNm.containsIgnoreCase(docTitleNm) : null;
    }

    private BooleanExpression refUserIdContains(String refUserId) {
        return hasText(refUserId) ? approvalHeader.refUserId.containsIgnoreCase(refUserId) : null;
    }

    private BooleanExpression nextAppUserIdEq(String loginId) {
        return hasText(loginId) ? approvalHeader.nextAppUserId.eq(loginId) : null;
    }

    private BooleanExpression draftIdEq(String draftId) {
        return hasText(draftId) ? approvalHeader.draftId.eq(draftId) : null;
    }

    private BooleanExpression deptCdEq(String wrtDeptCd) {
        return hasText(wrtDeptCd) ? employee.deptCd.eq(wrtDeptCd) : null;
    }

    private BooleanExpression aAprverIdEq(String loginId) {
        return hasText(loginId) ? approvalDetail.aAprverId.eq(loginId) : null;
    }

    private BooleanExpression slipNoContains(String slipNo) {
        return hasText(slipNo) ? approvalHeader.slipNo.containsIgnoreCase(slipNo) : null;
    }

    @Override
    public List<ApprovalHeaderDto> getNextStage(ApprovalDetailDto approvalDetailDto) {
          return queryFactory
              .select(new QApprovalHeaderDto(
                  approvalHeader.nextStage,
                  approvalHeader.nextAppUserId,
                  approvalDetail.apprTypeCd
              ))
              .from(approvalHeader)
              .leftJoin(approvalDetail).on(
                  approvalDetail.compCd.eq(approvalHeader.compCd),
                  approvalDetail.apprGroupId.eq(approvalHeader.apprGroupId),
                  approvalDetail.apprStage.eq(approvalHeader.nextStage))
              .where(approvalHeader.compCd.eq(approvalDetailDto.getCompCd()),
                  approvalHeader.apprGroupId.eq(approvalDetailDto.getApprGroupId()))
              .fetch();
    }

    @Override
    public void updateCardUseList(String compCd, BigDecimal apprGroupId) {
          queryFactory
          .update(cardUseList)
          .set(cardUseList.status, "CC")
          .where(cardUseList.compCd.eq(compCd),
              cardUseList.slipHeaderId.in(JPAExpressions.select(erpSlipHeader.slipHeaderId).from(erpSlipHeader)
                .where(erpSlipHeader.approvalGroupId.eq(apprGroupId), erpSlipHeader.orgId.eq(Integer.parseInt(compCd)))),
              cardUseList.status.ne("07"))
          .execute();

        em.flush();
        em.clear();
    }

    @Override
    public ApprovalHeaderDto getNextAppUser(ApprovalHeaderDto approvalHeaderDto) {
          return queryFactory
              .select(new QApprovalHeaderDto(
                  approvalHeader.apprGroupId,
                  approvalHeader.nextAppUserId,
                  approvalDetail.apprTypeCd,
                  employee.empNm
              ))
              .from(approvalHeader)
              .leftJoin(approvalDetail).on(approvalDetail.compCd.eq(approvalHeader.compCd),
                  approvalDetail.apprGroupId.eq(approvalHeader.apprGroupId),
                  approvalDetail.apprStage.eq(approvalHeader.nextStage))
              .leftJoin(employee).on(employee.compCd.eq(approvalHeader.compCd),
                  employee.empNo.eq(approvalHeader.nextAppUserId))
              .where(approvalHeader.compCd.eq(approvalHeaderDto.getCompCd()),
                  approvalHeader.apprGroupId.eq(approvalHeaderDto.getApprGroupId()),
                  approvalHeader.slipStatus.eq(SlipStatusType.AP.getCode()),
                  approvalHeader.nextStage.eq(approvalHeaderDto.getNextStage()))
              .fetchFirst();
    }

    @Override
    public Boolean existsByFirstStage(ApprovalHeaderDto approvalHeaderDto) {
          Integer fetchOne =  queryFactory
              .selectOne()
              .from(slipHeader)
              .innerJoin(approvalHeader).on(approvalHeader.compCd.eq(slipHeader.compCd), approvalHeader.apprGroupId.eq(slipHeader.approvalGroupId))
              .where(slipHeader.compCd.eq(util.getLoginCompCd()),
                  approvalHeader.nextStage.eq("1"),
                  slipHeader.slipNo.eq(approvalHeaderDto.getSlipNo()),
                  slipHeader.slipHeaderId.eq(approvalHeaderDto.getApprGroupId()))
              .fetchFirst();
          return fetchOne != null;
    }

    @Override
    public Integer getMaxStage(String compCd, BigDecimal apprGroupId) {
          String max = queryFactory
              .select(approvalDetail.apprStage.max())
              .from(approvalDetail)
              .where(approvalDetail.compCd.eq(compCd),
                  approvalDetail.apprGroupId.eq(apprGroupId))
              .fetchOne();
          return Integer.parseInt(max);
    }

}

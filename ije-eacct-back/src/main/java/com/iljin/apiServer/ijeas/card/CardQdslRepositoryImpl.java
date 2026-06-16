package com.iljin.apiServer.ijeas.card;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.code.QCodeDetail;
import com.iljin.apiServer.ijeas.system.emp.QEmployee;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.iljin.apiServer.ijeas.card.QCard.card;
import static com.iljin.apiServer.ijeas.card.QCardUseList.cardUseList;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipLine.erpSlipLine;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static com.iljin.apiServer.ijeas.system.acct.QAccount.account;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static com.iljin.apiServer.ijeas.system.item.QItem.item;
import static com.iljin.apiServer.ijeas.system.pjt.QProject.project;
import static com.iljin.apiServer.ijeas.system.task.QTask.task;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class CardQdslRepositoryImpl implements CardQdslRepository {

    private final JPAQueryFactory queryFactory;

    private final CardRepository cardRepository;


    private final Util util;

    private enum EntityType {
        CARD_MASTER
    }

    @Override
    public Long getBusinessCreditCardCount(CardDto cardDto) {

        QCodeDetail codeDetail1 = new QCodeDetail("codeDetail1");
        QCodeDetail codeDetail2 = new QCodeDetail("codeDetail2");
        QCodeDetail codeDetail3 = new QCodeDetail("codeDetail3");
        QCodeDetail codeDetail4 = new QCodeDetail("codeDetail4");

        return queryFactory
                .select(card.crdNo.count())
                .from(card)
                .leftJoin(codeDetail1).on(codeDetail1.compCd.eq(card.compCd), codeDetail1.useYn.eq("Y") , codeDetail1.groupCd.eq("COMP_CD") , codeDetail1.detailCd.eq(card.compCd) )
                .leftJoin(codeDetail2).on(codeDetail2.compCd.eq(card.compCd), codeDetail2.useYn.eq("Y") , codeDetail2.groupCd.eq("CRD_STAT_CD") , codeDetail2.detailCd.eq(card.crdStatCd) )
                .leftJoin(codeDetail3).on(codeDetail3.compCd.eq(card.compCd), codeDetail3.useYn.eq("Y") , codeDetail3.groupCd.eq("CRD_FG_CD") , codeDetail3.detailCd.eq(card.crdFgCd) )
                .leftJoin(codeDetail4).on(codeDetail4.compCd.eq(card.compCd), codeDetail4.useYn.eq("Y") , codeDetail4.groupCd.eq("CRD_COMP_CD") , codeDetail4.detailCd.eq(card.crdCompCd) )
                .leftJoin(employee).on(employee.compCd.eq(card.compCd), employee.empNo.eq(card.crdPssrId) )
                .where(compCdEq(cardDto.getCompCd())
                        ,crdPubCDtGoe(cardDto.getSearchDtmFr())
                        ,crdPubCDtLoe(cardDto.getSearchDtmTo())
                        ,crdStatCdEq(cardDto.getCrdStatCd())
                        ,crdNoLk(cardDto.getCrdNo())
                        ,crdPssrIdEq(cardDto.getCrdPssrId())
                        ,crdCompCdEq(cardDto.getCrdCompCd())
                        ,crdFgCdEq(cardDto.getCrdFgCd())
                )
                .fetchFirst();
    }

    @Override
    public List<CardDto> getBusinessCreditCardList(CardDto cardDto, Pageable pageable) {

        QCodeDetail codeDetail1 = new QCodeDetail("codeDetail1");
        QCodeDetail codeDetail2 = new QCodeDetail("codeDetail2");
        QCodeDetail codeDetail3 = new QCodeDetail("codeDetail3");
        QCodeDetail codeDetail4 = new QCodeDetail("codeDetail4");

        return queryFactory
                .select(new QCardDto(
                        card.crdNo,
                        card.compCd,
                        codeDetail1.detailNm.trim().as("compNm"),
                        card.crdStatCd,
                        codeDetail2.detailNm.trim().as("crdStatNm"),
                        card.crdFgCd,
                        codeDetail3.detailNm.trim().as("crdFgNm"),
                        card.crdPssrId,
                        employee.empNm.as("crdPssrNm"),
                        card.crdPubcDt,
                        card.crdVldYm,
                        card.crdPlmtAmt,
                        card.crdCompCd,
                        codeDetail4.detailNm.trim().as("crdCompNm"),
                        card.crdOln
                        ))
                .from(card)
                .leftJoin(codeDetail1).on(codeDetail1.compCd.eq(card.compCd), codeDetail1.useYn.eq("Y") , codeDetail1.groupCd.eq("COMP_CD") , codeDetail1.detailCd.eq(card.compCd) )
                .leftJoin(codeDetail2).on(codeDetail2.compCd.eq(card.compCd), codeDetail2.useYn.eq("Y") , codeDetail2.groupCd.eq("CRD_STAT_CD") , codeDetail2.detailCd.eq(card.crdStatCd) )
                .leftJoin(codeDetail3).on(codeDetail3.compCd.eq(card.compCd), codeDetail3.useYn.eq("Y") , codeDetail3.groupCd.eq("CRD_FG_CD") , codeDetail3.detailCd.eq(card.crdFgCd) )
                .leftJoin(codeDetail4).on(codeDetail4.compCd.eq(card.compCd), codeDetail4.useYn.eq("Y") , codeDetail4.groupCd.eq("CRD_COMP_CD") , codeDetail4.detailCd.eq(card.crdCompCd) )
                .leftJoin(employee).on(employee.compCd.eq(card.compCd), employee.empNo.eq(card.crdPssrId) )
                .where(compCdEq(cardDto.getCompCd())
                ,crdPubCDtGoe(cardDto.getSearchDtmFr())
                ,crdPubCDtLoe(cardDto.getSearchDtmTo())
                ,crdStatCdEq(cardDto.getCrdStatCd())
                ,crdNoLk(cardDto.getCrdNo())
                ,crdPssrIdEq(cardDto.getCrdPssrId())
                ,crdCompCdEq(cardDto.getCrdCompCd())
                ,crdFgCdEq(cardDto.getCrdFgCd())
                )
                .orderBy(card.crdNo.desc() , codeDetail4.detailNm.asc(), card.crdPubcDt.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<CardDto> getBusinessCreditCardPopList(CardDto cardDto, Pageable pageable) {

        QCodeDetail codeDetail1 = new QCodeDetail("codeDetail1");
        QCodeDetail codeDetail2 = new QCodeDetail("codeDetail2");
        QCodeDetail codeDetail3 = new QCodeDetail("codeDetail3");
        QCodeDetail codeDetail4 = new QCodeDetail("codeDetail4");

        return queryFactory
                .select(new QCardDto(
                        card.crdNo,
                        card.compCd,
                        codeDetail1.detailNm.trim().as("compNm"),
                        card.crdStatCd,
                        codeDetail2.detailNm.trim().as("crdStatNm"),
                        card.crdFgCd,
                        codeDetail3.detailNm.trim().as("crdFgNm"),
                        card.crdPssrId,
                        employee.empNm.as("crdPssrNm"),
                        card.crdPubcDt,
                        card.crdVldYm,
                        card.crdPlmtAmt,
                        card.crdCompCd,
                        codeDetail4.detailNm.trim().as("crdCompNm"),
                        card.crdOln
                ))
                .from(card)
                .leftJoin(codeDetail1).on(codeDetail1.compCd.eq(card.compCd), codeDetail1.useYn.eq("Y") , codeDetail1.groupCd.eq("COMP_CD") , codeDetail1.detailCd.eq(card.compCd) )
                .leftJoin(codeDetail2).on(codeDetail2.compCd.eq(card.compCd), codeDetail2.useYn.eq("Y") , codeDetail2.groupCd.eq("CRD_STAT_CD") , codeDetail2.detailCd.eq(card.crdStatCd) )
                .leftJoin(codeDetail3).on(codeDetail3.compCd.eq(card.compCd), codeDetail3.useYn.eq("Y") , codeDetail3.groupCd.eq("CRD_FG_CD") , codeDetail3.detailCd.eq(card.crdFgCd) )
                .leftJoin(codeDetail4).on(codeDetail4.compCd.eq(card.compCd), codeDetail4.useYn.eq("Y") , codeDetail4.groupCd.eq("CRD_COMP_CD") , codeDetail4.detailCd.eq(card.crdCompCd) )
                .leftJoin(employee).on(employee.compCd.eq(card.compCd), employee.empNo.eq(card.crdPssrId) )
                .where(compCdEq(cardDto.getCompCd())
                        ,crdPubCDtGoe(cardDto.getSearchDtmFr())
                        ,crdPubCDtLoe(cardDto.getSearchDtmTo())
                        ,crdStatCdEq(cardDto.getCrdStatCd())
                        ,crdNoLk(cardDto.getCrdNo())
                        ,crdPssrIdEq(cardDto.getCrdPssrId())
                        ,crdCompCdEq(cardDto.getCrdCompCd())
                        ,crdFgCdIn(cardDto.getCrdFgCd())
                )
                .orderBy(card.crdNo.desc() , codeDetail4.detailNm.asc(), card.crdPubcDt.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<CardDto> getCardDetail(String crdNo) {

        QEmployee employee1 = new QEmployee("employee1");
        QEmployee employee2 = new QEmployee("employee2");

        return queryFactory
                .select(new QCardDto(
                        card.crdNo,
                        card.crdCompCd,
                        card.crdStatCd,
                        card.crdPssrId,
                        employee1.empNm.as("crdPssrNm"),
                        employee1.jobDutNm.as("crdPssrDut"),
                        employee1.deptNm.as("crdPssrDept"),
                        employee1.compCd.as("crdPssrCompCd"),
                        employee1.compNm.as("crdPssrCompNm"),
                        card.crdUsgCd,
                        card.crdPubcDt,
                        card.crdAbltDt,
                        card.crdPlmtAmt,
                        card.crdVldYm,
                        card.preCrdNo,
                        card.crdFgCd,
                        card.crdUseId,
                        employee2.empNm.as("crdUseNm"),
                        employee2.jobDutNm.as("crdUseDut"),
                        employee2.deptNm.as("crdUseDept"),
                        card.bnkCd,
                        card.stlDd,
                        card.crdUseStrDt,
                        card.crdUseEndDt,
                        card.stlBacctNo,
                        card.crdOln,
                        card.vendCd,
                        card.vendNm
                ))
                .from(card)
                .leftJoin(employee1).on(employee1.compCd.eq(card.compCd), employee1.empNo.eq(card.crdPssrId) )
                .leftJoin(employee2).on(employee2.compCd.eq(card.compCd), employee2.empNo.eq(card.crdUseId) )
                .where(card.crdNo.eq(crdNo))
                .fetch();
    }

    @Override
    public List<CardUseListDto> getCardUseList(CardUseListDto cardUseListDto) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QCardUseListDto(
                        cardUseList.compCd,
                        cardUseList.usedNo,
                        cardUseList.cardNo,
                        employee.jobGradeNm,
                        employee.deptNm,
                        cardUseList.userId,
                        cardUseList.userNm,
                        cardUseList.usedDt,
                        cardUseList.usedTime,
                        cardUseList.buyDt,
                        cardUseList.status.coalesce("01").as("status"),
                        cardUseList.cancelFlag,
                        cardUseList.apprNo,
                        cardUseList.irsNo,
                        cardUseList.usedAmt,
                        new CaseBuilder().when(cardUseList.abroadFlag.eq("Y")).then(cardUseList.usedAmt).otherwise(cardUseList.originAmt).as("originAmt"),
                        new CaseBuilder().when(cardUseList.abroadFlag.eq("Y")).then("0").otherwise(cardUseList.surtax).as("surtax"),
                        new CaseBuilder().when(cardUseList.abroadFlag.eq("N")).then(cardUseList.serviceCharge).otherwise("0").as("serviceCharge"),
                        cardUseList.storeNm,
                        cardUseList.storeAddr,
                        cardUseList.slipNo,
                        slipHeader.status.as("slipStatus"),
                        cardUseList.mccNm,
                        cardUseList.taxType,
                        cardUseList.abroadFlag,
                        cardUseList.temp1,
                        cardUseList.temp3,
                        item.itemGroupNm.as("productNm"),
                        employee.deptNm.as("deptNmBack"),
                        employee.cctrNm,
                        JPAExpressions.select(account.acctNm).distinct().from(account).where(erpSlipLine.acctCode.eq(account.acctCd), account.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipLine.orgId)) , erpSlipLine.drCr.eq(account.drCr)),
//                        account.acctNm.as("trAcctNm"),
                        erpSlipLine.taxCode,
                        project.projectNm.as("pjtNm"),
                        task.taskNm.as("taskName"),
                        erpSlipLine.description.as("tText")
                ))
                .from(cardUseList)
                .leftJoin(employee).on(employee.compCd.eq(cardUseList.compCd), employee.empNo.eq(cardUseList.employeeNo) )
                .leftJoin(erpSlipLine).on(cardUseList.slipHeaderId.eq(erpSlipLine.slipHeaderId),cardUseList.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipLine.orgId)) , cardUseList.slipLineId.eq(erpSlipLine.slipLineId), cardUseList.usedNo.eq(erpSlipLine.cardUsedNo))
                .leftJoin(slipHeader).on(cardUseList.slipHeaderId.eq(slipHeader.slipHeaderId),cardUseList.compCd.eq(slipHeader.compCd))
                .leftJoin(item).on(erpSlipLine.itemGroupCode.eq(item.itemGroupCd),item.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipLine.orgId)))
//                .leftJoin(account).on(erpSlipLine.acctCode.eq(account.acctCd) , erpSlipLine.drCr.eq(account.drCr))
                .leftJoin(project).on(erpSlipLine.projectCode.eq(project.projectCd) , project.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipLine.orgId)) , erpSlipLine.projectId.eq(project.projectId) )
                .leftJoin(task).on(erpSlipLine.projectId.eq(task.projectId), task.compCd.eq(Expressions.stringTemplate("TO_CHAR({0})", erpSlipLine.orgId)) , erpSlipLine.taskCode.eq(task.taskNo) , erpSlipLine.taskId.eq(task.taskId) )
                .leftJoin(card).on(card.compCd.eq(compCd) ,card.crdNo.eq(cardUseList.cardNo))
                .where(
                cardUseList.compCd.eq(compCd),
                useDtGoe(cardUseListDto.getSearchDtmFr()),
                useDtLoe(cardUseListDto.getSearchDtmTo()),
                userIdEq(cardUseListDto.getUserId()),
                cctrCdEq(cardUseListDto.getCctrCd()),
                deptCdEq(cardUseListDto.getBlgDeptCd()),
                statusEq(cardUseListDto.getStatus()),
                cardTypeEq(cardUseListDto.getCardType()),
                cardLk(cardUseListDto.getCardNo()),
                nvlStatus(cardUseList.status).notIn("SD", "FR" , "SC")
                )
                .orderBy(cardUseList.usedDt.desc() , cardUseList.usedTime.desc())
                .fetch();
    }

    @Override
    public List<CardUseListDto> getSimpleCardUsedData(String apprNo) {

        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QCardUseListDto(
                        cardUseList.cardNo
                        ,cardUseList.usedDt
                        ,cardUseList.cardComNm
                        ,cardUseList.originAmt
                        ,cardUseList.surtax
                        ,cardUseList.serviceCharge
                        ,cardUseList.usedAmt
                        ,cardUseList.apprNo
                        ,cardUseList.storeNm
                        ,cardUseList.storeAddr
                        ,cardUseList.storeCd
                        ,cardUseList.mccNm
                        ,cardUseList.irsNo
                        ,cardUseList.storeOwner
                        ,cardUseList.userNm
                        ))
                .from(cardUseList)
                .where(cardUseList.compCd.eq(compCd),
                        cardUseList.apprNo.eq(apprNo))
                .fetch();
    }


// AS-IS와 다른 쿼리임. CUSTOM으로 새로 개발함.
//    @Override
//    public List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto) {
//
//        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
//        String compCd = util.getLoginCompCd();
//
//        BigDecimal slipHeaderId = BigDecimal.valueOf(0);
//
//        return queryFactory
//                .select(new QCardUseListDto(
//                        surTaxCode.taxRateId.as("taxId"),
//                        surTaxCode.taxAcctCode,
//                        surTaxCode.percentageRate.as("taxPercentageRate"),
//                        cardUseList.compCd,
//                        cardUseList.usedNo,
//                        cardUseList.cardNo,
//                        new CaseBuilder()
//                                .when(cardUseList.slipNo.substring(1,2).eq("TG"))
//                                .then(cardUseList.slipNo)
//                                .otherwise((JPAExpressions
//                                        .select(slipDetail.slipNo.coalesce(""))
//                                        .from(slipDetail)
//                                        .where(slipDetail.bungaeNo.eq(cardUseList.slipNo))
//                                )).as("slipNo"),
//                        cardUseList.abroadFlag,
//                        cardUseList.usedDt,
//                        cardUseList.buyDt,
//                        cardUseList.usedTime,
//                        cardUseList.userId,
//                        cardUseList.userNm,
//                        cardUseList.deptCd,
//                        cardUseList.deptNm,
//                        cardUseList.irsNo,
//                        cardUseList.storeCd,
//                        cardUseList.storeNm,
//                        cardUseList.mccNm,
//                        cardUseList.usedAmt,
//                        cardUseList.usedCur,
//                        cardUseList.usedForAmt,
//                        cardUseList.cardComCd,
//                        cardUseList.cardComNm,
//                        cardUseList.originAmt,
//                        cardUseList.surtax,
//                        cardUseList.serviceCharge,
//                        cardUseList.status,
//                        JPAExpressions
//                                .select(codeDetail.detailNm.as("statusText"))
//                                .from(codeDetail)
//                                .where(codeDetail.groupCd.eq("PROGRESS_STATUS_CD") , codeDetail.detailCd.eq(cardUseList.status)),
//                        cardUseList.cancelFlag,
//                        new CaseBuilder().when(cardUseList.cancelFlag.eq("N")).then("승인").otherwise("취소").as("cancelFlagText"),
//                        cardUseList.employeeNo,
//                        cardUseList.storeOwner,
//                        cardUseList.storeAddr,
//                        cardUseList.desc1,
//                        cardUseList.desc4,
//                        cardUseList.mccCd,
//                        cardUseList.apprNo,
//                        cardUseList.taxType,
//                        new CaseBuilder().when(cardUseList.surtax.eq("0")).then("N").otherwise("Y").as("taxFlag"),
//                        new CaseBuilder().when(cardUseList.surtax.eq("0")).then("불공제").otherwise("공제").as("taxFlagText"),
//                        cardUseList.temp1,
//                        cardUseList.cardType,
//                        cardUseList.taxTypeCd
//                ))
//                .from(cardUseList)
//                .leftJoin(erpGlCodes).on(erpGlCodes.lineMeaning.eq(cardUseList.taxType))
//                .leftJoin(surTaxCode).on(surTaxCode.taxRateCode.eq(erpGlCodes.lineAttribute1))
//                .where(cardUseList.compCd.eq(compCd),
//                        erpGlCodes.codeType.eq("CD0930"),
//                        cardUseList.slipHeaderId.nullif(slipHeaderId).eq(slipHeaderId),
//                        cardUseList.cardType.ne("F"),
//                        useDtGoe(cardUseListDto.getSearchDtmFr()),
//                        useDtLoe(cardUseListDto.getSearchDtmTo()),
//                        cardUseList.status.in("01","07","SV"),
//                        surTaxCode.taxEvidenceType.eq(cardUseListDto.getTaxEvidenceType()),
//                        surTaxCode.ledgerId.eq(Integer.valueOf(ledgerId))
//                )
//                .orderBy(cardUseList.usedDt.asc(),cardUseList.usedTime.asc())
//                .fetch();
//    }



    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? card.compCd.eq(compCd) : null;
    }

    private BooleanExpression crdPubCDtGoe (String searchDtmFr) {
        return Objects.nonNull(searchDtmFr) ? card.crdPubcDt.goe(searchDtmFr) : null;
    }

    private BooleanExpression crdPubCDtLoe (String searchDtmTo) {
        return Objects.nonNull(searchDtmTo) ? card.crdPubcDt.loe(searchDtmTo) : null;
    }

    private BooleanExpression useDtGoe (String searchDtmFr) {
        return Objects.nonNull(searchDtmFr) ? cardUseList.usedDt.goe(searchDtmFr) : null;
    }

    private BooleanExpression useDtLoe (String searchDtmTo) {
        return Objects.nonNull(searchDtmTo) ? cardUseList.usedDt.loe(searchDtmTo) : null;
    }

    private BooleanExpression crdStatCdEq(String crdStatCd) {
        return hasText(crdStatCd) ? card.crdStatCd.eq(crdStatCd) : null;
    }

    private BooleanExpression crdNoLk(String crdNo) {
        return hasText(crdNo) ? card.crdNo.likeIgnoreCase("%" + crdNo + "%") : null;
    }

    private BooleanExpression crdPssrIdEq(String crdPssrId) {
        return hasText(crdPssrId) ? card.crdPssrId.eq(crdPssrId) : null;
    }

    private BooleanExpression crdCompCdEq(String crdCompCd) {
        return hasText(crdCompCd) ? card.crdCompCd.eq(crdCompCd) : null;
    }

    private BooleanExpression crdFgCdEq(String crdFgCd) {
        return hasText(crdFgCd) ? card.crdFgCd.eq(crdFgCd) : null;
    }

    private BooleanExpression crdFgCdIn(String crdFgCd) {
        BooleanExpression booleanExpression = null;
        if(hasText(crdFgCd)){
            if(crdFgCd.equals("A")){
                booleanExpression = card.crdFgCd.in("A","C");
            }else{
                booleanExpression = card.crdFgCd.eq(crdFgCd);
            }
        }
        return booleanExpression;
    }

    private BooleanExpression userIdEq(String userId) {
        return hasText(userId) ? cardUseList.userId.eq(userId) : null;
    }

    private BooleanExpression cctrCdEq(String cctrCd) {
        return hasText(cctrCd) ? employee.cctrCd.eq(cctrCd) : null;
    }

    private BooleanExpression deptCdEq(String deptCd) {
        return hasText(deptCd) ? employee.deptCd.eq(deptCd) : null;
    }

    private BooleanExpression statusEq(String status) {
        return hasText(status) ? cardUseList.status.eq(status) : null;
    }

    private BooleanExpression cardTypeEq(String cardType) {
        return hasText(cardType) ? ("F".equals(cardType) ? card.crdFgCd.eq("F") : card.crdFgCd.ne("F") ) : null;
    }

    private BooleanExpression cardLk(String cardNo) {
        return hasText(cardNo) ? cardUseList.cardNo.likeIgnoreCase("%" +cardNo + "%") : null;
    }

    private StringPath nvlStatus(StringPath status) {
        return Objects.nonNull(status) ? cardUseList.status : Expressions.stringPath("01");
    }

}

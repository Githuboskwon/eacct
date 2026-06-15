package com.iljin.apiServer.ijeas.slip.tax;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeader.erpSlipHeader;
import static com.iljin.apiServer.ijeas.slip.tax.QTaxDtiNtsMain.taxDtiNtsMain;
import static com.iljin.apiServer.ijeas.slip.tax.TaxCompCd.*;
import static com.iljin.apiServer.ijeas.slipCommon.CompCd.*;
import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.vendor.QVendor.vendor;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class TaxDtiNtsMainQdslRepositoryImpl implements TaxDtiNtsMainQdslRepository{

    private final Util util;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaxDtiNtsMainDto> getTaxDtiNtsMainList(SearchTaxDtiNtsDto searchDto) {

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        return queryFactory
                .select(new QTaxDtiNtsMainDto(
                        taxDtiNtsMain.issueId
                          , taxDtiNtsMain.supbuyType
                          , taxDtiNtsMain.dtiType
                          , taxDtiNtsMain.amendYn
                          , taxDtiNtsMain.interfaceBatchId
                          , taxDtiNtsMain.fileIdx
                          , Expressions.stringTemplate("TO_CHAR({0}, '{1s}')", taxDtiNtsMain.dtiWdate, "YYYY-MM-DD").as("issueDate")
                          , Expressions.stringTemplate("TO_CHAR({0}, '{1s}')", taxDtiNtsMain.dtiIdate, "YYYY-MM-DD").as("issueDt")
                          , taxDtiNtsMain.dtiSdate
                          , taxDtiNtsMain.issueSystem
                          , taxDtiNtsMain.typeCode
                          , taxDtiNtsMain.typeCode.as("typeCodeTxt")
                          , taxDtiNtsMain.taxDemand.as("purpCode")
                          , taxDtiNtsMain.seqId.as("interNo")
                          , taxDtiNtsMain.exchangedDocId
                          , taxDtiNtsMain.supComRegno.substring(0,3).concat(Expressions.asString("-")).concat(taxDtiNtsMain.supComRegno.substring(3,5)).concat(Expressions.asString("-")).concat(taxDtiNtsMain.supComRegno.substring(5,10)).as("suId")
                          , taxDtiNtsMain.supBizplaceCode.as("suMin")
                          , taxDtiNtsMain.supComName.as("suName")
                          , taxDtiNtsMain.supEmpName.as("suRepres")
                          , taxDtiNtsMain.supComType.as("suBustype")
                          , taxDtiNtsMain.supComClassify.as("suIndtype")
                          , taxDtiNtsMain.supComAddr.as("suAddr")
                          , taxDtiNtsMain.supDeptName.as("suDeptname")
                          , taxDtiNtsMain.supPersonName.as("suPersname")
                          , taxDtiNtsMain.supTelNum.as("suTelno")
                          , taxDtiNtsMain.supEmail.as("suEmail")
                          , taxDtiNtsMain.byrComRegno.as("ipId")
                          , taxDtiNtsMain.byrBizplaceCode.as("ipMin")
                          , taxDtiNtsMain.byrComName.as("ipName")
                          , taxDtiNtsMain.byrEmpName.as("ipRepres")
                          , taxDtiNtsMain.byrComType.as("ipBustype")
                          , taxDtiNtsMain.byrComClassify.as("ipIndtype")
                          , taxDtiNtsMain.byrComAddr.as("ipAddr")
                          , taxDtiNtsMain.byrDeptName.as("ipDeptname1")
                          , taxDtiNtsMain.byrDeptName2.as("ipDeptname2")
                          , taxDtiNtsMain.byrPersonName.as("ipPersname1")
                          , taxDtiNtsMain.byrPersonName2.as("ipPersname2")
                          , taxDtiNtsMain.byrTelNum.as("ipTelno1")
                          , taxDtiNtsMain.byrTelNum2.as("ipTelno2")
                          , taxDtiNtsMain.byrEmail.as("ipEmail1")
                          , taxDtiNtsMain.byrEmail2.as("ipEmail2")
                          , taxDtiNtsMain.byrBusinessTypeCode.as("ipTypecode")
                          , taxDtiNtsMain.brokerComRegno.as("bpId")
                          , taxDtiNtsMain.brokerBizplaceCode.as("bpMin")
                          , taxDtiNtsMain.brokerComName.as("bpName")
                          , taxDtiNtsMain.brokerEmpName.as("bpRepres")
                          , taxDtiNtsMain.brokerComType.as("bpBustype")
                          , taxDtiNtsMain.brokerComClassify.as("bpIndtype")
                          , taxDtiNtsMain.brokerComAddr.as("bpAddr")
                          , taxDtiNtsMain.brokerDeptName.as("bpDeptname")
                          , taxDtiNtsMain.brokerPersonName.as("bpPersname")
                          , taxDtiNtsMain.brokerTelNum.as("bpTelno")
                          , taxDtiNtsMain.brokerEmail.as("bpEmail")
                          , taxDtiNtsMain.cashAmount
                          , taxDtiNtsMain.checkAmount
                          , taxDtiNtsMain.noteAmount
                          , taxDtiNtsMain.receivableAmount
                          , taxDtiNtsMain.supAmount.as("chargetotal")
                          , taxDtiNtsMain.taxAmount.nullif(BigDecimal.valueOf(0)).as("taxtotal")
                          , taxDtiNtsMain.totalAmount.as("grandtotal")
                          , taxDtiNtsMain.remark.as("descText1")
                          , taxDtiNtsMain.remark2.as("descText2")
                          , taxDtiNtsMain.remark3.as("descText3")
                          , taxDtiNtsMain.amendCode
                          , taxDtiNtsMain.createdBy.as("ernam")
                          , taxDtiNtsMain.creationDate.as("erdat")
                          , taxDtiNtsMain.lastUpdatedBy
                          , Expressions.asString("01") // taxStatus
                          , new CaseBuilder().when(taxDtiNtsMain.dtiType.eq("01")).then("세금계산서")
                                             .when(taxDtiNtsMain.dtiType.eq("02")).then("계산서")
                                             .otherwise("기타").as("slipTypeTet")
                )).from(taxDtiNtsMain)
                .where(compCdEq(compCd, taxDtiNtsMain.byrComRegno)
                        ,taxDtiNtsMain.supbuyType.eq("AP")
                        ,taxSmartbillNoNe(searchDto.getSearchTaxSmartbillNo())
                        ,taxDtiNtsMain.issueId.notIn(
                                JPAExpressions.select(erpSlipHeader.taxSmartbillNo)
                                              .from(erpSlipHeader)
                                              .where(erpSlipHeader.slipStatus.notIn("AR", "CR", "SC", "FR")
                                                    ,erpSlipHeader.taxSmartbillNo.isNotNull()
                                                    ,slipHeaderIdEq(searchDto.searchSlipHeaderId)
                                              )
                        )
                        ,dtiWdateFromGoe(searchDto.getSearchFrom())
                        ,dtiWdateToLoe(searchDto.getSearchTo())
                        ,supComRegnoSearch(searchDto.getSearchSuId())
                        ,issueIdLk(searchDto.getSearchIssueId())
                        ,byrEmailSearch(searchDto.getSearchEmail())
                        ,byrPersonNameSearch(searchDto.searchIpPersname)
                        //,taxEvidenceTypeSearch(searchDto.searchTaxEvidenceType)
                        ,lineAttribute6Search(searchDto.searchLineAttribute6)
                        //,taxTotalZero(searchDto.searchLineAttribute6)
                ).orderBy(taxDtiNtsMain.issueId.desc())
                .fetch();
    }

    @Override
    public List<TaxDtiNtsMainDto> getTaxRcvList(SearchTaxDtiNtsDto searchDto) {

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        return queryFactory
                .select(new QTaxDtiNtsMainDto(
                        taxDtiNtsMain.issueId
                        , taxDtiNtsMain.supbuyType
                        , taxDtiNtsMain.dtiType
                        , taxDtiNtsMain.amendYn
                        , taxDtiNtsMain.interfaceBatchId
                        , taxDtiNtsMain.fileIdx
                        , taxDtiNtsMain.dtiWdate
                        , taxDtiNtsMain.dtiIdate
                        , taxDtiNtsMain.dtiSdate
                        , taxDtiNtsMain.issueSystem
                        , taxDtiNtsMain.typeCode
                        , ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                        .from(codeDetail)
                                        .where(codeDetail.detailCd.eq(taxDtiNtsMain.typeCode)
                                                .and(codeDetail.groupCd.eq("TYPE_CODE"))
                                                .and(codeDetail.compCd.eq(compCd))
                                        ),
                                "typeCodeTxt")
                        , taxDtiNtsMain.taxDemand
                        , taxDtiNtsMain.seqId
                        , taxDtiNtsMain.exchangedDocId
                        , taxDtiNtsMain.supComRegno.substring(0,3).concat(Expressions.asString("-")).concat(taxDtiNtsMain.supComRegno.substring(3,5)).concat(Expressions.asString("-")).concat(taxDtiNtsMain.supComRegno.substring(5,10)).as("supComRegno")
                        , taxDtiNtsMain.supBizplaceCode
                        , taxDtiNtsMain.supComName
                        , taxDtiNtsMain.supEmpName
                        , taxDtiNtsMain.supComType
                        , taxDtiNtsMain.supComClassify
                        , taxDtiNtsMain.supComAddr
                        , taxDtiNtsMain.supDeptName
                        , taxDtiNtsMain.supPersonName
                        , taxDtiNtsMain.supTelNum
                        , taxDtiNtsMain.supEmail
                        , taxDtiNtsMain.byrComRegno
                        , taxDtiNtsMain.byrBizplaceCode
                        , taxDtiNtsMain.byrComName
                        , taxDtiNtsMain.byrEmpName
                        , taxDtiNtsMain.byrComType
                        , taxDtiNtsMain.byrComClassify
                        , taxDtiNtsMain.byrComAddr
                        , taxDtiNtsMain.byrDeptName
                        , taxDtiNtsMain.byrDeptName2
                        , taxDtiNtsMain.byrPersonName
                        , taxDtiNtsMain.byrPersonName2
                        , taxDtiNtsMain.byrTelNum
                        , taxDtiNtsMain.byrTelNum2
                        , taxDtiNtsMain.byrEmail
                        , taxDtiNtsMain.byrEmail2
                        , taxDtiNtsMain.byrBusinessTypeCode
                        , taxDtiNtsMain.brokerComRegno
                        , taxDtiNtsMain.brokerBizplaceCode
                        , taxDtiNtsMain.brokerComName
                        , taxDtiNtsMain.brokerEmpName
                        , taxDtiNtsMain.brokerComType
                        , taxDtiNtsMain.brokerComClassify
                        , taxDtiNtsMain.brokerComAddr
                        , taxDtiNtsMain.brokerDeptName
                        , taxDtiNtsMain.brokerPersonName
                        , taxDtiNtsMain.brokerTelNum
                        , taxDtiNtsMain.brokerEmail
                        , taxDtiNtsMain.cashAmount
                        , taxDtiNtsMain.checkAmount
                        , taxDtiNtsMain.noteAmount
                        , taxDtiNtsMain.receivableAmount
                        , taxDtiNtsMain.supAmount
                        , taxDtiNtsMain.taxAmount
                        , taxDtiNtsMain.totalAmount
                        , taxDtiNtsMain.remark
                        , taxDtiNtsMain.remark2
                        , taxDtiNtsMain.remark3
                        , taxDtiNtsMain.amendCode
                        , ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                .from(codeDetail)
                                .where(codeDetail.detailCd.eq(taxDtiNtsMain.amendCode)
                                        .and(codeDetail.groupCd.eq("AMEND_CD"))
                                        .and(codeDetail.compCd.eq(compCd))
                                ),
                        "amendCodeNm")
                        , taxDtiNtsMain.createdBy
                        , taxDtiNtsMain.creationDate
                        , taxDtiNtsMain.lastUpdatedBy
                        , ExpressionUtils.as(JPAExpressions.select(codeDetail.detailNm)
                                .from(codeDetail)
                                .where(codeDetail.detailCd.eq(taxDtiNtsMain.dtiType)
                                        .and(codeDetail.groupCd.eq("ETAX_TYPE"))
                                        .and(codeDetail.compCd.eq(compCd))
                                ),
                        "dtiTypeTxt")
                        , erpSlipHeader.slipNumber
                        /*, ExpressionUtils.as(JPAExpressions.select(erpSlipHeader.slipNumber.max())
                                .from(erpSlipHeader)
                                .where(erpSlipHeader.taxSmartbillNo.eq(taxDtiNtsMain.issueId)
                                        .and(erpSlipHeader.slipStatus.notIn("SD", "CR", "SC", "FR"))),
                        "slipNo")
                        , ExpressionUtils.as(JPAExpressions.select(.max())
                                .from(erpSlipHeader)
                                .where(erpSlipHeader.taxSmartbillNo.eq(taxDtiNtsMain.issueId)
                                        .and(erpSlipHeader.slipStatus.notIn("SD", "CR", "SC", "FR"))),
                        "slipStatus")*/
                        , erpSlipHeader.slipStatus
                        , ExpressionUtils.as(JPAExpressions.select(vendor.integrationVendorNum.max())
                                .from(vendor)
                                .where(Expressions.stringTemplate("replace({0}, '-', '')", vendor.vatRegistrationNum).eq(taxDtiNtsMain.supComRegno)
                                        .and(vendor.vatRegistrationNum.isNotNull())
                                        .and(vendor.compCd.eq(compCd))
                                ),
                        "erpVendorNum")
                        , ExpressionUtils.as(JPAExpressions.select(vendor.integrationVendorName.max())
                                .from(vendor)
                                .where(Expressions.stringTemplate("replace({0}, '-', '')", vendor.vatRegistrationNum).eq(taxDtiNtsMain.supComRegno)
                                        .and(vendor.vatRegistrationNum.isNotNull())
                                        .and(vendor.compCd.eq(compCd))
                                ),
                        "erpVendorName")
                )).from(taxDtiNtsMain)
                .leftJoin(erpSlipHeader).on(erpSlipHeader.taxSmartbillNo.eq(taxDtiNtsMain.issueId)
                                            .and(erpSlipHeader.orgId.eq(Integer.valueOf(compCd))))
                .where(
                        compCdEq(compCd, taxDtiNtsMain.byrComRegno)
                                .and(dtiWdateFromGoe(searchDto.getSearchFrom()))
                                .and(dtiWdateToLoe(searchDto.getSearchTo()))
                                .and(taxDtiNtsMain.supbuyType.eq("AP"))
                                .and(issueIdLk(searchDto.searchIssueId))
                                .and(vendorLike(searchDto.searchVendor))
                                .and(personOrEmailLike(searchDto.searchEmail))
                                .and(searchStatus(searchDto.searchStatus))
                ).orderBy(taxDtiNtsMain.issueId.desc())
                .fetch();
    }

    @Override
    public List<TaxDtiNtsMainDto> getCountUntrEtaxList(String compCd, String previousDate, String nextDate) {

        LocalDateTime vPreviousDate = LocalDate.parse(previousDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
        LocalDateTime vNextDate = LocalDate.parse(nextDate, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();

        return queryFactory
                .select(new QTaxDtiNtsMainDto(
                        taxDtiNtsMain.count()
                )).from(taxDtiNtsMain)
                .leftJoin(erpSlipHeader)
                .on(taxDtiNtsMain.issueId.eq(erpSlipHeader.taxSmartbillNo)
                .and(erpSlipHeader.orgId.eq(Integer.valueOf(compCd))))
                .where(compCdEq(compCd, taxDtiNtsMain.byrComRegno)
                        .and(taxDtiNtsMain.supbuyType.eq("AP"))
                        .and(dtiWdateFromGoe(vPreviousDate))
                        .and(dtiWdateToLoe(vNextDate))
                        .and(erpSlipHeader.slipStatus.isNull().or(erpSlipHeader.slipStatus.in("AR", "CR", "SC", "FR")))
                ).fetch();
    }

    private BooleanExpression slipHeaderIdEq(BigDecimal searchSlipHeaderId) {
        return Objects.nonNull(searchSlipHeaderId) ? erpSlipHeader.slipHeaderId.ne(searchSlipHeaderId) : null;
    }

    private BooleanExpression taxSmartbillNoNe(String searchTaxSmartbillNo) {
        return hasText(searchTaxSmartbillNo) ? taxDtiNtsMain.issueId.ne(searchTaxSmartbillNo) : null;
    }

    private BooleanBuilder taxEvidenceTypeSearch(String searchTaxEvidenceType) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        boolean hasText = hasText(searchTaxEvidenceType);

        if(hasText || searchTaxEvidenceType.equals("110")){
            booleanBuilder.and(taxDtiNtsMain.dtiType.in("01", "B01"));
        } else {
            booleanBuilder.and(taxDtiNtsMain.dtiType.notIn("01", "B01"));
        }

        return booleanBuilder;
    }

    private BooleanBuilder byrPersonNameSearch(String searchIpPersname) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(searchIpPersname)){
            booleanBuilder.or(taxDtiNtsMain.byrPersonName.likeIgnoreCase("%" + searchIpPersname + "%"))
                    .or(taxDtiNtsMain.byrPersonName2.likeIgnoreCase("%" + searchIpPersname + "%"));
        }
        return booleanBuilder;
    }

    private BooleanBuilder byrEmailSearch(String searchEmail) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(searchEmail)){
            booleanBuilder.or(taxDtiNtsMain.byrEmail.likeIgnoreCase("%" + searchEmail + "%"))
                    .or(taxDtiNtsMain.byrEmail2.likeIgnoreCase("%" + searchEmail + "%"));
        }
        return booleanBuilder;
    }

    private BooleanBuilder supComRegnoSearch(String searchSuId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(searchSuId)){

            String replaceValue = searchSuId.replace("-", "");
            booleanBuilder.or(taxDtiNtsMain.supComRegno.likeIgnoreCase("%" + replaceValue + "%"))
                    .or((taxDtiNtsMain.supComName.likeIgnoreCase("%" + replaceValue + "%")));
        }
        return booleanBuilder;
    }

    private BooleanExpression issueIdLk(String searchIssueId) {
        return hasText(searchIssueId) ? taxDtiNtsMain.issueId.likeIgnoreCase("%" + searchIssueId + "%") : null ;
    }

    private BooleanExpression dtiWdateFromGoe(LocalDateTime fromDate) {
        return Objects.nonNull(fromDate) ? taxDtiNtsMain.dtiWdate.goe(fromDate) : null;
    }

    private BooleanExpression dtiWdateToLoe(LocalDateTime toDate) {
        return Objects.nonNull(toDate) ? taxDtiNtsMain.dtiWdate.loe(toDate) : null;
    }

    private BooleanBuilder taxTotalZero(String searchLineAttribute6) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (hasText(searchLineAttribute6) && searchLineAttribute6.equals("I")) {
            BooleanExpression condition = taxDtiNtsMain.taxAmount.eq(BigDecimal.ZERO)
                    .or(taxDtiNtsMain.taxAmount.stringValue().isEmpty());

            booleanBuilder.and(condition);
        }
        return booleanBuilder;

    }

    private BooleanBuilder lineAttribute6Search(String searchLineAttribute6) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        boolean hasText = hasText(searchLineAttribute6);

        if (hasText) {
            if ("I".equals(searchLineAttribute6)) {
                // 매입전자계산서
                booleanBuilder.and(taxDtiNtsMain.dtiType.in("02", "B02"));
            } else if ("T".equals(searchLineAttribute6)) {
                // 매입전자세금계산서
                booleanBuilder.and(taxDtiNtsMain.dtiType.in("01", "B01"));
            }
        }

        return booleanBuilder;
    }

    private BooleanExpression compCdEq(String compCd, StringPath byrComRegno) {

        BooleanExpression condition;

        if (ELECTRIC.getCode().equals(compCd)) {
            condition = byrComRegno.eq(String.valueOf(ELECTRIC_TAXCODE.getCode()));
        } else if (HOLDINGS.getCode().equals(compCd)) {
            condition = byrComRegno.eq(String.valueOf(HOLDINGS_TAXCODE.getCode()));
        } else if (DNCO.getCode().equals(compCd)) {
            condition = byrComRegno.eq(String.valueOf(DNCO_TAXCODE.getCode()));
        } else {
            condition = byrComRegno.eq(String.valueOf(PARTNERS_TAXCODE.getCode()));
        }
        return condition;
    }

    private BooleanExpression vendorLike(String searchVendor) {
        return hasText(searchVendor) ? taxDtiNtsMain.supComName.likeIgnoreCase("%" + searchVendor + "%") : null ;
    }

    private BooleanExpression personOrEmailLike(String search) {
        return hasText(search) ? (taxDtiNtsMain.byrPersonName.likeIgnoreCase("%" + search + "%")
                                    .or(taxDtiNtsMain.byrEmail.likeIgnoreCase("%" + search + "%")) ) : null ;
    }

    private BooleanExpression searchStatus(String searchStatus) {

        BooleanExpression result = null;

        if (hasText(searchStatus)) {

            if ("00".equals(searchStatus)) {
                // 매입전자계산서
                result = erpSlipHeader.slipStatus.isNull().or(erpSlipHeader.slipStatus.in("AR", "CR", "SC", "FR"));
            } else if("10".equals(searchStatus)) {
                // 매입전자계산서
                result = erpSlipHeader.slipStatus.isNull();
            } else if ("20".equals(searchStatus)) {
                // 매입전자세금계산서
                result = erpSlipHeader.slipStatus.in("SV", "AP", "CP");
            } else if ("30".equals(searchStatus)) {
                // 매입전자세금계산서
                result = erpSlipHeader.slipStatus.eq("CC");
            } else if ("40".equals(searchStatus)) {
                result = erpSlipHeader.slipStatus.in("AR", "CR", "SC", "FR");
            }
        }

        return result;
    }

}

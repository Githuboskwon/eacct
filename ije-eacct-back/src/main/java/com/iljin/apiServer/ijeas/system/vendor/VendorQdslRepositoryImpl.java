package com.iljin.apiServer.ijeas.system.vendor;


import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iljin.apiServer.ijeas.system.benCountry.QBenCountry.benCountry;
import static com.iljin.apiServer.ijeas.system.vendor.QCboGlTermsV.cboGlTermsV;
import static com.iljin.apiServer.ijeas.system.vendor.QSupplierBank.supplierBank;
import static com.iljin.apiServer.ijeas.system.vendor.QVendor.vendor;
import static org.springframework.util.StringUtils.hasText;


@Repository
@RequiredArgsConstructor
public class VendorQdslRepositoryImpl implements VendorQdslRepository{
    private final JPAQueryFactory queryFactory;
    private com.querydsl.core.types.dsl.Expressions Expressions;
    @PersistenceContext
    EntityManager entityManager;
    private final Util util;

    @Override
    public List<VendorDto> getVendorList(VendorDto vendorDto, Pageable pageable) {
        QueryResults<VendorDto> result = queryFactory
                .select( new QVendorDto(vendor.compCd, vendor.integrationVendorNum, vendor.integrationVendorName, vendor.vatRegistrationNum, vendor.bizType,
                        vendor.bizTypeName, benCountry.benCountryNm, vendor.regionCode, vendor.regionName, vendor.customerId, vendor.customerSiteId, vendor.customerNum,
                        vendor.customerName, vendor.custVatRegistrationNum, vendor.customerPartyId, vendor.customerPartyNumber, vendor.custPartySiteId,
                        vendor.custLocationId, vendor.customerType, vendor.custRepresentativeName, vendor.custBusinessType, vendor.custBusinessCondition,
                        vendor.custAddress, vendor.custCategoryCode, vendor.custPaymentTermId, vendor.custPaymentTermName, vendor.custPartyStatus,
                        vendor.custPartySitesStatus, vendor.custAccountStatus, vendor.custSiteStatus, vendor.custNumberOld, vendor.custAcctSiteId, vendor.billToSiteUseId,
                        vendor.custSiteUseId, vendor.custDamboFlag, vendor.custDamboFlagName, vendor.custEmail, vendor.vendorId, vendor.vendorSiteId, vendor.vendorNum,
                        vendor.vendorName, vendor.vendorNameAlt, vendor.venVatRegistrationNum, vendor.vendorTypeLookupCode, vendor.venRepresontativeName,
                        vendor.venBusinessCondition, vendor.venBusinessType, vendor.venZip, vendor.venPhone, vendor.venSiteValid, vendor.venTermsId,
                        vendor.venTermsName, vendor.venPayGroupLookupCd, vendor.venInvoiceCurrencyCd, vendor.venPaymentCurrencyCd, vendor.venAddress,
                        vendor.venTaxAwtFlag, vendor.venPartyId, vendor.venPartySiteId, vendor.venEnabledFlag, vendor.venSiteInactiveDate, vendor.venVatCode,
                        vendor.venAttribute5, vendor.venNumberOld, vendor.venEmail, vendor.apFlag, vendor.arFlag, vendor.aparFlag,
                        vendor.regId, vendor.regDtm, vendor.chgId, vendor.chgDtm, Expressions.asNumber(new BigDecimal(0)).as("prepayCnt"),
                        ExpressionUtils.as(JPAExpressions.select(cboGlTermsV.description)
                                        .from(cboGlTermsV)
                                        .where(cboGlTermsV.termId.eq(vendor.custPaymentTermId)
                                                .and(cboGlTermsV.orgId.eq(vendorDto.compCd))
                                        ),
                                "custPaymentDescription"),
                        ExpressionUtils.as(JPAExpressions.select(cboGlTermsV.description)
                                        .from(cboGlTermsV)
                                        .where(cboGlTermsV.termId.eq(vendor.venTermsId)
                                                .and(cboGlTermsV.orgId.eq(vendorDto.compCd))
                                        ),
                                "venPaymentDescription")))
                .from(vendor)
                .leftJoin(benCountry).on(benCountry.benCountryCd.eq(vendor.territoryCode))
                .where(vendor.compCd.eq(vendorDto.compCd)
                        .and(integrationVendorSearch(vendorDto.integrationVendorNum, vendorDto.integrationVendorName))
                        .and(apFlagEq(vendorDto.apFlag))
                        .and(arFlagEq(vendorDto.arFlag))
                        .and(territoryCodeEq(vendorDto.territoryCode))
                        .and(regionNameEq(vendorDto.regionName))
                        .and(venPayGroupLookupCdEq(vendorDto.venPayGroupLookupCd))
                        .and(bizTypeNameEq(vendorDto.bizTypeName))
                        .and(vatRegistrationNumLike(vendorDto.vatRegistrationNum))
                )
                .orderBy(vendor.integrationVendorNum.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<VendorDto> list = result.getResults();

        return list;
    }

    public List<VendorDto> getVendorListSearch(VendorDto vendorDto, Pageable pageable){
        QueryResults<VendorDto> result = queryFactory
                .select( new QVendorDto(vendor.compCd, vendor.integrationVendorNum, vendor.integrationVendorName, vendor.vatRegistrationNum, vendor.customerId,
                        vendor.customerSiteId, vendor.vendorId, vendor.vendorSiteId, vendor.venPayGroupLookupCd))
                .from(vendor)
                .where(vendor.compCd.eq(vendorDto.compCd)
                        .and(integrationVendorSearch(vendorDto.integrationVendorNum, vendorDto.integrationVendorName))
                        .and(apFlagEq(vendorDto.apFlag))
                        .and(arFlagEq(vendorDto.arFlag))
                )
                .orderBy(vendor.integrationVendorNum.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<VendorDto> list = result.getResults();

        return list;
    }

    @Override
    public Long getVendorCount(VendorDto vendorDto){
        return queryFactory
                .select(vendor.integrationVendorNum.count())
                .from(vendor)
                .where(vendor.compCd.eq(vendorDto.compCd)
                        .and(integrationVendorSearch(vendorDto.integrationVendorNum, vendorDto.integrationVendorName))
                        .and(apFlagEq(vendorDto.apFlag))
                        .and(arFlagEq(vendorDto.arFlag))
                        .and(territoryCodeEq(vendorDto.territoryCode))
                        .and(regionNameEq(vendorDto.regionName))
                        .and(venPayGroupLookupCdEq(vendorDto.venPayGroupLookupCd))
                        .and(bizTypeNameEq(vendorDto.bizTypeName))
                        .and(vatRegistrationNumLike(vendorDto.vatRegistrationNum))
                )
                .fetchFirst();

    };

    @Override
    public List<VendorDto> getBankList(VendorDto vendorDto){
        return queryFactory
                .select(new QVendorDto(
                        supplierBank.noteAccountFlag,
                        supplierBank.bankName,
                        supplierBank.bankAccountNumber,
                        supplierBank.bankAccountName,
                        supplierBank.primaryFlag,
                        supplierBank.bankAccountId,
                        supplierBank.currencyCode
                ))
                .from(supplierBank)
                .where(supplierBank.integrationVendorNum.eq(vendorDto.integrationVendorNum)
                        .and(bankAccountIdEq(vendorDto.bankAccountId))
                        .and(noteAccountFlagEq(vendorDto.noteAccountFlag))
                        .and(currencyCodeEq(vendorDto.currencyCode)))
                .orderBy(supplierBank.noteAccountFlag.asc(),
                         supplierBank.primaryFlag.desc())
                .fetch();
    }

    @Override
    public Map<String, String> callErpVendorUpdate(){
        Map<String, String> result = new HashMap<>();

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("SP_MST_CUSTOMER_VENDOR");
        storedProcedureQuery.execute();

        result.put("Message", (String) storedProcedureQuery.getOutputParameterValue("Msg"));

        return result;
    }

    @Override
    public List<VendorDto> getTermsList(VendorDto vendorDto){
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        return queryFactory
                .select(new QVendorDto(
                        cboGlTermsV.trxType,
                        cboGlTermsV.termId,
                        cboGlTermsV.name,
                        cboGlTermsV.description,
                        cboGlTermsV.orgId,
                        cboGlTermsV.notesFlag,
                        cboGlTermsV.maturityDays,
                        cboGlTermsV.dueDateCalcFlag,
                        cboGlTermsV.currencyType,
                        cboGlTermsV.paymentMethod,
                        cboGlTermsV.vendorAcctCheck,
                        cboGlTermsV.spEnabledFlag,
                        cboGlTermsV.defaultTermFlag,
                        Expressions.asString("").as("seachNm"),
                        Expressions.asString("").as("seachCd")
                ))
                .from(cboGlTermsV)
                .where(cboGlTermsV.orgId.eq(compCd)
                        .and(descriptionLike(vendorDto.searchNm)))
                .fetch();
    }

    //조건
    private BooleanExpression integrationVendorNumLike(String integrationVendorNum) {
        return hasText(integrationVendorNum) ? vendor.integrationVendorNum.containsIgnoreCase(integrationVendorNum) : vendor.integrationVendorNum.likeIgnoreCase("%");
    }

    private BooleanExpression integrationVendorNameLike(String integrationVendorName) {
        return hasText(integrationVendorName) ? vendor.integrationVendorName.containsIgnoreCase(integrationVendorName.toUpperCase()) : vendor.integrationVendorName.likeIgnoreCase("%");
    }

    private BooleanBuilder integrationVendorSearch(String integrationVendorNum, String integrationVendorName){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(integrationVendorNum)){
            booleanBuilder.or(vendor.integrationVendorNum.containsIgnoreCase(integrationVendorNum));
        }
        if(hasText(integrationVendorName)) {
            booleanBuilder.or(vendor.integrationVendorName.containsIgnoreCase(integrationVendorName.toUpperCase()));
        }

        return booleanBuilder;
    }

    private BooleanBuilder vatRegistrationNumLike(String vatRegistrationNum){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(vatRegistrationNum)){
            booleanBuilder.or(vendor.vatRegistrationNum.containsIgnoreCase(vatRegistrationNum))
                    .or(Expressions.stringTemplate("replace({0}, '-', '')", vendor.vatRegistrationNum).containsIgnoreCase(vatRegistrationNum.replace("-", "")));
        }

        return booleanBuilder;
    }

    private BooleanExpression territoryCodeEq(String territoryCode) {
        return hasText(territoryCode) ? vendor.territoryCode.eq(territoryCode) : null;
    }
    private BooleanExpression regionNameEq(String regionName) {
        return hasText(regionName) ? vendor.regionName.eq(regionName) : null;
    }
    private BooleanExpression venPayGroupLookupCdEq(String venPayGroupLookupCd) {
        return hasText(venPayGroupLookupCd) ? vendor.venPayGroupLookupCd.eq(venPayGroupLookupCd) : null;
    }
    private BooleanExpression bizTypeNameEq(String bizTypeName) {
        return hasText(bizTypeName) ? vendor.bizTypeName.eq(bizTypeName) : null;
    }

    private BooleanExpression apFlagEq(String apFlag) {
        return hasText(apFlag) ? vendor.apFlag.eq(apFlag) : null;
    }

    private BooleanExpression arFlagEq(String arFlag) {
        return hasText(arFlag) ? vendor.arFlag.eq(arFlag) : null;
    }

    private BooleanExpression bankAccountIdEq(Integer bankAccountId){
        return bankAccountId != null ? supplierBank.bankAccountId.eq(bankAccountId) : null;
    }

    private BooleanExpression noteAccountFlagEq(String noteAccountFlag){
        return hasText(noteAccountFlag) ? supplierBank.noteAccountFlag.eq(noteAccountFlag) : null;
    }

    private BooleanExpression currencyCodeEq(String currencyCode){
        return hasText(currencyCode) ? supplierBank.currencyCode.eq(currencyCode) : null;
    }

    private BooleanExpression descriptionLike(String description) {
        return hasText(description) ? cboGlTermsV.description.containsIgnoreCase(description) : null;
    }

}

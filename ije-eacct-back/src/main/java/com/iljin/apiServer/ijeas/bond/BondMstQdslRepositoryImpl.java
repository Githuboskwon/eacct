package com.iljin.apiServer.ijeas.bond;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.iljin.apiServer.ijeas.bond.QBondMst.bondMst;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class BondMstQdslRepositoryImpl implements BondMstQdslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BondMstDto> getBondMstList(BondMstDto bondMstDto){
        return queryFactory
            .select(new QBondMstDto(
                bondMst.compCd,
                bondMst.refNo,
                bondMst.bondCd,
                bondMst.benCountry,
                bondMst.projectNm,
                bondMst.intBankNm,
                bondMst.openingDt,
                bondMst.budget,
                bondMst.releaseDt,
                bondMst.benCountryCd,
                bondMst.customerNm,
                bondMst.customerId,
                bondMst.projectId,
                bondMst.localBankNm,
                bondMst.localBankId,
                bondMst.currencyAmt,
                bondMst.currencyCd
            ))
            .from(bondMst)
            .where(compCdEq(bondMstDto.getCompCd()),
                openingDtFromGoe(bondMstDto.getOpeningDtFrom()),
                openingDtToLoe(bondMstDto.getOpeningDtTo()),
                customerIdEq(bondMstDto.getCustomerId()),
                benCountryCdEq(bondMstDto.getBenCountryCd()),
                refNoContains(bondMstDto.getRefNo())
                )
            .fetch();
    }



    @Override
    public List<BondMstDto> getBondMstRefNoList(BondMstDto bondMstDto){
        return queryFactory
            .select(new QBondMstDto(
                bondMst.compCd,
                bondMst.refNo,
                bondMst.bondCd,
                bondMst.benCountry,
                bondMst.projectNm,
                bondMst.intBankNm,
                bondMst.openingDt,
                bondMst.budget,
                bondMst.releaseDt,
                bondMst.benCountryCd,
                bondMst.customerNm,
                bondMst.customerId,
                bondMst.projectId,
                bondMst.localBankNm,
                bondMst.localBankId,
                bondMst.currencyAmt,
                bondMst.currencyCd
            ))
            .from(bondMst)
            .where(bondMst.compCd.eq(bondMstDto.getCompCd()),
                    refNoLk(bondMstDto.getRefNo()),
                    customerNmLk(bondMstDto.getCustomerNm())
            )
            .fetch();
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? bondMst.compCd.eq(compCd) : null;
    }

    private BooleanExpression openingDtFromGoe(LocalDateTime openingDtFrom) {
        return Objects.nonNull(openingDtFrom) ? bondMst.openingDt.goe(openingDtFrom) : null;
    }

    private BooleanExpression openingDtToLoe(LocalDateTime openingDtTo) {
        return Objects.nonNull(openingDtTo) ? bondMst.openingDt.loe(openingDtTo) : null;
    }

    private BooleanExpression customerIdEq(String customerId) {
        return hasText(customerId) ? bondMst.customerId.eq(customerId) : null;
    }

    private BooleanExpression benCountryCdEq(String benCountryCd) {
        return hasText(benCountryCd) ? bondMst.benCountryCd.eq(benCountryCd) : null;
    }

    private BooleanExpression refNoContains(String refNo) {
        return hasText(refNo) ? bondMst.refNo.contains(refNo) : null;
    }

    private BooleanExpression refNoLk(String refNo) {
        return hasText(refNo) ? bondMst.refNo.toUpperCase().likeIgnoreCase("%" + refNo.toUpperCase() + "%") : null;
    }

    private BooleanExpression customerNmLk(String customerNm) {
        return hasText(customerNm) ? bondMst.customerNm.toUpperCase().likeIgnoreCase("%" + customerNm.toUpperCase() + "%") : null;
    }

}

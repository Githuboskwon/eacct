package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTrxTypeHdDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeaderDto;
import com.iljin.apiServer.ijeas.es.erpViews.QErpGlTrxTypeHdDto;
import com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeaderDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.es.erpViews.QErpGlTrxTypeHd.erpGlTrxTypeHd;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpSlipHeader.erpSlipHeader;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ErpSubmitQdslRepositoryImpl implements ErpSubmitQdslRepository {

    private final Util util;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpGlTrxTypeHdDto> getErpSlipSubmitDealTypeList(String search){
            String compCd = util.getLoginCompCd();

            return queryFactory
                    .select(new QErpGlTrxTypeHdDto(
                            erpGlTrxTypeHd.trxTypeCode,
                            erpGlTrxTypeHd.trxTypeName
                    ))
                    .from(erpGlTrxTypeHd)
                    .where(erpGlTrxTypeHd.orgId.eq(compCd),
                            trxTypeSearch(search)
                    ).fetch();
    }

    @Override
    public List<ErpSlipHeaderDto> getErpSlipStatus(ErpSlipSubmitDto erpSlipSubmitDto){
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpSlipHeaderDto(
                        erpSlipHeader.slipIfFlag,
                        erpSlipHeader.trxTypeCode,
                        erpSlipHeader.slipDataFixFlag,
                        erpSlipHeader.slipForcedIfFlag,
                        erpSlipHeader.approvalCompleteFlag
                ))
                .from(erpSlipHeader)
                .where(erpSlipHeader.orgId.eq(Integer.valueOf(compCd)),
                        erpSlipHeader.slipHeaderId.eq(erpSlipSubmitDto.getApprovalGroupId())
                ).fetch();
    }

    @Override
    public List<ErpSlipHeaderDto> getErpSlipFlag(ErpSlipSubmitDto erpSlipSubmitDto){
        String compCd = util.getLoginCompCd();

        return queryFactory
                .select(new QErpSlipHeaderDto(
                        erpSlipHeader.slipIfFlag,
                        erpSlipHeader.approvalCompleteFlag,
                        erpSlipHeader.slipForcedIfFlag
                ))
                .from(erpSlipHeader)
                .where(erpSlipHeader.orgId.eq(Integer.valueOf(compCd)),
                        erpSlipHeader.slipHeaderId.eq(erpSlipSubmitDto.getApprovalGroupId())
                ).fetch();
    }



    private BooleanBuilder trxTypeSearch(String search){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(hasText(search)){
            booleanBuilder.or(erpGlTrxTypeHd.trxTypeCode.containsIgnoreCase(search.toUpperCase()));
        }
        if(hasText(search)) {
            booleanBuilder.or(erpGlTrxTypeHd.trxTypeName.containsIgnoreCase(search.toUpperCase()));
        }

        return booleanBuilder;
    }


}

package com.iljin.apiServer.ijeas.bond;

import static com.iljin.apiServer.ijeas.bond.QBondHeader.bondHeader;
import static com.iljin.apiServer.ijeas.es.erpViews.QErpPaProjectsAll.erpPaProjectsAll;
import static com.iljin.apiServer.ijeas.slip.QSlipHeader.slipHeader;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpPaProjectsAllDto;
import com.iljin.apiServer.ijeas.es.erpViews.QErpPaProjectsAllDto;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BondHeaderQdslRepositoryImpl implements BondHeaderQdslRepository{

    private final JPAQueryFactory queryFactory;
    private final Util util;

    @Override
    public Boolean existsByTypeAndStatusNotIn(SlipDto slipDto){
        Integer fetchOne = queryFactory.selectOne()
            .from(bondHeader)
            .innerJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipHeaderId.eq(bondHeader.slipHeaderId))
            .where(bondHeader.compCd.eq(slipDto.getCompCd()),
                bondHeader.type.eq("LOCAL"),
                bondHeader.refNo.eq(slipDto.getBondHeaderDto().getRefNo()),
                slipHeader.slipHeaderId.ne(slipDto.getSlipHeaderId()),
                slipHeader.status.notIn("CC", "IC", "IE", "BD9", "04", "SC", "SD")
            ).fetchFirst();
        return fetchOne != null;
    }

    @Override
    public List<BondHeader> findAllByTypeAndStatusNotIn(SlipDto slipDto){
        return  queryFactory
            .selectFrom(bondHeader)
            .innerJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipHeaderId.eq(bondHeader.slipHeaderId))
            .where(bondHeader.compCd.eq(slipDto.getCompCd()),
                bondHeader.type.eq("LOCAL"),
                bondHeader.refNo.eq(slipDto.getBondHeaderDto().getRefNo()),
                slipHeader.slipHeaderId.ne(slipDto.getSlipHeaderId()),
                slipHeader.status.notIn("CC", "IC", "IE", "BD9", "04", "SC", "SD", "AR", "CR", "FR")
            ).fetch();
    }

    @Override
    public Boolean existsByTypeAndStatusIn(String compCd, String refNo, BigDecimal slipHeaderId) {
        Integer fetchOne = queryFactory.selectOne()
            .from(bondHeader)
            .innerJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipHeaderId.eq(bondHeader.slipHeaderId))
            .where(bondHeader.compCd.eq(compCd),
                bondHeader.type.eq("LOCAL"),
                bondHeader.refNo.eq(refNo),
                slipHeader.slipHeaderId.ne(slipHeaderId),
                slipHeader.status.in("CC","IC" ,"BD9", "04")
            ).fetchFirst();
        return fetchOne != null;
    }

    @Override
    public List<BondHeaderDto> findAllByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId) {
        return queryFactory
            .select(new QBondHeaderDto(
                bondHeader.splitEtcYn.coalesce("N").as("splitEtcYn"),
                bondHeader.type,
                bondHeader.refNo
            ))
            .from(bondHeader)
            .leftJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipHeaderId.eq(bondHeader.slipHeaderId),
                slipHeader.slipNo.eq(bondHeader.slipNo))
            .where(bondHeader.compCd.eq(compCd),
                bondHeader.slipNo.eq(slipNo),
                bondHeader.slipHeaderId.eq(slipHeaderId)
            ).fetch();
    }

    @Override
    public Boolean existsByLocalComplete(String compCd, String refNo, BigDecimal slipHeaderId) {
        Integer fetchOne = queryFactory.selectOne()
            .from(bondHeader)
            .leftJoin(slipHeader).on(slipHeader.compCd.eq(bondHeader.compCd), slipHeader.slipNo.eq(bondHeader.slipNo))
            .where(bondHeader.type.eq("LOCAL"),
                bondHeader.compCd.eq(compCd),
                bondHeader.refNo.eq(refNo),
                slipHeader.slipHeaderId.ne(slipHeaderId),
                slipHeader.status.in("CC", "04"))
            .fetchFirst();
        return fetchOne != null;
    }
    @Override
    public List<ErpPaProjectsAllDto> getProjectList(String searchWord) {
        return queryFactory
            .select(new QErpPaProjectsAllDto(
                erpPaProjectsAll.projectId,
                erpPaProjectsAll.projectStatusCode,
                erpPaProjectsAll.projectType,
                erpPaProjectsAll.name
            ))
            .from(erpPaProjectsAll)
            .where(erpPaProjectsAll.orgId.eq(Integer.parseInt(util.getLoginCompCd()))
                .and(searchContainsIgnoreCase(searchWord)))
            .fetch();
    }

    private BooleanBuilder searchContainsIgnoreCase(String searchWord) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(searchWord)) {
            booleanBuilder.or(erpPaProjectsAll.name.containsIgnoreCase(searchWord))
                .or(erpPaProjectsAll.projectType.containsIgnoreCase(searchWord));
        }
        return  booleanBuilder;
    }


}

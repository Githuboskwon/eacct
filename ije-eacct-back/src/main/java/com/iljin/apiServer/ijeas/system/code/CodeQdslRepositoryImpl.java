package com.iljin.apiServer.ijeas.system.code;

import com.iljin.apiServer.core.util.Pair;
import com.iljin.apiServer.ijeas.system.code.QCodeDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.iljin.apiServer.ijeas.system.code.QCodeDetail.codeDetail;
import static com.iljin.apiServer.ijeas.system.code.QCodeHeader.codeHeader;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class CodeQdslRepositoryImpl implements CodeQdslRepository {

    private final JPAQueryFactory queryFactory;

    private enum EntityType {
        CODE_HEADER, CODE_DETAIL
    }

    @Override
    public List<Pair> getUsedComboByCodeDto(CodeDto codeDto) {
        return queryFactory
                .select(codeDetail.detailCd.as("key"),
                        codeDetail.detailNm.as("value"))
                .from(codeDetail)
                .where(codeDetail.useYn.eq("Y"),
                        compCdEq(codeDto.getCompCd(), EntityType.CODE_DETAIL),
                        groupCdEq(codeDto.getGroupCd(), EntityType.CODE_DETAIL),
                        remark1Eq(codeDto.getRemark1()),
                        remark2Eq(codeDto.getRemark2()),
                        remark3Lk(codeDto.getRemark3()))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch()
                .stream()
                .map(list -> new Pair(list.get(0, String.class), list.get(1, String.class)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair> getUsedComboSeqByCodeDto(CodeDto codeDto) {
        return queryFactory
                .select(codeDetail.orderSeq.as("key"),
                        codeDetail.detailNm.as("value"))
                .from(codeDetail)
                .where(codeDetail.useYn.eq("Y"),
                        compCdEq(codeDto.getCompCd(), EntityType.CODE_DETAIL),
                        groupCdEq(codeDto.getGroupCd(), EntityType.CODE_DETAIL),
                        remark1Eq(codeDto.getRemark1()),
                        remark2Eq(codeDto.getRemark2()),
                        remark3Lk(codeDto.getRemark3()))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch()
                .stream()
                .map(list -> new Pair(list.get(0, String.class), list.get(1, String.class)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CodeDto> getGroupCodeList(CodeDto codeDto) {

        return queryFactory
                .select(new QCodeDto(
                        codeHeader.compCd,
                        codeHeader.groupCd,
                        codeHeader.groupNm,
                        codeHeader.useYn,
                        codeHeader.groupDesc))
                .from(codeHeader)
                .where(compCdEq(codeDto.getCompCd(), EntityType.CODE_HEADER),
//                        groupCdLk(codeDto.getGroupCd()),
                        groupSearch(codeDto.getGroupCd()),
                        useYnLk(codeDto.getUseYn()))
                .orderBy(codeHeader.groupCd.asc())
                .fetch();
    }

    @Override
    public List<CodeDto> getGroupCodeDetailList(CodeDto codeDto) {

        return queryFactory
                .select(new QCodeDto(
                        codeDetail.compCd,
                        codeDetail.groupCd,
                        codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.useYn,
                        codeDetail.orderSeq,
                        codeDetail.detailDesc,
                        codeDetail.remark1,
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark4,
                        codeDetail.remark5
                ))
                .from(codeDetail)
                .where(compCdEq(codeDto.getCompCd(), EntityType.CODE_DETAIL),
                        groupCdEq(codeDto.getGroupCd(), EntityType.CODE_DETAIL))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch();
    }

    @Override
    public List<Map> findByGroupCd(String compCd, String groupCd) {
        List<Tuple> tupleList = queryFactory
                .select(codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.remark1,
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark4,
                        codeDetail.remark5)
                .from(codeDetail)
                .where(compCdEq(compCd, EntityType.CODE_DETAIL),
                        groupCdEq(groupCd, EntityType.CODE_DETAIL),
                        codeDetail.useYn.eq("Y"))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch();

        return getMaps(tupleList);
    }


    @Override
    public List<Map> findByGroupCdAndRemark3(String compCd, String groupCd, String remark3) {
        List<Tuple> tupleList = queryFactory
                .select(codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.remark1,
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark4,
                        codeDetail.remark5)
                .from(codeDetail)
                .where(compCdEq(compCd, EntityType.CODE_DETAIL),
                        groupCdEq(groupCd, EntityType.CODE_DETAIL),
                        remark3Lk(remark3),
                        codeDetail.useYn.eq("Y"))
                .orderBy(codeDetail.orderSeq.asc())
                .fetch();

        return getMaps(tupleList);
    }

    @Override
    public Optional<CodeDto> getGroupCodeDetailCode(CodeDto codeDto) {
        CodeDto returnValue = queryFactory
                .select(new QCodeDto(
                        codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.remark1,
                        codeDetail.remark2,
                        codeDetail.remark3,
                        codeDetail.remark4,
                        codeDetail.remark5))
                .from(codeDetail)
                .where(compCdEq(codeDto.getCompCd(), EntityType.CODE_DETAIL),
                        codeDetail.groupCd.eq("TYPE_CODE"),
                        codeDetail.useYn.eq("Y"),
                        detailCdEq(codeDto.getDetailCd()))
                .orderBy(codeDetail.orderSeq.asc())
                .fetchOne();

        return Optional.ofNullable(returnValue);
    }

    @Override
    public List<Map> getCodeAll() {

        List<Tuple> tuples = queryFactory
                .select(codeHeader.compCd,
                        codeHeader.groupCd,
                        codeHeader.groupNm,
                        codeDetail.detailCd,
                        codeDetail.detailNm,
                        codeDetail.useYn)
                .from(codeHeader)
                .leftJoin(codeHeader.codeDetails, codeDetail)
                .fetch();

        return tuples.stream().map(tuple -> {
            Map<String, Object> map = new HashMap<>();
            map.put("compCd", tuple.get(codeHeader.compCd));
            map.put("groupCd", tuple.get(codeHeader.groupCd));
            map.put("groupNm", tuple.get(codeHeader.groupNm));
            map.put("detailCd", tuple.get(codeDetail.detailCd));
            map.put("detailNm", tuple.get(codeDetail.detailNm));
            map.put("useYn", tuple.get(codeDetail.useYn));
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<CodeDto> searchGroupCodePageSimple(CodeDto codeDto, Pageable pageable) {
        QueryResults<CodeDto> codeDtoQueryResults = queryFactory
                .select(new QCodeDto(
                        codeHeader.compCd,
                        codeHeader.groupCd,
                        codeHeader.groupNm,
                        codeHeader.useYn,
                        codeHeader.groupDesc))
                .from(codeHeader)
                .where(compCdEq(codeDto.getCompCd(), EntityType.CODE_HEADER),
                        groupCdLk(codeDto.getGroupCd()),
                        useYnLk(codeDto.getUseYn()))
                .orderBy(codeHeader.groupCd.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        //실제데이터
        List<CodeDto> results = codeDtoQueryResults.getResults();
        int startNumber = (int) pageable.getOffset() + 1;
        for (CodeDto result : results) {
            result.setNo(startNumber);
            startNumber++;
        }
        long total = codeDtoQueryResults.getTotal();
        return new PageImpl<>(results, pageable, total);
    }

    @NotNull
    private List<Map> getMaps(List<Tuple> tupleList) {
        return tupleList.stream().map(tuple -> {
            Map<String, Object> map = new HashMap<>();
            map.put("detailCd", tuple.get(codeDetail.detailCd));
            map.put("detailNm", tuple.get(codeDetail.detailNm));
            map.put("remark1", tuple.get(codeDetail.remark1));
            map.put("remark2", tuple.get(codeDetail.remark2));
            map.put("remark3", tuple.get(codeDetail.remark3));
            map.put("remark4", tuple.get(codeDetail.remark4));
            map.put("remark5", tuple.get(codeDetail.remark5));
            return map;
        }).collect(Collectors.toList());
    }

    private BooleanExpression compCdEq(String compCd, EntityType entityType) {
        if (!StringUtils.hasText(compCd)) {
            return null;
        }
        switch (entityType) {
            case CODE_HEADER:
                return codeHeader.compCd.eq(compCd);
            case CODE_DETAIL:
                return codeDetail.compCd.eq(compCd);
            default:
                return null;
        }
    }

    private BooleanExpression groupCdEq(String groupCd, EntityType entityType) {
        if (!StringUtils.hasText(groupCd)) {
            return null;
        }
        switch (entityType) {
            case CODE_HEADER:
                return codeHeader.groupCd.eq(groupCd);
            case CODE_DETAIL:
                return codeDetail.groupCd.eq(groupCd);
            default:
                return null;
        }
    }

    private BooleanExpression detailCdEq(String detailCd) {
        return hasText(detailCd) ? codeDetail.detailCd.eq(detailCd) : null;
    }

    private BooleanExpression groupCdLk(String groupCd) {
        return hasText(groupCd) ? codeHeader.groupCd.likeIgnoreCase("%" + groupCd + "%") : null;
    }

    private BooleanBuilder groupSearch(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(codeHeader.groupCd.contains(value))
                    .or(codeHeader.groupNm.contains(value));
        }
        return booleanBuilder;
    }

    private BooleanExpression useYnLk(String useYn) {
        return hasText(useYn) ? codeHeader.useYn.likeIgnoreCase("%" + useYn + "%") : null;
    }

    private BooleanExpression remark1Eq(String remark1) {
        return hasText(remark1) ? codeDetail.remark1.eq(remark1) : null;
    }

    private BooleanExpression remark2Eq(String remark2) {
        return hasText(remark2) ? codeDetail.remark2.eq(remark2) : null;
    }

    private BooleanExpression remark3Lk(String remark3) {
        return hasText(remark3) ? codeDetail.remark3.likeIgnoreCase("%" + remark3 + "%") : null;
    }
}

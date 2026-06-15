package com.iljin.apiServer.ijeas.system.dff;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.iljin.apiServer.ijeas.system.dff.QDff.dff;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class DffQdslRepositoryImpl implements DffQdslRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DffDto> getDffFromAccount(DffDto dffDto){
        List<DffDto> list = queryFactory
                .select(new QDffDto(
                        dff.applicationShortCd,
                        dff.acctCd,
                        dff.mngItemCd,
                        dff.mngItemNm,
                        dff.mngItemTypeCd,
                        dff.fixLen,
                        dff.hAlignCd,
                        dff.orderSeq,
                        dff.requiredYn,
                        dff.validationType,
                        dff.flexValueSetId,
                        dff.treeYn,
                        dff.treeCd,
                        dff.treeSeq
                ))
                .from(dff)
                .where(dff.acctCd.eq(dffDto.acctCd),
                        dff.applicationShortCd.eq(dffDto.applicationShortCd),
                        dff.useYn.eq("Y"))
                .orderBy(dff.orderSeq.asc())
                .fetch();

        return list;
    }

    @Override
    public int getDffDetailCount(String beforeValue, String search){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT Count(*) " +
                "    FROM (" +
                "          SELECT T.* " +
                "               , ROWNUM AS ROW_CNT " +
                "            FROM ( " +
                "                  SELECT VALUE_NAME" +
                "                       , MEANING_NAME  " +
                "                    FROM CBO.CBO_SP_ACCT_DFF_TEMP  " +
                "                    WHERE 1 = 1 ");

        if(hasText(beforeValue)){
            sb.append("                AND NVL(MEANING_NAME, '') like :beforeValue || '%'");
        }

        if(hasText(search)){
            sb.append("                AND ( NVL(VALUE_NAME, '') like '%' || :search || '%' " +
                    "                   OR NVL(MEANING_NAME, '') like '%' || :search || '%') ");
        }

        sb.append("                 ORDER BY VALUE_NAME  " +
                "                 ) T " +
                "         ) A ");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(hasText(beforeValue)) {
            query.setParameter("beforeValue", beforeValue);
        }

        if(hasText(search)) {
            query.setParameter("search", search);
        }

        return new JpaResultMapper().list(query, DffDto.class).get(0).count;
    }

    @Override
    public List<DffDto> getDffDetail(String beforeValue, Integer page, String search){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT A.* " +
                "    FROM (" +
                "          SELECT T.* " +
                "               , ROWNUM AS ROW_CNT " +
                "            FROM ( " +
                "                  SELECT VALUE_NAME" +
                "                       , MEANING_NAME  " +
                "                    FROM CBO.CBO_SP_ACCT_DFF_TEMP  " +
                "                    WHERE 1 = 1 ");

        if(hasText(beforeValue)){
            sb.append("                AND NVL(MEANING_NAME, '') like :beforeValue || '%'");
        }

        if(hasText(search)){
            sb.append("                AND ( NVL(VALUE_NAME, '') like '%' || :search || '%' " +
                      "                   OR NVL(MEANING_NAME, '') like '%' || :search || '%') ");
        }

        sb.append("                ORDER BY VALUE_NAME  " +
                "                 ) T " +
                "         ) A " +
                "   WHERE A.ROW_CNT BETWEEN :start AND :end ");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(hasText(beforeValue)) {
            query.setParameter("beforeValue", beforeValue);
        }

        if(hasText(search)) {
            query.setParameter("search", search);
        }
        query.setParameter("start", (100*page) + 1);
        query.setParameter("end", 100 * (page+1));

        return new JpaResultMapper().list(query, DffDto.class);
    }
}

package com.iljin.apiServer.ijeas.sm.close;

import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@AllArgsConstructor
@Repository
public class AcctPeriodRepositoryCustomImpl implements AcctPeriodRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    @Override
    public List<AcctPeriodDto> getAcctPeriodList(AcctPeriodDto acctPeriodDto) {
        String compCd = acctPeriodDto.getCompCd();
        String closYmFrom = acctPeriodDto.getCloseYmFrom();
        String closYmTo = acctPeriodDto.getCloseYmTo();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
                "    CM.COMP_CD," +
                "    CM.BA_CD," +
                "    A.DETAIL_NM AS BA_NM," +
                "    CM.CLOS_YM," +
                "    CM.CLOS_STAT_CD," +
                "    B.DETAIL_NM AS CLOS_STAT_NM," +
                "    CM.MAIN_CLOSE_YN," +
                "    CM.CHG_ID," +
                "    C.EMP_NM AS CHG_NM," +
                "    CM.CHG_DTM" +
                "  FROM TB_CLOSE_MNG CM" +
                "       LEFT OUTER JOIN TB_CODE_DT A ON A.COMP_CD = CM.COMP_CD AND A.USE_YN = 'Y' AND A.GROUP_CD = 'BA_CD' AND A.DETAIL_CD = CM.BA_CD" +
                "       LEFT OUTER JOIN TB_CODE_DT B ON B.COMP_CD = CM.COMP_CD AND B.USE_YN = 'Y' AND B.GROUP_CD = 'CLOS_STAT_CD' AND B.DETAIL_CD = CM.CLOS_STAT_CD" +
                "       LEFT OUTER JOIN TB_MST_EMP C ON C.COMP_CD = CM.COMP_CD AND C.EMP_NO = CM.CHG_ID" +
                " WHERE 1=1" +
                "   AND CM.COMP_CD = :compCd" +
                "   AND CM.CLOS_YM >= :closYmFrom"
            +   "   AND CM.CLOS_YM <= :closYmTo"
            +   " ORDER BY CM.CLOS_YM DESC"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("closYmFrom", closYmFrom);
        query.setParameter("closYmTo", closYmTo);


        return new JpaResultMapper().list(query, AcctPeriodDto.class);
    }

    @Override
    public List<AcctPeriodDto> getAcctPeriodOpenList(AcctPeriodDto acctPeriodDto) {
        String compCd = acctPeriodDto.getCompCd();
        String closStatCd = acctPeriodDto.getClosStatCd();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONCAT(SUBSTR(CM.CLOS_YM ,1,4),'-',SUBSTR(CM.CLOS_YM ,5,6)) AS CLOS_YM " +
                "       , CM.CLOS_STAT_CD " +
                "    FROM TB_CLOSE_MNG CM" +
                "   WHERE 1=1" +
                "     AND CM.COMP_CD = :compCd" +
                "     AND CM.CLOS_STAT_CD = :closStatCd " +
                "ORDER BY CLOS_YM ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("closStatCd", closStatCd);

        return new JpaResultMapper().list(query, AcctPeriodDto.class);
    }

    @Override
    public AcctPeriodDto getAcctPeriodOpenCloseDate() {
        String compCd = util.getLoginCompCd();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT TO_CHAR((TO_DATE(MIN(CLOS_YM),'YYYYMM')),'YYYYMMDD') AS OPEN_DAY " +
                "       , TO_CHAR(LAST_DAY(TO_DATE(MAX(CLOS_YM),'YYYYMM')),'YYYYMMDD') AS CLOSE_DAY " +
                "       , 'SLIP' AS FLAG " +
                "    FROM TB_CLOSE_MNG" +
                "   WHERE 1=1" +
                "     AND COMP_CD = :compCd" +
                "     AND CLOS_STAT_CD = 'Open' "
                );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        return new JpaResultMapper().uniqueResult(query, AcctPeriodDto.class);
    }

}

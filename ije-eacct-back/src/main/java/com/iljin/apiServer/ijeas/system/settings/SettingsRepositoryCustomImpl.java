package com.iljin.apiServer.ijeas.system.settings;

import com.iljin.apiServer.ijeas.card.CardUseListDto;
import com.iljin.apiServer.ijeas.slip.SlipDetailDto;
import com.iljin.apiServer.ijeas.slip.SlipHeaderDto;
import com.iljin.apiServer.ijeas.system.authority.MenuAuthDto;
import com.iljin.apiServer.ijeas.system.emp.EmployeeDto;
import com.iljin.apiServer.ijeas.system.menu.MenuDto;
import com.iljin.apiServer.ijeas.system.menu.UserMenuDto;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Repository
public class SettingsRepositoryCustomImpl implements SettingsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MenuDto> getAllMenuList() {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                " WITH RECURSIVE MN (MENU_NO, MENU_NM, UPPER_MENU_NO, MENU_ORDR, PROGRAM_FILE_NM) AS " +
                "     ( " +
                "     SELECT MENU_NO" +
                "            ,MENU_NM " +
                "            ,UPPER_MENU_NO " +
                "            ,MENU_ORDR" +
                "            ,PROGRAM_FILE_NM" +
                "     FROM A_MENU" +
                "     WHERE IFNULL(UPPER_MENU_NO, '') = ''" +
                "     UNION ALL" +
                "     SELECT R.MENU_NO " +
                "            ,R.MENU_NM " +
                "            ,R.UPPER_MENU_NO " +
                "            ,R.MENU_ORDR" +
                "            ,R.PROGRAM_FILE_NM" +
                "     FROM A_MENU R" +
                "     INNER JOIN MN ON" +
                "           R.UPPER_MENU_NO = MN.MENU_NO " +
                "     ) " +
                " SELECT M.MENU_ORDR ,M.MENU_NO ,M.MENU_NM ,M.UPPER_MENU_NO, M.PROGRAM_FILE_NM" +
                " FROM MN M" +
                " ORDER BY M.MENU_NO, M.MENU_ORDR");
        Query query = entityManager.createNativeQuery(sb.toString());

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, MenuDto.class);
    }

    @Override
    public List<UserMenuDto> getUserMenuList(String compCd, String loginId) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT UM.MENU_NO," +
                "       A.MENU_NM," +
                "       A.PROGRAM_FILE_NM," +
                "       UM.MENU_ICON_CD" +
                "  FROM A_USER_MENU UM" +
                "       LEFT OUTER JOIN A_MENU A ON A.MENU_NO = UM.MENU_NO" +
                " WHERE UM.COMP_CD = :compCd" +
                "   AND UM.USER_ID = :loginId" +
                " ORDER BY UM.MENU_ORDR");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, UserMenuDto.class);
    }

    @Override
    public List<CardUseListDto> getUntrCardUseList(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "    SELECT X.STORE_NM," +
                "           X.USED_DT," +
                "           X.USED_AMT," +
                "           X.STATUS" +
                "      FROM ( " +
                "            SELECT CU.STORE_NM," +
                "                   CU.USED_DT," +
                "                   CU.USED_AMT," +
                "                   CU.STATUS" +
                "              FROM TB_CARD_USE_LIST CU" +
                "         LEFT JOIN TB_CARD_MASTER CM" +
                "                ON CU.COMP_CD = CM.COMP_CD" +
                "               AND CU.CARD_NO = CM.CRD_NO" +
                "             WHERE 1=1 " +
                "               AND CU.COMP_CD = :compCd " +
                "               AND CU.STATUS = '01' " +
                "               AND CU.USED_DT <= :nextDate " +
                "               AND CU.USED_DT >= :previousDate " +
                "               AND CM.CRD_PSSR_ID = :loginId " +
                "               AND CU.CARD_TYPE IN ('A', 'C') " +  // A : 법인카드, F : 항공권카드
                "             ORDER BY CU.USED_DT DESC, CU.BUY_DT DESC, CU.TEMP2 DESC " +
                "           ) X " +
                "     WHERE ROWNUM < 4" );
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, CardUseListDto.class);
    }

    @Override
    public List<SlipHeaderDto> getRecentSlipList(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT T.GR_SLIP_NO," +
                "       T.SLIP_TYPE_CD," +
                "       A.DETAIL_NM AS SLIP_TYPE_NM," +
                "       T.TOT_AMT," +
                "       T.SLIP_STAT_CD," +
                "       B.DETAIL_NM AS SLIP_STAT_NM," +
                "       T.HD_SGTXT," +
                "       DATE_FORMAT(T.CHG_DTM, '%Y.%m.%d') AS CHG_DTM" +
                "  FROM TB_SLIP_GR T" +
                "       LEFT OUTER JOIN TB_CODE_DT A ON A.COMP_CD = T.COMP_CD AND A.GROUP_CD = 'SLIP_TYPE_CD' AND A.USE_YN = 'Y' AND A.DETAIL_CD = T.SLIP_TYPE_CD" +
                "       LEFT OUTER JOIN TB_CODE_DT B ON B.COMP_CD = T.COMP_CD AND B.GROUP_CD = 'SLIP_STAT_CD' AND B.USE_YN = 'Y' AND B.DETAIL_CD = T.SLIP_STAT_CD" +
                " WHERE T.COMP_CD = :compCd" +
                "   AND T.REG_ID = :loginId" +
                "   AND T.POST_DT < :nextDate" +
                "   AND T.POST_DT >= :previousDate" +
                " ORDER BY CHG_DTM DESC" +
                " LIMIT 3");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipHeaderDto.class);
    }

    @Override
    public List<EmployeeDto> getUntrDelegateCardUseList(String compCd, String loginId, String previousDate, String nextDate) {
        System.out.println("nextDate = " + nextDate);
        System.out.println("previousDate = " + previousDate);
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "     SELECT EMP.EMP_NO " +
                "          , EMP.EMP_NM " +
                "          , EMP.DEPT_NM " +
                "          , EMP.JOB_NM " +
                "          , COUNT(EMP.EMP_NO) AS CNT " +
                "       FROM TB_MST_EMP EMP " +
                " INNER JOIN A_USER U  " +
                "         ON EMP.COMP_CD = U.COMP_CD " +
                "        AND EMP.EMP_NO = U.LOGIN_ID " +
                "  LEFT JOIN TB_CARD_MASTER CD " +
                "         ON U.COMP_CD = CD.COMP_CD  " +
                "        AND U.LOGIN_ID = CD.CRD_PSSR_ID " +
                "  LEFT JOIN TB_CARD_USE_LIST CU " +
                "         ON CD.COMP_CD = CU.COMP_CD  " +
                "        AND CD.CRD_NO = CU.CARD_NO " +
                "      WHERE EMP.COMP_CD = :compCd " +
                "        AND EMP.EMP_NO IN ( " +
                "                           SELECT GIVE_USER_ID " +
                "                             FROM TB_MST_DELEGATE DLG " +
                "                            WHERE COMP_CD = :compCd " +
                "                              AND DLG.DELEGATE_STAT_CD = '1' " +
                "                              AND DLG.RECEIVE_USER_ID = :loginId ) " +
                "        AND CU.CARD_TYPE IN ('A', 'C') " +
                "        AND CU.STATUS = '01' " +
                "        AND CU.USED_DT <= :nextDate " +
                "        AND CU.USED_DT >= :previousDate " +
                "   GROUP BY EMP.EMP_NO " +
                "          , EMP.EMP_NM " +
                "          , EMP.DEPT_NM " +
                "          , EMP.JOB_NM " +
                "   ORDER BY COUNT(EMP.EMP_NO) DESC " +
                "          , EMP.DEPT_NM  " +
                "          , EMP.EMP_NM " );
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);
        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, EmployeeDto.class);
    }

    @Override
    public List<SlipDetailDto> getPresentCondition(String compCd, String loginId, String thisYear) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                " SELECT A.ACCT_CD," +
                "        A.ACCT_NM," +
                "        A.SUM_ACCT_AMT" +
                "   FROM (" +
                "     SELECT C.ACCT_CD," +
                "            C.ACCT_NM," +
                "            C.SUM_ACCT_AMT," +
                "            @rownum\\:=@rownum+1 AS RNUM" +
                "       FROM (" +
                "         SELECT T.ACCT_CD," +
                "                T.ACCT_NM," +
                "                SUM(T.ACCT_AMT) AS SUM_ACCT_AMT" +
                "           FROM (" +
                "             SELECT SH.EA_SLIP_NO," +
                "                    SD.ITEM_SEQ," +
                "                    SD.ACCT_CD," +
                "                    AC.ACCT_NM," +
                "                    SD.ACCT_AMT" +
                "               FROM TB_SLIP_HD SH" +
                "                    INNER JOIN TB_SLIP_DT SD ON SD.COMP_CD = SH.COMP_CD AND SD.EA_SLIP_NO = SH.EA_SLIP_NO AND SD.DC_CD = 'D'" +
                "                    INNER JOIN TB_MST_ACCT AC ON AC.COMP_CD = SH.COMP_CD AND AC.DEPT_CD = SD.CCTR_CD AND AC.ACCT_CD = SD.ACCT_CD AND AC.VAT_YN = 'N'" +
                "              WHERE SH.COMP_CD = :compCd" +
                "                AND SH.WRT_ID = :loginId" +
                "                AND SH.SLIP_STAT_CD = '70'" +
                "                AND SH.POST_DT LIKE :thisYear " +
                "            ) T" +
                "          GROUP BY T.ACCT_CD" +
                "          ORDER BY SUM(T.ACCT_AMT) DESC" +
                "       ) C, (SELECT @rownum\\:=0) AS R" +
                "   ) A" +
                "  WHERE A.RNUM <= 5" +
                " UNION ALL" +
                " SELECT '-' AS ACCT_CD," +
                "        '기타' AS ACCT_NM," +
                "        IFNULL(SUM(A.SUM_ACCT_AMT), 0) AS SUM_ACCT_AMT" +
                "   FROM (" +
                "     SELECT C.ACCT_CD," +
                "            C.ACCT_NM," +
                "            C.SUM_ACCT_AMT," +
                "            @rownum2\\:=@rownum2+1 AS RNUM" +
                "       FROM (" +
                "         SELECT T.ACCT_CD," +
                "                T.ACCT_NM," +
                "                SUM(T.ACCT_AMT) AS SUM_ACCT_AMT" +
                "           FROM (" +
                "             SELECT SH.EA_SLIP_NO," +
                "                    SD.ITEM_SEQ," +
                "                    SD.ACCT_CD," +
                "                    AC.ACCT_NM," +
                "                    SD.ACCT_AMT" +
                "               FROM TB_SLIP_HD SH" +
                "                    INNER JOIN TB_SLIP_DT SD ON SD.COMP_CD = SH.COMP_CD AND SD.EA_SLIP_NO = SH.EA_SLIP_NO AND SD.DC_CD = 'D'" +
                "                    INNER JOIN TB_MST_ACCT AC ON AC.COMP_CD = SH.COMP_CD AND AC.DEPT_CD = SD.CCTR_CD AND AC.ACCT_CD = SD.ACCT_CD AND AC.VAT_YN = 'N'" +
                "              WHERE SH.COMP_CD = :compCd" +
                "                AND SH.WRT_ID = :loginId" +
                "                AND SH.SLIP_STAT_CD = '70'" +
                "                AND SH.POST_DT LIKE :thisYear " +
                "            ) T" +
                "          GROUP BY T.ACCT_CD" +
                "          ORDER BY SUM(T.ACCT_AMT) DESC" +
                "       ) C, (SELECT @rownum2\\:=0) AS R2" +
                "   ) A" +
                "  WHERE A.RNUM > 5");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("thisYear", thisYear +"%");

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipDetailDto.class);
    }

    @Override
    public List<SlipDetailDto> getPresentBudgetCondition(String compCd, String deptCd, String thisYear) {
        StringBuilder sb = new StringBuilder();
//        sb.append("" +
//                " SELECT BUDGET_ACCT_CODE" +
//                "      , BUDGET_ACCT_DESC" +
//                "      , BUDGET_AMOUNT" +
//                "      , ACCT_NAME " +
//                "      , T_ACTUAL_AMOUNT " +
//                "   FROM APPS.CBO_SP_BUD_ACT_V " +
//                "  WHERE PERIOD_NAME = :thisYear " +
//                "    AND ORG_ID = :compCd " +
//                "    AND BUDGET_DEPT_CODE = :deptCd ");
        sb.append("" +
                " SELECT BUDGET_ACCT_CODE" +
                "      , BUDGET_ACCT_DESC" +
                "      , BUDGET_AMOUNT" +
                "      , ACCT_NAME " +
                "      , T_ACTUAL_AMOUNT " +
                "   FROM TABLE(CBO_GL_BUDGET_PKG.ESLIP_FUNS_INQUERY( " +
                "    :thisYear, " +
                "    :compCd, " +
                "    :deptCd) )");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("deptCd", deptCd);
        query.setParameter("thisYear", thisYear);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipDetailDto.class);
    }

    @Override
    public List<SettingsDto> getCountUntrCardUseList(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "  SELECT COUNT(1) AS UN_CARD_CNT" +
                "    FROM TB_CARD_USE_LIST CU" +
                "   WHERE CARD_NO IN (" +
                "                     SELECT CRD_NO " +
                "                       FROM TB_CARD_MASTER CM " +
                "                      WHERE CM.COMP_CD = CU.COMP_CD " +
                "                        AND CM.CRD_NO = CU.CARD_NO " +
                "                        AND CM.CRD_PSSR_ID =:loginId " +
                "                        AND CM.CRD_FG_CD IN ('A', 'C') ) " +
                "     AND CU.COMP_CD = :compCd" +
                "     AND CU.STATUS IN ('01' , 'RU') " +
                "     AND CU.CARD_TYPE = 'A' " +
                "     AND CU.USER_ID = :loginId " +
                "     AND CU.USED_DT <= :nextDate" +
                "     AND CU.USED_DT >= :previousDate");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountUntrAirlineCardUseList(String compCd, String loginId, String previousDate, String nextDate) {

        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "  SELECT COUNT(1) AS UN_CARD_CNT" +
                "    FROM TB_CARD_USE_LIST CU" +
                "   WHERE CARD_NO IN (" +
                "                     SELECT CRD_NO " +
                "                       FROM TB_CARD_MASTER CM" +
                "                      WHERE CM.CRD_NO = CU.CARD_NO ) " +
                "     AND CU.COMP_CD = :compCd" +
                "     AND CU.STATUS = '01' " +
                "     AND CU.CARD_TYPE = 'F' " +
                "     AND CU.USER_ID = :loginId " +
                "     AND CU.USED_DT <= :nextDate" +
                "     AND CU.USED_DT >= :previousDate" +
                "     AND CU.CANCEL_FLAG = 'N' ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountUntrEtaxList(String compCd, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS UN_ETAX_CNT" +
                "  FROM XXSB_DTI_NTS_MAIN TAX" +
                " WHERE 1=1" +
                "   AND TAX.COMP_CD = :compCd" +
                "   AND TAX.SUPBUY_TYPE = 'AP' " +
                "   AND TAX.ISSUE_ID NOT IN ( " +
                "                            SELECT TAX_SMARTBILL_NO " +
                "                              FROM APPS.CBO_SP_SLIP_HEADER " +
                "                             WHERE SLIP_STATUS NOT IN ('SD','CR','SC','FR' ) " +
                "                               AND TAX_SMARTBILL_NO IS NOT NULL " +
                "                            ) " +
                "   AND TAX.DTI_WDATE < :nextDate" +
                "   AND TAX.DTI_WDATE >= :previousDate");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountUnApprovalSlip(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "    SELECT COUNT(1) AS UN_SLIP_CNT " +
                "      FROM TB_SLIP_HD A " +
                " LEFT JOIN APPS.CBO_SP_SLIP_HEADER B " +
                "        ON A.COMP_CD = B.ORG_ID " +
                "       AND A.SLIP_HEADER_ID = B.SLIP_HEADER_ID " +
                "     WHERE A.COMP_CD = :compCd " +
                "       AND A.EMP_NO = :loginId " +
                "       AND A.POSTING_DT < :nextDate " +
                "       AND A.POSTING_DT >= :previousDate " +
                "       AND NVL(A.STATUS, B.SLIP_STATUS) = 'SV' " +
                "       AND A.SLIP_TYPE NOT IN ('30','91') ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountUnApprovalErpSlip(String compCd, String loginId, String previousDate, String nextDate) {
        String erpSlips = "'";
//        erpSlips += SlipType.SLIP_PAYMENT_FUND.getCode() + "', '";//자금지급전표
//        erpSlips += SlipType.SLIP_PURCHASE_ORDER.getCode() + "', '";//구매전표
//        erpSlips += SlipType.SLIP_ETC_SALES.getCode() + "', '";//기타매출전표
//        erpSlips += SlipType.SLIP_COST_CONSTRUCTION.getCode() + "'";//공사

        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS UN_ERP_SLIP_CNT" +
                "  FROM TB_SLIP_GR T" +
                " WHERE T.COMP_CD = :compCd" +
                "   AND T.WRT_ID = :loginId" +
                "   AND T.POST_DT < :nextDate" +
                "   AND T.POST_DT >= :previousDate" +
                "   AND T.SLIP_TYPE_CD IN (" +erpSlips +")" +
                "   AND T.SLIP_STAT_CD = '10'");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountUnApprovalBdgt(String compCd, String loginId) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS UN_BDGT_CNT" +
                "  FROM TB_BUD_HD BH" +
                " WHERE BH.COMP_CD = :compCd" +
                "   AND BH.REG_ID = :loginId" +
                "   AND BH.BUD_STAT_CD = '10'");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountTodoApproval(String compCd, String loginId) {
        StringBuilder sb = new StringBuilder();

        sb.append("" +
                  "    SELECT COUNT(*) AS TODO_CNT " +
                  "      FROM TB_SLIP_HD A " +
                  " LEFT JOIN APPS.CBO_SP_SLIP_HEADER B " +
                  "        ON A.COMP_CD = B.ORG_ID " +
                  "       AND A.SLIP_HEADER_ID = B.SLIP_HEADER_ID " +
                  "INNER JOIN TB_APPR_HD C " +
                  "        ON A.COMP_CD = C.COMP_CD " +
                  "       AND A.APPROVAL_GROUP_ID = C.APPR_GROUP_ID " +
                  "       AND C.NEXT_APP_USER_ID = :loginId " +
                  "     WHERE A.COMP_CD = :compCd " +
                  "       AND NVL(A.STATUS, B.SLIP_STATUS) = 'AP' " +
                  "       AND A.SLIP_TYPE NOT IN ('91') ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountTodoApproval(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS TODO_CNT" +
                "  FROM TB_APPR_HD HD" +
                " INNER JOIN TB_APPR_DT DT ON HD.APPR_NO = DT.APPR_NO" +
                "        AND DT.APPR_SEQ = (SELECT MIN(T.APPR_SEQ) FROM TB_APPR_DT T WHERE T.APPR_NO = HD.APPR_NO AND T.APPR_FG_CD = 'X')" +
                "        AND DT.APRVER_ID IN (SELECT DL.ADLG_ID" +
                "                            FROM TB_APPR_DELEGATE DL" +
                "                           WHERE DL.ACT_ID = :loginId" +
                "                             AND DL.ADLG_STAT_CD = '10'" +
                "                             AND DL.ADLG_STR_DT <= DATE_FORMAT(NOW(), '%Y%m%d')" +
                "                             AND DL.ADLG_END_DT >= DATE_FORMAT(NOW(), '%Y%m%d')" +
                "                           UNION ALL" +
                "                          SELECT :loginId" +
                "                            FROM DUAL)" +
                " WHERE 1 = 1" +
                "   AND HD.COMP_CD = :compCd" +
                "   AND DATE_FORMAT(HD.DRAFT_DTM, '%Y%m%d') < :nextDate" +
                "   AND DATE_FORMAT(HD.DRAFT_DTM, '%Y%m%d') >= :previousDate" +
                "   AND HD.DOC_STAT_CD IN('REQ', 'ING')");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    // @Override
    // public List<SettingsDto> getCountRejectedApproval(String compCd, String loginId, String thisMonth) {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("" +
    //             "SELECT COUNT(1) AS RJ_CNT" +
    //             "  FROM TB_APPR_HD HD" +
    //             " INNER JOIN TB_APPR_DT DT ON HD.APPR_NO = DT.APPR_NO" +
    //             "        AND DT.APPR_FG_CD IN ('A', 'R') AND DT.APRVER_ID = :loginId" +
    //             "        AND DT.APPR_TYPE_CD = '10'" +
    //             " INNER JOIN TB_SLIP_GR GR ON GR.COMP_CD = HD.COMP_CD" +
    //             "        AND GR.GR_SLIP_NO = HD.DOC_MNG_NO" +
    //             "        AND GR.ELEC_APPR_ID = HD.APPR_NO" +
    //             " WHERE HD.COMP_CD = :compCd" +
    //             "   AND DATE_FORMAT(HD.DRAFT_DTM, '%Y%m%d') LIKE :thisMonth" +
    //             "   AND HD.DOC_STAT_CD IN('ING', 'APR', 'REJ')" +
    //             "   AND HD.DOC_STAT_CD = 'REJ'");
    //     Query query = entityManager.createNativeQuery(sb.toString());
    //     query.setParameter("compCd", compCd);
    //     query.setParameter("loginId", loginId);
    //     query.setParameter("thisMonth", thisMonth + "%");

    //     return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    // }

    @Override
    public List<SettingsDto> getCountRejected(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "     SELECT COUNT(1) AS RJ_APP_CNT " +
                "       FROM TB_SLIP_HD A " +
                "  LEFT JOIN APPS.CBO_SP_SLIP_HEADER B " +
                "         ON A.COMP_CD = B.ORG_ID " +
                "        AND A.SLIP_HEADER_ID = B.SLIP_HEADER_ID " +
                "      WHERE A.COMP_CD = :compCd " +
                "        AND A.EMP_NO = :loginId "+
                "        AND A.POSTING_DT < :nextDate " +
                "        AND A.POSTING_DT >= :previousDate " +
                "        AND NVL(A.STATUS, B.SLIP_STATUS) IN ('AR', 'CR', 'FR', 'SC') " +
                "        AND A.SLIP_TYPE NOT IN ('91') ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountRejectedApproval(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                  "     SELECT COUNT(1) AS RJ_APP_CNT " +
                  "       FROM TB_SLIP_HD A " +
                  "  LEFT JOIN APPS.CBO_SP_SLIP_HEADER B " +
                  "         ON A.COMP_CD = B.ORG_ID " +
                  "        AND A.SLIP_HEADER_ID = B.SLIP_HEADER_ID " +
                  "      WHERE A.COMP_CD = :compCd " +
                  "        AND A.EMP_NO = :loginId "+
                  "        AND A.POSTING_DT < :nextDate " +
                  "        AND A.POSTING_DT >= :previousDate " +
                  "        AND NVL(A.STATUS, B.SLIP_STATUS) = 'AR' ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountRejectedFinance(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "     SELECT COUNT(1) AS RJ_FNC_CNT " +
                "       FROM TB_SLIP_HD A " +
                "  LEFT JOIN APPS.CBO_SP_SLIP_HEADER B " +
                "         ON A.COMP_CD = B.ORG_ID " +
                "        AND A.SLIP_HEADER_ID = B.SLIP_HEADER_ID " +
                "      WHERE A.COMP_CD = :compCd " +
                "        AND A.EMP_NO = :loginId "+
                "        AND A.POSTING_DT < :nextDate " +
                "        AND A.POSTING_DT >= :previousDate " +
                "        AND NVL(A.STATUS, B.SLIP_STATUS) = 'FR' ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<MenuDto> getDashboardSettings(String loginId, String roleCd) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT T.*" +
                "  FROM (" +
                "      SELECT CASE WHEN UM.MENU_NO THEN '1' ELSE '0' END REG_YN," +
                "             MN.MENU_NO," +
                "             CONCAT(A.MENU_NM, ' - ', MN.MENU_NM) AS MENU_NM," +
                "             UM.MENU_ICON_CD," +
                "             UM.MENU_ORDR AS MENU_ORDER" +
                "        FROM A_MENU MN" +
                "             LEFT OUTER JOIN A_USER_MENU UM ON UM.MENU_NO = MN.MENU_NO AND UM.USER_ID = :loginId" +
                "             INNER JOIN A_MENU A ON A.MENU_NO = MN.UPPER_MENU_NO" +
                "             INNER JOIN A_MENU_ROLE B ON B.ROLE_CD = IFNULL(:roleCd, '') AND B.MENU_NO = MN.MENU_NO" +
                "       WHERE IFNULL(MN.UPPER_MENU_NO, 0) <> 0" +
                "       ) T" +
                " ORDER BY T.REG_YN DESC, T.MENU_ORDER ASC, T.MENU_NO ASC");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("loginId", loginId);
        query.setParameter("roleCd", roleCd);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, MenuDto.class);
    }

    @Override
    public List<MenuAuthDto> getAuthMenuList(String roleCd, String compCd) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "WITH RECURSIVE MN (MENU_NO, MENU_NM, UPPER_MENU_NO, MENU_ORDR, PROGRAM_FILE_NM, LV) AS " +
                "    ( " +
                "    SELECT MENU_NO" +
                "           ,MENU_NM " +
                "           ,UPPER_MENU_NO " +
                "           ,MENU_ORDR" +
                "           ,PROGRAM_FILE_NM" +
                "           ,0 LV" +
                "      FROM A_MENU" +
                "     WHERE MENU_ORDR = 0" +
                "    UNION ALL " +
                "    SELECT R.MENU_NO " +
                "           ,R.MENU_NM " +
                "           ,R.UPPER_MENU_NO " +
                "           ,R.MENU_ORDR" +
                "           ,R.PROGRAM_FILE_NM" +
                "           ,MN.LV +1" +
                "      FROM A_MENU R" +
                "           INNER JOIN MN ON R.UPPER_MENU_NO = MN.MENU_NO " +
                "    ) " +
                "SELECT CASE" +
                "         WHEN LENGTH(MR.ROLE_CD) > 0 THEN '1'" +
                "             ELSE '0' END ROLE_CK," +
                "       MR.ROLE_CD," +
                "       MN.MENU_NO," +
                "       MN.MENU_NM," +
                "       CONCAT('Lv ', MN.LV, ' - ', MN.MENU_NM) AS MENU_DC," +
                "       MR.COMP_CD," +
                "       MN.LV AS menuLv," +
                "       MN.MENU_ORDR AS menuOrder," +
                "       PROGRAM_FILE_NM," +
                "       MN.UPPER_MENU_NO" +
                "  FROM MN" +
                "       INNER JOIN A_MENU_ROLE MR ON MR.ROLE_CD = :roleCd AND MR.COMP_CD = :compCd AND MR.MENU_NO = MN.MENU_NO" +
                " WHERE MN.MENU_ORDR <> 0" +
                " ORDER BY MN.MENU_NO, MN.MENU_ORDR");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roleCd", roleCd);
        query.setParameter("compCd", compCd);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, MenuAuthDto.class);
    }

    @Override
    public List<SettingsDto> getCountCompleteApproval(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS COMPLETE_CNT" +
                "  FROM TB_APPR_HD HD" +
                " INNER JOIN TB_APPR_DT DT ON HD.APPR_NO = DT.APPR_NO" +
                "   AND DT.APPR_FG_CD IN ('A', 'R')" +
                "   AND DT.APPR_TYPE_CD <> '10'" +
                "   AND DT.A_APRVER_ID = :loginId" +
                " WHERE HD.COMP_CD = :compCd" +
                "   AND HD.DOC_STAT_CD IN('ING', 'APR', 'REJ')" +
//                "   AND HD.DRAFT_DTM >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 MONTH), '%Y%m%d')" +
//                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') <= DATE_FORMAT(NOW(), '%Y%m%d')");
                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') < :nextDate" +
                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') >= :previousDate");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountRequestApproval(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS REQ_CNT" +
                "  FROM TB_APPR_HD HD" +
                " INNER JOIN TB_APPR_DT DT ON HD.APPR_NO = DT.APPR_NO" +
                "   AND DT.APPR_TYPE_CD = '10'" +
                " WHERE HD.COMP_CD = :compCd" +
                "   AND HD.DRAFT_ID = :loginId" +
//                "   AND HD.DRAFT_DTM >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 MONTH), '%Y%m%d')" +
//                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') <= DATE_FORMAT(NOW(), '%Y%m%d')");
                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') < :nextDate" +
                "   AND DATE_FORMAT(HD.DRAFT_DTM,'%Y%m%d') >= :previousDate");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountRejectedBudget(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS RJ_BJT_CNT" +
                "  FROM TB_BUD_HD T" +
                " WHERE T.COMP_CD = :compCd" +
                "   AND T.WRT_ID = :loginId" +
                "   AND T.WRT_DT < :nextDate" +
                "   AND T.WRT_DT >= :previousDate" +
                "   AND T.BUD_STAT_CD = :budStatCd");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);
        //query.setParameter("budStatCd", BudgetStatus.REJECT_APPROVAL.getCode());

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }

    @Override
    public List<SettingsDto> getCountRejectedPlanning(String compCd, String loginId, String previousDate, String nextDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT COUNT(1) AS RJ_PLN_CNT" +
                "  FROM TB_BUD_HD T" +
                " WHERE T.COMP_CD = :compCd" +
                "   AND T.WRT_ID = :loginId" +
                "   AND T.WRT_DT < :nextDate" +
                "   AND T.WRT_DT >= :previousDate" +
                "   AND T.BUD_STAT_CD = :budStatCd");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("loginId", loginId);
        query.setParameter("previousDate", previousDate);
        query.setParameter("nextDate", nextDate);
        //query.setParameter("budStatCd", BudgetStatus.REJECT_PLANNING.getCode());

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SettingsDto.class);
    }
}

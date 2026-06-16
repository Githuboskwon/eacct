package com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis;

import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PjtPerfAnalysisRepositoryCustomImpl implements PjtPerfAnalysisRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;


    @Override
    public List<PjtPerfAnalysisDto> getPerfAnalysisList(PjtPerfAnalysisDto pjtPerfAnalysisDto) {

        String compCd = util.getLoginCompCd();

        StringBuilder sb = new StringBuilder();

        sb.append(" 	          SELECT X.CONTRACT_ORI_AMOUNT                                               ");
        sb.append(" 	                  , X.SALES_PER                                                      ");
        sb.append(" 	                  , X.PROCESS_PER                                                    ");
        sb.append(" 	                  , X.CLAIM_PER                                                      ");
        sb.append(" 	                  , X.COLLECTION_PER                                                 ");
        sb.append(" 	                  , X.CT_AMOUNT                                                      ");
        sb.append(" 	                  , X.CT_AMOUNT_FOR                                                  ");
        sb.append(" 	                  , X.DEGREE                                                         ");
        sb.append(" 	                  , X.END_DATE                                                       ");
        sb.append(" 	                  , X.FTAM                                                           ");
        sb.append(" 	                  , TO_CHAR(X.ORG_ID) ORG_ID                                         ");
        sb.append(" 	                  , X.PLAN_AMT                                                       ");
        sb.append(" 	                  , X.PLAN_AMT_PER                                                   ");
        sb.append(" 	                  , X.PROJECT_CODE                                                   ");
        sb.append(" 	                  , X.PROJECT_MANAGE_NO                                              ");
        sb.append(" 	                  , X.PROJECT_NAME                                                   ");
        sb.append(" 	                  , X.PROJECT_GUBUN                                                  ");
        sb.append(" 	                  , X.RESULT_AMT                                                     ");
        sb.append(" 	                  , X.RESULT_AMT1                                                    ");
        sb.append(" 	                  , X.RESULT_AMT_PER                                                 ");
        sb.append(" 	                  , X.RESULT_AMT_PER1                                                ");
        sb.append(" 	                  , X.REVENUE_AMOUNT                                                 ");
        sb.append(" 	                  , X.REVENUE_AMOUNT1                                                ");
        sb.append(" 	                  , X.SLIP_HEADER_ID                                                 ");
        sb.append(" 	                  , X.SLIP_NO                                                        ");
        sb.append(" 	                  , X.START_DATE                                                     ");
        sb.append(" 	                  , X.TASK_NUMBER                                                    ");
        sb.append(" 	                  , X.TOTAL_MONTH                                                    ");
        sb.append(" 	                  , X.TOTAL_OFFER_UNIT_COST                                          ");
        sb.append(" 	                  , X.TOTAL_SALES_PROFIT_BUDGET                                      ");
        sb.append(" 	                  , X.TOTAL_SALES_PROFIT_BUDGET1                                     ");
        sb.append(" 	                  , X.TOTAL_SALES_PROFIT_PLAN                                        ");
        sb.append(" 	                  , X.VERSION                                      ");
        sb.append(" 	                  , X.COUNTRY_CODE                                 ");
        sb.append(" 	                  , X.COUNTRY_NAME                                 ");
        sb.append(" 	                  , X.ADD_USER_ID                                 ");
        sb.append(" 	            FROM                                  ");
        sb.append(" 	                (SELECT A.ORG_ID                                                                                               ");
        sb.append(" 	                        ,A.PROJECT_CODE                                 ");
        sb.append(" 	                        ,A.COUNTRY_CODE                                 ");
        sb.append(" 	                        ,A.COUNTRY_NAME                                 ");
        sb.append(" 	                        ,A.PROJECT_GUBUN                                                                                           ");
        sb.append(" 	                        ,E.VERSION                                  ");
        sb.append(" 	                        ,E.ADD_USER_ID                                               ");
        sb.append(" 	                        ,NVL((SELECT SUM(NVL(Y.TOTAL_OFFER_UNIT_COST, 0))                                                                            ");
        sb.append(" 	                                FROM CBOSFA.SFA_PJTDT Y                                                                                     ");
        sb.append(" 	                               WHERE Y.PROJECT_NUMBER = A.PROJECT_CODE                                                                         ");
        sb.append(" 	                                 AND Y.VERSION = G.VERSION                                                                                                                         ");
        sb.append(" 	                                 AND Y.TASK_NUMBER = A.TASK_NUMBER),0) AS TOTAL_OFFER_UNIT_COST                                  ");
        sb.append(" 	                        ,A.SLIP_NO                                                                                                ");
        sb.append(" 	                        ,(SELECT SLIP_HEADER_ID FROM CBO_SP_SLIP_HEADER WHERE SLIP_NUMBER = A.SLIP_NO) AS SLIP_HEADER_ID                                                           ");
        sb.append(" 	                        ,A.TASK_NUMBER                                                                                             ");
        sb.append(" 	                        ,A.PROJECT_MANAGE_NO                                                                                         ");
        sb.append(" 	                        ,A.PROJECT_NAME                                                                                             ");
        sb.append(" 	                        ,NVL(A.CT_AMOUNT, 0) AS CT_AMOUNT                                                                                    ");
        sb.append(" 	                        ,NVL(A.CT_AMOUNT_FOR, 0) AS CT_AMOUNT_FOR                                                                               ");
        sb.append(" 	                        ,NVL(B.AMT,0) AS PLAN_AMT                                                                                       ");
        sb.append(" 	                        ,CASE WHEN NVL(A.CT_AMOUNT, 0) = 0 THEN 0                                                                               ");
        sb.append(" 	                             ELSE ROUND((NVL(B.AMT,0) / NVL(A.CT_AMOUNT, 0)) * 100 ,2) END AS PLAN_AMT_PER                                                           ");
        sb.append(" 	                        ,NVL(A.CT_AMOUNT, 0) - NVL(B.AMT,0) AS TOTAL_SALES_PROFIT_PLAN                                                                     ");
        sb.append(" 	                        ,NVL(C.REVENUE_AMOUNT,0) AS REVENUE_AMOUNT                                                                               ");
        sb.append(" 	                        ,NVL(D.AMOUNT,0) AS RESULT_AMT                                                                                     ");
        sb.append(" 	                        ,NVL(D.FTAM,0) AS FTAM                                                                                          ");
        sb.append(" 	                        ,CASE WHEN NVL(C.REVENUE_AMOUNT,0) = 0 THEN 0                                                                              ");
        sb.append(" 	                            ELSE ROUND((NVL(D.AMOUNT,0) / NVL(C.REVENUE_AMOUNT,0)) *100 ,2) END AS RESULT_AMT_PER                                                        ");
        sb.append(" 	                        ,NVL(C.REVENUE_AMOUNT,0) - NVL(D.AMOUNT,0) AS TOTAL_SALES_PROFIT_BUDGET                                                                  ");
        sb.append(" 	                        ,NVL(A.CT_AMOUNT, 0) - NVL(C.REVENUE_AMOUNT,0)AS REVENUE_AMOUNT1                                                                    ");
        sb.append(" 	                        ,NVL(D.AMOUNT,0) - NVL(B.AMT,0) AS RESULT_AMT1                                                                              ");
        sb.append(" 	                        ,(CASE WHEN NVL(C.REVENUE_AMOUNT,0) = 0 THEN 0 ELSE ROUND((NVL(D.AMOUNT,0) / NVL(C.REVENUE_AMOUNT,0)) *100 ,2) END)                                           ");
        sb.append(" 	                          - (CASE WHEN NVL(A.CT_AMOUNT, 0) = 0 THEN 0 ELSE ROUND((NVL(B.AMT,0) / NVL(A.CT_AMOUNT, 0)) * 100 ,2) END) AS RESULT_AMT_PER1                                     ");
        sb.append(" 	                        ,(NVL(C.REVENUE_AMOUNT,0) - NVL(D.AMOUNT,0)) - (NVL(A.CT_AMOUNT, 0) - NVL(B.AMT,0)) AS TOTAL_SALES_PROFIT_BUDGET1                                           ");
        sb.append(" 	                        ,A.DEGREE                                                                                                ");
        sb.append(" 	                        ,A.START_DATE                                                                                              ");
        sb.append(" 	                        ,A.END_DATE                                                                                                ");
        sb.append(" 	                        ,A.TOTAL_MONTH                                 ");
        sb.append(" 	                        ,NVL(ROUND(G.AMOUNT),0) AS CONTRACT_ORI_AMOUNT                                 ");
        sb.append(" 	                        ,CASE WHEN NVL(A.CT_AMOUNT,0) = 0 THEN '0'                                                                                            ");
        sb.append(" 	                               ELSE TO_CHAR(ROUND(NVL((C.REVENUE_AMOUNT / A.CT_AMOUNT),0) *100 ,2), 'FM9,999,990.0') || '%' END AS SALES_PER                                                  ");
        sb.append(" 	                        ,TO_CHAR(NVL((SELECT T.OBJECTIVE_MON                                                                                                                                 ");
        sb.append(" 	                                        FROM CBOSLIP.SPJPPM T                                                                                                                                                 ");
        sb.append(" 	                                       WHERE 1 = 1                                                                                                                                                    ");
        sb.append(" 	                                         AND T.PROJECT_MANAGE_NO = A.PROJECT_MANAGE_NO                                                                                                                ");
        sb.append(" 	                                         AND T.ITEM_CODE = 'B'                                                                                                                                        ");
        sb.append(" 	                                         AND T.OBJECTIVE_MON <> 0                                                                                                                                     ");
        sb.append(" 	                                         AND NVL(T.DEGREE, 1) = NVL((SELECT MAX(DEGREE)                                                                                                               ");
        sb.append(" 	                                                                       FROM CBOSLIP.SPJTM                                                                                                                     ");
        sb.append(" 	                                                                      WHERE ORG_ID = T.ORG_ID                                                                                                         ");
        sb.append(" 	                                                                        AND PROJECT_MANAGE_NO = T.PROJECT_MANAGE_NO ),1)                                                                              ");
        sb.append(" 	                                                                        AND T.YYYYMM = (SELECT MAX(B.YYYYMM)                                                                                                                         ");
        sb.append(" 	                                                                                          FROM CBOSLIP.SPJPPM B                                 ");
        sb.append(" 	                                                                                         WHERE 1 = 1                                                                                                                                 ");
        sb.append(" 	                                                                                           AND B.PROJECT_MANAGE_NO = A.PROJECT_MANAGE_NO                                                                                             ");
        sb.append(" 	                                                                                           AND B.ITEM_CODE = 'B'                                                                                                                     ");
        sb.append(" 	                                                                                           AND B.OBJECTIVE_MON <> 0                                                                                                                  ");
        sb.append(" 	                                                                                           AND NVL(B.DEGREE, 1) = NVL((SELECT MAX(DEGREE)                                                                                            ");
        sb.append(" 	                                                                                                                         FROM CBOSLIP.SPJTM                                 ");
        sb.append(" 	                                                                                                                        WHERE ORG_ID = A.ORG_ID                                                                                      ");
        sb.append(" 	                                                                                                                          AND PROJECT_MANAGE_NO = B.PROJECT_MANAGE_NO ),1))),0), 'FM9,999,990.0') || '%' AS PROCESS_PER              ");
        sb.append(" 	                       ,CASE WHEN NVL(A.CT_AMOUNT,0) = 0 THEN '0'                                                                                                   ");
        sb.append(" 	                              ELSE TO_CHAR(ROUND(NVL(((SELECT SUM(BILL_AMOUNT)                                                                          ");
        sb.append(" 	                                                         FROM CBOSLIP.CBO_IMS_BILL_T                                                                     ");
        sb.append(" 	                                                        WHERE 1 = 1                                                                               ");
        sb.append(" 	                                                          AND PROJECT_NUMBER = A.PROJECT_CODE                                                                 ");
        sb.append(" 	                                                        GROUP BY PROJECT_NUMBER)/ A.CT_AMOUNT),0) *100 ,2), 'FM9,999,990.0') || '%' END AS CLAIM_PER                                     ");
        sb.append(" 	                       ,CASE WHEN NVL(A.CT_AMOUNT,0) = 0 THEN '0'                                                                                                   ");
        sb.append(" 	                              ELSE TO_CHAR(ROUND(NVL(((SELECT SUM(RECEIPT_AMOUNT)                                                                        ");
        sb.append(" 	                                                         FROM CBOSLIP.CBO_IMS_RECEIPT_T                                                                     ");
        sb.append(" 	                                                        WHERE 1 = 1                                                                               ");
        sb.append(" 	                                                          AND PROJECT_NUMBER = A.PROJECT_CODE                                                                 ");
        sb.append(" 	                                                        GROUP BY PROJECT_NUMBER)/ A.CT_AMOUNT),0) *100 ,2), 'FM9,999,990.0') || '%' END AS COLLECTION_PER                                   ");
        sb.append(" 	                FROM CBOSLIP.SPJTM A                                 ");
        sb.append(" 	                     ,(SELECT ORG_ID                                             ");
        sb.append(" 	                              ,PROJECT_MANAGE_NO                                       ");
        sb.append(" 	                              ,DEGREE                                             ");
        sb.append(" 	                              ,SUM(NVL(AMT,0)) AS AMT                                     ");
        sb.append(" 	                         FROM CBOSLIP.SPJPBM                                            ");
        sb.append(" 	                        WHERE 1=1                                             ");
        sb.append(" 	                       GROUP BY ORG_ID                                           ");
        sb.append(" 	                                ,PROJECT_MANAGE_NO                                     ");
        sb.append(" 	                                ,DEGREE)B                                 ");
        sb.append(" 	                     ,(SELECT PROJECT_NUMBER                                                   ");
        sb.append(" 	                              ,SUM(NVL(REVENUE_AMOUNT,0)) AS REVENUE_AMOUNT                                   ");
        sb.append(" 	                         FROM CBOSLIP.CBO_PA_REVENUE_T                                              ");
        sb.append(" 	                        WHERE 1=1                                                       ");
        sb.append(" 	                          AND REVENUE_PERIOD < TO_CHAR(SYSDATE,'YYYY-MM')                                   ");
        sb.append(" 	                       GROUP BY PROJECT_NUMBER)C                                                                                  ");
        sb.append(" 	                     ,(SELECT Z.ORG_ID                                                         ");
        sb.append(" 	                              ,Z.PROJECT_NUMBER                                                    ");
        sb.append(" 	                              ,Z.TASK_NUMBER                                                      ");
        sb.append(" 	                              ,SUM(Z.BUDGET_AMOUNT) + SUM(EMPM_AMOUNT) AS AMOUNT                                   ");
        sb.append(" 	                              ,SUM(Z.FTAM_AMOUNT) + SUM(EMPM_AMOUNT) AS FTAM                                     ");
        sb.append(" 	                             FROM (SELECT ORG_ID                                                     ");
        sb.append(" 	                                          ,PROJECT_NUMBER                                                 ");
        sb.append(" 	                                          ,'3CT' AS TASK_NUMBER                                             ");
        sb.append(" 	                                          ,SUM(NVL(AMOUNT,0)) AS BUDGET_AMOUNT                                     ");
        sb.append(" 	                                          ,0 AS EMPM_AMOUNT                                               ");
        sb.append(" 	                                          ,0 AS FTAM_AMOUNT                                               ");
        sb.append(" 	                                     FROM CBOSLIP.CBO_IMS_EXPENSE_ACT_T                                          ");
        sb.append(" 	                                    WHERE SEGMENT5 = '3CT'                                               ");
        sb.append(" 	                                      AND ACCT_CODE LIKE '53%'                                             ");
        sb.append(" 	                                      AND PERIOD_YEAR||LPAD(PERIOD_NUM,2,0) < TO_CHAR(SYSDATE,'YYYYMM')                                     ");
        sb.append(" 	                                   GROUP BY ORG_ID                                                   ");
        sb.append(" 	                                            ,PROJECT_NUMBER                                               ");
        sb.append(" 	                                   UNION ALL                                                     ");
        sb.append(" 	                                   SELECT T.ORG_ID                                                    ");
        sb.append(" 	                                          ,T.PROJECT_NUMBER                                                ");
        sb.append(" 	                                          ,'3CT' AS TASK_NUMBER                                              ");
        sb.append(" 	                                          ,0                                                       ");
        sb.append(" 	                                          ,0                                                       ");
        sb.append(" 	                                          ,SUM(NVL(AMOUNT, 0)) AS FTAM_AMOUNT                                       ");
        sb.append(" 	                                     FROM CBOSLIP.CBO_IMS_EXPENSE_ACT_T T                                       ");
        sb.append(" 	                                          ,CBOSLIP.SPJTM M                                                   ");
        sb.append(" 	                                    WHERE T.PROJECT_NUMBER = M.PROJECT_CODE                                       ");
        sb.append(" 	                                      AND T.SEGMENT5 = '3CT'                                               ");
        sb.append(" 	                                      AND T.ACCT_CODE LIKE '53%'                                             ");
        sb.append(" 	                                      AND T.PERIOD_YEAR||LPAD(T.PERIOD_NUM,2,0) < TO_CHAR(SYSDATE,'YYYYMM')                                                                       ");
        sb.append(" 	                                      AND M.DEGREE = (SELECT MAX(DEGREE) FROM CBOSLIP.SPJTM WHERE ORG_ID = M.ORG_ID AND PROJECT_MANAGE_NO = M.PROJECT_CODE || M.TASK_NUMBER)                                           ");
        sb.append(" 	                                      AND CASE WHEN T.PERIOD_NUM < 10 THEN T.PERIOD_YEAR || '0' || T.PERIOD_NUM ELSE T.PERIOD_YEAR || T.PERIOD_NUM END >= SUBSTR(M.START_DATE, 0,6)                                   ");
        sb.append(" 	                                      AND CASE WHEN T.PERIOD_NUM < 10 THEN T.PERIOD_YEAR || '0' || T.PERIOD_NUM ELSE T.PERIOD_YEAR || T.PERIOD_NUM END <= SUBSTR(M.END_DATE, 0,6)                                   ");
        sb.append(" 	                                   GROUP BY T.ORG_ID                                                   ");
        sb.append(" 	                                            ,T.PROJECT_NUMBER                                               ");
        sb.append(" 	                                            ,M.START_DATE                                                 ");
        sb.append(" 	                                            ,M.END_DATE)Z                                                                           ");
        sb.append(" 	                    GROUP BY Z.ORG_ID                                                     ");
        sb.append(" 	                             ,Z.PROJECT_NUMBER                                                 ");
        sb.append(" 	                             ,Z.TASK_NUMBER)D                                   ");
        sb.append(" 	            ,(SELECT MAX(SPHD.VERSION) VERSION                                                     ");
        sb.append(" 	                              ,SPHD.PROJECT_NUMBER                                                     ");
        sb.append(" 	                              ,SPHD.ORG_ID                                                     ");
        sb.append(" 	                              ,SPHD.ADD_USER_ID                                                     ");
        sb.append(" 	                         FROM CBOSFA.SFA_PJTHD SPHD                                                     ");
        sb.append(" 	                        WHERE 1=1                                                         ");
        sb.append(" 	                        GROUP BY SPHD.PROJECT_NUMBER                                                     ");
        sb.append(" 	                                 ,SPHD.ORG_ID                                                     ");
        sb.append(" 	                                 ,SPHD.ADD_USER_ID) E                                                     ");
        sb.append(" 	                   ,(SELECT SP.PROJECT_NUMBER                                  ");
        sb.append(" 	                            ,SP.TASK_NUMBER                                  ");
        sb.append(" 	                            ,SP.ORG_ID                                 ");
        sb.append(" 	                            ,SP.VERSION                                 ");
        sb.append(" 	                            ,SUM(SP.TOTAL_OFFER_UNIT_COST) TOTAL_OFFER_UNIT_COST2                                 ");
        sb.append(" 	                            ,SUM(SPD.AMOUNT) AMOUNT                                 ");
        sb.append(" 	                       FROM CBOSFA.SFA_PJTDT_BD SPB                                 ");
        sb.append(" 	                            ,CBOSFA.SFA_PJTDT    SP                                 ");
        sb.append(" 	                            ,CBOSFA.SFA_PJTDT_BDD SPD                                 ");
        sb.append(" 	                            ,(SELECT MAX(SPJ.VERSION) AS VERSION                                 ");
        sb.append(" 	                                    ,SPJ.PROJECT_NUMBER                                 ");
        sb.append(" 	                                    ,SPJ.ORG_ID                                 ");
        sb.append(" 	                               FROM SFA_PJTHD SPJ                                 ");
        sb.append(" 	                                    ,APPS.CBO_SP_VENDOR_CUSTOMER_V CSVC                                 ");
        sb.append(" 	                              WHERE 1=1                                 ");
        sb.append(" 	                                AND SPJ.CUSTOMER_ID = CSVC.CUSTOMER_ID                                 ");
        sb.append(" 	                                AND SPJ.INTERFACE_FLAG = 'S'                                        ");
        sb.append(" 	                             GROUP BY SPJ.PROJECT_NUMBER                                 ");
        sb.append(" 	                                      ,SPJ.ORG_ID) SPJT                                   ");
        sb.append(" 	                      WHERE SPB.CNT = SP.CNT                                                                                                              ");
        sb.append(" 	                        AND SPB.COST_TYPE = 'CONT'                                 ");
        sb.append(" 	                        AND SPB.VERSION = SP.VERSION                                 ");
        sb.append(" 	                        AND SPB.VERSION = SPJT.VERSION                                 ");
        sb.append(" 	                        AND SPB.PROJECT_NUMBER = SPJT.PROJECT_NUMBER                                 ");
        sb.append(" 	                        AND SP.ORG_ID = SPJT.ORG_ID                                 ");
        sb.append(" 	                        AND SPB.PROJECT_NUMBER = SP.PROJECT_NUMBER                                 ");
        sb.append(" 	                        AND SP.EMRQ_ITEM_CODE = 'H11'                                                                       ");
        sb.append(" 	                        AND SP.BUDGET_STATUS >= '40'                                                                        ");
        sb.append(" 	                        AND SP.EMRQ_COUNT <> '0'                                 ");
        sb.append(" 	                        AND SPD.SFA_PJTDT_BD_ID = SPB.SFA_PJTDT_BD_ID                                 ");
        sb.append(" 	                        AND (SPD.LINE_NO <> '23' OR SPD.ACCOUNT_CODE <> '5310510')                                           ");
        sb.append(" 	                     GROUP BY SP.PROJECT_NUMBER                                 ");
        sb.append(" 	                              ,SP.TASK_NUMBER                                 ");
        sb.append(" 	                              ,SP.VERSION                                 ");
        sb.append(" 	                              ,SP.ORG_ID)G                                 ");
        sb.append(" 	              WHERE 1 = 1                                  ");
        sb.append(" 	                AND A.ORG_ID = B.ORG_ID(+)                                               ");
        sb.append(" 	                AND A.PROJECT_MANAGE_NO = B.PROJECT_MANAGE_NO(+)                                 ");
        sb.append(" 	                AND A.DEGREE = B.DEGREE(+)                                 ");
        sb.append(" 	                AND A.PROJECT_CODE = C.PROJECT_NUMBER(+)                                 ");
        sb.append(" 	                AND A.ORG_ID = D.ORG_ID(+)                                                            ");
        sb.append(" 	                AND A.PROJECT_CODE = D.PROJECT_NUMBER(+)                                      ");
        sb.append(" 	                AND A.PROJECT_CODE = G.PROJECT_NUMBER(+)                                 ");
        sb.append(" 	                AND A.ORG_ID = G.ORG_ID(+)                                 ");
        sb.append(" 	                AND A.DEGREE = (SELECT MAX(DEGREE)                                  ");
        sb.append(" 	                                  FROM CBOSLIP.SPJTM                                  ");
        sb.append(" 	                                 WHERE ORG_ID = A.ORG_ID                                  ");
        sb.append(" 	                                   AND PROJECT_MANAGE_NO = A.PROJECT_MANAGE_NO)                                 ");
        sb.append(" 	               AND A.PROJECT_CODE = G.PROJECT_NUMBER(+)                                 ");
        sb.append(" 	               AND A.TASK_NUMBER = G.TASK_NUMBER(+)                                 ");
        sb.append(" 	               AND A.ORG_ID = G.ORG_ID(+)                                 ");
        sb.append(" 	               AND A.PROJECT_CODE = E.PROJECT_NUMBER                                 ");
        sb.append(" 	               )X                                 ");
        sb.append(" 	            WHERE 1 = 1                                                     ");
        sb.append(" 	            AND X.ORG_ID = :compCd					 						");


//        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getStartDate())) {
//            sb.append("	A.START_DATE <= :startDate	");
//        }
//        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getEndDate())) {
//            sb.append("	A.END_DATE >= :endDate	");
//        }

        if("Y".equals(pjtPerfAnalysisDto.getInAmtPerFlag())){
            sb.append(" AND X.PROCESS_PER <> '100.0%'											");
        }

        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectName())) {
            sb.append("	AND UPPER(X.PROJECT_NAME) LIKE '%' || UPPER(:projectName) || '%'	");
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectGubun())) {
            sb.append("	AND X.PROJECT_GUBUN = :projectGubun  								");
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getCountryCode())) {
            sb.append("	AND UPPER(X.COUNTRY_CODE) = UPPER(:countryCode) 					");
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getCountryName())) {
            sb.append("	AND UPPER(X.COUNTRY_NAME) LIKE '%' || UPPER(:countryName) || '%'  	");
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectManageNo())) {
            sb.append("	AND UPPER(X.PROJECT_MANAGE_NO) = UPPER(:projectManageNo) 				");
        }

        sb.append(" 	ORDER BY X.PROJECT_CODE                        ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);

//        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getStartDate())) {
//            query.setParameter("startDate", pjtPerfAnalysisDto.getStartDate());
//        }
//        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getEndDate())) {
//            query.setParameter("endDate", pjtPerfAnalysisDto.getEndDate());
//        }

        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectName())) {
            query.setParameter("projectName", pjtPerfAnalysisDto.getProjectName());
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectGubun())) {
            query.setParameter("projectGubun", pjtPerfAnalysisDto.getProjectGubun());
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getCountryCode())) {
            query.setParameter("countryCode", pjtPerfAnalysisDto.getCountryCode());
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getCountryName())) {
            query.setParameter("countryName", pjtPerfAnalysisDto.getCountryName());
        }
        if(!StringUtils.isEmpty(pjtPerfAnalysisDto.getProjectManageNo())) {
            query.setParameter("projectManageNo", pjtPerfAnalysisDto.getProjectManageNo());
        }

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, PjtPerfAnalysisDto.class);
    }

    @Override
    public List<PjtPerfAnalysisDto> getProfitSalesInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {

        String projectNumber = pjtPerfAnalysisDto.getProjectNumber();

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 SELECT REVENUE_PERIOD				 			\n");
        sb.append(" 	      , PROJECT_NUMBER				 			\n");
        sb.append(" 	      , PROJECT_NAME				 			\n");
        sb.append(" 	      , ITEM_GROUP					 			\n");
        sb.append(" 	      , SO_PA_TYPE					 			\n");
        sb.append(" 	      , SO_PA_NUMBER				 			\n");
        sb.append(" 	      , SO_LINE_PA_EVENT_NUMBER		 			\n");
        sb.append(" 	      , ITEM_CODE					 			\n");
        sb.append(" 	      , SUM(ENTERED_AMOUNT) AS ENTERED_AMOUNT	\n");
        sb.append(" 	      , SUM(REVENUE_AMOUNT) AS REVENUE_AMOUNT	\n");
        sb.append(" 	      , COA_SEGMENT1				 			\n");
        sb.append(" 	      , COA_SEGMENT1_DESC			 			\n");
        sb.append(" 	   FROM CBOSLIP.CBO_PA_REVENUE_T				\n");
        sb.append("        WHERE PROJECT_NUMBER = :projectNumber 		\n");
        sb.append(" 	 	 AND ITEM_GROUP = '3CT' 			\n");//task로 집계할수 없음 제품군(공사 = 3CT)로 걸어야함
        sb.append(" 	     AND REVENUE_PERIOD < TO_CHAR(SYSDATE,'YYYY-MM')		\n");
        sb.append(" 	  GROUP BY REVENUE_PERIOD				 		\n");
        sb.append(" 	      , PROJECT_NUMBER				 			\n");
        sb.append(" 	      , PROJECT_NAME				 			\n");
        sb.append(" 	      , ITEM_GROUP					 			\n");
        sb.append(" 	      , SO_PA_TYPE					 			\n");
        sb.append(" 	      , SO_PA_NUMBER				 			\n");
        sb.append(" 	      , SO_LINE_PA_EVENT_NUMBER		 			\n");
        sb.append(" 	      , ITEM_CODE					 			\n");
        sb.append(" 	      , COA_SEGMENT1				 			\n");
        sb.append(" 	      , COA_SEGMENT1_DESC			 			\n");
        sb.append(" 	  UNION ALL				 						\n");
        sb.append(" 	 SELECT '합계'				 					\n");
        sb.append(" 	      , ''				 						\n");
        sb.append(" 	      , ''				 						\n");
        sb.append(" 	      , ''					 					\n");
        sb.append(" 	      , ''					 					\n");
        sb.append(" 	      , ''				 						\n");
        sb.append(" 	      , ''		 								\n");
        sb.append(" 	      , ''					 					\n");
        sb.append(" 	      , SUM(ENTERED_AMOUNT)				 		\n");
        sb.append(" 	      , SUM(REVENUE_AMOUNT)				 		\n");
        sb.append(" 	      , ''					 					\n");
        sb.append(" 	      , '' 				 						\n");
        sb.append(" 	   FROM CBOSLIP.CBO_PA_REVENUE_T				\n");
        sb.append("        WHERE PROJECT_NUMBER = :projectNumber 		\n");
        sb.append(" 	 	 AND ITEM_GROUP = '3CT' 			\n");//task로 집계할수 없음 제품군(공사 = 3CT)로 걸어야함
        sb.append(" 	     AND REVENUE_PERIOD < TO_CHAR(SYSDATE,'YYYY-MM')		\n");
        sb.append(" 	  ORDER BY REVENUE_PERIOD						\n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("projectNumber", projectNumber);


        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, PjtPerfAnalysisDto.class);
    }

    @Override
    public List<PjtPerfAnalysisDto> getClaimInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {

        String projectNumber = pjtPerfAnalysisDto.getProjectNumber();

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 SELECT SUBSTR(T.PERIOD_NAME,1,4) || '-' || SUBSTR(T.PERIOD_NAME,5,2) AS PERIOD_NAME	\n");
        sb.append(" 	      , T.PROJECT_NUMBER				 												\n");
        sb.append(" 	      , T.PROJECT_NAME				 													\n");
        sb.append(" 	      , '3CT' AS ITEM_GROUP					 											\n");
        sb.append(" 	      , SUM(T.BILL_AMOUNT) AS BILL_AMOUNT					 							\n");
        sb.append(" 	   FROM CBOSLIP.CBO_IMS_BILL_T T														\n");
        sb.append(" 	  WHERE T.PROJECT_NUMBER = :projectNumber 											\n");
        sb.append(" 	                    AND T.PERIOD_NAME < TO_CHAR(SYSDATE,'YYYYMM')						\n");
        sb.append(" 	  GROUP BY T.PROJECT_NUMBER				 												\n");
        sb.append(" 	         , T.PROJECT_NAME				 												\n");
        sb.append(" 	         , T.PERIOD_NAME					 											\n");
        sb.append(" 	  UNION ALL				 											\n");
        sb.append(" 	 SELECT '합계'				 										\n");
        sb.append(" 	      , ''				 											\n");
        sb.append(" 	      , ''				 											\n");
        sb.append(" 	      , ''					 										\n");
        sb.append(" 	      , SUM(BILL_AMOUNT)				 							\n");
        sb.append(" 	   FROM CBOSLIP.CBO_IMS_BILL_T										\n");
        sb.append(" 	  WHERE PROJECT_NUMBER = :projectNumber 							\n");
        sb.append(" 	                    AND PERIOD_NAME < TO_CHAR(SYSDATE,'YYYYMM')		\n");
        sb.append(" 	  ORDER BY PERIOD_NAME												\n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("projectNumber", projectNumber);


        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, PjtPerfAnalysisDto.class);
    }

    @Override
    public List<PjtPerfAnalysisDto> getCollectionInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {

        String projectNumber = pjtPerfAnalysisDto.getProjectNumber();

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 SELECT SUBSTR(T.PERIOD_NAME,1,4) || '-' || SUBSTR(T.PERIOD_NAME,5,2) AS PERIOD_NAME	\n");
        sb.append(" 	      , T.PROJECT_NUMBER				 												\n");
        sb.append(" 	      , T.PROJECT_NAME				 													\n");
        sb.append(" 	      , '3CT' AS ITEM_GROUP					 											\n");
        sb.append(" 	      , SUM(T.RECEIPT_AMOUNT) AS RECEIPT_AMOUNT					 						\n");
        sb.append(" 	   FROM CBOSLIP.CBO_IMS_RECEIPT_T T														\n");
        sb.append(" 	  WHERE T.PROJECT_NUMBER = :projectNumber 											\n");
        sb.append(" 	                    AND T.PERIOD_NAME < TO_CHAR(SYSDATE,'YYYYMM')						\n");
        sb.append(" 	  GROUP BY T.PROJECT_NUMBER				 												\n");
        sb.append(" 	         , T.PROJECT_NAME				 												\n");
        sb.append(" 	         , T.PERIOD_NAME					 											\n");
        sb.append(" 	  UNION ALL				 											\n");
        sb.append(" 	 SELECT '합계'				 										\n");
        sb.append(" 	      , ''				 											\n");
        sb.append(" 	      , ''				 											\n");
        sb.append(" 	      , ''					 										\n");
        sb.append(" 	      , SUM(RECEIPT_AMOUNT)				 							\n");
        sb.append(" 	   FROM CBOSLIP.CBO_IMS_RECEIPT_T									\n");
        sb.append(" 	  WHERE PROJECT_NUMBER = :projectNumber 							\n");
        sb.append(" 	                    AND PERIOD_NAME < TO_CHAR(SYSDATE,'YYYYMM')		\n");
        sb.append(" 	  ORDER BY PERIOD_NAME												\n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("projectNumber", projectNumber);


        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, PjtPerfAnalysisDto.class);
    }

    @Override
    public List<PjtPerfAnalysisDto> getTotalAmt(PjtPerfAnalysisDto pjtPerfAnalysisDto) {

        String compCd = util.getLoginCompCd();

        String projectNumber = pjtPerfAnalysisDto.getProjectNumber();

        String degree = pjtPerfAnalysisDto.getDegree();

        StringBuilder sb = new StringBuilder();

        sb.append("                	SELECT BAD.PROJECT_NUMBER															\n");
        sb.append("                		 , PJT.PROJECT_NAME																\n");
        sb.append("                		 , BAD.PERIOD_YEAR																\n");
        sb.append("                		 , BAD.PERIOD_NUM																\n");
        sb.append("                		 , SUM(BAD.AMOUNT) AS TOTAL_AMOUNT												\n");
        sb.append("                		 , PJT.START_DATE																\n");
        sb.append("                		 , PJT.END_DATE																	\n");
        sb.append("             		  FROM CBOSLIP.CBO_IMS_EXPENSE_ACT_T BAD										\n");
        sb.append("             		     , SPJTM PJT																	\n");
        sb.append("   				 WHERE BAD.ORG_ID = PJT.ORG_ID														\n");
        sb.append("					   AND BAD.PROJECT_NUMBER = PJT.PROJECT_CODE										\n");
        sb.append("					   AND BAD.SEGMENT5 = '3CT'															\n");
        sb.append("					   AND BAD.ACCT_CODE LIKE '53%'														\n");
        sb.append(" 	                   AND BAD.PERIOD_YEAR||LPAD(BAD.PERIOD_NUM,2,0) < TO_CHAR(SYSDATE,'YYYYMM') 		\n");
        sb.append("     AND BAD.ORG_ID = :compCd															        	\n");
        sb.append("     AND BAD.PROJECT_NUMBER = :projectNumber														\n");
        sb.append("     AND PJT.DEGREE = :degree																\n");
        sb.append("            		 GROUP BY BAD.PROJECT_NUMBER														\n");
        sb.append("                   		, BAD.PERIOD_YEAR															\n");
        sb.append("                   		, BAD.PERIOD_NUM															\n");
        sb.append("                   		, PJT.PROJECT_NAME															\n");
        sb.append("                   		, PJT.START_DATE															\n");
        sb.append("                   		, PJT.END_DATE																\n");
        sb.append("                   UNION ALL																			\n");
        sb.append("           		SELECT '합계'																			\n");
        sb.append("                		 , ''																			\n");
        sb.append("                		 , NULL																			\n");
        sb.append("                		 , NULL																			\n");
        sb.append("                		 , SUM(BAD.AMOUNT) AS TOTAL_AMOUNT												\n");
        sb.append("                		 , ''																			\n");
        sb.append("                		 , ''																			\n");
        sb.append("             		  FROM CBOSLIP.CBO_IMS_EXPENSE_ACT_T BAD											\n");
        sb.append("             		     , SPJTM PJT																	\n");
        sb.append("   				 WHERE BAD.ORG_ID = PJT.ORG_ID														\n");
        sb.append("					   AND BAD.PROJECT_NUMBER = PJT.PROJECT_CODE										\n");
        sb.append("					   AND BAD.SEGMENT5 = '3CT'															\n");
        sb.append("					   AND BAD.ACCT_CODE LIKE '53%'														\n");
        sb.append(" 	                   AND BAD.PERIOD_YEAR||LPAD(BAD.PERIOD_NUM,2,0) < TO_CHAR(SYSDATE,'YYYYMM') 		\n");
        sb.append("                        AND BAD.ORG_ID = :compCd																\n");
        sb.append("                        AND BAD.PROJECT_NUMBER = :projectNumber 														\n");
        sb.append("                        AND PJT.DEGREE = :degree																\n");
        sb.append("					  ORDER BY PERIOD_YEAR , PERIOD_NUM													\n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("projectNumber", projectNumber);
        query.setParameter("degree", degree);


        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, PjtPerfAnalysisDto.class);
    }
}

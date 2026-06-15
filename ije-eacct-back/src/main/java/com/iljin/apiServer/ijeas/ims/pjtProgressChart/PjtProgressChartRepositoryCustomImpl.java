package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis.PjtPerfAnalysisDto;
import com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis.PjtPerfAnalysisRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PjtProgressChartRepositoryCustomImpl implements PjtProgressChartRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;



    /* 가변형 컬럼 조회로 인한 네이티브 쿼리 사용. */
    @Override
    public List<Map<String,Object>> getProgressChartList(PjtProgressChartDto pjtProgressChartDto) {

        String compCd = util.getLoginCompCd();

        String MonthList = String.join(",", pjtProgressChartDto.getParam());

        /********************************************************************************************************
         PJT진행현황표
         월별 계획실적 내역에대한 Parameter 부분 &PARAM은 조회조건의 착공일 준공일을 월별로 ,붙여 연결해야함
         예시) 착공일~준공일 202301~202303 이면 &PARAM 은 202301,202302,202303 으로 변환하여 호출
         ********************************************************************************************************/
        StringBuilder sb = new StringBuilder();
        sb.append("    SELECT L.LINE                                                       ");
        sb.append(" 	           ,A.ORG_ID                                               ");
        sb.append(" 	           ,A.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	           ,A.PROJECT_GUBUN                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN A.PROJECT_NAME                                             ");     /*PJT명*/
        sb.append(" 	                 WHEN L.LINE = 2 THEN A.PROJECT_NAME                                             ");     /*PJT명*/
        sb.append(" 	                 WHEN L.LINE = 3 THEN A.PROJECT_NAME                                             ");     /*PJT명*/
        sb.append(" 	                 WHEN L.LINE = 4 THEN A.CONTENT                                             ");          /*선종*/
        sb.append(" 	            END AS PJT_LINE                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN A.COUNTRY_NAME                                             ");     /*PJT정보:국가*/
        sb.append(" 	                 WHEN L.LINE = 2 THEN A.CUSTOMER_NAME                                             ");    /*PJT정보:거래처*/
        sb.append(" 	                 WHEN L.LINE = 3 THEN A.PJT_SITE_MANAGER                                             "); /*PJT정보:담당자*/
        sb.append(" 	                 WHEN L.LINE = 4 THEN A.PROJECT_PERIOD                                             ");   /*PJT정보:기간*/
        sb.append(" 	            END AS PJT_INFO                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN A.CONTRACT_INFO                                             ");    /*수주*/
        sb.append(" 	                 WHEN L.LINE = 2 THEN A.CONTRACT_AMOUNT                                             ");  /*수주금액*/
        sb.append(" 	                 WHEN L.LINE = 3 THEN A.REVENUE_INFO                                             ");     /*매출*/
        sb.append(" 	                 WHEN L.LINE = 4 THEN A.REVENUE_AMOUNT                                             ");   /*매출금액*/
        sb.append(" 	            END AS PJT_REV                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN A.CONTRACT_COST_AMOUNT                                             "); /*수주원가*/
        sb.append(" 	                 WHEN L.LINE = 2 THEN A.CONTRACT_COST_RATE                                             ");   /*수주원가율*/
        sb.append(" 	                 WHEN L.LINE = 3 THEN A.BUDGET_AMOUNT                                             ");        /*실행예산*/
        sb.append(" 	                 WHEN L.LINE = 4 THEN A.BUDGET_RATE                                             ");          /*실행예산율*/
        sb.append(" 	            END AS PJT_REV2                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN '공정'||CHR(10)||'(%)'                                             ");
        sb.append(" 	                 WHEN L.LINE = 2 THEN '공정'||CHR(10)||'(%)'                                             ");
        sb.append(" 	                 WHEN L.LINE = 3 THEN '실행'||CHR(10)||'예산'||CHR(10)||'(%)'                                             ");
        sb.append(" 	                 WHEN L.LINE = 4 THEN '실행'||CHR(10)||'예산'||CHR(10)||'(%)'                                             ");
        sb.append(" 	            END AS CT_BD                                             ");
        sb.append(" 	           ,CASE WHEN L.LINE = 1 THEN '계획'                                             ");
        sb.append(" 	                 WHEN L.LINE = 2 THEN '실적'                                             ");
        sb.append(" 	                 WHEN L.LINE = 3 THEN '계획'                                             ");
        sb.append(" 	                 WHEN L.LINE = 4 THEN '실적'                                             ");
        sb.append(" 	            END AS PLAN_ACT                                             ");
        sb.append(" 	           ,A.ISSUE                                             ");                /*이슈사항*/
        sb.append(" 	           ,A.ISSUE_MEASURE                                             ");        /*대책*/
        sb.append(" 	           ,A.ISSUE_CHANGE_DATE                                             ");    /*최종수정일*/
        sb.append(" 	           ,A.ISSUE_CHANGE_USER_ID                                             "); /*최종수정자*/
        sb.append(" 	           ,A.REMARK                                             ");               /*비고*/
        sb.append(" 	           ,A.PROJECT_CODE                                             ");
        sb.append(" 	           ,A.TASK_NUMBER                                             ");
        sb.append(" 	           ,A.WEEKLY_ISSUE                                             ");
        sb.append(" 	           ,A.VERSION                                             ");
        sb.append(" 	           ,M.ADD_USER_ID                                             ");
        sb.append(" 	           ,CTP.*                                             ");
        sb.append(" 	       FROM (                                             ");
        sb.append(" 	             SELECT PJT.ORG_ID                                             ");
        sb.append(" 	                   ,PJT.PROJECT_ID                                            ");
        sb.append(" 	                   ,PJT.PROJECT_MANAGE_NO                                            ");
        sb.append(" 	                   ,COD.TEXT2                                                                              AS PROJECT_GUBUN                                             "); /*구분*/
        sb.append(" 	                   ,PJT.PROJECT_NAME                                                                       AS PROJECT_NAME                                             "); /*PJT명*/
        sb.append(" 	                   ,PJL.CONTENT                                                                            AS CONTENT                                             "); /*선종*/
        sb.append(" 	                   ,NVL(SFP.COUNTRY_NAME,PJT.COUNTRY_NAME)                                                 AS COUNTRY_NAME                                             "); /*PJT정보:국가*/
        sb.append(" 	                   ,NVL(CUS.CUSTOMER_NAME,PJT.CUSTOMER_NAME)                                               AS CUSTOMER_NAME                                             "); /*PJT정보:거래처*/
        sb.append(" 	                   ,'소장/담당자 : ' || PJT.PJT_SITE_MANAGER_USER_NAME                                         AS PJT_SITE_MANAGER                                             "); /*PJT정보:담당자*/
        sb.append(" 	                   ,'착공/준공:' ||                                              ");
        sb.append(" 	                    TO_CHAR(TO_DATE(PJT.START_DATE,'YYYYMMDD'), 'YY.MM') || '~' ||                                             ");
        sb.append(" 	                    TO_CHAR(TO_DATE(PJT.END_DATE  ,'YYYYMMDD'), 'YY.MM')                                   AS PROJECT_PERIOD                                             "); /*PJT정보:기간*/
        sb.append(" 	                   ,'수주(억)'                                                                                AS CONTRACT_INFO                                             "); /*수주*/
        sb.append(" 	                   ,TO_CHAR(ROUND((NVL(TO_NUMBER(PJT.CT_AMOUNT),1) / 100000000), 2), 'FM9,999,990.00')     AS CONTRACT_AMOUNT                                             "); /*수주금액*/
        sb.append(" 	                   ,'매출(억)'                                                                                AS REVENUE_INFO                                             "); /*매출*/
        sb.append(" 	                   ,TO_CHAR(ROUND((REV.REVENUE_AMOUNT / 100000000), 2), 'FM9,999,990.00') || '(' ||                                             ");
        sb.append(" 	                     CASE WHEN NVL(TO_NUMBER(PJT.CT_AMOUNT),0) = 0 THEN '0'                                                                               ");
        sb.append(" 	                          ELSE TO_CHAR(ROUND((REV.REVENUE_AMOUNT / NVL(TO_NUMBER(PJT.CT_AMOUNT),1)) * 100, 1), 'FM9,999,990.0')                                                  ");
        sb.append(" 	                     END || '%' || ')'                                                                      AS REVENUE_AMOUNT                                             "); /*매출금액*/
        sb.append(" 	                   ,NVL(TO_CHAR(ROUND((CPL.CONTRACT_COST_AMOUNT  / 100000000), 2) ,'FM9,999,990.00'), '0') AS CONTRACT_COST_AMOUNT                                             "); /*수주원가*/
        sb.append(" 	                   ,CASE WHEN NVL(TO_NUMBER(PJT.CT_AMOUNT),0) = 0 THEN '0'                                                         ");
        sb.append(" 	                          ELSE NVL(TO_CHAR(ROUND((CPL.CONTRACT_COST_AMOUNT / NVL(TO_NUMBER(PJT.CT_AMOUNT),1)) * 100, 2), 'FM9,999,990.00'), '0')                                             ");
        sb.append(" 	                     END || '%'                                                                             AS CONTRACT_COST_RATE                                             "); /*수주원가율*/
        sb.append(" 	                   ,CASE WHEN NVL(BPL.BUDGET_AMOUNT,0) = 0 THEN '0'                                               ");
        sb.append(" 	                         ELSE NVL(TO_CHAR(ROUND((NVL(BPL.BUDGET_AMOUNT,0) / 100000000), 2) ,'FM9,999,990.00'),'0')                                             ");
        sb.append(" 	                    END                                                                                    AS BUDGET_AMOUNT                                             "); /*실행예산*/
        sb.append(" 	                   ,CASE WHEN NVL(BPL.BUDGET_AMOUNT,0) = 0 THEN '0'                                              ");
        sb.append(" 	                         ELSE NVL(TO_CHAR(ROUND((BPL.BUDGET_AMOUNT / NVL(TO_NUMBER(PJT.CT_AMOUNT),1) * 100) ,2),'FM9990.99'),'0')                                              ");
        sb.append(" 	                    END || '%'                                                                             AS BUDGET_RATE                                             "); /*실행예산율*/
        sb.append(" 	                   ,PJT.ISSUE                                                                              AS ISSUE                                             "); /*이슈사항*/
        sb.append(" 	                   ,PJT.ISSUE_MEASURE                                                                      AS ISSUE_MEASURE                                             "); /*대책*/
        sb.append(" 	                   ,PJT.ISSUE_CHANGE_DATE                                                                  AS ISSUE_CHANGE_DATE                                             "); /*최종수정일*/
        sb.append(" 	                   ,SLIP_GET_USER_NAME(PJT.ORG_ID, PJT.ISSUE_CHANGE_USER_ID)                               AS ISSUE_CHANGE_USER_ID                                             "); /*최종수정자*/
        sb.append(" 	                   ,PJT.REMARK                                                                             AS REMARK                                             "); /*비고*/
        sb.append(" 	                   ,PJT.PROJECT_CODE                                              ");
        sb.append(" 	                   ,PJT.TASK_NUMBER                                                   ");
        sb.append(" 	                   ,PJT.WEEKLY_ISSUE                                              ");
        sb.append(" 	                   ,SFP.VERSION                                             ");
        sb.append(" 	                   ,PJT.DEGREE                                             ");
        sb.append(" 	               FROM SPJTM  PJT                                             "); /*PJT 현장등록*/
        sb.append(" 	                   ,SCODE  COD                                             "); /*CODE 테이블*/
        sb.append(" 	                   ,SPJTML PJL                                             "); /*PJT현장등록 공사개요 LINE*/
        sb.append(" 	                   ,(SELECT *                                             ");
        sb.append(" 	                       FROM CBOSFA.SFA_PJTHD                                             ");
        sb.append(" 	                      WHERE (PROJECT_NUMBER,VERSION) IN(SELECT PROJECT_NUMBER                                             ");
        sb.append(" 	                                                              ,MAX(VERSION)                                             ");
        sb.append(" 	                                                          FROM CBOSFA.SFA_PJTHD                                             ");
        sb.append(" 	                                                         GROUP BY PROJECT_NUMBER)                                             ");
        sb.append(" 	                    )      SFP                                             "); /*SFA 프로젝트*/
        sb.append(" 	                   ,APPS.CBO_SP_VENDOR_CUSTOMER_V CUS                                             "); /*거래처기준정보*/
        sb.append(" 	                   ,(SELECT PROJECT_NUMBER                                             ");
        sb.append(" 	                           ,SUM(NVL(REVENUE_AMOUNT,0)) REVENUE_AMOUNT                                             ");
        sb.append(" 	                       FROM CBOSLIP.CBO_PA_REVENUE_T                                             ");
        sb.append(" 	                      WHERE ITEM_GROUP = '3CT'                                             ");
        sb.append(" 	                      GROUP BY PROJECT_NUMBER                                             ");
        sb.append(" 	                    )      REV                                              ");/*매출실적*/
        sb.append(" 	                   ,(SELECT SP.PROJECT_NUMBER                                             ");
        sb.append(" 	                           ,SP.TASK_NUMBER                                             ");
        sb.append(" 	                           ,SP.VERSION                                             ");
        sb.append(" 	                           ,SP.ORG_ID                                             ");
        sb.append(" 	                           ,SUM(NVL(SPD.AMOUNT,0)) CONTRACT_COST_AMOUNT                                             ");
        sb.append(" 	                       FROM CBOSFA.SFA_PJTDT_BD  SPB                                             ");
        sb.append(" 	                           ,CBOSFA.SFA_PJTDT     SP                                             ");
        sb.append(" 	                           ,CBOSFA.SFA_PJTDT_BDD SPD                                             ");
        sb.append(" 	                      WHERE SPB.CNT = SP.CNT                                              ");
        sb.append(" 	                        AND SPB.COST_TYPE       = 'CONT'                                             ");
        sb.append(" 	                        AND SPB.VERSION         = SP.VERSION                                             ");
        sb.append(" 	                        AND SPB.PROJECT_NUMBER  = SP.PROJECT_NUMBER                                             ");
        sb.append(" 	                        AND SP.EMRQ_ITEM_CODE   = 'H11'                                                      ");
        // /*AND SP.BUDGET_STATUS >= '40'*/ /*기존 전선시공관리(AS-IS)에 존재하는 조건이나 DATA확인하여 제거함 */
        sb.append(" 	                        AND SP.EMRQ_COUNT      <> '0'                                             ");
        sb.append(" 	                        AND SPD.SFA_PJTDT_BD_ID = SPB.SFA_PJTDT_BD_ID                                             ");
        sb.append(" 	                        AND SPD.LINE_NO         < '23'                                              ");
        sb.append(" 	                        AND SPD.ACCOUNT_CODE   <> '5310510'                                             ");
        sb.append(" 	                        AND (SP.TASK_ID,SP.VERSION) IN(SELECT TASK_ID                                             ");
        sb.append(" 	                                                             ,MAX(VERSION)                                             ");
        sb.append(" 	                                                         FROM CBOSFA.SFA_PJTDT                                             ");
        sb.append(" 	                                                        GROUP BY TASK_ID)                                             ");
        sb.append(" 	                      GROUP BY SP.PROJECT_NUMBER                                             ");
        sb.append(" 	                              ,SP.TASK_NUMBER                                             ");
        sb.append(" 	                              ,SP.VERSION                                             ");
        sb.append(" 	                              ,SP.ORG_ID                                             ");
        sb.append(" 	                    )      CPL                                              ");/*SFA 수주원가*/
        sb.append(" 	                   ,(SELECT ORG_ID                                                             ");
        sb.append(" 	                           ,PROJECT_MANAGE_NO                                                     ");
        sb.append(" 	                           ,DEGREE                                                           ");
        sb.append(" 	                           ,SUM(NVL(TO_NUMBER(AMT),0)) AS BUDGET_AMOUNT                                             ");
        sb.append(" 	                       FROM SPJPBM                                                                ");
        sb.append(" 	                      GROUP BY ORG_ID                                                             ");
        sb.append(" 	                              ,PROJECT_MANAGE_NO                                                   ");
        sb.append(" 	                              ,DEGREE                                             ");
        sb.append(" 	                    )      BPL                                              ");/*프로젝트 실행예산*/
        sb.append(" 	              WHERE PJT.PROJECT_GUBUN     = COD.CODE(+)                                             ");
        sb.append(" 	                AND COD.TYPE(+)           = 'F206'                                             ");
        sb.append(" 	                AND COD.LANGUAGE(+)       = 'KO'                                             ");
        sb.append(" 	                AND PJT.ORG_ID            = PJL.ORG_ID                                             ");
        sb.append(" 	                AND PJT.PROJECT_MANAGE_NO = PJL.PROJECT_MANAGE_NO(+)                                             ");
        sb.append(" 	                AND PJT.DEGREE            = PJL.DEGREE(+)                                             ");
        sb.append(" 	                AND PJL.CODE(+)           = 'A'                                             ");
        sb.append(" 	                AND PJL.SEQ(+)            = '1'                                             ");
        sb.append(" 	                AND PJT.PROJECT_ID        = SFP.PROJECT_ID(+)                                             ");
        sb.append(" 	                AND SFP.CUSTOMER_ID       = CUS.CUSTOMER_ID(+)                                             ");
        sb.append(" 	                AND PJT.PROJECT_CODE      = REV.PROJECT_NUMBER(+)                                             ");
        sb.append(" 	                AND PJT.PROJECT_CODE      = CPL.PROJECT_NUMBER(+)                                             ");
        sb.append(" 	                AND PJT.TASK_NUMBER       = CPL.TASK_NUMBER(+)                                             ");
        sb.append(" 	                AND PJT.ORG_ID            = CPL.ORG_ID(+)                                             ");
        sb.append(" 	                AND PJT.ORG_ID            = BPL.ORG_ID(+)                                             ");
        sb.append(" 	                AND PJT.PROJECT_MANAGE_NO = BPL.PROJECT_MANAGE_NO(+)                                             ");
        sb.append(" 	                AND PJT.DEGREE            = BPL.DEGREE(+)                                             ");
        sb.append(" 	                AND (PJT.PROJECT_ID,PJT.DEGREE) IN(SELECT PROJECT_ID                                             ");
        sb.append(" 	                                                         ,MAX(DEGREE)                                             ");
        sb.append(" 	                                                     FROM SPJTM                                             ");
        sb.append(" 	                                                    GROUP BY PROJECT_ID)                                             ");

        /* where  */

        if("Y".equals(pjtProgressChartDto.getInAmtPerFlag())){
            sb.append(" 	  	  AND (CASE WHEN NVL( PJT.CT_AMOUNT, 0) = 0 	THEN 0 				 ");
            sb.append(" 	  	  ELSE ROUND((REV.REVENUE_AMOUNT / NVL(PJT.CT_AMOUNT,1)) *100, 2)	 ");
            sb.append(" 	  	  END) <> 100	 					                                 ");
        }

        if(!StringUtils.isEmpty(pjtProgressChartDto.getProjectName())){
            sb.append("	AND UPPER(PJT.PROJECT_NAME) LIKE '%' || UPPER('"+ pjtProgressChartDto.getProjectName() +"') || '%' ");
        }

        if(!StringUtils.isEmpty(pjtProgressChartDto.getProjectGubun())){
            sb.append("	AND PJT.PROJECT_GUBUN = '"+ pjtProgressChartDto.getProjectGubun() +"' ");
        }

        if(!StringUtils.isEmpty(pjtProgressChartDto.getCountryCode())){
            sb.append("	AND UPPER(PJT.COUNTRY_CODE) = UPPER('"+ pjtProgressChartDto.getCountryCode() +"') ");
        }

        if(!StringUtils.isEmpty(pjtProgressChartDto.getCountryName())){
            sb.append("	AND UPPER(PJT.COUNTRY_NAME) LIKE '%' || UPPER('"+ pjtProgressChartDto.getCountryName() +"') || '%' ");
        }


        sb.append(" 	            ) A                                             ");
        sb.append(" 	           ,(SELECT *                                             ");
        sb.append(" 	               FROM (                                              ");/*프로젝트 공정률 계획실적*/
        sb.append(" 	                     SELECT ORG_ID             AS C_ORG_ID                                             ");
        sb.append(" 	                           ,PROJECT_MANAGE_NO  AS C_PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                           ,DEGREE             AS C_DEGREE                                             ");
        sb.append(" 	                           ,YYYYMM                                             ");
        sb.append(" 	                           ,MAX(CASE WHEN ITEM_CODE = 'A' THEN TO_NUMBER(OBJECTIVE_MON) END) AS CT_PLAN                                             ");
        sb.append(" 	                           ,MAX(CASE WHEN ITEM_CODE = 'B' THEN TO_NUMBER(OBJECTIVE_MON) END) AS CT_ACTUAL                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS BUDGET_PLAN                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS BUDGET_ACTUAL                                             ");
        sb.append(" 	                       FROM SPJPPM                                             ");
        sb.append(" 	                      GROUP BY ORG_ID                                             ");
        sb.append(" 	                              ,PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                              ,DEGREE                                             ");
        sb.append(" 	                              ,YYYYMM                                             ");
        sb.append(" 	                      UNION ALL                                             ");
        /*프로젝트 실행예산 계획*/
        sb.append(" 	                     SELECT B.ORG_ID                                             ");
        sb.append(" 	                           ,B.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                           ,B.DEGREE                                             ");
        sb.append(" 	                           ,B.YYYYMM                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS CT_PLAN                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS CT_ACTUAL                                             ");
        sb.append(" 	                           ,ROUND(SUM(SUM(NVL(TO_NUMBER(B.AMT),0))) OVER(PARTITION BY B.ORG_ID, B.PROJECT_MANAGE_NO, B.DEGREE                                             ");
        sb.append(" 	                                                                             ORDER BY B.ORG_ID, B.PROJECT_MANAGE_NO, B.DEGREE, B.YYYYMM)                                              ");
        /*실행예산계획 월별누계*/
        sb.append(" 	                                 /(SELECT CASE WHEN SUM(NVL(TO_NUMBER(T.AMT),0)) = 0 THEN 1                                              ");
        sb.append(" 	                                               ELSE SUM(NVL(TO_NUMBER(T.AMT),0))                                             ");
        sb.append(" 	                                          END                                            ");/*실행예산계획 총계*/
        sb.append(" 	                                     FROM SPJPBM T                                             ");
        sb.append(" 	                                    WHERE T.ORG_ID = B.ORG_ID                                             ");
        sb.append(" 	                                      AND T.PROJECT_MANAGE_NO = B.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                                      AND T.DEGREE = B.DEGREE) * 100 ,1)                     AS BUDGET_PLAN                                              ");/*실행예산계획률*/
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS BUDGET_ACTUAL                                             ");
        sb.append(" 	                       FROM SPJPBM B                                                 ");
        sb.append(" 	                      GROUP BY B.ORG_ID                                                             ");
        sb.append(" 	                              ,B.PROJECT_MANAGE_NO                                                   ");
        sb.append(" 	                              ,B.DEGREE                                               ");
        sb.append(" 	                              ,B.YYYYMM                                             ");
        sb.append(" 	                      UNION ALL                                             ");
        /*프로젝트 실행예산 실적*/
        sb.append(" 	                     SELECT PJ.ORG_ID                                             ");
        sb.append(" 	                           ,PJ.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                           ,PJ.DEGREE                                             ");
        sb.append(" 	                           ,IEA.PERIOD_NAME AS YYYYMM                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS CT_PLAN                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS CT_ACTUAL                                             ");
        sb.append(" 	                           ,TO_NUMBER(NULL)                                                  AS BUDGET_PLAN                                             ");
        sb.append(" 	                           ,ROUND(SUM(SUM(IEA.AMOUNT)) OVER(PARTITION BY PJ.ORG_ID, PJ.PROJECT_MANAGE_NO, PJ.DEGREE                                             ");
        sb.append(" 	                                                                ORDER BY PJ.ORG_ID, PJ.PROJECT_MANAGE_NO, PJ.DEGREE, IEA.PERIOD_NAME)            "); /*실행예산실적 월별누계*/
        sb.append(" 	                                 /(SELECT CASE WHEN SUM(NVL(TO_NUMBER(T.AMT),0)) = 0 THEN 1                                              ");
        sb.append(" 	                                               ELSE SUM(NVL(TO_NUMBER(T.AMT),0))                                             ");
        sb.append(" 	                                          END                                              ");/*실행예산계획 총계*/
        sb.append(" 	                                     FROM SPJPBM T                                             ");
        sb.append(" 	                                    WHERE T.ORG_ID = PJ.ORG_ID                                             ");
        sb.append(" 	                                      AND T.PROJECT_MANAGE_NO = PJ.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                                      AND T.DEGREE = PJ.DEGREE) * 100 ,1)                     AS BUDGET_ACTUAL                                              ");/*실행예산계획률*/
        sb.append(" 	                       FROM CBOSLIP.CBO_IMS_EXPENSE_ACT_T IEA                                             ");
        sb.append(" 	                           ,(SELECT ORG_ID                                             ");
        sb.append(" 	                                   ,PROJECT_CODE      AS PROJECT_NUMBER                                             ");
        sb.append(" 	                                   ,PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                                   ,DEGREE                                             ");
        sb.append(" 	                               FROM SPJTM                                             ");
        sb.append(" 	                              WHERE (PROJECT_ID,DEGREE) IN(SELECT PROJECT_ID                                             ");
        sb.append(" 	                                                                 ,MAX(DEGREE)                                             ");
        sb.append(" 	                                                             FROM SPJTM                                             ");
        sb.append(" 	                                                            GROUP BY PROJECT_ID)) PJ                                             ");
        sb.append(" 	                      WHERE IEA.ORG_ID         = PJ.ORG_ID                                             ");
        sb.append(" 	                        AND IEA.PROJECT_NUMBER = PJ.PROJECT_NUMBER                                             ");
        sb.append(" 	                        AND IEA.SEGMENT5       = '3CT'                                             ");
        sb.append(" 	                      GROUP BY PJ.ORG_ID                                             ");
        sb.append(" 	                              ,PJ.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	                              ,PJ.DEGREE                                             ");
        sb.append(" 	                              ,IEA.PERIOD_NAME)                                             ");
        sb.append(" 	              PIVOT (SUM(CT_PLAN)       AS CT_PLAN                                              ");
        sb.append(" 	                    ,SUM(CT_ACTUAL)     AS CT_ACTUAL                                              ");
        sb.append(" 	                    ,SUM(BUDGET_PLAN)   AS BUDGET_PLAN                                              ");
        sb.append(" 	                    ,SUM(BUDGET_ACTUAL) AS BUDGET_ACTUAL                                              ");
        sb.append(" 	                FOR YYYYMM IN("+ MonthList +"))                                             ");
        sb.append(" 	            )      CTP                                              ");/*프로젝트 공정률 계획실적, 실행예산 계획실적*/

        sb.append(" 	                   ,(SELECT MAX(SPHD.VERSION) VERSION                                     ");
        sb.append(" 	                   ,SPHD.PROJECT_ID                                     ");
        sb.append(" 	                   ,SPHD.ORG_ID                                     ");
        sb.append(" 	                   ,SPHD.ADD_USER_ID                                     ");
        sb.append(" 	                FROM CBOSFA.SFA_PJTHD SPHD                                        ");
        sb.append(" 	                WHERE 1=1                                        ");
        sb.append(" 	                GROUP BY SPHD.PROJECT_ID                                        ");
        sb.append(" 	                ,SPHD.ORG_ID                                        ");
        sb.append(" 	                ,SPHD.ADD_USER_ID) M                                        ");

        sb.append(" 	           ,(SELECT 1 AS LINE                                             ");
        sb.append(" 	               FROM DUAL                                             ");
        sb.append(" 	              UNION ALL                                             ");
        sb.append(" 	             SELECT 2 AS LINE                                             ");
        sb.append(" 	               FROM DUAL                                             ");
        sb.append(" 	              UNION ALL                                             ");
        sb.append(" 	             SELECT 3 AS LINE                                             ");
        sb.append(" 	               FROM DUAL                                             ");
        sb.append(" 	              UNION ALL                                             ");
        sb.append(" 	             SELECT 4 AS LINE                                             ");
        sb.append(" 	               FROM DUAL) L                                             ");
        sb.append(" 	      WHERE A.ORG_ID            = CTP.C_ORG_ID(+)                                             ");
        sb.append(" 	        AND A.PROJECT_MANAGE_NO = CTP.C_PROJECT_MANAGE_NO(+)                                             ");
        sb.append(" 	        AND A.DEGREE            = CTP.C_DEGREE(+)                                             ");
        sb.append(" 	        AND A.PROJECT_ID        = M.PROJECT_ID                                             ");
        sb.append(" 	      ORDER BY CASE A.PROJECT_GUBUN WHEN '관납' THEN 1                                                                         ");
        sb.append(" 	                                    WHEN '민수' THEN 2                                                                      ");
        sb.append(" 	                                    WHEN '로컬' THEN 3                                                                      ");
        sb.append(" 	                                    WHEN '해외' THEN 4                                                                      ");
        sb.append(" 	                                    WHEN '지사' THEN 5                                                                      ");
        sb.append(" 	                END                                                              ");
        sb.append(" 	              ,A.PROJECT_MANAGE_NO                                             ");
        sb.append(" 	              ,A.DEGREE DESC                                             ");
        sb.append(" 	              ,L.LINE                                             ");



        Query query = entityManager.createNativeQuery(sb.toString())
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }
}

package com.iljin.apiServer.ijeas.costBudget;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.approval.*;
import com.iljin.apiServer.ijeas.slip.SlipHeader;
import com.iljin.apiServer.ijeas.slip.SlipHeaderKey;
import com.iljin.apiServer.ijeas.slip.SlipHeaderRepository;
import com.iljin.apiServer.ijeas.system.acct.AccountDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class CostBudgetRepositoryCustomImpl implements CostBudgetRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final CostBudgetRepository costBudgetRepository;

    private final SlipHeaderRepository slipHeaderRepository;

    private final BudgetHdRepository budgetHdRepository;

    private final BudgetDtRepository budgetDtRepository;

    private final ApprovalHeaderRepository approvalHeaderRepository;

    private final ApprovalDetailRepository approvalDetailRepository;

    private final Util util;


    @Override
    public List<CostBudgetDto> getBudgetAmountListCnt(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String search_degree = costBudgetDto.getSearchDegree();
        String COMP_CD = compCd;
        String ledger_id = ledgerId;

        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 0;
        int s_period_month	= 0;
        s_period_month = Integer.parseInt(period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m2[] = new String[select_cnt];
        String period_m3[] = new String[select_cnt];
        String period_m4[] = new String[select_cnt];
        String period_m5[] = new String[select_cnt];
        String period_m6[] = new String[select_cnt];

        StringBuilder sb = new StringBuilder();

        sb.append("         SELECT COUNT(*) AS CNT																						 \n");
        sb.append("         FROM (SELECT C.COMP_CD,                                                                                    \n");
        sb.append("                      C.LEDGER_ID,                                                                                       \n");
        sb.append("                      C.PERIOD_YEAR,                                                                                     \n");
        sb.append("                      C.CCTR_CD,                                                                                      \n");
        sb.append("                      C.ACCT_CD,                                                                                       \n");
        sb.append("                      C.ITEM_GROUP_CD,                                                                                  \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m6[i] = "0" + (pm_int);
            }else{
                period_m6[i]= (pm_int) + "";
            }
            sb.append("                    SUM(C.M"+period_m6[i]+"_PLAN_AMT) AS M"+period_m6[i]+"_PLAN_AMT,                       \n");
            sb.append("                    SUM(C.M"+period_m6[i]+"_EMERGENCY_PLAN_AMT) AS M"+period_m6[i]+"_EMERGENCY_PLAN_AMT,                       \n");
            sb.append("                    SUM(C.M"+period_m6[i]+"_BUDGET_AMT) AS M"+period_m6[i]+"_BUDGET_AMT,                       \n");
            sb.append("                    MAX(C.M"+period_m6[i]+"_REMARK) AS M"+period_m6[i]+"_REMARK,                       \n");
        }

        sb.append("                      C.PJT_CD                                                                                    \n");
        sb.append("                 FROM (SELECT SP.COMP_CD,                                                                           \n");
        sb.append("                              SP.LEDGER_ID,                                                                              \n");
        sb.append("                              SP.PERIOD_YEAR,                                                                            \n");
        sb.append("                              SP.CCTR_CD,                                                                             \n");
        sb.append("                              SP.ACCT_CD,                                                                              \n");
        sb.append("                              SP.ITEM_GROUP_CD,                                                                         \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ) {
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if ((pm_int) < 10) {
                period_m2[i] = "0" + (pm_int);
            } else {
                period_m2[i] = (pm_int) + "";
            }
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '" + period_m2[i] + "', SP.PLAN_AMT,0)) AS M" + period_m2[i] + "_PLAN_AMT,                       \n");
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '" + period_m2[i] + "', SP.EMERGENCY_PLAN_AMT,0)) AS M" + period_m2[i] + "_EMERGENCY_PLAN_AMT,   \n");
            sb.append("                    0 AS M" + period_m2[i] + "_BUDGET_AMT,   			\n");
            sb.append("                    TO_CHAR(NULL) AS M" + period_m2[i] + "_REMARK,   \n");
        }

        sb.append("                              SP.PJT_CD                                                                            \n");
        sb.append("                         FROM TB_PLAN_AMT SP                                                                           \n");
        sb.append("                        WHERE 1 = 1                                                                        			   \n");
        sb.append("                         AND SP.LEDGER_ID = :ledgerId                                                         \n");
        sb.append("                         AND SP.PERIOD_YEAR = :periodYear                                                       \n");
        sb.append("                         AND SP.CCTR_CD = :cctrCd                                                        \n");
        sb.append("                         AND (                                                                        \n");



        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m3[i] = "0" + (pm_int);
            }else{
                period_m3[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SP.PERIOD_MONTH = ?                                              \n");
            }else{
                sb.append("    OR SP.PERIOD_MONTH = ?                                           \n");
            }
        }

        sb.append("                        )                                                                                              \n");
        sb.append("                        GROUP BY SP.COMP_CD,                                                                        \n");
        sb.append("                                 SP.LEDGER_ID,                                                                           \n");
        sb.append("                                 SP.PERIOD_YEAR,                                                                         \n");
        sb.append("                                 SP.CCTR_CD,                                                                          \n");
        sb.append("                                 SP.ACCT_CD,                                                                           \n");
        sb.append("                                 SP.ITEM_GROUP_CD,                                                                      \n");
        sb.append("                                 SP.PJT_CD                                                                         \n");
        sb.append("                                                                                                                         \n");
        sb.append("                       UNION ALL                                                                                         \n");
        sb.append("                                                                                                                         \n");
        sb.append("                       SELECT SBD.COMP_CD,                                                                          \n");
        sb.append("                              SBD.LEDGER_ID,                                                                             \n");
        sb.append("                              SBD.PERIOD_YEAR,                                                                           \n");
        sb.append("                              SBD.CCTR_CD,                                                                            \n");
        sb.append("                              SBD.ACCT_CD,                                                                             \n");
        sb.append("                              SBD.ITEM_GROUP_CD,                                                                        \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m4[i] = "0" + (pm_int);
            }else{
                period_m4[i]= (pm_int) + "";
            }
            sb.append("                    0 AS M"+period_m4[i]+"_PLAN_AMT,                 \n");
            sb.append("                    0 AS M"+period_m4[i]+"_EMERGENCY_PLAN_AMT,                 \n");
            sb.append("                    SUM(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.BUDGET_AMT,0)) AS M"+period_m4[i]+"_BUDGET_AMT,                 \n");
            sb.append("                    MAX(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.REMARK,'')) AS M"+period_m4[i]+"_REMARK,                    \n");
        }

        sb.append("                              SBD.PJT_CD                                                                          \n");
        sb.append("                         FROM TB_COST_BUDGET SBD                                                                          \n");
        sb.append("                        WHERE 1 = 1                                                                                      \n");
        sb.append("                         AND SBD.LEDGER_ID = :ledgerId                                                         \n");
        sb.append("                         AND SBD.PERIOD_YEAR = :periodYear                                                       \n");
        sb.append("                         AND SBD.CCTR_CD = :cctrCd                                                        \n");
        sb.append("                          AND (                                                                        \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m5[i] = "0" + (pm_int);
            }else{
                period_m5[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SBD.PERIOD_MONTH = "+period_m5[i]+"                                              \n");
            }else{
                sb.append("    OR SBD.PERIOD_MONTH = "+period_m5[i]+"                                           \n");
            }
        }

        sb.append("                        )                                                                                              \n");
        sb.append("                          AND SBD.DEGREE = (SELECT MAX(SBD2.DEGREE)                                                      \n");
        sb.append("                                              FROM TB_COST_BUDGET SBD2                                                    \n");
        sb.append("                                             WHERE SBD2.LEDGER_ID = SBD.LEDGER_ID                                        \n");
        sb.append("                                               AND SBD2.PERIOD_YEAR = SBD.PERIOD_YEAR                                    \n");
        sb.append("                                               AND SBD2.PERIOD_MONTH = SBD.PERIOD_MONTH                                  \n");
        sb.append("                                               AND SBD2.CCTR_CD = SBD.CCTR_CD)                                     \n");
        sb.append("                                                                                                                         \n");
        sb.append("                        GROUP BY SBD.COMP_CD,                                                                       \n");
        sb.append("                                 SBD.LEDGER_ID,                                                                          \n");
        sb.append("                                 SBD.PERIOD_YEAR,                                                                        \n");
        sb.append("                                 SBD.CCTR_CD,                                                                         \n");
        sb.append("                                 SBD.ACCT_CD,                                                                          \n");
        sb.append("                                 SBD.ITEM_GROUP_CD,                                                                     \n");
        sb.append("                                 SBD.PJT_CD) C                                                                     \n");
        sb.append("                                                                                                                         \n");
        sb.append("                GROUP BY C.COMP_CD,                                                                                 \n");
        sb.append("                         C.LEDGER_ID,                                                                                    \n");
        sb.append("                         C.PERIOD_YEAR,                                                                                  \n");
        sb.append("                         C.CCTR_CD,                                                                                   \n");
        sb.append("                         C.ACCT_CD,                                                                                    \n");
        sb.append("                         C.ITEM_GROUP_CD,                                                                               \n");
        sb.append("                         C.PJT_CD) D                                                                               \n");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("ledgerId", ledger_id);
        query.setParameter("periodYear", period_year);
        query.setParameter("deptCd", search_dept_code);

        return new JpaResultMapper().list(query, CostBudgetDto.class);
    }

    @Override
    public CostBudgetResult getBudgetAmountList(CostBudgetDto costBudgetDto) throws Exception {


        CostBudgetResult costBudgetResult = new CostBudgetResult();


        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String search_degree = costBudgetDto.getSearchDegree();
        String COMP_CD = compCd;
        String ledger_id = ledgerId;

        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 0;
        int s_period_month	= 0;
        s_period_month = Integer.parseInt(period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        StringBuilder sb = new StringBuilder();

        String period_m2[] = new String[select_cnt];
        String period_m3[] = new String[select_cnt];
        String period_m4[] = new String[select_cnt];
        String period_m5[] = new String[select_cnt];
        String period_m6[] = new String[select_cnt];
        String period_m7[] = new String[select_cnt];
        String period_m8[] = new String[select_cnt];

        sb.append(" SELECT "+
                " E.COMP_CD    " +
                " , E.LEDGER_ID     " +
                " , E.PERIOD_YEAR   " +
                " , TO_CHAR("+period_month+") AS PERIOD_MONTH "+
                " , E.CCTR_DIV_CD  " +
                " , E.CCTR_DIV_NM  " +
                " , E.CCTR_CD  " +
                " , E.CCTR_NM  " +
                " , E.ACCT_DIV_CD  " +
                " , E.ACCT_DIV_NM  " +
                " , E.ACCT_CD  " +
                " , E.ACCT_NM  " +
                " , E.ITEM_GROUP_CD    " +
                " , E.ITEM_GROUP_NM    " +
                " , E.PJT_CD    " +
                " , E.PJT_NM    " +
                " , E.ATTRIBUTE1 AS USER_YN     ");
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m8[i] = "0" + (pm_int);
            }else{
                period_m8[i]= (pm_int) + "";
            }
            sb.append("                 , E.M"+period_m8[i]+"_PLAN_AMT   "+
                    "                 , E.M"+period_m8[i]+"_EMERGENCY_PLAN_AMT	"+
                    "                 , E.M"+period_m8[i]+"_BUDGET_AMT  "+
                    "                 , E.M"+period_m8[i]+"_REMARK    "
            );
        }
        sb.append("          FROM (                                                                                                                       " +
                "       SELECT D.COMP_CD,                                                                                            " +
                "              D.LEDGER_ID,                                                                                               " +
                "              D.PERIOD_YEAR,                                                                                             " +
                "              APPS.CBO_SP_BD_VS_ACT_PKG.GET_BDEPT_DIV_F(D.LEDGER_ID, D.CCTR_CD, 'CODE') AS CCTR_DIV_CD,            " +
                "              APPS.CBO_SP_BD_VS_ACT_PKG.GET_BDEPT_DIV_F(D.LEDGER_ID, D.CCTR_CD, 'NAME') AS CCTR_DIV_NM,            " +
                "              D.CCTR_CD,                                                                                              " +
                "              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.CCTR_CD, 2) AS CCTR_NM,                       " +
                "              APPS.CBO_GL_COMMON_PKG.GET_VARIABLE_FIXED_TYPE(D.ACCT_CD) AS ACCT_DIV_CD,                              " +
                "              APPS.CBO_SP_BD_VS_ACT_PKG.GET_ACCT_DIV_NAME_F(D.ACCT_CD) AS ACCT_DIV_NM,                               " +
                "              D.ACCT_CD,                                                                                               " +
                "              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.ACCT_CD, 4) AS ACCT_NM,                         " +
                "              D.ITEM_GROUP_CD,                                                                                          " +
                "              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.ITEM_GROUP_CD, 5) AS ITEM_GROUP_NM,               "
        );
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m7[i] = "0" + (pm_int);
            }else{
                period_m7[i]= (pm_int) + "";
            }
            sb.append("                    NVL(D.M"+period_m7[i]+"_PLAN_AMT,0) AS M"+period_m7[i]+"_PLAN_AMT,						");
            sb.append("                    NVL(D.M"+period_m7[i]+"_EMERGENCY_PLAN_AMT,0) AS M"+period_m7[i]+"_EMERGENCY_PLAN_AMT,	");
            sb.append("                    NVL(D.M"+period_m7[i]+"_BUDGET_AMT,0) AS M"+period_m7[i]+"_BUDGET_AMT,					");
            sb.append("                    NVL(D.M"+period_m7[i]+"_REMARK,'') AS M"+period_m7[i]+"_REMARK,																");
        }
        sb.append("              D.PJT_CD,                                                                                            ");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.PJT_CD, 6) AS PJT_NM,                   ");
        sb.append("              D.ATTRIBUTE1                                                                                               ");
        sb.append("         FROM (SELECT C.COMP_CD,                                                                                    ");
        sb.append("                      C.LEDGER_ID,                                                                                       ");
        sb.append("                      C.PERIOD_YEAR,                                                                                     ");
        sb.append("                      C.CCTR_CD,                                                                                      ");
        sb.append("                      C.ACCT_CD,                                                                                       ");
        sb.append("                      C.ITEM_GROUP_CD,                                                                                  ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m6[i] = "0" + (pm_int);
            }else{
                period_m6[i]= (pm_int) + "";
            }
            sb.append("                    SUM(C.M"+period_m6[i]+"_PLAN_AMT) AS M"+period_m6[i]+"_PLAN_AMT,						    ");
            sb.append("                    SUM(C.M"+period_m6[i]+"_EMERGENCY_PLAN_AMT) AS M"+period_m6[i]+"_EMERGENCY_PLAN_AMT,	    ");
            sb.append("                    SUM(C.M"+period_m6[i]+"_BUDGET_AMT) AS M"+period_m6[i]+"_BUDGET_AMT,					    ");
            sb.append("                    MAX(C.M"+period_m6[i]+"_REMARK) AS M"+period_m6[i]+"_REMARK,						    ");
        }
        sb.append("                      C.PJT_CD,                                                                                    ");
        sb.append("                      C.ATTRIBUTE1                                                                                       ");
        sb.append("                 FROM (SELECT SP.COMP_CD,                                                                           ");
        sb.append("                              SP.LEDGER_ID,                                                                              ");
        sb.append("                              SP.PERIOD_YEAR,                                                                            ");
        sb.append("                              SP.CCTR_CD,                                                                             ");
        sb.append("                              SP.ACCT_CD,                                                                              ");
        sb.append("                              SP.ITEM_GROUP_CD,                                                                         ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m2[i] = "0" + (pm_int);
            }else{
                period_m2[i]= (pm_int) + "";
            }
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.PLAN_AMT,0)) AS M"+period_m2[i]+"_PLAN_AMT,                       ");
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.EMERGENCY_PLAN_AMT,0)) AS M"+period_m2[i]+"_EMERGENCY_PLAN_AMT,   ");
            sb.append("                    0 AS M"+period_m2[i]+"_BUDGET_AMT,   			");
            sb.append("                    TO_CHAR(NULL) AS M"+period_m2[i]+"_REMARK,   ");
        }
        sb.append("                              SP.PJT_CD,                                                                            ");
        sb.append("                              'Y' AS ATTRIBUTE1                                                                            ");
        sb.append("                         FROM TB_PLAN_AMT SP                                                                           ");
        sb.append("                        WHERE 1 = 1                                                                        			   ");
        sb.append("    AND SP.LEDGER_ID = :ledgerId                                                         ");
        sb.append("    AND SP.PERIOD_YEAR = :periodYear                                                       ");
        sb.append("    AND SP.CCTR_CD = :deptCd                                                        ");
        sb.append("                    AND (                                                                        ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m3[i] = "0" + (pm_int);
            }else{
                period_m3[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SP.PERIOD_MONTH = "+period_m3[i]+"                                              ");
            }else{
                sb.append("    OR SP.PERIOD_MONTH = "+period_m3[i]+"                                           ");
            }
        }
        sb.append("                        )                                                                                                ");
        sb.append("                        GROUP BY SP.COMP_CD,                                                                        ");
        sb.append("                                 SP.LEDGER_ID,                                                                           ");
        sb.append("                                 SP.PERIOD_YEAR,                                                                         ");
        sb.append("                                 SP.CCTR_CD,                                                                          ");
        sb.append("                                 SP.ACCT_CD,                                                                           ");
        sb.append("                                 SP.ITEM_GROUP_CD,                                                                      ");
        sb.append("                                 SP.PJT_CD                                                                         ");
        sb.append("                                                                                                                         ");
        sb.append("                       UNION ALL                                                                                         ");
        sb.append("                                                                                                                         ");
        sb.append("                       SELECT SBD.COMP_CD,                                                                          ");
        sb.append("                              SBD.LEDGER_ID,                                                                             ");
        sb.append("                              SBD.PERIOD_YEAR,                                                                           ");
        sb.append("                              SBD.CCTR_CD,                                                                            ");
        sb.append("                              SBD.ACCT_CD,                                                                             ");
        sb.append("                              SBD.ITEM_GROUP_CD,                                                                        ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m4[i] = "0" + (pm_int);
            }else{
                period_m4[i]= (pm_int) + "";
            }
            sb.append("                    0 AS M"+period_m4[i]+"_PLAN_AMT,                 ");
            sb.append("                    0 AS M"+period_m4[i]+"_EMERGENCY_PLAN_AMT,                 ");
            sb.append("                    SUM(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.BUDGET_AMT,0)) AS M"+period_m4[i]+"_BUDGET_AMT,                 ");
            sb.append("                    MAX(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.REMARK,'')) AS M"+period_m4[i]+"_REMARK,                    ");
        }
        sb.append("                              SBD.PJT_CD,                                                                          ");
        sb.append("                              SBD.ATTRIBUTE1                                                                             ");
        sb.append("                         FROM TB_COST_BUDGET SBD                                                                          ");
        sb.append("                        WHERE 1 = 1                                                                                      ");
        sb.append("    AND SBD.LEDGER_ID = :ledgerId                                                                        ");
        sb.append("    AND SBD.PERIOD_YEAR = :periodYear                                                                      ");
        sb.append("    AND SBD.CCTR_CD = :deptCd                                                                       ");
        sb.append("                    AND (                                                                        ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m5[i] = "0" + (pm_int);
            }else{
                period_m5[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SBD.PERIOD_MONTH = "+period_m5[i]+"                                              ");
            }else{
                sb.append("    OR SBD.PERIOD_MONTH = "+period_m5[i]+"                                           ");
            }
        }
        sb.append("                        )                                                                                                ");
        sb.append("                          AND SBD.DEGREE = (SELECT MAX(SBD2.DEGREE)                                                      ");
        sb.append("                                              FROM TB_COST_BUDGET SBD2                                                    ");
        sb.append("                                             WHERE SBD2.LEDGER_ID = SBD.LEDGER_ID                                        ");
        sb.append("                                               AND SBD2.PERIOD_YEAR = SBD.PERIOD_YEAR                                    ");
        sb.append("                                               AND SBD2.PERIOD_MONTH = SBD.PERIOD_MONTH                                  ");
        sb.append("                                               AND SBD2.CCTR_CD = SBD.CCTR_CD)                                     ");
        sb.append("                                                                                                                         ");
        sb.append("                        GROUP BY SBD.COMP_CD,                                                                       ");
        sb.append("                                 SBD.LEDGER_ID,                                                                          ");
        sb.append("                                 SBD.PERIOD_YEAR,                                                                        ");
        sb.append("                                 SBD.CCTR_CD,                                                                         ");
        sb.append("                                 SBD.ACCT_CD,                                                                          ");
        sb.append("                                 SBD.ITEM_GROUP_CD,                                                                     ");
        sb.append("                                 SBD.PJT_CD,                                                                       ");
        sb.append("                                 SBD.ATTRIBUTE1) C                                                                       ");
        sb.append("                                                                                                                         ");
        sb.append("                GROUP BY C.COMP_CD,                                                                                 ");
        sb.append("                         C.LEDGER_ID,                                                                                    ");
        sb.append("                         C.PERIOD_YEAR,                                                                                  ");
        sb.append("                         C.CCTR_CD,                                                                                   ");
        sb.append("                         C.ACCT_CD,                                                                                    ");
        sb.append("                         C.ITEM_GROUP_CD,                                                                               ");
        sb.append("                         C.PJT_CD,                                                                                 ");
        sb.append("                         C.ATTRIBUTE1) D                                                                                 ");
        sb.append("       UNION ALL                                                                                         ");
        sb.append("       SELECT NULL,                                                                                      ");
        sb.append("              NULL,                                                                                      ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              'Total',                                                                                   ");
        sb.append("              '합계',                                                                                     ");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m7[i] = "0" + (pm_int);
            }else{
                period_m7[i]= (pm_int) + "";
            }
            sb.append("                    NVL(D.M"+period_m7[i]+"_PLAN_AMT,0) AS M"+period_m7[i]+"_PLAN_AMT,						");
            sb.append("                    NVL(D.M"+period_m7[i]+"_EMERGENCY_PLAN_AMT,0) AS M"+period_m7[i]+"_EMERGENCY_PLAN_AMT,	");
            sb.append("                    NVL(D.M"+period_m7[i]+"_BUDGET_AMT,0) AS M"+period_m7[i]+"_BUDGET_AMT,					");
            sb.append("                    '',																							");
        }

        sb.append("              '',                                                                                        ");
        sb.append("              '',                                                                                        ");
        sb.append("              ''                                                                                         ");
        sb.append("         FROM (SELECT NULL,                                                                              ");
        sb.append("                      NULL,                                                                              ");
        sb.append("                      '',                                                                                ");
        sb.append("                      '',                                                                                ");
        sb.append("                      '',                                                                                ");
        sb.append("                      '',                                                                                ");


        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m6[i] = "0" + (pm_int);
            }else{
                period_m6[i]= (pm_int) + "";
            }
            sb.append("                    SUM(C.M"+period_m6[i]+"_PLAN_AMT) AS M"+period_m6[i]+"_PLAN_AMT,						");
            sb.append("                    SUM(C.M"+period_m6[i]+"_EMERGENCY_PLAN_AMT) AS M"+period_m6[i]+"_EMERGENCY_PLAN_AMT,	");
            sb.append("                    SUM(C.M"+period_m6[i]+"_BUDGET_AMT) AS M"+period_m6[i]+"_BUDGET_AMT,					");
            sb.append("                    MAX(C.M"+period_m6[i]+"_REMARK) AS M"+period_m6[i]+"_REMARK,						");
        }


        sb.append("                      '',                                                            ");
        sb.append("                      ''                                                             ");
        sb.append("                 FROM (SELECT NULL,                                                  ");
        sb.append("                              NULL,                                                  ");
        sb.append("                              '',                                                    ");
        sb.append("                              '',                                                    ");
        sb.append("                              '',                                                    ");
        sb.append("                              '',                                                    ");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m2[i] = "0" + (pm_int);
            }else{
                period_m2[i]= (pm_int) + "";
            }
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.PLAN_AMT,0)) AS M"+period_m2[i]+"_PLAN_AMT,                       ");
            sb.append("                    SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.EMERGENCY_PLAN_AMT,0)) AS M"+period_m2[i]+"_EMERGENCY_PLAN_AMT,   ");
            sb.append("                    0 AS M"+period_m2[i]+"_BUDGET_AMT,   			    ");
            sb.append("                    TO_CHAR(NULL) AS M"+period_m2[i]+"_REMARK,      ");
        }

        sb.append("                              '',                                                                 ");
        sb.append("                              ''                                                                 ");
        sb.append("                         FROM TB_PLAN_AMT SP                                                   ");
        sb.append("                        WHERE 1 = 1                                                              ");
        sb.append("                         AND SP.LEDGER_ID = :ledgerId                                                         ");
        sb.append("                         AND SP.PERIOD_YEAR = :periodYear                                                       ");
        sb.append("                         AND SP.CCTR_CD = :deptCd                                                        ");
        sb.append("                        GROUP BY SP.COMP_CD,                                                ");
        sb.append("                                 SP.LEDGER_ID,                                                   ");
        sb.append("                                 SP.PERIOD_YEAR,                                                 ");
        sb.append("                                 SP.CCTR_CD                                                   ");
        sb.append("                                                                                                 ");
        sb.append("                       UNION ALL                                                                 ");
        sb.append("                                                                                                 ");
        sb.append("                       SELECT NULL,                                                       	    ");
        sb.append("                              NULL,                                                              ");
        sb.append("                              '',                                                                ");
        sb.append("                              '',                                                                ");
        sb.append("                              '',                                                                ");
        sb.append("                              '',                                                                ");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m4[i] = "0" + (pm_int);
            }else{
                period_m4[i]= (pm_int) + "";
            }
            sb.append("                    0 AS M"+period_m4[i]+"_PLAN_AMT,                 ");
            sb.append("                    0 AS M"+period_m4[i]+"_EMERGENCY_PLAN_AMT,       ");
            sb.append("                    SUM(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.BUDGET_AMT,0)) AS M"+period_m4[i]+"_BUDGET_AMT,                 ");
            sb.append("                    MAX(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.REMARK,'')) AS M"+period_m4[i]+"_REMARK,                    ");

        }


        sb.append("                              '',                                                                         ");
        sb.append("                              ''                                                                          ");
        sb.append("                         FROM TB_COST_BUDGET SBD                                                           ");
        sb.append("                        WHERE 1 = 1                                                                       ");
        sb.append("                         AND SBD.LEDGER_ID = :ledgerId                                                         ");
        sb.append("                         AND SBD.PERIOD_YEAR = :periodYear                                                       ");
        sb.append("                         AND SBD.CCTR_CD = :deptCd                                                        ");
        sb.append("                          AND SBD.DEGREE = (SELECT MAX(SBD2.DEGREE)                                                      ");
        sb.append("                                              FROM TB_COST_BUDGET SBD2                                                    ");
        sb.append("                                             WHERE SBD2.LEDGER_ID = SBD.LEDGER_ID                                        ");
        sb.append("                                               AND SBD2.PERIOD_YEAR = SBD.PERIOD_YEAR                                    ");
        sb.append("                                               AND SBD2.PERIOD_MONTH = SBD.PERIOD_MONTH                                  ");
        sb.append("                                               AND SBD2.CCTR_CD = SBD.CCTR_CD)                                     ");
        sb.append("                                                                                                                         ");
        sb.append("                        GROUP BY SBD.COMP_CD,                                                                       ");
        sb.append("                                 SBD.LEDGER_ID,                                                                          ");
        sb.append("                                 SBD.PERIOD_YEAR,                                                                        ");
        sb.append("                                 SBD.CCTR_CD) C ) D ) E                                                               ");
        sb.append("                ORDER BY E.ACCT_CD, E.ITEM_GROUP_CD, E.PJT_CD                                              	    ");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("ledgerId", ledger_id);
        query.setParameter("periodYear", period_year);
        query.setParameter("deptCd", search_dept_code);


        costBudgetResult.setList(new JpaResultMapper().list(query, CostBudgetDto.class));

        StringBuilder sb2 = new StringBuilder();

        // 전체 예산승인 flag 불러오기
        sb2.append("              SELECT SB.LEDGER_ID                                               \n");
        sb2.append("                   , SB.PERIOD_YEAR                                             \n");
        sb2.append("                   , SB.CCTR_CD                                              \n");
        sb2.append("                   , SB.CCTR_NM                                              \n");
        String b_month = "";
        for(int i = 1; i <= 12; i++) {
            if((i) < 10){
                b_month = "0" + (i);
                sb2.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+b_month+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+b_month+"_APPR_YN \n");
            }else{
                b_month = (i) + "";
                sb2.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+b_month+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+b_month+"_APPR_YN \n");
            }
        }
        sb2.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
        sb2.append("               WHERE 1 = 1                                                                                           \n");
        sb2.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
        sb2.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
        sb2.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
        sb2.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
        sb2.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
        sb2.append("                                  SELECT MAX(DEGREE)                                                                 \n");
        sb2.append("                                    FROM TB_PLAN_AMT                                                                       \n");
        sb2.append("                                   WHERE 1 = 1                                                                       \n");
        sb2.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
        sb2.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
        sb2.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
        sb2.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
        sb2.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
        sb2.append("                                 ), 1)                                                                               \n");
        sb2.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
        sb2.append("                      , SB.PERIOD_YEAR                                                                               \n");
        sb2.append("                      , SB.CCTR_CD                                                                            	   \n");
        sb2.append("                      , SB.CCTR_NM                                                                            	   \n");

        Query query2 = entityManager.createNativeQuery(sb2.toString());

        query2.setParameter("compCd", compCd);
        query2.setParameter("ledgerId", ledger_id);
        query2.setParameter("periodYear", period_year);
        query2.setParameter("deptCd", search_dept_code);

        costBudgetResult.setPlanConfirm(new JpaResultMapper().list(query2, CostBudgetDto.class));

        StringBuilder sb3 = new StringBuilder();

        // 전체 실적저장 flag 불러오기

        sb3.append("              SELECT SB.LEDGER_ID                                               \n");
        sb3.append("                   , 'budget' AS Flag                                             \n");
        sb3.append("                   , SB.PERIOD_YEAR                                             \n");
        sb3.append("                   , SB.CCTR_CD                                              \n");
        sb3.append("                   , SB.CCTR_NM                                              \n");
        String a_month = "";
        for(int i = 1; i <= 12; i++) {
            if((i) < 10){
                a_month = "0" + (i);
                sb3.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+a_month+",SB.ACTUAL_APPR_YN,'N')) AS ACTUAL"+a_month+"_APPR_YN \n");
            }else{
                a_month = (i) + "";
                sb3.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+a_month+",SB.ACTUAL_APPR_YN,'N')) AS ACTUAL"+a_month+"_APPR_YN \n");
            }
        }
        sb3.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
        sb3.append("               WHERE 1 = 1                                                                                           \n");
        sb3.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
        sb3.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
        sb3.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
        sb3.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
        sb3.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
        sb3.append("                                  SELECT MAX(DEGREE)                                                                 \n");
        sb3.append("                                    FROM TB_PLAN_AMT                                                                       \n");
        sb3.append("                                   WHERE 1 = 1                                                                       \n");
        sb3.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
        sb3.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
        sb3.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
        sb3.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
        sb3.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
        sb3.append("                                 ), 1)                                                                               \n");
        sb3.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
        sb3.append("                      , SB.PERIOD_YEAR                                                                               \n");
        sb3.append("                      , SB.CCTR_CD                                                                            	   \n");
        sb3.append("                      , SB.CCTR_NM                                                                                \n");

        Query query3 = entityManager.createNativeQuery(sb3.toString());

        query3.setParameter("compCd", compCd);
        query3.setParameter("ledgerId", ledger_id);
        query3.setParameter("periodYear", period_year);
        query3.setParameter("deptCd", search_dept_code);

        costBudgetResult.setSavePerformance(new JpaResultMapper().list(query3, CostBudgetDto.class));

        return costBudgetResult;
    }

    @Override
    public List<CostBudgetDto> getPlanTotal(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String COMP_CD = compCd;
        String ledger_id = ledgerId;

        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 12;

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m1[] = new String[select_cnt];
        String period_m2[] = new String[select_cnt];
        String period_m3[] = new String[select_cnt];

        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("       SELECT A.COMP_CD                                                               					\n");
        sb.append("            , A.LEDGER_ID	                                                              						\n");
        sb.append("            , A.PERIOD_YEAR	                                                              					\n");
        sb.append("            , A.CCTR_CD	                                                              					\n");
        sb.append("            , A.CCTR_NM	                                                              					\n");
        sb.append("            , A.ACCT_CD	                                                              						\n");
        sb.append("            , A.ACCT_NM	                                                              						\n");
        sb.append("            , A.PJT_CD	                                                              					\n");
        sb.append("            , A.PJT_NM	                                                              					\n");
        sb.append("            , A.ITEM_GROUP_CD	                                                              				\n");
        sb.append("            , A.ITEM_GROUP_NM	                                                              				\n");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++){
            // 한 자릿수, 두 자릿수 구분
            if((i+1) < 10){
                period_m3[i] = "0" + (i+1);
            }else{
                period_m3[i]= (i+1) + "";
            }
            sb.append("        , A.M"+period_m3[i]+"_PLAN_AMT																	\n");
        }
        sb.append("            , A.TOTAL_PLAN_AMT	                                                              				\n");
        sb.append("         FROM (                                                                  								\n");
        sb.append("       SELECT SP.COMP_CD,                                                               					\n");
        sb.append("              SP.LEDGER_ID,                                                                  					\n");
        sb.append("              SP.PERIOD_YEAR,                                                                					\n");
        sb.append("              SP.CCTR_CD,                                                                 					\n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(SP.LEDGER_ID, SP.CCTR_CD, 2) AS CCTR_NM,			\n");
        sb.append("              SP.ACCT_CD,                                                                  					\n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(SP.LEDGER_ID, SP.ACCT_CD, 4) AS ACCT_NM,			\n");
        sb.append("              SP.PJT_CD,                                                               					\n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(SP.LEDGER_ID, SP.PJT_CD, 6) AS PJT_NM,		\n");
        sb.append("              SP.ITEM_GROUP_CD,                                                             					\n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(SP.LEDGER_ID, SP.ITEM_GROUP_CD, 5) AS ITEM_GROUP_NM,	\n");
        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 한 자릿수, 두 자릿수 구분
            if((i+1) < 10){
                period_m1[i] = "0" + (i+1);
            }else{
                period_m1[i]= (i+1) + "";
            }
            sb.append("          SUM(DECODE(SP.PERIOD_MONTH, '"+period_m1[i]+"', SP.PLAN_AMT,0)) AS M"+period_m1[i]+"_PLAN_AMT,	\n");
        }
        sb.append("              SUM(DECODE(SP.PERIOD_YEAR,  "+period_year+", SP.PLAN_AMT,0)) AS TOTAL_PLAN_AMT    				\n");
        sb.append("         FROM TB_PLAN_AMT SP                                                               							\n");
        sb.append("        WHERE 1 = 1                                                                    		\n");
        sb.append("         AND SP.LEDGER_ID = :ledgerId														\n");
        sb.append("         AND SP.PERIOD_YEAR = :periodYear													\n");
        sb.append("         AND SP.CCTR_CD = :deptCd														\n");
        sb.append("        GROUP BY SP.COMP_CD,																\n");
        sb.append("                 SP.LEDGER_ID,                                                               	\n");
        sb.append("                 SP.PERIOD_YEAR,                                                             	\n");
        sb.append("                 SP.CCTR_CD,                                                              	\n");
        sb.append("                 SP.ACCT_CD,                                                               	\n");
        sb.append("                 SP.ITEM_GROUP_CD,                                                          	\n");
        sb.append("                 SP.PJT_CD                                                             	\n");
        sb.append("        UNION ALL                                                                            	\n");
        sb.append("       SELECT NULL,                                                                          	\n");
        sb.append("              NULL,                                                                          	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '',                                                                            	\n");
        sb.append("              '합계',                                                                          	\n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 한 자릿수, 두 자릿수 구분
            if((i+1) < 10){
                period_m2[i] = "0" + (i+1);
            }else{
                period_m2[i]= (i+1) + "";
            }
            sb.append("          SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.PLAN_AMT,0)) AS M"+period_m2[i]+"_PLAN_AMT,	\n");
        }

        sb.append("              SUM(DECODE(SP.PERIOD_YEAR,  "+period_year+", SP.PLAN_AMT,0)) AS TOTAL_PLAN_AMT    				\n");
        sb.append("         FROM TB_PLAN_AMT SP                                                              	\n");
        sb.append("        WHERE 1 = 1                                                                          	\n");
        sb.append("         AND SP.LEDGER_ID = :ledgerId														\n");
        sb.append("         AND SP.PERIOD_YEAR = :periodYear													\n");
        sb.append("         AND SP.CCTR_CD = :deptCd ) A													\n");
        sb.append("                ORDER BY A.ACCT_CD, A.ITEM_GROUP_CD, A.PJT_CD DESC                                              \n");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("ledgerId", ledger_id);
        query.setParameter("periodYear", period_year);
        query.setParameter("deptCd", search_dept_code);

        return new JpaResultMapper().list(query, CostBudgetDto.class);
    }

    @Override
    public List<CostBudgetDto> getSumDetail(CostBudgetDto costBudgetDto) throws Exception {
        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String search_degree = costBudgetDto.getSearchDegree();
        String COMP_CD = compCd;
        String ledger_id = ledgerId;



        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 0;
        int s_period_month	= 0;
        s_period_month = Integer.parseInt(period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m2[] = new String[select_cnt];
        String period_m3[] = new String[select_cnt];
        String period_m4[] = new String[select_cnt];
        String period_m5[] = new String[select_cnt];
        String period_m6[] = new String[select_cnt];
        String period_m7[] = new String[select_cnt];

        StringBuilder sb = new StringBuilder();

        // 상세데이터 불러오기
        sb.append("       SELECT D.COMP_CD,                                                                                            \n");
        sb.append("              D.LEDGER_ID,                                                                                               \n");
        sb.append("              D.PERIOD_YEAR,                                                                                             \n");
        sb.append("              APPS.CBO_SP_BD_VS_ACT_PKG.GET_BDEPT_DIV_F(D.LEDGER_ID, D.CCTR_CD, 'CODE') AS CCTR_DIV_CD,            \n");
        sb.append("              APPS.CBO_SP_BD_VS_ACT_PKG.GET_BDEPT_DIV_F(D.LEDGER_ID, D.CCTR_CD, 'NAME') AS CCTR_DIV_NM,            \n");
        sb.append("              D.CCTR_CD,                                                                                              \n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.CCTR_CD, 2) AS CCTR_NM,                       \n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_VARIABLE_FIXED_TYPE(D.ACCT_CD) AS ACCT_DIV_CD,                              \n");
        sb.append("              APPS.CBO_SP_BD_VS_ACT_PKG.GET_ACCT_DIV_NAME_F(D.ACCT_CD) AS ACCT_DIV_NM,                               \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m7[i] = "0" + (pm_int);
            }else{
                period_m7[i]= (pm_int) + "";
            }
            sb.append("          NVL(D.M"+period_m7[i]+"_PLAN_AMT_T,0) AS M"+period_m7[i]+"_PLAN_AMT_T,						\n");
            sb.append("          NVL(D.M"+period_m7[i]+"_EMERGENCY_PLAN_AMT_T,0) AS M"+period_m7[i]+"_EMERGENCY_PLAN_AMT_T,	\n");
            sb.append("          NVL(D.M"+period_m7[i]+"_BUDGET_AMT_T,0) AS M"+period_m7[i]+"_BUDGET_AMT_T,					\n");
        }

        sb.append("              D.ACCT_CD,                                                                                               \n");
        sb.append("              APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(D.LEDGER_ID, D.ACCT_CD, 4) AS ACCT_NM                          \n");
        sb.append("         FROM (SELECT C.COMP_CD,                             \n");
        sb.append("                      C.LEDGER_ID,                                \n");
        sb.append("                      C.PERIOD_YEAR,                              \n");
        sb.append("                      C.CCTR_CD,                               \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m6[i] = "0" + (pm_int);
            }else{
                period_m6[i]= (pm_int) + "";
            }
            sb.append("                    SUM(C.M"+period_m6[i]+"_PLAN_AMT) AS M"+period_m6[i]+"_PLAN_AMT_T,						\n");
            sb.append("                    SUM(C.M"+period_m6[i]+"_EMERGENCY_PLAN_AMT) AS M"+period_m6[i]+"_EMERGENCY_PLAN_AMT_T,	\n");
            sb.append("                    SUM(C.M"+period_m6[i]+"_BUDGET_AMT) AS M"+period_m6[i]+"_BUDGET_AMT_T,					\n");
        }

        sb.append("                      C.ACCT_CD																						\n");
        sb.append("                 FROM (SELECT SP.COMP_CD,																		\n");
        sb.append("                              SP.LEDGER_ID,																			\n");
        sb.append("                              SP.PERIOD_YEAR,																			\n");
        sb.append("                              SP.CCTR_CD,																			\n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m2[i] = "0" + (pm_int);
            }else{
                period_m2[i]= (pm_int) + "";
            }
            sb.append("      SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.PLAN_AMT,0)) AS M"+period_m2[i]+"_PLAN_AMT,						\n");
            sb.append("      SUM(DECODE(SP.PERIOD_MONTH, '"+period_m2[i]+"', SP.EMERGENCY_PLAN_AMT,0)) AS M"+period_m2[i]+"_EMERGENCY_PLAN_AMT,	\n");
            sb.append("      0 AS M"+period_m2[i]+"_BUDGET_AMT,   																					\n");
        }

        sb.append("                              SP.ACCT_CD								\n");
        sb.append("                         FROM TB_PLAN_AMT SP							\n");
        sb.append("                        WHERE 1 = 1										\n");
        sb.append("                          AND SP.LEDGER_ID = :ledgerId									\n");
        sb.append("                          AND SP.PERIOD_YEAR = :periodYear								\n");
        sb.append("                          AND SP.CCTR_CD = :deptCd									\n");
        sb.append("                    AND (													\n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m3[i] = "0" + (pm_int);
            }else{
                period_m3[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SP.PERIOD_MONTH = "+ period_m3[i] +"							\n");
            }else{
                sb.append("    OR SP.PERIOD_MONTH = "+ period_m3[i] +"						\n");
            }
        }

        sb.append("                        )                                             	\n");
        sb.append("                        GROUP BY SP.COMP_CD,                     	\n");
        sb.append("                                 SP.LEDGER_ID,                        	\n");
        sb.append("                                 SP.PERIOD_YEAR,                      	\n");
        sb.append("                                 SP.CCTR_CD,                       	\n");
        sb.append("                                 SP.ACCT_CD                         	\n");
        sb.append("                       UNION ALL                                      	\n");
        sb.append("                       SELECT SBD.COMP_CD,                       	\n");
        sb.append("                              SBD.LEDGER_ID,                          	\n");
        sb.append("                              SBD.PERIOD_YEAR,                        	\n");
        sb.append("                              SBD.CCTR_CD,                         	\n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m4[i] = "0" + (pm_int);
            }else{
                period_m4[i]= (pm_int) + "";
            }
            sb.append("                    0 AS M"+period_m4[i]+"_PLAN_AMT,                 															\n");
            sb.append("                    0 AS M"+period_m4[i]+"_EMERGENCY_PLAN_AMT,                 												\n");
            sb.append("                    SUM(DECODE(SBD.PERIOD_MONTH, '"+period_m4[i]+"', SBD.BUDGET_AMT,0)) AS M"+period_m4[i]+"_BUDGET_AMT,	\n");
        }

        sb.append("                              SBD.ACCT_CD									\n");
        sb.append("                         FROM TB_COST_BUDGET SBD                               \n");
        sb.append("                        WHERE 1 = 1                                           \n");
        sb.append("                          AND SBD.LEDGER_ID = :ledgerId									\n");
        sb.append("                          AND SBD.PERIOD_YEAR = :periodYear								\n");
        sb.append("                          AND SBD.CCTR_CD = :deptCd									\n");
        sb.append("                    AND (                                                     \n");

        // 조회할 칼럼의 기간에 해당되는 데이터만 조회
        for(int i = 0 ; i < select_cnt ; i++ ){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + i;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_m5[i] = "0" + (pm_int);
            }else{
                period_m5[i]= (pm_int) + "";
            }
            if(i == 0){
                sb.append("    SBD.PERIOD_MONTH = " + period_m5[i] + "								\n");
            }else{
                sb.append("    OR SBD.PERIOD_MONTH = " + period_m5[i] + "                           \n");
            }
        }

        sb.append("                        )                                                                 \n");
        sb.append("                          AND SBD.DEGREE = (SELECT MAX(SBD2.DEGREE)						\n");
        sb.append("                                              FROM TB_COST_BUDGET SBD2                     \n");
        sb.append("                                             WHERE SBD2.LEDGER_ID = SBD.LEDGER_ID         \n");
        sb.append("                                               AND SBD2.PERIOD_YEAR = SBD.PERIOD_YEAR     \n");
        sb.append("                                               AND SBD2.PERIOD_MONTH = SBD.PERIOD_MONTH   \n");
        sb.append("                                               AND SBD2.CCTR_CD = SBD.CCTR_CD)      \n");
        sb.append("                        GROUP BY SBD.COMP_CD,                                        \n");
        sb.append("                                 SBD.LEDGER_ID,                                           \n");
        sb.append("                                 SBD.PERIOD_YEAR,                                         \n");
        sb.append("                                 SBD.CCTR_CD,                                          \n");
        sb.append("                                 SBD.ACCT_CD) C                                         \n");
        sb.append("                GROUP BY C.COMP_CD,                                                  \n");
        sb.append("                         C.LEDGER_ID,                                                     \n");
        sb.append("                         C.PERIOD_YEAR,                                                   \n");
        sb.append("                         C.CCTR_CD,                                                    \n");
        sb.append("                         C.ACCT_CD) D                                                   \n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("ledgerId", ledger_id);
        query.setParameter("periodYear", period_year);
        query.setParameter("deptCd", search_dept_code);

        return new JpaResultMapper().list(query, CostBudgetDto.class);
    }

    @Override
    public List<CostBudgetDto> getPerformanceCheck(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String pre_period_month = costBudgetDto.getPrePeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String ledger_id = ledgerId;

        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        StringBuilder sb = new StringBuilder();


        sb.append(" 	    SELECT T.LEDGER_ID                                                                       \n");
        sb.append("           , T.PERIOD_YEAR                                                                     \n");
        sb.append("           , T.PERIOD_MONTH                                                                    \n");
        sb.append("           , T.PRE_PERIOD_YEAR                                                                 \n");
        sb.append("           , T.PRE_PERIOD_MONTH                                                                \n");
        sb.append("           , T.CCTR_CD                                                                      \n");
        sb.append("           , T.CCTR_NM                                                                      \n");
        sb.append("           , T.ACCT_DIV_NM                                                                   \n");
        sb.append("           , T.ACCT_CD                                                                       \n");
        sb.append("           , T.ACCT_NM                                                                       \n");
        sb.append("           , T.ITEM_GROUP_CD                                                                  \n");
        sb.append("           , T.ITEM_GROUP_NM                                                                  \n");
        sb.append("           , T.PJT_CD                                                                    \n");
        sb.append("           , T.PJT_NM                                                                    \n");
        sb.append("           , T.PREV_PLAN_AMT                                                                \n");
        sb.append("           , T.PREV_EMERGENCY_PLAN_AMT                                                      \n");
        sb.append("           , T.PREV_BD_AMT                                                                  \n");
        sb.append("           , T.PTD_ACTUAL_AMT                                                               \n");
        sb.append("           , T.PREV_PTD_GAP_AMT                                                             \n");
        sb.append("           , T.PREV_YTD_PLAN_AMT                                                            \n");
        sb.append("           , T.PREV_YTD_EMERGENCY_PLAN_AMT                                                  \n");
        sb.append("           , T.PREV_YTD_BD_AMT                                                              \n");
        sb.append("           , T.YTD_ACTUAL_AMT                                                               \n");
        sb.append("           , T.PREV_YTD_GAP_AMT                                                             \n");
        sb.append("           , T.PLAN_AMT                                                                     \n");
        sb.append("           , T.EMERGENCY_PLAN_AMT                                                           \n");
        sb.append("           , T.BD_AMT                                                                       \n");
        sb.append("           , T.NEXT_PLAN_AMT                                                                \n");
        sb.append("           , T.NEXT_EMERGENCY_PLAN_AMT                                                      \n");
        sb.append("           , T.NEXT_BD_AMT                                                                  \n");
        sb.append("           , T.NEXT2_PLAN_AMT                                                               \n");
        sb.append("           , T.NEXT2_EMERGENCY_PLAN_AMT                                                     \n");
        sb.append("           , T.NEXT2_BD_AMT                                                                 \n");
        sb.append("        FROM (                                                                                 \n");
        sb.append("              SELECT C.LEDGER_ID                                                               \n");
        sb.append("                   , C.PERIOD_YEAR                                                             \n");
        sb.append("                   , '"+period_month+"' AS PERIOD_MONTH                                        \n");
        sb.append("                   , C.PRE_PERIOD_YEAR                                                      \n");
        sb.append("                   , C.PRE_PERIOD_MONTH                                                      \n");
        sb.append("                   , C.CCTR_CD                                                              \n");
        sb.append("                   , C.CCTR_NM                                                              \n");
        sb.append("                   , C.ACCT_DIV_NM                                                           \n");
        sb.append("                   , C.ACCT_CD                                                               \n");
        sb.append("                   , C.ACCT_NM                                                               \n");
        sb.append("                   , C.PJT_CD                                                            \n");
        sb.append("                   , C.PJT_NM                                                            \n");
        sb.append("                   , C.ITEM_GROUP_CD                                                          \n");
        sb.append("                   , C.ITEM_GROUP_NM                                                          \n");
        sb.append("                   , NVL(C.PREV_PLAN_AMT,0) AS PREV_PLAN_AMT                             \n");//(M-1)월 사업계획
        sb.append("                   , NVL(C.PREV_EMERGENCY_PLAN_AMT,0) AS PREV_EMERGENCY_PLAN_AMT         \n");//(M-1)월 비상계획
        sb.append("                   , NVL(C.PREV_BD_AMT,0) AS PREV_BD_AMT                                 \n");//(M-1)월 비용예산
        sb.append("                   , NVL(C.PTD_ACTUAL_AMT,0) AS PTD_ACTUAL_AMT                           \n");//(M-1)월 실적
        sb.append("                   , NVL(C.PREV_PTD_GAP_AMT,0) AS PREV_PTD_GAP_AMT                       \n");//(M-1)월 차이
        sb.append("                   , NVL(C.PREV_YTD_PLAN_AMT,0) AS PREV_YTD_PLAN_AMT                     \n");//(M-1)월누계 사업계획
        sb.append("                   , NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0) AS PREV_YTD_EMERGENCY_PLAN_AMT \n");//(M-1)월누계 비상계획
        sb.append("                   , NVL(C.PREV_YTD_BD_AMT,0) AS PREV_YTD_BD_AMT                         \n");//(M-1)월누계 비용예산
        sb.append("                   , NVL(C.YTD_ACTUAL_AMT,0) AS YTD_ACTUAL_AMT                           \n");//(M-1)월누계 실적
        sb.append("                   , NVL(C.PREV_YTD_GAP_AMT,0) AS PREV_YTD_GAP_AMT                       \n");//(M-1)월누계 차이
        sb.append("                   , NVL(C.PLAN_AMT,0) AS PLAN_AMT                                       \n");//당월 사업계획
        sb.append("                   , NVL(C.EMERGENCY_PLAN_AMT,0) AS EMERGENCY_PLAN_AMT                   \n");//당월 비상계획
        sb.append("                   , NVL(C.BD_AMT,0) AS BD_AMT                                           \n");//당월 비용예산
        sb.append("                   , NVL(C.NEXT_PLAN_AMT,0) AS NEXT_PLAN_AMT                             \n");//(M+1)월 사업계획
        sb.append("                   , NVL(C.NEXT_EMERGENCY_PLAN_AMT,0) AS NEXT_EMERGENCY_PLAN_AMT         \n");//(M+1)월 비상계획
        sb.append("                   , NVL(C.NEXT_BD_AMT,0) AS NEXT_BD_AMT                                 \n");//(M+1)월 비용예산
        sb.append("                   , NVL(C.NEXT2_PLAN_AMT,0) AS NEXT2_PLAN_AMT                           \n");//(M+2)월 사업계획
        sb.append("                   , NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0) AS NEXT2_EMERGENCY_PLAN_AMT       \n");//(M+2)월 비상계획
        sb.append("                   , NVL(C.NEXT2_BD_AMT,0) AS NEXT2_BD_AMT                               \n");//(M+2)월 비용예산
                                                    /* AS - IS   GET_PLAN_BD_ACT_F   */
        sb.append("                FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F                         \n");
        sb.append("                          ( P_LEDGER_ID =>    "+ledger_id+"                                    \n");
        sb.append("                           ,P_PERIOD_YEAR =>  '"+period_year+"'                                  \n");
        sb.append("                           ,P_PERIOD_MONTH => '"+period_month+"'                                 \n");
        sb.append("                           ,P_CCTR_CD =>   '"+search_dept_code+"')) C                        \n");
        sb.append("               UNION ALL                                                                       \n");
        sb.append("              SELECT NULL                                                               		 \n");
        sb.append("                   , NULL                                                            			 \n");
        sb.append("                   , ''                                        								 \n");
        sb.append("                   , ''                                        								 \n");
        sb.append("                   , ''                                                              			 \n");
        sb.append("                   , ''                                                              			 \n");
        sb.append("                   , ''                                                              			 \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , ''                                                                        \n");
        sb.append("                   , '합계'                                                                   	 \n");
        sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0))                                            \n");//(M-1)월 사업계획
        sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0))                                  \n");//(M-1)월 비상계획
        sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0))                                              \n");//(M-1)월 비용예산
        sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0))                                           \n");//(M-1)월 실적
        sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0))                                         \n");//(M-1)월 차이
        sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0))                                        \n");//(M-1)월누계 사업계획
        sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0))                              \n");//(M-1)월누계 비상계획
        sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0))                                          \n");//(M-1)월누계 비용예산
        sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0))                                           \n");//(M-1)월누계 실적
        sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0))                                         \n");//(M-1)월누계 차이
        sb.append("                   , SUM(NVL(C.PLAN_AMT,0))                                                 \n");//당월 사업계획
        sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0))                                       \n");//당월 비상계획
        sb.append("                   , SUM(NVL(C.BD_AMT,0))                                                   \n");//당월 비용예산
        sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0))                                            \n");//(M+1)월 사업계획
        sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0))                                  \n");//(M+1)월 비상계획
        sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0))                                              \n");//(M+1)월 비용예산
        sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0))                                           \n");//(M+2)월 사업계획
        sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0))                                 \n");//(M+2)월 비상계획
        sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0))                                             \n");//(M+2)월 비용예산
        sb.append("                FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F                         \n");
        sb.append("                          ( P_LEDGER_ID =>    "+ledger_id+"                                    \n");
        sb.append("                           ,P_PERIOD_YEAR =>  '"+period_year+"'                                  \n");
        sb.append("                           ,P_PERIOD_MONTH => '"+period_month+"'                                 \n");
        sb.append("                           ,P_CCTR_CD =>   '"+search_dept_code+"')) C                        \n");
        sb.append("               GROUP BY C.LEDGER_ID                                                            \n");
        sb.append("                      , C.PERIOD_YEAR                                                          \n");
        sb.append("                      , C.CCTR_CD                                                           \n");
        sb.append("                      , C.CCTR_NM                                                           \n");
        sb.append("             ) T                                                                               \n");
        sb.append("       ORDER BY T.ACCT_DIV_NM, T.ACCT_CD                                                   \n");

        Query query = entityManager.createNativeQuery(sb.toString());

        return new JpaResultMapper().list(query, CostBudgetDto.class);
    }

    @Override
    public AccountDetailResult getPerformanceCheckDetail(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        AccountDetailResult accountDetailResult = new AccountDetailResult();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String pre_period_month = costBudgetDto.getPrePeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String acct_code = costBudgetDto.getAcctCd();
        String ledger_id = ledgerId;

        if("".equals(period_year)){
            throw new CostBudgetException("년도가 없습니다.");
        }

        if("".equals(period_month)){
            throw new CostBudgetException("월이 없습니다.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 	   SELECT A.TYPE							\n");
        sb.append(" 	            , A.TEAM_CODE AS TEAM_CD					\n");
        sb.append(" 	            , A.TEAM_NAME AS TEAM_NM					\n");
        sb.append(" 	            , A.PERIOD_YEAR						\n");
        sb.append(" 	            , A.QUARTER_NUM						\n");
        sb.append(" 	            , A.PERIOD_NUM						\n");
        sb.append(" 	            , A.ACCT_CODE AS ACCT_CD					\n");
        sb.append(" 	            , A.ACCT_NAME AS ACCT_NM					\n");
        sb.append(" 	            , A.EVENT_ID						\n");
        sb.append(" 	            , A.JOURNAL_LINE_ID					\n");
        sb.append(" 	            , A.SLIP_NUMBER AS SLIP_NO						\n");
        sb.append(" 	            , A.LINE_NUM AS LINE_NO					\n");
        sb.append(" 	            , A.DESCRIPTION						\n");
        sb.append(" 	            , A.VENDOR_ID						\n");
        sb.append(" 	            , A.VENDOR_SITE_ID					\n");
        sb.append(" 	            , A.VENDOR_NAME						\n");
        sb.append(" 	            , A.INTEGRATION_VENDOR_NUM			\n");
        sb.append(" 	            , A.CURRENCY_CODE					\n");
        sb.append(" 	            , A.EXCHANGE_RATE					\n");
        sb.append(" 	            , A.AMOUNT							\n");
        sb.append(" 	            , A.GL_DATE							\n");
        sb.append(" 	            , A.DEPT_CODE						\n");
        sb.append(" 	            , A.DEPT_NAME						\n");
        sb.append(" 	            , A.SEGMENT5						\n");
        sb.append(" 	            , A.CODE_COMBINATION_ID				\n");
        sb.append(" 	            , A.PROJECT_NUMBER					\n");
        sb.append(" 	            , A.TASK_NUMBER						\n");
        sb.append(" 	            , A.CREATED_BY						\n");
        sb.append(" 	            , A.CREATED_NAME					\n");
        sb.append(" 	            , A.ORG_ID 							\n");
        sb.append(" 	            , LGL.TRX_TYPE_CODE AS SLIP_TYPE 	\n");
        sb.append(" 	            , LGL.SLIP_HEADER_ID 				\n");
        sb.append(" 	            , LGL.APPROVAL_GROUP_ID 			\n");
        sb.append(" 	            , LGL.SLIP_GROUP_NUMBER 			\n");
        sb.append(" 	            , 'N' AS SLIP_GROUP_YN				\n");
        sb.append(" 	            , LGL.SLIP_STATUS AS SLIP_STATUS	\n");
        sb.append(" 	            , CASE WHEN LGL.SLIP_NUMBER IS NOT NULL THEN '10' ELSE '' END AS SLIP_FORM	\n");
        sb.append(" 	            , LGL.TTYPE_INPUT_MODULE 							\n");
        sb.append(" 	 		FROM CBOSLIP.CBO_AP_BUGET_ACT_DETAIL_T	 A				\n");
        sb.append(" 	 		LEFT OUTER JOIN APPS.CBO_SP_SLIP_HEADER LGL				\n");
        sb.append(" 	 					 ON A.SLIP_NUMBER = LGL.SLIP_NUMBER			\n");
        sb.append(" 	 					AND A.ORG_ID = LGL.ORG_ID					\n");
        sb.append(" 	 	   WHERE 1 = 1				 								\n");
        sb.append(" 	 	     AND A.ORG_ID = '"+ compCd +"'			 		\n");
//			sb.append(" 	 	     AND A.TYPE IN ('AP', 'PO')				 				\n");
        if(!"".equals(acct_code)){
            sb.append(" 	     AND A.ACCT_CODE = '"+ acct_code +"'					\n");
        }
        sb.append(" 	    AND (A.PERIOD_NUM = "+ Integer.parseInt(period_month) +")	\n");
        sb.append(" 	    AND (A.PERIOD_YEAR = "+ period_year +")					 	\n");
        sb.append(" 	 	AND A.TEAM_CODE = '" + search_dept_code + "'				\n");
        sb.append(" 	  ORDER BY A.ACCT_CODE ASC, A.GL_DATE ASC, A.SLIP_NUMBER ASC	\n");

        Query query = entityManager.createNativeQuery(sb.toString());

        accountDetailResult.setList(new JpaResultMapper().list(query, CostBudgetDto.class));

        sb = new StringBuilder();

        //총금액
        sb.append(" 	  SELECT NVL(SUM(AMOUNT),0)	AS ORIGIN_AMT			\n");
        sb.append(" 	 	FROM CBOSLIP.CBO_AP_BUGET_ACT_DETAIL_T A		\n");
        sb.append(" 	 	   WHERE 1 = 1				 								\n");
        sb.append(" 	 	     AND A.ORG_ID = '"+ compCd +"'			 		\n");
//			sb.append(" 	 	     AND A.TYPE IN ('AP', 'PO')				 				\n");
        if(!"".equals(acct_code)){
            sb.append(" 	     AND A.ACCT_CODE = '"+ acct_code +"'					\n");
        }
        sb.append(" 	    AND (A.PERIOD_NUM = "+ Integer.parseInt(period_month) +")	\n");
        sb.append(" 	    AND (A.PERIOD_YEAR = "+ period_year +")					 	\n");
        sb.append(" 	 	AND A.TEAM_CODE = '" + search_dept_code + "'				\n");
        sb.append(" 	  ORDER BY A.ACCT_CODE ASC, A.GL_DATE ASC, A.SLIP_NUMBER ASC	\n");


        Query query2 = entityManager.createNativeQuery(sb.toString());

        accountDetailResult.setTotal((new JpaResultMapper().uniqueResult(query2, CostBudgetDto.class)).getCount());

        return accountDetailResult;
    }

    @Override
    public String doApprovalSave(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        String msg = "예산이 확정 되었습니다.";

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String session_user_id = util.getLoginUserId();
        BigDecimal ledger_Id = BigDecimal.valueOf(Long.parseLong(ledgerId));
        String insert_yn = "";

        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 0;
        int s_period_month	= 0;
        s_period_month = Integer.parseInt(period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 갯수에 따라 크기 설정
        String period_dyn[] 	= new String[select_cnt];
        String approval_flag[]	= new String[select_cnt];
        String period_bmc[]		= new String[select_cnt];
        String period_um[] 		= new String[select_cnt];


        // 검증(1)
        if("".equals(search_dept_code)){
            throw new CostBudgetException("예산부서가 없습니다.");
        }

        // 검증(2) - 조회기간에 해당되는 각 월의 데이터 존재여부 확인
        for(int a = 0 ; a < select_cnt ; a++){
            // 숫자로 계산
            int pm_int = Integer.parseInt(period_month) + a;
            // 한 자릿수, 두 자릿수 구분
            if((pm_int) < 10){
                period_dyn[a] = "0" + (pm_int);
            }else{
                period_dyn[a]= (pm_int) + "";
            }

            StringBuilder sb = new StringBuilder();

            sb.append("              SELECT COUNT(*) AS CNT                         \n");
            sb.append("                FROM TB_COST_BUDGET SB                                \n");
            sb.append("               WHERE 1 = 1                                   \n");
            sb.append("               AND SB.LEDGER_ID = :ledgerId                        \n");
            sb.append("               AND SB.COMP_CD = :compCd                     \n");
            sb.append("               AND SB.CCTR_CD = :deptCd                       \n");
            sb.append("               AND SB.PERIOD_YEAR = :periodYear                      \n");
            sb.append("               AND SB.PERIOD_MONTH = :periodMonth                     \n");

            Query query = entityManager.createNativeQuery(sb.toString());

            query.setParameter("compCd", compCd);
            query.setParameter("ledgerId", ledgerId);
            query.setParameter("periodYear", period_year);
            query.setParameter("periodMonth", period_dyn[a]);
            query.setParameter("deptCd", search_dept_code);

            BigDecimal sf_cnt = new JpaResultMapper().uniqueResult(query, CostBudgetDto.class).getCount();

            int b_cnt = sf_cnt.intValue();
            if(b_cnt == 0) {
                throw new CostBudgetException(period_dyn[a]+"월 데이터 저장 후 [예산확정] 하시기 바랍니다.");
            }

        }

        // 예산승인 flag 불러오기
        StringBuilder sb = new StringBuilder();
        sb.append("              SELECT SB.LEDGER_ID                                               \n");
        sb.append("                   , SB.PERIOD_YEAR                                             \n");
        sb.append("                   , SB.CCTR_CD                                              \n");
        sb.append("                   , SB.CCTR_NM                                              \n");
        String b_month = "";
        for(int i = 0; i < select_cnt; i++) {
            int pm_int = Integer.parseInt(period_month) + i;
            if((pm_int) < 10){
                b_month = "0" + (pm_int);
                sb.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+b_month+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+b_month+"_APPR_YN \n");
            }else{
                b_month = (pm_int) + "";
                sb.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+b_month+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+b_month+"_APPR_YN \n");
            }
        }
        sb.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
        sb.append("               WHERE 1 = 1                                                                                           \n");
        sb.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
        sb.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
        sb.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
        sb.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
        sb.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
        sb.append("                                  SELECT MAX(DEGREE)                                                                 \n");
        sb.append("                                    FROM TB_COST_BUDGET                                                                       \n");
        sb.append("                                   WHERE 1 = 1                                                                       \n");
        sb.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
        sb.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
        sb.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
        sb.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
        sb.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
        sb.append("                                 ), 1)                                                                               \n");
        sb.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
        sb.append("                      , SB.PERIOD_YEAR                                                                               \n");
        sb.append("                      , SB.CCTR_CD                                                                            	   \n");
        sb.append("                      , SB.CCTR_NM                                                                            	   \n");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("ledgerId", ledgerId);
        query.setParameter("periodYear", period_year);
        query.setParameter("deptCd", search_dept_code);

        List<CostBudgetDto> sf = new JpaResultMapper().list(query, CostBudgetDto.class);

        // 검증(3) - 예산확정여부 월별 확인
        if(sf.size() > 0){

            for(int j = 0 ; j < select_cnt ; j++){
                // 숫자로 계산
                int pm_int = Integer.parseInt(period_month) + j;
                // 한 자릿수, 두 자릿수 구분
                if((pm_int) < 10){
                    period_bmc[j] = "0" + (pm_int);
                }else{
                    period_bmc[j]= (pm_int) + "";
                }

                if("Y".equals(sf.get(0).getBudget01ApprYn()) || "Y".equals(sf.get(0).getBudget02ApprYn()) || "Y".equals(sf.get(0).getBudget03ApprYn()) ) {
                    throw new CostBudgetException("3개월 내에 이미 예산 확정된 월이 존재합니다.");
                }
            }
        }

        try {

            // Flag Update
            for(int b = 0 ; b < select_cnt ; b++){


                // 숫자로 계산
                int pm_int = Integer.parseInt(period_month) + b;
                // 한 자릿수, 두 자릿수 구분
                if((pm_int) < 10){
                    period_um[b] = "0" + (pm_int);
                }else{
                    period_um[b]= (pm_int) + "";
                }

                costBudgetRepository.updateBudgetApprYn("Y",compCd,ledger_Id,search_dept_code,period_year,period_um[b]);
            }
            insert_yn = "update";

        }catch (Exception e) {
            if("update".equals(insert_yn)){

                // Flag Rollback
                String period_rb[] = new String[select_cnt];
                for(int c = 0 ; c < select_cnt ; c++){
                    // 숫자로 계산
                    int pm_int = Integer.parseInt(period_month) + c;
                    // 한 자릿수, 두 자릿수 구분
                    if((pm_int) < 10){
                        period_rb[c] = "0" + (pm_int);
                    }else{
                        period_rb[c]= (pm_int) + "";
                    }
                    costBudgetRepository.updateBudgetApprYn("N",compCd,ledger_Id,search_dept_code,period_year,period_rb[c]);
                }
            }

            msg = "예산 확정이 실패되었습니다.";
        } finally {

        }

        return msg;
    }

    @Override
    public String doApprovalClear(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        String msg = "예산이 해제 되었습니다.";

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String search_dept_name = costBudgetDto.getSearchDeptNm();
        String session_user_id = util.getLoginUserId();
        BigDecimal ledger_Id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        // 조회할 칼럼의 갯수 구하기
        int select_cnt		= 0;
        int s_period_month	= Integer.parseInt(period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m1[] = new String[select_cnt];
        String period_m2[] = new String[select_cnt];

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String app_yn[] = new String[select_cnt];


        StringBuilder sb = new StringBuilder();

        sb.append("              SELECT COUNT(*) AS CNT                         \n");
        sb.append("                FROM TB_COST_BUDGET SB                       \n");
        sb.append("               WHERE 1 = 1                                   \n");
        sb.append("                AND SB.LEDGER_ID = :ledgerId                        \n");
        sb.append("                AND SB.COMP_CD = :compCd                          \n");
        sb.append("                AND SB.CCTR_CD = :deptCd                          \n");
        sb.append("                AND SB.PERIOD_YEAR = :periodYear                      \n");
        sb.append("                AND SB.PERIOD_MONTH = :periodMonth                    \n");


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("ledgerId", ledgerId);
        query.setParameter("periodYear", period_year);
        query.setParameter("periodMonth", period_month);
        query.setParameter("deptCd", search_dept_code);

        BigDecimal sf_cnt = new JpaResultMapper().uniqueResult(query, CostBudgetDto.class).getCount();


        int b_cnt = sf_cnt.intValue();
        if(b_cnt == 0) {
            throw new CostBudgetException(period_month+"월 데이터 저장 후 [예산확정] 하시기 바랍니다.");
        }

        sb = new StringBuilder();

        sb.append("              SELECT SB.LEDGER_ID                                               \n");
        sb.append("                   , SB.PERIOD_YEAR                                             \n");
        sb.append("                   , SB.CCTR_CD                                              \n");
        sb.append("                   , SB.CCTR_NM                                              \n");
        sb.append("                   , SB.ACTUAL_APPR_YN                                       \n");
        sb.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
        sb.append("               WHERE 1 = 1                                                                                           \n");
        sb.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
        sb.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
        sb.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
        sb.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
        sb.append("                 AND SB.PERIOD_MONTH = :periodMonth                                                                              \n");
        sb.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
        sb.append("                                  SELECT MAX(DEGREE)                                                                 \n");
        sb.append("                                    FROM TB_COST_BUDGET                                                                       \n");
        sb.append("                                   WHERE 1 = 1                                                                       \n");
        sb.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
        sb.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
        sb.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
        sb.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
        sb.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
        sb.append("                                 ), 1)                                                                               \n");
        sb.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
        sb.append("                      , SB.PERIOD_YEAR                                                                               \n");
        sb.append("                      , SB.CCTR_CD                                                                            	   \n");
        sb.append("                      , SB.CCTR_NM                                                                                \n");
        sb.append("                      , SB.ACTUAL_APPR_YN                                                                            \n");

        Query query2 = entityManager.createNativeQuery(sb.toString());

        query2.setParameter("compCd", compCd);
        query2.setParameter("ledgerId", ledgerId);
        query2.setParameter("periodYear", period_year);
        query2.setParameter("periodMonth", period_month);
        query2.setParameter("deptCd", search_dept_code);

        List<CostBudgetDto> sf_a = new JpaResultMapper().list(query2, CostBudgetDto.class);

        if(sf_a.size() == 1 && "Y".equals(sf_a.get(0).getActualApprYn())){
            throw new CostBudgetException("예산부서 : "+ search_dept_name + "의 실적저장이 되어 수정 할 수 없습니다.");
        }

        costBudgetRepository.updateBudgetApprYn("N",compCd,ledger_Id,search_dept_code,period_year,period_month);

        return msg;
    }

    @Override
    public String doSave(CostBudgetSaveList costBudgetSaveList) throws Exception {

        String msg = "저장하였습니다.";

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        CostBudgetDto main = costBudgetSaveList.getMain();
        List<CostBudgetDto> costBudgetDtos = costBudgetSaveList.getList();
        CostBudgetDto planConfirm = costBudgetSaveList.getPlanConfirm();

        String search_period_year = main.getPeriodYear();
        String search_period_month = main.getPeriodMonth();
        String session_user_id = util.getLoginUserId();
        BigDecimal ledger_Id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        Integer thisMonth = Integer.parseInt(main.getPeriodMonth());

        int select_cnt		= 0;
        int s_period_month	= Integer.parseInt(search_period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m1[] = new String[select_cnt];
        String bd_amt[] = new String[select_cnt];
        String desc[] = new String[select_cnt];
        String degree[] = new String[select_cnt];

        for(CostBudgetDto costBudgetDto : costBudgetDtos){
            int arrayCnt = 0;
            String period_year		= costBudgetDto.getPeriodYear();
            String bdept_code		= costBudgetDto.getCctrCd();
            String bdept_name		= costBudgetDto.getCctrNm();
            String acct_code		= costBudgetDto.getAcctCd();
            String acct_name		= costBudgetDto.getAcctNm();
            String project_code		= costBudgetDto.getPjtCd();
            String project_name		= costBudgetDto.getPjtNm();
            String itemgroup_code	= costBudgetDto.getItemGroupCd();
            String itemgroup_name	= costBudgetDto.getItemGroupNm();
            String insert_yn		= costBudgetDto.getInsertYn();
            String user_yn			= costBudgetDto.getUserYn();


            // 검증 - 연도 비교(Grid의 연도와 조회조건의 연도 비교)
            if(!search_period_year.equals(period_year)){
                //조회조건의 년도와 수정하려는 행의 년도가 맞지 않습니다.
                throw new CostBudgetException("조회조건의 년도와 수정하려는 행의 년도가 맞지 않습니다.");
            }


            for(int j = 0; j < select_cnt; j++) {
                if(j == 0){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM01BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM01Remark();	// 적요
                }else if(j == 1){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM02BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM02Remark();	// 적요
                }else if(j == 2){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM03BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM03Remark();	// 적요
                }


                // 숫자로 변환
                int pm_int = Integer.parseInt(search_period_month) + j;
                // 한 자릿수, 두 자릿수 구분
                if(pm_int < 10){
                    period_m1[j] = "0" + (pm_int);
                }else{
                    period_m1[j] = (pm_int) + "";
                }
                // 3개월에 해당되는 예산확정 flag 불러오기

                StringBuilder sb = new StringBuilder();
                sb.append("              SELECT SB.LEDGER_ID                                               		\n");
                sb.append("                   , SB.PERIOD_YEAR                                             		\n");
                sb.append("                   , SB.CCTR_CD                                              		\n");
                sb.append("                   , SB.CCTR_NM                                              		\n");
                sb.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+period_m1[j]+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+period_m1[j]+"_APPR_YN \n");
                sb.append("           , NVL((SELECT MAX(DEGREE)                                       			\n");
                sb.append("               	  FROM TB_COST_BUDGET                                             			\n");
                sb.append("              	 WHERE 1 = 1                                             			\n");
                sb.append("               	   AND COMP_CD = SB.COMP_CD                   			\n");
                sb.append("                 	   AND LEDGER_ID = SB.LEDGER_ID                         			\n");
                sb.append("                     AND PERIOD_YEAR = SB.PERIOD_YEAR                     			\n");
                sb.append("                     AND PERIOD_MONTH = :periodMonth                              	\n");
//                sb.append("                     AND CCTR_CD = SB.CCTR_CD),1) AS M"+period_m1[j]+"_DEGREE	\n");
                sb.append("                     AND CCTR_CD = SB.CCTR_CD),1) AS DEGREE	\n");
                sb.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
                sb.append("               WHERE 1 = 1                                                                                           \n");
                sb.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
                sb.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
                sb.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
                sb.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
                sb.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
                sb.append("                                  SELECT MAX(DEGREE)                                                                 \n");
                sb.append("                                    FROM TB_COST_BUDGET                                                                       \n");
                sb.append("                                   WHERE 1 = 1                                                                       \n");
                sb.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
                sb.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
                sb.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
                sb.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
                sb.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
                sb.append("                                 ), 1)                                                                               \n");
                sb.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
                sb.append("                      , SB.COMP_CD                                                                              \n");
                sb.append("                      , SB.PERIOD_YEAR                                                                               \n");
                sb.append("                      , SB.CCTR_CD                                                                            	   \n");
                sb.append("                      , SB.CCTR_NM                                                                                \n");

                Query query = entityManager.createNativeQuery(sb.toString());

                query.setParameter("compCd", compCd);
                query.setParameter("ledgerId", ledgerId);
                query.setParameter("periodYear", period_year);
                query.setParameter("periodMonth", period_m1[j]);
                query.setParameter("deptCd", bdept_code);

                List<CostBudgetDto> sf = new JpaResultMapper().list(query, CostBudgetDto.class);



                //  || "Y".equals(sf.get(0).getBudget02ApprYn()) || "Y".equals(sf.get(0).getBudget03ApprYn()) )) {

                // 검증(4) - 예산확정여부 확인
//                if(sf.size() == 1 && ("Y".equals(sf.get(0).getBudget01ApprYn()))){
//                    throw new Exception("예산부서 : "+ bdept_name + "의" + thisMonth + "월 예산이 확정되어 저장 할 수 없습니다.");
//                }
//
//                if(sf.size() == 1 && ("Y".equals(sf.get(0).getBudget02ApprYn()))){
//                    throw new Exception("예산부서 : "+ bdept_name + "의" + (thisMonth+1) + "월 예산이 확정되어 저장 할 수 없습니다.");
//                }
//
//                if(sf.size() == 1 && ("Y".equals(sf.get(0).getBudget03ApprYn()))){
//                    throw new Exception("예산부서 : "+ bdept_name + "의" + (thisMonth+2) + "월 예산이 확정되어 저장 할 수 없습니다.");
//                }

                // +2의 값 체크 할 것
                CostBudgetDto ActualApprVal = getActualApprYnChk(planConfirm,thisMonth);

                boolean ActualApprYnFlag = ActualApprVal.getChkFlag();
                msg = ActualApprVal.getMsg();

                if(sf.size() == 1 && ActualApprYnFlag ){
                    throw new CostBudgetException(msg);
//                    throw new Exception("예산부서 : "+ bdept_name + "의" + thisMonth + "월 예산이 확정되어 저장 할 수 없습니다.");
                }



                if(sf.size() == 1) {
                    degree[j]	= sf.get(0).getDegree();
                } else {
                    degree[j] = "1";
                }

                // 테이블 카운트 (Insert 또는 Update)
                sb = new StringBuilder();
                sb.append("              SELECT COUNT(*) AS CNT           \n");
                sb.append("                FROM TB_COST_BUDGET                     \n");
                sb.append("               WHERE 1 = 1                     \n");
                sb.append("                AND LEDGER_ID = :ledgerId             \n");
                sb.append("                AND COMP_CD = :compCd          \n");
                sb.append("                AND PERIOD_YEAR = :periodYear           \n");
                sb.append("                AND PERIOD_MONTH = :periodMonth          \n");
                sb.append("                AND CCTR_CD = :deptCd            \n");
                sb.append("                AND ACCT_CD = :acctCd             \n");
                sb.append("                AND PJT_CD = :pjtCd          \n");
                sb.append("                AND ITEM_GROUP_CD = :itemGroupCd        \n");
                sb.append("                AND DEGREE = :degree        		 \n");

                Query query2 = entityManager.createNativeQuery(sb.toString());

                query2.setParameter("compCd", compCd);
                query2.setParameter("ledgerId", ledgerId);
                query2.setParameter("periodYear", period_year);
                query2.setParameter("periodMonth", period_m1[j]);
                query2.setParameter("deptCd", bdept_code);
                query2.setParameter("acctCd", acct_code);
                query2.setParameter("pjtCd", project_code);
                query2.setParameter("itemGroupCd", itemgroup_code);
                query2.setParameter("degree", degree[j]);

                int cnt = new JpaResultMapper().uniqueResult(query2, CostBudgetDto.class).getCount().intValue();

                // 검증 - 신규로 입력한 데이터가 존재 할 경우
                if(cnt > 0 && "Y".equals(insert_yn)) {
                    // 기존 데이터가 존재합니다.
                    throw new CostBudgetException("예산부서 : " + bdept_name+"\n"+ "COA : " + acct_code +"-"+ project_code +"-"+ itemgroup_code+"\n" + "기존 데이터가 존재합니다.");
                }

                if(cnt > 0) {
                    // 수정
                    costBudgetRepository.updateBudgetAmt(
                            BigDecimal.valueOf(Long.parseLong(bd_amt[j])),desc[j],compCd,ledger_Id,bdept_code,period_year,
                            period_m1[j],acct_code,project_code,itemgroup_code,degree[j]
                    );

                } else {
                    // 신규

                    CostBudget costBudget = new CostBudget().builder()
                            .compCd(compCd)
                            .ledgerId(ledger_Id)
                            .periodYear(period_year)
                            .periodMonth(period_m1[j])
                            .cctrCd(bdept_code)
                            .cctrNm(bdept_name)
                            .acctCd(acct_code)
                            .acctNm(acct_name)
                            .pjtCd(project_code)
                            .pjtNm(project_name)
                            .itemGroupCd(itemgroup_code)
                            .itemGroupNm(itemgroup_name)
                            .budgetAmt(BigDecimal.valueOf(Long.parseLong(bd_amt[j])))
                            .remark(desc[j])
                            .degree(degree[j])
                            .budgetApprYn("N")
                            .actualApprYn("N")
                            .attribute1("Y")
                            .build();

                    costBudgetRepository.save(costBudget);

                }
            }

        }

        return msg;
    }


    @Override
    public String doDelete(CostBudgetSaveList costBudgetSaveList) throws Exception {

        String msg = "삭제하였습니다.";

        String compCd = util.getLoginCompCd();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        CostBudgetDto main = costBudgetSaveList.getMain();
        List<CostBudgetDto> costBudgetDtos = costBudgetSaveList.getList();

        String search_period_year = main.getPeriodYear();
        String search_period_month = main.getPeriodMonth();
        String session_user_id = util.getLoginUserId();
        BigDecimal ledger_Id = BigDecimal.valueOf(Long.parseLong(ledgerId));

        int select_cnt		= 0;
        int s_period_month	= Integer.parseInt(search_period_month);
        if(s_period_month == 11){
            select_cnt = 2;
        } else if (s_period_month == 12){
            select_cnt = 1;
        } else {
            select_cnt = 3;
        }

        // 조회할 칼럼의 기간에 해당되는 Array 생성
        String period_m1[] = new String[select_cnt];
        String bd_amt[] = new String[select_cnt];
        String desc[] = new String[select_cnt];
        String degree[] = new String[select_cnt];

        for(CostBudgetDto costBudgetDto : costBudgetDtos){
            int arrayCnt = 0;
            String period_year		= costBudgetDto.getPeriodYear();
            String bdept_code		= costBudgetDto.getCctrCd();
            String bdept_name		= costBudgetDto.getCctrNm();
            String acct_code		= costBudgetDto.getAcctCd();
            String acct_name		= costBudgetDto.getAcctNm();
            String project_code		= costBudgetDto.getPjtCd();
            String project_name		= costBudgetDto.getPjtNm();
            String itemgroup_code	= costBudgetDto.getItemGroupCd();
            String itemgroup_name	= costBudgetDto.getItemGroupNm();
            String insert_yn		= costBudgetDto.getInsertYn();
            String user_yn			= costBudgetDto.getUserYn();


            // 검증 - 연도 비교(Grid의 연도와 조회조건의 연도 비교)
            if(!search_period_year.equals(period_year)){
                //조회조건의 년도와 수정하려는 행의 년도가 맞지 않습니다.
                throw new CostBudgetException("조회조건의 년도와 수정하려는 행의 년도가 맞지 않습니다.");
            }

            if(!user_yn.equals("Y")) {
                //사용자가 직접 입력한 데이터만 삭제할 수 있습니다.
                throw new CostBudgetException("사용자가 직접 입력한 데이터만 삭제할 수 있습니다.");
            }

            for(int j = 0; j < select_cnt; j++) {
                if(j == 0){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM01BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM01Remark();	// 적요
                }else if(j == 1){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM02BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM02Remark();	// 적요
                }else if(j == 2){
                    bd_amt[j] 	= String.valueOf(costBudgetDto.getM03BudgetAmt());	// 예산
                    desc[j] 	= costBudgetDto.getM03Remark();	// 적요
                }


                // 숫자로 변환
                int pm_int = Integer.parseInt(search_period_month) + j;
                // 한 자릿수, 두 자릿수 구분
                if(pm_int < 10){
                    period_m1[j] = "0" + (pm_int);
                }else{
                    period_m1[j] = (pm_int) + "";
                }


                StringBuilder sb = new StringBuilder();

                // 테이블 카운트
                sb.append("              SELECT COUNT(*) AS CNT           \n");
                sb.append("                FROM TB_PLAN_AMT                     \n");
                sb.append("               WHERE 1 = 1                     \n");
                sb.append("                 AND LEDGER_ID = :ledgerId             \n");
                sb.append("                 AND COMP_CD = :compCd          \n");
                sb.append("                 AND PERIOD_YEAR = :periodYear           \n");
                sb.append("                 AND PERIOD_MONTH = :periodMonth          \n");
                sb.append("                 AND CCTR_CD = :cctrCd            \n");
                sb.append("                 AND ACCT_CD = :acctCd             \n");
                sb.append("                 AND PJT_CD = :pjtCd          \n");
                sb.append("                 AND ITEM_GROUP_CD = :itemGroupCd        \n");

                Query query0 = entityManager.createNativeQuery(sb.toString());

                query0.setParameter("compCd", compCd);
                query0.setParameter("ledgerId", ledgerId);
                query0.setParameter("periodYear", period_year);
                query0.setParameter("periodMonth", period_m1[j]);
                query0.setParameter("cctrCd", bdept_code);
                query0.setParameter("acctCd", acct_code);
                query0.setParameter("pjtCd", project_code);
                query0.setParameter("itemGroupCd", itemgroup_code);

                BigDecimal sf_cnt = new JpaResultMapper().uniqueResult(query0, CostBudgetDto.class).getCount();


                int cnt = sf_cnt.intValue();

                // 검증 - 사업계획이 존재할 경우 삭제 불가
                if(cnt > 0) {
                    // 사업계획이 존재하여 삭제할 수 없습니다.
                    throw new CostBudgetException("사업계획이 존재하여 삭제할 수 없습니다.");
                }

                // 3개월에 해당되는 예산확정 flag 불러오기

                sb = new StringBuilder();
                sb.append("              SELECT SB.LEDGER_ID                                               		\n");
                sb.append("                   , SB.PERIOD_YEAR                                             		\n");
                sb.append("                   , SB.CCTR_CD                                              		\n");
                sb.append("                   , SB.CCTR_NM                                              		\n");
                sb.append("           , MAX(DECODE(SB.PERIOD_MONTH,"+period_m1[j]+",SB.BUDGET_APPR_YN,'N')) AS BUDGET"+period_m1[j]+"_APPR_YN \n");
                sb.append("           , NVL((SELECT MAX(DEGREE)                                       			\n");
                sb.append("               	  FROM TB_COST_BUDGET                                             			\n");
                sb.append("              	 WHERE 1 = 1                                             			\n");
                sb.append("               	   AND COMP_CD = SB.COMP_CD                   			\n");
                sb.append("                 	   AND LEDGER_ID = SB.LEDGER_ID                         			\n");
                sb.append("                     AND PERIOD_YEAR = SB.PERIOD_YEAR                     			\n");
                sb.append("                     AND PERIOD_MONTH = :periodMonth                              	\n");
//                sb.append("                     AND CCTR_CD = SB.CCTR_CD),1) AS M"+period_m1[j]+"_DEGREE	\n");
                sb.append("                     AND CCTR_CD = SB.CCTR_CD),1) AS DEGREE	\n");
                sb.append("                FROM TB_COST_BUDGET SB                                                                                        \n");
                sb.append("               WHERE 1 = 1                                                                                           \n");
                sb.append("                 AND SB.LEDGER_ID = :ledgerId                                                                                \n");
                sb.append("                 AND SB.COMP_CD = :compCd                                                                             \n");
                sb.append("                 AND SB.CCTR_CD = :deptCd                                                                               \n");
                sb.append("                 AND SB.PERIOD_YEAR = :periodYear                                                                              \n");
                sb.append("                 AND NVL(SB.DEGREE, 1) = NVL((                                                                       \n");
                sb.append("                                  SELECT MAX(DEGREE)                                                                 \n");
                sb.append("                                    FROM TB_COST_BUDGET                                                                       \n");
                sb.append("                                   WHERE 1 = 1                                                                       \n");
                sb.append("                                     AND COMP_CD = SB.COMP_CD                                              \n");
                sb.append("                                     AND LEDGER_ID = SB.LEDGER_ID                                                    \n");
                sb.append("                                     AND PERIOD_YEAR = SB.PERIOD_YEAR                                                \n");
                sb.append("                                     AND PERIOD_MONTH = SB.PERIOD_MONTH                                              \n");
                sb.append("                                     AND CCTR_CD = SB.CCTR_CD                                                  \n");
                sb.append("                                 ), 1)                                                                               \n");
                sb.append("               GROUP BY SB.LEDGER_ID                                                                                 \n");
                sb.append("                      , SB.COMP_CD                                                                              \n");
                sb.append("                      , SB.PERIOD_YEAR                                                                               \n");
                sb.append("                      , SB.CCTR_CD                                                                            	   \n");
                sb.append("                      , SB.CCTR_NM                                                                                \n");

                Query query = entityManager.createNativeQuery(sb.toString());

                query.setParameter("compCd", compCd);
                query.setParameter("ledgerId", ledgerId);
                query.setParameter("periodYear", period_year);
                query.setParameter("periodMonth", period_m1[j]);
                query.setParameter("deptCd", bdept_code);

                List<CostBudgetDto> sf = new JpaResultMapper().list(query, CostBudgetDto.class);

                // 검증(4) - 예산확정여부 확인
                if(sf.size() == 1 && ("Y".equals(sf.get(0).getBudget01ApprYn()) || "Y".equals(sf.get(0).getBudget02ApprYn()) || "Y".equals(sf.get(0).getBudget03ApprYn()) )) {
                    throw new CostBudgetException("예산부서 : "+ bdept_name + "의 예산이 확정되어 삭제 할 수 없습니다.");
                }

                if(sf.size() == 1) {
                    degree[j]	= sf.get(0).getDegree();
                } else {
                    degree[j] = "1";
                }

                CostBudgetKey costBudgetKey = new CostBudgetKey(compCd,ledger_Id,period_year,period_m1[j],bdept_code,acct_code,project_code,itemgroup_code,degree[j]);
                costBudgetRepository.deleteById(costBudgetKey);

            }

        }

        return msg;
    }


    @Override
    public DraftListResult getDraftList(CostBudgetDto costBudgetDto) throws Exception {

        String compCd = util.getLoginCompCd();
        String ledgerId = util.getLoginUser().getEmployee().getLedgerId();

        String period_year = costBudgetDto.getPeriodYear();
        String period_month = costBudgetDto.getPeriodMonth();
        String pre_period_month = costBudgetDto.getPrePeriodMonth();
        String search_dept_code = costBudgetDto.getSearchDeptCd();
        String ledger_id = ledgerId;

        String slip_no = costBudgetDto.getSlipNo();
        String slip_type = costBudgetDto.getSlipType();
        BigDecimal slip_header_id = costBudgetDto.getSlipHeaderId();

        String ref_user_id = "XXX";
        String ref_user_name = "XXX";

        StringBuilder sb = new StringBuilder();

        DraftListResult draftListResult = new DraftListResult();

        // 신규 건
        if (slip_no.equals("")) {

            sb.append("     SELECT T.LEDGER_ID                                                              \n");
            sb.append("          , T.PERIOD_YEAR                                                            \n");
            sb.append("          , T.PERIOD_MONTH                                                           \n");
            sb.append("          , T.CCTR_CD                                                             \n");
            sb.append("          , T.CCTR_NM                                                             \n");
            sb.append("          , T.ACCT_DIV_NM                               						   \n");
            sb.append("          , T.ACCT_CD                                                              \n");
            sb.append("          , T.ACCT_NM                                                              \n");
            sb.append("          , T.PJT_CD                                                           \n");
            sb.append("          , T.PJT_NM                                                           \n");
            sb.append("          , T.ITEM_GROUP_CD                                                         \n");
            sb.append("          , T.ITEM_GROUP_NM                                                         \n");
            sb.append("          , T.PREV_PLAN_AMT                                \n");
            sb.append("          , T.PREV_EMERGENCY_PLAN_AMT                                \n");
            sb.append("          , T.PREV_BD_AMT                                \n");
            sb.append("          , T.PTD_ACTUAL_AMT                                \n");
            sb.append("          , T.PREV_PTD_GAP_AMT                                \n");
            sb.append("          , T.PREV_YTD_PLAN_AMT                                \n");
            sb.append("          , T.PREV_YTD_EMERGENCY_PLAN_AMT                                \n");
            sb.append("          , T.PREV_YTD_BD_AMT                                \n");
            sb.append("          , T.YTD_ACTUAL_AMT                                \n");
            sb.append("          , T.PREV_YTD_GAP_AMT                                \n");
            sb.append("          , T.PLAN_AMT                                \n");
            sb.append("          , T.EMERGENCY_PLAN_AMT                                \n");
            sb.append("          , T.BD_AMT                                \n");
            sb.append("          , T.NEXT_PLAN_AMT                                \n");
            sb.append("          , T.NEXT_EMERGENCY_PLAN_AMT                                \n");
            sb.append("          , T.NEXT_BD_AMT                                \n");
            sb.append("          , T.NEXT2_PLAN_AMT                                \n");
            sb.append("          , T.NEXT2_EMERGENCY_PLAN_AMT                                \n");
            sb.append("          , T.NEXT2_BD_AMT                                \n");
            sb.append("       FROM (                                                                        \n");

            sb.append("             SELECT C.LEDGER_ID                                                      \n");
            sb.append("                  , C.PERIOD_YEAR                                                    \n");
            sb.append("                  , '"+period_month+"' AS PERIOD_MONTH                               \n");
            sb.append("                  , C.CCTR_CD                                                     \n");
            sb.append("                  , C.CCTR_NM                                                     \n");
            sb.append("                  , NVL(C.ACCT_DIV_NM, 'X') AS ACCT_DIV_NM                       \n");
            sb.append("                  , C.ACCT_CD                                                      \n");
            sb.append("                  , C.ACCT_NM                                                      \n");
            sb.append("                  , '' AS PJT_CD                                               \n");
            sb.append("                  , '' AS PJT_NM                                               \n");
            sb.append("                  , '' AS ITEM_GROUP_CD                                             \n");
            sb.append("                  , '' AS ITEM_GROUP_NM                                             \n");

            sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0)) AS PREV_PLAN_AMT                             \n");//(M-1)월 사업계획
            sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0)) AS PREV_EMERGENCY_PLAN_AMT         \n");//(M-1)월 비상계획
            sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0)) AS PREV_BD_AMT                                 \n");//(M-1)월 비용예산
            sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0)) AS PTD_ACTUAL_AMT                           \n");//(M-1)월 실적
            sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0)) AS PREV_PTD_GAP_AMT                       \n");//(M-1)월 차이
            sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0)) AS PREV_YTD_PLAN_AMT                     \n");//(M-1)월누계 사업계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0)) AS PREV_YTD_EMERGENCY_PLAN_AMT \n");//(M-1)월누계 비상계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0)) AS PREV_YTD_BD_AMT                         \n");//(M-1)월누계 비용예산
            sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0)) AS YTD_ACTUAL_AMT                           \n");//(M-1)월누계 실적
            sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0)) AS PREV_YTD_GAP_AMT                       \n");//(M-1)월누계 차이
            sb.append("                   , SUM(NVL(C.PLAN_AMT,0)) AS PLAN_AMT                                       \n");//당월 사업계획
            sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0)) AS EMERGENCY_PLAN_AMT                   \n");//당월 비상계획
            sb.append("                   , SUM(NVL(C.BD_AMT,0)) AS BD_AMT                                           \n");//당월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0)) AS NEXT_PLAN_AMT                             \n");//(M+1)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0)) AS NEXT_EMERGENCY_PLAN_AMT         \n");//(M+1)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0)) AS NEXT_BD_AMT                                 \n");//(M+1)월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0)) AS NEXT2_PLAN_AMT                           \n");//(M+2)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0)) AS NEXT2_EMERGENCY_PLAN_AMT       \n");//(M+2)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0)) AS NEXT2_BD_AMT                               \n");//(M+2)월 비용예산
                                        /* AS - IS   GET_PLAN_BD_ACT_F   */
            sb.append("               FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F               \n");
            sb.append("                         ( P_LEDGER_ID =>    "+ledger_id+"                          \n");
            sb.append("                          ,P_PERIOD_YEAR =>  '"+period_year+"'                      \n");
            sb.append("                          ,P_PERIOD_MONTH => '"+period_month+"'                     \n");
            sb.append("                          ,P_CCTR_CD =>   '"+search_dept_code+"')) C                   \n");

            sb.append("              WHERE ACCT_DIV_NM = '변동비'                                                \n");

            sb.append("              GROUP BY C.LEDGER_ID                                                  \n");
            sb.append("                     , C.PERIOD_YEAR                                                \n");
            sb.append("                     , C.CCTR_CD                                                 \n");
            sb.append("                     , C.CCTR_NM                                                 \n");
            sb.append("                     , C.ACCT_DIV_NM                                              \n");
            sb.append("                     , C.ACCT_CD                                                  \n");
            sb.append("                     , C.ACCT_NM                                                  \n");
            sb.append("              UNION ALL                                                             \n");
            sb.append("             SELECT C.LEDGER_ID                                                     \n");
            sb.append("                  , C.PERIOD_YEAR                                                   \n");
            sb.append("                  , '"+period_month+"' AS PERIOD_MONTH                              \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , '변동비 소계'                                                       	  \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                    		  \n");

            sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0))                                            \n");//(M-1)월 사업계획
            sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0))                                  \n");//(M-1)월 비상계획
            sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0))                                              \n");//(M-1)월 비용예산
            sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0))                                           \n");//(M-1)월 실적
            sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0))                                         \n");//(M-1)월 차이
            sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0))                                        \n");//(M-1)월누계 사업계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0))                              \n");//(M-1)월누계 비상계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0))                                          \n");//(M-1)월누계 비용예산
            sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0))                                           \n");//(M-1)월누계 실적
            sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0))                                         \n");//(M-1)월누계 차이
            sb.append("                   , SUM(NVL(C.PLAN_AMT,0))                                                 \n");//당월 사업계획
            sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0))                                       \n");//당월 비상계획
            sb.append("                   , SUM(NVL(C.BD_AMT,0))                                                   \n");//당월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0))                                            \n");//(M+1)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0))                                  \n");//(M+1)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0))                                              \n");//(M+1)월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0))                                           \n");//(M+2)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0))                                 \n");//(M+2)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0))                                             \n");//(M+2)월 비용예산

            sb.append("               FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F                \n");
            sb.append("                          ( P_LEDGER_ID =>    "+ledger_id+"                          \n");
            sb.append("                           ,P_PERIOD_YEAR =>  '"+period_year+"'                      \n");
            sb.append("                           ,P_PERIOD_MONTH => '"+period_month+"'                     \n");
            sb.append("                           ,P_CCTR_CD =>   '"+search_dept_code+"')) C                   \n");

            sb.append("              WHERE ACCT_DIV_NM = '변동비'                                                \n");

            sb.append("              GROUP BY C.LEDGER_ID                                                   \n");
            sb.append("                     , C.PERIOD_YEAR                                                 \n");
            sb.append("                     , C.CCTR_CD                                                  \n");
            sb.append("                     , C.CCTR_NM                                                  \n");
            sb.append("              UNION ALL                                                             \n");

            sb.append("             SELECT C.LEDGER_ID                                                      \n");
            sb.append("                  , C.PERIOD_YEAR                                                    \n");
            sb.append("                  , '"+period_month+"' AS PERIOD_MONTH                               \n");
            sb.append("                  , C.CCTR_CD                                                     \n");
            sb.append("                  , C.CCTR_NM                                                     \n");
            sb.append("                  , NVL(C.ACCT_DIV_NM, 'X') AS ACCT_DIV_NM                       \n");
            sb.append("                  , C.ACCT_CD                                                      \n");
            sb.append("                  , C.ACCT_NM                                                      \n");
            sb.append("                  , '' AS PJT_CD                                               \n");
            sb.append("                  , '' AS PJT_NM                                               \n");
            sb.append("                  , '' AS ITEM_GROUP_CD                                             \n");
            sb.append("                  , '' AS ITEM_GROUP_NM                                             \n");

            sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0)) AS PREV_PLAN_AMT                             \n");//(M-1)월 사업계획
            sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0)) AS PREV_EMERGENCY_PLAN_AMT         \n");//(M-1)월 비상계획
            sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0)) AS PREV_BD_AMT                                 \n");//(M-1)월 비용예산
            sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0)) AS PTD_ACTUAL_AMT                           \n");//(M-1)월 실적
            sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0)) AS PREV_PTD_GAP_AMT                       \n");//(M-1)월 차이
            sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0)) AS PREV_YTD_PLAN_AMT                     \n");//(M-1)월누계 사업계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0)) AS PREV_YTD_EMERGENCY_PLAN_AMT \n");//(M-1)월누계 비상계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0)) AS PREV_YTD_BD_AMT                         \n");//(M-1)월누계 비용예산
            sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0)) AS YTD_ACTUAL_AMT                           \n");//(M-1)월누계 실적
            sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0)) AS PREV_YTD_GAP_AMT                       \n");//(M-1)월누계 차이
            sb.append("                   , SUM(NVL(C.PLAN_AMT,0)) AS PLAN_AMT                                       \n");//당월 사업계획
            sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0)) AS EMERGENCY_PLAN_AMT                   \n");//당월 비상계획
            sb.append("                   , SUM(NVL(C.BD_AMT,0)) AS BD_AMT                                           \n");//당월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0)) AS NEXT_PLAN_AMT                             \n");//(M+1)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0)) AS NEXT_EMERGENCY_PLAN_AMT         \n");//(M+1)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0)) AS NEXT_BD_AMT                                 \n");//(M+1)월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0)) AS NEXT2_PLAN_AMT                           \n");//(M+2)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0)) AS NEXT2_EMERGENCY_PLAN_AMT       \n");//(M+2)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0)) AS NEXT2_BD_AMT                               \n");//(M+2)월 비용예산
                                                    /* AS - IS   GET_PLAN_BD_ACT_F   */
            sb.append("               FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F               \n");
            sb.append("                         ( P_LEDGER_ID =>    "+ledger_id+"                          \n");
            sb.append("                          ,P_PERIOD_YEAR =>  '"+period_year+"'                      \n");
            sb.append("                          ,P_PERIOD_MONTH => '"+period_month+"'                     \n");
            sb.append("                          ,P_CCTR_CD =>   '"+search_dept_code+"')) C                   \n");

            sb.append("              WHERE ACCT_DIV_NM = '고정비'                                                \n");

            sb.append("              GROUP BY C.LEDGER_ID                                                  \n");
            sb.append("                     , C.PERIOD_YEAR                                                \n");
            sb.append("                     , C.CCTR_CD                                                 \n");
            sb.append("                     , C.CCTR_NM                                                 \n");
            sb.append("                     , C.ACCT_DIV_NM                                              \n");
            sb.append("                     , C.ACCT_CD                                                  \n");
            sb.append("                     , C.ACCT_NM                                                  \n");
            sb.append("              UNION ALL                                                             \n");
            sb.append("             SELECT C.LEDGER_ID                                                     \n");
            sb.append("                  , C.PERIOD_YEAR                                                   \n");
            sb.append("                  , '"+period_month+"' AS PERIOD_MONTH                              \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , '고정비 소계'                                                       	  \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                    		  \n");

            sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0))                                            \n");//(M-1)월 사업계획
            sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0))                                  \n");//(M-1)월 비상계획
            sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0))                                              \n");//(M-1)월 비용예산
            sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0))                                           \n");//(M-1)월 실적
            sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0))                                         \n");//(M-1)월 차이
            sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0))                                        \n");//(M-1)월누계 사업계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0))                              \n");//(M-1)월누계 비상계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0))                                          \n");//(M-1)월누계 비용예산
            sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0))                                           \n");//(M-1)월누계 실적
            sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0))                                         \n");//(M-1)월누계 차이
            sb.append("                   , SUM(NVL(C.PLAN_AMT,0))                                                 \n");//당월 사업계획
            sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0))                                       \n");//당월 비상계획
            sb.append("                   , SUM(NVL(C.BD_AMT,0))                                                   \n");//당월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0))                                            \n");//(M+1)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0))                                  \n");//(M+1)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0))                                              \n");//(M+1)월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0))                                           \n");//(M+2)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0))                                 \n");//(M+2)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0))                                             \n");//(M+2)월 비용예산

            sb.append("               FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F                \n");
            sb.append("                          ( P_LEDGER_ID =>    "+ledger_id+"                          \n");
            sb.append("                           ,P_PERIOD_YEAR =>  '"+period_year+"'                      \n");
            sb.append("                           ,P_PERIOD_MONTH => '"+period_month+"'                     \n");
            sb.append("                           ,P_CCTR_CD =>   '"+search_dept_code+"')) C                   \n");

            sb.append("              WHERE ACCT_DIV_NM = '고정비'                                                \n");

            sb.append("              GROUP BY C.LEDGER_ID                                                   \n");
            sb.append("                     , C.PERIOD_YEAR                                                 \n");
            sb.append("                     , C.CCTR_CD                                                  \n");
            sb.append("                     , C.CCTR_NM                                                  \n");
            sb.append("              UNION ALL                                                             \n");
            sb.append("             SELECT C.LEDGER_ID                                                     \n");
            sb.append("                  , C.PERIOD_YEAR                                                   \n");
            sb.append("                  , '"+period_month+"' AS PERIOD_MONTH                              \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , '합계'                                                       	  \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                             \n");
            sb.append("                  , ' '                                                    		  \n");

            sb.append("                   , SUM(NVL(C.PREV_PLAN_AMT,0))                                            \n");//(M-1)월 사업계획
            sb.append("                   , SUM(NVL(C.PREV_EMERGENCY_PLAN_AMT,0))                                  \n");//(M-1)월 비상계획
            sb.append("                   , SUM(NVL(C.PREV_BD_AMT,0))                                              \n");//(M-1)월 비용예산
            sb.append("                   , SUM(NVL(C.PTD_ACTUAL_AMT,0))                                           \n");//(M-1)월 실적
            sb.append("                   , SUM(NVL(C.PREV_PTD_GAP_AMT,0))                                         \n");//(M-1)월 차이
            sb.append("                   , SUM(NVL(C.PREV_YTD_PLAN_AMT,0))                                        \n");//(M-1)월누계 사업계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0))                              \n");//(M-1)월누계 비상계획
            sb.append("                   , SUM(NVL(C.PREV_YTD_BD_AMT,0))                                          \n");//(M-1)월누계 비용예산
            sb.append("                   , SUM(NVL(C.YTD_ACTUAL_AMT,0))                                           \n");//(M-1)월누계 실적
            sb.append("                   , SUM(NVL(C.PREV_YTD_GAP_AMT,0))                                         \n");//(M-1)월누계 차이
            sb.append("                   , SUM(NVL(C.PLAN_AMT,0))                                                 \n");//당월 사업계획
            sb.append("                   , SUM(NVL(C.EMERGENCY_PLAN_AMT,0))                                       \n");//당월 비상계획
            sb.append("                   , SUM(NVL(C.BD_AMT,0))                                                   \n");//당월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT_PLAN_AMT,0))                                            \n");//(M+1)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT_EMERGENCY_PLAN_AMT,0))                                  \n");//(M+1)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT_BD_AMT,0))                                              \n");//(M+1)월 비용예산
            sb.append("                   , SUM(NVL(C.NEXT2_PLAN_AMT,0))                                           \n");//(M+2)월 사업계획
            sb.append("                   , SUM(NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0))                                 \n");//(M+2)월 비상계획
            sb.append("                   , SUM(NVL(C.NEXT2_BD_AMT,0))                                             \n");//(M+2)월 비용예산

            sb.append("               FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F                \n");
            sb.append("                          ( P_LEDGER_ID =>    "+ledger_id+"                          \n");
            sb.append("                           ,P_PERIOD_YEAR =>  '"+period_year+"'                      \n");
            sb.append("                           ,P_PERIOD_MONTH => '"+period_month+"'                     \n");
            sb.append("                           ,P_CCTR_CD =>   '"+search_dept_code+"')) C                   \n");

            sb.append("              GROUP BY C.LEDGER_ID                                                   \n");
            sb.append("                     , C.PERIOD_YEAR                                                 \n");
            sb.append("                     , C.CCTR_CD                                                  \n");
            sb.append("                     , C.CCTR_NM                                                  \n");
            sb.append("            ) T                                                                      \n");
//            sb.append("      ORDER BY T.ACCT_DIV_NM DESC, T.ACCT_CD                                     \n");

            Query query = entityManager.createNativeQuery(sb.toString());

            draftListResult.setList(new JpaResultMapper().list(query, CostBudgetDto.class));

        } else {


            // 전표 조회

            sb.append("    SELECT 	COMP_CD,							\n");
            sb.append("    		SLIP_NO ,							\n");
            sb.append("    		SLIP_HEADER_ID ,							\n");
            sb.append("    		NVL(EMP_NO,REG_ID) AS EMP_NO ,							\n");
            sb.append("    		NVL((SELECT EMP_NM FROM TB_MST_EMP B WHERE B.EMP_NO = A.EMP_NO), (SELECT EMP_NM FROM TB_MST_EMP B WHERE B.EMP_NO = A.REG_ID)) AS EMP_NM,							\n");
            sb.append("    		DEPT_NO,							\n");
            sb.append("    		(SELECT DEPT_NM FROM TB_MST_CCTR_BLG_ERP B WHERE B.DEPT_CD = A.DEPT_NO) AS DEPT_NM,							\n");
            sb.append("    		(SELECT DISTINCT CCTR_CD FROM TB_BUDGET_HD B WHERE B.SLIP_NO = A.SLIP_NO) AS CCTR_CD,							\n");
            sb.append("    		(SELECT DISTINCT CCTR_NM FROM TB_BUDGET_HD B WHERE B.SLIP_NO = A.SLIP_NO) AS CCTR_NM,							\n");
            sb.append("    		(SELECT DISTINCT PERIOD_YEAR FROM TB_BUDGET_HD B WHERE B.SLIP_NO = A.SLIP_NO) AS PERIOD_YEAR,							\n");
            sb.append("    		(SELECT DISTINCT PERIOD_MONTH FROM TB_BUDGET_HD B WHERE B.SLIP_NO = A.SLIP_NO) AS PERIOD_MONTH,							\n");
            sb.append("    		HEADER_REMARK,							\n");
            sb.append("    		REMARK,							\n");
            sb.append("    		POSTING_DT							\n");
            sb.append(" 	 	   , (SELECT COUNT(*) FROM U_FILE WHERE COMP_CD = A.COMP_CD AND DOCUMENT_H_ID = A.SLIP_NO) AS FILE_CNT												 ");
            sb.append(" 	 	   , (CASE WHEN (DOC_TITLE IS NULL OR DOC_TITLE = '') THEN 0                                                                 ");
            sb.append(" 	 	           WHEN NVL(REGEXP_COUNT(DOC_TITLE , ','), 0) > 0 THEN NVL(REGEXP_COUNT(DOC_TITLE , ','), 0)+1                       ");
            sb.append(" 	 	           ELSE 1 END                                                                                                        ");
            sb.append("               ) AS JINI_CNT											                         ");
            sb.append("    FROM TB_SLIP_HD A							\n");
            sb.append("    WHERE COMP_CD = :compCd							\n");
            sb.append("      AND SLIP_TYPE = :slipType							\n");
            sb.append("      AND SLIP_NO = :slipNo							\n");
            sb.append("      AND SLIP_HEADER_ID = :slipHeaderId							\n");

            Query query2 = entityManager.createNativeQuery(sb.toString());
            query2.setParameter("compCd", compCd);
            query2.setParameter("slipType", slip_type);
            query2.setParameter("slipNo", slip_no);
            query2.setParameter("slipHeaderId", slip_header_id);

            draftListResult.setData(new JpaResultMapper().list(query2, CostBudgetDto.class));



            sb = new StringBuilder();
            sb.append("         SELECT T.LEDGER_ID                                                                                              \n");
            sb.append("              , T.PERIOD_YEAR                                                                                            \n");
            sb.append("              , T.PERIOD_MONTH                                                                                           \n");
            sb.append("              , T.CCTR_CD                                                                                             \n");
            sb.append("              , T.CCTR_NM                                                                                             \n");
            sb.append("              , T.ACCT_DIV_NM                                                                                          \n");
            sb.append("              , T.ACCT_CD                                                                                              \n");
            sb.append("              , T.ACCT_NM                                                                                              \n");
            sb.append("              , T.PJT_CD                                                                                           \n");
            sb.append("              , T.PJT_NM                                                                                           \n");
            sb.append("              , T.ITEM_GROUP_CD                                                                                         \n");
            sb.append("              , T.ITEM_GROUP_NM                                                                                         \n");
            sb.append("              , T.PLAN_PRE_AMT                                                                                        \n");
            sb.append("              , T.EMCY_PLAN_PRE_AMT                                                                                   \n");
            sb.append("              , T.BUDGET_PRE_AMT                                                                                      \n");
            sb.append("              , T.ACTUAL_PRE_AMT                                                                                      \n");
            sb.append("              , T.VARI_AMT                                                                                            \n");
            sb.append("              , T.PLAN_ACCM_AMT                                                                                       \n");
            sb.append("              , T.EMCY_PLAN_ACCM_AMT                                                                                  \n");
            sb.append("              , T.BUDGET_ACCM_AMT                                                                                     \n");
            sb.append("              , T.ACTUAL_ACCM_AMT                                                                                     \n");
            sb.append("              , T.ACCM_VARI_AMT                                                                                       \n");
            sb.append("              , T.PLAN_AMT                                                                                            \n");
            sb.append("              , T.EMCY_PLAN_AMT                                                                                       \n");
            sb.append("              , T.BUDGET_AMT                                                                                          \n");
            sb.append("              , T.PLAN_NEXT_AMT                                                                                       \n");
            sb.append("              , T.EMCY_PLAN_NEXT_AMT                                                                                  \n");
            sb.append("              , T.BUDGET_NEXT_AMT                                                                                     \n");
            sb.append("              , T.PLAN_NEXT2_AMT                                                                                      \n");
            sb.append("              , T.EMCY_PLAN_NEXT2_AMT                                                                                 \n");
            sb.append("              , T.BUDGET_NEXT2_AMT                                                                                    \n");
            sb.append("           FROM (                                                                                                        \n");
            sb.append("                 SELECT DH.LEDGER_ID                                                                                     \n");
            sb.append("                      , DH.PERIOD_YEAR                                                                                   \n");
            sb.append("                      , DH.PERIOD_MONTH                                                                                  \n");
            sb.append("                      , DH.CCTR_CD                                                                                    \n");
            sb.append("                      , DH.CCTR_NM                                                                                    \n");
            sb.append("                      , NVL(DH.ACCT_DIV_NM, 'X') AS ACCT_DIV_NM                                                      \n");
            sb.append("                      , DH.ACCT_CD                                                                                     \n");
            sb.append("                      , DH.ACCT_NM                                                                                     \n");
            sb.append("                      , '' AS PJT_CD                                                                                  \n");
            sb.append("                      , '' AS PJT_NM                                                                                  \n");
            sb.append("                      , '' AS ITEM_GROUP_CD                                                                                \n");
            sb.append("                      , '' AS ITEM_GROUP_NM                                                                                \n");

            sb.append("                      , SUM(NVL(DH.PREV_PLAN_AMT,0)) AS PLAN_PRE_AMT                      \n");
            sb.append("                      , SUM(NVL(DH.PREV_EMERGENCY_PLAN_AMT,0)) AS EMCY_PLAN_PRE_AMT       \n");
            sb.append("                      , SUM(NVL(DH.PREV_BD_AMT,0)) AS BUDGET_PRE_AMT                      \n");
            sb.append("                      , SUM(NVL(DH.PTD_ACTUAL_AMT,0)) AS ACTUAL_PRE_AMT                   \n");
            sb.append("                      , SUM(NVL(DH.PREV_PTD_GAP_AMT,0)) AS VARI_AMT                       \n");

            sb.append("                      , SUM(NVL(DH.PREV_YTD_PLAN_AMT,0)) AS PLAN_ACCM_AMT                 \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_EMERGENCY_PLAN_AMT,0)) AS EMCY_PLAN_ACCM_AMT  \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_BD_AMT,0)) AS BUDGET_ACCM_AMT                 \n");
            sb.append("                      , SUM(NVL(DH.YTD_ACTUAL_AMT,0)) AS ACTUAL_ACCM_AMT                  \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_GAP_AMT,0)) AS ACCM_VARI_AMT                  \n");

            sb.append("                      , SUM(NVL(DH.PLAN_AMT,0)) AS PLAN_AMT                               \n");
            sb.append("                      , SUM(NVL(DH.EMERGENCY_PLAN_AMT,0)) AS EMCY_PLAN_AMT                \n");
            sb.append("                      , SUM(NVL(DH.BD_AMT,0)) AS BUDGET_AMT                               \n");

            sb.append("                      , SUM(NVL(DH.NEXT_PLAN_AMT,0)) AS PLAN_NEXT_AMT                     \n");
            sb.append("                      , SUM(NVL(DH.NEXT_EMERGENCY_PLAN_AMT,0)) AS EMCY_PLAN_NEXT_AMT      \n");
            sb.append("                      , SUM(NVL(DH.NEXT_BD_AMT,0)) AS BUDGET_NEXT_AMT                     \n");

            sb.append("                      , SUM(NVL(DH.NEXT2_PLAN_AMT,0)) AS PLAN_NEXT2_AMT                   \n");
            sb.append("                      , SUM(NVL(DH.NEXT2_EMERGENCY_PLAN_AMT,0)) AS EMCY_PLAN_NEXT2_AMT    \n");
            sb.append("                      , SUM(NVL(DH.NEXT2_BD_AMT,0)) AS BUDGET_NEXT2_AMT                   \n");
            sb.append("                   FROM TB_BUDGET_HD DH                                                                                         \n");
            sb.append("                  WHERE 1 = 1                                                                                            \n");
            sb.append("                     AND DH.COMP_CD = :compCd                                                                              \n");
            sb.append("                     AND DH.LEDGER_ID = :ledgerId                                                                                 \n");
            sb.append("                     AND DH.SLIP_NO = :slipNo                                                                                   \n");
            sb.append("                     AND DH.SLIP_HEADER_ID = :slipHeaderId                                                                            \n");
            sb.append("                  GROUP BY DH.LEDGER_ID                                                                                  \n");
            sb.append("                       , DH.PERIOD_YEAR                                                                                  \n");
            sb.append("                       , DH.PERIOD_MONTH                                                                                 \n");
            sb.append("                       , DH.CCTR_CD                                                                                   \n");
            sb.append("                       , DH.CCTR_NM                                                                                   \n");
            sb.append("                       , NVL(DH.ACCT_DIV_NM, 'X')				                                                       \n");
            sb.append("                       , DH.ACCT_CD                                                                                    \n");
            sb.append("                       , DH.ACCT_NM                                                                                    \n");
            sb.append("                  UNION ALL                                                                                              \n");
            sb.append("                 SELECT DH.LEDGER_ID                                                                                     \n");
            sb.append("                      , DH.PERIOD_YEAR                                                                                   \n");
            sb.append("                      , DH.PERIOD_MONTH                                                                                  \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , '합계'                                                                                       	   \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");
            sb.append("                      , ' '                                                                                              \n");

            sb.append("                      , SUM(NVL(DH.PREV_PLAN_AMT,0))                                    \n");
            sb.append("                      , SUM(NVL(DH.PREV_EMERGENCY_PLAN_AMT,0))                          \n");
            sb.append("                      , SUM(NVL(DH.PREV_BD_AMT,0))                                      \n");
            sb.append("                      , SUM(NVL(DH.PTD_ACTUAL_AMT,0))                                   \n");
            sb.append("                      , SUM(NVL(DH.PREV_PTD_GAP_AMT,0))                                 \n");

            sb.append("                      , SUM(NVL(DH.PREV_YTD_PLAN_AMT,0))                                \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_EMERGENCY_PLAN_AMT,0))                      \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_BD_AMT,0))                                  \n");
            sb.append("                      , SUM(NVL(DH.YTD_ACTUAL_AMT,0))                                   \n");
            sb.append("                      , SUM(NVL(DH.PREV_YTD_GAP_AMT,0))                                 \n");

            sb.append("                      , SUM(NVL(DH.PLAN_AMT,0))                                         \n");
            sb.append("                      , SUM(NVL(DH.EMERGENCY_PLAN_AMT,0))                               \n");
            sb.append("                      , SUM(NVL(DH.BD_AMT,0))                                           \n");

            sb.append("                      , SUM(NVL(DH.NEXT_PLAN_AMT,0))                                    \n");
            sb.append("                      , SUM(NVL(DH.NEXT_EMERGENCY_PLAN_AMT,0))                          \n");
            sb.append("                      , SUM(NVL(DH.NEXT_BD_AMT,0))                                      \n");

            sb.append("                      , SUM(NVL(DH.NEXT2_PLAN_AMT,0))                                   \n");
            sb.append("                      , SUM(NVL(DH.NEXT2_EMERGENCY_PLAN_AMT,0))                         \n");
            sb.append("                      , SUM(NVL(DH.NEXT2_BD_AMT,0))                                     \n");
            sb.append("                   FROM TB_BUDGET_HD DH                                                                                         \n");
            sb.append("                  WHERE 1 = 1                                                                                            \n");
            sb.append("                     AND DH.COMP_CD = :compCd                                                                              \n");
            sb.append("                     AND DH.LEDGER_ID = :ledgerId                                                                                 \n");
            sb.append("                     AND DH.SLIP_NO = :slipNo                                                                                   \n");
            sb.append("                     AND DH.SLIP_HEADER_ID = :slipHeaderId                                                                            \n");
            sb.append("                  GROUP BY DH.LEDGER_ID                                                                                  \n");
            sb.append("                         , DH.PERIOD_YEAR                                                                                \n");
            sb.append("                         , DH.PERIOD_MONTH                                                                               \n");
            sb.append("                ) T                                                                                                      \n");
            sb.append("          ORDER BY T.ACCT_DIV_NM DESC, T.ACCT_CD                                                                     \n");

            Query query = entityManager.createNativeQuery(sb.toString());

            query.setParameter("compCd", compCd);
            query.setParameter("ledgerId", ledgerId);
            query.setParameter("slipNo", slip_no);
            query.setParameter("slipHeaderId", slip_header_id);

            draftListResult.setList(new JpaResultMapper().list(query, CostBudgetDto.class));

//            sb = new StringBuilder();
//
//            sb.append(" SELECT C.SLIP_NO												\n");
//            sb.append("  , C.SLIP_HEADER_ID										\n");
//            sb.append("  , C.APPROVAL_GROUP_ID										\n");
//            sb.append("  , C.HEADER_REMARK	         								\n");
//            sb.append("  , C.USER_ID												\n");
//            sb.append("  , C.USER_NM AS USER_NM  								 \n");
//            sb.append("  , C.POSTING_DT                   						\n");
//            sb.append("  , C.SCAN_NO                         					\n");
//            sb.append("  , C.REMARK  											\n");
//            sb.append("  , C.NEXT_APP_USER_ID									\n");
//            sb.append("  , (SELECT EMP_NM FROM TB_MST_EMP TME WHERE TME.EMP_NO = C.NEXT_APP_USER_ID) AS NEXT_APP_USER_NM	\n");
//            sb.append("  , C.DOC_TITLE												\n");
//            sb.append("  , C.DOC_URL												\n");
//            sb.append("  FROM (SELECT SLGL.SLIP_NO                        						\n");//1
//            sb.append("		 , SLGL.SLIP_HEADER_ID										\n");//2
//            sb.append("		 , SLGL.APPROVAL_GROUP_ID										\n");//2
//            sb.append("		 , SLGL.HEADER_REMARK	         								\n");//3,
//            sb.append("		 , SLGL.REG_ID AS USER_ID            					\n");//4
//            sb.append("		 , (SELECT EMP_NM FROM TB_MST_EMP TME WHERE TME.EMP_NO = SLGL.REG_ID) AS USER_NM	\n");//4
//            sb.append("		 , SLGL.POSTING_DT                   						\n");//8
//            sb.append("		 , SLGL.SCAN_NO                         					\n");//9
//            sb.append("		 , SLGL.REMARK                         						\n");//10
//            sb.append("   	 , (SELECT ALN.REG_ID 									\n");
//            sb.append("   	 	  FROM TB_APPR_HD AGL										\n");
//            sb.append("   		     , TB_APPR_DT ALN										\n");
//            sb.append("   		 WHERE AGL.COMP_CD = ALN.COMP_CD				\n");
//            sb.append("   		   AND AGL.APPR_GROUP_ID = ALN.APPR_GROUP_ID	\n");
//            sb.append("   		   AND AGL.NEXT_STAGE = ALN.APPR_STAGE						\n");
//            sb.append("   		   AND AGL.COMP_CD = SLGL.COMP_CD				\n");
//            sb.append("   		   AND AGL.APPR_GROUP_ID = SLGL.APPROVAL_GROUP_ID	\n");
//            sb.append("   		   AND ALN.APPR_TYPE_CD='02') AS NEXT_APP_USER_ID				\n");//15
//            sb.append(" 	  	 , SLGL.DOC_TITLE								 			\n");//16
//            sb.append(" 	  	 , SLGL.DOC_URL												\n");//17
//            sb.append("	  FROM TB_SLIP_HD SLGL	                            				\n");
//            sb.append("   WHERE 1 = 1													\n");
//            sb.append("     AND SLGL.SLIP_TYPE = :slipType              			\n");
//            sb.append("     AND SLGL.COMP_CD = :compCd						\n");
//            sb.append("     AND SLGL.SLIP_NO = :slipNo							\n");
//            sb.append("     AND SLGL.SLIP_HEADER_ID = :slipHeaderId) C						\n");
//

            sb = new StringBuilder();

            sb.append("  SELECT BD.COMP_CD                                                      \n");
            sb.append("       , BD.LEDGER_ID                                                         \n");
            sb.append("       , BD.SLIP_NO                                                           \n");
            sb.append("       , BD.SLIP_HEADER_ID                                                    \n");
            sb.append("       , BD.PERIOD_YEAR                                                       \n");
            sb.append("       , BD.PERIOD_MONTH                                                      \n");
            sb.append("       , BD.CCTR_CD                                                        \n");
            sb.append("       , BD.CCTR_NM                                                        \n");
            sb.append("       , BD.ACCT_DIV_NM                                                     \n");
            sb.append("       , BD.ACCT_CD                                                         \n");
            sb.append("       , BD.ACCT_NM                                                         \n");
            sb.append("       , BD.PAST_OVER_AMT                                                     \n");
            sb.append("       , BD.PAST_OVER_REASON                                                  \n");
            sb.append("       , BD.CUR_OVER_AMT                                                      \n");
            sb.append("       , BD.CUR_OVER_REASON                                                   \n");
//            sb.append("       , BD.REG_DTM                                                          \n");
//            sb.append("       , BD.REG_ID                                                          \n");
//            sb.append("       , BD.CHG_DTM                                                       \n");
//            sb.append("       , BD.CHG_ID                                                    \n");
//            sb.append("       , (SELECT LGL.DOC_TITLE                                             \n");
//            sb.append("            FROM TB_SLIP_HD LGL                                                 \n");
//            sb.append("           WHERE 1 = 1                                                     \n");
//            sb.append("             AND LGL.COMP_CD = BD.COMP_CD                        \n");
//            sb.append("             AND LGL.LEDGER_ID = BD.LEDGER_ID                              \n");
//            sb.append("             AND LGL.SLIP_NO = BD.SLIP_NO                                  \n");
//            sb.append("             AND LGL.SLIP_HEADER_ID = BD.SLIP_HEADER_ID) AS DOC_TITLE      \n");
//            sb.append("       , (SELECT LGL.DOC_URL                                               \n");
//            sb.append("            FROM TB_SLIP_HD LGL                                                 \n");
//            sb.append("           WHERE 1 = 1                                                     \n");
//            sb.append("             AND LGL.COMP_CD = BD.COMP_CD                        \n");
//            sb.append("             AND LGL.LEDGER_ID = BD.LEDGER_ID                              \n");
//            sb.append("             AND LGL.SLIP_NO = BD.SLIP_NO                                  \n");
//            sb.append("             AND LGL.SLIP_HEADER_ID = BD.SLIP_HEADER_ID) AS DOC_URL        \n");
            sb.append("    FROM TB_BUDGET_DT BD                                                          \n");
            sb.append("   WHERE 1 =1                                                              \n");
            sb.append("	  AND BD.LEDGER_ID = :ledgerId          \n");
            sb.append("	  AND BD.SLIP_NO = :slipNo            \n");
            sb.append("	  AND BD.SLIP_HEADER_ID = :slipHeaderId     \n");
            sb.append("  ORDER BY BD.CHG_DTM			\n");

            Query query3 = entityManager.createNativeQuery(sb.toString());

            query3.setParameter("ledgerId", ledgerId);
            query3.setParameter("slipNo", slip_no);
            query3.setParameter("slipHeaderId", slip_header_id);

            draftListResult.setDtList(new JpaResultMapper().list(query3, CostBudgetDto.class));

        }

        return draftListResult;
    }

    @Override
    public CostBudgetDto doDraftSave(DraftResult draftResult) throws Exception {

        CostBudgetDto data = draftResult.getData();
        List<CostBudgetDto> subData = draftResult.subData;
//        List<CostBudgetDto> apprLine = draftResult.apprLine;

        //반환값 저장
        CostBudgetDto returnDto = new CostBudgetDto();

        String ledger_id			= util.getLoginUser().getEmployee().getLedgerId();

        BigDecimal ledgerId = BigDecimal.valueOf(Long.parseLong(ledger_id));

        String slip_no				= data.getSlipNo();
        String slip_header_id		= String.valueOf(data.getSlipHeaderId());

        BigDecimal slipHeaderId;

        String approval_group_id	= String.valueOf(data.getApprovalGroupId());

        BigDecimal approvalGroupId;

        String slip_type			= data.getSlipType();
        String scan_no				= data.getScanNo();
        String posting_date			= data.getPostDt();
        String period_year			= data.getPeriodYear();
        String period_month			= data.getPeriodMonth();
        String compCd = util.getLoginCompCd();

        String user_id				= String.valueOf(data.getUserId());
        String user_name			= data.getUserNm();
        String dept_code			= data.getDeptCd();
        String dept_name			= data.getDeptNm();
        String bdept_code			= data.getCctrCd();
        String bdept_name			= data.getCctrNm();
        String header_text			= data.getHeaderRemark();
        String ref_user_id			= data.getRefUserId();
        String bigo					= data.getRemark();

        String doc_title			= data.getDocTitle();
        String doc_url				= data.getDocUrl();
        String doc_no 				= data.getDocNo();

        // 월 형식 변환
        int pm_int = Integer.parseInt(period_month);
        if((pm_int) < 10){
            period_month = "0" + (pm_int);
        }else{
            period_month = (pm_int) + "";
        }

        if("".equals(slip_header_id)) {
            slipHeaderId = slipHeaderRepository.getSlipHeaderId();
        }else if("null".equals(slip_header_id)){
            slipHeaderId = slipHeaderRepository.getSlipHeaderId();
        }else{
            slipHeaderId = data.getSlipHeaderId();
        }

        if("".equals(slip_header_id)) {
            approvalGroupId = slipHeaderId;
        }else if("null".equals(slip_header_id)){
            approvalGroupId = slipHeaderId;
        }else{
            approvalGroupId = data.getApprovalGroupId();
        }

        if("".equals(slip_no)) {
            slip_no = slipHeaderRepository.callGetSlipNumber("BOB", util.getLoginCompCd());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("  SELECT COUNT(*) AS CNT	                \n");
        sb.append("  FROM TB_SLIP_HD	                            \n");
        sb.append("  WHERE 1 = 1                             \n");
        sb.append("  AND COMP_CD = :compCd	\n");
        sb.append("  AND ERP_INVOICE_ID = :erpInvoiceId	\n");
        sb.append("  AND SLIP_NO = :slipNo			\n");
        sb.append("  AND SLIP_HEADER_ID = :slipHeaderId	\n");
        sb.append("  AND SLIP_TYPE = :slipType		\n");
        sb.append("  AND STATUS <> :status			\n");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("erpInvoiceId", period_year+period_month+"_"+bdept_code);
        query.setParameter("slipNo", slip_no);
        query.setParameter("slipHeaderId", slipHeaderId);
        query.setParameter("slipType", "90");
        query.setParameter("status", "SD");

        BigDecimal sf = new JpaResultMapper().uniqueResult(query, CostBudgetDto.class).getCount();

        if(sf.intValue() == 0) {

            sb = new StringBuilder();

            sb.append("        SELECT '"+compCd+"' AS COMPANY_CODE                    \n");//1
            sb.append("             , C.LEDGER_ID                                           \n");//2
            sb.append("             , '"+slip_no+"' AS SLIP_NO                              \n");//3
            sb.append("             , TO_NUMBER('"+slipHeaderId+"') AS SLIP_HEADER_ID       \n");//4
            sb.append("             , TO_CHAR(C.PERIOD_YEAR) AS PERIOD_YEAR                 \n");//5
            sb.append("             , '"+period_month+"' AS PERIOD_MONTH                    \n");//6
            sb.append("             , C.CCTR_CD                                          \n");//7
            sb.append("             , C.CCTR_NM                                          \n");//8
            sb.append("             , C.ACCT_DIV_NM                                       \n");//9
            sb.append("             , C.ACCT_CD                                           \n");//10
            sb.append("             , C.ACCT_NM                                           \n");//11
            sb.append("             , C.PJT_CD                                        \n");//12
            sb.append("             , C.PJT_NM                                        \n");//13
            sb.append("             , C.ITEM_GROUP_CD                                      \n");//14
            sb.append("             , C.ITEM_GROUP_NM                                      \n");//15

            sb.append("             , NVL(C.PREV_PLAN_AMT,0)                             \n");//16
            sb.append("             , NVL(C.PREV_EMERGENCY_PLAN_AMT,0)                   \n");//17
            sb.append("             , NVL(C.PREV_BD_AMT,0)                               \n");//18
            sb.append("             , NVL(C.PTD_ACTUAL_AMT,0)                            \n");//19
            sb.append("             , NVL(C.PREV_PTD_GAP_AMT,0)                          \n");//20

            sb.append("             , NVL(C.PREV_YTD_PLAN_AMT,0)                         \n");//21
            sb.append("             , NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0)               \n");//22
            sb.append("             , NVL(C.PREV_YTD_BD_AMT,0)                           \n");//23
            sb.append("             , NVL(C.YTD_ACTUAL_AMT,0)                            \n");//24
            sb.append("             , NVL(C.PREV_YTD_GAP_AMT,0)                          \n");//25

            sb.append("             , NVL(C.PLAN_AMT,0)                                  \n");//26
            sb.append("             , NVL(C.EMERGENCY_PLAN_AMT,0)                        \n");//27
            sb.append("             , NVL(C.BD_AMT,0)                                    \n");//28

            sb.append("             , NVL(C.NEXT_PLAN_AMT,0)                             \n");//29
            sb.append("             , NVL(C.NEXT_EMERGENCY_PLAN_AMT,0)                   \n");//30
            sb.append("             , NVL(C.NEXT_BD_AMT,0)                               \n");//31

            sb.append("             , NVL(C.NEXT2_PLAN_AMT,0)                            \n");//32
            sb.append("             , NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0)                  \n");//33
            sb.append("             , NVL(C.NEXT2_BD_AMT,0)                              \n");//34


            sb.append("         FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F \n");
            sb.append("                    ( P_LEDGER_ID =>    "+ledger_id+"                \n");
            sb.append("                     ,P_PERIOD_YEAR =>  '"+period_year+"'            \n");
            sb.append("                     ,P_PERIOD_MONTH => '"+period_month+"'           \n");
            sb.append("                     ,P_CCTR_CD =>   '"+bdept_code+"')) C        \n");

            Query query1 = entityManager.createNativeQuery(sb.toString());

            List<CostBudgetDto> budgetHdList = new JpaResultMapper().list(query1, CostBudgetDto.class);

            for(CostBudgetDto hd : budgetHdList){
                BudgetHd budgetHd = new BudgetHd().builder()
                        .compCd(compCd)
                        .ledgerId(ledgerId)
                        .slipNo(slip_no)
                        .slipHeaderId(slipHeaderId)
                        .periodYear(period_year)
                        .periodMonth(period_month)
                        .cctrCd(hd.getCctrCd())
                        .cctrNm(hd.getCctrNm())
                        .acctDivNm(hd.getAcctDivNm())
                        .acctCd(hd.getAcctCd())
                        .acctNm(hd.getAcctNm())
                        .pjtCd(hd.getPjtCd())
                        .pjtNm(hd.getPjtNm())
                        .itemGroupCd(hd.getItemGroupCd())
                        .itemGroupNm(hd.getItemGroupNm())
                        .prevPlanAmt(hd.getPrevPlanAmt())
                        .prevEmergencyPlanAmt(hd.getPrevEmergencyPlanAmt())
                        .prevBdAmt(hd.getPrevBdAmt())
                        .ptdActualAmt(hd.getPtdActualAmt())
                        .prevPtdGapAmt(hd.getPrevPtdGapAmt())
                        .prevYtdPlanAmt(hd.getPrevYtdPlanAmt())
                        .prevYtdEmergencyPlanAmt(hd.getPrevYtdEmergencyPlanAmt())
                        .prevYtdBdAmt(hd.getPrevYtdBdAmt())
                        .ytdActualAmt(hd.getYtdActualAmt())
                        .prevYtdGapAmt(hd.getPrevYtdGapAmt())
                        .planAmt(hd.getPlanAmt())
                        .emergencyPlanAmt(hd.getEmergencyPlanAmt())
                        .bdAmt(hd.getBdAmt())
                        .nextPlanAmt(hd.getNextPlanAmt())
                        .nextEmergencyPlanAmt(hd.getNextEmergencyPlanAmt())
                        .nextBdAmt(hd.getNextBdAmt())
                        .next2PlanAmt(hd.getNext2PlanAmt())
                        .next2EmergencyPlanAmt(hd.getNext2EmergencyPlanAmt())
                        .next2BdAmt(hd.getNext2BdAmt())
                        .build();

                budgetHdRepository.save(budgetHd);

            }


            for(CostBudgetDto dt : subData){

                int arrayCnt = 0;
                int attributeCheckCnt = 0;

                String 	l_company_code		=	compCd;
                String 	l_ledger_id			=	ledger_id;
                String 	l_slip_no	 		=	dt.getSlipNo();
                BigDecimal 	l_slip_header_id	=	dt.getSlipHeaderId();
                String 	l_period_year		=	dt.getPeriodYear();
                String 	l_period_month		=	dt.getPeriodMonth();
                String 	l_bdept_code		=	dt.getCctrCd();
                String 	l_dept_code			=	dt.getDeptCd();
                String 	l_acct_code			=	dt.getAcctCd();
                String 	l_acct_name			=	dt.getAcctNm();
                String 	l_past_over_amt		=	dt.getPastOverAmt();
                String 	l_past_over_reason	=	dt.getPastOverReason();
                String 	l_cur_over_amt		=	dt.getCurOverAmt();
                String 	l_cur_over_reason	=	dt.getCurOverReason();

                BudgetDt budgetDt = new BudgetDt().builder()
                        .compCd(compCd)
                        .ledgerId(ledgerId)
                        .slipNo(slip_no)
                        .slipHeaderId(slipHeaderId)
                        .periodYear(period_year)
                        .periodMonth(period_month)
                        .cctrCd(l_bdept_code)
                        .deptCd(l_dept_code)
                        .acctCd(l_acct_code)
                        .acctNm(l_acct_name)
                        .pastOverAmt(l_past_over_amt)
                        .pastOverReason(l_past_over_reason)
                        .curOverAmt(l_cur_over_amt)
                        .curOverReason(l_cur_over_reason)
                        .build();

                budgetDtRepository.save(budgetDt);
            }

            SlipHeader slipHeader = new SlipHeader().builder()
                    .compCd(compCd)
                    .ledgerId(ledgerId)
                    .slipNo(slip_no)
                    .slipHeaderId(slipHeaderId)
                    .empNo(user_id)
                    .deptNo(dept_code)
                    .slipType(slip_type)
                    .usedCur("KRW")
                    .status("SV")
                    .postingDt(posting_date)
                    .approvalGroupId(approvalGroupId)
                    .scanNo(scan_no)
                    .erpInvoiceId(period_year+period_month+"_"+bdept_code)
                    .headerRemark(header_text)
                    .remark(bigo)
                    .docTitle(doc_title)
                    .docUrl(doc_url)
                    .build();


            slipHeaderRepository.save(slipHeader);

        }else{
            // 수정

            // 기 작성분 헤더 삭제
            budgetHdRepository.deleteBySlipNoAndSlipHeaderId(slip_no,slipHeaderId);

            // 기 작성분 존재 여부 확인
            Integer count =  budgetDtRepository.countBySlipNoAndSlipHeaderId(slip_no,slipHeaderId);
            // 기 작성분 라인 삭제
            if(count > 0) budgetDtRepository.deleteBySlipNoAndSlipHeaderId(slip_no,slipHeaderId);

            sb = new StringBuilder();

            sb.append("        SELECT '"+compCd+"' AS COMPANY_CODE                    \n");//1
            sb.append("             , C.LEDGER_ID                                           \n");//2
            sb.append("             , '"+slip_no+"' AS SLIP_NO                              \n");//3
            sb.append("             , TO_NUMBER('"+slipHeaderId+"') AS SLIP_HEADER_ID       \n");//4
            sb.append("             , TO_CHAR(C.PERIOD_YEAR) AS PERIOD_YEAR                 \n");//5
            sb.append("             , '"+period_month+"' AS PERIOD_MONTH                    \n");//6
            sb.append("             , C.CCTR_CD                                          \n");//7
            sb.append("             , C.CCTR_NM                                          \n");//8
            sb.append("             , C.ACCT_DIV_NM                                       \n");//9
            sb.append("             , C.ACCT_CD                                           \n");//10
            sb.append("             , C.ACCT_NM                                           \n");//11
            sb.append("             , C.PJT_CD                                        \n");//12
            sb.append("             , C.PJT_NM                                        \n");//13
            sb.append("             , C.ITEM_GROUP_CD                                      \n");//14
            sb.append("             , C.ITEM_GROUP_NM                                      \n");//15

            sb.append("             , NVL(C.PREV_PLAN_AMT,0)                             \n");//16
            sb.append("             , NVL(C.PREV_EMERGENCY_PLAN_AMT,0)                   \n");//17
            sb.append("             , NVL(C.PREV_BD_AMT,0)                               \n");//18
            sb.append("             , NVL(C.PTD_ACTUAL_AMT,0)                            \n");//19
            sb.append("             , NVL(C.PREV_PTD_GAP_AMT,0)                          \n");//20

            sb.append("             , NVL(C.PREV_YTD_PLAN_AMT,0)                         \n");//21
            sb.append("             , NVL(C.PREV_YTD_EMERGENCY_PLAN_AMT,0)               \n");//22
            sb.append("             , NVL(C.PREV_YTD_BD_AMT,0)                           \n");//23
            sb.append("             , NVL(C.YTD_ACTUAL_AMT,0)                            \n");//24
            sb.append("             , NVL(C.PREV_YTD_GAP_AMT,0)                          \n");//25

            sb.append("             , NVL(C.PLAN_AMT,0)                                  \n");//26
            sb.append("             , NVL(C.EMERGENCY_PLAN_AMT,0)                        \n");//27
            sb.append("             , NVL(C.BD_AMT,0)                                    \n");//28

            sb.append("             , NVL(C.NEXT_PLAN_AMT,0)                             \n");//29
            sb.append("             , NVL(C.NEXT_EMERGENCY_PLAN_AMT,0)                   \n");//30
            sb.append("             , NVL(C.NEXT_BD_AMT,0)                               \n");//31

            sb.append("             , NVL(C.NEXT2_PLAN_AMT,0)                            \n");//32
            sb.append("             , NVL(C.NEXT2_EMERGENCY_PLAN_AMT,0)                  \n");//33
            sb.append("             , NVL(C.NEXT2_BD_AMT,0)                              \n");//34


            sb.append("         FROM TABLE(APPS.CBO_SP_BD_VS_ACT_PKG.GET_PLAN_CCTR_CD_ACT_F \n");
            sb.append("                    ( P_LEDGER_ID =>    "+ledger_id+"                \n");
            sb.append("                     ,P_PERIOD_YEAR =>  '"+period_year+"'            \n");
            sb.append("                     ,P_PERIOD_MONTH => '"+period_month+"'           \n");
            sb.append("                     ,P_CCTR_CD =>   '"+bdept_code+"')) C        \n");

            Query query1 = entityManager.createNativeQuery(sb.toString());

            List<CostBudgetDto> budgetHdList = new JpaResultMapper().list(query1, CostBudgetDto.class);

            CostBudgetDto budgetHdDto = budgetHdList.get(0);

            for(CostBudgetDto hd : budgetHdList){
                BudgetHd budgetHd = new BudgetHd().builder()
                        .compCd(compCd)
                        .ledgerId(ledgerId)
                        .slipNo(slip_no)
                        .slipHeaderId(slipHeaderId)
                        .periodYear(period_year)
                        .periodMonth(period_month)
                        .cctrCd(hd.getCctrCd())
                        .cctrNm(hd.getCctrNm())
                        .acctDivNm(hd.getAcctDivNm())
                        .acctCd(hd.getAcctCd())
                        .acctNm(hd.getAcctNm())
                        .pjtCd(hd.getPjtCd())
                        .pjtNm(hd.getPjtNm())
                        .itemGroupCd(hd.getItemGroupCd())
                        .itemGroupNm(hd.getItemGroupNm())
                        .prevPlanAmt(hd.getPrevPlanAmt())
                        .prevEmergencyPlanAmt(hd.getPrevEmergencyPlanAmt())
                        .prevBdAmt(hd.getPrevBdAmt())
                        .ptdActualAmt(hd.getPtdActualAmt())
                        .prevPtdGapAmt(hd.getPrevPtdGapAmt())
                        .prevYtdPlanAmt(hd.getPrevYtdPlanAmt())
                        .prevYtdEmergencyPlanAmt(hd.getPrevYtdEmergencyPlanAmt())
                        .prevYtdBdAmt(hd.getPrevYtdBdAmt())
                        .ytdActualAmt(hd.getYtdActualAmt())
                        .prevYtdGapAmt(hd.getPrevYtdGapAmt())
                        .planAmt(hd.getPlanAmt())
                        .emergencyPlanAmt(hd.getEmergencyPlanAmt())
                        .bdAmt(hd.getBdAmt())
                        .nextPlanAmt(hd.getNextPlanAmt())
                        .nextEmergencyPlanAmt(hd.getNextEmergencyPlanAmt())
                        .nextBdAmt(hd.getNextBdAmt())
                        .next2PlanAmt(hd.getNext2PlanAmt())
                        .next2EmergencyPlanAmt(hd.getNext2EmergencyPlanAmt())
                        .next2BdAmt(hd.getNext2BdAmt())
                        .build();

                budgetHdRepository.save(budgetHd);
            }


            for(CostBudgetDto dt : subData){

                int arrayCnt = 0;
                int attributeCheckCnt = 0;

                String 	l_company_code		=	compCd;
                String 	l_ledger_id			=	ledger_id;
                String 	l_slip_no	 		=	dt.getSlipNo();
                BigDecimal 	l_slip_header_id	=	dt.getSlipHeaderId();
                String 	l_period_year		=	dt.getPeriodYear();
                String 	l_period_month		=	dt.getPeriodMonth();

                String 	l_bdept_code		=	dt.getCctrCd();
                String 	l_dept_code			=	dt.getDeptCd();
                String 	l_acct_code			=	dt.getAcctCd();
                String 	l_acct_name			=	dt.getAcctNm();
                String 	l_past_over_amt		=	dt.getPastOverAmt();
                String 	l_past_over_reason	=	dt.getPastOverReason();
                String 	l_cur_over_amt		=	dt.getCurOverAmt();
                String 	l_cur_over_reason	=	dt.getCurOverReason();

                BudgetDt budgetDt = new BudgetDt().builder()
                        .compCd(compCd)
                        .ledgerId(ledgerId)
                        .slipNo(slip_no)
                        .slipHeaderId(slipHeaderId)
                        .periodYear(period_year)
                        .periodMonth(period_month)
                        .cctrCd(l_bdept_code)
                        .deptCd(l_dept_code)
                        .acctCd(l_acct_code)
                        .acctNm(l_acct_name)
                        .pastOverAmt(l_past_over_amt)
                        .pastOverReason(l_past_over_reason)
                        .curOverAmt(l_cur_over_amt)
                        .curOverReason(l_cur_over_reason)
                        .build();

                budgetDtRepository.save(budgetDt);
            }

            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCd,slip_no);
            Optional<SlipHeader> slipHeader = slipHeaderRepository.findById(slipHeaderKey);

            String erpInvoiceId = period_year + period_month + "_" + bdept_code;

            // 기 작성된 TB_SLIP_HD 수정
            slipHeader.ifPresent(c -> {
                c.updateBudgetSlip(
                        dept_code
                        , slip_type
                        , "KRW"
                        , "SV"
                        , posting_date
                        , approvalGroupId
                        , scan_no
                        , erpInvoiceId
                        , header_text
                        , bigo
                        , doc_title
                        , doc_url
                );
            });

            }

        data.setSlipNo(slip_no);
        data.setSlipHeaderId(slipHeaderId);
        data.setPeriodYear(period_year);
        data.setPeriodMonth(period_month);

        return data;
    }

    @Override
    public List<AccountDto> getAccountPopList(CostBudgetDto costBudgetDto) {

        String compCd = util.getLoginCompCd();
        String user_id = util.getLoginUserId();
        String ledger_id = util.getLoginUser().getEmployee().getLedgerId();
        String search_bdept_code = costBudgetDto.getDeptCd();
        String search = costBudgetDto.getAcctCd();

        StringBuilder sb = new StringBuilder();

        sb.append("                  SELECT CSV.ATTRIBUTE2                         \n");
        sb.append("                    FROM APPS.CBO_GL_COA_SEGMENT_V CSV          \n");
        sb.append("                   WHERE 1 = 1                                  \n");
        sb.append("                     AND CSV.SEGMENT_NUM = '2'                  \n");
        sb.append("                     AND CSV.LEDGER_ID = :ledger_id                      \n");
        sb.append("                     AND CSV.VALUE_CODE = :search_bdept_code             \n");

        Query query1 = entityManager.createNativeQuery(sb.toString());

        query1.setParameter("ledger_id", ledger_id);
        query1.setParameter("search_bdept_code", search_bdept_code);

        List<CostBudgetDto> sf = new JpaResultMapper().list(query1, CostBudgetDto.class);

        // 공통/판관/제조 구분
        String bdept_gubun = sf.get(0).getAttribute2();

        sb = new StringBuilder();
        sb.append("                  SELECT GAV.ACCT_CODE                                                   			\n");
        sb.append("                       , GAV.ACCT_NAME                                                   			\n");
        sb.append("                    FROM APPS.CBO_GL_ACCT_V GAV                                          			\n");
        sb.append("                   WHERE 1 = 1                                                           			\n");
        sb.append("                     AND GAV.LEDGER_ID = :ledger_id                                               			\n");
        sb.append("                     AND GAV.ENABLED_FLAG = 'Y'                                          			\n");
        sb.append("                 AND (GAV.ACCT_CODE LIKE '53%' OR GAV.ACCT_CODE LIKE '55%' OR GAV.ACCT_CODE LIKE '57%')   \n");
        sb.append("                     AND GAV.ACCT_TYPE = 'E'                                          \n");
        sb.append("                     AND GAV.ACCT_CODE NOT IN (										\n");
        sb.append("                     							 SELECT ACCT_CODE						\n");
        sb.append("                     							   FROM APPS.CBO_BUDGET_ACCT_V AV		\n");
        sb.append("                     							  WHERE 1 = 1							\n");
        sb.append("                     								AND AV.LEDGER_ID = GAV.LEDGER_ID	\n");
        sb.append("                     							)										\n");

        // 부서에 따른 계정 Control
        if(bdept_gubun != null && sf.size()>0) {
            if(bdept_gubun == "M") {
                // 제조부서
                sb.append("                     AND GAV.ATTRIBUTE2 IN ('M', 'C')                                 	\n");
            } else if(bdept_gubun == "S") {
                // 판관부서
                sb.append("                     AND GAV.ATTRIBUTE2 IN ('S', 'C')                                    	\n");
            } else {
                // 공통부서
                sb.append("                     AND GAV.ATTRIBUTE2 IN ('M', 'S', 'C')                            	\n");
            }
        }

        sb.append("                     AND SYSDATE BETWEEN GAV.START_DATE_ACTIVE AND GAV.END_DATE_ACTIVE	\n");
        sb.append("  AND ( GAV.ACCT_CODE LIKE '%' || :search || '%'					        	\n");
        sb.append("  OR GAV.ACCT_NAME LIKE '%' || :search || '%' )				        	\n");
        sb.append(" 	                 ORDER BY GAV.ACCT_CODE					 								\n");

        Query query2 = entityManager.createNativeQuery(sb.toString());
        query2.setParameter("ledger_id", ledger_id);
        query2.setParameter("search", search);


        return new JpaResultMapper().list(query2, AccountDto.class);
    }

    public CostBudgetDto getActualApprYnChk(CostBudgetDto planConfirmList , Integer month){

        CostBudgetDto result = new CostBudgetDto();

        Boolean val = false;
        String msg = "저장하였습니다";

        switch (month) {
            case 1 :
                if("Y".equals(planConfirmList.actual01ApprYn) && "Y".equals(planConfirmList.actual02ApprYn) && "Y".equals(planConfirmList.actual03ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual01ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual02ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual03ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 2 :
                if("Y".equals(planConfirmList.actual02ApprYn) && "Y".equals(planConfirmList.actual03ApprYn) && "Y".equals(planConfirmList.actual04ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual02ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual03ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual04ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 3 :
                if("Y".equals(planConfirmList.actual03ApprYn) && "Y".equals(planConfirmList.actual04ApprYn) && "Y".equals(planConfirmList.actual05ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual03ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual04ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual05ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 4 :
                if("Y".equals(planConfirmList.actual04ApprYn) && "Y".equals(planConfirmList.actual05ApprYn) && "Y".equals(planConfirmList.actual06ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual04ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual05ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual06ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 5 :
                if("Y".equals(planConfirmList.actual05ApprYn) && "Y".equals(planConfirmList.actual06ApprYn) && "Y".equals(planConfirmList.actual07ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual05ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual06ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual07ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 6 :
                if("Y".equals(planConfirmList.actual06ApprYn) && "Y".equals(planConfirmList.actual07ApprYn) && "Y".equals(planConfirmList.actual08ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual06ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual07ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual08ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 7 :
                if("Y".equals(planConfirmList.actual07ApprYn) && "Y".equals(planConfirmList.actual08ApprYn) && "Y".equals(planConfirmList.actual09ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual07ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual08ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual09ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 8 :
                if("Y".equals(planConfirmList.actual08ApprYn) && "Y".equals(planConfirmList.actual09ApprYn) && "Y".equals(planConfirmList.actual10ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual08ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual09ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual10ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 9 :
                if("Y".equals(planConfirmList.actual09ApprYn) && "Y".equals(planConfirmList.actual10ApprYn) && "Y".equals(planConfirmList.actual11ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual09ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual10ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual11ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 10 :
                if("Y".equals(planConfirmList.actual10ApprYn) && "Y".equals(planConfirmList.actual11ApprYn) && "Y".equals(planConfirmList.actual12ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual10ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual11ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual12ApprYn)) {
                        msg = (month+2)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 11 :
                if("Y".equals(planConfirmList.actual11ApprYn) && "Y".equals(planConfirmList.actual12ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual11ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }else if ("N".equals(planConfirmList.actual12ApprYn)) {
                        msg = (month+1)+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
            case 12 :
                if("Y".equals(planConfirmList.actual12ApprYn)){
                    val = true;
                }else{
                    val = false;
                    if("N".equals(planConfirmList.actual12ApprYn)){
                        msg = month+"월의 예산이 이미 확정 되었습니다.";
                    }
                }
                break;
        }

        result.setChkFlag(val);
        result.setMsg(msg);

        return result;
    }

}

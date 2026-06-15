package com.iljin.apiServer.ijeas.ims.pjtPersonnelPlan;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.ims.pjtPersonnelResult.PjtPersonnelResult;
import com.iljin.apiServer.ijeas.ims.pjtPersonnelResult.PjtPersonnelResultRepository;
import com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class PjtPersonnelPlanRepositoryCustomImpl implements PjtPersonnelPlanRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    private final PjtPersonnelPlanRepository pjtPersonnelPlanRepository;

    private final PjtPersonnelResultRepository pjtPersonnelResultRepository;



    /* 가변형 컬럼 조회로 인한 네이티브 쿼리 사용. */
    @Override
    public List<Map<String,Object>> getPersonnelList(PjtPersonnelPlanDto pjtPersonnelPlanDto) {



        String MonthList = String.join(",", pjtPersonnelPlanDto.getParam());

        String user_id = pjtPersonnelPlanDto.getUserId();
        String compCd = util.getLoginCompCd();
        String search_project_manage_no = pjtPersonnelPlanDto.getProjectManageNo();
        String degree = pjtPersonnelPlanDto.getDegree();
        String total_month = String.valueOf(pjtPersonnelPlanDto.param.size());
        int t_month = 0;
        if(!"".equals(total_month)){
            t_month = Integer.parseInt(total_month);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 SELECT 					 \n");
        sb.append(" 	             TO_CHAR(B.ORG_ID) AS ORG_ID 					 \n");
        sb.append(" 	             , B.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	             , B.POSITION_CODE					 \n");
        sb.append(" 	             , (SELECT AMT					 \n");
        sb.append(" 	             	FROM SPJPEM					 \n");
        sb.append(" 	             	WHERE ORG_ID = B.ORG_ID					 \n");
        sb.append(" 	             	AND POSITION_CODE = B.POSITION_CODE					 \n");
        sb.append(" 	             	) AS AMT					 \n");

        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	             , SUM(B.M_A) AS M_A					 \n");
                sb.append(" 	             , SUM(B.EM_A) AS EM_A					 \n");
                sb.append(" 	             , SUM(B.M) AS M					 \n");
                sb.append(" 	             , SUM(B.EM) AS EM					 \n");
            }else{
                sb.append(" 	             , SUM(B.M" + i + "_A) AS M" + i + "_A					 \n");
                sb.append(" 	             , SUM(B.EM" + i + "_A) AS EM" + i + "_A					 \n");
                sb.append(" 	             , SUM(B.M" + i + ") AS M" + i + "					 \n");
                sb.append(" 	             , SUM(B.EM" + i + ") AS EM" + i + "					 \n");
            }
        }
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.M_A");
            }else{
                sb.append(" + B.M" + i + "_A				 \n");
            }
        }
        sb.append(") AS M_A_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.M");
            }else{
                sb.append(" + B.M" + i + "				 \n");
            }
        }
        sb.append(") AS M_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.EM_A");
            }else{
                sb.append(" + B.EM" + i + "_A				 \n");
            }
        }
        sb.append(") AS EM_A_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.EM");
            }else{
                sb.append(" + B.EM" + i + "				 \n");
            }
        }
        sb.append(") AS EM_TOTAL									\n");
        sb.append(" 	 FROM (					 \n");
        sb.append(" 	  	             SELECT 					 					 \n");
        sb.append(" 	  	                       D.ORG_ID 					 					 \n");
        sb.append(" 	  	                       , D.PROJECT_MANAGE_NO					 					 \n");
        sb.append(" 	  	                       , D.POSITION_CODE					 					 \n");
        sb.append(" 	  	                       , '1' AS ORDER_SEQ					 					 \n");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M', D.DAY, 0)) AS M					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM', D.DAY, 0)) AS EM					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M', D.AMT, 0)) AS M_A					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM', D.AMT, 0)) AS EM_A					 \n");
            }else{
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M"+ i +"', D.DAY, 0)) AS M"+ i +"					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM"+ i +"', D.DAY, 0)) AS EM"+ i +"					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M"+ i +"', D.AMT, 0)) AS M"+ i +"_A					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM"+ i +"', D.AMT, 0)) AS EM"+ i +"_A					 \n");
            }
        }
        sb.append(" 	  	             FROM (					 \n");
        sb.append(" 	                			SELECT 					 \n");
        sb.append(" 	                                C.ORG_ID					 \n");
        sb.append(" 	                                , C.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                , C.POSITION_CODE					 \n");
        sb.append(" 	                                , C.PERIOD_YEAR					 \n");
        sb.append(" 	                                , C.PERIOD_MONTH					 \n");
        sb.append(" 	                                , C.YYYYMM					 \n");
        sb.append(" 	                                , C.M_TEXT					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            if(i == 1){
                sb.append(" 	                                        , SUM(C.D"+ i +") 					 \n");
            }else{
                sb.append(" 	                                        + SUM(C.D"+ i +") 					 \n");
            }
        }
        sb.append(" 	                                        AS DAY 					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            if(i == 1){
                sb.append(" 	                                        , SUM(C.D"+ i +"_AMT) 					 \n");
            }else{
                sb.append(" 	                                        + SUM(C.D"+ i +"_AMT) 					 \n");
            }
        }
        sb.append(" 	                                        AS AMT 					 \n");
        sb.append(" 	                     FROM (					 \n");
        sb.append(" 	                               SELECT 					 \n");
        sb.append(" 	                                        A.ORG_ID																					 \n");
        sb.append(" 	                                        , A.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                        , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                        , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                        , A.YYYYMM					 \n");
        sb.append(" 	                                        , A.M_TEXT					 \n");
        sb.append(" 	                                        , A.POSITION_CODE					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) AS D"+ i +"					 \n");
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) * B.AMT AS D"+ i +"_AMT					 \n");
        }
        sb.append(" 	                               FROM SPJMPM A					 \n");
        sb.append(" 	                               LEFT OUTER JOIN SPJPEM B					 \n");
        sb.append(" 	                               ON A.ORG_ID = B.ORG_ID					 \n");
        sb.append(" 	                               AND B.PROJECT_MANAGE_NO = 'JET19821018'				 \n");
        sb.append(" 	                               AND A.POSITION_CODE = B.POSITION_CODE					 \n");
        sb.append(" 	             WHERE A.ORG_ID = '"+compCd+"'					 \n");
        sb.append(" 	             AND A.PROJECT_MANAGE_NO = '"+search_project_manage_no+"'					 \n");
        sb.append(" 	             AND A.DEGREE = '"+degree+"'					 \n");

        sb.append(" 	                               GROUP BY A.ORG_ID 					 \n");
        sb.append(" 	                                                , A.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                                , A.M_TEXT  					 \n");
        sb.append(" 	                                                , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                                , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                                , A.YYYYMM					 \n");
        sb.append(" 	                                                , A.M_TEXT					 \n");
        sb.append(" 	                                                , A.POSITION_CODE					 \n");
        sb.append(" 	                                                , B.AMT												 \n");
        sb.append(" 	                               UNION ALL 					 \n");
        sb.append(" 	                               SELECT 					 \n");
        sb.append(" 	                                        A.ORG_ID																					 \n");
        sb.append(" 	                                        , A.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                        , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                        , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                        , A.YYYYMM					 \n");
        sb.append(" 	                                        , 'E' || A.M_TEXT 					 \n");
        sb.append(" 	                                        , A.POSITION_CODE					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) AS D"+ i +"					 \n");
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) * B.AMT AS D"+ i +"_AMT					 \n");
        }
        sb.append(" 	                               FROM SPJEMPM A					 \n");
        sb.append(" 	                               LEFT OUTER JOIN SPJPEM B					 \n");
        sb.append(" 	                               ON A.ORG_ID = B.ORG_ID					 \n");
        sb.append(" 	                               AND A.POSITION_CODE = B.POSITION_CODE					 \n");
        sb.append(" 	                               AND B.PROJECT_MANAGE_NO = 'JET19821018'						 \n");
        sb.append(" 	                               WHERE A.ORG_ID = '"+compCd+"'					 \n");
        sb.append(" 	                               AND A.PROJECT_MANAGE_NO = '"+search_project_manage_no+"'					 \n");
        sb.append(" 	                               AND A.DEGREE = '"+degree+"'					 \n");

        sb.append(" 	                               GROUP BY A.ORG_ID 					 \n");
        sb.append(" 	                                                , A.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                                , A.M_TEXT  					 \n");
        sb.append(" 	                                                , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                                , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                                , A.YYYYMM					 \n");
        sb.append(" 	                                                , A.M_TEXT					 \n");
        sb.append(" 	                                                , A.POSITION_CODE					 \n");
        sb.append(" 	                                                , B.AMT												 \n");
        sb.append(" 	                     ) C					 															\n");
        sb.append(" 	                     GROUP BY C.ORG_ID 											 \n");
        sb.append(" 	                                      , C.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                      , C.POSITION_CODE 					 \n");
        sb.append(" 	                                      , C.M_TEXT  											\n");
        sb.append(" 	                                      , C.PERIOD_YEAR					 					\n");
        sb.append(" 	                                      , C.PERIOD_MONTH					 					\n");
        sb.append(" 	                                      , C.YYYYMM					 												\n");
        sb.append(" 	                ) D 					 																			\n");
        sb.append(" 	    						GROUP BY D.ORG_ID 					 									\n");
        sb.append(" 	                                      , D.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                      , D.POSITION_CODE 					 \n");
        sb.append(" 	                                      , D.M_TEXT  					 							\n");
        sb.append(" 	 ) B					 																\n");
        sb.append(" 	 GROUP BY B.ORG_ID 					 \n");
        sb.append(" 	             , B.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	             , B.POSITION_CODE					 \n");
        sb.append(" 	             , B.ORDER_SEQ					 \n");
        sb.append(" 	 UNION ALL 					 \n");

        sb.append(" 	 SELECT 					 \n");
        sb.append(" 	             '' 					 \n");
        sb.append(" 	             , 'Total'					 \n");
        sb.append(" 	             , ''					 \n");
        sb.append(" 	             , ''					 \n");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	             , SUM(B.M_A) AS M_A					 \n");
                sb.append(" 	             , SUM(B.EM_A) AS EM_A					 \n");
                sb.append(" 	             , SUM(B.M) AS M					 \n");
                sb.append(" 	             , SUM(B.EM) AS EM					 \n");
            }else{
                sb.append(" 	             , SUM(B.M" + i + "_A) AS M" + i + "_A					 \n");
                sb.append(" 	             , SUM(B.EM" + i + "_A) AS EM" + i + "_A					 \n");
                sb.append(" 	             , SUM(B.M" + i + ") AS M" + i + "					 \n");
                sb.append(" 	             , SUM(B.EM" + i + ") AS EM" + i + "					 \n");
            }
        }
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.M_A");
            }else{
                sb.append(" + B.M" + i + "_A				 \n");
            }
        }
        sb.append(") AS M_A_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.M");
            }else{
                sb.append(" + B.M" + i + "				 \n");
            }
        }
        sb.append(") AS M_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.EM_A");
            }else{
                sb.append(" + B.EM" + i + "_A				 \n");
            }
        }
        sb.append(") AS EM_A_TOTAL									\n");
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.EM");
            }else{
                sb.append(" + B.EM" + i + "				 \n");
            }
        }
        sb.append(") AS EM_TOTAL									\n");
        sb.append(" 	 FROM (					 \n");
        sb.append(" 	  	             SELECT 					 					 \n");
        sb.append(" 	  	                       D.ORG_ID 					 					 \n");
        sb.append(" 	  	                       , D.PROJECT_MANAGE_NO					 					 \n");
        sb.append(" 	  	                       , '1' AS ORDER_SEQ					 					 \n");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M', D.DAY, 0)) AS M					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM', D.DAY, 0)) AS EM					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M', D.AMT, 0)) AS M_A					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM', D.AMT, 0)) AS EM_A					 \n");
            }else{
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M"+ i +"', D.DAY, 0)) AS M"+ i +"					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM"+ i +"', D.DAY, 0)) AS EM"+ i +"					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'M"+ i +"', D.AMT, 0)) AS M"+ i +"_A					 \n");
                sb.append(" 	                       , MAX(DECODE(D.M_TEXT,'EM"+ i +"', D.AMT, 0)) AS EM"+ i +"_A					 \n");
            }
        }
        sb.append(" 	  	             FROM (					 \n");
        sb.append(" 	                			SELECT 					 \n");
        sb.append(" 	                                C.ORG_ID					 \n");
        sb.append(" 	                                , C.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                , C.PERIOD_YEAR					 \n");
        sb.append(" 	                                , C.PERIOD_MONTH					 \n");
        sb.append(" 	                                , C.YYYYMM					 \n");
        sb.append(" 	                                , C.M_TEXT					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            if(i == 1){
                sb.append(" 	                                        , SUM(C.D"+ i +") 					 \n");
            }else{
                sb.append(" 	                                        + SUM(C.D"+ i +") 					 \n");
            }
        }
        sb.append(" 	                                        AS DAY 					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            if(i == 1){
                sb.append(" 	                                        , SUM(C.D"+ i +"_AMT) 					 \n");
            }else{
                sb.append(" 	                                        + SUM(C.D"+ i +"_AMT) 					 \n");
            }
        }
        sb.append(" 	                                        AS AMT 					 \n");
        sb.append(" 	                     FROM (					 \n");
        sb.append(" 	                               SELECT 					 \n");
        sb.append(" 	                                        A.ORG_ID																					 \n");
        sb.append(" 	                                        , A.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                        , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                        , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                        , A.YYYYMM					 \n");
        sb.append(" 	                                        , A.M_TEXT					 \n");
        sb.append(" 	                                        , A.POSITION_CODE					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) AS D"+ i +"					 \n");
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) * B.AMT AS D"+ i +"_AMT					 \n");
        }
        sb.append(" 	                               FROM SPJMPM A					 \n");
        sb.append(" 	                               LEFT OUTER JOIN SPJPEM B					 \n");
        sb.append(" 	                               ON A.ORG_ID = B.ORG_ID					 \n");
        sb.append(" 	                               AND A.POSITION_CODE = B.POSITION_CODE					 \n");
        sb.append(" 	                               WHERE A.ORG_ID = '"+compCd+"'					 \n");
        sb.append(" 	                               AND A.PROJECT_MANAGE_NO = '"+search_project_manage_no+"'					 \n");
        sb.append(" 	                               AND A.DEGREE = '"+degree+"'					 \n");

        sb.append(" 	                               GROUP BY A.ORG_ID 					 \n");
        sb.append(" 	                                                , A.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                                , A.M_TEXT  					 \n");
        sb.append(" 	                                                , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                                , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                                , A.YYYYMM					 \n");
        sb.append(" 	                                                , A.M_TEXT					 \n");
        sb.append(" 	                                                , A.POSITION_CODE					 \n");
        sb.append(" 	                                                , B.AMT												 \n");
        sb.append(" 	                               UNION ALL 					 \n");
        sb.append(" 	                               SELECT 					 \n");
        sb.append(" 	                                        A.ORG_ID																					 \n");
        sb.append(" 	                                        , A.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	                                        , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                        , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                        , A.YYYYMM					 \n");
        sb.append(" 	                                        , 'E' || A.M_TEXT 					 \n");
        sb.append(" 	                                        , A.POSITION_CODE					 \n");
        for(int i = 1 ; i <= 1 ; i++){
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) AS D"+ i +"					 \n");
            sb.append(" 	                                        , SUM(NVL(A.D"+ i +",0)) * B.AMT AS D"+ i +"_AMT					 \n");
        }
        sb.append(" 	                               FROM SPJEMPM A					 \n");
        sb.append(" 	                               LEFT OUTER JOIN SPJPEM B					 \n");
        sb.append(" 	                               ON A.ORG_ID = B.ORG_ID					 \n");
        sb.append(" 	                               AND A.POSITION_CODE = B.POSITION_CODE					 \n");
        sb.append(" 	                               WHERE A.ORG_ID = '"+compCd+"'					 \n");
        sb.append(" 	                               AND A.PROJECT_MANAGE_NO = '"+search_project_manage_no+"'					 \n");
        sb.append(" 	                               AND A.DEGREE = '"+degree+"'					 \n");

        sb.append(" 	                               GROUP BY A.ORG_ID 					 \n");
        sb.append(" 	                                                , A.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                                , A.M_TEXT  					 \n");
        sb.append(" 	                                                , A.PERIOD_YEAR					 \n");
        sb.append(" 	                                                , A.PERIOD_MONTH					 \n");
        sb.append(" 	                                                , A.YYYYMM					 \n");
        sb.append(" 	                                                , A.M_TEXT					 \n");
        sb.append(" 	                                                , A.POSITION_CODE					 \n");
        sb.append(" 	                                                , B.AMT												 \n");
        sb.append(" 	                     ) C					 															\n");
        sb.append(" 	                     GROUP BY C.ORG_ID 											 \n");
        sb.append(" 	                                      , C.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                      , C.M_TEXT  											\n");
        sb.append(" 	                                      , C.PERIOD_YEAR					 					\n");
        sb.append(" 	                                      , C.PERIOD_MONTH					 					\n");
        sb.append(" 	                                      , C.YYYYMM					 												\n");
        sb.append(" 	                ) D 					 																			\n");
        sb.append(" 	    						GROUP BY D.ORG_ID 					 									\n");
        sb.append(" 	                                      , D.PROJECT_MANAGE_NO 					 \n");
        sb.append(" 	                                      , D.M_TEXT  					 							\n");
        sb.append(" 	 ) B					 																\n");
        sb.append(" 	 GROUP BY B.ORG_ID 					 \n");
        sb.append(" 	             , B.PROJECT_MANAGE_NO					 \n");
        sb.append(" 	             , B.ORDER_SEQ					 \n");


        Query query = entityManager.createNativeQuery(sb.toString())
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public String savePersonnelList(PersonnelResult personnelResult) throws Exception {

        String msg = "저장되었습니다.";

        PjtProcessRatePlanDto info = personnelResult.getInfo();
        List<PjtPersonnelPlanDto> list = personnelResult.getSaveList();

        LocalDateTime currentDateTime = LocalDateTime.now();

        String saveDate = currentDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String saveTime = currentDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        String compCd = util.getLoginCompCd();
        String loginId = util.getLoginUserId();
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();

        String project_manage_no = info.getProjectManageNo();
        String degree = info.getDegree();

        String start_date = info.getStartDate();
        String total_month = info.getTotalMonth();

        //엑셀데이터
        String g_project_manage_no = "";
        String position_code = "";
        String e_m_text = ""; //M TEXT
        String e_m_text1 = ""; //M TEXT
        String yyyymm = ""; //yyyymm
        String period_year = ""; //년도
        String period_month = ""; //월
        String cnt = ""; //계획 근무일수
        String cnt1 = ""; //실적 근무일수
        String insert_yn = ""; //신규입력 여부
        String amt_type = ""; //
        String remark = ""; //


        for(PjtPersonnelPlanDto pjtPersonnelPlanDto : list){


            g_project_manage_no = pjtPersonnelPlanDto.getProjectManageNo();
            position_code = pjtPersonnelPlanDto.getPositionCd();
            e_m_text = pjtPersonnelPlanDto.getMonthText();
            yyyymm = pjtPersonnelPlanDto.getYyyymm();
            period_year = pjtPersonnelPlanDto.getPeriodYear();
            period_month = pjtPersonnelPlanDto.getPeriodMonth();
            insert_yn = pjtPersonnelPlanDto.getInsertYn();
            cnt = pjtPersonnelPlanDto.getMVal();
            cnt1 = pjtPersonnelPlanDto.getEmVal();

//            period_year = yyyymm.substring(0, 4);
//            period_month = yyyymm.substring(4, 6);

            // 계획 테이블 먼저

            StringBuilder sb = new StringBuilder();
            sb.append(" 	 SELECT 					 							\n");
            sb.append(" 	 COUNT(A.PROJECT_MANAGE_NO) AS CNT				 \n");
            sb.append(" 	 FROM SPJMPM A					 \n");
            sb.append(" 	 WHERE A.ORG_ID = :compCd					 \n");
            sb.append(" 	 AND A.PROJECT_MANAGE_NO = :projectManageNo					 \n");
            sb.append(" 	 AND A.DEGREE = :degree					 \n");
            sb.append(" 	 AND A.YYYYMM = :yyyymm				 \n");
            sb.append(" 	 AND A.POSITION_CODE = :position_code				 \n");

            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("compCd", compCd);
            query.setParameter("projectManageNo", project_manage_no);
            query.setParameter("degree", degree);
            query.setParameter("yyyymm", yyyymm);
            query.setParameter("positionCode", position_code);

            BigDecimal count = new JpaResultMapper().uniqueResult(query, PjtPersonnelPlanDto.class).getCount();


            if("Y".equals(insert_yn)){
                if(count.intValue() != 0){
                    throw new Exception("이미 저장된 직책 입니다.");
                }else{
                    PjtPersonnelPlan pjtPersonnelPlan = PjtPersonnelPlan.builder()
                            .orgId(compCd)
                            .projectMngNo(project_manage_no)
                            .degree(degree)
                            .periodYear(period_year)
                            .periodMonth(period_month)
                            .yyyyMm(yyyymm)
                            .mText(e_m_text)
                            .remark(remark)
                            .d1(cnt)
                            .addDate(saveDate)
                            .addTime(saveTime)
                            .addUserId(loginId)
                            .build();
                    pjtPersonnelPlanRepository.save(pjtPersonnelPlan);
                }
            }else{
                if(count.intValue() != 1){
                    PjtPersonnelPlan pjtPersonnelPlan = PjtPersonnelPlan.builder()
                            .orgId(compCd)
                            .projectMngNo(project_manage_no)
                            .degree(degree)
                            .periodYear(period_year)
                            .periodMonth(period_month)
                            .yyyyMm(yyyymm)
                            .mText(e_m_text)
                            .remark(remark)
                            .d1(cnt)
                            .addDate(saveDate)
                            .addTime(saveTime)
                            .addUserId(loginId)
                            .build();
                    pjtPersonnelPlanRepository.save(pjtPersonnelPlan);
                }
                if(count.intValue() == 1){
                    pjtPersonnelPlanRepository.updatePjtPersonnelPlan(compCd, project_manage_no,period_year,period_month,yyyymm,e_m_text,
                            position_code,remark,cnt,saveDate,saveTime,loginId,degree);
                }
            }

            // 이후 실적 테이블에

            sb = new StringBuilder();
            sb.append(" 	 SELECT 					 							\n");
            sb.append(" 	 COUNT(A.PROJECT_MANAGE_NO) AS CNT				 \n");
            sb.append(" 	 FROM SPJEMPM A					 \n");
            sb.append(" 	 WHERE A.ORG_ID = :compCd					 \n");
            sb.append(" 	 AND A.PROJECT_MANAGE_NO = :projectManageNo					 \n");
            sb.append(" 	 AND A.DEGREE = :degree					 \n");
            sb.append(" 	 AND A.YYYYMM = :yyyymm				 \n");
            sb.append(" 	 AND A.POSITION_CODE = :position_code				 \n");

            Query query2 = entityManager.createNativeQuery(sb.toString());
            query2.setParameter("compCd", compCd);
            query2.setParameter("projectManageNo", project_manage_no);
            query2.setParameter("degree", degree);
            query2.setParameter("yyyymm", yyyymm);
            query2.setParameter("positionCode", position_code);

            BigDecimal count2 = new JpaResultMapper().uniqueResult(query2, PjtPersonnelPlanDto.class).getCount();

            if("Y".equals(insert_yn)){
                if(count2.intValue() != 0){
                    throw new Exception("이미 저장된 직책 입니다.");
                }else{
                    PjtPersonnelResult pjtPersonnelResult = PjtPersonnelResult.builder()
                            .orgId(compCd)
                            .projectMngNo(project_manage_no)
                            .degree(degree)
                            .periodYear(period_year)
                            .periodMonth(period_month)
                            .yyyyMm(yyyymm)
                            .mText(e_m_text)
                            .remark(remark)
                            .d1(cnt1)
                            .addDate(saveDate)
                            .addTime(saveTime)
                            .addUserId(loginId)
                            .build();
                    pjtPersonnelResultRepository.save(pjtPersonnelResult);
                }
            }else{
                if(count2.intValue() != 1){
                    PjtPersonnelResult pjtPersonnelResult = PjtPersonnelResult.builder()
                            .orgId(compCd)
                            .projectMngNo(project_manage_no)
                            .degree(degree)
                            .periodYear(period_year)
                            .periodMonth(period_month)
                            .yyyyMm(yyyymm)
                            .mText(e_m_text)
                            .remark(remark)
                            .d1(cnt1)
                            .addDate(saveDate)
                            .addTime(saveTime)
                            .addUserId(loginId)
                            .build();
                    pjtPersonnelResultRepository.save(pjtPersonnelResult);
                }
                if(count2.intValue() == 1){
                    pjtPersonnelResultRepository.updatePjtPersonnelResult(compCd, project_manage_no,period_year,period_month,yyyymm,e_m_text,
                            position_code,remark,cnt,saveDate,saveTime,loginId,degree);
                }
            }



//            if(count.intValue() == 0){
//
//                PjtProcessRatePlan pjtProcessRatePlan = PjtProcessRatePlan.builder()
//                        .orgId(compCd)
//                        .projectMngNo(project_manage_no)
//                        .degree(degree)
//                        .periodYear(period_year)
//                        .periodMonth(period_month)
//                        .yyyyMm(yyyymm)
//                        .mText(e_m_text)
//                        .itemCode(item_code)
//                        .objectiveAll(objective_all)
//                        .objectiveMon(objective_mon)
//                        .addDate(saveDate)
//                        .addTime(saveTime)
//                        .addUserId(loginId)
//                        .build();
//
//                pjtPersonnelPlanRepository.save(pjtProcessRatePlan);
//
//            }else
//            if(count.intValue() == 1){
//
//                pjtPersonnelPlanRepository.updatePjtProcessRatePlan(
//                                objective_mon,
//                                objective_all,
//                                saveDate,
//                                saveTime,
//                                loginId,
//                                compCd,
//                                project_manage_no,
//                                degree,
//                                item_code,
//                                yyyymm
//                                );
//
//            }
//
        }

        return msg;
    }
}

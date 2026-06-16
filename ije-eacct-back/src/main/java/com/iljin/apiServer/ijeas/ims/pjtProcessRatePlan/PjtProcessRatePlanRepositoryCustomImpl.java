package com.iljin.apiServer.ijeas.ims.pjtProcessRatePlan;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.approval.ApprovalHeaderDto;
import com.iljin.apiServer.ijeas.costBudget.CostBudgetDto;
import com.iljin.apiServer.ijeas.ims.pjtProgressChart.PjtProgressChartDto;
import com.iljin.apiServer.ijeas.ims.pjtProgressChart.PjtProgressChartRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PjtProcessRatePlanRepositoryCustomImpl implements PjtProcessRatePlanRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    private final PjtProcessRatePlanRepository pjtProcessRatePlanRepository;



    /* 가변형 컬럼 조회로 인한 네이티브 쿼리 사용. */
    @Override
    public List<Map<String,Object>> getProcessList(PjtProcessRatePlanDto pjtProcessRatePlanDto) {



        String MonthList = String.join(",", pjtProcessRatePlanDto.getParam());

        String user_id = pjtProcessRatePlanDto.getUserId();
        String compCd = util.getLoginCompCd();
        String search_project_manage_no = pjtProcessRatePlanDto.getProjectManageNo();
        String degree = pjtProcessRatePlanDto.getDegree();
        String total_month = String.valueOf(pjtProcessRatePlanDto.param.size());
        int t_month = 0;
        if(!"".equals(total_month)){
            t_month = Integer.parseInt(total_month);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(" 	 SELECT 					 																								");
        sb.append(" 	 			 NVL(C.ORG_ID, '"+ compCd +"') AS ORG_ID					 										");
        sb.append(" 	             , NVL(C.PROJECT_MANAGE_NO, '"+ search_project_manage_no +"') AS PROJECT_MANAGE_NO					 ");
        sb.append(" 	             , NVL(C.DEGREE, '"+ degree +"') AS DEGREE					 ");
        sb.append(" 	 			 , NVL(C.ITEM_CODE, S.CODE) AS ITEM_CODE					 ");
        sb.append(" 	             , NVL(C.ITEM_NAME, S.TEXT1) AS ITEM_NAME					 ");
        sb.append(" 	             , NVL(C.OBJECTIVE_ALL, '100') AS OBJECTIVE_ALL					 ");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	             , NVL(C.M,0) AS M				 ");
            }else{
                sb.append(" 	             , NVL(C.M" + i + ",0) AS M" + i + "					 ");
            }
        }
        sb.append(" 	             , C.M_TOTAL					 ");
        sb.append(" 	 FROM SCODE S					 ");
        sb.append(" 	 LEFT OUTER JOIN (					 ");
        sb.append(" 	 SELECT 					 ");
        sb.append(" 	             B.ORG_ID 					 ");
        sb.append(" 	             , B.PROJECT_MANAGE_NO					 ");
        sb.append(" 	             , B.DEGREE					 ");
        sb.append(" 	             , B.ITEM_CODE					 ");
        sb.append(" 	             , B.ITEM_NAME					 ");
        sb.append(" 	             , B.OBJECTIVE_ALL					 ");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	             , SUM(B.M) AS M					 ");
            }else{
                sb.append(" 	             , SUM(B.M" + i + ") AS M" + i + "					 ");
            }
        }
        sb.append(" 	             , SUM(");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append("B.M");
            }else{
                sb.append(" + B.M" + i + "				 ");
            }
        }
        sb.append(") AS M_TOTAL									");
        sb.append(" 	 FROM (					 ");
        sb.append(" 	             SELECT 					 ");
        sb.append(" 	                       A.ORG_ID 					 ");
        sb.append(" 	                       , A.PROJECT_MANAGE_NO					 ");
        sb.append(" 	                       , A.DEGREE					 ");
        sb.append(" 	                       , A.ITEM_CODE					 ");
        sb.append(" 	                       , A.ITEM_NAME					 ");
        sb.append(" 	                       , A.OBJECTIVE_ALL					 ");
        sb.append(" 	                       , '1' AS ORDER_SEQ					 ");
        for(int i = 0 ; i < t_month ; i++){
            if(i == 0){
                sb.append(" 	                       , MAX(DECODE(A.M_TEXT,'M', A.OBJECTIVE_MON, 0)) AS M					 ");
            }else{
                sb.append(" 	                       , MAX(DECODE(A.M_TEXT,'M"+ i +"', A.OBJECTIVE_MON, 0)) AS M"+ i +"					 ");
            }
        }
        sb.append(" 	             FROM SPJPPM A					 ");
        sb.append(" 	             WHERE ORG_ID = '"+ compCd +"'					 ");
        sb.append(" 	             AND PROJECT_MANAGE_NO = '"+ search_project_manage_no +"'					 ");
        sb.append(" 	             AND DEGREE = '"+ degree +"'					 ");
        sb.append(" 	             GROUP BY A.ORG_ID 					 ");
        sb.append(" 	                       , A.PROJECT_MANAGE_NO					 ");
        sb.append(" 	                       , A.DEGREE					 ");
        sb.append(" 	                       , A.ITEM_CODE					 ");
        sb.append(" 	                       , A.ITEM_NAME					 ");
        sb.append(" 	                       , A.M_TEXT					 ");
        sb.append(" 	                       , A.OBJECTIVE_ALL					 ");
        sb.append(" 	                       , A.OBJECTIVE_MON					 ");
        sb.append(" 	 ) B					 																");
        sb.append(" 	 GROUP BY B.ORG_ID 					 ");
        sb.append(" 	             , B.PROJECT_MANAGE_NO					 ");
        sb.append(" 	             , B.DEGREE					 ");
        sb.append(" 	             , B.ITEM_CODE					 ");
        sb.append(" 	             , B.ITEM_NAME					 ");
        sb.append(" 	             , B.OBJECTIVE_ALL					 ");
        sb.append(" 	             , B.ORDER_SEQ					 ");
        sb.append(" 	 ) C					 											");
        sb.append(" 	 ON S.CODE = C.ITEM_CODE					 ");
        sb.append(" 	 WHERE S.LANGUAGE = 'KO'					 ");
        sb.append(" 	 AND S.TYPE = 'F202'					 ");
        sb.append(" 	 ORDER BY  NVL(C.ITEM_CODE, S.CODE)					 ");


        Query query = entityManager.createNativeQuery(sb.toString())
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        return query.getResultList();
    }

    @Override
    public String saveProcessList(ProcessResult processResult) {

        String msg = "저장되었습니다.";

        PjtProcessRatePlanDto info = processResult.getInfo();
        List<PjtProcessRatePlanDto> list = processResult.getSaveList();

        LocalDateTime currentDateTime = LocalDateTime.now();

        String saveDate = currentDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String saveTime = currentDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        String compCd = util.getLoginCompCd();
        String loginId = util.getLoginUserId();

        String project_manage_no = info.getProjectManageNo();
        String degree = info.getDegree();

        String item_code = "";
        String objective_all = "";
        String e_m_text = ""; //M TEXT
        String yyyymm = ""; //yyyymm
        String period_year = ""; //년도
        String period_month = ""; //월
        String objective_mon = ""; //


        for(PjtProcessRatePlanDto pjtProcessRatePlanDto : list){

            item_code = pjtProcessRatePlanDto.getItemCode();
            objective_all = pjtProcessRatePlanDto.getObjectiveAll();
            e_m_text = pjtProcessRatePlanDto.getMonthText();
            yyyymm = pjtProcessRatePlanDto.getYyyymm();
            period_year = pjtProcessRatePlanDto.getPeriodYear();
            period_month = pjtProcessRatePlanDto.getPeriodMonth();
            objective_mon = pjtProcessRatePlanDto.objectiveMon;

            StringBuilder sb = new StringBuilder();
            sb.append(" 	 SELECT					 \n");
            sb.append(" 	             COUNT(PROJECT_MANAGE_NO) AS CNT 		\n");
            sb.append(" 	 FROM SPJPPM					 										\n");
            sb.append(" 	 WHERE ORG_ID = :compCd							\n");
            sb.append(" 	 AND PROJECT_MANAGE_NO = :projectManageNo			\n");
            sb.append(" 	 AND DEGREE = :degree			\n");
            sb.append(" 	 AND YYYYMM = :yyyymm								\n");
            sb.append(" 	 AND ITEM_CODE = :itemCode							\n");

            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("compCd", compCd);
            query.setParameter("projectManageNo", project_manage_no);
            query.setParameter("degree", degree);
            query.setParameter("yyyymm", yyyymm);
            query.setParameter("itemCode", item_code);

            BigDecimal count = com.iljin.apiServer.core.util.ResultMapperUtil.uniqueResult(query, PjtProcessRatePlanDto.class).getCount();

            if(count.intValue() == 0){

                PjtProcessRatePlan pjtProcessRatePlan = PjtProcessRatePlan.builder()
                        .orgId(compCd)
                        .projectMngNo(project_manage_no)
                        .degree(degree)
                        .periodYear(period_year)
                        .periodMonth(period_month)
                        .yyyyMm(yyyymm)
                        .mText(e_m_text)
                        .itemCode(item_code)
                        .objectiveAll(objective_all)
                        .objectiveMon(objective_mon)
                        .addDate(saveDate)
                        .addTime(saveTime)
                        .addUserId(loginId)
                        .build();

                pjtProcessRatePlanRepository.save(pjtProcessRatePlan);

            }else
            if(count.intValue() == 1){

                pjtProcessRatePlanRepository.updatePjtProcessRatePlan(
                                objective_mon,
                                objective_all,
                                saveDate,
                                saveTime,
                                loginId,
                                compCd,
                                project_manage_no,
                                degree,
                                item_code,
                                yyyymm
                                );

            }

        }

        return msg;
    }
}

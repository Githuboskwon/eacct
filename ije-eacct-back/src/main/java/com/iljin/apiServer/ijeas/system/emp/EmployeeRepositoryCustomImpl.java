package com.iljin.apiServer.ijeas.system.emp;

import com.iljin.apiServer.core.util.Util;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    @Override
    public List<EmployeeDto> getEmployeeList(EmployeeDto employeeDto){


        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
                "      A.COMP_CD AS compCd, " +
                "      A.EMP_NO AS empNo, " +
                "      A.EMP_NM AS empNm, " +
                "      A.DEPT_CD AS deptCd," +
                "      A.DEPT_NM AS deptNm," +
                "      A.JOB_DUT_CD as jobDutCd," +
                "      A.JOB_DUT_NM AS jobDutNm," +
                "      A.JOB_GRADE_CD AS jobGradeCd," +
                "      A.JOB_GRADE_NM AS jobGradeNm," +
                "      A.SERVE_CD AS serveCd," +
                "      A.SERVE_NM AS serveNm," +
                "      CASE WHEN " +
                "      (SELECT COUNT(B.DEPT_CD) FROM TB_EMP_DEPT_ROLE B WHERE A.COMP_CD = B.COMP_CD AND A.EMP_NO = B.EMP_NO) > 0 " +
                "      THEN 'Y' ELSE 'N' END AS deptRoleYn" +
                "      FROM TB_MST_EMP A " +
                "      WHERE 1 = 1 " +
                "        AND A.COMP_CD = :compCd   "
                );


        if(!StringUtils.isEmpty(employeeDto.getCompCd())) {
            sb.append("   		 and A.COMP_CD = :compCd");
        }

        if(!StringUtils.isEmpty(employeeDto.getServeCd())) {
            sb.append("   		 and A.SERVE_CD = :serveCd");
        }

        if(!StringUtils.isEmpty(employeeDto.getDeptCd())) {
            sb.append("   		 and A.DEPT_CD = :deptCd");
        }

        if(!StringUtils.isEmpty(employeeDto.getEmpNo()) || !StringUtils.isEmpty(employeeDto.getEmpNm())) {
            sb.append("   		 and (A.EMP_NO like concat(concat('%',:empNo), '%') or A.EMP_NM like concat(concat('%',:empNm), '%') )");
        }

//        if(!StringUtils.isEmpty(employeeDto.getEmpNm())) {
//            sb.append("   		 or A.EMP_NM like concat(concat('%',:empNm), '%') ");
//        }

        if(!StringUtils.isEmpty(employeeDto.getDeptRoleYn())) {
            if(employeeDto.getDeptRoleYn().equals("Y")){
                sb.append("   		 and (SELECT COUNT(B.DEPT_CD) FROM TB_EMP_DEPT_ROLE B WHERE A.EMP_NO = B.EMP_NO) > 0");
            }else{
                sb.append("   		 and (SELECT COUNT(B.DEPT_CD) FROM TB_EMP_DEPT_ROLE B WHERE A.EMP_NO = B.EMP_NO) = 0");
            }
        }

        sb.append("      ORDER BY A.DEPT_CD , A.EMP_NO ");

//
//        sb.append(" ORDER BY A.postingDt, A.regDtm");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(!StringUtils.isEmpty(employeeDto.getCompCd())) {
            query.setParameter("compCd", employeeDto.getCompCd());
        }

        if(!StringUtils.isEmpty(employeeDto.getServeCd())) {
            query.setParameter("serveCd", employeeDto.getServeCd());
        }

        if(!StringUtils.isEmpty(employeeDto.getDeptCd())) {
            query.setParameter("deptCd", employeeDto.getDeptCd());
        }

        if(!StringUtils.isEmpty(employeeDto.getEmpNo())) {
            query.setParameter("empNo", employeeDto.getEmpNo());
        }

        if(!StringUtils.isEmpty(employeeDto.getEmpNm())) {
            query.setParameter("empNm", employeeDto.getEmpNm());
        }


        return new JpaResultMapper().list(query, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getDelegatingEmployeesByEmpNoOrEmpNm(String value) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "     EMP.COMP_CD" +
            ",    EMP.COMP_NM" +
            ",    EMP.EMP_NO" +
            ",    EMP.EMP_NM" +
            ",    TO_CHAR(EMP.PERSON_ID)" +
            ",    EMP.DEPT_CD" +
            ",    EMP.DEPT_NM" +
            ",    EMP.UPPER_DEPT_CD" +
            ",    EMP.UPPER_DEPT_NM" +
            ",    EMP.JOB_DUT_CD" +
            ",    EMP.JOB_DUT_NM" +
            ",    EMP.JOB_GRADE_CD" +
            ",    EMP.JOB_GRADE_NM" +
            ",    UR.ROLE" +
            ",    R.ROLE_NM" +
            ",    EMP.SERVE_CD" +
            ",    EMP.SERVE_NM" +
            ",    EMP.EMAIL" +
            ",    EMP.MOB_TEL_NO" +
            ",    TO_CHAR(EMP.LEDGER_ID)" +
            ",    EMP.PRODUCT_CD" +
            ",    EMP.PRODUCT_NM" +
            ",    EMP.PJT_ID" +
            ",    EMP.PJT_CD" +
            ",    EMP.PJT_NM" +
            ",    EMP.TR_ACCT_CD" +
            ",    EMP.TR_ACCT_NM" +
            ",    EMP.SLIP_TYPE_CD" +
            ",    EMP.SLIP_TYPE_NM" +
            ",    EMP.SEGMENT1_CD" +
            ",    EMP.SEGMENT9_CD" +
            ",    EMP.SEGMENT10_CD" +
            ",    EMP.CCTR_CD" +
            ",    EMP.CCTR_NM" +
            ",    EMP.TASK_NO" +
            ",    EMP.TASK_NM" +
            ",    EMP.TASK_ID" +
            ",    (SELECT ATTRIBUTE1 FROM TB_MST_CCTR_BLG_ERP CCTR WHERE CCTR.DEPT_CD = EMP.DEPT_CD AND CCTR.COMP_CD = :compCd) AS ATTRIBUTE1 " +
            ",    (SELECT ATTRIBUTE2 FROM TB_MST_CCTR_BLG_ERP CCTR WHERE CCTR.DEPT_CD = EMP.DEPT_CD AND CCTR.COMP_CD = :compCd) AS ATTRIBUTE2 " +
            ",    (SELECT ATTRIBUTE3 FROM TB_MST_CCTR_BLG_ERP CCTR WHERE CCTR.DEPT_CD = EMP.DEPT_CD AND CCTR.COMP_CD = :compCd) AS ATTRIBUTE3 " +
            ",    (SELECT " +
            "       HL.LOCATION_CODE " +
            "       FROM " +
            "       CBO_SP_LOCATION_V HL " +
            "       WHERE " +
            "       HL.ORG_ID = EMP.COMP_CD " +
            "       AND HL.LEGAL_ADDRESS_FLAG = 'N' " +
            "       AND HL.LOCATION_TYPE =  " +
            "       (SELECT " +
            "           A.ATTRIBUTE1 " +
            "           FROM " +
            "           CBO_GL_COA_SEGMENT_V A " +
            "           WHERE " +
            "           A.SEGMENT_NUM = '3' " +
            "           AND A.ORG_ID = EMP.COMP_CD " +
            "           AND SYSDATE BETWEEN A.START_DATE_ACTIVE AND A.END_DATE_ACTIVE " +
            "           AND A.VALUE_CODE = EMP.DEPT_CD " +
            "        ) " +
            "     ) AS TAX_LOCATION_CODE "+
            ",    (SELECT " +
            "       HL.DESCRIPTION " +
            "       FROM " +
            "       CBO_SP_LOCATION_V HL " +
            "       WHERE " +
            "       HL.ORG_ID = EMP.COMP_CD " +
            "       AND HL.LEGAL_ADDRESS_FLAG = 'N' " +
            "       AND HL.LOCATION_TYPE =  " +
            "       (SELECT " +
            "           A.ATTRIBUTE1 " +
            "           FROM " +
            "           CBO_GL_COA_SEGMENT_V A " +
            "           WHERE " +
            "           A.SEGMENT_NUM = '3' " +
            "           AND A.ORG_ID = EMP.COMP_CD " +
            "           AND SYSDATE BETWEEN A.START_DATE_ACTIVE AND A.END_DATE_ACTIVE " +
            "           AND A.VALUE_CODE = EMP.DEPT_CD " +
            "        ) " +
            "       ) AS TAX_LOCATION_NAME "+
            "   FROM " +
            "    TB_MST_EMP EMP " +
            "   INNER JOIN " +
            "       A_USER U ON U.LOGIN_ID = EMP.EMP_NO AND U.COMP_CD = :compCd " +
            "   INNER JOIN " +
            "       A_USER_ROLE UR ON UR.USER_ID = U.ID AND UR.COMP_CD = :compCd " +
            "   INNER JOIN " +
            "       A_ROLE R ON R.ROLE_CD = UR.ROLE AND R.COMP_CD = :compCd " +
            "   WHERE EMP.COMP_CD = :compCd " +
            "       AND EMP.EMP_NO IN " +
            "           (SELECT " +
            "               GIVE_USER_ID " +
            "           FROM TB_MST_DELEGATE DLG " +
            "           WHERE COMP_CD = :compCd " +
            "               AND DLG.RECEIVE_USER_ID = :loginId " +
            "           UNION " +
            "           SELECT " +
            "               EMP_NO " +
            "           FROM TB_MST_EMP " +
            "           WHERE EMP_NO = :loginId ) ");

        if(!StringUtils.isEmpty(value)) {
            sb.append("AND (EMP.EMP_NO LIKE '%' || :value || '%' OR EMP.EMP_NM LIKE '%' || :value || '%') ");
        }
        sb.append("  ORDER BY EMP.DEPT_CD, EMP.EMP_NM ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("loginId", util.getLoginId());
        query.setParameter("compCd", util.getLoginCompCd());

        if(!StringUtils.isEmpty(value)) {
            query.setParameter("value", value);
        }

        return new JpaResultMapper().list(query, EmployeeDto.class);
    }
}

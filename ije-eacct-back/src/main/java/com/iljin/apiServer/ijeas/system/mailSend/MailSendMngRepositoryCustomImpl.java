package com.iljin.apiServer.ijeas.system.mailSend;

import com.iljin.apiServer.core.util.Util;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MailSendMngRepositoryCustomImpl implements MailSendMngRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;
    private final Util util;

    @Override
    public List<MailHistoryDto> getUnApprUserList(MailHistoryDto mailHistoryDto) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "  B.COMP_CD " +
            ", A.EMP_NO " +
            ", A.CNT AS CNT " +
            ", B.EMP_NM " +
            ", B.DEPT_CD " +
            ", B.DEPT_NM " +
            ", B.JOB_CD " +
            ", B.JOB_NM " +
            ", C.REG_ID AS SEND_USER_NM" +
            ", TO_CHAR(C.MAIL_SEND_DT) " +
            " FROM (SELECT EMP_NO, COUNT(*) AS CNT  " +
            "           FROM TB_SLIP_HD " +
            "           WHERE 1=1 " +
            "           AND COMP_CD = :compCd" +
            "           AND STATUS = 'SV' " +
            "           AND POSTING_DT >= :searchDt " +
            "           GROUP BY EMP_NO) A " +
            " JOIN TB_MST_EMP B ON A.EMP_NO = B.EMP_NO AND B.COMP_CD = :compCd " +
            " LEFT JOIN (SELECT * FROM (SELECT RECEIVE_USER_CD, REG_ID, MAIL_SEND_DT, MAIL_TYPE_CD, ROW_NUMBER() OVER (PARTITION BY COMP_CD, RECEIVE_USER_CD ORDER BY MAIL_SEND_DT DESC) AS ROW_NUM FROM TB_MAIL_HISTORY WHERE COMP_CD = :compCd AND MAIL_TYPE_CD = '01') WHERE ROW_NUM = 1) C ON A.EMP_NO = C.RECEIVE_USER_CD" +
            " LEFT JOIN TB_MST_EMP D ON D.COMP_CD = :compCd AND C.REG_ID = D.EMP_NO " +
            " ORDER BY B.DEPT_CD , B.EMP_NO ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", util.getLoginCompCd());
        query.setParameter("searchDt", mailHistoryDto.getSearchDt());
        return new JpaResultMapper().list(query, MailHistoryDto.class);
    }

    @Override
    public List<MailHistoryDto> getUnTrUserList(MailHistoryDto mailHistoryDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "  B.COMP_CD " +
            ", A.EMP_NO " +
            ", A.CNT AS CNT " +
            ", B.EMP_NM " +
            ", B.DEPT_CD " +
            ", B.DEPT_NM " +
            ", B.JOB_CD " +
            ", B.JOB_NM " +
            ", C.REG_ID AS SEND_USER_NM" +
            ", TO_CHAR(C.MAIL_SEND_DT) " +
            " FROM (SELECT EMPLOYEE_NO AS EMP_NO, COUNT(*) AS CNT  " +
            "           FROM TB_CARD_USE_LIST " +
            "           WHERE 1=1 " +
            "           AND COMP_CD = :compCd" +
            "           AND STATUS = '01' " +
            "           AND USED_DT >= :searchDt " +
            "           GROUP BY EMPLOYEE_NO) A " +
            " JOIN TB_MST_EMP B ON A.EMP_NO = B.EMP_NO AND B.COMP_CD = :compCd " +
            " LEFT JOIN (SELECT * FROM (SELECT RECEIVE_USER_CD, REG_ID, MAIL_SEND_DT, MAIL_TYPE_CD, ROW_NUMBER() OVER (PARTITION BY COMP_CD, RECEIVE_USER_CD ORDER BY MAIL_SEND_DT DESC) AS ROW_NUM FROM TB_MAIL_HISTORY WHERE COMP_CD = :compCd AND MAIL_TYPE_CD = '02') WHERE ROW_NUM = 1) C ON A.EMP_NO = C.RECEIVE_USER_CD" +
            " LEFT JOIN TB_MST_EMP D ON D.COMP_CD = :compCd AND C.REG_ID = D.EMP_NO " +
            " ORDER BY B.DEPT_CD , B.EMP_NO ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", util.getLoginCompCd());
        query.setParameter("searchDt", mailHistoryDto.getSearchDt());
        return new JpaResultMapper().list(query, MailHistoryDto.class);

    }

}

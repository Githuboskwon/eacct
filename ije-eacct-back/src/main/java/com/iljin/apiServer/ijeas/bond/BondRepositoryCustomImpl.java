package com.iljin.apiServer.ijeas.bond;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

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
public class BondRepositoryCustomImpl implements BondRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    @Override
    public List<BondExpendDto> getBondExpendList(BondExpendDto bondExpendDto) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "    CASE " +
            "        WHEN NVL(BND.AMEND_SEQ, 0) = 0 THEN '신규' " +
            "        WHEN NVL(BND.AMEND_SEQ, 0) > 0 THEN 'AMEND:' || BND.AMEND_SEQ " +
            "    END AS AMEND_SEQ " +
            ",   APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(CSSL.LEDGER_ID, CSSL.ACTUAL_DEPT_CODE, '3') AS DEPT_NM " +
            ",   BND.BEN_COUNTRY AS BEN_COUNTRY " +
            ",   BND.BUDGET AS BUDGET " +
            ",   BND.CURRENCY_CD AS BOND_CURRENCY_CD " +
            ",   HD.USED_CUR AS CURRENCY_CD " +
            ",   HD.REG_ID " +
            ",   HD.SLIP_TYPE " +
            ",   HD.SLIP_HEADER_ID " +
            ",   HD.APPROVAL_GROUP_ID " +
            ",   HD.STATUS AS SLIP_STATUS " +
            ",   BND.CUSTOMER_NM AS CUSTOMER_NM " +
            ",   BND.GUARANTEE_AMT AS GUARANTEE_AMT " +
            ",   CSSL.DESCRIPTION AS HEADER_TEXT" +
            ",   BND.INT_BANK_NM AS INT_BANK_NM " +
            ",   BND.LOCAL_BANK_NM AS LOCAL_BANK_NM " +
            ",   CASE " +
            "        WHEN BND.TYPE = 'LOCAL' THEN TO_CHAR(BND.MATURITY_DT, 'YYYY-MM-DD') " +
            "        ELSE '' " +
            "    END AS MATURITY_DT_I" +
            ",   CASE " +
            "        WHEN BND.TYPE = 'OVERSEA' THEN TO_CHAR(BND.MATURITY_DT, 'YYYY-MM-DD') " +
            "        ELSE '' " +
            "    END AS MATURITY_DT_F" +
            ",   CASE " +
            "        WHEN BND.TYPE = 'LOCAL' THEN BND.RATE " +
            "    END AS RATE_I " +
            ",   CASE " +
            "        WHEN BND.TYPE = 'OVERSEA' THEN BND.RATE " +
            "    END AS RATE_F " +
            ",   CASE " +
            "        WHEN BND.TYPE = 'LOCAL' THEN HD.USED_AMT " +
            "    END AS USED_AMT_I " +
            ",   CASE " +
            "        WHEN BND.TYPE = 'OVERSEA' THEN HD.USED_FOR_AMT " +
            "    END AS USED_AMT_F " +
            ",   TO_CHAR(BND.OPENING_DT, 'YYYY-MM-DD') AS OPENING_DT " +
            ",   BND.PROJECT_NM " +
            ",   BND.REF_NO AS REF_NO " +
            ",   BND.SLIP_NO AS SLIP_NO " +
            ",   BND.SPLIT_ETC_YN AS SPLIT_ETC_YN " +
            ",   BND.BOND_CD " +
            ",   CASE " +
            "        WHEN BND.TYPE = 'LOCAL' THEN '국내 수수료' " +
            "        WHEN BND.TYPE = 'OVERSEA' THEN '해외 수수료' " +
            "    END AS TYPE " +
            "FROM " +
            "    TB_BOND_HD BND " +
            "JOIN TB_SLIP_HD HD ON HD.COMP_CD = BND.COMP_CD AND HD.SLIP_HEADER_ID = BND.SLIP_HEADER_ID " +
            "LEFT JOIN CBO_SP_SLIP_LINE CSSL ON HD.COMP_CD = CSSL.ORG_ID AND HD.SLIP_HEADER_ID = CSSL.SLIP_HEADER_ID " +
            "WHERE " +
            "    HD.STATUS IN ('CC', 'IC', 'BD9', '04') " +
            "    AND HD.SLIP_TYPE IN ('SPAP010', '18') " +
            "    AND HD.COMP_CD = :compCd ");

        if(hasText(bondExpendDto.getDeptCd())) {
            sb.append("    AND UPPER(CSSL.ACTUAL_DEPT_CODE) LIKE '%' || :deptCd || '%' ");
        }
        if(hasText(bondExpendDto.getRefNo())) {
            sb.append("    AND BND.REF_NO = :refNo ");
        }
        if(hasText(bondExpendDto.getBenCountryCd())) {
            sb.append("    AND BND.BEN_COUNTRY_CD = :benCountryCd ");
        }
        if(hasText(bondExpendDto.getCustomerId())) {
            sb.append("    AND BND.CUSTOMER_ID = :customerId ");
        }
        if(hasText(bondExpendDto.getProjectId())) {
            sb.append("    AND BND.PROJECT_ID = :projectId ");
        }
        if(hasText(bondExpendDto.getOpeningDt())) {
            sb.append("    AND BND.OPENING_DT = TO_DATE(:openingDt, 'YYYYMMDD') ");
        }
        if(hasText(bondExpendDto.getLocalMaturityDt()) && !hasText(bondExpendDto.getOverseasMaturityDt())) {
            sb.append("    AND TYPE = 'LOCAL' AND BND.MATURITY_DT = TO_DATE(:localMaturityDt, 'YYYYMMDD') ");
        } else if (!hasText(bondExpendDto.getLocalMaturityDt()) && hasText(bondExpendDto.getOverseasMaturityDt())) {
            sb.append("    AND TYPE = 'OVERSEA' AND BND.MATURITY_DT = TO_DATE(:overseasMaturityDt, 'YYYYMMDD') ");
        } else if(hasText(bondExpendDto.getLocalMaturityDt()) && hasText(bondExpendDto.getOverseasMaturityDt())){
            sb.append("    AND (BND.MATURITY_DT = TO_DATE(:localMaturityDt, 'YYYYMMDD') " +
                " OR BND.MATURITY_DT = TO_DATE(:overseasMaturityDt, 'YYYYMMDD')) ");
        }
        if(nonNull(bondExpendDto.getGuaranteeAmt())) {
            sb.append("    AND BND.GUARANTEE_AMT = :guaranteeAmt ");
        }
        sb.append(" ORDER BY BND.OPENING_DT, BND.GUARANTEE_AMT ASC ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", util.getLoginCompCd());

        if(hasText(bondExpendDto.getDeptCd())) {
            query.setParameter("deptCd", bondExpendDto.getDeptCd());
        }
        if(hasText(bondExpendDto.getRefNo())) {
            query.setParameter("refNo", bondExpendDto.getRefNo());
        }
        if(hasText(bondExpendDto.getBenCountryCd())) {
            query.setParameter("benCountryCd", bondExpendDto.getBenCountryCd());
        }
        if(hasText(bondExpendDto.getCustomerId())) {
            query.setParameter("customerId", bondExpendDto.getCustomerId());
        }
        if(hasText(bondExpendDto.getProjectId())) {
            query.setParameter("projectId", bondExpendDto.getProjectId());
        }
        if(hasText(bondExpendDto.getOpeningDt())) {
            query.setParameter("openingDt", bondExpendDto.getOpeningDt());
        }
        if(hasText(bondExpendDto.getLocalMaturityDt()) && !hasText(bondExpendDto.getOverseasMaturityDt())) {
            query.setParameter("localMaturityDt", bondExpendDto.getLocalMaturityDt());
        } else if (!hasText(bondExpendDto.getLocalMaturityDt()) && hasText(bondExpendDto.getOverseasMaturityDt())) {
            query.setParameter("overseasMaturityDt", bondExpendDto.getOverseasMaturityDt());
        } else if(hasText(bondExpendDto.getLocalMaturityDt()) && hasText(bondExpendDto.getOverseasMaturityDt())){
            query.setParameter("localMaturityDt", bondExpendDto.getLocalMaturityDt());
            query.setParameter("overseasMaturityDt", bondExpendDto.getOverseasMaturityDt());
        }
        if(nonNull(bondExpendDto.getGuaranteeAmt())) {
            query.setParameter("guaranteeAmt", bondExpendDto.getGuaranteeAmt());
        }
        return new JpaResultMapper().list(query, BondExpendDto.class);
    }

    @Override
    public List<BondExpendDto> getBondRefNoList(BondMstDto bondMstDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "       BM.REF_NO " +
            ",      BM.CUSTOMER_NM " +
            ",      TO_CHAR(BM.OPENING_DT, 'YYYYMMDD') AS OPENING_DT " +
            ",      BM.BEN_COUNTRY_CD " +
            ",      BM.BEN_COUNTRY " +
            ",      BM.CUSTOMER_ID " +
            ",      BM.INT_BANK_NM " +
            ",      BM.LOCAL_BANK_NM " +
            ",      BM.BUDGET " +
            ",      BM.PROJECT_NM " +
            ",      BM.PROJECT_ID " +
            ",      BM.CURRENCY_CD " +
            ",      BM.CURRENCY_AMT " +
            ",      TO_CHAR(NVL(MAX(AMEND_SEQ),0)) AS AMEND_SEQ " +
            ",      COUNT(SB.REF_NO) AS COUNT " +
            ",      BM.BOND_CD " +
            "  FROM TB_BOND_MASTER BM " +
            "  LEFT OUTER JOIN ( " +
            "       SELECT " +
            "       SH.STATUS " +
            "      , SH.COMP_CD " +
            "      , SH.SLIP_NO " +
            "      , B.REF_NO " +
            "      , B.AMEND_SEQ " +
            "  FROM TB_SLIP_HD SH " +
            "     , TB_BOND_HD B " +
            "  WHERE SH.COMP_CD = B.COMP_CD " +
            "  AND SH.SLIP_NO = B.SLIP_NO " +
            "  AND SH.STATUS IN ('CC','IC' ,'BD9', '04') " +
            "  AND B.TYPE = 'LOCAL' " +
            "  ) SB " +
            " ON BM.COMP_CD = SB.COMP_CD " +
            " AND BM.REF_NO = SB.REF_NO " +
            " WHERE 1 = 1 " +
            " AND BM.COMP_CD = :compCd " +
            " AND NVL(BM.RELEASE_DT, TO_DATE('29991231', 'YYYYMMDD')) >= SYSDATE ");

        if(!StringUtils.isEmpty(bondMstDto.getRefNo())) {
            sb.append(" AND UPPER(BM.REF_NO) LIKE '%' || UPPER(:refNo) || '%' ");
        }
        if(!StringUtils.isEmpty(bondMstDto.getCustomerNm())) {
            sb.append(" AND UPPER(BM.CUSTOMER_NM) LIKE '%' || UPPER(:customerNm) || '%' ");
        }

        sb.append(" GROUP BY " +
            "       BM.REF_NO " +
            "     , BM.CUSTOMER_NM " +
            "     , BM.OPENING_DT " +
            "     , BM.BEN_COUNTRY_CD " +
            "     , BM.BEN_COUNTRY " +
            "     , BM.CUSTOMER_ID " +
            "     , BM.INT_BANK_NM" +
            "     , BM.LOCAL_BANK_NM" +
            "     , BM.BUDGET" +
            "     , BM.PROJECT_NM" +
            "     , BM.PROJECT_ID " +
            "     , BM.CURRENCY_CD " +
            "     , BM.CURRENCY_AMT " +
            "     , BM.BOND_CD " +
            " ORDER BY BM.OPENING_DT DESC ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", util.getLoginCompCd());
        if(!StringUtils.isEmpty(bondMstDto.getRefNo())) {
            query.setParameter("refNo", bondMstDto.getRefNo());
        }
        if(!StringUtils.isEmpty(bondMstDto.getCustomerNm())) {
            query.setParameter("customerNm", bondMstDto.getCustomerNm());
        }
        return new JpaResultMapper().list(query, BondExpendDto.class);
    }

}

package com.iljin.apiServer.ijeas.slip.etax;

import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class SalesTaxInvoiceRepositoryImpl implements SalesTaxInvoiceRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SalesTaxInvoiceDto> getSalesTaxInvoiceList(SalesTaxInvoiceDto salesTaxInvoiceDto){
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT " +
                        "TV.CUSTOMER_TRX_ID ," +
                        "(SELECT TRX.COMPLETE_FLAG " +
                        "   FROM AR.RA_CUSTOMER_TRX_ALL TRX " +
                        "  WHERE 1 = 1 " +
                        "    AND TRX.CUSTOMER_TRX_ID = TV.CUSTOMER_TRX_ID) AS COMPLETE_FLAG, " +
                        "TV.TRX_NUMBER ," +
                        "TV.GROUP_NUMBER ," +
                        "TV.TRX_DATE ," +
                        "TO_CHAR(TV.GL_DATE, 'YYYY-MM-DD') AS GL_DATE ," +
                        "TV.ORG_ID ," +
                        "TV.CUSTOMER_ID ," +
                        "TV.CUSTOMER_NAME ," +
                        "TV.INTEGRATION_VENDOR_NUM ," +
                        "TV.TAX_REFERENCE ," +
                        "TV.REPRESENTATIVE_NAME ," +
                        "TV.CUST_BUSINESS_CONDITION ," +
                        "TV.CUST_BUSINESS_TYPE ," +
                        "TV.CORPORATION_NUMBER ," +
                        "TV.CUSTOMER_ADDRESS ," +
                        "TV.POSTAL_CODE ," +
                        "TV.PHONE_NUMBER ," +
                        "TV.FAX_NUMBER ," +
                        "TV.E_MAIL ," +
                        "TV.COMMENTS ," +
                        "TV.CURRENCY_CODE ," +
                        "TV.TAX_EVIDENCE_TYPE ," +
                        "TV.TAX_EVIDENCE_TYPE_NAME ," +
                        "TV.TRX_TYPE ," +
                        "TV.SUPBUY_TYPE ," +
                        "CASE WHEN TV.ETAX_ISSUE_ID IS NOT NULL THEN 'Y' " +
                        "     ELSE 'N' " +
                        " END AS ETAX_ISSUE_ID_YN , " +
                        "TV.ETAX_ISSUE_ID ," +
                        "(SELECT COUNT(*) " +
                        "   FROM APPS.CBO_AR_TAX_CUST_TRX_ALL_V TRX " +
                        "  WHERE TRX.TAX_CODE IS NOT NULL " +
                        "    AND TRX.ORG_ID = TV.ORG_ID " +
                        "    AND TRX.ETAX_ISSUE_ID = TV.ETAX_ISSUE_ID) AS ETAX_ISSUE_ID_CNT ," +
                        "TV.DTI_STATUS ," +
                        "(SELECT DT.DETAIL_NM " +
                        "   FROM TB_CODE_DT DT " +
                        "  WHERE DT.GROUP_CD = 'DTI_STATUS' " +
                        "    AND DT.COMP_CD = TO_CHAR(TV.ORG_ID) " +
                        "    AND DT.DETAIL_CD = TV.DTI_STATUS) AS DTI_STATUS_TEXT ," +
                        "TV.TAX_CODE ," +
                        "TV.DTI_TYPE ," +
                        "TV.TOTAL_AMOUNT ," +
                        "TV.SUPPLY_AMOUNT ," +
                        "TV.TAX_AMOUNT ," +
                        "TV.CREATE_DEPT_CODE ," +
                        "(SELECT DEPT.DEPT_NM " +
                        "   FROM TB_MST_CCTR_BLG_ERP DEPT " +
                        "  WHERE DEPT.ENABLED_FLAG = 'Y' " +
                        "    AND DEPT.COMP_CD = TO_CHAR(TV.ORG_ID) " +
                        "    AND DEPT.DEPT_CD = TV.CREATE_DEPT_CODE) AS CREATE_DEPT_NAME ," +
                        "TV.CREATE_EMP_NO ," +
                        "(SELECT EMP.EMP_NM " +
                        "   FROM TB_MST_EMP EMP " +
                        "  WHERE EMP.COMP_CD = TO_CHAR(TV.ORG_ID) " +
                        "    AND EMP.EMP_NO = TV.CREATE_EMP_NO) AS CREATE_EMP_NAME ," +
                        "(SELECT MAX(CATM.ETAX_EXCLUDE_FLAG) " +
                        "   FROM CBO_AR_TRX_MERGE CATM " +
                        "  WHERE ETAX_ISSUE_ID = TV.ETAX_ISSUE_ID) AS ETAX_EXCLUDE_FLAG ," +
                        "TV.TAX_LOCATION ," +
                        "TV.DIRECTION " +
                " FROM APPS.CBO_AR_TAX_CUST_TRX_ALL_V TV " +
                "WHERE TV.TAX_CODE IS NOT NULL " +
                "  AND TV.ORG_ID = :orgId " +
                "  AND TV.GL_DATE BETWEEN TO_DATE(:fromDate, 'YYYYMMDD') AND TO_DATE(:toDate, 'YYYYMMDD') + 0.99999 ");

        if(hasText(salesTaxInvoiceDto.etaxIssueIdYn)){
            if(salesTaxInvoiceDto.etaxIssueIdYn.equals("Y")){
                sb.append("  AND TV.ETAX_ISSUE_ID IS NOT NULL ");
            }else if(salesTaxInvoiceDto.etaxIssueIdYn.equals("N")){
                sb.append("  AND TV.ETAX_ISSUE_ID IS NULL ");
            }
        }

        if(hasText(salesTaxInvoiceDto.completeFlag)){
            sb.append(" AND COMPLETE_FLAG = :accountingFlag ");
        }

        if(hasText(salesTaxInvoiceDto.integrationVendorNum)){
            sb.append(" AND TV.INTEGRATION_VENDOR_NUM = :vendorNum ");
        }

        if(hasText(salesTaxInvoiceDto.createDeptCode)){
            sb.append(" AND TV.CREATE_DEPT_CODE = :deptCode ");
        }

        if(hasText(salesTaxInvoiceDto.createEmpNo)){
            sb.append(" AND TV.CREATE_EMP_NO = :empNo ");
        }

        sb.append(
                "ORDER BY ETAX_ISSUE_ID, INTEGRATION_VENDOR_NUM, GL_DATE"
        );


        Query query = entityManager.createNativeQuery(sb.toString());


        query.setParameter("orgId", salesTaxInvoiceDto.orgId);
        query.setParameter("fromDate", salesTaxInvoiceDto.fromDate);
        query.setParameter("toDate", salesTaxInvoiceDto.toDate);


        if(hasText(salesTaxInvoiceDto.completeFlag)){
            query.setParameter("accountingFlag", salesTaxInvoiceDto.completeFlag);
        }

        if(hasText(salesTaxInvoiceDto.integrationVendorNum)){
            query.setParameter("vendorNum", salesTaxInvoiceDto.integrationVendorNum);
        }

        if(hasText(salesTaxInvoiceDto.createDeptCode)){
            query.setParameter("deptCode", salesTaxInvoiceDto.createDeptCode);
        }

        if(hasText(salesTaxInvoiceDto.createEmpNo)){
            query.setParameter("empNo", salesTaxInvoiceDto.createEmpNo);
        }

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }


    @Override
    public List<SalesTaxInvoiceDto> getLine(Integer trxId){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT LV.LINE_NUMBER " +
                "       , LV.DESCRIPTION " +
                "       , LV.TAX_CLASSIFICATION_CODE " +
                "       , LV.SUPPLY_AMOUNT " +
                "       , LV.TAX_AMOUNT " +
                "       , LV.QUANTITY_INVOICED " +
                "       , LV.UNIT_SELLING_PRICE " +
                "       , LV.UOM_CODE " +
                "       , LV.CUSTOMER_TRX_ID " +
                "       , LV.CUSTOMER_TRX_LINE_ID " +
                "   FROM APPS.CBO_AR_TAX_CUST_TRX_LINE_V LV" +
                "  WHERE LV.CUSTOMER_TRX_ID = :trxId");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("trxId", trxId);

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }



    // 매출세금계산서 발행

    @Override
    public List<SalesTaxInvoiceDto> getSalesTaxInvoiceIssueList(SalesTaxInvoiceDto salesTaxInvoiceDto){
        StringBuilder sb = new StringBuilder();

        sb.append(
                " SELECT TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE " +
                "      , TO_CHAR(MV.GL_DATE, 'YYYY-MM-DD') AS GL_DATE " +
                "      , MV.BYR_COM_REGNO " +
                "      , MV.CUSTOMER_NAME " +
                "      , MV.CONVERSATION_ID " +
                "      , MV.DTI_STATUS " +
                "      , (SELECT DETAIL_NM  " +
                "           FROM TB_CODE_DT  " +
                "          WHERE GROUP_CD = 'DTI_STATUS' " +
                "            AND COMP_CD = :compCd " +
                "            AND DETAIL_CD = MV.DTI_STATUS  " +
                "        ) AS DTI_STATUS_TEXT  " +
                "      , NVL(MV.BYR_EMAIL_ISSUE, (NVL( (SELECT caeit.BYR_EMAIL " +
                "                                        FROM CBO_AR_ETAX_ISSUE caeit " +
                "                                       WHERE caeit.ETAX_ISSUE_ID = (SELECT MAX(caei.ETAX_ISSUE_ID)  " +
                "                                                                      FROM CBO_AR_ETAX_ISSUE caei " +
                "                                                                          ,AR.ra_customer_trx_all rctz " +
                "                                                                     WHERE caei.ETAX_ISSUE_ID = rctz.GLOBAL_ATTRIBUTE21 " +
                "                                                                       AND rctz.BILL_TO_CUSTOMER_ID = MV.CUSTOMER_ID)) " +
                "                                    , MV.BYR_EMAIL_CUST))) AS BYR_EMAIL " +
                "      , MV.TAX_CODE " +
                "      , MV.SUPPLY_AMOUNT " +
                "      , MV.TAX_AMOUNT " +
                "      , MV.TOTAL_AMOUNT " +
                "      , MV.REMARK   " +
                "      , MV.ORG_ID   " +
                "      , MV.ETAX_ISSUE_ID   " +
                "      , MV.BYR_TEL_NUM   " +
                "      , MV.CUSTOMER_ID " +
                "      , MV.INTEGRATION_VENDOR_NUM " +
                "      , MV.BYR_REP_NAME " +
                "      , MV.BYR_COM_TYPE " +
                "      , MV.BYR_COM_CLASSIFY " +
                "      , MV.CORPORATION_NUMBER " +
                "      , MV.BYR_COM_ADDR " +
                "      , MV.CURRENCY_CODE " +
                "      , NVL(MV.SUPBUY_TYPE, 'AR') AS SUPBUY_TYPE " +
                "      , (CASE WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AP' THEN '매입' " +
                "              WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AR' THEN '매출' END" +
                "        ) AS SUPBUY_TYPE_TEXT  " +
                "      , MV.DIRECTION " +
                "      , MV.INTERFACE_BATCH_ID " +
                "      , MV.TAX_DEMAND " +
                "      , MV.SUP_EMP_NAME " +
                "      , MV.SUP_EMAIL " +
                "      , MV.SUP_TEL_NUM " +
                "      , MV.SUP_COM_NAME " +
                "      , MV.SUP_COM_REGNO " +
                "      , MV.SUP_REP_NAME " +
                "      , MV.SUP_COM_CLASSIFY " +
                "      , MV.SUP_COM_TYPE " +
                "      , MV.SUP_COM_ADDR " +
                "      , MV.DTI_STATUS_MEANING " +
                "      , MV.APPROVE_ID " +
                "      , MV.DTI_TYPE AS DTI_TYPE " +
                "      , (SELECT DETAIL_NM  " +
                "           FROM TB_CODE_DT  " +
                "          WHERE GROUP_CD = 'ETAX_TYPE' " +
                "            AND COMP_CD = :compCd " +
                "            AND DETAIL_CD = MV.DTI_TYPE " +
                "        ) AS DTI_TYPE_TEXT  " +
                "      , MV.ITEM_NAME " +
                "      , (SELECT RETURN_DESCRIPTION FROM CBOTAX.XXSB_DTI_STATUS WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_DESCRIPTION " +
                "      , (SELECT RETURN_CODE FROM CBOTAX.XXSB_DTI_STATUS WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_CODE " +
                "      , MV.AMEND_CODE " +
                "      , MV.ORI_ISSUE_ID " +
                "      , MV.ORI_ETAX_ISSUE_ID " +
                "      , TO_CHAR(MV.ORI_DTI_WDATE, 'YYYY-MM-DD') AS ORI_DTI_WDATE " +
                "      , TO_CHAR(MV.LOCAL_LC_OPEN_DATE, 'YYYY-MM-DD') AS LOCAL_LC_OPEN_DATE " +
                "      , MV.CREATE_DEPT_CODE  " +
                "      , (SELECT DEPT_NM " +
                "           FROM TB_MST_CCTR_BLG_ERP " +
                "          WHERE COMP_CD = MV.ORG_ID " +
                "            AND DEPT_CD = MV.CREATE_DEPT_CODE  " +
                "        ) AS CREATE_DEPT_NAME " +
                "      , MV.CREATE_EMP_NO  " +
                "      , (SELECT EMP_NM FROM TB_MST_EMP WHERE COMP_CD = MV.ORG_ID AND EMP_NO = MV.CREATE_EMP_NO) AS CREATE_EMP_NAME  " +
                "      , NVL((SELECT NVL(cgcl.attribute1, '0000') " +
                "               FROM APPS.hr_locations_all   hla " +
                "                  , APPS.cbo_gl_code_header cgch " +
                "                  , APPS.cbo_gl_code_line   cgcl " +
                "              WHERE hla.global_attribute3 = cgcl.code " +
                "                AND cgcl.code_type_id = cgch.code_type_id " +
                "                AND hla.LEGAL_ADDRESS_FLAG = 'N' " +
                "                AND hla.GLOBAL_ATTRIBUTE3 = ( SELECT coa.ATTRIBUTE1 " +
                "                                                FROM APPS.cbo_sp_emp_simple_v  emp " +
                "                                                   , APPS.cbo_gl_coa_segment_v coa " +
                "                                               WHERE 1 = 1 " +
                "                                                 AND emp.emp_no = :empNo " +
                "                                                 AND emp.dept_code = coa.value_code " +
                "                                                 AND coa.segment_num = '3' " +
                "                                                 AND coa.ledger_id = :ledgerId) " +
                "                AND cgch.code_type = 'CD0170' " +
                "        ), '0000') AS SUP_BIZPLACE_CODE  " +
                "      , MV.BYR_BIZPLACE_CODE  " +
                "      , (CASE (SELECT SEND_REQUEST FROM CBOTAX.XXSB_DTI_NTS WHERE CONVERSATION_ID = MV.CONVERSATION_ID) " +
                "               WHEN '9' THEN '미전송' " +
                "               WHEN '0' THEN '전송중' " +
                "               WHEN '1' THEN '국세청 처리중' " +
                "               WHEN '2' THEN '전송실패(전)' " +
                "               WHEN '3' THEN '전송실패(후)' " +
                "               WHEN '7' THEN '전송완료' " +
                "               ELSE '' END) AS SEND_REQUEST " +
                "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V MV " +
                " WHERE MV.ORG_ID = :compCd " +
                "   AND (MV.SUPPLY_AMOUNT <> '0' OR MV.TAX_AMOUNT <> '0') ");


        //조건 설정
        if(salesTaxInvoiceDto.dtiStatus.equals("X")){
            sb.append("   AND (MV.DTI_STATUS IS NULL OR MV.DTI_STATUS= 'S') ");
        }else if(!salesTaxInvoiceDto.dtiStatus.equals("ALL")){
            sb.append("   AND MV.DTI_STATUS= '" + salesTaxInvoiceDto.dtiStatus + "' ");
        }


        sb.append("   AND MV.GL_DATE BETWEEN TO_DATE(:postDtFrom, 'YYYYMMDD') AND TO_DATE(:postDtTo, 'YYYYMMDD') + 0.99999 ");

        if(hasText(salesTaxInvoiceDto.integrationVendorNum)){
            sb.append("   AND MV.INTEGRATION_VENDOR_NUM = :integrationVendorNum ");
        }

        if(hasText(salesTaxInvoiceDto.issueDtFrom) && hasText(salesTaxInvoiceDto.issueDtTo)){
            sb.append("   AND MV.DTI_WDATE BETWEEN TO_DATE(:issueDtFrom, 'YYYYMMDD') AND TO_DATE(:issueDtTo, 'YYYYMMDD') + 0.99999 ");
        }

        if(hasText(salesTaxInvoiceDto.wrtDeptCd)){
            sb.append("   AND MV.CREATE_DEPT_CODE = :wrtDeptCd ");
        }

        if(hasText(salesTaxInvoiceDto.wrtId)){
            sb.append("   AND MV.CREATE_EMP_NO = :wrtId ");
        }





        sb.append(" ORDER BY MV.INTEGRATION_VENDOR_NUM, MV.DTI_WDATE " );


        Query query = entityManager.createNativeQuery(sb.toString());


        query.setParameter("compCd", salesTaxInvoiceDto.orgId);
        query.setParameter("empNo", salesTaxInvoiceDto.empNo);
        query.setParameter("ledgerId", 2021);
        query.setParameter("postDtFrom", salesTaxInvoiceDto.postDtFrom);
        query.setParameter("postDtTo", salesTaxInvoiceDto.postDtTo);

        if(hasText(salesTaxInvoiceDto.integrationVendorNum)){
            query.setParameter("integrationVendorNum", salesTaxInvoiceDto.integrationVendorNum);
        }

        if(hasText(salesTaxInvoiceDto.issueDtFrom) && hasText(salesTaxInvoiceDto.issueDtTo)){
            query.setParameter("issueDtFrom", salesTaxInvoiceDto.issueDtFrom);
            query.setParameter("issueDtTo", salesTaxInvoiceDto.issueDtTo);
        }

        if(hasText(salesTaxInvoiceDto.wrtDeptCd)){
            query.setParameter("wrtDeptCd", salesTaxInvoiceDto.wrtDeptCd);
        }

        if(hasText(salesTaxInvoiceDto.wrtId)){
            query.setParameter("wrtId", salesTaxInvoiceDto.wrtId);
        }


        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }


    @Override
    public List<SalesTaxInvoiceDto> getItem(Integer etaxIssueId){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT IV.ETAX_ISSUE_ID " +
                "       , IV.CONVERSATION_ID " +
                "       , IV.DTI_LINE_NUM " +
                "       , IV.ITEM_NAME " +
                "       , IV.ITEM_QTY " +
                "       , TO_CHAR(IV.ITEM_MD,'YYYY-MM-DD') AS ITEM_MD " +
                "       , IV.UNIT_PRICE " +
                "       , IV.SUPPLY_AMOUNT " +
                "       , IV.TAX_AMOUNT " +
                "   FROM APPS.CBO_AR_ETAX_DTI_ITEM_V IV " +
               // "   LEFT OUTER JOIN CBOTAX.XXSB_DTI_ITEM_UPDATE UP " +
               // "     ON IV.ETAX_ISSUE_ID = UP.ETAX_ISSUE_ID " +
                "  WHERE IV.ETAX_ISSUE_ID = :etaxIssueId");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("etaxIssueId", etaxIssueId);

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }

    @Override
    public List<SalesTaxInvoiceDto> getSlipNo(Integer etaxIssueId){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT RCTA.TRX_NUMBER " +
                "       , '' AS TEMP " +
                "    FROM CBO_AR_TRX_MERGE CATM " +
                "       , AR.RA_CUSTOMER_TRX_ALL RCTA " +
                "   WHERE CATM.ETAX_ISSUE_ID = :etaxIssueId " +
                "     AND CATM.CUSTOMER_TRX_ID = RCTA.CUSTOMER_TRX_ID " +
                "     AND CATM.DELETE_FLAG = 'N' " +
                "   ORDER BY RCTA.TRX_NUMBER ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("etaxIssueId", etaxIssueId);

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }

    @Override
    public List<SalesTaxInvoiceDto> getModifyInfo(BigDecimal compCd, String oriIssueId){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT " +
                "       TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE ," +
                "       '' AS GL_DATE ," +
                "       MV.BYR_COM_REGNO ," +
                "       MV.CUSTOMER_NAME ," +
                "       MV.CONVERSATION_ID ," +
                "       MV.DTI_STATUS ," +
                "       (SELECT DETAIL_NM " +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'DTI_STATUS' " +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = MV.DTI_STATUS ) AS DTI_STATUS_TEXT ," +
                "       MV.BYR_EMAIL ," +
                "       MV.TAX_CODE ," +
                "       MV.SUPPLY_AMOUNT ," +
                "       MV.TAX_AMOUNT ," +
                "       NVL(MV.TOTAL_AMOUNT, 0) AS TOTAL_AMOUNT ," +
                "       MV.REMARK ," +
                "       MV.ORG_ID ," +
                "       MV.ETAX_ISSUE_ID ," +
                "       MV.BYR_TEL_NUM ," +
                "       MV.CUSTOMER_ID ," +
                "       MV.INTEGRATION_VENDOR_NUM ," +
                "       MV.BYR_REP_NAME ," +
                "       MV.BYR_COM_TYPE ," +
                "       MV.BYR_COM_CLASSIFY ," +
                "       MV.CORPORATION_NUMBER ," +
                "       MV.BYR_COM_ADDR ," +
                "       MV.CURRENCY_CODE ," +
                "       NVL(MV.SUPBUY_TYPE, 'AR') AS SUPBUY_TYPE ," +
                "       (CASE WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AP' THEN '매입' " +
                "             WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AR' THEN '매출' END" +
                "        ) AS SUPBUY_TYPE_TEXT , " +
                "       MV.DIRECTION ," +
                "       MV.INTERFACE_BATCH_ID ," +
                "       MV.TAX_DEMAND ," +
                "       MV.SUP_EMP_NAME ," +
                "       MV.SUP_EMAIL ," +
                "       MV.SUP_TEL_NUM ," +
                "       MV.SUP_COM_NAME ," +
                "       MV.SUP_COM_REGNO ," +
                "       MV.SUP_REP_NAME ," +
                "       MV.SUP_COM_CLASSIFY ," +
                "       MV.SUP_COM_TYPE ," +
                "       MV.SUP_COM_ADDR ," +
                "       MV.DTI_STATUS_MEANING ," +
                "       MV.APPROVE_ID ," +
                "       NVL(MV.DTI_TYPE, '01') AS DTI_TYPE ," +
                "       (SELECT DETAIL_NM" +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'ETAX_TYPE'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = NVL(MV.DTI_TYPE, '01') ) AS DTI_TYPE_TEXT ," +
                "       MV.ITEM_NAME ," +
                "       (SELECT RETURN_DESCRIPTION" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_DESCRIPTION ," +
                "       (SELECT RETURN_CODE" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_CODE ," +
                "       MV.AMEND_CODE ," +
                "       MV.ORI_ISSUE_ID ," +
                "       MV.ORI_ETAX_ISSUE_ID ," +
                "       TO_CHAR(MV.ORI_DTI_WDATE, 'YYYY-MM-DD') AS ORI_DTI_WDATE ," +
                "       TO_CHAR(MV.LOCAL_LC_OPEN_DATE, 'YYYY-MM-DD') AS LOCAL_LC_OPEN_DATE ," +
                "       '' AS createDeptCode ," +
                "       '' AS createDeptName ," +
                "       '' AS createEmpNo ," +
                "       '' AS createEmpName ," +
                "       '' AS supBizplaceCode ," +
                "       '' AS byrBizplaceCode ," +
                "       '' AS sendRequest" +
                "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V MV" +
                " WHERE MV.ORG_ID = :compCd" +
                "   AND MV.APPROVE_ID = :oriIssueId    " +

                "UNION ALL " +
                "SELECT " +
                "       TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE ," +
                "       '' AS GL_DATE ," +
                "       MV.BYR_COM_REGNO ," +
                "       MV.CUSTOMER_NAME ," +
                "       MV.CONVERSATION_ID ," +
                "       MV.DTI_STATUS ," +
                "       (SELECT DETAIL_NM " +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'DTI_STATUS'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = MV.DTI_STATUS ) AS DTI_STATUS_TEXT ," +
                "       MV.BYR_EMAIL ," +
                "       MV.TAX_CODE ," +
                "       MV.SUPPLY_AMOUNT ," +
                "       MV.TAX_AMOUNT ," +
                "       NVL(MV.TOTAL_AMOUNT, 0) AS TOTAL_AMOUNT ," +
                "       MV.REMARK ," +
                "       MV.ORG_ID ," +
                "       MV.ETAX_ISSUE_ID ," +
                "       MV.BYR_TEL_NUM ," +
                "       MV.CUSTOMER_ID ," +
                "       MV.INTEGRATION_VENDOR_NUM ," +
                "       MV.BYR_REP_NAME ," +
                "       MV.BYR_COM_TYPE ," +
                "       MV.BYR_COM_CLASSIFY ," +
                "       MV.CORPORATION_NUMBER ," +
                "       MV.BYR_COM_ADDR ," +
                "       MV.CURRENCY_CODE ," +
                "       NVL(MV.SUPBUY_TYPE, 'AR') AS SUPBUY_TYPE ," +
                "       (CASE WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AP' THEN '매입' " +
                "             WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AR' THEN '매출' END" +
                "        ) AS SUPBUY_TYPE_TEXT , " +
                "       MV.DIRECTION ," +
                "       MV.INTERFACE_BATCH_ID ," +
                "       MV.TAX_DEMAND ," +
                "       MV.SUP_EMP_NAME ," +
                "       MV.SUP_EMAIL ," +
                "       MV.SUP_TEL_NUM ," +
                "       MV.SUP_COM_NAME ," +
                "       MV.SUP_COM_REGNO ," +
                "       MV.SUP_REP_NAME ," +
                "       MV.SUP_COM_CLASSIFY ," +
                "       MV.SUP_COM_TYPE ," +
                "       MV.SUP_COM_ADDR ," +
                "       MV.DTI_STATUS_MEANING ," +
                "       MV.APPROVE_ID ," +
                "       NVL(MV.DTI_TYPE, '01') AS DTI_TYPE ," +
                "       (SELECT DETAIL_NM" +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'ETAX_TYPE'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = NVL(MV.DTI_TYPE, '01') ) AS DTI_TYPE_TEXT ," +
                "       MV.ITEM_NAME ," +
                "       (SELECT RETURN_DESCRIPTION" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_DESCRIPTION ," +
                "       (SELECT RETURN_CODE" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_CODE ," +
                "       MV.AMEND_CODE ," +
                "       MV.ORI_ISSUE_ID ," +
                "       MV.ORI_ETAX_ISSUE_ID ," +
                "       TO_CHAR(MV.ORI_DTI_WDATE, 'YYYY-MM-DD') AS ORI_DTI_WDATE ," +
                "       TO_CHAR(MV.LOCAL_LC_OPEN_DATE, 'YYYY-MM-DD') AS LOCAL_LC_OPEN_DATE ," +
                "       '' AS createDeptCode ," +
                "       '' AS createDeptName ," +
                "       '' AS createEmpNo ," +
                "       '' AS createEmpName ," +
                "       '' AS supBizplaceCode ," +
                "       '' AS byrBizplaceCode ," +
                "       '' AS sendRequest" +
                "  FROM cboslip.cbo_ar_etax_old_dti_main MV" +
                " WHERE MV.ORG_ID = :compCd" +
                "   AND MV.APPROVE_ID = :oriIssueId    ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("oriIssueId", oriIssueId);

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }

    @Override
    public List<SalesTaxInvoiceDto> getModifyList(SalesTaxInvoiceDto salesTaxInvoiceDto){
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT " +
                "       TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE ," +
                "       '' AS GL_DATE ," +
                "       MV.BYR_COM_REGNO ," +
                "       MV.CUSTOMER_NAME ," +
                "       MV.CONVERSATION_ID ," +
                "       MV.DTI_STATUS ," +
                "       (SELECT DETAIL_NM " +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'DTI_STATUS'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = MV.DTI_STATUS ) AS DTI_STATUS_TEXT ," +
                "       MV.BYR_EMAIL ," +
                "       MV.TAX_CODE ," +
                "       MV.SUPPLY_AMOUNT ," +
                "       MV.TAX_AMOUNT ," +
                "       NVL(MV.TOTAL_AMOUNT, 0) AS TOTAL_AMOUNT ," +
                "       MV.REMARK ," +
                "       MV.ORG_ID ," +
                "       MV.ETAX_ISSUE_ID ," +
                "       MV.BYR_TEL_NUM ," +
                "       MV.CUSTOMER_ID ," +
                "       MV.INTEGRATION_VENDOR_NUM ," +
                "       MV.BYR_REP_NAME ," +
                "       MV.BYR_COM_TYPE ," +
                "       MV.BYR_COM_CLASSIFY ," +
                "       MV.CORPORATION_NUMBER ," +
                "       MV.BYR_COM_ADDR ," +
                "       MV.CURRENCY_CODE ," +
                "       NVL(MV.SUPBUY_TYPE, 'AR') AS SUPBUY_TYPE ," +
                "       (CASE WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AP' THEN '매입' " +
                "             WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AR' THEN '매출' END" +
                "        ) AS SUPBUY_TYPE_TEXT , " +
                "       MV.DIRECTION ," +
                "       MV.INTERFACE_BATCH_ID ," +
                "       MV.TAX_DEMAND ," +
                "       MV.SUP_EMP_NAME ," +
                "       MV.SUP_EMAIL ," +
                "       MV.SUP_TEL_NUM ," +
                "       MV.SUP_COM_NAME ," +
                "       MV.SUP_COM_REGNO ," +
                "       MV.SUP_REP_NAME ," +
                "       MV.SUP_COM_CLASSIFY ," +
                "       MV.SUP_COM_TYPE ," +
                "       MV.SUP_COM_ADDR ," +
                "       MV.DTI_STATUS_MEANING ," +
                "       MV.APPROVE_ID ," +
                "       NVL(MV.DTI_TYPE, '01') AS DTI_TYPE ," +
                "       (SELECT DETAIL_NM" +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'ETAX_TYPE'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = NVL(MV.DTI_TYPE, '01') ) AS DTI_TYPE_TEXT ," +
                "       MV.ITEM_NAME ," +
                "       (SELECT RETURN_DESCRIPTION" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_DESCRIPTION ," +
                "       (SELECT RETURN_CODE" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_CODE ," +
                "       MV.AMEND_CODE ," +
                "       MV.ORI_ISSUE_ID ," +
                "       MV.ORI_ETAX_ISSUE_ID ," +
                "       TO_CHAR(MV.ORI_DTI_WDATE, 'YYYY-MM-DD') AS ORI_DTI_WDATE ," +
                "       TO_CHAR(MV.LOCAL_LC_OPEN_DATE, 'YYYY-MM-DD') AS LOCAL_LC_OPEN_DATE ," +
                "       '' AS createDeptCode ," +
                "       '' AS createDeptName ," +
                "       '' AS createEmpNo ," +
                "       '' AS createEmpName ," +
                "       '' AS supBizplaceCode ," +
                "       '' AS byrBizplaceCode ," +
                "       '' AS sendRequest" +
                "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V MV " +
                "     , CBOTAX.XXSB_DTI_NTS TS " +
                " WHERE MV.ORG_ID = :compCd" +
                "   AND MV.CONVERSATION_ID = TS.CONVERSATION_ID  " +
                "   AND MV.INTEGRATION_VENDOR_NUM = :integrationVendorNum " +
                "   AND MV.CURRENCY_CODE = :currencyCode " +
                "   AND (TS.SEND_REQUEST IN ('7') OR MV.DTI_STATUS IN ('C')) " +
                "   AND MV.APPROVE_ID <> NVL(MV.ORI_ISSUE_ID, 'X') " +
                "   AND MV.ORI_ISSUE_ID IS NULL   ");

                if(salesTaxInvoiceDto.amendCode.equals("05")){
                    sb.append("   AND MV.TAX_CODE = :taxCode ");
                }

        sb.append("UNION ALL " +
                "SELECT " +
                "       TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE ," +
                "       '' AS GL_DATE ," +
                "       MV.BYR_COM_REGNO ," +
                "       MV.CUSTOMER_NAME ," +
                "       MV.CONVERSATION_ID ," +
                "       MV.DTI_STATUS ," +
                "       (SELECT DETAIL_NM " +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'DTI_STATUS'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = MV.DTI_STATUS ) AS DTI_STATUS_TEXT ," +
                "       MV.BYR_EMAIL ," +
                "       MV.TAX_CODE ," +
                "       MV.SUPPLY_AMOUNT ," +
                "       MV.TAX_AMOUNT ," +
                "       NVL(MV.TOTAL_AMOUNT, 0) AS TOTAL_AMOUNT ," +
                "       MV.REMARK ," +
                "       MV.ORG_ID ," +
                "       MV.ETAX_ISSUE_ID ," +
                "       MV.BYR_TEL_NUM ," +
                "       MV.CUSTOMER_ID ," +
                "       MV.INTEGRATION_VENDOR_NUM ," +
                "       MV.BYR_REP_NAME ," +
                "       MV.BYR_COM_TYPE ," +
                "       MV.BYR_COM_CLASSIFY ," +
                "       MV.CORPORATION_NUMBER ," +
                "       MV.BYR_COM_ADDR ," +
                "       MV.CURRENCY_CODE ," +
                "       NVL(MV.SUPBUY_TYPE, 'AR') AS SUPBUY_TYPE ," +
                "       (CASE WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AP' THEN '매입' " +
                "             WHEN NVL(MV.SUPBUY_TYPE, 'AR') = 'AR' THEN '매출' END" +
                "        ) AS SUPBUY_TYPE_TEXT , " +
                "       MV.DIRECTION ," +
                "       MV.INTERFACE_BATCH_ID ," +
                "       MV.TAX_DEMAND ," +
                "       MV.SUP_EMP_NAME ," +
                "       MV.SUP_EMAIL ," +
                "       MV.SUP_TEL_NUM ," +
                "       MV.SUP_COM_NAME ," +
                "       MV.SUP_COM_REGNO ," +
                "       MV.SUP_REP_NAME ," +
                "       MV.SUP_COM_CLASSIFY ," +
                "       MV.SUP_COM_TYPE ," +
                "       MV.SUP_COM_ADDR ," +
                "       MV.DTI_STATUS_MEANING ," +
                "       MV.APPROVE_ID ," +
                "       NVL(MV.DTI_TYPE, '01') AS DTI_TYPE ," +
                "       (SELECT DETAIL_NM" +
                "          FROM TB_CODE_DT" +
                "         WHERE GROUP_CD = 'ETAX_TYPE'" +
                "           AND COMP_CD = :compCd " +
                "           AND DETAIL_CD = NVL(MV.DTI_TYPE, '01') ) AS DTI_TYPE_TEXT ," +
                "       MV.ITEM_NAME ," +
                "       (SELECT RETURN_DESCRIPTION" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_DESCRIPTION ," +
                "       (SELECT RETURN_CODE" +
                "          FROM CBOTAX.XXSB_DTI_STATUS" +
                "         WHERE CONVERSATION_ID = MV.CONVERSATION_ID) AS RETURN_CODE ," +
                "       MV.AMEND_CODE ," +
                "       MV.ORI_ISSUE_ID ," +
                "       MV.ORI_ETAX_ISSUE_ID ," +
                "       TO_CHAR(MV.ORI_DTI_WDATE, 'YYYY-MM-DD') AS ORI_DTI_WDATE ," +
                "       TO_CHAR(MV.LOCAL_LC_OPEN_DATE, 'YYYY-MM-DD') AS LOCAL_LC_OPEN_DATE ," +
                "       '' AS createDeptCode ," +
                "       '' AS createDeptName ," +
                "       '' AS createEmpNo ," +
                "       '' AS createEmpName ," +
                "       '' AS supBizplaceCode ," +
                "       '' AS byrBizplaceCode ," +
                "       '' AS sendRequest" +
                "  FROM cboslip.cbo_ar_etax_old_dti_main MV" +
                " WHERE MV.ORG_ID = :compCd " +
                "   AND MV.INTEGRATION_VENDOR_NUM = :integrationVendorNum    " +
                "   AND MV.APPROVE_ID <> NVL(MV.ORI_ISSUE_ID, 'X') " +
                "   AND MV.ORI_ISSUE_ID IS NULL " +
                "   AND MV.DTI_STATUS IN ('C') ");

                if(salesTaxInvoiceDto.amendCode.equals("05")){
                    sb.append("   AND MV.TAX_CODE = :taxCode ");
                }

        sb.append("ORDER BY DTI_WDATE DESC");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", salesTaxInvoiceDto.orgId);
        query.setParameter("integrationVendorNum", salesTaxInvoiceDto.integrationVendorNum);
        query.setParameter("currencyCode", salesTaxInvoiceDto.currencyCode);

        if(salesTaxInvoiceDto.amendCode.equals("05")) {
            query.setParameter("taxCode", salesTaxInvoiceDto.taxCode);
        }

        return new JpaResultMapper().list(query, SalesTaxInvoiceDto.class);
    }
}

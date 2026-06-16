package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class ErpSubmitRepositoryCustomImpl implements ErpSubmitRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;



    @Override
    public List<ErpSlipSubmitDto> getErpSlipSubmitList(ErpSlipSubmitDto erpSlipSubmitDto) {

        String compCd = util.getLoginCompCd();


        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM ( " +
                "  SELECT   NVL(CSSH.TRX_TYPE_CODE, GL.SLIP_TYPE) AS SLIP_TYPE " +
                "                , NVL(CSSH.ORG_ID, GL.COMP_CD) AS COMP_CD" +
                "                , NVL(CSSH.SLIP_NUMBER, GL.SLIP_NO) AS SLIP_NO" +
                "                , NVL(CSSH.SLIP_HEADER_ID, GL.SLIP_HEADER_ID) AS SLIP_HEADER_ID" +
                "                , NVL(CSSH.APPROVAL_GROUP_ID, GL.APPROVAL_GROUP_ID) AS APPROVAL_GROUP_ID" +
//                "     , NVL(APPS.CBO_SP_COMMON_PKG.GET_TRX_TYPE_NAME(CSSH.TRX_TYPE_CODE) , GL.SLIP_TYPE) AS SLIP_TYPE_TEXT " +
//                "                , SLIP_GET_USER_NAME(GL.COMP_CD, GL.ERP_APP_USER_ID) || '_' || GL.ERP_APP_USER_ID AS CREATE_USER " +
                "                , NVL((SELECT TRX.TRX_TYPE_NM FROM TB_MST_TRX TRX WHERE TRX.TRX_TYPE_CD = CSSH.TRX_TYPE_CODE AND TRX.COMP_CD = CSSH.ORG_ID) , GL.SLIP_TYPE) AS SLIP_TYPE_TEXT" +
                "                , NVL((SELECT EMP.EMP_NM FROM TB_MST_EMP EMP WHERE GL.ERP_APP_USER_ID = EMP.EMP_NO) || '_' || GL.ERP_APP_USER_ID, '') AS CREATE_USER" +
                "                , CASE WHEN NVL(CSSH.SLIP_DATA_FIX_FLAG, 'N') = 'Y' THEN 'Y' ELSE 'N' END AS REPORT_ARRAIGN" +
                "                , CASE WHEN NVL(CSSH.SLIP_STATUS, GL.STATUS) IN ('CP', 'CC', 'IC', 'IE') THEN 'Y' ELSE 'N' END AS APPROVAL_ARRAIGN " +
                "                , CASE WHEN NVL(CSSH.SLIP_STATUS, GL.STATUS) IN ('CC', 'IC', 'IE') THEN 'Y' ELSE 'N' END AS SEAL_ARRAIGN " +
                "                , CSSH.INTEGRATION_VENDOR_NUM" +
                "                , (SELECT INTEGRATION_VENDOR_NAME" +
                "                     FROM APPS.CBO_SP_VENDOR_CUSTOMER_V " +
                "                    WHERE ORG_ID = CSSH.ORG_ID " +
                "                      AND INTEGRATION_VENDOR_NUM = CSSH.INTEGRATION_VENDOR_NUM " +
                "                  ) AS STORE_NAME " +
                "                , CSSH.VALIDATION_FLAG AS ERP_VALIDATION" +
                "                , CSSH.ENTERED_AMOUNT " +
                "                , CSSH.ACCOUNTED_AMOUNT AS USED_AMT " +
                "                , CSSH.VALIDATION_FLAG AS APPROVAL_STATUS " +
                "                , CSSH.SLIP_CURRENCY_CODE " +
                "                , CASE WHEN CSSH.SLIP_CURRENCY_CODE <> 'KRW' THEN CSSH.ENTERED_AMOUNT " +
                "                   ELSE 0     " +
                "                  END AS USED_F_AMT   " +
//                "                , TO_CHAR(CSSH.GL_DATE, 'YYYY-MM-DD') AS GL_DATE " +
                "                , CSSH.GL_DATE " +
                "                , CSSH.SLIP_DATA_FIX_FLAG " +
                "                , CASE WHEN GL.SLIP_TYPE = 'GROUP' " +
                "                  THEN (CASE WHEN (SELECT COUNT(*) FROM TB_SLIP_HD WHERE COMP_CD = GL.COMP_CD AND APPROVAL_GROUP_ID = GL.APPROVAL_GROUP_ID AND APPROVAL_GROUP_ID <> SLIP_HEADER_ID)  " +
                "                   != (SELECT COUNT(*) FROM APPS.CBO_SP_SLIP_HEADER WHERE ORG_ID = GL.COMP_CD AND APPROVAL_GROUP_ID = GL.APPROVAL_GROUP_ID AND SLIP_IF_FLAG = 'Y')  " +
                "                 THEN 'N'" +
                "                 ELSE 'Y' END)" +
                "                  ELSE CSSH.SLIP_IF_FLAG" +
                "                 END AS SLIP_IF_FLAG " +
                "                , CSSH.SLIP_INTERFACE_ERROR_MSG " +
                "                , NVL(CSSH.SLIP_FORCED_IF_FLAG, GL.TRANSFER_TYPE) AS TRANSFER_TYPE " +
                "               , GL.STATUS " +
                "               , GL.POSTING_DT" +
                "               , GL.ERP_APP_USER_ID" +
                "                 , CSSH.SLIP_STATUS" +
                "                 , (SELECT LINE_MEANING" +
                "                      FROM APPS.CBO_GL_CODE_V" +
                "                     WHERE 1 = 1 " +
                "                       AND CODE_TYPE = 'CD0160'" +
                "                       AND HEADER_ENABLED_FLAG = 'Y'" +
                "                       AND LINE_ENABLED_FLAG = 'Y'" +
                "                       AND CODE = NVL(CSSH.SLIP_STATUS, GL.STATUS)" +
                "                   ) AS SLIP_STATUS_TEXT" +
                "                 , (SELECT LINE_MEANING" +
                "                      FROM APPS.CBO_GL_CODE_V" +
                "                     WHERE 1 = 1 " +
                "                       AND CODE_TYPE = 'CD0160'" +
                "                       AND HEADER_ENABLED_FLAG = 'Y'" +
                "                       AND LINE_ENABLED_FLAG = 'Y'" +
                "                       AND CODE = NVL(CSSH.SLIP_STATUS, GL.STATUS)" +
                "                   ) AS ERP_STATE " +
                "                 , (SELECT LINE_MEANING" +
                "                      FROM APPS.CBO_GL_CODE_V" +
                "                     WHERE 1 = 1 " +
                "                       AND CODE_TYPE = 'CD0160'" +
                "                       AND HEADER_ENABLED_FLAG = 'Y'" +
                "                       AND LINE_ENABLED_FLAG = 'Y'" +
                "                       AND CODE = NVL(CSSH.SLIP_STATUS, GL.STATUS)" +
                "                   ) AS SLIP_STATUS_NAME" +
                "                , CASE WHEN GL.SLIP_TYPE = 'GROUP'" +
                "                  THEN 'Y'" +
                "                  ELSE '' " +
                "                 END AS SLIP_GROUP_YN " +
                "                 , CSSH.LEDGER_ID" +
                "                 , CSSH.SLIP_GROUP_NUMBER" +
                "                 , CSSH.TTYPE_INPUT_MODULE" +
                "                 , GL.SLIP_FORM" +
                "                 , GL.ERP_INVOICE_ID" +
                "  FROM TB_SLIP_HD GL  " +
                "  LEFT OUTER JOIN APPS.CBO_SP_SLIP_HEADER CSSH" +
                "  ON GL.COMP_CD = CSSH.ORG_ID" +
                "  AND GL.SLIP_HEADER_ID = CSSH.SLIP_HEADER_ID" +
                "  WHERE  GL.SLIP_HEADER_ID = GL.APPROVAL_GROUP_ID " +
                "  AND  CSSH.SLIP_DISPLAY_FLAG = 'Y' " +
                "  AND NVL(CSSH.SLIP_STATUS, GL.STATUS) NOT IN ('SD', 'AR', 'CR')" +
                "  AND GL.SLIP_TYPE NOT IN ('21', '22', '23', '24', '25', '90' ,'92')" +
                "  AND  GL.COMP_CD = :compCd " +
                "  ) A " +
                "  WHERE 1 = 1 ");


        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSearchMonth())) {
            sb.append("   		 AND SUBSTR(A.POSTING_DT, 0, 6) = :searchMonth");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipIfFlag())) {
            sb.append("   		 AND A.SLIP_IF_FLAG = :slipIfFlag");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getTransferType())) {
            sb.append("   		 AND A.TRANSFER_TYPE = :transferType");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipType())) {
            sb.append("   		 AND A.SLIP_TYPE = :slipType");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipNo())) {
            sb.append("   		 AND A.SLIP_NO = :slipNo");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getIntegrationVendorNum())) {
            sb.append("   		 AND A.INTEGRATION_VENDOR_NUM = :integrationVendorNum");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getReportArraign())) {
            sb.append("   		 AND A.REPORT_ARRAIGN = :reportArraign");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getApprovalArraign())) {
            sb.append("   		 AND A.APPROVAL_ARRAIGN = :approvalArraign");
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSealArraign())) {
            sb.append("   		 AND A.SEAL_ARRAIGN = :sealArraign");
        }

        sb.append(" ORDER BY A. APPROVAL_GROUP_ID, A.SLIP_HEADER_ID");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSearchMonth())) {
            query.setParameter("searchMonth", erpSlipSubmitDto.getSearchMonth());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipIfFlag())) {
            query.setParameter("slipIfFlag", erpSlipSubmitDto.getSlipIfFlag());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getTransferType())) {
            query.setParameter("transferType", erpSlipSubmitDto.getTransferType());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipType())) {
            query.setParameter("slipType", erpSlipSubmitDto.getSlipType());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSlipNo())) {
            query.setParameter("slipNo", erpSlipSubmitDto.getSlipNo());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getIntegrationVendorNum())) {
            query.setParameter("integrationVendorNum", erpSlipSubmitDto.getIntegrationVendorNum());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getReportArraign())) {
            query.setParameter("reportArraign", erpSlipSubmitDto.getReportArraign());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getApprovalArraign())) {
            query.setParameter("approvalArraign", erpSlipSubmitDto.getApprovalArraign());
        }

        if(!StringUtils.isEmpty(erpSlipSubmitDto.getSealArraign())) {
            query.setParameter("sealArraign", erpSlipSubmitDto.getSealArraign());
        }

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, ErpSlipSubmitDto.class);
    }



}

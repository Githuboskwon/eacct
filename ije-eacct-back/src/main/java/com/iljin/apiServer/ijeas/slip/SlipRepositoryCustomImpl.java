package com.iljin.apiServer.ijeas.slip;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeader;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class SlipRepositoryCustomImpl implements SlipRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;


    @Override
    public BigDecimal getSlipHistoryCount(SlipHistoryDto slipHistoryDto){
        String loginId = util.getLoginId();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(*) FROM " +
                "     ( SELECT " +
                "         HD.COMP_CD AS compCd" +
                "         , HD.SLIP_NO AS slipNo" +
                "         , HD.DEPT_NO AS deptNo" +
                "         , HD.SLIP_TYPE AS slipType" +
                "         , HD.STATUS AS status" +
                "         , HD.POSTING_DT AS postingDt" +
                "         , HD.HEADER_REMARK AS headerRemark" +
                "         , CSSH.INTEGRATION_VENDOR_NUM AS integrationVendorNum" +
                "         , (SELECT vendor.INTEGRATION_VENDOR_NAME " +
                "              FROM TB_MST_CUSTOMER_VENDOR vendor " +
                "             WHERE vendor.COMP_CD = HD.COMP_CD AND vendor.INTEGRATION_VENDOR_NUM = CSSH.INTEGRATION_VENDOR_NUM) AS integrationVendorName" +
                "         , HD.REG_DTM AS regDtm" +
                "         , HD.REG_ID AS regId" +
                "         , HD.EMP_NO AS empNo" +
                "         , AH.NEXT_APP_USER_ID AS nextApprId" +
                "         , HD.USED_CUR AS usedCur" +
                "         , CSSH.TERM_ID AS termId" +
                "         , CASE WHEN (SELECT count(*) FROM APPS.CBO_AP_VIEW_PREPAYS_V WHERE invoice_id = CSSH.std_invoice_trx_id) > 0 THEN 'C' " +
                "                ELSE (SELECT payment_status_flag " +
                "                        FROM APPS.AP_INVOICES_ALL " +
                "                       WHERE org_id = CSSH.org_id " +
                "                         AND invoice_id = CSSH.std_invoice_trx_id" +
                "                         AND global_attribute19 = CSSH.approval_group_id)" +
                "           END AS intStatus " +
                "      FROM TB_SLIP_HD HD " +
                "      LEFT OUTER JOIN APPS.CBO_SP_SLIP_HEADER CSSH " +
                "      ON HD.SLIP_HEADER_ID = CSSH.SLIP_HEADER_ID " +
                "      AND HD.COMP_CD = CSSH.ORG_ID  " +
                "      LEFT OUTER JOIN TB_APPR_HD AH " +
                "      ON HD.APPROVAL_GROUP_ID = AH.APPR_GROUP_ID " +
                "      AND HD.COMP_CD = AH.COMP_CD  " +
                "      WHERE HD.SLIP_TYPE NOT IN ('28', '29', '30', '91') " +
                ") A " +
                "      WHERE 1 = 1 " +
                "      AND A.status NOT IN ( 'SD', 'BD9', '04', '06') ");



        if(!StringUtils.isEmpty(slipHistoryDto.getCompCd())) {
            sb.append("   		 and A.compCd = :compCd");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getPostDtFrom())) {
            sb.append("   		 and A.postingDt >= :postDtFrom");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getPostDtTo())) {
            sb.append("   		 and A.postingDt <= :postDtTo");
        }

//        if(!StringUtils.isEmpty(slipHistoryDto.getWrtDeptCd())) {
//            sb.append("   		 and A.deptNo in (");
//            String [] depts = slipHistoryDto.getWrtDeptCd().split(",");
//            for(int i=0; i<depts.length; i++){
//                if(i!=0){
//                    sb.append(",");
//                }
//                sb.append("'" + depts[i] + "'");
//            }
//            sb.append("   		 ) ");
//        }

        // 부서가 달라져도 내 전표면 볼 수 있게 변경 (부서가 있으면서 작성자가 없을때, 혹은 작성자와 로그인 아이디가 다를때 로직 타도록 수정)
        if (!StringUtils.isEmpty(slipHistoryDto.getWrtDeptCd())
                && (StringUtils.isEmpty(slipHistoryDto.getWrtId())
                || !slipHistoryDto.getWrtId().equals(loginId))) {
            sb.append("   		 and A.deptNo in (");
            String[] depts = slipHistoryDto.getWrtDeptCd().split(",");
            for (int i = 0; i < depts.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append("'" + depts[i] + "'");
            }
            sb.append("   		 ) ");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getEvdCustNm())) {
            sb.append("   		 and A.integrationVendorName like concat(concat('%',:evdCustNm), '%') ");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getSlipTypeCd())) {
            sb.append("   		 and A.slipType = :slipTypeCd");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getWrtId())) {
            sb.append("   		 and A.empNo = :wrtId");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getRegDtFrom())) {
            sb.append("   		 and A.regDtm >= TO_DATE( :regDtFrom , 'YYYYMMDD')");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getRegDtTo())) {
            sb.append("   		 and A.regDtm <= TO_DATE( :regDtTo , 'YYYYMMDD')+0.99999");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getSlipStatCd())) {
            sb.append("   		 and A.status in (");
            String [] stat = slipHistoryDto.getSlipStatCd().split(",");
            for(int i=0; i<stat.length; i++){
                if(i!=0){
                    sb.append(",");
                }
                sb.append("'" + stat[i] + "'");
            }
            sb.append("   		 ) ");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getSlipNo())) {
            sb.append("   		 and A.slipNo like concat(concat('%', :slipNo), '%') ");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getHdSgtxt())) {
            sb.append("   		 and A.headerRemark like concat(concat('%', :hdSgtxt), '%')");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getNextAppUserId())) {
            sb.append("   		 and A.nextApprId = :nextApprId");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getCurrency())) {
            sb.append("   		 and A.usedCur = :currency");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getTermId())) {
            sb.append("   		 and A.termId = :termId");
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getIntStatus())) {
            sb.append("   		 and A.intStatus = :intStatus");
        }

        // 반려전표 조회 기능 추가
        if(Objects.nonNull(slipHistoryDto.getReturnFlag())){
            if(slipHistoryDto.getReturnFlag()) {
                sb.append("   		 AND A.status IN ( 'AR', 'CR', 'FR', 'SC') ");
            }
        }

        sb.append(" ORDER BY A.postingDt, A.regDtm");

        Query query = entityManager.createNativeQuery(sb.toString());

        if(!StringUtils.isEmpty(slipHistoryDto.getCompCd())) {
            query.setParameter("compCd", slipHistoryDto.getCompCd());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getPostDtFrom())) {
            query.setParameter("postDtFrom", slipHistoryDto.getPostDtFrom());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getPostDtTo())) {
            query.setParameter("postDtTo", slipHistoryDto.getPostDtTo());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getEvdCustNm())) {
            query.setParameter("evdCustNm", slipHistoryDto.getEvdCustNm());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getSlipTypeCd())) {
            query.setParameter("slipTypeCd", slipHistoryDto.getSlipTypeCd());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getWrtId())) {
            query.setParameter("wrtId", slipHistoryDto.getWrtId());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getRegDtFrom())) {
            query.setParameter("regDtFrom", slipHistoryDto.getRegDtFrom());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getRegDtTo())) {
            query.setParameter("regDtTo", slipHistoryDto.getRegDtTo());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getSlipNo())) {
            query.setParameter("slipNo", slipHistoryDto.getSlipNo());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getHdSgtxt())) {
            query.setParameter("hdSgtxt", slipHistoryDto.getHdSgtxt());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getNextAppUserId())) {
            query.setParameter("nextApprId", slipHistoryDto.getNextAppUserId());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getCurrency())) {
            query.setParameter("currency", slipHistoryDto.getCurrency());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getTermId())) {
            query.setParameter("termId", slipHistoryDto.getTermId());
        }

        if(!StringUtils.isEmpty(slipHistoryDto.getIntStatus())) {
            query.setParameter("intStatus", slipHistoryDto.getIntStatus());
        }

        BigDecimal count = (new JpaResultMapper().uniqueResult(query, SlipHistoryDto.class)).getCount();

        return count;
    }

    @Override
    public List<SlipHeaderDto> getSlipHeader(String slipNo){
        StringBuilder sb = new StringBuilder();

        sb.append(" 	   SELECT CSSH.SLIP_HEADER_ID																							");
        sb.append(" 	        , CSSH.SLIP_NUMBER																								");
        sb.append(" 	        , CSSH.SLIP_GROUP_NUMBER																						");
        sb.append(" 	        , CSSH.INVOICE_TYPE_LOOKUP_CODE																					");
        sb.append(" 	        , CSSH.BATCH_SOURCE_NAME																						");
        sb.append(" 	        , CSSH.TRX_TYPE_CODE																							");
        sb.append(" 	        , (SELECT TRX_TYPE_NM FROM TB_MST_TRX WHERE COMP_CD = CSSH.ORG_ID AND TRX_TYPE_CD = CSSH.TRX_TYPE_CODE) AS TRX_TYPE_NAME					");
        sb.append(" 	        , CSSH.TTYPE_INPUT_MODULE																						");
        sb.append(" 	        , CSSH.TTYPE_INTERFACE_MODULE																					");
        sb.append(" 	        , CSSH.TTYPE_INTERFACE_SLIP_TYPE																				");
        sb.append(" 	        , CSSH.TTYPE_PREPAYMENT_FLAG																					");
        sb.append(" 	        , CSSH.TTYPE_CLEARING_ACCT_CODE																					");
        sb.append(" 	        , CSSH.TTYPE_ADD_INFO_TYPE																						");
        sb.append(" 	        , CSSH.TTYPE_INTEGRATION_VENDOR_NUM																				");
        sb.append(" 	        , CSSH.TTYPE_PAYMENT_RECEIPT_TERM_ID																			");
        sb.append(" 	        , CSSH.ORG_ID																									");
        sb.append(" 	        , CSSH.LEDGER_ID																								");
        sb.append(" 	        , CSSH.COMP_CODE																								");
        sb.append(" 	        , CSSH.BUDGET_DEPT_CODE																							");
        sb.append(" 	        , (SELECT DEPT_NM FROM TB_MST_CCTR_BLG_ERP WHERE COMP_CD = CSSH.ORG_ID AND DEPT_CD = CSSH.BUDGET_DEPT_CODE) AS BUDGET_DEPT_NAME			");
        sb.append(" 	        , CSSH.PROJECT_CODE																								");
        sb.append(" 	        , CSSH.PROJECT_ID																								");
        sb.append(" 	        , (SELECT CSTV.TASK_ITEM_GROUP																					");
        sb.append(" 	        	 FROM TB_TASK_GROUP CSTV		            																");
        sb.append(" 	        	WHERE CSTV.PROJECT_ID = CSSH.PROJECT_ID																		");
        sb.append(" 	        	  AND CSTV.COMP_CD = CSSH.ORG_ID         	    															");
        sb.append(" 	        	  AND CSTV.TASK_NO = CSSH.TASK_CODE) AS TASK_ITEM_GROUP					 				    				");
        sb.append(" 	        , (SELECT CSTV.ITEM_GROUP_TYPE																					");
        sb.append(" 	        	 FROM TB_TASK_GROUP CSTV																	               	");
        sb.append(" 	        	WHERE CSTV.PROJECT_ID = CSSH.PROJECT_ID																		");
        sb.append(" 	        	  AND CSTV.COMP_CD = CSSH.ORG_ID       																		");
        sb.append(" 	        	  AND CSTV.TASK_NO = CSSH.TASK_CODE) AS ITEM_GROUP_TYPE											    		");
        sb.append(" 	        , (SELECT PROJECT_NM FROM TB_PROJECT_GROUP WHERE COMP_CD = CSSH.ORG_ID AND PROJECT_CD = CSSH.BUDGET_DEPT_CODE) AS PROJECT_NAME			");

        sb.append(" 	        , CSSH.TASK_CODE					 																			");
        sb.append(" 	        , CSSH.TASK_ID					 																				");
        sb.append(" 	        , (SELECT TASK_NM FROM TB_TASK_GROUP WHERE COMP_CD = CSSH.ORG_ID AND PROJECT_ID = CSSH.PROJECT_ID AND TASK_ID = CSSH.TASK_ID) AS TASK_NAME");
        sb.append(" 	        , CSSH.ITEM_GROUP_CODE																							");
        sb.append(" 	        , (SELECT ITEM_GROUP_NM FROM TB_ITEM_GROUP WHERE COMP_CD = CSSH.ORG_ID AND ITEM_GROUP_CD = CSSH.ITEM_GROUP_CODE) AS ITEM_GROUP_NAME		");
        sb.append(" 	        , CSSH.CODE_COMBINATION_ID																						");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_CCID_CODE(CSSH.CODE_COMBINATION_ID,CSSH.LEDGER_ID,'A') AS COA_FULL_CODE			");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_CCID_DESC(CSSH.CODE_COMBINATION_ID,CSSH.LEDGER_ID,'A') AS COA_FULL_DESC			");
        sb.append(" 	        , CSSH.ACCT_CODE AS HEADER_ACCT_CODE																			");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC_ORG(CSSH.ORG_ID, CSSH.ACCT_CODE, '4') AS HEADER_ACCT_NAME             ");
        sb.append(" 	        , CSSH.GL_DATE                              																	");
        sb.append(" 	        , CSSH.SLIP_DATE																								");
        sb.append(" 	        , CSSH.TAX_EVIDENCE_TYPE					 																	");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_GL_CODE_LINE_NAME_CD('CD0100',CSSH.TAX_EVIDENCE_TYPE) AS TAX_EVIDENCE_TYPE_NAME	");
        sb.append(" 	        , (SELECT LINE_ATTRIBUTE1  																						");
        sb.append(" 	          	 FROM	APPS.CBO_GL_TRX_TYPE_EVIDENCE_V																		");
        sb.append(" 	          	WHERE TRX_TYPE_CODE = CSSH.TRX_TYPE_CODE																	");
        sb.append(" 	          	  AND EVIDENCE_CODE = CSSH.TAX_EVIDENCE_TYPE																");
        sb.append(" 	          	  AND ENABLED_FLAG = 'Y') AS LINE_ATTRIBUTE1 																");
        sb.append(" 	        , (SELECT CASE WHEN LINE_ATTRIBUTE1 = 'V' THEN '부가세'  WHEN LINE_ATTRIBUTE1 = 'W' THEN '원천세' ELSE '' END 	");
        sb.append(" 	          	 FROM APPS.CBO_GL_TRX_TYPE_EVIDENCE_V																		");
        sb.append(" 	          	WHERE TRX_TYPE_CODE = CSSH.TRX_TYPE_CODE																	");
        sb.append(" 	          	  AND EVIDENCE_CODE = CSSH.TAX_EVIDENCE_TYPE																");
        sb.append(" 	          	  AND ENABLED_FLAG = 'Y') AS LINE_ATTRIBUTE1_NAME 															");
        sb.append(" 	        , (SELECT NVL(CGCV.LINE_ATTRIBUTE2, '')		 																	");
        sb.append(" 	          	 FROM APPS.CBO_GL_CODE_V CGCV 		 																		");
        sb.append(" 	          	WHERE CGCV.CODE_TYPE = 'CD0100' 		 																	");
        sb.append(" 	          	  AND CGCV.CODE = CSSH.TAX_EVIDENCE_TYPE 		 															");
        sb.append(" 	          	  AND CGCV.HEADER_ENABLED_FLAG = 'Y' 																		");
        sb.append(" 	          	  AND CGCV.LINE_ENABLED_FLAG = 'Y') AS LINE_ATTRIBUTE2  													");
        sb.append(" 	        , (SELECT NVL(CGCV.LINE_ATTRIBUTE3, 'C')		 																");
        sb.append(" 	          	 FROM APPS.CBO_GL_CODE_V CGCV 		 																		");
        sb.append(" 	          	WHERE CGCV.CODE_TYPE = 'CD0100' 		 																	");
        sb.append(" 	          	  AND CGCV.CODE = CSSH.TAX_EVIDENCE_TYPE 		 															");
        sb.append(" 	          	  AND CGCV.HEADER_ENABLED_FLAG = 'Y' 		 																");
        sb.append(" 	          	  AND CGCV.LINE_ENABLED_FLAG = 'Y') AS LINE_ATTRIBUTE3  		 											");
        sb.append(" 	        , CSSH.SLIP_CURRENCY_CODE					 																	");
        sb.append(" 	        , (SELECT PRECISION																								");
        sb.append(" 	          	 FROM APPS.CBO_SP_CURRENCIES_V 		 																		");
        sb.append(" 	          	WHERE CURRENCY_CODE = CSSH.SLIP_CURRENCY_CODE) AS SLIP_CURRENCY_PRECISION  		 							");
        sb.append(" 	        , CSSH.EXCHANGE_RATE_TYPE																						");
        sb.append(" 	        , CSSH.EXCHANGE_DATE                                    														");
        sb.append(" 	        , CSSH.EXCHANGE_RATE																							");
        sb.append(" 	        , CSSH.ENTERED_AMOUNT																							");
        sb.append(" 	        , CSSH.ACCOUNTED_AMOUNT																							");
        sb.append(" 	        , CSSH.REFERENCE_SLIP_MODULE																					");
        sb.append(" 	        , CSSH.REFERENCE_SLIP_NUMBER																					");
        sb.append(" 	        , CSSH.DESCRIPTION																								");
        sb.append(" 	        , CSSH.INTEGRATION_VENDOR_NUM																					");
        sb.append(" 	        , (SELECT INTEGRATION_VENDOR_NAME																				");
        sb.append(" 	             FROM TB_MST_CUSTOMER_VENDOR 																	        	");
        sb.append(" 	            WHERE COMP_CD = CSSH.ORG_ID 																				");
        sb.append(" 	              AND INTEGRATION_VENDOR_NUM = CSSH.INTEGRATION_VENDOR_NUM) AS INTEGRATION_VENDOR_NAME 						");
        sb.append(" 	        , (SELECT VAT_REGISTRATION_NUM																					");
        sb.append(" 	             FROM TB_MST_CUSTOMER_VENDOR         																		");
        sb.append(" 	            WHERE COMP_CD = CSSH.ORG_ID 				 																");
        sb.append(" 	              AND INTEGRATION_VENDOR_NUM = CSSH.INTEGRATION_VENDOR_NUM) AS VAT_REGISTRATION_NUM 						");
        sb.append(" 	        , CSSH.VENDOR_ID																								");
        sb.append(" 	        , CSSH.VENDOR_SITE_ID																							");
        sb.append(" 	        , CSSH.VENDOR_PARTY_ID																							");
        sb.append(" 	        , CSSH.VENDOR_PARTY_SITE_ID																						");
        sb.append(" 	        , CSSH.CUSTOMER_ID																								");
        sb.append(" 	        , CSSH.CUSTOMER_SITE_ID																							");
        sb.append(" 	        , CSSH.CUSTOMER_PARTY_ID																						");
        sb.append(" 	        , CSSH.CUSTOMER_PARTY_SITE_ID																					");
        sb.append(" 	        , (SELECT COUNT(*) 																								");
        sb.append(" 	             FROM APPS.CBO_SP_APPLY_PREPAYS_V 																			");
        sb.append(" 	            WHERE VENDOR_ID = CSSH.VENDOR_ID																			");
        sb.append(" 	              AND ORG_ID = CSSH.ORG_ID 																					");
        sb.append(" 	              AND VENDOR_SITE_ID = CSSH.VENDOR_SITE_ID) AS PREPAY_CNT	 												");
        sb.append(" 	        , CSSH.PAY_GROUP_LOOKUP_CODE																					");
        sb.append(" 	        , CSSH.TERM_NAME																								");
        sb.append(" 	        , CSSH.TERM_ID																									");
        sb.append(" 	        , (SELECT DESCRIPTION																							");
        sb.append(" 	             FROM APPS.CBO_GL_TERMS_V 																					");
        sb.append(" 	            WHERE ORG_ID = CSSH.ORG_ID 																					");
        sb.append(" 	              AND TERM_ID = CSSH.TERM_ID) AS TERM_DESCRIPTION 															");
        sb.append(" 	        , (SELECT VENDOR_ACCT_CHECK																						");
        sb.append(" 	             FROM APPS.CBO_GL_TERMS_V 																					");
        sb.append(" 	            WHERE ORG_ID = CSSH.ORG_ID 																					");
        sb.append(" 	              AND TERM_ID = CSSH.TERM_ID) AS VENDOR_ACCT_CHECK 															");
        sb.append(" 	        , CSSH.TERM_DUE_DATE                                    														");
        sb.append(" 	        , CSSH.PREPAYMENT_APPLY_FLAG																					");
        sb.append(" 	        , CSSH.PAYMENT_RECEIPT_METHOD_CODE																				");
        sb.append(" 	        , CSSH.NOTE_FLAG																								");
        sb.append(" 	        , CSSH.MATURITY_DATE                                    														");
        sb.append(" 	        , CSSH.AWT_GROUP_ID																								");
        sb.append(" 	        , CSSH.AWT_LOCATION_CODE																						");
        sb.append(" 	        , CSSH.TAX_CODE																									");
        sb.append(" 	        , CSSH.VAT_TAX_ID																								");
        sb.append(" 	        , CSSH.TAX_ACCT_CODE																							");
        sb.append(" 	        , NVL((SELECT TAX_RATE_CODE FROM APPS.CBO_SP_TAX_CODE_V 														");
        sb.append(" 	          		WHERE LEDGER_ID = CSSH.LEDGER_ID 																		");
        sb.append(" 	          		  AND TAX_RATE_ID = CSSH.VAT_TAX_ID 																	");
        sb.append(" 	          		  AND TAX_ACCT_CODE = CSSH.TAX_ACCT_CODE 					 											");
        sb.append(" 	          		  AND TAX_EVIDENCE_TYPE = CSSH.TAX_EVIDENCE_TYPE ),'') AS TAX_ACCT_NAME 			     			    ");
        sb.append(" 	        , CSSH.TAX_CODE_COMBINATION_ID																					");
        sb.append(" 	        , NVL((SELECT PERCENTAGE_RATE FROM APPS.CBO_SP_TAX_CODE_V 														");
        sb.append(" 	          		WHERE LEDGER_ID = CSSH.LEDGER_ID 																		");
        sb.append(" 	          		  AND TAX_RATE_ID = CSSH.VAT_TAX_ID 																	");
        sb.append(" 	          		  AND TAX_ACCT_CODE = CSSH.TAX_ACCT_CODE 					 											");
        sb.append(" 	          		  AND TAX_EVIDENCE_TYPE = CSSH.TAX_EVIDENCE_TYPE ),0) AS PERCENTAGE_RATE 					 			");
        sb.append(" 	        , CSSH.EVIDENCE_VENDOR_NUM																						");
        sb.append(" 	        , (SELECT INTEGRATION_VENDOR_NAME																				");
        sb.append(" 	             FROM TB_MST_CUSTOMER_VENDOR 																	        	");
        sb.append(" 	            WHERE COMP_CD = CSSH.ORG_ID 																				");
        sb.append(" 	              AND INTEGRATION_VENDOR_NUM = CSSH.EVIDENCE_VENDOR_NUM) AS EVIDENCE_VENDOR_NAME     						");
        sb.append(" 	        , CSSH.EVIDENCE_VENDOR_CUST_SITE_ID																				");
        sb.append(" 	        , (SELECT VAT_REGISTRATION_NUM																					");
        sb.append(" 	             FROM TB_MST_CUSTOMER_VENDOR 					        													");
        sb.append(" 	            WHERE COMP_CD = CSSH.ORG_ID 																				");
        sb.append(" 	              AND INTEGRATION_VENDOR_NUM = CSSH.EVIDENCE_VENDOR_NUM) AS EVIDENCE_VAT_REGISTRATION_NUM					");
        sb.append(" 	        , CSSH.EVIDENCE_DATE                                    														");
        sb.append(" 	        , CSSH.SUPPLY_AMOUNT																							");
        sb.append(" 	        , CSSH.TAX_AMOUNT																								");
        sb.append("			, (SELECT ATTRIBUTE2			    																				");
        sb.append("			  	 FROM TB_MST_CCTR_BLG_ERP CGCV               																	");
        sb.append("		 	    WHERE 1 = 1 					    																			");
        sb.append("			 	  AND CGCV.COMP_CD = CSSH.ORG_ID 	    																		");
        sb.append("			 	  AND CGCV.DEPT_CD = CSSH.ACTUAL_DEPT_CODE 					        											");
        sb.append("			 	  AND SYSDATE BETWEEN CGCV.START_DATE_ACTIVE AND CGCV.END_DATE_ACTIVE) AS DEPT_TYPE 			    			");
        sb.append(" 	        , CSSH.TR_BANK_ACCT_CODE																						");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(CSSH.LEDGER_ID,CSSH.TR_BANK_ACCT_CODE,'7')  AS TR_BANK_ACCT_NAME		");

        //slipType
        sb.append(" 	        , CSSH.SLIP_TYPE_CODE																							");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(CSSH.LEDGER_ID,CSSH.SLIP_TYPE_CODE,'8')  AS SLIP_TYPE_NAME			");


        sb.append(" 	        , CSSH.SEGMENT9_CODE																							");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(CSSH.LEDGER_ID,CSSH.SEGMENT9_CODE,'9')  AS SEGMENT9_NAME				");
        sb.append(" 	        , CSSH.SEGMENT10_CODE																							");
        sb.append(" 	        , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(CSSH.LEDGER_ID,CSSH.SEGMENT10_CODE,'10')  AS SEGMENT10_NAME			");
        sb.append(" 	        , CSSH.BANK_ACCOUNT_ID																							");
        sb.append(" 	        , CSSH.BANK_ACCOUNT_NAME																						");
        sb.append(" 	        , CSSH.SOURCE_SLIP_HEADER_ID																					");
        sb.append(" 	        , CSSH.PO_NUMBER																								");
        sb.append(" 	        , CSSH.SLIP_STATUS																								");
        sb.append("	  	        , (SELECT DETAIL_NM                                                 		    								");
        sb.append("	  	       	     FROM TB_CODE_DT                                                        									");
        sb.append("	  	       	    WHERE GROUP_CD = 'PROGRESS_STATUS_CD'                                      									");
        sb.append("	  	       	      AND COMP_CD = CSSH.ORG_ID                                                             					");
        sb.append("	  	       	      AND USE_YN = 'Y'                                                             								");
        sb.append("	  	       	      AND DETAIL_CD = CSSH.SLIP_STATUS) AS SLIP_STATUS_NAME                       								");
        sb.append(" 	        , CSSH.DR_CR					 																				");
        sb.append(" 	        , CSSH.SLIP_DISPLAY_FLAG																						");
        sb.append(" 	        , CSSH.SLIP_CREATION_TARGET_FLAG																				");
        sb.append(" 	        , CSSH.VALIDATION_FLAG																							");
        sb.append(" 	        , CSSH.APPROVAL_GROUP_ID																						");
        sb.append(" 	        , CSSH.APPROVAL_COMPLETE_FLAG																					");
        sb.append(" 	        , CSSH.SLIP_IF_FLAG																								");
        sb.append(" 	        , CSSH.STD_INVOICE_TRX_ID																						");
        sb.append(" 	        , CSSH.SLIP_IF_DATE																								");
        sb.append(" 	        , CSSH.SLIP_IF_LAST_APPROVAL_USER																				");
        sb.append(" 	        , CSSH.SLIP_INTERFACE_ERROR_MSG																					");
        sb.append(" 	        , CSSH.DUE_DATE_UPDATE_FLAG					");
        sb.append(" 	        , CSSH.SLIP_DELETE_FLAG						");
        sb.append(" 	        , CSSH.SLIP_DELETE_DATE						");
        sb.append(" 	        , CSSH.ERP_SLIP_CANCEL_DATE					");
        sb.append(" 	        , CSSH.TR_BANK_ACCT_CODE AS TR_ACCT_CODE	");
        sb.append(" 	        , CSSH.TAX_SMARTBILL_NO						");
        sb.append(" 	        , CSSH.ATTRIBUTE_CATEGORY			 		");
        sb.append(" 	        , CSSH.ATTRIBUTE1					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE2					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE3					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE4					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE5					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE6					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE7					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE8					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE9					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE10					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE11					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE12					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE13					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE14					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE15					 		");
        sb.append(" 	        , CSSH.ATTRIBUTE15 AS REPAYMENT_DUE_DATE	");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE_CATEGORY			");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE1					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE2					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE3					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE4					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE5					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE6					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE7					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE8					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE9					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE10					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE11					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE12					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE13					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE14					");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE15					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE1					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE2					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE3					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE4					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE5					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE6					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE7					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE8					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE9					");
        sb.append(" 	        , CSSH.HEADER_ATTRIBUTE10					");
        sb.append(" 	        , CSSH.CREATED_PERSON_ID					");
        sb.append(" 	        , CSSH.CREATED_EMP_NO	 AS USER_ID			");
        sb.append(" 	        , CSSH.CREATION_DATE						");
        sb.append(" 	        , CSSH.LAST_UPDATED_PERSON_ID				");
        sb.append(" 	        , CSSH.LAST_UPDATED_EMP_NO					");
        sb.append(" 	        , CSSH.LAST_UPDATE_DATE						");
        sb.append(" 	        , CSSH.LAST_UPDATE_LOGIN 					");
        sb.append(" 	        , HD.POSTING_DT 				    		");
        sb.append(" 	        , HD.SLIP_NO 								");
        sb.append(" 	        , HD.SCAN_NO 								");
        sb.append(" 	        , CSSH.GLOBAL_ATTRIBUTE8 AS ACCOUNT_YN		");

        sb.append(" 	        , (SELECT SUP_AMOUNT													");
        sb.append(" 	        	 FROM CBOTAX.XXSB_DTI_NTS_MAIN										");
        sb.append(" 	        	WHERE SUPBUY_TYPE = 'AP'											");
        sb.append(" 	        	  AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_SUPPLY_AMOUNT	");
        sb.append(" 	        , (SELECT TAX_AMOUNT													");
        sb.append(" 	        	 FROM CBOTAX.XXSB_DTI_NTS_MAIN										");
        sb.append(" 	        	WHERE SUPBUY_TYPE = 'AP'											");
        sb.append(" 	        	  AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_TAX_AMOUNT		");
        sb.append(" 	        , (SELECT NVL(TOTAL_AMOUNT,0)											");
        sb.append(" 	        	 FROM CBOTAX.XXSB_DTI_NTS_MAIN										");
        sb.append(" 	        	WHERE SUPBUY_TYPE = 'AP'											");
        sb.append(" 	        	  AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_TOTAL_AMOUNT		");

        sb.append(" 	        , HD.REMARK 					                                         ");
        sb.append(" 	        , HD.EVIDENCE_YN AS EVIDENCE_FLAG 					                     ");
        sb.append(" 	        , HD.DOC_TITLE 					                                     ");
        sb.append(" 	        , HD.DOC_URL 					                                         ");
        sb.append(" 	        , HD.TAXBILL_AMT_MODIFY_YN 					                         ");
        sb.append(" 	        , HD.TAXBILL_SU_ID 					                                 ");

        sb.append(" 	        , (SELECT EMP_NM FROM TB_MST_EMP WHERE COMP_CD = CSSH.ORG_ID AND EMP_NO = CSSH.CREATED_EMP_NO ) AS USER_NAME 	 ");
        sb.append(" 	        , CSSH.CREATED_PERSON_ID AS PERSON_ID 					 ");


        //생성자 기준으로 조회
        sb.append(" 	        , CASE WHEN CSSH.TAX_LOCATION_CODE IS NULL THEN (SELECT LOCATION_CODE " +
                "                                                                  FROM APPS.CBO_SP_LOCATION_V " +
                "                                                                 WHERE ORG_ID = CSSH.ORG_ID " +
                  "                                                                AND LOCATION_TYPE = (SELECT ATTRIBUTE1                   " +
                  "                                                                                       FROM APPS.CBO_GL_COA_SEGMENT_V    " +
                  "                                                                                      WHERE SEGMENT_NUM = '3'            " +
                  "                                                                                        AND ORG_ID = CSSH.ORG_ID         " +
                  "                                                                                        AND SYSDATE BETWEEN START_DATE_ACTIVE AND END_DATE_ACTIVE  " +
                  "                                                                                        AND VALUE_CODE = (SELECT DEPT_CD FROM TB_MST_EMP WHERE EMP_NO = CSSH.CREATED_EMP_NO))   " +
                  "                                                                AND LEGAL_ADDRESS_FLAG='N'	)   					 ");
        sb.append(" 	          ELSE CSSH.TAX_LOCATION_CODE END AS TAX_LOCATION_CODE  					 ");
        sb.append(" 	        , CASE WHEN CSSH.TAX_LOCATION_CODE IS NULL THEN (SELECT DESCRIPTION " +
                "                                                                  FROM APPS.CBO_SP_LOCATION_V " +
                "                                                                 WHERE ORG_ID = CSSH.ORG_ID " +
                "                                                                AND LOCATION_TYPE = (SELECT ATTRIBUTE1                   " +
                "                                                                                       FROM APPS.CBO_GL_COA_SEGMENT_V    " +
                "                                                                                      WHERE SEGMENT_NUM = '3'            " +
                "                                                                                        AND ORG_ID = CSSH.ORG_ID         " +
                "                                                                                        AND SYSDATE BETWEEN START_DATE_ACTIVE AND END_DATE_ACTIVE  " +
                "                                                                                        AND VALUE_CODE = (SELECT DEPT_CD FROM TB_MST_EMP WHERE EMP_NO = CSSH.CREATED_EMP_NO))   " +
                "                                                                AND LEGAL_ADDRESS_FLAG='N'	)   					 ");
        sb.append(" 	          ELSE (SELECT DESCRIPTION FROM APPS.CBO_SP_LOCATION_V WHERE ORG_ID = CSSH.ORG_ID AND LOCATION_CODE = CSSH.TAX_LOCATION_CODE AND LEGAL_ADDRESS_FLAG='N'	)  END AS TAX_LOCATION_NAME  					 ");


        sb.append(" 	        , NVL(CSSH.TAX_ISSUE_TYPE_CODE, 					 ");
        sb.append(" 	         	(SELECT CODE			 ");
        sb.append(" 	          	FROM APPS.CBO_GL_CODE_V			 ");
        sb.append(" 	          	WHERE CODE_TYPE = 'CD0090'			 ");
        sb.append(" 	          	AND HEADER_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          	AND LINE_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          	AND LINE_ATTRIBUTE1 = 'Y'			 ");
        sb.append(" 	          	) 		 ");
        sb.append(" 	          ) AS TAX_ISSUE_TYPE_CODE		 ");
        sb.append(" 	        , (SELECT LINE_MEANING			 ");
        sb.append(" 	          FROM APPS.CBO_GL_CODE_V			 ");
        sb.append(" 	          WHERE CODE_TYPE = 'CD0090'			 ");
        sb.append(" 	          AND HEADER_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          AND LINE_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          AND LINE_ATTRIBUTE1 = 'Y'			 ");
        sb.append(" 	          AND CODE = 			 ");
        sb.append(" 	        	NVL(CSSH.TAX_ISSUE_TYPE_CODE, 					 ");
        sb.append(" 	         		(SELECT CODE			 ");
        sb.append(" 	          		FROM APPS.CBO_GL_CODE_V			 ");
        sb.append(" 	          		WHERE CODE_TYPE = 'CD0090'			 ");
        sb.append(" 	          		AND HEADER_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          		AND LINE_ENABLED_FLAG = 'Y'			 ");
        sb.append(" 	          		AND LINE_ATTRIBUTE1 = 'Y'			 ");
        sb.append(" 	          		) 		 ");
        sb.append(" 	          	)		 ");
        sb.append(" 	          ) AS TAX_ISSUE_TYPE_NAME		 ");


        //생성자 기준으로 조회
        sb.append(" 	      , (SELECT INTEGRATION_VENDOR_NUM FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_INTEGRATION_VENDOR_NUM 					 ");
        sb.append(" 	      , (SELECT INTEGRATION_VENDOR_NAME FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_INTEGRATION_VENDOR_NAME 					 ");
        sb.append(" 	      , (SELECT CUSTOMER_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_CUSTOMER_ID 					 ");
        sb.append(" 	      , (SELECT CUSTOMER_SITE_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_CUSTOMER_SITE_ID 					 ");
        sb.append(" 	      , (SELECT VENDOR_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_VENDOR_ID 					 ");
        sb.append(" 	      , (SELECT VENDOR_SITE_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_VENDOR_SITE_ID					 ");
        sb.append(" 	      , (SELECT VEN_PARTY_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_VENDOR_PARTY_ID					 ");
        sb.append(" 	      , (SELECT VEN_PARTY_SITE_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_VENDOR_PARTY_SITE_ID					 ");
        sb.append(" 	      , (SELECT CUSTOMER_PARTY_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_CUSTOMER_PARTY_ID					 ");
        sb.append(" 	      , (SELECT CUST_PARTY_SITE_ID FROM TB_MST_CUSTOMER_VENDOR WHERE COMP_CD = CSSH.ORG_ID AND VAT_REGISTRATION_NUM = CSSH.CREATED_EMP_NO) AS PERSON_CUSTOMER_PARTY_SITE_ID					 ");
        //sb.append(" 	      , '" + email_address + "' AS EMAIL_ADDRESS					 ");


        sb.append(" 	 			,( SELECT SUM(NVL(AMOUNT_TO_APPLY,0))												 ");
        sb.append(" 	 			   FROM CBO.CBO_SP_PREPAYMENT_APPLY												 ");
        sb.append(" 	 			   WHERE ORG_ID = CSSH.ORG_ID												 ");
        sb.append(" 	 			   AND LEDGER_ID = CSSH.LEDGER_ID												 ");
        sb.append(" 	 			   AND SLIP_HEADER_ID = CSSH.SLIP_HEADER_ID										 ");
        sb.append(" 	 			   AND VENDOR_ID = CSSH.VENDOR_ID										 ");
        sb.append(" 	 			   AND VENDOR_SITE_ID = CSSH.VENDOR_SITE_ID										 ");
        sb.append(" 	 			   AND NVL(PREPAY_CANCELLED_FLAG, 'N') = 'N'										 ");
        sb.append(" 	 			 ) AS AMOUNT_TO_APPLY												 ");


        sb.append(" 	 	   , HD.DEPT_NO AS DEPT_CD												 ");
        sb.append(" 	 	   , (SELECT DEPT_NM FROM TB_MST_CCTR_BLG_ERP WHERE COMP_CD = CSSH.ORG_ID AND DEPT_CD = HD.DEPT_NO) AS DEPT_NM												 ");
        sb.append(" 	 	   , HD.SLIP_TYPE_CD   												 ");
        sb.append(" 	 	   , HD.PREPAYMENT_YN  												 ");
        sb.append(" 	 	   , (SELECT COUNT(*) FROM U_FILE WHERE COMP_CD = CSSH.ORG_ID AND DOCUMENT_H_ID = HD.SLIP_NO) AS FILE_CNT												 ");
        sb.append(" 	 	   , (CASE WHEN (DOC_TITLE IS NULL OR DOC_TITLE = '') THEN 0                                                                 ");
        sb.append(" 	 	           WHEN NVL(REGEXP_COUNT(DOC_TITLE , ','), 0) > 0 THEN NVL(REGEXP_COUNT(DOC_TITLE , ','), 0)+1                       ");
        sb.append(" 	 	           ELSE 1 END                                                                                                        ");
        sb.append("               ) AS JINI_CNT											                                                                 ");
        sb.append(" 	 	   , (SELECT COUNT(*) FROM U_JINIFILE WHERE COMP_CD = CSSH.ORG_ID AND DOCUMENT_H_ID = HD.SLIP_NO) AS JINIFILE_CNT	    	 ");



        sb.append(" 	   FROM APPS.CBO_SP_SLIP_HEADER CSSH 					 ");
        sb.append(" 	   	  , TB_SLIP_HD HD 					 ");
        sb.append(" 	  WHERE CSSH.ORG_ID = HD.COMP_CD 					 ");
        sb.append(" 	    AND CSSH.SLIP_HEADER_ID = HD.SLIP_HEADER_ID 					 ");
        sb.append(" 	    AND CSSH.SLIP_NUMBER = HD.SLIP_NO 					 ");


///조회 조건
        //sb.append("         AND CSSH.SLIP_HEADER_ID = :slipHeaderId 					 ");
        sb.append("         AND HD.SLIP_NO = :slipNo              					 ");
        //sb.append("         AND HD.SLIP_TYPE = :slipType          					 ");
        //sb.append("         AND CSSH.CREATED_EMP_NO = :createEmpNo 					 ");*/


        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("slipNo", slipNo);


        return new JpaResultMapper().list(query, SlipHeaderDto.class);
    }


    //전표 라인 조회
    @Override
    public List<SlipDetailDto> getSlipDetail(String slipNo){
        StringBuilder sb = new StringBuilder();

        String compCd = util.getLoginCompCd();

        sb.append(" 	 SELECT 					 ");
        sb.append(" 	              LN.SLIP_HEADER_ID					 ");
        sb.append(" 	              , LN.SLIP_LINE_ID					 ");
        sb.append(" 	              , LN.SLIP_LINE_NUMBER					 ");
        sb.append(" 	              , NVL(TSD.SLIP_SEQ,LN.SLIP_LINE_NUMBER) AS SLIP_SEQ			 ");
        sb.append(" 	              , LN.LINE_TYPE_LOOKUP_CODE					 ");
        sb.append(" 	              , TO_CHAR(LN.ORG_ID)					 ");
        sb.append(" 	              , LN.LEDGER_ID					 ");
        sb.append(" 	              , LN.TTYPE_INTERFACE_MODULE					 ");
        sb.append(" 	              , LN.SOURCE					 ");
        sb.append(" 	              , LN.COMP_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ACCT_CODE,'4')  AS ACCT_NAME					 ");
        sb.append(" 	       		  , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ACTUAL_DEPT_CODE,'3')  AS ACTUAL_DEPT_NAME				 ");

        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.COMP_CODE,'1') AS COMP_NAME 					 ");
        sb.append(" 	              , LN.BUDGET_DEPT_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.BUDGET_DEPT_CODE,'2') AS BUDGET_DEPT_NAME					 ");
        sb.append(" 	              , LN.PROJECT_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.PROJECT_CODE,'6') AS PROJECT_NAME					 ");
        sb.append(" 	              , LN.PROJECT_ID					 ");
        sb.append(" 	              , LN.TASK_CODE					 ");
        sb.append(" 	              , APPS.CBO_SP_COMMON_PKG.GET_TASK_NAME(LN.PROJECT_CODE, LN.TASK_CODE) AS TASK_NAME						 ");
        sb.append(" 	              , LN.TASK_ID					 ");

        sb.append(" 	        	  , (SELECT CSTV.TASK_ITEM_GROUP						    			 ");
        sb.append(" 	        		   FROM APPS.CBO_SP_TASK_ITEMGRP_V CSTV							     ");
        sb.append(" 	        		  WHERE CSTV.PROJECT_ID = LN.PROJECT_ID								 ");
        sb.append(" 	        		    AND CSTV.TASK_NUMBER =  LN.TASK_CODE							 ");
        sb.append(" 	        		) AS TASK_ITEM_GROUP															 								 ");
        sb.append(" 	        	  , (SELECT CSTV.ITEM_GROUP_TYPE							    		 ");
        sb.append(" 	        		   FROM APPS.CBO_SP_TASK_ITEMGRP_V CSTV					    		 ");
        sb.append(" 	        		  WHERE CSTV.PROJECT_ID = LN.PROJECT_ID								 ");
        sb.append(" 	        		    AND CSTV.TASK_NUMBER =  LN.TASK_CODE							 ");
        sb.append(" 	        		) AS ITEM_GROUP_TYPE															 								 ");

        sb.append(" 	              , LN.ITEM_GROUP_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ITEM_GROUP_CODE,'5') AS ITEM_GROUP_NAME					 ");
        sb.append(" 	              , LN.CODE_COMBINATION_ID					 ");
        sb.append(" 	              , LN.ACCT_CODE					 ");
        sb.append(" 	              , LN.INTERFACE_SLIP_TYPE					 ");
        sb.append(" 	              , LN.ACTUAL_DEPT_CODE					 ");
        sb.append("				  ,(SELECT ATTRIBUTE2									");
        sb.append("			 		  FROM APPS.CBO_GL_COA_SEGMENT_V CGCV 									");
        sb.append("		 	 		  WHERE 1 = 1 									");
        sb.append("			 		  AND CGCV.ORG_ID = LN.ORG_ID 									");
        sb.append("			 		  AND CGCV.LEDGER_ID = LN.LEDGER_ID 									");
        sb.append("			 		  AND CGCV.VALUE_CODE = LN.ACTUAL_DEPT_CODE 									");
        sb.append("			 		  AND CGCV.SEGMENT_NUM = '3' 									");
        sb.append("			 		  AND SYSDATE BETWEEN CGCV.START_DATE_ACTIVE AND CGCV.END_DATE_ACTIVE 			");
        sb.append("					) AS ACTUAL_DEPT_TYPE 															");

        sb.append(" 	              , LN.TR_ACCT_CODE					 ");
        sb.append(" 	        	  , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,TR_ACCT_CODE,'7')  AS TR_ACCT_NAME				 ");

        sb.append(" 	              , LN.SLIP_TYPE_CODE					 ");
        sb.append(" 	              , LN.SEGMENT9_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.SEGMENT9_CODE,'9')  AS SEGMENT9_NAME				 ");
        sb.append(" 	              , LN.SEGMENT10_CODE					 ");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.SEGMENT10_CODE,'10')  AS SEGMENT10_NAME				 ");
        sb.append(" 	        	  , NVL((SELECT PERCENTAGE_RATE FROM APPS.CBO_SP_TAX_CODE_V 					 ");
        sb.append(" 	         	   WHERE LEDGER_ID = LN.LEDGER_ID 					 ");
        sb.append(" 	         	   AND TAX_RATE_ID = LN.TAX_ID 					 ");
        sb.append(" 	         	   AND TAX_EVIDENCE_TYPE = (SELECT TAX_EVIDENCE_TYPE FROM CBO.CBO_SP_SLIP_HEADER WHERE SLIP_HEADER_ID = LN.SLIP_HEADER_ID )),0) AS TAX_PERCENTAGE_RATE 					 ");

        sb.append(" 	              , LN.SUPPLY_AMOUNT					 ");
        sb.append(" 	              , LN.ACCOUNTED_SUPPLY_AMOUNT					 ");
        sb.append(" 	              , LN.VAT_AMOUNT					 ");
        sb.append(" 	              , LN.ACCOUNTED_VAT_AMOUNT					 ");
        sb.append(" 	              , (NVL(LN.SUPPLY_AMOUNT,0) + NVL(LN.VAT_AMOUNT,0)) AS USED_AMT					 ");
        sb.append(" 	              , LN.TAX_CODE					 ");
        sb.append(" 	              , LN.TAX_ID					 ");
        sb.append(" 	              , LN.TAX_ACCT_CODE					 ");
        sb.append("                   , NVL((SELECT TAX_RATE_CODE FROM APPS.CBO_SP_TAX_CODE_V 														");
        sb.append(" 	          		      WHERE LEDGER_ID = LN.LEDGER_ID 																		");
        sb.append(" 	          		        AND TAX_RATE_ID = (SELECT VAT_TAX_ID FROM CBO_SP_SLIP_HEADER WHERE SLIP_HEADER_ID = LN.SLIP_HEADER_ID )	");
        sb.append(" 	          		        AND TAX_ACCT_CODE = LN.TAX_ACCT_CODE 					 											");
        sb.append(" 	          		        AND TAX_EVIDENCE_TYPE = (SELECT TAX_EVIDENCE_TYPE FROM CBO.CBO_SP_SLIP_HEADER WHERE SLIP_HEADER_ID = LN.SLIP_HEADER_ID )),'') ");
        sb.append("                     AS TAX_ACCT_NAME 					 			");
        sb.append(" 	              , LN.TAX_CODE_COMBINATION_ID					 ");
        sb.append(" 	              , LN.DESCRIPTION 					         ");
        sb.append(" 	              , LN.CARD_USED_NO					         ");
        sb.append(" 	              , LN.QUANTITY_INVOICED					 ");
        sb.append(" 	              , LN.UNIT_PRICE					         ");
        sb.append(" 	              , LN.ASSETS_TRACKING_FLAG					 ");
        sb.append(" 	              , LN.PO_HEADER_ID					         ");
        sb.append(" 	              , (SELECT PHA.SEGMENT1                     " +
                  "                        FROM APPS.PO_HEADERS_ALL   PHA        " +
                  "                       WHERE  PHA.PO_HEADER_ID = LN.PO_HEADER_ID  " +
                  "                     ) AS PO_HEADER_NUM					     ");
        sb.append(" 	              , LN.PO_LINE_ID					         ");
        sb.append(" 	              , (SELECT PLA.LINE_NUM                     " +
                 "                         FROM APPS.PO_LINES_ALL PLA            " +
                 "                        WHERE  PLA.PO_HEADER_ID = LN.PO_HEADER_ID  " +
                 "                          AND PLA.PO_LINE_ID = LN.PO_LINE_ID       " +
                 "                      ) AS PO_LINE_NUM					     ");
        sb.append(" 	              , LN.PO_LINE_LOCATION_ID					 ");
        sb.append(" 	              , LN.PO_DISTRIBUTION_ID					 ");
        sb.append(" 	              , LN.RCV_TRANSACTION_ID					 ");
        sb.append(" 	              , LN.PO_UNIT_OF_MEASURE					 ");
        sb.append(" 	              , LN.INVENTORY_ITEM_ID					 ");
        sb.append(" 	              , LN.ITEM_CODE					         ");
        sb.append(" 	              , LN.PREPAY_INVOICE_ID					 ");
        sb.append(" 	              , LN.PREPAY_LINE_NUMBER					 ");
        sb.append(" 	              , LN.INVOICE_INCLUDES_PREPAY_FLAG					 ");
        sb.append(" 	              , LN.VALIDATION_FLAG					 ");
        sb.append(" 	              , LN.SLIP_INTERFACE_FLAG					 ");
        sb.append(" 	              , LN.SLIP_INTERFACE_ERROR_MSG					 ");

        //dff
        sb.append(" 	              , (SELECT COUNT(*)  	 					 ");
        sb.append(" 	              	FROM APPS.CBO_SP_DFF_COLUMN_V   	 				 ");
        sb.append(" 	              	WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE 	 			 ");
        sb.append(" 	              	AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')	 				 ");
        sb.append(" 	              	AND CONTEXT_CODE = LN.ACCT_CODE 				 ");
        sb.append(" 	               ) AS DFF_CNT 			 							");
        sb.append(" 	              , (SELECT COUNT(*)  	 					 ");
        sb.append(" 	              	FROM APPS.CBO_SP_DFF_COLUMN_V   	 				 ");
        sb.append(" 	              	WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE 	 			 ");
        sb.append(" 	              	AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')				 ");
        sb.append(" 	              	AND CONTEXT_CODE = LN.ACCT_CODE 				 ");
        sb.append(" 	              	AND REQUIRED_FLAG = 'Y' 				 ");
        sb.append(" 	               ) AS REQUIRED_FLAG_CNT 			 							");
        sb.append(" 	              , NVL(LN.ATTRIBUTE_CATEGORY, CASE WHEN					 ");
        sb.append(" 	                    	(SELECT COUNT(*)  	 															 ");
        sb.append(" 	                         	FROM APPS.CBO_SP_DFF_COLUMN_V   	 												 ");
        sb.append(" 	                         	WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE			 ");
        sb.append(" 	                         	AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')			 ");
        sb.append(" 	                         	AND CONTEXT_CODE = LN.ACCT_CODE											 ");
        sb.append(" 	                        ) > 0			 														");
        sb.append(" 	              	THEN LN.ACCT_CODE ELSE '없음' END  		 ");
        sb.append(" 	              	) AS ATTRIBUTE_CATEGORY 				 ");
        sb.append(" 	              , CASE WHEN 				 ");
        sb.append(" 	                    	(SELECT COUNT(*)  	 															 ");
        sb.append(" 	                         	FROM APPS.CBO_SP_DFF_COLUMN_V   	 												 ");
        sb.append(" 	                         	WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE			 ");
        sb.append(" 	                         	AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')			 ");
        sb.append(" 	                         	AND CONTEXT_CODE = LN.ACCT_CODE											 ");
        sb.append(" 	                        ) > 0			 														");
        sb.append(" 	              	THEN APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ACCT_CODE,'4') ELSE '없음' END  		 ");
        sb.append(" 	              	AS ATTRIBUTE_CATEGORY_NAME 					 ");

        sb.append(" 	              , LN.ATTRIBUTE1					 ");
        sb.append(" 	              , LN.ATTRIBUTE2					 ");
        sb.append(" 	              , LN.ATTRIBUTE3					 ");
        sb.append(" 	              , LN.ATTRIBUTE4					 ");
        sb.append(" 	              , LN.ATTRIBUTE5					 ");
        sb.append(" 	              , LN.ATTRIBUTE6					 ");
        sb.append(" 	              , LN.ATTRIBUTE7					 ");
        sb.append(" 	              , LN.ATTRIBUTE8					 ");
        sb.append(" 	              , LN.ATTRIBUTE9					 ");
        sb.append(" 	              , LN.ATTRIBUTE10					 ");
        sb.append(" 	              , LN.ATTRIBUTE11					 ");
        sb.append(" 	              , LN.ATTRIBUTE12					 ");
        sb.append(" 	              , LN.ATTRIBUTE13					 ");
        sb.append(" 	              , LN.ATTRIBUTE14					 ");
        sb.append(" 	              , LN.ATTRIBUTE15					 ");
        // 출장출장
        sb.append(" 	              , LN.ATTRIBUTE6 AS C_TYPE					 ");
        sb.append(" 	              , LN.ATTRIBUTE7 AS SUB_TRX_TYPE			 ");
        sb.append(" 	              , LN.ATTRIBUTE8 AS SUB_TRX_TYPE_NAME		 ");
        sb.append(" 	              , LN.ATTRIBUTE9 AS O_AMT_TYPE				 ");
        sb.append(" 	              , LN.ATTRIBUTE9 AS I_AMT_TYPE				 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE_CATEGORY			 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE1					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE2					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE3					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE4					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE5					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE6					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE7					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE8					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE9					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE10					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE11					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE12					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE13					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE14					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE15					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE16					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE17					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE18					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE19					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE20					 ");
        // 20180525 사용일자, 가맹점상호 칼럼순서 변경
/*        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE15 AS GLOBAL_ATTRIBUTE15_S					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE17 AS GLOBAL_ATTRIBUTE17_S					 ");
        sb.append(" 	              , LN.GLOBAL_ATTRIBUTE18 AS GLOBAL_ATTRIBUTE18_S					 ");*/

        sb.append(" 	              , LN.LINE_ATTRIBUTE1					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE2					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE3					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE4					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE5					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE6					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE7					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE8					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE9					 ");
        sb.append(" 	              , LN.LINE_ATTRIBUTE10					 ");

        sb.append(" 	              , LN.CREATED_PERSON_ID				 ");
        sb.append(" 	              , LN.CREATED_EMP_NO					 ");
        sb.append(" 	              , TO_CHAR(LN.CREATION_DATE)					 ");
        sb.append(" 	              , LN.LAST_UPDATED_PERSON_ID					 ");
        sb.append(" 	              , LN.LAST_UPDATED_EMP_NO					 ");
        sb.append(" 	              , TO_CHAR(LN.LAST_UPDATE_DATE)					 ");
        sb.append(" 	              , LN.LAST_UPDATE_LOGIN 					 ");
        sb.append(" 	              , LN.DR_CR 					 ");
        sb.append(" 	              , TSD.ORIGINAL_SUPPLY_AMT 					 ");
        sb.append(" 	              , TSD.ORIGINAL_VAT_AMT 					 ");
        sb.append(" 	              , TSD.ORIGINAL_USED_AMT 					 ");
        sb.append(" 	              , TSD.ORIGINAL_TAX_FLAG 					 ");
        sb.append(" 	              , TSD.ORIGINAL_TAX_ACCT_CD 					 ");
        sb.append(" 	              , TSD.TAX_FLAG 					 ");
        sb.append(" 	              , TSD.DRCR_TYPE 					 ");

        sb.append(" 	              , TSD.ATTRIBUTE1 AS ATTRIBUTE1_CODE 					 ");
        sb.append(" 	              , TSD.ATTRIBUTE2 AS ATTRIBUTE2_CODE 					 ");
        sb.append(" 	              , TSD.ATTRIBUTE3 AS ATTRIBUTE3_CODE 					 ");
        sb.append(" 	              , TSD.ATTRIBUTE4 AS ATTRIBUTE4_CODE 					 ");
        sb.append(" 	              , TSD.ATTRIBUTE5 AS ATTRIBUTE5_CODE 					 ");
        sb.append(" 	              , TSD.ATTRIBUTE6 AS ATTRIBUTE6_CODE 					 ");

        //원천세 관련 수정 250307
        sb.append(" 	  , CASE WHEN INSTR((SELECT \"DESCRIPTION\" FROM APPS.CBO_SP_AWT_GROUP_V WHERE AWT_GROUP_ID = LN.AWT_GROUP_ID AND ORG_ID = :compCd), '소득세') > 0 THEN '1' ");
        sb.append("		     WHEN INSTR((SELECT \"DESCRIPTION\" FROM APPS.CBO_SP_AWT_GROUP_V WHERE AWT_GROUP_ID = LN.AWT_GROUP_ID AND ORG_ID = :compCd), '고용보험') > 0 THEN '2' ");
        sb.append("		     WHEN INSTR((SELECT \"DESCRIPTION\" FROM APPS.CBO_SP_AWT_GROUP_V WHERE AWT_GROUP_ID = LN.AWT_GROUP_ID AND ORG_ID = :compCd), '국민연금') > 0 THEN '3' ");
        sb.append("		     WHEN INSTR((SELECT \"DESCRIPTION\" FROM APPS.CBO_SP_AWT_GROUP_V WHERE AWT_GROUP_ID = LN.AWT_GROUP_ID AND ORG_ID = :compCd), '건강보험') > 0 THEN '4' ");
        sb.append("					    END AS WITHHOLDING_TAX_CODE			  		 ");

        sb.append(" 	   FROM CBO.CBO_SP_SLIP_LINE LN					 ");
        sb.append(" 	   LEFT OUTER JOIN TB_SLIP_DT TSD					 ");
        sb.append(" 	   ON LN.ORG_ID = TSD.COMP_CD				 ");
        sb.append(" 	   AND LN.SLIP_HEADER_ID = TSD.SLIP_HEADER_ID				 ");
        sb.append(" 	   AND LN.SLIP_LINE_ID = TSD.SLIP_LINE_ID				 ");
        sb.append(" 	   WHERE 1 = 1	AND LN.SLIP_HEADER_ID = (SELECT CSH.SLIP_HEADER_ID FROM CBO_SP_SLIP_HEADER CSH WHERE CSH.SLIP_NUMBER = :slipNo AND CSH.SLIP_DISPLAY_FLAG = 'Y') ");
        sb.append(" 	   ORDER BY TO_NUMBER(TSD.SLIP_SEQ)				                 ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("slipNo", slipNo);

        return new JpaResultMapper().list(query, SlipDetailDto.class);
    }

    //전표 교통비 라인 조회
    @Override
    public List<SlipDetailDto> getSlipTrafficDetail(String slipNo){
        StringBuilder sb = new StringBuilder();

        String compCd = util.getLoginCompCd();

        sb.append(" 	 SELECT 		LN.SLIP_HEADER_ID					 																");
        sb.append(" 	              , LN.COMP_CD					 																		");
        sb.append(" 	              , LN.SLIP_SEQ				 																			");
        sb.append(" 	              , LN.LEDGER_ID					 																	");
        sb.append(" 	              , LN.TTYPE_INTERFACE_MODULE					 														");
        sb.append(" 	              , LN.COMP_CODE					 																	");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.COMP_CODE,'1') AS COMP_NAME 				");
        sb.append(" 	              , LN.BUDGET_DEPT_CODE					 																");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.BUDGET_DEPT_CODE,'2') AS BUDGET_DEPT_NAME	");
        sb.append(" 	              , LN.PROJECT_CODE					 																	");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.PROJECT_CODE,'6') AS PROJECT_NAME			");
        sb.append(" 	              , LN.PROJECT_ID					 																	");
        sb.append(" 	              , LN.TASK_CODE					 																	");
        sb.append(" 	              , APPS.CBO_SP_COMMON_PKG.GET_TASK_NAME(LN.PROJECT_CODE, LN.TASK_CODE) AS TASK_NAME					");
        sb.append(" 	              , LN.TASK_ID					 																		");
        sb.append(" 	              , LN.ITEM_GROUP_CODE					 																");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ITEM_GROUP_CODE,'5') AS ITEM_GROUP_NAME		");
        sb.append(" 	              , LN.CODE_COMBINATION_ID																				");
        sb.append(" 	              , LN.ACCT_CODE					 																	");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ACCT_CODE,'4')  AS ACCT_NAME				");
        sb.append(" 	              , LN.INTERFACE_SLIP_TYPE					 															");
        sb.append(" 	              , LN.ACTUAL_DEPT_CODE					 																");
        sb.append(" 	       		  , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.ACTUAL_DEPT_CODE,'3')  AS ACTUAL_DEPT_NAME	");
        sb.append("				      , (SELECT ATTRIBUTE2																					");
        sb.append("			 		       FROM APPS.CBO_GL_COA_SEGMENT_V CGCV 																");
        sb.append("		 	 		      WHERE 1 = 1 																						");
        sb.append("			 		        AND CGCV.ORG_ID = LN.COMP_CD 																	");
        sb.append("			 		        AND CGCV.LEDGER_ID = LN.LEDGER_ID 																");
        sb.append("			 		        AND CGCV.VALUE_CODE = LN.ACTUAL_DEPT_CODE 														");
        sb.append("			 		        AND CGCV.SEGMENT_NUM = '3' 																		");
        sb.append("			 		        AND SYSDATE BETWEEN CGCV.START_DATE_ACTIVE AND CGCV.END_DATE_ACTIVE 							");
        sb.append("					    ) AS ACTUAL_DEPT_TYPE 																				");

        sb.append(" 	              , LN.TR_ACCT_CODE					 																	");
        sb.append(" 	        	  , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,TR_ACCT_CODE,'7')  AS TR_ACCT_NAME				");

        sb.append(" 	              , LN.SLIP_TYPE_CODE					 																");
        sb.append(" 	              , LN.SEGMENT9_CODE					 																");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.SEGMENT9_CODE,'9')  AS SEGMENT9_NAME		");
        sb.append(" 	              , LN.SEGMENT10_CODE					 																");
        sb.append(" 	              , APPS.CBO_GL_COMMON_PKG.GET_SEGMENT_DESC(LN.LEDGER_ID,LN.SEGMENT10_CODE,'10')  AS SEGMENT10_NAME		");

        sb.append(" 	              , LN.SUPPLY_AMOUNT					 																");
        sb.append(" 	              , LN.ACCOUNTED_SUPPLY_AMOUNT					 														");
        sb.append(" 	              , LN.VAT_AMOUNT					 																	");
        sb.append(" 	              , LN.ACCOUNTED_VAT_AMOUNT					 															");

        sb.append(" 	              , (NVL(LN.SUPPLY_AMOUNT,0) + NVL(LN.VAT_AMOUNT,0)) AS USED_AMT					 					");
        sb.append(" 	              , LN.ASSETS_TRACKING_FLAG					 															");
        sb.append(" 	              , LN.VALIDATION_FLAG					 																");
        sb.append(" 	              , LN.SLIP_INTERFACE_FLAG					 															");
        sb.append(" 	              , (SELECT COUNT(*)  	 					 															");
        sb.append(" 	              	   FROM APPS.CBO_SP_DFF_COLUMN_V   	 				 												");
        sb.append(" 	              	  WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE 	 			 							");
        sb.append(" 	      				AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')		");
        sb.append(" 	              		AND CONTEXT_CODE = LN.ACCT_CODE 				 												");
        sb.append(" 	                ) AS DFF_CNT 			 																			");
        sb.append(" 	              , (SELECT COUNT(*)  	 					 															");
        sb.append(" 	              	   FROM APPS.CBO_SP_DFF_COLUMN_V   	 				 												");
        sb.append(" 	              	  WHERE APPLICATION_SHORT_NAME = LN.TTYPE_INTERFACE_MODULE 	 										");
        sb.append(" 	              		AND DESCRIPTIVE_FLEXFIELD_NAME = DECODE(LN.TTYPE_INTERFACE_MODULE,'AP','AP_INVOICE_LINES','AR','RA_CUSTOMER_TRX_LINES','#')		");
        sb.append(" 	              		AND CONTEXT_CODE = LN.ACCT_CODE 				 												");
        sb.append(" 	              		AND REQUIRED_FLAG = 'Y' 				 														");
        sb.append(" 	                ) AS REQUIRED_FLAG_CNT 			 																	");
        sb.append(" 	              , LN.DR_CR 					 																		");
        sb.append(" 	              , LN.LINE_TYPE_LOOKUP_CODE 					 														");


        //sb.append(" 	              , STD.COMP_CD                         ");
        sb.append(" 	              , STD.SLIP_NO                              ");
        //sb.append(" 	              , STD.SLIP_HEADER_ID                       ");
        //sb.append(" 	              , STD.SLIP_SEQ                             ");
        sb.append(" 	              , STD.USED_DT                            ");
        sb.append(" 	              , STD.FROM_AREA                            ");
        sb.append(" 	              , STD.FROM_AREA_TEXT                       ");
        sb.append(" 	              , STD.TO_AREA                              ");
        sb.append(" 	              , STD.TO_AREA_TEXT                         ");
        sb.append(" 	              , STD.TRAFFIC_TYPE                         ");
        sb.append(" 	              , STD.DISTANCE                             ");
        sb.append(" 	              , STD.OIL_PRICE                            ");
        sb.append(" 	              , STD.MILEAGE                              ");
        sb.append(" 	              , STD.OIL_AMT                              ");
        sb.append(" 	              , STD.OIL_BUNGAE                           ");
        sb.append(" 	              , STD.ETC_TYPE1                            ");
        sb.append(" 	              , STD.ETC_AMT1                             ");
        sb.append(" 	              , STD.ETC_BUNGAE1                          ");
        sb.append(" 	              , STD.ETC_CHIT_CD1                      ");
        sb.append(" 	              , STD.ETC_USED_NO1                         ");
        sb.append(" 	              , STD.ETC_TYPE2                            ");
        sb.append(" 	              , STD.ETC_AMT2                             ");
        sb.append(" 	              , STD.ETC_BUNGAE2                          ");
        sb.append(" 	              , STD.ETC_CHIT_CD2                      ");
        sb.append(" 	              , STD.ETC_USED_NO2                         ");
        sb.append(" 	              , STD.ETC_TYPE3                            ");
        sb.append(" 	              , STD.ETC_AMT3                             ");
        sb.append(" 	              , STD.ETC_BUNGAE3                          ");
        sb.append(" 	              , STD.ETC_CHIT_CD3                      ");
        sb.append(" 	              , STD.ETC_USED_NO3                         ");
        sb.append(" 	              , STD.SUM_AMT                              ");
        sb.append(" 	              , STD.REMARK                               ");
        sb.append(" 	              , STD.REG_ID                             ");
        sb.append(" 	              , STD.REG_DTM                          ");
        sb.append(" 	              , STD.CHG_ID                              ");
        sb.append(" 	              , STD.CHG_DTM                               ");
        sb.append(" 	              , STD.TEMP1                                ");
        // 20180629 일진전기 박신재사원 - 여비교통비 추가개발
        // 자가운전 보조금 대상자
        sb.append(" 	              , NVL(STD.TEMP2,'2') AS TEMP2       		 ");
        sb.append(" 	              , STD.TEMP3                                ");
        sb.append(" 	              , STD.TEMP4                                ");
        sb.append(" 	              , STD.TEMP5                                ");
        sb.append(" 	              , STD.OIL_AMT_TYPE                         ");
        sb.append(" 	              , STD.CONF_DIST                            ");
        sb.append(" 	              , STD.PAYMENT_DT                         ");
        sb.append(" 	   FROM (										 				");
        sb.append(" 	 				SELECT LN.SLIP_HEADER_ID						");
        sb.append(" 	      						, SLLN.COMP_CD							");
        sb.append(" 	      						, SLLN.SLIP_NO						");
        sb.append(" 	      						, SLLN.SLIP_SEQ						");
        sb.append(" 	     						, LN.LEDGER_ID						");
        sb.append(" 	      						, LN.LINE_TYPE_LOOKUP_CODE			");
        sb.append(" 	      						, LN.TTYPE_INTERFACE_MODULE			");
        sb.append(" 	      						, LN.COMP_CODE						");
        sb.append(" 	      						, LN.BUDGET_DEPT_CODE				");
        sb.append(" 	      						, LN.PROJECT_CODE					");
        sb.append(" 	      						, LN.PROJECT_ID						");
        sb.append(" 	      						, LN.TASK_CODE						");
        sb.append(" 	      						, LN.TASK_ID						");
        sb.append(" 	      						, LN.ITEM_GROUP_CODE				");
        sb.append(" 	      						, LN.CODE_COMBINATION_ID			");
        sb.append(" 	      						, LN.ACCT_CODE						");
        sb.append(" 	      						, LN.INTERFACE_SLIP_TYPE			");
        sb.append(" 	      						, LN.ACTUAL_DEPT_CODE				");
        sb.append(" 	      						, LN.TR_ACCT_CODE					");
        sb.append(" 	      						, LN.SLIP_TYPE_CODE					");
        sb.append(" 	      						, LN.SEGMENT9_CODE					");
        sb.append(" 	      						, LN.SEGMENT10_CODE					");
        sb.append(" 	      						, SUM(LN.VAT_AMOUNT) AS VAT_AMOUNT								");
        sb.append(" 	      						, SUM(LN.SUPPLY_AMOUNT) AS SUPPLY_AMOUNT						");
        sb.append(" 	      						, SUM(LN.ACCOUNTED_SUPPLY_AMOUNT) AS ACCOUNTED_SUPPLY_AMOUNT	");
        sb.append(" 	      						, SUM(LN.ACCOUNTED_VAT_AMOUNT) AS ACCOUNTED_VAT_AMOUNT			");
        sb.append(" 	      						, LN.ASSETS_TRACKING_FLAG			");
        sb.append(" 	      						, LN.VALIDATION_FLAG				");
        sb.append(" 	      						, LN.SLIP_INTERFACE_FLAG			");
        sb.append(" 	      						, LN.CREATED_PERSON_ID				");
        sb.append(" 	      						, LN.CREATED_EMP_NO					");
        sb.append(" 	      						, LN.DR_CR 							");
        sb.append(" 	   				  FROM CBO.CBO_SP_SLIP_LINE LN					");
        sb.append(" 	   				  LEFT OUTER JOIN TB_SLIP_DT SLLN 					 	");
        sb.append(" 	   					ON LN.ORG_ID = SLLN.COMP_CD 				");
        sb.append(" 	   				   AND LN.SLIP_HEADER_ID = SLLN.SLIP_HEADER_ID 	");
        sb.append(" 	   				   AND LN.SLIP_LINE_ID = SLLN.SLIP_LINE_ID 		");
        sb.append(" 	  				 WHERE 1 = 1							 			");
        sb.append("                        AND LN.ORG_ID = :compCd							");
        sb.append("                        AND SLLN.SLIP_NO = :slipNo					");
        sb.append(" 	  				 GROUP BY LN.SLIP_HEADER_ID			");
        sb.append(" 	      				 , SLLN.COMP_CD						");
        sb.append(" 	      				 , LN.LEDGER_ID					");
        sb.append(" 	      				 , LN.LINE_TYPE_LOOKUP_CODE		");
        sb.append(" 	      				 , LN.TTYPE_INTERFACE_MODULE		");
        sb.append(" 	      				 , LN.COMP_CODE					");
        sb.append(" 	      				 , LN.BUDGET_DEPT_CODE			");
        sb.append(" 	      				 , LN.PROJECT_CODE				");
        sb.append(" 	      				 , LN.PROJECT_ID					");
        sb.append(" 	      				 , LN.TASK_CODE					");
        sb.append(" 	      				 , LN.TASK_ID					");
        sb.append(" 	      				 , LN.ITEM_GROUP_CODE			");
        sb.append(" 	      				 , LN.CODE_COMBINATION_ID		");
        sb.append(" 	      				 , LN.ACCT_CODE					");
        sb.append(" 	      				 , LN.INTERFACE_SLIP_TYPE		");
        sb.append(" 	      				 , LN.ACTUAL_DEPT_CODE			");
        sb.append(" 	      				 , LN.TR_ACCT_CODE				");
        sb.append(" 	      				 , LN.SLIP_TYPE_CODE				");
        sb.append(" 	      				 , LN.SEGMENT9_CODE				");
        sb.append(" 	      				 , LN.SEGMENT10_CODE				");
        sb.append(" 	      				 , LN.ASSETS_TRACKING_FLAG		");
        sb.append(" 	      				 , LN.VALIDATION_FLAG			");
        sb.append(" 	      				 , LN.SLIP_INTERFACE_FLAG		");
        sb.append(" 	      				 , LN.CREATED_PERSON_ID			");
        sb.append(" 	      				 , LN.CREATED_EMP_NO				");
        sb.append(" 	      				 , LN.DR_CR 						");
        sb.append(" 	      				 , SLLN.SLIP_SEQ 				");
        sb.append(" 	      				 , SLLN.SLIP_NO 				");
        sb.append(" 	   	 ) LN, TB_SLIP_TRAFFIC_DT STD							");
        sb.append(" 	 WHERE LN.COMP_CD = STD.COMP_CD						");
        sb.append(" 	   AND LN.SLIP_HEADER_ID = STD.SLIP_HEADER_ID				");
        sb.append(" 	   AND LN.SLIP_SEQ = STD.SLIP_SEQ					 		");
        sb.append("        AND LN.COMP_CD = :compCd							            ");
        sb.append("        AND LN.SLIP_NO = :slipNo                                ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);
        query.setParameter("slipNo", slipNo);

        return new JpaResultMapper().list(query, SlipDetailDto.class);
    }

    //항공권 전표 조회
    @Override
    public List<SlipHeaderDto> getAirlineSlip(SlipHeaderDto slipHeaderDto) {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT A.*                                                                                           " +
                "      , (SELECT INTEGRATION_VENDOR_NAME                                                                " +
                "           FROM TB_MST_CUSTOMER_VENDOR                                                                 " +
                "          WHERE COMP_CD = A.COMPANY_CODE                                                               " +
                "            AND INTEGRATION_VENDOR_NUM = A.INTEGRATION_VENDOR_NUM                                      " +
                "        ) AS ERP_VENDOR_NAME                                                                           " +
                "   FROM (                                                                                              " +
                "          SELECT TSH.COMP_CD AS COMPANY_CODE                                                           " +
                "               , TSH.SLIP_HEADER_ID                                                                    " +
                "               , TSH.APPROVAL_GROUP_ID                                                                 " +
                "               , CASE WHEN TSH.SLIP_TYPE = 'GROUP' THEN 'Y' ELSE '' END AS SLIP_GROUP_YN               " +
                "               , TSH.STATUS                                                                            " +
                "               , NVL(HD.SLIP_STATUS, TSH.STATUS) AS SLIP_STATUS                                        " +
                "               , (SELECT DETAIL_NM                                                                     " +
                "                    FROM TB_CODE_DT                                                                    " +
                "                   WHERE GROUP_CD = 'PROGRESS_STATUS_CD'                                               " +
                "                     AND COMP_CD = TSH.COMP_CD                                                         " +
                "                     AND USE_YN = 'Y'                                                                  " +
                "                     AND DETAIL_CD = NVL(HD.SLIP_STATUS, TSH.STATUS)                                   " +
                "                 ) AS SLIP_STATUS_NAME                                                                 " +
                "               , TSH.SLIP_FORM                                                                         " +

                //전자전표유형 조회
                "               , (SELECT LINE_MEANING                                                                  " +
                "                    FROM APPS.CBO_GL_CODE_V                                                            " +
                "                   WHERE CODE_TYPE = 'CD0060'                                                          " +
                "                     AND HEADER_ENABLED_FLAG = 'Y'                                                     " +
                "                     AND LINE_ENABLED_FLAG = 'Y'                                                       " +
                "                     AND CODE = TSH.SLIP_FORM                                                          " +
                "                 ) AS SLIP_FORM_TEXT                                                                   " +

                "               , TSH.SLIP_TYPE                                                                         " +
                "               , (SELECT TRX_TYPE_NM FROM TB_MST_TRX WHERE COMP_CD = TSH.COMP_CD AND TRX_TYPE_CD = TSH.SLIP_TYPE) AS SLIP_TYPE_TEXT " +
                "               , TSH.EMP_NO AS USER_ID                                                                 " +
                "               , (SELECT EMP_NM FROM TB_MST_EMP WHERE COMP_CD = TSH.COMP_CD AND EMP_NO = TSH.EMP_NO) AS USER_NAME                " +
                "               , TSH.DEPT_NO AS DEPT_CODE                                                              " +
                "               , (SELECT DEPT_NM FROM TB_MST_CCTR_BLG_ERP WHERE COMP_CD = TSH.COMP_CD AND DEPT_CD = TSH.DEPT_NO)  AS DEPT_NAME       " +
                "               , TSH.POSTING_DT AS POSTING_DATE                                                        " +
                "               , TSH.SLIP_NO                                                                           " +
                "               , TSH.SLIP_GROUP_NO                                                                     " +
                "               , TSH.USED_CUR                                                                          " +
                "               , TO_CHAR(TSH.REG_DTM,'YYYYMMDD') AS ADD_DATE                                           " +
                "               , TSH.HEADER_REMARK AS HEADER_TEXT                                                      " +
                "               , TSH.LEDGER_ID                                                                         " +
                "               , (CASE WHEN NVL(TSH.USED_CUR, 'KRW') = 'KRW' THEN TSH.USED_AMT ELSE TSH.USED_FOR_AMT END)  AS USED_AMT " +
                "               , HD.TTYPE_INPUT_MODULE                                                                 " +
                "               , HD.INTEGRATION_VENDOR_NUM                                                             " +
                "            FROM TB_SLIP_HD TSH                                                                        " +
                "            LEFT OUTER JOIN APPS.CBO_SP_SLIP_HEADER HD                                                 " +
                "                 ON TSH.COMP_CD = HD.ORG_ID                                                            " +
                "                 AND TSH.SLIP_HEADER_ID = HD.SLIP_HEADER_ID                                            " +
                "           WHERE 1 = 1                                                                                 " +
                "             AND NVL(HD.SLIP_STATUS, TSH.STATUS)  IN  ( 'AP', 'CP', 'CC' )                             " +
                "             AND NVL(TSH.SLIP_FORM, 'X') = '10'                                                        " +
                "             AND NVL(HD.SLIP_DISPLAY_FLAG,'1') = (CASE WHEN TSH.SLIP_TYPE = 'GROUP' THEN '1'  ELSE 'Y' END) " +
                "             AND TSH.SLIP_HEADER_ID = TSH.APPROVAL_GROUP_ID                                            " +
                "             AND TSH.SLIP_TYPE IN ('SPAP005')                                                          ");

        if(hasText(slipHeaderDto.compCd)){
            sb.append("             AND TSH.COMP_CD = :compCd                                                           ");
        }
        if(hasText(slipHeaderDto.empNo)) {
            sb.append("             AND UPPER(TSH.EMP_NO) = :empNo                                                      ");
        }
        if(hasText(slipHeaderDto.fromDate) && hasText(slipHeaderDto.toDate)) {
            sb.append("             AND TSH.POSTING_DT BETWEEN :fromDate AND :toDate                                    ");
        }

        sb.append("           UNION ALL                                                                                 " +
                "          SELECT TO_CHAR(HD.ORG_ID) AS COMPANY_CODE                                                    " +
                "               , HD.SLIP_HEADER_ID                                                                     " +
                "               , HD.APPROVAL_GROUP_ID                                                                  " +
                "               , CASE WHEN TSH.SLIP_TYPE = 'GROUP' THEN 'Y'                                            " +
                "                      ELSE ''                                                                          " +
                "                 END AS SLIP_GROUP_YN                                                                  " +
                "               , TSH.STATUS                                                                            " +
                "               , NVL(HD.SLIP_STATUS, TSH.STATUS) AS SLIP_STATUS                                        " +
                "               , (SELECT DETAIL_NM                                                                     " +
                "                    FROM TB_CODE_DT                                                                    " +
                "                   WHERE GROUP_CD = 'PROGRESS_STATUS_CD'                                               " +
                "                     AND COMP_CD = HD.ORG_ID                                                           " +
                "                     AND USE_YN = 'Y'                                                                  " +
                "                     AND DETAIL_CD = NVL(HD.SLIP_STATUS, TSH.STATUS)                                   " +
                "                 ) AS SLIP_STATUS_NAME                                                                 " +
                "               , TSH.SLIP_FORM                                                                         " +

                //전자전표유형 조회
                "               , (SELECT LINE_MEANING                                                                  " +
                "                    FROM APPS.CBO_GL_CODE_V                                                            " +
                "                   WHERE CODE_TYPE = 'CD0060'                                                          " +
                "                     AND HEADER_ENABLED_FLAG = 'Y'                                                     " +
                "                     AND LINE_ENABLED_FLAG = 'Y'                                                       " +
                "                     AND CODE = TSH.SLIP_FORM                                                          " +
                "                 ) AS SLIP_FORM_TEXT                                                                   " +
                "               , HD.TRX_TYPE_CODE AS SLIP_TYPE                                                         " +
                "               , (SELECT TRX_TYPE_NM FROM TB_MST_TRX WHERE COMP_CD = HD.ORG_ID AND TRX_TYPE_CD = HD.TRX_TYPE_CODE) AS SLIP_TYPE_TEXT " +
                "               , NVL(HD.CREATED_EMP_NO, TSH.EMP_NO) AS USER_ID                                         " +
                "               , (SELECT EMP_NM FROM TB_MST_EMP WHERE COMP_CD = HD.ORG_ID AND EMP_NO = NVL(HD.CREATED_EMP_NO, TSH.EMP_NO)) AS USER_NAME " +
                "               , HD.ACTUAL_DEPT_CODE AS DEPT_CODE                                                      " +
                "               , (SELECT DEPT_NM FROM TB_MST_CCTR_BLG_ERP WHERE COMP_CD = HD.ORG_ID AND DEPT_CD = HD.ACTUAL_DEPT_CODE) AS DEPT_NAME " +
                "               , TO_CHAR(HD.GL_DATE, 'YYYYMMDD') AS POSTING_DATE                                       " +
                "               , HD.SLIP_NUMBER AS SLIP_NO                                                             " +
                "               , HD.SLIP_GROUP_NUMBER                                                                  " +
                "               , HD.SLIP_CURRENCY_CODE AS USED_CUR                                                     " +
                "               , TO_CHAR(CREATION_DATE,'YYYYMMDD') AS ADD_DATE                                         " +
                "               , HD.DESCRIPTION AS HEADER_TEXT                                                         " +
                "               , HD.LEDGER_ID                                                                          " +
                "               , TO_CHAR(HD.ENTERED_AMOUNT) AS USED_AMT                                                " +
                "               , HD.TTYPE_INPUT_MODULE                                                                 " +
                "               , HD.INTEGRATION_VENDOR_NUM                                                             " +
                "           FROM TB_SLIP_HD TSH                                                                         " +
                "          RIGHT OUTER JOIN APPS.CBO_SP_SLIP_HEADER HD                                                  " +
                "                ON TSH.COMP_CD = HD.ORG_ID                                                             " +
                "                AND TSH.SLIP_HEADER_ID = HD.SLIP_HEADER_ID                                             " +
                "          WHERE 1 = 1                                                                                  " +
                "            AND NVL(HD.SLIP_STATUS, TSH.STATUS) IN  ( 'AP', 'CP', 'CC' )                               " +
                "            AND NVL(TSH.SLIP_FORM, 'X') <> '10'                                                        " +
                "            AND NVL(HD.SLIP_DISPLAY_FLAG,'1') = (CASE WHEN TSH.SLIP_TYPE = 'GROUP' THEN '1'  ELSE 'Y' END) " +
                "            AND HD.TRX_TYPE_CODE IN ('SPAP005')                                                        ");


        if(hasText(slipHeaderDto.compCd)) {
            sb.append("            AND HD.ORG_ID = :compCd                                                              ");
        }
        if(hasText(slipHeaderDto.empNo)) {
            sb.append("            AND UPPER(NVL(HD.CREATED_EMP_NO, TSH.EMP_NO)) = UPPER(:empNo)                        ");
        }
        if(hasText(slipHeaderDto.fromDate) && hasText(slipHeaderDto.toDate)) {
            sb.append("            AND TO_CHAR(HD.GL_DATE, 'YYYYMMDD') BETWEEN :fromDate AND :toDate                    ");
        }


        sb.append("       ) A                                                                                           " +
                "  WHERE 1 = 1                                                                                          ");


        if(slipHeaderDto.slipHeaderId != null) {
            sb.append("    AND A.SLIP_HEADER_ID <> :slipHeaderId                                                            ");
        }


        sb.append("  ORDER BY A.APPROVAL_GROUP_ID DESC, A.ADD_DATE DESC, A.POSTING_DATE DESC, A.SLIP_NO DESC");


        Query query = entityManager.createNativeQuery(sb.toString());

        if(hasText(slipHeaderDto.compCd)) {
            query.setParameter("compCd", slipHeaderDto.compCd);
        }
        if(hasText(slipHeaderDto.fromDate) && hasText(slipHeaderDto.toDate)) {
            query.setParameter("fromDate", slipHeaderDto.fromDate);
            query.setParameter("toDate", slipHeaderDto.toDate);
        }
        if(hasText(slipHeaderDto.empNo)) {
            query.setParameter("empNo", slipHeaderDto.empNo);
        }
        if(slipHeaderDto.slipHeaderId != null) {
            query.setParameter("slipHeaderId", slipHeaderDto.slipHeaderId);
        }

        return new JpaResultMapper().list(query, SlipHeaderDto.class);
    }

    @Override
    public List<SlipHeaderDto> getSlipInfo(String compCd, String slipNo, BigDecimal slipHeaderId) {
        StringBuilder sb = new StringBuilder();
        sb.append("   SELECT " +
            "     (SELECT SUP_AMOUNT " +
            "         FROM CBOTAX.XXSB_DTI_NTS_MAIN " +
            "        WHERE SUPBUY_TYPE = 'AP' " +
            "          AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_SUPPLY_AMOUNT " +
            "    , (SELECT TAX_AMOUNT " +
            "         FROM CBOTAX.XXSB_DTI_NTS_MAIN " +
            "        WHERE SUPBUY_TYPE = 'AP' " +
            "          AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_TAX_AMOUNT " +
            "    , (SELECT NVL(TOTAL_AMOUNT,0) " +
            "         FROM CBOTAX.XXSB_DTI_NTS_MAIN " +
            "        WHERE SUPBUY_TYPE = 'AP' " +
            "          AND ISSUE_ID = CSSH.TAX_SMARTBILL_NO) AS TAXBILL_TOTAL_AMOUNT " +
            "    , CASE WHEN CSSH.TAX_LOCATION_CODE IS NULL" +
            "     THEN (SELECT LOCATION_CODE " +
            "               FROM APPS.CBO_SP_LOCATION_V " +
            "           WHERE ORG_ID = CSSH.ORG_ID " +
            "           AND LOCATION_TYPE = (SELECT ATTRIBUTE1 FROM APPS.CBO_GL_COA_SEGMENT_V WHERE SEGMENT_NUM = '3' AND ORG_ID = CSSH.ORG_ID AND SYSDATE BETWEEN START_DATE_ACTIVE AND END_DATE_ACTIVE AND VALUE_CODE = (SELECT DEPT_CD FROM TB_MST_EMP WHERE EMP_NO = CSSH.CREATED_EMP_NO)) " +
            "           AND LEGAL_ADDRESS_FLAG='N' ) " +
            "     ELSE CSSH.TAX_LOCATION_CODE " +
            "     END AS TAX_LOCATION_CODE " +
            "     FROM CBO_SP_SLIP_HEADER CSSH  " +
            "        , TB_SLIP_HD HD " +
            "    WHERE CSSH.ORG_ID = HD.COMP_CD " +
            "      AND CSSH.SLIP_HEADER_ID = HD.SLIP_HEADER_ID " +
            "      AND CSSH.SLIP_NUMBER = HD.SLIP_NO " +
            "      AND CSSH.SLIP_HEADER_ID = :slipHeaderId " +
            "      AND HD.SLIP_NO = :slipNo" +
            "      AND HD.COMP_CD = :compCd");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("slipHeaderId", slipHeaderId);
        query.setParameter("slipNo", slipNo);
        query.setParameter("compCd", compCd);

        return new JpaResultMapper().list(query, SlipHeaderDto.class);
    }

    @Override
    public List<SlipHistoryDto> getCoaHierarchyCnt(BigDecimal slipHeaderId, Integer orgId, BigDecimal ledgerId, String postingDt) {
        StringBuilder sb = new StringBuilder();
        sb.append("     SELECT " +
            " COUNT(*) AS CNT " +
            " FROM APPS.CBO_GL_COA_HIERARCHY_V CGCH, APPS.GL_LEDGERS GL " +
            " WHERE CGCH.FLEX_VALUE IN (SELECT ACCT_CODE FROM APPS.CBO_SP_SLIP_LINE WHERE SLIP_HEADER_ID = :slipHeaderId AND ORG_ID = :orgId) " +
            " AND GL.LEDGER_ID = :ledgerId AND CGCH.COA_ID = GL.CHART_OF_ACCOUNTS_ID AND CGCH.SEGMENT_NUM = 4 AND CGCH.HIERARCHY_CODE LIKE '%통제%' " +
            " AND TO_DATE(:postingDt,'YYYYMMDD') BETWEEN CGCH.START_DATE_ACTIVE AND CGCH.END_DATE_ACTIVE ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("slipHeaderId", slipHeaderId);
        query.setParameter("orgId", orgId);
        query.setParameter("ledgerId", ledgerId);
        query.setParameter("postingDt", postingDt);
        return new JpaResultMapper().list(query, SlipHistoryDto.class);
    }

    @Override
    public void callBudgetCheckProcedure(BigDecimal slipHeaderId) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_GL_BUDGET_PKG.CHECK_FUNDS_AVAILABLE");
        storedProcedureQuery.setParameter("p_source_id", slipHeaderId);
        storedProcedureQuery.setParameter("p_source_type", "SLIP");
        storedProcedureQuery.execute();

        String availableFlag = (String) storedProcedureQuery.getOutputParameterValue("x_available_flag");
        String summaryAccount = (String) storedProcedureQuery.getOutputParameterValue("x_summary_account");
        Object fundsAvailableObj = storedProcedureQuery.getOutputParameterValue("x_funds_available");
        String fundsAvailable = "";
        if(nonNull(fundsAvailableObj)) {
            fundsAvailable = fundsAvailableObj.toString();
        }
        String errFlag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String errStep = (String) storedProcedureQuery.getOutputParameterValue("x_err_step");
        String errMsg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");

        if(!"S".equals(errFlag)) {
            throw new SlipException("Step : " + errStep + " Msg : " + errMsg);
        }

        if("UNAVAILABLE".equals(availableFlag)) {
            String budgetMsg = "예산이 초과되었습니다. \n 초과계정 : [" + summaryAccount + "] 초과금액 : [" + fundsAvailable + "] ";
            throw new SlipException(budgetMsg);
        }
    }

    @Override
    public void callBudgetResetProcedure(ErpSlipHeader erpSlipHeader) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_GL_BUDGET_PKG.INSERT_ENCUMBRANCE");
        storedProcedureQuery.setParameter("p_org_id", erpSlipHeader.getOrgId());
        storedProcedureQuery.setParameter("p_ledger_id", erpSlipHeader.getLedgerId());
        // 전부 값 확인 필요
        storedProcedureQuery.setParameter("p_source_module", erpSlipHeader.getTtypeInterfaceModule());
        storedProcedureQuery.setParameter("p_source_table", "CBO_SP_SLIP_HEADER");
        storedProcedureQuery.setParameter("p_currency_code", erpSlipHeader.getSlipCurrencyCode());
        storedProcedureQuery.setParameter("p_source_slip_id", erpSlipHeader.getSlipNumber());
        storedProcedureQuery.setParameter("p_source_line_num", null);
        storedProcedureQuery.setParameter("p_gl_date", erpSlipHeader.getGlDate());
        storedProcedureQuery.setParameter("p_ccid", erpSlipHeader.getCodeCombinationId());
        storedProcedureQuery.setParameter("p_amount", erpSlipHeader.getSupplyAmount());
        storedProcedureQuery.setParameter("p_cancelled_flag", "Y");
        storedProcedureQuery.execute();

        String errFlag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String errMsg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");




    }

    @Override
    public List<SlipDto> getTaxLocationCode(String deptCd){
        StringBuilder sb = new StringBuilder();

        Query query = null;

        String compCd = util.getLoginCompCd();

        if(deptCd.equals("all")){
            sb.append(
                    "    SELECT LOCATION_CODE" +
                    "         , DESCRIPTION     " +
                    "      FROM APPS.CBO_SP_LOCATION_V " +
                    "     WHERE ORG_ID = :compCd " +
                    "       AND LEGAL_ADDRESS_FLAG='N' ");

            query = entityManager.createNativeQuery(sb.toString());

            query.setParameter("compCd", compCd);
        }else{
            sb.append("" +
                    "    SELECT LOCATION_CODE " +
                    "         , DESCRIPTION " +
                    "      FROM APPS.CBO_SP_LOCATION_V " +
                    "     WHERE ORG_ID = :compCd" +
                    "       AND LOCATION_TYPE = (SELECT ATTRIBUTE1 " +
                    "                              FROM APPS.CBO_GL_COA_SEGMENT_V " +
                    "                             WHERE SEGMENT_NUM = '3' " +
                    "                               AND ORG_ID = :compCd  " +
                    "                               AND SYSDATE BETWEEN START_DATE_ACTIVE AND END_DATE_ACTIVE " +
                    "                               AND VALUE_CODE = :deptCd )" +
                    "       AND LEGAL_ADDRESS_FLAG='N' ");

            query = entityManager.createNativeQuery(sb.toString());

            query.setParameter("compCd", compCd);
            query.setParameter("deptCd", deptCd);
        }

        return new JpaResultMapper().list(query, SlipDto.class);
    }

}

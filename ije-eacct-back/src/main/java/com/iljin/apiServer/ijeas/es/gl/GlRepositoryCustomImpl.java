package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
@Repository
public class GlRepositoryCustomImpl implements GlRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;


    @Override
    public List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto) {

        String compCd = util.getLoginCompCd();
        String search_code = erpSlipRequestDto.getSearchCd();
        String search_name = erpSlipRequestDto.getSearchNm();
        String ttype_interface_module = erpSlipRequestDto.getTrxTypeCd();
        String integration_vendor_num = erpSlipRequestDto.getVendorCd();
        String dept_code = erpSlipRequestDto.getDeptCd();
        String currency_type = erpSlipRequestDto.getCurrencyType();

//        Y 로 고정된 값 변수로 받지않고 직접 Y로 넣도록 변경
//        String sp_enabled_flag = "Y";
//        String default_term_flag = "Y";
//        String ea_enabled_flag = "Y";

        StringBuilder sb = new StringBuilder();

        sb.append("               SELECT T.TERM_NAME                                                                     \n");
        sb.append("                    , T.DESCRIPTION                                                                 \n");
        sb.append("                    , T.TERM_ID                                                                     \n");
        sb.append("                    , T.NOTES_FLAG                                                                  \n");
        sb.append("                    , T.PAYMENT_METHOD                                                              \n");
        sb.append("                    , T.VENDOR_ACCT_CHECK                                                           \n");
        sb.append("                 FROM (                                                							  \n");
        sb.append("                 	     SELECT CGTV.NAME AS TERM_NAME                                                \n");
        sb.append("                            , CGTV.DESCRIPTION                                                      \n");
        sb.append("                            , CGTV.TERM_ID                                                          \n");
        sb.append("                            , CGTV.NOTES_FLAG                                                       \n");
        sb.append("                            , CGTV.PAYMENT_METHOD                                                   \n");
        sb.append("                            , CGTV.VENDOR_ACCT_CHECK                                                \n");
        sb.append("                            , CGTV.ORG_ID                                                           \n");
        sb.append("                            , CGTV.CURRENCY_TYPE                                                    \n");
        sb.append("                            , CGTV.TRX_TYPE                                                         \n");
        sb.append("                         FROM APPS.CBO_GL_TERMS_V CGTV                                              \n");
        sb.append("                        WHERE 1 = 1                                                                 \n");
        sb.append("			                AND CGTV.ORG_ID = :compCd				 							  			  \n");
        sb.append("			                AND CGTV.SP_ENABLED_FLAG = 'Y'				 							  \n");
        sb.append("                         AND CGTV.DEFAULT_TERM_FLAG = 'Y'                                            \n");
        if( null != integration_vendor_num && !integration_vendor_num.equals("")) {
            sb.append("                        UNION                                                                       \n");
            sb.append("                       SELECT GTV.NAME AS TERM_NAME                                                 \n");
            sb.append("                            , GTV.DESCRIPTION                                                       \n");
            sb.append("                            , GTV.TERM_ID                                                           \n");
            sb.append("                            , GTV.NOTES_FLAG                                                        \n");
            sb.append("                            , GTV.PAYMENT_METHOD                                                    \n");
            sb.append("                            , GTV.VENDOR_ACCT_CHECK                                                 \n");
            sb.append("                            , GTV.ORG_ID                                                            \n");
            sb.append("                            , GTV.CURRENCY_TYPE                                                     \n");
            sb.append("                            , GTV.TRX_TYPE                                                          \n");
            sb.append("                         FROM APPS.CBO_GL_TERMS_V GTV                                               \n");
            sb.append("                        WHERE 1 = 1                                                                 \n");
            sb.append("			AND GTV.ORG_ID = :compCd				 							  			  \n");
            sb.append("			AND GTV.SP_ENABLED_FLAG = 'Y'				 							  	  \n");
            sb.append("                          AND GTV.TERM_ID IN (                                                      \n");
            sb.append("                                              SELECT VC.VEN_TERMS_ID                                \n");
            sb.append("                                                FROM APPS.CBO_SP_VENDOR_CUSTOMER_V VC               \n");
            sb.append("                                               WHERE 1 = 1                                          \n");
            sb.append("                                                 AND VC.ORG_ID = GTV.ORG_ID                         \n");
            sb.append("	  							   AND VC.ORG_ID = :compCd				 				  \n");
            sb.append("	  							   AND VC.INTEGRATION_VENDOR_NUM = '"+integration_vendor_num+"'"        );
            sb.append("                                             )                                                      \n");
        }
        if(null != dept_code && !dept_code.equals("")) {
            sb.append("                        UNION                                                                       \n");
            sb.append("                       SELECT ST.NAME AS TERM_NAME                                                  \n");
            sb.append("                            , ST.DESCRIPTION                                                        \n");
            sb.append("                            , ST.TERM_ID AS TERM_ID                                                \n");
            sb.append("                            , ST.NOTES_FLAG                                                         \n");
            sb.append("                            , ST.PAYMENT_METHOD                                                     \n");
            sb.append("                            , ST.VENDOR_ACCT_CHECK                                                  \n");
            sb.append("                            , ST.COMP_CD AS ORG_ID                                                             \n");
            sb.append("                            , ST.CURRENCY_TYPE                                                      \n");
            sb.append("                            , ST.TRX_TYPE                                                           \n");
            sb.append("                         FROM TB_STERMS ST                                                             \n");
            sb.append("                        WHERE 1 = 1                                                                 \n");
            sb.append("			AND ST.COMP_CD = :compCd				 							  			  \n");
            if (!StringUtils.isEmpty(dept_code)) {// 제목
                sb.append("			AND ST.DEPT_CD = :deptCd				 							  		  \n");
            }
            sb.append("			AND ST.EA_ENABLED_FLAG = 'Y'				 							  	  \n");
        }
        sb.append("                      ) T                                          								  \n");
        sb.append("                WHERE 1 = 1                                                                         \n");
        sb.append("	AND T.ORG_ID = :compCd				 							  			  		               \n");
        sb.append("	AND T.CURRENCY_TYPE = :currency_type				 											   \n");
        if (!StringUtils.isEmpty(ttype_interface_module)) {
            sb.append("	AND T.TRX_TYPE = :ttype_interface_module			 													  \n");
        }
        sb.append("	AND UPPER(T.TERM_ID) LIKE '%' || UPPER(:search_code) || '%' 						  	            \n");
        sb.append("	AND UPPER(T.TERM_NAME || T.DESCRIPTION) LIKE '%' || UPPER(:search_name) || '%' 		                \n");
        sb.append(" 	 				  ORDER BY T.TERM_NAME				 											\n");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", compCd);

        query.setParameter("currency_type", currency_type);
        query.setParameter("search_code", search_code);
        query.setParameter("search_name", search_name);
        if (!StringUtils.isEmpty(dept_code)) {
            query.setParameter("deptCd", dept_code);
        }
        if (!StringUtils.isEmpty(ttype_interface_module)) {
            query.setParameter("ttype_interface_module", ttype_interface_module);
        }

//        query.setParameter("ttype_interface_module", ttype_interface_module);
//        query.setParameter("sp_enabled_flag", sp_enabled_flag);
//        query.setParameter("default_term_flag", default_term_flag);
//        query.setParameter("integration_vendor_num", integration_vendor_num);
//        query.setParameter("ea_enabled_flag", ea_enabled_flag);
//        query.setParameter("deptCd", dept_code);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, ErpGlTermsDto.class);
    }

    @Override
    public List<ErpGlCodesDto> getGlSlipTypeList(ErpGlSlipDto erpGlSlipDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
            "     L.CODE_TYPE_ID " +
            " ,   L.CODE " +
            " ,   L.MEANING " +
            " FROM APPS.CBO_GL_CODE_HEADER H " +
            "    , APPS.CBO_GL_CODE_LINE   L " +
            "         WHERE H.CODE_TYPE_ID = L.CODE_TYPE_ID " +
            "           AND H.CODE_TYPE = 'CD0230'");

        if(!StringUtils.isEmpty(erpGlSlipDto.getSearch())) {
            sb.append(" AND (L.CODE LIKE '%' || :search || '%' ");
            sb.append(" OR L.MEANING LIKE '%' || :search || '%') ");
        }

        Query query = entityManager.createNativeQuery(sb.toString());

        if(!StringUtils.isEmpty(erpGlSlipDto.getSearch())) {
            query.setParameter("search", erpGlSlipDto.getSearch());
        }

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, ErpGlCodesDto.class);
    }
}

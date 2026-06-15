package com.iljin.apiServer.ijeas.system.trx;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.iljin.apiServer.ijeas.system.trx.QTrx.trx;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class TrxQdslRepositoryImpl implements TrxQdslRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final Util util;

    @Override
    public List<TrxDto> getTrxList(TrxDto trxDto){
        List<TrxDto> result = queryFactory
                .select(new QTrxDto(
                        trx.compCd,
                        trx.trxTypeCd,
                        trx.trxTypeNm,
                        trx.slipTypeCd
                ))
                .from(trx)
                .where(trx.compCd.eq(trxDto.compCd)
                        .and(trx.enabledFlag.eq("Y"))
                        .and(trxSearch(trxDto.trxTypeNm))
                )
                .fetch();

        return result;
    }

    private BooleanExpression trxSearch(String trxTypeNm){
        if(hasText(trxTypeNm)){
            return trx.trxTypeCd.containsIgnoreCase(trxTypeNm)
                    .or(trx.trxTypeNm.containsIgnoreCase(trxTypeNm));
        }else{
            return null;
        }
    }

    /*@Override
    public List<TrxDto> getSlipTrxList(TrxDto trxDto){
        List<TrxDto> result = queryFactory
                .select(new QTrxDto(
                        trx.compCd,
                        trx.trxTypeCd,
                        trx.trxTypeNm,
                        trx.trxSpTypeCd,
                        trx.inputMoule,
                        trx.interfaceSlipType,
                        trx.prepaymentFlag,
                        trx.clearingAcctCd,
                        trx.addInfoType,
                        trx.integrationVendorNum,
                        trx.paymentReceiptTermId,
                        account.acctCd,
                        account.drCr,
                        trx.trxTypeDescription,
                        trx.slipDisplayFlag,
                        trx.slipCreationTargetFlag,
                        trx.awtGroupId,
                        trx.awtGroupNm,
                        trx.orderBy,
                        ExpressionUtils.as(JPAExpressions
                                        .select(cboSpAwtGroupV.locationCode)
                                        .from(cboSpAwtGroupV)
                                        .where(cboSpAwtGroupV.orgId.eq(Integer.valueOf(trxDto.compCd)))
                                ,"locationCode"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(cboSpLocationV.description)
                                        .from(cboSpLocationV)
                                        .where()
                                ,"locationName"),
                        Expressions.asNumber(trxDto.personId).as("personId")
                ))
                .from(trx)
                .join(trxEmp).on(
                        trx.compCd.eq(trxEmp.compCd)
                        .and(trx.trxTypeCd.eq(trxEmp.trxTypeCd))
                        .and(trxEmp.enableFlag.eq("Y"))
                        .and(trxEmp.personId.eq(trxDto.personId))
                )
                .leftJoin(account).on(
                        trx.trxTypeCd.eq(account.trxTypeCd)
                        .and(account.enabledFlag.eq("Y"))
                        .and(account.drCr.eq( trx.interfaceModule.toString().equals("AP") ? "Cr" : "Dr" ))
                )
                .where(trx.compCd.eq(trxDto.compCd)
                        .and(trx.enabledFlag.eq("Y"))
                        .and(trx.inputMoule.in("SP", "PP", "IN"))
                        .and(trx.trxSpTypeCd.eq(trxDto.trxSpTypeCd))
                )
                .fetch();


        return null;
    }*/



    public List<TrxDto> getSlipTrxList(TrxDto trxDto){
        StringBuilder sb = new StringBuilder();


        sb.append("SELECT A.TRX_TYPE_NM  " +
                "               , A.TRX_TYPE_CD   " +
                "               , A.TRX_SP_TYPE_CD   " +
                "               , A.SLIP_TYPE_CD   " +
                "               , A.SLIP_TYPE_NM   " +
                "                  , A.INPUT_MODULE   " +
                "                  , A.INTERFACE_MODULE  " +
                "                  , A.INTERFACE_SLIP_TYPE  " +
                "                  , A.PREPAYMENT_FLAG  " +
                "                  , A.CLEARING_ACCT_CD " +
                "                  , A.ADD_INFO_TYPE  " +
                "                  , A.INTEGRATION_VENDOR_NUM  " +
                "                  , A.PAYMENT_RECEIPT_TERM_ID  " +
                "                  , COUNT(A.ACCT_CD) AS HEADER_ACCT_CNT " +
                "                  , MAX(A.ACCT_CD) AS HEADER_ACCT_CODE " +
                "                  , MAX(A.TRX_TYPE_DESCRIPTION) AS HEADER_DESCRIPTION " +
                "                  , MAX(A.ACCT_NM) AS HEADER_ACCT_NAME " +
                "                  , A.DR_CR  " +
                "                  , A.SLIP_DISPLAY_FLAG   " +
                "                  , A.SLIP_CREATION_TARGET_FLAG  " +
                "                  , A.AWT_GROUP_ID  " +
                "                  , A.AWT_GROUP_NM  " +
                "                  , A.USER_VENDOR_FLAG " +
                "                  , A.LOCATION_CODE " +
                "                  , A.LOCATION_NAME  " +
                "                    , B.EVIDENCE_NAME  " +
                "                    , B.EVIDENCE_CODE  " +
                "                    , B.LINE_ATTRIBUTE1  " +
                "                    , B.LINE_ATTRIBUTE1_NAME   " +
                "                    , B.LINE_ATTRIBUTE2   " +
                "                    , B.LINE_ATTRIBUTE3 " +
                " " +
                "                FROM (  " +
                "                     SELECT CGTH.TRX_TYPE_NM " +
                "                           ,CGTH.TRX_TYPE_CD " +
                "                           ,CGTH.TRX_SP_TYPE_CD " +
                "                           ,CGTH.SLIP_TYPE_CD " +
                "                           ,CGTH.SLIP_TYPE_NM " +
                "                           ,CGTH.INPUT_MODULE " +
                "                           ,CGTH.INTERFACE_MODULE " +
                "                           ,CGTH.INTERFACE_SLIP_TYPE " +
                "                           ,CGTH.PREPAYMENT_FLAG " +
                "                           ,CGTH.CLEARING_ACCT_CD " +
                "                           ,CGTH.ADD_INFO_TYPE " +
                "                           ,CGTH.INTEGRATION_VENDOR_NUM " +
                "                           ,CGTH.PAYMENT_RECEIPT_TERM_ID " +
                "                           ,CGTA.ACCT_CD " +
                "                           ,CGTA.ACCT_NM " +
                "                           ,CGTA.DR_CR " +
                "                           ,CGTH.TRX_TYPE_DESCRIPTION " +
                "                           ,CGTH.SLIP_DISPLAY_FLAG  " +
                "                           ,CGTH.SLIP_CREATION_TARGET_FLAG " +
                "                           ,CGTH.AWT_GROUP_ID  " +
                "                           ,CGTH.AWT_GROUP_NM  " +
                "                           ,CGTH.ORDER_BY " +
                "                           ,CGTH.USER_VENDOR_FLAG " +
                "                           ,(SELECT CSAG.LOCATION_CODE " +
                "                               FROM APPS.CBO_SP_AWT_GROUP_V CSAG " +
                "                              WHERE CSAG.ORG_ID = CGTH.COMP_CD " +
                "                                AND CSAG.AWT_GROUP_ID = CGTH.AWT_GROUP_ID " +
                "                            ) AS LOCATION_CODE " +
                "                           ,(SELECT LV.DESCRIPTION " +
                "                               FROM APPS.CBO_SP_AWT_GROUP_V CSAG " +
                "                                    ,APPS.CBO_SP_LOCATION_V LV " +
                "                              WHERE CSAG.ORG_ID = LV.ORG_ID " +
                "                                AND CSAG.LOCATION_CODE = LV.LOCATION_CODE  " +
                "                                AND CSAG.ORG_ID = CGTH.COMP_CD " +
                "                                AND CSAG.AWT_GROUP_ID = CGTH.AWT_GROUP_ID " +
                "                            ) AS LOCATION_NAME " +
                "                      FROM TB_MST_TRX CGTH " +
                "                      JOIN TB_MST_TRX_EMP CAET " +
                "                        ON CGTH.COMP_CD = CAET.COMP_CD " +
                "                       AND CGTH.TRX_TYPE_CD = CAET.TRX_TYPE_CD " +
                "                       AND CAET.ENABLE_FLAG = 'Y' " +
                "                       AND CAET.PERSON_ID = :personId " +
                "                      LEFT OUTER JOIN TB_MST_ACCT CGTA " +
                "                        ON CGTH.TRX_TYPE_CD = CGTA.TRX_TYPE_CD " +
                "                       AND CGTA.ENABLED_FLAG = 'Y' " +
                "                       AND CGTA.DR_CR = DECODE(CGTH.INTERFACE_MODULE,'AP','Cr','Dr') " +
                "                     WHERE CGTH.ENABLED_FLAG = 'Y' " +
                "                       AND CGTH.INPUT_MODULE IN ('SP', 'PP', 'IM') " +
                "                       AND CGTH.COMP_CD = :compCd " +
                "                       AND CGTH.TRX_SP_TYPE_CD = :trxSpTypeCd " +
                "                   ) A " +
                "                 LEFT OUTER JOIN ( " +
                "                    SELECT CGEV.EVIDENCE_NAME  " +
                "                           , CGEV.EVIDENCE_CODE  " +
                "                           , CGEV.TRX_TYPE_CODE  " +
                "                           , CGEV.LINE_ATTRIBUTE1  " +
                "                           , CASE WHEN CGEV.LINE_ATTRIBUTE1 = 'V' THEN '부가세'  " +
                "                                  WHEN CGEV.LINE_ATTRIBUTE1 = 'W' THEN '원천세'  " +
                "                                  ELSE '' END AS LINE_ATTRIBUTE1_NAME  " +
                "                           , NVL(CGCV.LINE_ATTRIBUTE2, '') AS LINE_ATTRIBUTE2   " +
                "                           , NVL(CGCV.LINE_ATTRIBUTE3, 'C') AS LINE_ATTRIBUTE3   " +
                "                           , CGCV.CODE    " +
                "                      FROM APPS.CBO_GL_TRX_TYPE_EVIDENCE_V CGEV   " +
                "                      LEFT OUTER JOIN APPS.CBO_GL_CODE_V CGCV   " +
                "                        ON CGEV.EVIDENCE_CODE = CGCV.CODE   " +
                "                       AND CGCV.CODE_TYPE = 'CD0100'    " +
                "                       AND CGCV.HEADER_ENABLED_FLAG = 'Y'   " +
                "                       AND CGCV.LINE_ENABLED_FLAG = 'Y'  " +
                "                     WHERE CGEV.ORG_ID = :compCd  " +
                "                       AND CGEV.ENABLED_FLAG = 'Y'  " +
                "                       AND CGEV.TRX_TYPE_CODE IN (SELECT TRX_TYPE_CODE  " +
                "                                                    FROM APPS.CBO_GL_TRX_TYPE_EVIDENCE_V  " +
                "                                                   WHERE ENABLED_FLAG = 'Y' HAVING COUNT(TRX_TYPE_CODE) = 1  " +
                "                                                   GROUP BY TRX_TYPE_CODE)  " +
                "                 ) B   " +
                "                 ON A.TRX_TYPE_CD = B.TRX_TYPE_CODE " +
                "                WHERE 1 = 1  ");

        if(hasText(trxDto.trxTypeCd)) {
            sb.append("            AND A.TRX_TYPE_CD like '%' || UPPER(:trxTypeCd) || '%' " );
        }

        if(hasText(trxDto.trxTypeNm)){
            sb.append("            AND A.TRX_TYPE_NM like '%' || UPPER(:trxTypeNm) || '%' " );
        }

        sb.append(" " +
                "                GROUP BY A.TRX_TYPE_NM " +
                "                      ,A.TRX_TYPE_CD " +
                "                      ,A.SLIP_TYPE_CD " +
                "                      ,A.SLIP_TYPE_NM " +
                "                      ,A.INPUT_MODULE " +
                "                      ,A.INTERFACE_MODULE " +
                "                      ,A.INTERFACE_SLIP_TYPE " +
                "                      ,A.PREPAYMENT_FLAG " +
                "                      ,A.CLEARING_ACCT_CD " +
                "                      ,A.ADD_INFO_TYPE " +
                "                      ,A.INTEGRATION_VENDOR_NUM " +
                "                      ,A.PAYMENT_RECEIPT_TERM_ID  " +
                "                      ,A.TRX_SP_TYPE_CD  " +
                "                      ,A.DR_CR   " +
                "                      ,A.SLIP_DISPLAY_FLAG  " +
                "                      ,A.SLIP_CREATION_TARGET_FLAG  " +
                "                      ,A.AWT_GROUP_ID  " +
                "                      ,A.AWT_GROUP_NM " +
                "                      ,A.LOCATION_CODE  " +
                "                      ,A.LOCATION_NAME  " +
                "                      ,A.ORDER_BY  " +
                "                      ,A.USER_VENDOR_FLAG" +
                "                      ,B.EVIDENCE_NAME " +
                "                      ,B.EVIDENCE_CODE " +
                "                      ,B.LINE_ATTRIBUTE1 " +
                "                      ,B.LINE_ATTRIBUTE1_NAME  " +
                "                      ,B.LINE_ATTRIBUTE2 " +
                "                        ,B.LINE_ATTRIBUTE3 " +
                "                ORDER BY A.ORDER_BY, A.INPUT_MODULE DESC, A.TRX_TYPE_CD");





        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("compCd", trxDto.compCd);
        query.setParameter("trxSpTypeCd", trxDto.trxSpTypeCd);
        query.setParameter("personId", trxDto.personId);

        if(hasText(trxDto.trxTypeCd)) {
            query.setParameter("trxTypeCd", trxDto.trxTypeCd);
        }

        if(hasText(trxDto.trxTypeNm)){
            query.setParameter("trxTypeNm", trxDto.trxTypeNm);
        }

        return new JpaResultMapper().list(query, TrxDto.class);

    }

    @Override
    public List<TrxDto> getAwtInfo(TrxDto trxDto){
        StringBuilder sb = new StringBuilder();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        sb.append("SELECT CSAT.GROUP_ID " +
                "       , CSAT.TAX_NAME " +
                "       , CSAT.DESCRIPTION " +
                "       , CSAT.TAX_RATE_ID " +
                "       , CSAT.AWT_ACCOUNT " +
                "       , CSAT.AWT_ACCOUNT_NAME " +
                "       , NVL(CSAT.TAX_RATE, 0) AS TAX_RATE " +
                "       , CSAT.ORG_ID " +
                "       , CSAT.LOCATION_CODE " +
                "       , CSAT.VENDOR_NAME " +
                "       , CSAT.VENDOR_SITE_NAME " +
                "       , CSAT.TAX_CODE_COA " +
                "    FROM APPS.CBO_SP_AWT_TAX_CODE_V CSAT " +
                "   WHERE CSAT.GROUP_ID = :awtGroupId " +
                "     AND CSAT.ORG_ID = :orgId ");

//        if(trxDto.trxTypeCd.equals("ETCAWT")){
//            sb.append("UNION ALL " +
//                    "  SELECT CSAT.GROUP_ID " +
//                    "       , CSAT.TAX_NAME " +
//                    "       , CSAT.DESCRIPTION " +
//                    "       , CSAT.TAX_RATE_ID " +
//                    "       , CSAT.AWT_ACCOUNT " +
//                    "       , CSAT.AWT_ACCOUNT_NAME " +
//                    "       , NVL(CSAT.TAX_RATE, 0) AS TAX_RATE " +
//                    "       , CSAT.ORG_ID " +
//                    "       , CSAT.LOCATION_CODE " +
//                    "       , CSAT.VENDOR_NAME " +
//                    "       , CSAT.VENDOR_SITE_NAME " +
//                    "       , CSAT.TAX_CODE_COA " +
//                    "    FROM APPS.CBO_SP_AWT_TAX_CODE_V CSAT " +
//                    "   WHERE CSAT.ORG_ID = :orgId " +
//                    "     AND CSAT.GROUP_ID = (SELECT CSAG.AWT_GROUP_ID " +
//                    "                            FROM APPS.CBO_SP_AWT_GROUP_V CSAG " +
//                    "                           WHERE CSAG.DESCRIPTION like '%고용보험%' " +
//                    "                             AND CSAG.AWT_GROUP_NAME like 'WHTG%' " +
//                    "                             AND CSAG.LOCATION_CODE = (SELECT LOCATION_CODE FROM APPS.CBO_SP_AWT_GROUP_V WHERE AWT_GROUP_ID = :awtGroupId)) ");
//        }

        if(trxDto.trxTypeCd.equals("ETCAWT")){
            sb.append("UNION ALL " +
                    "  SELECT CSAT.GROUP_ID " +
                    "       , CSAT.TAX_NAME " +
                    "       , CSAT.DESCRIPTION " +
                    "       , CSAT.TAX_RATE_ID " +
                    "       , CSAT.AWT_ACCOUNT " +
                    "       , CSAT.AWT_ACCOUNT_NAME " +
                    "       , NVL(CSAT.TAX_RATE, 0) AS TAX_RATE " +
                    "       , CSAT.ORG_ID " +
                    "       , CSAT.LOCATION_CODE " +
                    "       , CSAT.VENDOR_NAME " +
                    "       , CSAT.VENDOR_SITE_NAME " +
                    "       , CSAT.TAX_CODE_COA " +
                    "    FROM APPS.CBO_SP_AWT_TAX_CODE_V CSAT " +
                    "   WHERE CSAT.ORG_ID = :orgId " +
                    "     AND CSAT.GROUP_ID IN (SELECT C.AWT_GROUP_ID " +
                    "                            FROM CBO_SP_AWT_GROUP_V C " +
                    "                           WHERE C.AWT_GROUP_NAME IN('WHTG_IJE_120_O_20','WHTG_IJE_120_O_30','WHTG_IJE_120_O_40')) ");
        }

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("awtGroupId", trxDto.awtGroupId);
        query.setParameter("orgId", compCd);

        return new JpaResultMapper().list(query, TrxDto.class);
    }
}


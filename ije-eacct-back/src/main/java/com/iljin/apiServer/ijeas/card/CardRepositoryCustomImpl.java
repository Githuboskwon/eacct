package com.iljin.apiServer.ijeas.card;

import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CardRepositoryCustomImpl implements CardRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;


    @Override
    public List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto) {

        String erpSlipFlag = cardUseListDto.getErpSlipFlag();

        StringBuilder sb = new StringBuilder();
        sb.append("     SELECT A.COMP_CD " +
                  "          , A.USED_NO " +
                  "          , A.CARD_NO " +
                  "          , CASE WHEN SUBSTR(A.SLIP_NO,1,2) = 'TG' THEN A.SLIP_NO                       " +
                  "            ELSE NVL((SELECT SLIP_NO FROM TB_SLIP_DT WHERE BUNGAE_NO=A.SLIP_NO),'') END AS SLIP_NO  " +
                  "          , A.ABROAD_FLAG  " +
                  "          , A.USED_DT " +
                  "          , A.BUY_DT " +
                  "          , A.USED_TIME " +
                  "          , A.USER_ID " +
                  "          , A.USER_NM " +
                  "          , A.DEPT_CD " +
                  "          , A.DEPT_NM " +
                  "          , A.IRS_NO " +
                  "          , A.STORE_CD " +
                  "          , A.STORE_NM " +
                  "          , A.MCC_NM " +
                  "          , A.USED_AMT " +
                  "          , A.USED_CUR " +
                  "          , A.USED_FOR_AMT " +
                  "          , A.CARD_COM_CD " +
                  "          , A.CARD_COM_NM " +
                  "          , A.ORIGIN_AMT " +
                  "          , A.SURTAX " +
                  "          , A.SERVICE_CHARGE " +
                  "          , A.STATUS " +
                  "          , SF_GET_COMMNAME(:compCd ,'CARD_STATUS_CD', A.STATUS) AS STATUS_TEXT " +
                  "          , A.CANCEL_FLAG " +
                  "          , SF_GET_COMMNAME(:compCd ,'CARD_CANCEL_YN', A.CANCEL_FLAG) AS CANCEL_FLAG_TEXT " +
                  "          , A.EMPLOYEE_NO " +
                  "          , A.STORE_OWNER " +
                  "          , A.STORE_ADDR " +
                  "          , A.DESC1 " +
                  "          , A.DESC4 " +
                  "          , A.MCC_CD " +
                  "          , A.APPR_NO " +
                  "          , A.TAX_TYPE " +
                  "          , CAST(CASE WHEN A.SURTAX = 0 THEN 'N' ELSE 'Y' END AS VARCHAR2(2))  TAX_FLAG " +
                  "          , SF_GET_COMMNAME(:compCd ,'TAX_FLAG', CASE WHEN A.SURTAX = 0 THEN 'N' ELSE 'Y' END) AS TAX_FLAG_TEXT " +
                  "          , A.TEMP1" +
                  "          , A.CARD_TYPE " +
                  "          , CASE WHEN USED_DT >= (SELECT CLOS_YM || '01' FROM TB_CLOSE_MNG WHERE CLOS_YM = SUBSTR(USED_DT, 1, 6) AND TB_CLOSE_MNG.COMP_CD = A.COMP_CD AND CLOS_STAT_CD = 'Open') " +
                  "                  AND USED_DT <= (SELECT TO_CHAR(LAST_DAY(TO_DATE(CLOS_YM, 'YYYYMM')), 'YYYYMMDD') FROM TB_CLOSE_MNG WHERE CLOS_YM = SUBSTR(USED_DT, 1, 6) AND TB_CLOSE_MNG.COMP_CD = A.COMP_CD AND CLOS_STAT_CD = 'Open') " +
                  "                 THEN 'USED_DATE' " +
                  "                 ELSE 'BUY_DATE' END AS DT_CASE " +
                  "          , B.TAX_RATE_ID AS TAX_ID " +
                  "          , B.TAX_RATE_CODE AS TAX_CODE " +
                  "          , B.TAX_ACCT_CODE " +
                  "          , B.PERCENTAGE_RATE AS TAX_PERCENTAGE_RATE " +
                  "       FROM TB_CARD_USE_LIST A " +
                  "  LEFT JOIN APPS.CBO_SP_TAX_CODE_V B " +
                  "         ON APPS.CBO_SP_COMMON_PKG.GET_CARD_TAX_CODE (P_TAX_CODE      => '0' " +
                  "                                                     ,P_SUPPLY_AMOUNT => A.ORIGIN_AMT " +
                  "                                                     ,P_TAX_AMOUNT    => A.SURTAX " +
                  "                                                     ,P_BUSINESS_TYPE => A.MCC_NM ,P_LEDGER_ID => :ledgerId ) = B.TAX_RATE_CODE " +
                  "        AND B.TAX_EVIDENCE_TYPE = :taxEvidenceType " +
                  "        AND B.LEDGER_ID = :ledgerId " +
                  "  LEFT JOIN TB_CARD_MASTER C " +
                  "         ON C.CRD_NO = A.CARD_NO" +
                  "        AND C.COMP_CD = :compCd " +
                  "      WHERE 1 = 1 " +
//                  "        AND A.CARD_TYPE <> 'F' " +
                  "        AND A.COMP_CD = :compCd");

        if(!StringUtils.isEmpty(cardUseListDto.getSlipHeaderId())) {
            sb.append("    AND NVL(A.SLIP_HEADER_ID, :slipHeaderId) = :slipHeaderId");
        }
        if (!StringUtils.isEmpty(cardUseListDto.getSearchDtmFr())) {
            sb.append(" AND A.USED_DT >= :searchDtmFr");
        }
        if (!StringUtils.isEmpty(cardUseListDto.getSearchDtmTo())) {
            sb.append(" AND A.USED_DT <=  :searchDtmTo");
        }
        if(!StringUtils.isEmpty(cardUseListDto.getStatus())) {
//AS-IS 소스, 주석처리함
//            sb.append("    AND A.STATUS IN ('01', 'SV')" );
//        } else {
//            sb.append("    AND A.STATUS IN ('01', '07', 'SV')" );
            if(cardUseListDto.getStatus().equals("R1")) {
                //상태값이 미처리 + 미처리(반려) 인 경우 미처리(반려) 내용도 포함한다.
                sb.append("    AND A.STATUS IN ('01', 'RU')" );
            } else {
                sb.append("    AND A.STATUS = :status");
            }
        }
        if(!StringUtils.isEmpty(cardUseListDto.getCardType())) {
            if("F".equals(cardUseListDto.getCardType())){
                sb.append("    AND A.CARD_TYPE = 'F' ");
            }else{
                sb.append("    AND A.CARD_TYPE <> 'F' ");
            }
        }
        if(!StringUtils.isEmpty(cardUseListDto.getCancelFlag())) {
            sb.append("    AND A.CANCEL_FLAG = :cancelFlag");
        }
        if(!StringUtils.isEmpty(cardUseListDto.getCardNo())) {
            sb.append("    AND A.CARD_NO  = :cardNo");
        }
        if (erpSlipFlag != null && !erpSlipFlag.equals("Y")) {
            sb.append("    AND A.USER_ID  = :userId");
        }
        if(!StringUtils.isEmpty(cardUseListDto.getUserId())) {
            sb.append("    AND A.USER_ID  = :userId");
        }
        sb.append("   ORDER BY A.USED_DT DESC " +
                  "          , A.USED_TIME DESC" ) ;
        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("taxEvidenceType", cardUseListDto.getTaxEvidenceType());
        query.setParameter("compCd", cardUseListDto.getCompCd());
        query.setParameter("ledgerId", cardUseListDto.getLedgerId());

        if(!StringUtils.isEmpty(cardUseListDto.getSlipHeaderId())) {
            query.setParameter("slipHeaderId", cardUseListDto.getSlipHeaderId());
        }
        if (!StringUtils.isEmpty(cardUseListDto.getSearchDtmFr())) {// 기안일자
            query.setParameter("searchDtmFr", cardUseListDto.getSearchDtmFr().replaceAll("-", ""));
        }
        if (!StringUtils.isEmpty(cardUseListDto.getSearchDtmTo())) {
            query.setParameter("searchDtmTo", cardUseListDto.getSearchDtmTo().replaceAll("-", ""));
        }
        if(!StringUtils.isEmpty(cardUseListDto.getStatus())) {
            if(!cardUseListDto.getStatus().equals("R1")) {
                query.setParameter("status", cardUseListDto.getStatus());
            }
        }
//        if(!StringUtils.isEmpty(cardUseListDto.getCardType())) {
//            query.setParameter("cardType", cardUseListDto.getCardType());
//        }
        if(!StringUtils.isEmpty(cardUseListDto.getCancelFlag())) {
            query.setParameter("cancelFlag", cardUseListDto.getCancelFlag());
        }
        if(!StringUtils.isEmpty(cardUseListDto.getCardNo())) {
            query.setParameter("cardNo", cardUseListDto.getCardNo());
        }
        if(!StringUtils.isEmpty(cardUseListDto.getUserId())) {
            query.setParameter("userId", cardUseListDto.getUserId());
        }
        if(!StringUtils.isEmpty(cardUseListDto.getTaxEvidenceType())) {
            query.setParameter("taxEvidenceType", cardUseListDto.getTaxEvidenceType());
        }
        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, CardUseListDto.class);
    }
}

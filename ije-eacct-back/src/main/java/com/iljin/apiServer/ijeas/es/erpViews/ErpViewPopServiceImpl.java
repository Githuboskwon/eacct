package com.iljin.apiServer.ijeas.es.erpViews;

import com.iljin.apiServer.core.cert.CertFileDto;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ErpViewPopServiceImpl implements ErpViewPopService {


    @PersistenceContext
    private EntityManager entityManager;

    private final Util util;

    @Override
    public List<ErpViewPopFundSlipDto> getPopDealType(String searchDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT DISTINCT D.DEAL_TYPE , '' " +
                "  FROM CBO_SP_TR_TRX_HEADERS_V D");

        if(!StringUtils.isEmpty(searchDate)) {
            sb.append(" WHERE lower(D.DEAL_TYPE) LIKE '%'||  lower(:searchDate) ||'%' ");
        }

        Query query = entityManager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(searchDate)) {
            query.setParameter("searchDate", searchDate);
        }

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, ErpViewPopFundSlipDto.class);
    }

    @Override
    public List<ErpViewPopFundSlipDto> getPopProductType(String searchDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT DISTINCT '' , D.PRODUCT_TYPE " +
                "  FROM CBO_SP_TR_TRX_HEADERS_V D" +
                "  WHERE D.PRODUCT_TYPE IS NOT NULL");

        if(!StringUtils.isEmpty(searchDate)) {
            sb.append(" AND lower(D.PRODUCT_TYPE) LIKE '%'||  lower(:searchDate) ||'%' ");
        }

        Query query = entityManager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(searchDate)) {
            query.setParameter("searchDate", searchDate);
        }

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, ErpViewPopFundSlipDto.class);
    }

}

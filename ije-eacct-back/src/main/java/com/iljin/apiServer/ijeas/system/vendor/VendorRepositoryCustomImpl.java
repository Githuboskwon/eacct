package com.iljin.apiServer.ijeas.system.vendor;

import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class VendorRepositoryCustomImpl implements VendorRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal getPrepayCount(Integer vendorId, Integer vendorSiteId, String paymentCurrencyCode){
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT COUNT(*) " +
                "     FROM APPS.CBO_SP_APPLY_PREPAYS_V " +
                "    WHERE VENDOR_ID = :vendorId " +
                "      AND VENDOR_SITE_ID = :vendorSiteId " +
                "      AND PAYMENT_CURRENCY_CODE = :paymentCurrencyCode ");

        Query query = entityManager.createNativeQuery(sb.toString());

        query.setParameter("vendorId", vendorId);
        query.setParameter("vendorSiteId", vendorSiteId);
        query.setParameter("paymentCurrencyCode", paymentCurrencyCode);

        List<VendorDto> result = new JpaResultMapper().list(query, VendorDto.class);

        return result.get(0).prepayCnt;
    }
}

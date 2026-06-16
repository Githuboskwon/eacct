package com.iljin.apiServer.ijeas.sm.tax;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Repository
public class TaxRepositoryCustomImpl implements TaxRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TaxDto> getTaxCodeList(TaxDto taxDto) {
        String compCd = taxDto.getCompCd();
        String taxCd = taxDto.getTaxCd();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT" +
                "    TX.COMP_CD," +
                "    TX.TAX_CD," +
                "    TX.TAX_NM," +
                "    TX.TAX_RT," +
                "    TX.USE_YN," +
                "    TX.ORDER_SEQ," +
                "    TX.EVD_TYPE_CD," +
                "    TX.EVD_TYPE_NM," +
                "    TX.REF1," +
                "    TX.REF2," +
                "    TX.REF3" +
                "  FROM TB_MST_TAX TX" +
                " WHERE 1=1" +
                "   AND TX.COMP_CD = :compCd" +
                "   AND (UPPER(TX.TAX_CD) LIKE '%' || UPPER(:taxCd) || '%' OR UPPER(TX.TAX_NM) LIKE '%' || UPPER(:taxCd) || '%')" +
                " ORDER BY TX.ORDER_SEQ ASC");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("taxCd", taxCd);

        return com.iljin.apiServer.core.util.ResultMapperUtil.list(query, TaxDto.class);
    }
}

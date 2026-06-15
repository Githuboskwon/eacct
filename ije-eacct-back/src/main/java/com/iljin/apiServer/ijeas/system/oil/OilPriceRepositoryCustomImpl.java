package com.iljin.apiServer.ijeas.system.oil;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OilPriceRepositoryCustomImpl implements OilPriceRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto) {
        String compCd = oilPriceDto.getCompCd();
        String baseYm = oilPriceDto.getBaseYm();
        if(!baseYm.isEmpty()) {
            baseYm = baseYm.replaceAll("-", "");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT OP.COMP_CD" +
                "       ,OP.BASE_YM" +
                "       ,OP.OIL_KIND_CD" +
                "       ,A.DETAIL_NM AS OIL_KIND_NM" +
                "       ,OP.OIL_UPCE" +
                "       ,OP.OIL_EFF" +
                "  FROM TB_STD_OIL_UPCE OP" +
                "       LEFT OUTER JOIN TB_CODE_DT A ON A.COMP_CD = OP.COMP_CD AND A.USE_YN = 'Y' AND A.GROUP_CD = 'OIL_KIND_CD' AND A.DETAIL_CD = OP.OIL_KIND_CD" +
                " WHERE 1=1" +
                "   AND OP.COMP_CD = :compCd" +
                "   AND OP.BASE_YM LIKE ('%'||NVL(:baseYm, '')||'%')"
        );


        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("compCd", compCd);
        query.setParameter("baseYm", baseYm);

        return new JpaResultMapper().list(query, OilPriceDto.class);
    }
}

package com.iljin.apiServer.ijeas.system.authority;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AuthorityRepositoryCustomImpl implements AuthorityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MenuAuthDto> getMenuByAuthority(String roleCd, String compCd) {
        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "WITH MN (COMP_CD, MENU_NO, MENU_NM, UPPER_MENU_NO, MENU_ORDR, LV) AS " +
                "( " +
                "  SELECT COMP_CD, MENU_NO, MENU_NM, UPPER_MENU_NO, MENU_ORDR, 0 LV " +
                "  FROM A_MENU " +
                "  WHERE MENU_ORDR = 0 " +
                "    AND COMP_CD = :compCd" +
                "  UNION ALL " +
                "  SELECT R.COMP_CD, R.MENU_NO, R.MENU_NM, R.UPPER_MENU_NO, R.MENU_ORDR, MN.LV + 1 " +
                "  FROM A_MENU R " +
                "  INNER JOIN MN ON R.UPPER_MENU_NO = MN.MENU_NO AND R.COMP_CD = MN.COMP_CD" +
                ") " +
                "SELECT " +
                "  CASE WHEN LENGTHB(MR.ROLE_CD) > 0 THEN '1' ELSE '0' END ROLE_CK, " +
                "  :roleCd, " +
                "  MN.MENU_NO, " +
                "  MN.MENU_NM, " +
                "  'Lv ' || TO_CHAR(MN.LV) || ' - ' || MN.MENU_NM AS MENU_DC, " +
                "  MN.COMP_CD, " +
                "  MN.LV AS menuLv, " +
                "  MN.MENU_ORDR AS menuOrder, " +
                "  MN.UPPER_MENU_NO " +
                "FROM MN " +
                "LEFT OUTER JOIN A_MENU_ROLE MR ON MR.ROLE_CD = :roleCd AND MR.COMP_CD = :compCd AND MR.MENU_NO = MN.MENU_NO " +
                "WHERE MN.MENU_ORDR <> 0 " +
                "ORDER BY MN.MENU_NO, MN.MENU_ORDR");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roleCd", roleCd);
        query.setParameter("compCd", compCd);

        return new JpaResultMapper().list(query, MenuAuthDto.class);
    }

}

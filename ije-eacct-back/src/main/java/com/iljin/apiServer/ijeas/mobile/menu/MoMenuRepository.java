package com.iljin.apiServer.ijeas.mobile.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoMenuRepository extends JpaRepository<MoMenu,MoMenuKey> {
    List<MoMenu> findByLoginId(String loginId);

    @Query(value = "" +
            "SELECT CASE WHEN LENGTH(um.menuNo) > 0 THEN '1' ELSE '0' END AS REG_YN" +
            "       ,menu.menuNo" +
            "       ,CASE WHEN menu.upperMenuNo='1' then menu.menuNm else CONCAT(CONCAT(m.menuNm, ' - '), menu.menuNm) end AS MENU_NM" +
            "       ,menu.menuNm as oMenuNm" +
            "       ,menu.relateImagePath as rImagePath" +
            "  FROM Menu menu" +
            "       INNER JOIN MenuAuth mr ON mr.menuNo = menu.menuNo and mr.roleCd = :roleCd" +
            "       LEFT OUTER JOIN MoMenu um ON um.menuNo = menu.menuNo AND um.loginId = :loginId" +
            "       LEFT OUTER JOIN Menu m ON m.menuNo = menu.upperMenuNo" +
            " WHERE 1=1" +
            "   AND (CASE WHEN menu.upperMenuNo IS NULL THEN '0' ELSE menu.upperMenuNo END) <> '0'" +
            "   AND menu.menuNo <> '7030000'" +
            " ORDER BY CASE WHEN LENGTH(um.menuNo) > 0 THEN '1' ELSE '0' END DESC" +
            "          ,menu.menuNo ASC ")
    List<Object[]> getMenuList(@Param("loginId") String loginId, @Param("roleCd") String roleCd);

    void deleteByLoginIdAndCompCd(String loginId, String compCd);
}

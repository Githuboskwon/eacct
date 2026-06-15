package com.iljin.apiServer.ijeas.system.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, MenuKey> {
    @Query(value = "" +
            "SELECT menu.menuNo AS menuNo" +
            "       ,menu.menuNm AS menuNm" +
            "       ,menu.programFileNm AS programFileNm" +
            "       ,menu.upperMenuNo AS upperMenuNo" +
            "       ,menu.menuOrder AS menuOrder" +
            "  FROM Menu menu" +
            "       INNER JOIN MenuAuth mr ON mr.menuNo = menu.menuNo AND mr.compCd = : compCd" +
            " WHERE mr.roleCd = :roleCd " +
            "   AND menu.compCd = :compCd " +
            "   AND menu.menuNo not like '9%'" +
            " ORDER BY menu.menuOrder, menu.menuNo ")
    List<Object[]> getMenuListByAuthority(@Param("compCd") String compCd, @Param("roleCd") String roleCd);

    @Query(value = "" +
            "SELECT menu.compCd" +
            "       ,menu.menuNo" +
            "       ,menu.upperMenuNo" +
            "       ,menu.menuNm" +
            "       ,menu.programFileNm" +
            "       ,menu.menuOrder" +
            "       ,menu.relateImageNm" +
            "       ,menu.relateImagePath" +
            "       ,menu.menuDc" +
            "  FROM Menu menu" +
            " WHERE menu.menuNm LIKE '%' || ( CASE WHEN :menuNm IS NULL THEN '' ELSE :menuNm END ) || '%'" +
            "   AND menu.compCd = :compCd " +
            "   AND menu.menuNo not like '9%'" +
            " ORDER BY menu.menuNo ASC")
    List<Object[]> getMenuListByMenuNm(@Param("compCd") String compCd ,@Param("menuNm") String menuNm);
}

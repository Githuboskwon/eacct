package com.iljin.apiServer.ijeas.mobile.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoBoardRepository extends JpaRepository<MoBoard, MoBoardKey> {
    MoBoard findByIdAndCompCdAndLoginId(Long id, String compCd, String writeId);

    @Query(value = "" +
            "select id, " +
            "comp_cd, " +
            "login_id, " +
            "title, " +
            "content, "+
            "create_date, " +
            "hit " +
            "from tb_board_m " +
            "where (login_id LIKE CONCAT('%', :keyWord, '%') " +
            "or title LIKE CONCAT('%', :keyWord, '%') " +
            "or content LIKE CONCAT('%', :keyWord, '%')) " +
            "and date_format(create_date, '%Y-%m-%d') >= :searchDtmFr " +
            "and date_format(create_date, '%Y-%m-%d') <= :searchDtmTo " +
            "order by id", nativeQuery = true)
    List<MoBoard> searchBoard(@Param("keyWord") String keyWord, @Param("searchDtmFr") String searchDtmFr, @Param("searchDtmTo") String searchDtmTo);
}

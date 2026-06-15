package com.iljin.apiServer.ijeas.slip;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlipDetailRepository extends JpaRepository<SlipDetail, SlipDetailKey> {

    @Query(value = "SELECT FN_CMN_GET_MNG_NO(:compCd, 'BG', TO_CHAR(SYSDATE, 'YYYYMMDD')) FROM DUAL", nativeQuery = true)
    String getBungaeNo(@Param("compCd") String compCd);

    @Query(value = "SELECT CBO_SP_SLIP_LINE_S.NEXTVAL AS SLIP_LINE_ID FROM DUAL", nativeQuery = true)
    Optional<BigDecimal> getSlipLineId();

    List<SlipDetail> findAllByCompCdAndSlipNo(String compCd, String slipNo);

    // 전표 재사용시, 순서가 맞지 않아 세팅 된 데이터가 다르게 들어가는 문제로 쿼리 수정
    @Query(value = "SELECT * FROM TB_SLIP_DT WHERE COMP_CD = :compCd AND SLIP_NO = :slipNo ORDER BY LPAD(SLIP_SEQ , 10, '0') ", nativeQuery = true)
    List<SlipDetail> findAllByCompCdAndSlipNoOrderBySlipSeq(@Param("compCd") String compCd,@Param("slipNo") String slipNo);

    Boolean existsByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    void deleteAllByCompCdAndSlipNo(String compCd, String slipNo);

}

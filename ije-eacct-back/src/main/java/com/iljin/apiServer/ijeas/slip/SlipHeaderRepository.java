package com.iljin.apiServer.ijeas.slip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface SlipHeaderRepository extends JpaRepository<SlipHeader, SlipHeaderKey> {

    @Query(value = "SELECT APPS.CBO_GL_COMMON_PKG.GET_SLIP_NUMBER(:prefix, TO_NUMBER(:compCd)) FROM DUAL", nativeQuery = true)
    String callGetSlipNumber(@Param("prefix") String prefix, @Param("compCd") String compCd);

    @Query(value = "SELECT CBO.CBO_SP_SLIP_HEADER_S.NEXTVAL AS SLIP_HEADER_ID FROM DUAL", nativeQuery = true)
    BigDecimal getSlipHeaderId();

    @Query(value = "SELECT CBO_GL_COMMON_PKG.GET_CCID( p_sob_id => :pSobId, p_segment1 => :pSegment1, "
        + " p_segment2 => :pSegment2, p_segment3 => :pSegment3, p_segment4 => :pSegment4, p_segment5 => :pSegment5, p_segment6 => :pSegment6, "
        + "p_segment7 => :pSegment7, p_segment8 => :pSegment8, p_segment9 => :pSegment9 ,p_segment10 => :pSegment10) AS CODE_COMBINATION_ID FROM DUAL", nativeQuery = true)
    BigDecimal callPkgGetCcid(@Param("pSobId") BigDecimal pSobId, @Param("pSegment1") String pSegment1, @Param("pSegment2")String pSegment2, @Param("pSegment3")String pSegment3, @Param("pSegment4")String pSegment4, @Param("pSegment5")String pSegment5,
        @Param("pSegment6")String pSegment6, @Param("pSegment7")String pSegment7, @Param("pSegment8")String pSegment8, @Param("pSegment9")String pSegment9, @Param("pSegment10")String pSegment10);

    Optional<SlipHeader> findBySlipNo(String slipNo);
    Boolean existsBycompCdAndSlipNo(String compCd, String slipNo);

    Optional<SlipHeader> findByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    Optional<SlipHeader> findByCompCdAndSlipNoAndSlipTypeAndSlipHeaderId(String compCd, String slipNo, String SlipType, BigDecimal slipHeaderId);

    Optional<SlipHeader> findByCompCdAndApprovalGroupId(String compCd, BigDecimal approvalGroupId);

    Optional<SlipHeader> findByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    Optional<SlipHeader> findByCompCdAndSlipNo(String compCd, String slipNo);

    Optional<SlipHeader> findByCompCdAndSlipHeaderIdAndApprovalGroupId(String compCd, BigDecimal slipHeaderId, BigDecimal approvalGroupId);

    Optional<SlipHeader> findByCompCdAndSlipNoAndApprovalGroupId(String compCd, String slipNo, BigDecimal approvalGroupId);

    @Transactional
    @Modifying
    @Query("UPDATE FROM SlipHeader b SET b.status = 'SD' WHERE b.compCd = :compCd AND b.slipType = '30' AND SUBSTR(b.postingDt,0,6) = :month AND (:div IS NULL OR b.remark LIKE CONCAT(:div, '%'))")
    void deleteByMonth(@Param("compCd") String compCd, @Param("month") String month, @Param("div") String div);

}

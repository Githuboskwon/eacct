package com.iljin.apiServer.ijeas.es.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface ErpSalesOverseasRepository extends JpaRepository<ErpSalesOverseas, ErpSalesOverseasKey> {

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

    @Transactional
    @Modifying
    @Query("UPDATE ErpSalesOverseas e SET e.remark = :remark , e.certAmendYn = :certAmendYn WHERE e.compCd = :compCd AND e.slipNo = :slipNo")
    void updateRemark(@Param("remark") String remark, @Param("certAmendYn") String certAmendYn,  @Param("compCd") String compCd, @Param("slipNo") String slipNo);

    @Transactional
    @Modifying
    @Query("DELETE FROM ErpSalesOverseas b WHERE b.compCd = :compCd AND TO_CHAR(b.glDt, 'YYYY-MM') = :month AND (:div IS NULL OR b.coaSegment5 LIKE CONCAT(:div, '%'))")
    void deleteByMonth(@Param("compCd") String compCd, @Param("month") String month, @Param("div") String div);
}

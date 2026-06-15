package com.iljin.apiServer.ijeas.system.termsPayment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsPaymentRepository extends JpaRepository<TermsPayment, TermsPaymentKey> {

    Integer countByCompCdAndTermIdAndDeptCd(String compCd, Integer termId, String deptCd);

    @Modifying
    @Query("UPDATE TermsPayment a " +
            "SET " +
            "a.eaEnabledFlag = :eaEnabledFlag, " +
            "a.ptEnabledFlag = :ptEnabledFlag, " +
            "a.dtChangeFlag = :dtChangeFlag " +
            "WHERE a.compCd = :compCd " +
            "AND a.termId = :termId " +
            "AND a.deptCd = :deptCd")
    void updateSetEaEnabledFlagAndPtEnabledFlagAndDtChangeFlagByCompCdAndTermIdAndDeptCd
            (@Param("eaEnabledFlag") String eaEnabledFlag, @Param("ptEnabledFlag") String ptEnabledFlag, @Param("dtChangeFlag") String dtChangeFlag,
             @Param("compCd") String compCd, @Param("termId") Integer termId, @Param("deptCd")String deptCd);

    void deleteByCompCdAndTermIdAndDeptCd(String compCd, Integer termId, String deptCd);
}

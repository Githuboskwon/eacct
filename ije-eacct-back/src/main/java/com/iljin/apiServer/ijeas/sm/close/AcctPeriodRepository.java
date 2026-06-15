package com.iljin.apiServer.ijeas.sm.close;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcctPeriodRepository extends JpaRepository<AcctPeriod, AcctPeriodKey> {
    List<AcctPeriod> findByCompCdAndClosYmOrderByBaCd(String compCd, String closeYm);

    Optional<AcctPeriod> findByCompCdAndBaCdAndClosYm(String compCd, String baCd, String closeYm);

    Optional<AcctPeriod> findByCompCdAndClosYm(String compCd, String closeYm);

    Optional<AcctPeriod> findTopByClosStatCdOrderByClosYmAsc(String closStatCd);

    Optional<AcctPeriod> findTopByClosStatCdAndMainCloseYnOrderByClosYmAsc(String closStatCd, String mainCloseYn);

    Optional<AcctPeriod> findTopByClosStatCdAndMainCloseYnOrderByClosYmDesc(String closStatCd, String mainCloseYn);

    @Query(value = "SELECT CBO_GL_COMMON_PKG.GET_PERIOD_INFO(TO_DATE(:postingDt, 'YYYYMMDD'), :ledgerId, :ttypeInterfaceModule,'CS') AS OPEN_YN FROM DUAL", nativeQuery = true)
    String checkErpAcctPeriod(@Param("postingDt")String postingDt, @Param("ledgerId") BigDecimal ledgerId, @Param("ttypeInterfaceModule") String ttypeInterfaceModule);
}

package com.iljin.apiServer.ijeas.es.fund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface SpTrFundDtRepository extends JpaRepository<SpTrFundDt, SpTrFundDtKey> {

    @Query(value = "SELECT SEQ_TB_TR_FUND_DT.NEXTVAL FROM DUAL", nativeQuery = true)
    BigDecimal getSlipSeq();

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

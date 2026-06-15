package com.iljin.apiServer.ijeas.es.fund;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpTrFundHdRepository extends JpaRepository<SpTrFundHd, SpTrFundHdKey> {

    Boolean existsByCompCdAndSlipNo(String compCd, String slipNo);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

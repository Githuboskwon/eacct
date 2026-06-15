package com.iljin.apiServer.ijeas.bond;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BondHeaderRepository extends JpaRepository<BondHeader, BondHeaderKey> {
    Boolean existsByCompCdAndRefNo(String compCd, String refNo);

    Boolean existsByCompCdAndSlipNo(String compCd, String slipNo);

    Optional<BondHeader> findByCompCdAndSlipNo(String compCd, String slipNo);

    void deleteByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

    Optional<BondHeader> findByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

}

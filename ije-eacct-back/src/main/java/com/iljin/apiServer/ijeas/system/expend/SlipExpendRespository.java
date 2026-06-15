package com.iljin.apiServer.ijeas.system.expend;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlipExpendRespository extends JpaRepository<SlipExpend, SlipExpendKey> {

    Boolean existsByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    Optional<SlipExpend> findByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

}

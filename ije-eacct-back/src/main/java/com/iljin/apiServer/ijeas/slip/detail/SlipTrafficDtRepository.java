package com.iljin.apiServer.ijeas.slip.detail;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface SlipTrafficDtRepository extends JpaRepository<SlipTrafficDt, SlipTrafficDtKey> {

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

    Optional<SlipTrafficDt> findByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

    List<SlipTrafficDt> findAllByCompCdAndSlipHeaderId(String compCe, BigDecimal slipHeaderId);
}

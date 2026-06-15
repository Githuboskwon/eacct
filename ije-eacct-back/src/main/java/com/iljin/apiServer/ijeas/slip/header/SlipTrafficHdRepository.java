package com.iljin.apiServer.ijeas.slip.header;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlipTrafficHdRepository extends JpaRepository<SlipTrafficHd, SlipTrafficHdKey> {

    void deleteByCompCdAndSlipHeaderId(String compCd, BigDecimal slipHeaderId);

}

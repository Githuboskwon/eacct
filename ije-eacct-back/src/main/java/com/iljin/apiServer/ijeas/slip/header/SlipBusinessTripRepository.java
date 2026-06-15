package com.iljin.apiServer.ijeas.slip.header;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlipBusinessTripRepository extends JpaRepository<SlipBusinessTrip, SlipBusinessTripKey> {

    List<SlipBusinessTrip> findAllByCompCdAndMasterSlipNoAndMasterSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    Boolean existsByCompCdAndMasterSlipNoAndMasterSlipHeaderIdAndSeq(String compCd, String slipNo, BigDecimal slipHeaderId, Integer seq);

    void deleteByCompCdAndMasterSlipHeaderId(String compCd, BigDecimal MasterSlipHeaderId);

    Boolean existsByCompCdAndMasterSlipNoAndMasterSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    void deleteAllByCompCdAndMasterSlipNoAndMasterSlipHeaderId(String compCd, String slipnNo, BigDecimal slipHeaderId);

}

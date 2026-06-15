package com.iljin.apiServer.ijeas.es.item;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ApPaymentsItemRepository extends JpaRepository<ApPaymentsItem, ApPaymentsItemKey> {

    @Modifying
    void deleteByCompCdAndCheckId(String compCd, BigDecimal checkId);

    List<ApPaymentsItem> findByCompCdAndCheckId(String compCd, BigDecimal checkId);

    @Modifying
    List<ApPaymentsItem> deleteByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);
}

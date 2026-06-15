package com.iljin.apiServer.ijeas.es.bulk;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ApPaymentsHdRepository extends JpaRepository<ApPaymentsHd, ApPaymentsHdKey> {

//    @Query(value = "SELECT FN_CMN_GET_MNG_NO('APP', TO_CHAR(SYSDATE,'YYMMDD')) FROM DUAL", nativeQuery = true)
//    String getErpSlipNo();
//
//    @Query(value = "SELECT CBO.CBO_SP_SLIP_HEADER_S.NEXTVAL AS SLIP_HEADER_ID FROM DUAL", nativeQuery = true)
//    Long getErpSlipHeaderId();
//
//    @Modifying
//    @Query(value = "UPDATE CBO.CBO_SP_AP_IF_DETAIL_T CSD SET CSD.CANCELLED_FLAG = 'Y' "
//        + ", CSD.CANCELLED_DATE = SYSDATE WHERE CSD.ESLIP_TRANSFER_BATCH_ID = :eSlipTransferBatchId AND CSD.TRANSFER_TYPE = 'S'", nativeQuery = true)
//    void cancelPullErpItemSlip(@Param("eSlipTransferBatchId") String eSlipTransferBatchId);

    @Modifying
    void deleteByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

package com.iljin.apiServer.ijeas.es.item;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApPaymentsDtRepository extends JpaRepository<ApPaymentsDt, ApPaymentsDtKey> {

    @Query(value = "SELECT FN_CMN_GET_MNG_NO(:compCd, 'APP', TO_CHAR(SYSDATE, 'YYYYMMDD')) FROM DUAL", nativeQuery = true)
    String getErpSlipNo(@Param("compCd") String compCd);

    @Modifying
    @Query(value = "UPDATE CBO.CBO_SP_AP_IF_BATCH_T CSB SET CSB.CANCELLED_FLAG = 'Y' "
            + ", CSB.CANCELLED_DATE = SYSDATE WHERE CSB.ESLIP_TRANSFER_BATCH_ID = :eSlipTransferBatchId AND CSB.TRANSFER_TYPE = 'S'", nativeQuery = true)
    void cancelPullErpBulkSlip(@Param("eSlipTransferBatchId") String eSlipTransferBatchId);

    @Modifying
    @Query(value = "UPDATE CBO.CBO_SP_AP_IF_DETAIL_T CSD SET CSD.CANCELLED_FLAG = 'Y' "
        + ", CSD.CANCELLED_DATE = SYSDATE WHERE CSD.ESLIP_TRANSFER_ID = :EslipTransferId AND CSD.TRANSFER_TYPE = :transferType", nativeQuery = true)
    void cancelPullErpItemSlip(@Param("EslipTransferId") BigDecimal EslipTransferId, @Param("transferType") String transferType);

    @Modifying
    @Query(value = "UPDATE CBO.CBO_SP_AP_IF_DETAIL_T CSD SET CSD.CANCELLED_FLAG = 'Y' "
        + ", CSD.CANCELLED_DATE = SYSDATE WHERE CSD.ESLIP_TRANSFER_BATCH_ID = :EslipTransferBatchId AND CSD.TRANSFER_TYPE = :transferType", nativeQuery = true)
    void cancelPullErpItemSlipByEslipTransterBatchId(@Param("EslipTransferBatchId") String EslipTransferBatchId, @Param("transferType") String transferType);

    List<ApPaymentsDtDto> findByCompCdAndEslipTransferBatchId(String compCd, String eSlipTransferBatchId);


    @Modifying
    void deleteByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);
}

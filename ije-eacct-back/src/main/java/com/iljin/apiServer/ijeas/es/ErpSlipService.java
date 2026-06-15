package com.iljin.apiServer.ijeas.es;

import com.iljin.apiServer.ijeas.es.bulk.ErpBondSlipDto;
import com.iljin.apiServer.ijeas.es.bulk.ErpBulkSlipDto;
import com.iljin.apiServer.ijeas.es.collection.ErpCollectionSlipDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.fund.ErpFundSlipDto;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.iljin.apiServer.ijeas.es.item.ErpItemSlipDto;
import com.iljin.apiServer.ijeas.es.sale.ErpSalesOverseasDto;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

public interface ErpSlipService {

    @Modifying
    @Transactional
    String pullErpItemSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpBulkSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpBondSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpFundSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpClctSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpSalesSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpForeignSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String pullErpExportSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpItemSlipDto> getErpItemSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpGlSlipDto> getErpGlSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    ErpItemSlipDto getErpItemSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpBulkSlipDto> getErpBulkSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    ErpBulkSlipDto getErpBulkSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpBondSlipDto> getErpBondSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpFundSlipDto> getErpFundSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    ErpFundSlipDto getErpFundSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpCollectionSlipDto> getErpCollectionSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    ErpCollectionSlipDto getErpCollectionSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    List<ErpSalesOverseasDto> getErpSalesSlipList(ErpSlipRequestDto erpSlipRequestDto);

    @Transactional(readOnly = true)
    ErpGlSlipDto getErpGlSlipDetail(ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    String deleteErpSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpBulkSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpGlSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpBondSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpFundSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpClctSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String deleteErpSaleAndFrgnSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

    @Modifying
    @Transactional
    String pullErpGlSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveExportSlip(List<ErpSlipRequestDto> erpSlipRequestDto);

    @Modifying
    @Transactional
    String deleteErpExportSlip(List<ErpSlipRequestDto> erpSlipRequestDtos);

}

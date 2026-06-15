package com.iljin.apiServer.ijeas.slip;

import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchy;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchyDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface SlipService {
    BigDecimal getSlipHistoryCount(SlipHistoryDto slipHistoryDto);

    List<SlipHistoryDto> getSlipHistory(SlipHistoryDto slipHistoryDto);

    List<SlipHistoryDto> getSlipHistoryExcel(SlipHistoryDto slipHistoryDto);

    List<SlipDetailDto> getSlipTrafficDetail(String slipNo);

    List<SlipHeaderDto> getAirlineSlip(SlipHeaderDto slipHeaderDto);

    @Modifying
    @Transactional
    String saveSlip(SlipDto slipDto);

    @Modifying
    @Transactional
    Object deleteSlip(List<SlipDto> slipDto) throws Exception;

    SlipHeaderDto getSlipHeader(String slipNo);

    List<SlipDetailDto> getSlipDetail(String slipNo);

    @Modifying
    @Transactional
    Map<String, String> changeAppr(List<SlipHistoryDto> slipHistoryDto) throws Exception;

    @Transactional
    ResponseEntity<String> reuseSlip(SlipDto slipDto);

    List<SlipDto> getTaxLocationCode(String deptCd);

    List<CoaHierarchyDto> getControlledAccount();
}

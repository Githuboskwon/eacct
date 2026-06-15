package com.iljin.apiServer.ijeas.slip;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeader;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import java.math.BigDecimal;
import java.util.List;

public interface SlipRepositoryCustom {
    BigDecimal getSlipHistoryCount(SlipHistoryDto slipHistoryDto);

    //List<SlipHistoryDto> getSlipHistory(SlipHistoryDto slipHistoryDto);

    List<SlipHeaderDto> getSlipHeader(String slipNo);

    List<SlipDetailDto> getSlipDetail(String slipNo);

    //전표 라인 조회
    List<SlipDetailDto> getSlipTrafficDetail(String slipNo);

    List<SlipHeaderDto> getAirlineSlip(SlipHeaderDto slipHeaderDto);

    List<SlipHeaderDto> getSlipInfo(String compCd, String slipNo, BigDecimal slipHeaderId);

    List<SlipHistoryDto> getCoaHierarchyCnt(BigDecimal slipHeaderId, Integer orgId, BigDecimal ledgerId, String postingDt);

    void callBudgetCheckProcedure(BigDecimal slipHeaderId);

    void callBudgetResetProcedure(ErpSlipHeader erpSlipHeader);

    List<SlipDto> getTaxLocationCode(String deptCd);
}

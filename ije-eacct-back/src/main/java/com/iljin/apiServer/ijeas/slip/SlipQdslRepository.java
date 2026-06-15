package com.iljin.apiServer.ijeas.slip;

import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchy;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchyDto;

import java.math.BigDecimal;
import java.util.List;

public interface SlipQdslRepository {
    List<SlipHistoryDto> getSlipHistory(SlipHistoryDto slipHistoryDto);

    List<SlipHistoryDto> getSlipHistoryExcel(SlipHistoryDto slipHistoryDto);

    List<SlipHeaderDto> getSlipStatusGroup(String compCd, BigDecimal approvalGroupId);

    List<SlipHeaderDto> getSlipStatus(String compCd, BigDecimal approvalGroupId);

    List<BondHeaderDto> getBondSlipHeader(String compCd, String slipNo);

    List<CoaHierarchyDto> getControlledAccount();
}

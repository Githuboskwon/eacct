package com.iljin.apiServer.ijeas.system.expend;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.system.code.CodeDto;

import java.util.List;

public interface ExpendQdslRepository {

    List<ExpendDto> getExpendList(ExpendDto expendDto);

    List<CodeDto> getExpendCdList(String value);

    List<SlipExpendDto> getHrExpendList(SlipExpendDto slipExpendDto);

    List<ExpendDto> getSlipExpendList(ErpSlipRequestDto erpSlipRequestDto);

    SlipExpendDto getSlipExpend(String slipNo);
}

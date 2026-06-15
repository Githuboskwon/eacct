package com.iljin.apiServer.ijeas.bond;

import com.iljin.apiServer.ijeas.es.erpViews.ErpPaProjectsAllDto;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import java.math.BigDecimal;
import java.util.List;

public interface BondHeaderQdslRepository {

    Boolean existsByTypeAndStatusNotIn(SlipDto slipDto);

    List<BondHeader> findAllByTypeAndStatusNotIn(SlipDto slipDto);

    Boolean existsByTypeAndStatusIn(String compCd, String refNo, BigDecimal slipHeaderId);

    List<BondHeaderDto> findAllByCompCdAndSlipNoAndSlipHeaderId(String compCd, String slipNo, BigDecimal slipHeaderId);

    Boolean existsByLocalComplete(String compCd, String refNo, BigDecimal slipHeaderId);

    List<ErpPaProjectsAllDto> getProjectList(String searchWord);
}

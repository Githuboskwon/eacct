package com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis;

import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PjtPerfAnalysisServiceImpl implements PjtPerfAnalysisService {

    private final PjtPerfAnalysisRepositoryCustom pjtPerfAnalysisRepositoryCustom;

    private Util util;

    @Override
    public List<PjtPerfAnalysisDto> getPerfAnalysisList(PjtPerfAnalysisDto pjtPerfAnalysisDto) {
        return pjtPerfAnalysisRepositoryCustom.getPerfAnalysisList(pjtPerfAnalysisDto);
    }

    @Override
    public List<PjtPerfAnalysisDto> getProfitSalesInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {
        return pjtPerfAnalysisRepositoryCustom.getProfitSalesInfo(pjtPerfAnalysisDto);
    }

    @Override
    public List<PjtPerfAnalysisDto> getClaimInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {
        return pjtPerfAnalysisRepositoryCustom.getClaimInfo(pjtPerfAnalysisDto);
    }

    @Override
    public List<PjtPerfAnalysisDto> getCollectionInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto) {
        return pjtPerfAnalysisRepositoryCustom.getCollectionInfo(pjtPerfAnalysisDto);
    }

    @Override
    public List<PjtPerfAnalysisDto> getTotalAmt(PjtPerfAnalysisDto pjtPerfAnalysisDto) {
        return pjtPerfAnalysisRepositoryCustom.getTotalAmt(pjtPerfAnalysisDto);
    }
}

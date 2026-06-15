package com.iljin.apiServer.ijeas.ims.pjtPerfAnalysis;

import java.util.List;

public interface PjtPerfAnalysisRepositoryCustom {

    List<PjtPerfAnalysisDto> getPerfAnalysisList(PjtPerfAnalysisDto pjtPerfAnalysisDto);

    List<PjtPerfAnalysisDto> getProfitSalesInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto);

    List<PjtPerfAnalysisDto> getClaimInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto);

    List<PjtPerfAnalysisDto> getCollectionInfo(PjtPerfAnalysisDto pjtPerfAnalysisDto);

    List<PjtPerfAnalysisDto> getTotalAmt(PjtPerfAnalysisDto pjtPerfAnalysisDto);

}

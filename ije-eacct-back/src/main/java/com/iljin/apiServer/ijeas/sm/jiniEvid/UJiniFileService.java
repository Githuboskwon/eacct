package com.iljin.apiServer.ijeas.sm.jiniEvid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UJiniFileService {

    @Transactional(readOnly = true)
    Map<String, Object> getFileList(String docMngNo);

    @Modifying
    @Transactional
    List<UJiniFile> uploadEvidenceFiles(UJiniFileDto uJiniFileDto);

    @Modifying
    @Transactional
    String deleteEvidenceFiles(UJiniFileDto uJiniFileDto);

    UJiniFile getFileInfo(String compCd, BigDecimal id);
}

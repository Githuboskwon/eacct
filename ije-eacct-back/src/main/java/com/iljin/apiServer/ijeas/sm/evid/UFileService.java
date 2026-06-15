package com.iljin.apiServer.ijeas.sm.evid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UFileService {

    @Transactional(readOnly = true)
    Map<String, Object> getFileList(String docMngNo);

    @Modifying
    @Transactional
    List<UFile> uploadEvidenceFiles(UFileDto uFileDto);

    @Modifying
    @Transactional
    List<UFile> uploadEvidenceFilesMobile(UFileDto uFileDto);

    @Modifying
    @Transactional
    List<UFile> uploadEvidenceFilesBundle(UFileDto uFileDto);

    @Modifying
    @Transactional
    String deleteEvidenceFiles(UFileDto uFileDto);

    @Modifying
    @Transactional
    String removeMobileEvidenceFiles(UFileDto uFileDto);

    @Modifying
    @Transactional
    Optional<UFile> mobileCheckFile(UFileDto uFileDto);

    UFile getFileInfo(String compCd, BigDecimal id);
}

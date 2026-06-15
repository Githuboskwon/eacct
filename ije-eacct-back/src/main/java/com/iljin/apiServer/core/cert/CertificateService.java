package com.iljin.apiServer.core.cert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CertificateService {
    List<CertFileDto> getCertFiles(String searchDate);

    @Modifying
    @Transactional
    ResponseEntity<String> saveCertificate(List<CertFileDto> certFileDtos) throws Exception;

    ResponseEntity<String> deleteCertificate(String fileId);

    List<CertFileDto> getCertFilesWithDecrypt(String searchDate);
}

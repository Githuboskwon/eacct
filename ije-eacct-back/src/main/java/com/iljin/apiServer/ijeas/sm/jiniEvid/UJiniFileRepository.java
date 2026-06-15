package com.iljin.apiServer.ijeas.sm.jiniEvid;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UJiniFileRepository extends JpaRepository<UJiniFile, BigDecimal> {

    List<UJiniFile> findByCompCdAndAttribute3AndRegIdAndAttribute4NotOrderByRegDtmDesc(String compCd, String attr3, String loginId, String attr4);

    List<UJiniFile> findByCompCdAndDocumentHIdOrderByRegDtmDesc(String compCd, String docMngNo);

    Optional<UJiniFile> findTopByCompCdAndDocumentHIdOrderBySeqDesc(String compCd, String docMngNo);

    List<UJiniFile> findByCompCdAndDocumentHId(String compCd, String documentHId);

    List<UJiniFile> findByCompCdAndId(String compCd, BigDecimal id);

}

package com.iljin.apiServer.ijeas.sm.evid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UFileRepository extends JpaRepository<UFile, BigDecimal> {

    List<UFile> findByCompCdAndAttribute3AndRegIdAndAttribute4NotOrderByRegDtmDesc(String compCd, String attr3, String loginId, String attr4);

    List<UFile> findByCompCdAndDocumentHIdOrderByRegDtmDesc(String compCd, String docMngNo);

    Optional<UFile> findTopByCompCdAndDocumentHIdOrderBySeqDesc(String compCd, String docMngNo);

    List<UFile> findByCompCdAndDocumentHId(String compCd, String documentHId);

    List<UFile> findByCompCdAndId(String compCd, BigDecimal id);

}

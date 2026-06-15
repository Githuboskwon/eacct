package com.iljin.apiServer.ijeas.sm.jini;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JiniRepository extends JpaRepository<Jini, BigDecimal> {

    void deleteByCompCdAndDocumentId(String compCd, String documentId);

    List<Jini> findAllByCompCdAndDocumentId(String compCd, String documentId);

}

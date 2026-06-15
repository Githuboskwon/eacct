package com.iljin.apiServer.ijeas.es.collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface SpTrCollectionDtRepository extends JpaRepository<SpTrCollectionDt, SpTrCollectionDtKey> {

    @Query(value = "SELECT SEQ_TB_TR_COLLECTION_DT.NEXTVAL FROM DUAL", nativeQuery = true)
    BigDecimal getSlipSeq();

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

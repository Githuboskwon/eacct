package com.iljin.apiServer.ijeas.es.collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpTrCollectionHdRepository extends JpaRepository<SpTrCollectionHd, SpTrCollectionHdKey> {

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

package com.iljin.apiServer.ijeas.es.gl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GlHeaderRepository extends JpaRepository<GlHeader, GlHeaderKey> {

    Boolean existsByCompCdAndSlipNo(String compCd, String slipNo);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);


}

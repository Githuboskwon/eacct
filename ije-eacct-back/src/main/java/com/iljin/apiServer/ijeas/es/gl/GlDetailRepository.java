package com.iljin.apiServer.ijeas.es.gl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlDetailRepository extends JpaRepository<GlDetail, GlDetailKey> {

    List<GlDetail> findAllByCompCdAndSlipNo(String compCd, String slipNo);

    Boolean existsByCompCdAndSlipNo(String compCd, String slipNo);

    void deleteByCompCdAndSlipNo(String compCd, String slipNo);

}

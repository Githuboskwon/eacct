package com.iljin.apiServer.ijeas.system.oil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OilPriceRepository extends JpaRepository<OilPrice, OilPriceKey> {

    Optional<OilPrice> findByCompCdAndBaseYmAndOilKindCd(String compCd, String baseYm, String oilKindCd);
}

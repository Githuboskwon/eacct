package com.iljin.apiServer.ijeas.es.erpViews;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErpGlTermsRepository extends JpaRepository<ErpGlTerms, ErpGlTermsKey> {

    @Override
    Optional<ErpGlTerms> findById(ErpGlTermsKey erpGlTermsKey);
}

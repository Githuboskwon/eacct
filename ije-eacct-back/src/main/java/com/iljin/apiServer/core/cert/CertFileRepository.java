package com.iljin.apiServer.core.cert;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertFileRepository extends JpaRepository<CertFile, Integer> {
    Optional<CertFile> findByFileId(Integer id);
}

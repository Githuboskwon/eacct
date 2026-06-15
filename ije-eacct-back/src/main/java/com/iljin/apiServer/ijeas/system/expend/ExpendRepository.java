package com.iljin.apiServer.ijeas.system.expend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpendRepository extends JpaRepository<Expend, ExpendKey> {


    @Override
    Optional<Expend> findById(ExpendKey expendKey);

    Optional<Expend> findByExpendCdAndStartDate(String expendCd, String startDate);
}

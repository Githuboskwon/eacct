package com.iljin.apiServer.ijeas.bond;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BondMstRepository extends JpaRepository<BondMst, BondMstKey> {

    Optional<BondMst> findByCompCdAndRefNo(String compCd, String refNo);

}

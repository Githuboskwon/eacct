package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface RaCustomerTrxAllRepository extends JpaRepository<RaCustomerTrxAll, RaCustomerTrxAllKey> {
    @Transactional
    @Modifying
    @Query("update RaCustomerTrxAll r set r.globalAttribute21 = '' where r.customerTrxId = ?1")
    int updateGlobalAttribute21ByCustomerTrxId(@NonNull BigDecimal customerTrxId);

    @Transactional
    @Modifying
    @Query("update RaCustomerTrxAll r set r.globalAttribute4 = ?1, r.globalAttribute9 = ?2, r.globalAttribute10 = ?3, r.globalAttribute21 = ?4 " +
            "where r.customerTrxId = ?5")
    int updateGlobalAttribute4AndGlobalAttribute9AndGlobalAttribute10AndGlobalAttribute21ByCustomerTrxId(String globalAttribute4, String globalAttribute9, String globalAttribute10, String globalAttribute21, BigDecimal customerTrxId);

    @Transactional
    @Modifying
    @Query("update RaCustomerTrxAll r set r.globalAttribute22 = ?1, r.globalAttribute23 = ?2 where r.customerTrxId = ?3")
    int updateGlobalAttribute22AndGlobalAttribute23ByCustomerTrxId(String globalAttribute22, String globalAttribute23, BigDecimal customerTrxId);


}

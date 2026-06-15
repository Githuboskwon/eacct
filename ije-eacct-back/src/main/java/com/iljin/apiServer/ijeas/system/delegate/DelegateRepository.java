package com.iljin.apiServer.ijeas.system.delegate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DelegateRepository extends JpaRepository<Delegate, DelegateKey> {

    Integer countByGiveUserIdAndReceiveUserId(String giverUserId, String receiverUserId);

    @Query("SELECT COUNT(d) FROM Delegate d " +
            "WHERE d.giveUserId = :giveUserId " +
            "AND d.receiveUserId = :receiveUserId " +
            "AND d.delegateStatCd = '1' " +
            "AND NOT ( :startDate >= d.endDate OR :endDate <= d.startDate )" )
    Integer countByGiveUserIdAndReceiveUserIdAndDateBetween(
            @Param("giveUserId") String giveUserId,
            @Param("receiveUserId") String receiveUserId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

@Query("SELECT COUNT(d) FROM Delegate d " +
        "WHERE d.giveUserId = :giveUserId " +
        "AND d.receiveUserId = :receiveUserId " +
        "AND d.delegateStatCd = '1' " +
        "AND d.delegateSeq <> :delegateSeq " +
        "AND NOT ( :startDate >= d.endDate OR :endDate <= d.startDate )" )
    Integer countByGiveUserIdAndReceiveUserIdAndDateBetweenAndDelegateSeqNot(
            @Param("giveUserId") String giveUserId,
            @Param("receiveUserId") String receiveUserId,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("delegateSeq") BigDecimal delegateSeq
            );

    Optional<Delegate> findByGiveUserIdAndReceiveUserIdAndDelegateSeq(String giverUserId, String receiverUserId, BigDecimal delegateSeq);

}

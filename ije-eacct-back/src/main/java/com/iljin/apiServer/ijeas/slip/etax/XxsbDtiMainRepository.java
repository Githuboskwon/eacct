package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface XxsbDtiMainRepository extends JpaRepository<XxsbDtiMain, XxsbDtiMainKey> {
    @Transactional
    @Modifying
    @Query("update XxsbDtiMain x set x.interfaceBatchId = ?1 where x.conversationId = ?2")
    int updateInterfaceBatchIdByConversationId(BigDecimal interfaceBatchId, String conversationId);

}

package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface XxsbDtiInvoiceRepository extends JpaRepository<XxsbDtiInvoice, XxsbDtiInvoiceKey> {
    long deleteByConversationId(String conversationId);

    @Modifying
    void deleteByInvoiceNumAndApproveId(String invoiceId, String approveId);

    Integer countByInvoiceNumAndApproveId(String invoiceId, String approveId);

}

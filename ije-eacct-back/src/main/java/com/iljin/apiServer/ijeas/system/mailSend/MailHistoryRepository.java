package com.iljin.apiServer.ijeas.system.mailSend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailHistoryRepository extends JpaRepository<MailHistory, MailHistoryKey> {

}

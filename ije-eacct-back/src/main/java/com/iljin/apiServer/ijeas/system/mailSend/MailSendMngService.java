package com.iljin.apiServer.ijeas.system.mailSend;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MailSendMngService {

    @Transactional(readOnly = true)
    List<MailHistoryDto> getUnApprUserList(MailHistoryDto mailHistoryDto);

    @Transactional(readOnly = true)
    List<MailHistoryDto> getUnTrUserList(MailHistoryDto mailHistoryDto);

    @Modifying
    @Transactional
    String sendMail(MailHistoryDto mailHistoryDto);

}

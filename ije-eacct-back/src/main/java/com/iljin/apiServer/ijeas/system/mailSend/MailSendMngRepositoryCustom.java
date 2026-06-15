package com.iljin.apiServer.ijeas.system.mailSend;

import java.util.List;

public interface MailSendMngRepositoryCustom{

    List<MailHistoryDto> getUnApprUserList(MailHistoryDto mailHistoryDto);

    List<MailHistoryDto> getUnTrUserList(MailHistoryDto mailHistoryDto);

}

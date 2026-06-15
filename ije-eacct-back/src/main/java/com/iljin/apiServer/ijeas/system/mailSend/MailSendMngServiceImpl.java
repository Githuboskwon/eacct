package com.iljin.apiServer.ijeas.system.mailSend;

import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.mail.MailDto;
import com.iljin.apiServer.core.mail.MailService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailSendMngServiceImpl implements MailSendMngService{

    private final MailSendMngQdslRepository mailSendMngQdslRepository;
    private final MailSendMngRepositoryCustom mailSendMngRepositoryCustom;
    private final MailService mailService;

    private final MailHistoryRepository mailHistoryRepository;

    @Override
    public List<MailHistoryDto> getUnApprUserList(MailHistoryDto mailHistoryDto) {
        return mailSendMngRepositoryCustom.getUnApprUserList(mailHistoryDto);
    }

    @Override
    public List<MailHistoryDto> getUnTrUserList(MailHistoryDto mailHistoryDto) {
        return mailSendMngRepositoryCustom.getUnTrUserList(mailHistoryDto);

    }

    @Override
    public String sendMail(MailHistoryDto mailHistoryDto) {
        List<MailHistoryDto> sendList = mailHistoryDto.getSendList();
        String sendMailId = "system@iljin.co.kr";
        for(MailHistoryDto dto : sendList) {
            if(hasText(dto.getEmpNo())) {
                String receiveUserMail =  dto.getEmpNo() + "@iljin.co.kr";
                MailDto mailDto = MailDto.builder()
                    .from(sendMailId)
                    .to(receiveUserMail)
                    .subject(mailHistoryDto.getSubject())
                    .text(mailHistoryDto.getText())
                    .build();
                mailService.sendSimpleMessage(mailDto);

                MailHistory mailHistory = MailHistory.builder()
                    .compCd(mailHistoryDto.getCompCd())
                    .mailSendDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .mailSeq(new BigDecimal(1))
                    .mailTypeCd(mailHistoryDto.getMailTypeCd())
                    .receiveUserCd(dto.getEmpNo())
                    .build();
                mailHistoryRepository.save(mailHistory);
            }
        }
        return "메일이 발송되었습니다.";
    }

}

package com.iljin.apiServer.ijeas.system.mailSend;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/mailSend")
public class MailSendMngController {

    private final MailSendMngService mailSendMngService;

    @PostMapping("/unAppr")
    public ResponseEntity<List<MailHistoryDto>> getUnApprUserList(@RequestBody MailHistoryDto mailHistoryDto) {
        return new ResponseEntity<>(mailSendMngService.getUnApprUserList(mailHistoryDto), HttpStatus.OK);
    }

    @PostMapping("/unTr")
    public ResponseEntity<List<MailHistoryDto>> getUnTrUserList(@RequestBody MailHistoryDto mailHistoryDto) {
        return new ResponseEntity<>(mailSendMngService.getUnTrUserList(mailHistoryDto), HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailHistoryDto mailHistoryDto) {
        return new ResponseEntity<>(mailSendMngService.sendMail(mailHistoryDto), HttpStatus.OK);
    }
}

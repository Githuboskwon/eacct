package com.iljin.apiServer.ijeas.system.confirm;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConfirmService {

    List<ConfirmDto> getConfirmList(ConfirmDto confirmDto);

    ResponseEntity<String> deleteConfirmSeq(ConfirmDto confirmDto);

    ResponseEntity<String> saveConfirmSeq(ConfirmDto confirm);

    ResponseEntity<String> updateConfirmSeq(ConfirmDto confirm);

}

package com.iljin.apiServer.ijeas.system.delegate;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DelegateService {

    List<DelegateDto> getDelegateList(DelegateDto delegate);

    ResponseEntity<String> deleteDelegate(DelegateDto delegate);

    ResponseEntity<String> saveDelegate(DelegateDto delegate);

    ResponseEntity<String> updateDelegate(DelegateDto delegate);

}

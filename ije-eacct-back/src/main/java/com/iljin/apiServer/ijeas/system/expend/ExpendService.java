package com.iljin.apiServer.ijeas.system.expend;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpendService {
    List<ExpendDto> getExpendList(ExpendDto expendDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveExpend(List<ExpendDto> expendDto);

    @Modifying
    @Transactional
    ResponseEntity<String> deleteExpend(ExpendDto expendDto);
}

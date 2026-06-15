package com.iljin.apiServer.ijeas.sm.close;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AcctPeriodService {
    List<AcctPeriodDto> getAcctPeriodList(AcctPeriodDto acctPeriodDto);

    List<AcctPeriodDto> getAcctPeriodOpenList(String closStatCd);

    AcctPeriodDto getAcctPeriodOpenCloseDate();

    String getAcctPeriodOpenDate();

    String getMainAcctPeriodFromOpenDate();

    String getMainAcctPeriodToOpenDate();

    @Modifying
    @Transactional
    ResponseEntity<String> saveAcctPeriod(List<AcctPeriodDto> acctPeriodDtos);

    @Modifying
    @Transactional
    ResponseEntity<String> deleteAcctPeriod(AcctPeriodDto acctPeriodDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveAcctPeriodMonthlyAuto(String closeYm);

    @Modifying
    @Transactional
    ResponseEntity<String> openAcctPeriod(String closeYm);

    @Modifying
    @Transactional
    ResponseEntity<String> closeAcctPeriod(String closeYm);
}

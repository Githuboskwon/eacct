package com.iljin.apiServer.core.scheduled;

public interface ScheduleService {

    void runCancelApprDlgt();

    void runCancelCardDlgt();

    void runNoticeMailing();

    void runFinanceAlarming();

}

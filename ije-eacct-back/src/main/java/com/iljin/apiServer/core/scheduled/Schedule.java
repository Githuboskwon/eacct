package com.iljin.apiServer.core.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Schedule {

    private final ScheduleService scheduleService;

    /**
     * 전자결재
     * 위임기간 해제
     * Desc. 매일 새벽 1시 0분 0초 실행
     * */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cronJobCancelApprDlgt() { scheduleService.runCancelApprDlgt();}

    /**
     * 법인카드
     * 위임기간 해제
     * Desc. 매일 새벽 1시 10분 0초 실행
     * */
    @Scheduled(cron = "0 10 1 * * ?")
    public void cronJobCancelCardDlgt() { scheduleService.runCancelCardDlgt();}

    /**
     * scheduled Mailing
     * 알림 메일 발송 (D-2 ~ D-day: 3days)
     * 1) 작성중 전표
     * 2) 법인카드 미처리건
     * Desc. 매일 오전 8시 실행
     * */
    @Scheduled(cron = "0 0 8 * * ?")
    public void cronJobNoticeMailing() { scheduleService.runNoticeMailing();}

    /**
     * scheduled Alarm
     * Desc. 매일 오전 8시 실행
     * */
    @Scheduled(cron = "0 0 8 * * ?")
    public void cronJobFinanceAlarming() { scheduleService.runFinanceAlarming();}
}

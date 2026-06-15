package com.iljin.apiServer.core.scheduled;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Override
    public void runCancelApprDlgt() {

    }

    @Override
    public void runCancelCardDlgt() {

    }

    @Override
    public void runNoticeMailing() {

    }

    @Override
    public void runFinanceAlarming() {

    }
}

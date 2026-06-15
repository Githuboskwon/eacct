package com.iljin.apiServer.ijeas.system.scheduler;

public class SchedulerException extends RuntimeException {
    private static final long serialVersionUID = -3739584235738728261L;

    SchedulerException() {
        super();
    }

    SchedulerException(String msg) {
        super(msg);
    }
}

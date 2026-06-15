package com.iljin.apiServer.ijeas.system.scheduler;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SchedulerKey implements Serializable {

    private static final long serialVersionUID = -9059389120858637821L;

    String compCd;
    String schedulerCd;

    @Builder
    public SchedulerKey(String compCd, String schedulerCd) {
        this.compCd = compCd;
        this.schedulerCd = schedulerCd;
    }
}

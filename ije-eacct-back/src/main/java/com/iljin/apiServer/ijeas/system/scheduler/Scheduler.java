package com.iljin.apiServer.ijeas.system.scheduler;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@IdClass(SchedulerKey.class)
@Table(name = "TB_MST_SCHEDULER")
@Entity
public class Scheduler extends BaseEntity {

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "SCHEDULER_CD", nullable = false)
    String schedulerCd;

    @Column(name = "SCHEDULER_NM")
    String schedulerNm;

    @Column(name = "USE_YN")
    String useYn;

    @Column(name = "PROC_DTM")
    private LocalDateTime procDtm;

    @Column(name = "REMARK")
    String remark;

    @Builder
    public Scheduler(String compCd, String schedulerCd, String schedulerNm, String useYn, String remark, LocalDateTime procDtm) {
        this.compCd = compCd;
        this.schedulerCd = schedulerCd;
        this.schedulerNm = schedulerNm;
        this.useYn = useYn;
        this.remark = remark;
        this.procDtm = procDtm;
    }

    public Scheduler update(SchedulerDto dto) {
        this.schedulerNm = dto.getSchedulerNm();
        this.useYn = dto.getUseYn();
        this.remark = dto.getRemark();
        return this;
    }
}

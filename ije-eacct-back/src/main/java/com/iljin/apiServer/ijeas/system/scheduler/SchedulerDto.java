package com.iljin.apiServer.ijeas.system.scheduler;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class SchedulerDto implements Serializable {
    private static final long serialVersionUID = 3278092005187693816L;

    // 회사코드
    String compCd;

    // 스케줄러 코드
    String schedulerCd;

    // 스케줄러명
    String schedulerNm;

    // 사용여부
    String useYn;

    // 비고
    String remark;

    LocalDateTime procDtm;

    // 수정자
    String chgId;

    // 수정자 이름
    String chgNm;

    // 수정일시
    LocalDateTime chgDtm;

    @QueryProjection
    public SchedulerDto(String compCd, String schedulerCd, String schedulerNm, String useYn, LocalDateTime procDtm, String remark, String chgId, String chgNm, LocalDateTime chgDtm) {
        this.compCd = compCd;
        this.schedulerCd = schedulerCd;
        this.schedulerNm = schedulerNm;
        this.useYn = useYn;
        this.procDtm = procDtm;
        this.remark = remark;
        this.chgId = chgId;
        this.chgNm = chgNm;
        this.chgDtm = chgDtm;
    }
}

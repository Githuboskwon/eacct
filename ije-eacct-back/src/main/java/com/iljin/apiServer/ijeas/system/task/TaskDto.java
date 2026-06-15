package com.iljin.apiServer.ijeas.system.task;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TaskDto implements Serializable {
    private static final long serialVersionUID = -9090975153927479751L;
    String compCd;
    BigDecimal projectId;
    String taskNo;
    String taskNm;
    BigDecimal taskId;
    String taskItemGroup;
    String itemGroupType;

    @QueryProjection
    public TaskDto(String compCd, BigDecimal projectId, String taskNo, String taskNm, BigDecimal taskId, String taskItemGroup, String itemGroupType) {
        this.compCd = compCd;
        this.projectId = projectId;
        this.taskNo = taskNo;
        this.taskNm = taskNm;
        this.taskId = taskId;
        this.taskItemGroup = taskItemGroup;
        this.itemGroupType = itemGroupType;
    }
}

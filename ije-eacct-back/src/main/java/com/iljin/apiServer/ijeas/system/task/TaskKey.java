package com.iljin.apiServer.ijeas.system.task;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TaskKey implements Serializable {
    private static final long serialVersionUID = -7365000636922825878L;

    String compCd;
    BigDecimal projectId;
    BigDecimal taskId;

    public TaskKey(String compCd, BigDecimal projectId, BigDecimal taskId){
        this.compCd = compCd;
        this.projectId = projectId;
        this.taskId = taskId;
    }
}

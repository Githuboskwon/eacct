package com.iljin.apiServer.ijeas.es.erpViews;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpPaProjectsAllDto implements Serializable {

    private static final long serialVersionUID = -6528942100755873335L;

    BigDecimal projectId;
    String projectStatusCode;
    String projectType;
    String name;
    String search;
    @QueryProjection
    public ErpPaProjectsAllDto(BigDecimal projectId, String projectStatusCode, String projectType,
        String name) {
        this.projectId = projectId;
        this.projectStatusCode = projectStatusCode;
        this.projectType = projectType;
        this.name = name;
    }
}

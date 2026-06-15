package com.iljin.apiServer.ijeas.system.pjt;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto implements Serializable {

    private static final long serialVersionUID = 1353957043160389459L;

    String compCd;

    String projectCd;

    String projectNm;

    BigDecimal projectId;

    LocalDateTime startDateActive;

    LocalDateTime endDateActive;

    Integer segmentNum;

    String enabledFlag;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String attribute6;

    String attribute7;

    String attribute8;

    String attribute9;

    String attribute10;

    String attribute11;

    String attribute12;

    String attribute13;

    String attribute14;

    String attribute15;

    Integer personId;
    String postingDate;

    @QueryProjection
    public ProjectDto(String compCd, String projectCd, String projectNm, BigDecimal projectId) {
        this.compCd = compCd;
        this.projectCd = projectCd;
        this.projectNm = projectNm;
        this.projectId = projectId;
    }

    @QueryProjection
    public ProjectDto(String compCd, String projectCd, String projectNm, BigDecimal projectId, Integer personId, String postingDate) {
        this.compCd = compCd;
        this.projectCd = projectCd;
        this.projectNm = projectNm;
        this.projectId = projectId;
        this.personId = personId;
        this.postingDate = postingDate;
    }
}

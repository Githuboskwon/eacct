package com.iljin.apiServer.ijeas.system.pjt;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectKey implements Serializable {

    private static final long serialVersionUID = -3590296339283933122L;

    String compCd;
    String projectCd;

    public ProjectKey(String compCd, String projectCd) {
        this.compCd = compCd;
        this.projectCd = projectCd;
    }
}

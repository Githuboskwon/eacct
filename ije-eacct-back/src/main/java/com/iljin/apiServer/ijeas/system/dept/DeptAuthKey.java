package com.iljin.apiServer.ijeas.system.dept;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeptAuthKey implements Serializable {
    String compCd;
    String deptCd;
    String empNo;

    @Builder
    public DeptAuthKey(String compCd, String deptCd, String empNo) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.empNo = empNo;
    }

    public DeptAuthKey() {

    }
}

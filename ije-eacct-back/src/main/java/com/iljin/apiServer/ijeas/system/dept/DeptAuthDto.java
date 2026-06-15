package com.iljin.apiServer.ijeas.system.dept;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class DeptAuthDto {
    // 회사코드
    private String compCd;

    private String deptCd;

    private String deptNm;

    private String empNo;

    private String remark;

    @QueryProjection
    public DeptAuthDto(String compCd, String deptCd, String deptNm, String remark) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.remark = remark;
    }
}

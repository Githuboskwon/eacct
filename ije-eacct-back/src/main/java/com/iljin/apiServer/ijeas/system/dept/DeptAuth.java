package com.iljin.apiServer.ijeas.system.dept;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "TB_EMP_DEPT_ROLE")
@IdClass(DeptAuthKey.class)
public class DeptAuth extends BaseEntity{
    // 회사코드
    @Id
    @Column(name = "COMP_CD", nullable = false)
    private String compCd;

    @Id
    @Column(name = "DEPT_CD", nullable = false)
    private String deptCd;

    @Id
    @Column(name = "EMP_NO", nullable = false)
    private String empNo;

    @Column(name = "REMARK")
    private String remark;

    @Builder
    public DeptAuth(String compCd, String deptCd, String empNo, String remark) {
        this.compCd = compCd;
        this.deptCd = deptCd;
        this.empNo = empNo;
        this.remark = remark;
    }

    public DeptAuth updateDeptAuth(String deptCd, String remark){
        this.deptCd = deptCd;
        this.remark = remark;
        return this;
    }
}

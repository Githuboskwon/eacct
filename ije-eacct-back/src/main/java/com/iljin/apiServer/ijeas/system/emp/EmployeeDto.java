package com.iljin.apiServer.ijeas.system.emp;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class EmployeeDto {
    String compCd;
    String compNm;

    String empNo;
    String empNm;
    String personId;

    String password;

    String hitDeptCd;

    String hitDeptNm;

    String deptCd;

    String deptNm;

    String cctrCd;

    String cctrNm;

    String pjtId;

    String pjtCd;

    String pjtNm;

    String productCd;

    String productNm;

    String taskNo;

    String taskNm;

    BigDecimal taskId;

    String trAcctCd;

    String trAcctNm;

    String slipTypeCd;

    String slipTypeNm;

    String segment1Cd;

    String segment9Cd;

    String segment10Cd;


    String upperDeptCd;
    String upperDeptNm;

    String jobDutCd;
    String jobDutNm;
    String jobGradeCd;
    String jobGradeNm;

    String jobNm;

    String role;
    String roleNm;

    String serveCd;
    String serveNm;

    boolean enableFlag;

    String email;
    String mobTelNo;

    String deptRoleYn;

    LocalDateTime creationDate;
    LocalDateTime modifiedDate;

    String ledgerId;

    String attribute1;

    String attribute2;

    String attribute3;

    BigDecimal cnt;

    String taxLocationCode;

    String taxLocationName;


    @QueryProjection
    public EmployeeDto(String compCd, String empNo, String empNm, String deptCd, String deptNm, String jobDutCd, String jobDutNm, String jobGradeCd, String jobGradeNm, String serveCd, String serveNm, String deptRoleYn) {
        this.compCd = compCd;
        this.empNo = empNo;
        this.empNm = empNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.jobDutCd = jobDutCd;
        this.jobDutNm = jobDutNm;
        this.jobGradeCd = jobGradeCd;
        this.jobGradeNm = jobGradeNm;
        this.serveCd = serveCd;
        this.serveNm = serveNm;
        this.deptRoleYn = String.valueOf(deptRoleYn);    }

    public EmployeeDto(String compCd, String compNm, String empNo, String empNm, String deptCd, String deptNm, String upperDeptCd, String upperDeptNm, String jobDutCd, String jobDutNm, String jobGradeCd, String jobGradeNm, String role, String roleNm, String serveCd, String serveNm, boolean enableFlag, String email, String mobTelNo) {
        this.compCd = compCd;
        this.compNm = compNm;
        this.empNo = empNo;
        this.empNm = empNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.upperDeptCd = upperDeptCd;
        this.upperDeptNm = upperDeptNm;
        this.jobDutCd = jobDutCd;
        this.jobDutNm = jobDutNm;
        this.jobGradeCd = jobGradeCd;
        this.jobGradeNm = jobGradeNm;
        this.role = role;
        this.roleNm = roleNm;
        this.serveCd = serveCd;
        this.serveNm = serveNm;
        this.enableFlag = enableFlag;
        this.email = email;
        this.mobTelNo = mobTelNo;
    }

    @QueryProjection
    public EmployeeDto(String compCd, String compNm, String empNo, String empNm, String personId
            , String deptCd, String deptNm, String upperDeptCd, String upperDeptNm
            , String jobDutCd, String jobDutNm, String jobGradeCd, String jobGradeNm
            , String role, String roleNm
            , String serveCd, String serveNm, boolean enableFlag
            , String email, String mobTelNo
            , LocalDateTime creationDate, LocalDateTime modifiedDate
            , String ledgerId
            , String productCd, String productNm, String pjtId, String pjtCd, String pjtNm
            , String trAcctCd, String trAcctNm, String slipTypeCd, String slipTypeNm
            , String segment1Cd, String segment9Cd, String segment10Cd
            , String cctrCd, String cctrNm, String taskNo, String taskNm, BigDecimal taskId) {
        this.compCd = compCd;
        this.compNm = compNm;
        this.empNo = empNo;
        this.empNm = empNm;
        this.personId = personId;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.upperDeptCd = upperDeptCd;
        this.upperDeptNm = upperDeptNm;
        this.jobDutCd = jobDutCd;
        this.jobDutNm = jobDutNm;
        this.jobGradeCd = jobGradeCd;
        this.jobGradeNm = jobGradeNm;
        this.role = role;
        this.roleNm = roleNm;
        this.serveCd = serveCd;
        this.serveNm = serveNm;
        this.enableFlag = enableFlag;
        this.email = email;
        this.mobTelNo = mobTelNo;
        this.creationDate = creationDate;
        this.modifiedDate = modifiedDate;
        this.ledgerId = ledgerId;
        this.productCd = productCd;
        this.productNm = productNm;
        this.pjtId = pjtId;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.trAcctCd = trAcctCd;
        this.trAcctNm = trAcctNm;
        this.slipTypeCd = slipTypeCd;
        this.slipTypeNm = slipTypeNm;
        this.segment1Cd = segment1Cd;
        this.segment9Cd = segment9Cd;
        this.segment10Cd = segment10Cd;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.taskNo = taskNo;
        this.taskNm = taskNm;
        this.taskId = taskId;
    }


    /* 발생 전표 위임자 데이터*/
    public EmployeeDto(String compCd, String compNm, String empNo, String empNm, String personId
        , String deptCd, String deptNm, String upperDeptCd, String upperDeptNm
        , String jobDutCd, String jobDutNm, String jobGradeCd, String jobGradeNm
        , String role, String roleNm, String serveCd, String serveNm
        , String email, String mobTelNo, String ledgerId
        , String productCd, String productNm, String pjtId, String pjtCd, String pjtNm
        , String trAcctCd, String trAcctNm, String slipTypeCd, String slipTypeNm
        , String segment1Cd, String segment9Cd, String segment10Cd
        , String cctrCd, String cctrNm, String taskNo, String taskNm, BigDecimal taskId
        , String attribute1, String attribute2, String attribute3, String taxLocationCode, String taxLocationName) {
        this.compCd = compCd;
        this.compNm = compNm;
        this.empNo = empNo;
        this.empNm = empNm;
        this.personId = personId;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.upperDeptCd = upperDeptCd;
        this.upperDeptNm = upperDeptNm;
        this.jobDutCd = jobDutCd;
        this.jobDutNm = jobDutNm;
        this.jobGradeCd = jobGradeCd;
        this.jobGradeNm = jobGradeNm;
        this.role = role;
        this.roleNm = roleNm;
        this.serveCd = serveCd;
        this.serveNm = serveNm;
        this.email = email;
        this.mobTelNo = mobTelNo;
        this.ledgerId = ledgerId;
        this.productCd = productCd;
        this.productNm = productNm;
        this.pjtId = pjtId;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.trAcctCd = trAcctCd;
        this.trAcctNm = trAcctNm;
        this.slipTypeCd = slipTypeCd;
        this.slipTypeNm = slipTypeNm;
        this.segment1Cd = segment1Cd;
        this.segment9Cd = segment9Cd;
        this.segment10Cd = segment10Cd;
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.taskNo = taskNo;
        this.taskNm = taskNm;
        this.taskId = taskId;
        this.attribute1  = attribute1;
        this.attribute2  = attribute2;
        this.attribute3  = attribute3;
        this.taxLocationCode = taxLocationCode;
        this.taxLocationName = taxLocationName;
    }

    String compCode;

    /* 발생전표 행 추가 */
    @QueryProjection
    public EmployeeDto(String cctrCd, String cctrNm, String deptCd, String deptNm,
                       String pjtId, String pjtCd, String pjtNm, String productCd, String productNm,
                       String taskNo,  String taskNm, BigDecimal taskId,
                       String trAcctCd, String trAcctNm, String slipTypeCd, String slipTypeNm,
                       String compCode, String segment9Cd, String segment10Cd, String ledgerId,
                       String attribute2) {
        this.cctrCd = cctrCd;
        this.cctrNm = cctrNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.pjtId = pjtId;
        this.pjtCd = pjtCd;
        this.pjtNm = pjtNm;
        this.productCd = productCd;
        this.productNm = productNm;
        this.taskNo = taskNo;
        this.taskNm = taskNm;
        this.taskId = taskId;
        this.trAcctCd = trAcctCd;
        this.trAcctNm = trAcctNm;
        this.slipTypeCd = slipTypeCd;
        this.slipTypeNm = slipTypeNm;
        this.compCode = compCode;
        this.segment9Cd = segment9Cd;
        this.segment10Cd = segment10Cd;
        this.ledgerId = ledgerId;
        this.attribute2 = attribute2;
    }

    public EmployeeDto(String deptCd, String deptNm, String upperDeptCd, String upperDeptNm) {
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.upperDeptCd = upperDeptCd;
        this.upperDeptNm = upperDeptNm;
    }

    //hitDept 조회용 DTO
    public EmployeeDto(String hitDeptCd, String hitDeptNm){
        this.hitDeptCd = hitDeptCd;
        this.hitDeptNm = hitDeptNm;
    }

    public EmployeeDto(String empNo, String empNm, String deptNm, String jobNm, BigDecimal cnt) {
        this.empNo = empNo;
        this.empNm = empNm;
        this.deptNm = deptNm;
        this.jobNm = jobNm;
        this.cnt = cnt;
    }
}

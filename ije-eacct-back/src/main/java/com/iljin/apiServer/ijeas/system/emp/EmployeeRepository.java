package com.iljin.apiServer.ijeas.system.emp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeKey> {
    @Query(value=" select " +
            " 	EMP_NO as empNo, " +
            " 	COMP_CD as compCd, " +
            " 	EMP_NM as empNm, " +
            " 	COMP_NM as compNm, " +
            " 	DEPT_CD as deptCd, " +
            " 	DEPT_NM as deptNm, " +
            " 	CCTR_CD as cctrCd, " +
            " 	CCTR_NM as cctrNm, " +
            " 	BP_CD as bpCd, " +
            " 	BP_NM as bpNm, " +
            " 	UPPER_DEPT_CD as upperDeptCd, " +
            " 	UPPER_DEPT_NM as upperDeptNm, " +
            " 	EMP_DV_CD as empDvCd, " +
            " 	EMP_DV_NM as empDvNm, " +
            " 	JOB_GROUP_CD as jobGroupCd, " +
            " 	JOB_GROUP_NM as jobGroupNm, " +
            " 	GRADE_CD as gradeCd, " +
            " 	GRADE_NM as gradeNm, " +
            " 	JOB_CD as jobCd, " +
            " 	JOB_NM as jobNm, " +
            " 	DUT_CD as dutCd, " +
            " 	DUT_NM as dutNm, " +
            " 	TITLE_CD as titleCd, " +
            " 	TITLE_NM as titleNm, " +
            " 	SERVE_CD as serveCd, " +
            " 	SERVE_NM as serveNm, " +
            " 	HIRE_CD as hireCd, " +
            " 	HIRE_NM as hireNm, " +
            " 	GRP_JOIN_DT as grpJoinDt, " +
            " 	JOIN_DT as joinDt, " +
            " 	RETIRE_DT as retireDt, " +
            " 	EMAIL, " +
            " 	OFF_TEL_NO as offTelNo, " +
            " 	MOB_TEL_NO as mobTelNo, " +
            " 	VEND_CD as vendCd, " +
            " 	PICODE, " +
            " 	PISTAT, " +
            " 	PIDATE, " +
            " 	PITIME, " +
            " 	PIUSER, " +
            " 	PIMSG, " +
            " 	PIMSGID " +
            " from " +
            " 	tb_mst_emp " +
            " where " +
            " 	COMP_CD = :compCd " +
            " 	and SERVE_CD in (10, 20) " +
            " 	and (EMP_NO like nvl(:value,'') || '%' " +
            " 	or EMP_NM like '%' || nvl(:value,'') || '%' " +
            " 	or DEPT_NM like '%' || nvl(:value,'') || '%') ", nativeQuery=true)
    List<Map> getByEmpNoOrEmpNmOrDeptNmContaining(@Param("compCd") String compCd, @Param("value") String value);

    List<Employee> findByCompCdAndDeptCdOrderByEmpNmAsc(String compCd, String deptCd);

    @Query("select e.hitDeptCd, e.hitDeptNm from Employee e where e.empNo = ?1")
    List<Object[]> findHitDeptByEmpNo(String empNo);


    List<Employee> findByCompCdAndHitDeptCdOrderByEmpNmAsc(String compCd, String deptCd);

    Optional<Employee> findByCompCdAndEmpNo(String compCd, String loginId);

    @Query(value = "" +
            "SELECT emp.compCd" +
            "       ,emp.compNm" +
            "       ,emp.empNo" +
            "       ,emp.empNm" +
            "       ,emp.personId" +
            "       ,emp.deptCd" +
            "       ,emp.deptNm" +
            "       ,emp.upperDeptCd" +
            "       ,emp.upperDeptNm" +
            "       ,emp.jobDutCd" +
            "       ,emp.jobDutNm" +
            "       ,emp.jobGradeCd" +
            "       ,emp.jobGradeNm" +
            "       ,ur.role" +
            "       ,r.roleNm" +
            "       ,emp.serveCd" +
            "       ,emp.serveNm" +
            "       ,u.enableFlag" +
            "       ,emp.email" +
            "       ,emp.mobTelNo" +
            "       ,u.creationDate" +
            "       ,u.modifiedDate" +
            "       ,emp.ledgerId" +
            "       ,emp.productCd" +
            "       ,emp.productNm" +
            "       ,emp.pjtId" +
            "       ,emp.pjtCd" +
            "       ,emp.pjtNm" +
            "       ,emp.trAcctCd" +
            "       ,emp.trAcctNm" +
            "       ,emp.slipTypeCd" +
            "       ,emp.slipTypeNm" +
            "       ,emp.segment1Cd" +
            "       ,emp.segment9Cd" +
            "       ,emp.segment10Cd" +
            "       ,emp.cctrCd" +
            "       ,emp.cctrNm" +
            "       ,emp.taskNo" +
            "       ,emp.taskNm" +
            "       ,emp.taskId" +
            "  FROM Employee emp" +
            "       INNER JOIN User u ON u.loginId = emp.empNo AND u.compCd = emp.compCd " +
            "       INNER JOIN UserRole ur ON ur.userId = u.id AND ur.compCd = emp.compCd" +
            "       INNER JOIN Role r ON r.roleCd = ur.role AND r.compCd = emp.compCd" +
            " WHERE 1=1" +
            "   AND (emp.empNo LIKE '%' || :value || '%'" +
            "       OR emp.empNm LIKE '%' || :value || '%')" +
            "   AND emp.compCd = :compCd " +
            " ORDER BY emp.deptCd, emp.empNm")
    List<Object[]> getEmployeesByEmpNameOrEmpNo(@Param("compCd") String compCd, @Param("value") String value);

    @Query(value = "" +
            "SELECT emp.compCd" +
            "       ,emp.compNm" +
            "       ,emp.empNo" +
            "       ,emp.empNm" +
            "       ,emp.personId" +
            "       ,emp.deptCd" +
            "       ,emp.deptNm" +
            "       ,emp.upperDeptCd" +
            "       ,emp.upperDeptNm" +
            "       ,emp.jobDutCd" +
            "       ,emp.jobDutNm" +
            "       ,emp.jobGradeCd" +
            "       ,emp.jobGradeNm" +
            "       ,ur.role" +
            "       ,r.roleNm" +
            "       ,emp.serveCd" +
            "       ,emp.serveNm" +
            "       ,u.enableFlag" +
            "       ,emp.email" +
            "       ,emp.mobTelNo" +
            "       ,u.creationDate" +
            "       ,u.modifiedDate" +
            "       ,emp.ledgerId" +
            "       ,emp.productCd" +
            "       ,emp.productNm" +
            "       ,emp.pjtId" +
            "       ,emp.pjtCd" +
            "       ,emp.pjtNm" +
            "       ,emp.trAcctCd" +
            "       ,emp.trAcctNm" +
            "       ,emp.slipTypeCd" +
            "       ,emp.slipTypeNm" +
            "       ,emp.segment1Cd" +
            "       ,emp.segment9Cd" +
            "       ,emp.segment10Cd" +
            "       ,emp.cctrCd" +
            "       ,emp.cctrNm" +
            "       ,emp.taskNo" +
            "       ,emp.taskNm" +
            "       ,emp.taskId" +
            "  FROM Employee emp" +
            "       INNER JOIN User u ON u.loginId = emp.empNo AND u.compCd = emp.compCd " +
            "       INNER JOIN UserRole ur ON ur.userId = u.id AND ur.compCd = emp.compCd " +
            "       INNER JOIN Role r ON r.roleCd = ur.role AND r.compCd = emp.compCd " +
            " WHERE 1=1" +
            "   AND emp.compCd = :compCd "+
            "   AND emp.deptCd = :deptCd "+
            "   AND (emp.empNo LIKE '%' || :value || '%'" +
            "       OR emp.empNm LIKE '%' || :value || '%')" +
            " ORDER BY emp.deptCd, emp.empNm")
    List<Object[]> getDpetEmpListByEmpNameOrEmpNo(@Param("compCd") String compCd, @Param("value") String value, @Param("deptCd") String deptCd);

    @Query(value = "" +
            "SELECT emp.compCd" +
            "       ,emp.compNm" +
            "       ,emp.empNo" +
            "       ,emp.empNm" +
            "       ,emp.personId" +
            "       ,emp.deptCd" +
            "       ,emp.deptNm" +
            "       ,emp.upperDeptCd" +
            "       ,emp.upperDeptNm" +
            "       ,emp.jobDutCd" +
            "       ,emp.jobDutNm" +
            "       ,emp.jobGradeCd" +
            "       ,emp.jobGradeNm" +
            "       ,ur.role" +
            "       ,r.roleNm" +
            "       ,emp.serveCd" +
            "       ,emp.serveNm" +
            "       ,u.enableFlag" +
            "       ,emp.email" +
            "       ,emp.mobTelNo" +
            "       ,u.creationDate" +
            "       ,u.modifiedDate" +
            "       ,emp.ledgerId" +
            "       ,emp.productCd" +
            "       ,emp.productNm" +
            "       ,emp.pjtId" +
            "       ,emp.pjtCd" +
            "       ,emp.pjtNm" +
            "       ,emp.trAcctCd" +
            "       ,emp.trAcctNm" +
            "       ,emp.slipTypeCd" +
            "       ,emp.slipTypeNm" +
            "       ,emp.segment1Cd" +
            "       ,emp.segment9Cd" +
            "       ,emp.segment10Cd" +
            "       ,emp.cctrCd" +
            "       ,emp.cctrNm" +
            "       ,emp.taskNo" +
            "       ,emp.taskNm" +
            "       ,emp.taskId" +
            "  FROM Employee emp" +
            "       INNER JOIN User u ON u.loginId = emp.empNo AND u.compCd = emp.compCd " +
            "       INNER JOIN UserRole ur ON ur.userId = u.id AND ur.compCd = emp.compCd " +
            "       INNER JOIN Role r ON r.roleCd = ur.role AND r.compCd = emp.compCd " +
            " WHERE 1=1" +
            "   AND emp.empNo = :value")
    List<Object[]> getEmployeeDetailByLoginId(@Param("value") String value);

    @Query(value = "" +
            "SELECT DISTINCT emp.deptCd" +
            "       ,emp.deptNm" +
            "       ,emp.upperDeptCd" +
            "       ,emp.upperDeptNm" +
            "  FROM Employee emp" +
            " ORDER BY emp.deptCd ASC")
    List<Object[]> getEmployeeDeptList();

    @Query(value = "" +
            "SELECT DISTINCT emp.deptCd" +
            "       ,emp.deptNm" +
            "       ,emp.upperDeptCd" +
            "       ,emp.upperDeptNm" +
            "  FROM Employee emp" +
            " ORDER BY emp.deptCd ASC")
    List<Object[]> getEmployeeList(EmployeeDto employeeDto);

}

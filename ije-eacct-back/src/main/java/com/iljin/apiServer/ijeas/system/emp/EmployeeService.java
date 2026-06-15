package com.iljin.apiServer.ijeas.system.emp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import java.util.List;

public interface EmployeeService {

    /* get Employees */
    List<EmployeeDto> getEmpListByEmpNameOrEmpNo(String value);

    /* get Dept Employees */
    List<EmployeeDto> getDpetEmpListByEmpNameOrEmpNo(EmployeeDto employeeDto);

    /* get Employee detail*/
    EmployeeDto getEmployeeDetailByLoginId(String loginId);

    /* save Employee - New or Old */
    @Modifying
    @Transactional
    EmployeeDto saveEmployeeByLoginId(String loginId, EmployeeDto employeeDto);

    /* delete Employee */
    @Modifying
    @Transactional
    String deleteEmployeeByLoginId(String loginId);

    /* search Employees by EmpNo || EmpNm || DeptNm */
    List<Map> searchEmployeesEmpNoOrEmpNmOrDeptNm(String value);

    /* search Employees by department */
    List<Employee> getEmpsByDept(String deptCd);

    String getHitDeptByEmployee(String empNo);

    List<Employee> getEmpsByHitDept(String deptCd);

    List<EmployeeDto> getEmpDeptList();

    @Transactional(readOnly = true)
    List<EmployeeDto> getDelegatingEmployeeList(String value);
}

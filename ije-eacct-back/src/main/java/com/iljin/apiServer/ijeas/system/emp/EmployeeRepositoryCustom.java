package com.iljin.apiServer.ijeas.system.emp;


import java.util.List;

public interface EmployeeRepositoryCustom {
    List<EmployeeDto> getEmployeeList(EmployeeDto employeeDto);

    List<EmployeeDto> getDelegatingEmployeesByEmpNoOrEmpNm(String value);
}

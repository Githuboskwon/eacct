package com.iljin.apiServer.ijeas.system.emp;

import java.util.List;

public interface EmployeeQdslRepository {

    List<EmployeeDto> searchAccrualSlipEmployees(String value);

    List<EmployeeDto> getDelegatingEmployeesByEmpNoOrEmpNm(String value);
}

package com.iljin.apiServer.ijeas.system.dept;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<DepartmentDto> getDepartmentsByCombo();

    Optional<Department> getDepartmentByDeptNo(String deptCd);

    List<DeptAuthDto> getDeptAuthList(DeptAuthDto deptAuthDto);

    List<DeptAuthDto> getDeptAuthSlipList(DeptAuthDto deptAuthDto);

    @Modifying
    @Transactional
    ResponseEntity<String> saveDeptAuth(List<DeptAuthDto> deptAuthDto);

    @Modifying
    @Transactional
    ResponseEntity<String> delDeptAuth(DeptAuthDto deptAuthDto);
}

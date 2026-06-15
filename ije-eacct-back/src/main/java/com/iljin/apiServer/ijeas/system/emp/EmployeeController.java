package com.iljin.apiServer.ijeas.system.emp;

import com.iljin.apiServer.core.util.Error;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/emp")
public class EmployeeController {
    private final EmployeeService employeeService;

    private final EmployeeQdslRepository employeeQdslRepository;

    private final EmployeeRepositoryCustom employeeRepositoryCustom;

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<Error> empNotFound(EmployeeException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Subject : 임직원 List 조회
     * Features : 전체 조회  임직원 이름, 사번으로 조회
     */
//    @PostMapping("/list")
//    public ResponseEntity<List<EmployeeDto>> getEmployeeList(@RequestBody EmployeeDto employeeDto) {
//        List<EmployeeDto> employees = employeeQdslRepository.getEmployeeList(employeeDto);
//        return new ResponseEntity<>(employees, HttpStatus.OK);
//    }

    @PostMapping("/list")
    public ResponseEntity<List<EmployeeDto>> getEmployeeList(@RequestBody EmployeeDto employeeDto) {
        List<EmployeeDto> employees = employeeRepositoryCustom.getEmployeeList(employeeDto);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 List 조회
     * Features : 전체 조회  임직원 이름, 사번으로 조회
     */
    @GetMapping(value={"/pop/list", "/pop/list/{value}"})
    public ResponseEntity<List<EmployeeDto>> getEmployeeList(@PathVariable(required = false) String value) {
        List<EmployeeDto> employees = employeeService.getEmpListByEmpNameOrEmpNo(value);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 본인, 위임자 조회
     * Features : 전체 조회  임직원 이름, 사번으로 조회
     */
    @GetMapping(value={"/pop/delegate/list", "/pop/delegate/list/{value}"})
    public ResponseEntity<List<EmployeeDto>> getDelegatingEmployeeList(@PathVariable(required = false) String value) {
        List<EmployeeDto> employees = employeeService.getDelegatingEmployeeList(value);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Subject :  부서 임직원 List 조회
     * Features : 부서 조회  임직원 이름, 사번으로 조회
     */
    @PostMapping("/pop/dept/list")
    public ResponseEntity<List<EmployeeDto>> getDeptEmployeeList(@RequestBody EmployeeDto employeeDto) {
        List<EmployeeDto> employees = employeeService.getDpetEmpListByEmpNameOrEmpNo(employeeDto);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 detail 조회
     * Features : 임직원 상세조회
     */
    @GetMapping("/{loginId}")
    public ResponseEntity<EmployeeDto> getEmployeeDetail(@PathVariable(required = false) String loginId) {
        EmployeeDto employee = employeeService.getEmployeeDetailByLoginId(loginId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 정보 저장
     * Features : 신규 저장, 수정
     */
    @PutMapping("/{loginId}")
    public ResponseEntity<EmployeeDto> saveEmployee(@PathVariable(required = true) String loginId, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto employee = employeeService.saveEmployeeByLoginId(loginId, employeeDto);

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 정보 삭제
     * Features : 삭제
     */
    @DeleteMapping("/{loginId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(required = true) String loginId) {
        String response = employeeService.deleteEmployeeByLoginId(loginId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Subject : 임직원 부서 List 조회
     */
    @GetMapping(value="/dept")
    public ResponseEntity<List<EmployeeDto>> getEmployeeDeptList() {
        List<EmployeeDto> deptList = employeeService.getEmpDeptList();
        return new ResponseEntity<>(deptList, HttpStatus.OK);
    }

    /**
     * 임직원 조회
     */
    @GetMapping(value={"/search", "/search/{value}"})
    public ResponseEntity<List<Map>> searchEmployees(@PathVariable(required = false) String value) {
        List<Map> employees = employeeService.searchEmployeesEmpNoOrEmpNmOrDeptNm(value);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * 부서별 임직원 조회
     * */
    @GetMapping("/dept/{deptCd}")
    public ResponseEntity<List<Employee>> getEmployeesByDept(@PathVariable(required = true) String deptCd) {
        List<Employee> list = employeeService.getEmpsByDept(deptCd);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * HIT 부서 조회
     * */
    @GetMapping("/getHitDept/{empNo}")
    public ResponseEntity<String> getHitDeptByEmployee(@PathVariable(required = true) String empNo) {
        String result = employeeService.getHitDeptByEmployee(empNo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * HIT 부서별 임직원 조회
     * */
    @GetMapping("/hitDept/{deptCd}")
    public ResponseEntity<List<Employee>> getEmployeesByHitDept(@PathVariable(required = true) String deptCd) {
        List<Employee> list = employeeService.getEmpsByHitDept(deptCd);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    * 발생 전표 행추가 기본 정보
    * */
    @PostMapping("/accrualSlip/search/{value}")
    public ResponseEntity<List<EmployeeDto>> searchAccrualSlipEmployees(@PathVariable String value) {
        List<EmployeeDto> employees = employeeQdslRepository.searchAccrualSlipEmployees(value);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}

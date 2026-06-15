package com.iljin.apiServer.ijeas.system.dept;

import com.iljin.apiServer.core.util.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/dept")
public class DepartmentController {
    private final DepartmentService departmentService;

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<Error> deptNotFound(DepartmentException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getDepartmentsByCombo() {
        List<DepartmentDto> list = departmentService.getDepartmentsByCombo();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 조회 event
     * @param deptAuthDto
     * */
    @PostMapping("/deptAuth/list")
    public ResponseEntity<List<DeptAuthDto>> getDeptAuthList(@RequestBody DeptAuthDto deptAuthDto) {
        List<DeptAuthDto> list = departmentService.getDeptAuthList(deptAuthDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 전표내역 화면에서 조회 event
     * @param deptAuthDto
     * */
    @PostMapping("/deptAuth/slipLst")
    public ResponseEntity<List<DeptAuthDto>> getDeptAuthSlipList(@RequestBody DeptAuthDto deptAuthDto) {
        List<DeptAuthDto> list = departmentService.getDeptAuthSlipList(deptAuthDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 저장 event
     * @param deptAuthDto
     * */
    @PostMapping("/deptAuth/save")
    public ResponseEntity<String> saveDeptAuth(@RequestBody List<DeptAuthDto> deptAuthDto) {
        return departmentService.saveDeptAuth(deptAuthDto);
    }

    /**
     * 저장 event
     * @param deptAuthDto
     * */
    @PostMapping("/deptAuth/delete")
    public ResponseEntity<String> delDeptAuth(@RequestBody DeptAuthDto deptAuthDto) {
        return departmentService.delDeptAuth(deptAuthDto);
    }

}

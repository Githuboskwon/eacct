package com.iljin.apiServer.ijeas.system.dept;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.cctr.CostCenterDto;
import com.iljin.apiServer.ijeas.system.cctr.CostCenterQdslRepository;
import com.iljin.apiServer.ijeas.system.code.CodeDto;
import com.iljin.apiServer.ijeas.system.code.CodeQdslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;

    private final DeptAuthRepository deptAuthRepository;

    private final DeptAuthQdslRepository deptAuthQdslRepository;

    private final CodeQdslRepository codeQdslRepository;
    private final CostCenterQdslRepository costCenterQdslRepository;

    private final Util util;

    @Override
    public List<DepartmentDto> getDepartmentsByCombo() {
        List<DepartmentDto> list = departmentRepository.getDepartmentsByCombo()
                .stream()
                .map(s -> new DepartmentDto(
                        String.valueOf(Optional.ofNullable(s[0]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[1]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[2]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[3]).orElse(""))
                ))
                .collect(Collectors.toList())
                ;
        return list;
    }

    @Override
    public Optional<Department> getDepartmentByDeptNo(String deptCd) {
        return departmentRepository.findByDeptNo(deptCd);
    }

    @Override
    public List<DeptAuthDto> getDeptAuthList(DeptAuthDto deptAuthDto) {
        return deptAuthQdslRepository.getDeptAuthList(deptAuthDto);
    }

    @Override
    public List<DeptAuthDto> getDeptAuthSlipList(DeptAuthDto deptAuthDto) {
        List<DeptAuthDto> list = deptAuthQdslRepository.getDeptAuthList(deptAuthDto);

        List<Map> deptRole = codeQdslRepository.findByGroupCd(deptAuthDto.getCompCd(), "DEPT_ROLE_RANGE_CD");

        for(Map c : deptRole) {
            String detailCd = c.get("detailCd").toString();

            if(detailCd.equals(deptAuthDto.getRemark())){
                List<CostCenterDto> cctrList = costCenterQdslRepository.getCctrsBetween(deptAuthDto.getCompCd(), c.get("remark1").toString(), c.get("remark2").toString());

                for(CostCenterDto cctr : cctrList){
                    list.add(new DeptAuthDto(cctr.getCompCd(), cctr.getDeptCd(), cctr.getDeptNm(), ""));
                }

                break;
            }
        }

        return list;
    }

    @Override
    public ResponseEntity<String> saveDeptAuth(List<DeptAuthDto> deptAuthDtos) {
        String compCd = util.getLoginCompCd();

        List<DeptAuth> delDeptAuthDts = deptAuthRepository.findByEmpNo(deptAuthDtos.get(0).getEmpNo());
        deptAuthRepository.deleteAll(delDeptAuthDts);

        for(DeptAuthDto deptAuthDto : deptAuthDtos) {
            //insert
            DeptAuth deptAuth = new DeptAuth().builder()
                        .compCd(compCd)
                        .deptCd(deptAuthDto.getDeptCd())
                        .empNo(deptAuthDto.getEmpNo())
                        .remark(deptAuthDto.getRemark())
                        .build();
            deptAuthRepository.save(deptAuth);
        }
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> delDeptAuth(DeptAuthDto deptAuthDto) {
        String compCd = util.getLoginCompCd();

        DeptAuthKey deptAuthKey = new DeptAuthKey();
        deptAuthKey.setCompCd(compCd);
        deptAuthKey.setEmpNo(deptAuthDto.getEmpNo());
        deptAuthKey.setDeptCd(deptAuthDto.getDeptCd());

        deptAuthRepository.deleteById(deptAuthKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }


}

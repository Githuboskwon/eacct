package com.iljin.apiServer.ijeas.sm.apprLine;

import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.emp.EmployeeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Service
public class ApprLineServiceImpl implements ApprLineService {
    private final ApprLineQdslRepository apprLineQdslRepository;
    private final ApprLineRepositoryCustom apprLineRepositoryCustom;
    private final ApprLineHdRepository apprLineHdRepository;
    private final EmployeeRepository employeeRepository;
    private final Util util;

    @Override
    public List<ApprLineDto> getApprLineHdList(ApprLineDto apprLineHd) {
        return apprLineQdslRepository.getApprLineHdList(apprLineHd);
    }

    @Override
    public List<ApprLineDto> getApprLineDtList(ApprLineDto apprLineHd) {
        List<ApprLineDto> apprLineDt = apprLineQdslRepository.getApprLineDtList(apprLineHd);

        /*apprLineDt.forEach(dt -> {
            Optional<Employee> employee = employeeRepository.findByCompCdAndEmpNo(dt.compCd, dt.apprUserId);

            if(employee.isEmpty()){
                return;
            }else {
                dt.apprUserNm = employee.get().getEmpNm();
                dt.jobCd = employee.get().getJobCd();
                dt.jobNm = employee.get().getJobNm();
                dt.cctrCd = employee.get().getCctrCd();
                dt.cctrNm = employee.get().getCctrNm();
                dt.serveCd = employee.get().getServeCd();
            }
        });*/

        return apprLineDt;
    }

    @Override
    public ResponseEntity<String> delApprLineDt(ApprLineDt apprLineDt){
        if(apprLineDt.compCd == null || !hasText(apprLineDt.compCd)){
            return new ResponseEntity<>("회사코드가 없습니다.", HttpStatus.OK);
        }else if(apprLineDt.userId == null || !hasText(apprLineDt.userId)){
            return new ResponseEntity<>("사번정보가 없습니다.", HttpStatus.OK);
        }else if(apprLineDt.apprSeq == null || StringUtils.isEmpty(apprLineDt.apprSeq)){
            return new ResponseEntity<>("라인번호가 없습니다.", HttpStatus.OK);
        }else if(apprLineDt.subApprSeq == null || StringUtils.isEmpty(apprLineDt.subApprSeq)){
            return new ResponseEntity<>("상세라인번호가 없습니다.", HttpStatus.OK);
        }

        return apprLineQdslRepository.delApprLineDt(apprLineDt);
    }

    @Override
    public ResponseEntity<String> delApprLineHd(ApprLineHd apprLineHd){
        if(apprLineHd.compCd == null || !hasText(apprLineHd.compCd)){
            return new ResponseEntity<>("회사코드가 없습니다.", HttpStatus.OK);
        }else if(apprLineHd.userId == null || !hasText(apprLineHd.userId)){
            return new ResponseEntity<>("사번정보가 없습니다.", HttpStatus.OK);
        }else if(apprLineHd.apprSeq == null || StringUtils.isEmpty(apprLineHd.apprSeq)){
            return new ResponseEntity<>("라인번호가 없습니다.", HttpStatus.OK);
        }

        return apprLineQdslRepository.delApprLineHd(apprLineHd);
    }

    @Override
    public ResponseEntity<String> saveApprLineHd(List<ApprLineHd> apprLineHd){
        apprLineHd.forEach(hd -> {
            if(hd.apprSeq != null && hd.apprSeq > 0){
                apprLineHdRepository.save(hd);
            }
        });

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveApprLineDt(List<ApprLineDt> apprLineDt){
        return apprLineQdslRepository.saveApprLineDt(apprLineDt);
    }

    @Override
    public ResponseEntity<String> deleteAllList(ApprLineDt apprLineDt){
        return apprLineQdslRepository.deleteAllList(apprLineDt);
    }

    @Override
    public List<ApprLineDto> getMainList(ApprLineDto apprLineDto) {
        List<ApprLineDto> mainHeaderList = apprLineQdslRepository.getApprLineHdList(apprLineDto);
        List<ApprLineDto> apprLineDtos = null;
        if(mainHeaderList.size() > 0) {
            ApprLineDto mainHeader = mainHeaderList.get(0);
            ApprLineDto apprLineDtDto = ApprLineDto.builder()
                .compCd(mainHeader.getCompCd())
                .apprSeq(mainHeader.getApprSeq())
                .userId(mainHeader.getUserId())
                .build();
            apprLineDtos = apprLineQdslRepository.getApprLineDtList(apprLineDtDto);
        }
        return apprLineDtos;
    }
}

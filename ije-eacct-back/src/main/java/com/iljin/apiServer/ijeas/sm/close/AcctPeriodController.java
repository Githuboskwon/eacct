package com.iljin.apiServer.ijeas.sm.close;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AcctPeriodController {
    private final AcctPeriodService acctPeriodService;

    @Autowired
    public AcctPeriodController(AcctPeriodService acctPeriodService) {
        this.acctPeriodService = acctPeriodService;
    }

    @ExceptionHandler(AcctPeriodException.class)
    public ResponseEntity<Error> acctPeriodNorFound(AcctPeriodException e) {
        Error error = new Error("2001"+e.getMessage(),e );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * EA-06-04 회계기간관리
     * 리스트 조회
     * @param acctPeriodDto has two search variables
     *                      closeYmFrom,
     *                      closeYmTo
     * */
    @PostMapping("/acctPeriod/list")
    public ResponseEntity<List<AcctPeriodDto>> getAcctPeriodList(@RequestBody AcctPeriodDto acctPeriodDto) {
        List<AcctPeriodDto> list = acctPeriodService.getAcctPeriodList(acctPeriodDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /**
     * EA-06-04 오픈된 회계기간 리스트
     * 리스트 조회
     * @param closStatCd is 마감여부.
     * */
    @PostMapping("/acctPeriod/openList")
    public ResponseEntity<List<AcctPeriodDto>> getAcctPeriodOpenList(@RequestBody String closStatCd) {
        List<AcctPeriodDto> list = acctPeriodService.getAcctPeriodOpenList(closStatCd);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * EA-06-04 오픈된 회계기간의 시작날짜 - 종료 날짜
     * */
    @GetMapping("/acctPeriod/open/close/date")
    public ResponseEntity<AcctPeriodDto> getAcctPeriodOpenCloseDate() {
        return new ResponseEntity<>(acctPeriodService.getAcctPeriodOpenCloseDate(), HttpStatus.OK);
    }

    /**
     * EA-06-04 오픈된 회계기간의 시작날짜(오픈된 날짜가 없는 경우, 해당 월 기준)
     * */
    @GetMapping("/acctPeriod/openDate")
    public ResponseEntity<String> getAcctPeriodOpenDate() {
        String openDate  = acctPeriodService.getAcctPeriodOpenDate();
        return new ResponseEntity<>(openDate, HttpStatus.OK);
    }
    /**
     * EA-06-04 회계기간관리
     * 저장 event
     * @param acctPeriodDtos is accounting period data on grid.
     * */
    @PostMapping("/acctPeriod/save")
    public ResponseEntity<String> saveAcctPeriod(@RequestBody List<AcctPeriodDto> acctPeriodDtos) {
        return acctPeriodService.saveAcctPeriod(acctPeriodDtos);
    }
    /**
     * EA-06-04 회계기간관리
     * 행삭제 event
     * @param acctPeriodDto has three variables.
     *                      compCd, baCd, closYm
     * */
    @PostMapping("/acctPeriod/delete")
    public ResponseEntity<String> deleteAcctPeriod(@RequestBody AcctPeriodDto acctPeriodDto) {
        return acctPeriodService.deleteAcctPeriod(acctPeriodDto);
    }
    /**
     * EA-06-04 회계기간관리
     * 월별 자동생성 event
     * @param closeYm
     * Desc. 회계기간의 전체 사업영역 자동생성
     * */
    @PutMapping("/acctPeriod/monthlyAuto")
    public ResponseEntity<String> saveAcctPeriodMonthlyAuto(@RequestBody String closeYm) {
        return acctPeriodService.saveAcctPeriodMonthlyAuto(closeYm);
    }
    /**
     * EA-06-04 회계기간관리
     * 월별 오픈 event
     * @param closeYm
     * Desc. 회계기간의 전체 사업영역 오픈 처리
     * */
    @PutMapping("/acctPeriod/open")
    public ResponseEntity<String> openAcctPeriod(@RequestBody String closeYm) {
        return acctPeriodService.openAcctPeriod(closeYm);
    }
    /**
     * EA-06-04 회계기간관리
     * 월별 마감 event
     * @param closeYm
     * Desc. 회계기간의 전체 사업영역 마감 처리
     * */
    @PutMapping("/acctPeriod/close")
    public ResponseEntity<String> closeAcctPeriod(@RequestBody String closeYm) {
        return acctPeriodService.closeAcctPeriod(closeYm);
    }

}

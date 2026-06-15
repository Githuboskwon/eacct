package com.iljin.apiServer.ijeas.bond;

import com.iljin.apiServer.ijeas.es.erpViews.ErpPaProjectsAllDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class BondController {
    private final BondService bondService;

    private final BondMstQdslRepository bondMstQdslRepository;

    private final BondRepositoryCustom bondRepositoryCustom;

    @ExceptionHandler(BondMstException.class)
    public ResponseEntity<Error> bondMstNotFound(BondMstException e) {
        Error error = new Error(e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * BOND 마스터
     * 리스트 조회
     * @param bondMstDto has five search variables.
     *                   openDate, closeDate, customerId, benCountryCd, refNo
     * */
    @PostMapping("/bondMst/list")
    public ResponseEntity<List<BondMstDto>> getBondMstList(@RequestBody BondMstDto bondMstDto) {
        List<BondMstDto> result = bondService.getBondMstList(bondMstDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * BOND 마스터
     * 저장
     * @param
     * */
    @PostMapping("/bondMst/save")
    public ResponseEntity<String> saveBondMstList(@RequestBody List<BondMstDto> bondMstDtos) {
        return new ResponseEntity<>(bondService.saveBondMstList(bondMstDtos), HttpStatus.OK);
    }

    /**
     * BOND 마스터
     * 행 삭제
     * */
    @PostMapping("/bondMst/delete")
    public ResponseEntity<String> deleteBondMstList(@RequestBody List<BondMstDto> bondMstDtos) {
        return new ResponseEntity<>(bondService.deleteBondMstList(bondMstDtos), HttpStatus.OK);
    }


    /**
     * 발생 전표
     * REF NO 조회
     * @param bondMstDto has two search variables.
     *                   customerNm, refNo
     * */
    @PostMapping("/bondMst/refNo/list")
    public ResponseEntity<List<BondExpendDto>> getBondMstRefNoList(@RequestBody BondMstDto bondMstDto) {
        List<BondExpendDto> result = bondRepositoryCustom.getBondRefNoList(bondMstDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * BOND 경비조회
     * 조회
     * */
    @PostMapping("/bond/expend/list")
    public ResponseEntity<List<BondExpendDto>> getBondExpendList(@RequestBody BondExpendDto bondExpendDto) {
        return new ResponseEntity<>(bondService.getBondExpendList(bondExpendDto), HttpStatus.OK);
    }

    /**
     * BOND 경비조회
     * 프로젝트 팝업 조회
     * */
    @PostMapping("/bond/project")
    public ResponseEntity<List<ErpPaProjectsAllDto>> getProjectList(@RequestBody ErpPaProjectsAllDto erpPaProjectsAllDto) {
        return new ResponseEntity<>(bondService.getProjectList(erpPaProjectsAllDto), HttpStatus.OK);
    }


}

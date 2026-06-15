package com.iljin.apiServer.ijeas.system.expend;

import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.system.code.CodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExpendController {

    private final ExpendService expendService;

    private final ExpendQdslRepository expendQdslRepository;

    /**
     * 경조금 관리
     * 리스트 조회
     * @param expendDto
     * */
    @PostMapping("/expend/list")
    public ResponseEntity<List<ExpendDto>> getExpendList(@RequestBody ExpendDto expendDto) {
        List<ExpendDto> list = expendQdslRepository.getExpendList(expendDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /**
     * 저장 event
     * @param expendDto is oil price list on grid.
     * */
    @PostMapping("/expend/save")
    public ResponseEntity<String> saveExpend(@RequestBody List<ExpendDto> expendDto) {
        return expendService.saveExpend(expendDto);
    }
    /**
     * 행 삭제 event
     * @param expendDtos
     * */
    @PostMapping("/expend/delete")
    public ResponseEntity<String> deleteExpend(@RequestBody List<ExpendDto> expendDtos) {
        for(ExpendDto list : expendDtos){
            expendService.deleteExpend(list);
        }
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }


    /**
     * 경조 분류 조회
     */
    @GetMapping("/expend/code/list")
    public ResponseEntity<List<CodeDto>> getExpend(@RequestParam(value = "search", required = false) String search) {
        List<CodeDto> expends = expendQdslRepository.getExpendCdList(search);
        return new ResponseEntity<>(expends, HttpStatus.OK);
    }


    /**
     * hr 경조금신청 현황
     * 리스트 조회
     * @param slipExpendDto
     * */
    @PostMapping("/expend/hr/list")
    public ResponseEntity<List<SlipExpendDto>> getHrExpendList(@RequestBody SlipExpendDto slipExpendDto) {
        List<SlipExpendDto> list = expendQdslRepository.getHrExpendList(slipExpendDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    /**
     * 경조금 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/expend/popup/list")
    public ResponseEntity<List<ExpendDto>> getSlipExpendList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(expendQdslRepository.getSlipExpendList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 경조금 전표 조회
     * @param slipNo has searchConditions
     * */
    @GetMapping("/expend/slip/{slipNo}")
    public ResponseEntity<SlipExpendDto> getSlipExpend(@PathVariable String slipNo){
        return new ResponseEntity<>(expendQdslRepository.getSlipExpend(slipNo), HttpStatus.OK);
    }
}

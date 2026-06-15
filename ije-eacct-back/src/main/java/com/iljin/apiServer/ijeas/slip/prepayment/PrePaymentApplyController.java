package com.iljin.apiServer.ijeas.slip.prepayment;


import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/prepayment")
public class PrePaymentApplyController {

    private final PrePaymentApplyService prePaymentApplyService;


    /**
     * 선급금 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/advanced/list")
    public ResponseEntity<List<ApplyPrepaysDto>> getAdvancedList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(prePaymentApplyService.getAdvancedList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 반제신청 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/reimbursement/list")
    public ResponseEntity<List<PrepaymentApplyDto>> getReimbursementList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(prePaymentApplyService.getReimbursementList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 선급금 반제
     * @param prepaymentApplyDto has searchConditions
     * */
    @PostMapping("advanced/save")
    public ResponseEntity<String> saveAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDto) throws Exception {
        return prePaymentApplyService.saveAdvanced(prepaymentApplyDto);
    }

    /**
     * 선급금 미반제
     * @param prepaymentApplyDto has searchConditions
     * */
    @PostMapping("advanced/delete")
    public ResponseEntity<String> deleteAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDto) throws Exception {
        return prePaymentApplyService.deleteAdvanced(prepaymentApplyDto);
    }

}

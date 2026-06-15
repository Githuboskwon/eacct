package com.iljin.apiServer.ijeas.slip.hr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/slip/hr")
@RequiredArgsConstructor
public class SlipHrController {
    private final SlipHrService slipHrService;

    @PostMapping("/getPayrollBatchList")
    public ResponseEntity<List<PayrollBatch>> getPayrollBatchList(@RequestBody SlipHrDto slipHrDto){
        List<PayrollBatch> list = slipHrService.getPayrollBatchList(slipHrDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PostMapping("/getPayrollUploadList")
    public ResponseEntity<List<PayrollUpload>> getPayrollUploadList(@RequestBody SlipHrDto slipHrDto){
        List<PayrollUpload> list = slipHrService.getPayrollUploadList(slipHrDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/deletePayroll")
    public Map<String, String> deletePayroll(@RequestBody SlipHrDto slipHrDto){
        return slipHrService.deletePayroll(slipHrDto);
    }

    @PostMapping("/uploadPayroll")
    public Map<String, String> uploadPayroll(@RequestBody List<SlipHrDto> list){
        return slipHrService.uploadPayroll(list);
    }

    @PostMapping("/validationPayroll")
    public Map<String, String> validatePayroll(@RequestBody SlipHrDto slipHrDto){
        return slipHrService.validationPayroll(slipHrDto.payrollBatchId);
    }

    @PostMapping("/createPayroll")
    public Map<String, String> createPayroll(@RequestBody SlipHrDto slipHrDto){
        return slipHrService.createPayroll(slipHrDto.payrollBatchId);
    }

}

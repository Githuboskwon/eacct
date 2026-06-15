package com.iljin.apiServer.ijeas.slip;

import com.iljin.apiServer.core.util.Error;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchy;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/slip")
public class SlipController {
    private final SlipService slipService;

    @ExceptionHandler(SlipException.class)
    public ResponseEntity<Error> slipBadRequest(SlipException e) {
        Error error = new Error(400, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * 발생전표 저장
     *  @param slipDto  has list
     *                  1. slipHeader : 전표 헤더
     *                  2. subHeader
     *                  2-1. slipExpendMoney (경조금)
     *                  2-2. slipBusinessTrip (출장)
     *                  2-3. etc.......
     *                  3. slipDetail : 전표 상세
     * */
    @PostMapping("/save")
    public ResponseEntity<String> saveSlip(@RequestBody SlipDto slipDto){
        return new ResponseEntity<>(slipService.saveSlip(slipDto), HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteSlip(@RequestBody List<SlipDto> slipDtos) throws Exception {
        return new ResponseEntity<>(slipService.deleteSlip(slipDtos), HttpStatus.OK);
    }


    @PostMapping("/history/count")
    public ResponseEntity<BigDecimal> getSlipHistoryCount(@RequestBody SlipHistoryDto slipHistoryDto){
        BigDecimal count = slipService.getSlipHistoryCount(slipHistoryDto);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/history")
    public ResponseEntity<List<SlipHistoryDto>> getSlipHistory(@RequestBody SlipHistoryDto slipHistoryDto){

        List<SlipHistoryDto> list = slipService.getSlipHistory(slipHistoryDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/history/excel")
    public ResponseEntity<List<SlipHistoryDto>> getSlipHistoryExcel(@RequestBody SlipHistoryDto slipHistoryDto){
        List<SlipHistoryDto> list = slipService.getSlipHistoryExcel(slipHistoryDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/header/{slipNo}")
    public ResponseEntity<SlipHeaderDto> getSlipHeader(@PathVariable String slipNo){
        SlipHeaderDto slipDto = slipService.getSlipHeader(slipNo);

        return new ResponseEntity<>(slipDto , HttpStatus.OK);
    }

    @GetMapping("/line/{slipNo}")
    public ResponseEntity<List<SlipDetailDto>> getSlipDetail(@PathVariable String slipNo){
        List<SlipDetailDto> list = slipService.getSlipDetail(slipNo);

        return new ResponseEntity<>(list , HttpStatus.OK);
    }

    @GetMapping("/trafficLine/{slipNo}")
    public ResponseEntity<List<SlipDetailDto>> getSlipTrafficDetail(@PathVariable String slipNo){
        List<SlipDetailDto> list = slipService.getSlipTrafficDetail(slipNo);

        return new ResponseEntity<>(list , HttpStatus.OK);
    }

    @PostMapping("/getAirline")
    public ResponseEntity<List<SlipHeaderDto>> getAirlineSlip(@RequestBody SlipHeaderDto slipHeaderDto) {
        return new ResponseEntity<>(slipService.getAirlineSlip(slipHeaderDto), HttpStatus.OK);
    }

    @PostMapping("/changeAppr")
    public ResponseEntity<Map<String,String>> changeAppr(@RequestBody List<SlipHistoryDto> slipHistoryDto) throws Exception {
        Map<String,String> map = new HashMap<>();

        map = slipService.changeAppr(slipHistoryDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reuse")
    public ResponseEntity<String> reuseSlip(@RequestBody SlipDto slipDto) {
        return slipService.reuseSlip(slipDto);
    }

    @PostMapping("/get/taxLocationCode")
    public ResponseEntity<List<SlipDto>> getTaxLocationCode(@RequestBody SlipDto slipDto){
        List<SlipDto> list = slipService.getTaxLocationCode(slipDto.deptCd);

        return new ResponseEntity<>(list , HttpStatus.OK);
    }

    @GetMapping("/getControlledAccount")
    public ResponseEntity<List<CoaHierarchyDto>> getControlledAccount(){
        List<CoaHierarchyDto> list = slipService.getControlledAccount();

        return new ResponseEntity<>(list , HttpStatus.OK);
    }

}

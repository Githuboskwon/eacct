package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/gl")
public class GlController {

    private final GlService glService;

    /**
     * 발생전표 결제조건 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/term/list")
    public ResponseEntity<List<ErpGlTermsDto>> getErpTermList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpGlTermsDto> list = glService.getErpTermList(erpSlipRequestDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 발생전표 결제예정일 조회
     * @param erpGlTermsDto has searchConditions
     * */
    @PostMapping("/term/due/date")
    public ResponseEntity <ErpGlTermsDto> getErpTermGetDueDate(@RequestBody ErpGlTermsDto erpGlTermsDto) throws Exception {
        return new ResponseEntity<>(glService.getErpTermGetDueDate(erpGlTermsDto), HttpStatus.OK);
    }

    /**
     * 지급 수수료 발행 구분 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/tax/issue/list")
    public ResponseEntity<List<ErpGlCodesDto>> getTaxIsuueTypeList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpGlCodesDto> list = glService.getTaxIsuueTypeList(erpSlipRequestDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Gl전표 전표유형 팝업 조회
     * */
    @PostMapping("/slipType")
    public ResponseEntity<List<ErpGlCodesDto>> getGlSlipTypeList(@RequestBody ErpGlSlipDto erpGlSlipDto) {
        return new ResponseEntity<>(glService.getGlSlipTypeList(erpGlSlipDto), HttpStatus.OK);
    }


}

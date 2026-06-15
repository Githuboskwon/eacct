package com.iljin.apiServer.ijeas.system.oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class OilPriceController {

    private final OilPriceService oilPriceService;
    @Autowired
    public OilPriceController(OilPriceService oilPriceService) { this.oilPriceService = oilPriceService; }

    @ExceptionHandler(OilPriceException.class)
    public ResponseEntity<Error> oilPriceNotFound(OilPriceException e) {
        Error error = new Error("2001 "+e.getMessage() , e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * EA-06-11 유류단가관리
     * 리스트 조회
     * @param oilPriceDto
     * */
    @PostMapping("/oilPrice/list")
    public ResponseEntity<List<OilPriceDto>> getOilPriceList(@RequestBody OilPriceDto oilPriceDto) {
        List<OilPriceDto> list = oilPriceService.getOilPriceList(oilPriceDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /**
     * EA-06-11 유류단가관리
     * 저장 event
     * @param oilPrices is oil price list on grid.
     * */
    @PostMapping("/oilPrice/save")
    public ResponseEntity<String> saveOilPrice(@RequestBody List<OilPriceDto> oilPrices) {
        return oilPriceService.saveOilPrice(oilPrices);
    }
    /**
     * EA-06-11 유류단가관리
     * 행 삭제 event
     * @param
     * */
    @PostMapping("/oilPrice/delete")
    public ResponseEntity<String> deleteOilPrice(@RequestBody OilPriceDto oilPriceDto) {
        return oilPriceService.deleteOilPrice(oilPriceDto);
    }

    /**
     * 발생전표 유류단가 조회
     * */
    @PostMapping("/oilPrice/slip")
    public ResponseEntity<OilPriceDto> getOilPriceSlip(@RequestBody OilPriceDto oilPriceDto) {
        return new ResponseEntity<>(oilPriceService.getOilPriceSlip(oilPriceDto), HttpStatus.OK);
    }
}

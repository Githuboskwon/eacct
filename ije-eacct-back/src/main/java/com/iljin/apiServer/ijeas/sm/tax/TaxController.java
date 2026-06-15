package com.iljin.apiServer.ijeas.sm.tax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TaxController {

	private final TaxService taxService;

	@Autowired
    public TaxController(TaxService taxService) {
		this.taxService = taxService;
	}

	@ExceptionHandler(TaxException.class)
    public ResponseEntity<Error> taxNotFound(TaxException e) {
        Error error = new Error("2001"+e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 세금코드 콤보 조회
     */
    @GetMapping("/taxes")
    public ResponseEntity<List<Tax>> getComboBoxOfEvdType(@RequestParam("evdTypeCd") String evdTypeCd) {
    	List<Tax> taxes = taxService.getByEvdTypeCd(evdTypeCd);
    	return new ResponseEntity<>(taxes, HttpStatus.OK);
    }

    /**
     * EA-06-03 세금코드관리
     * 리스트 조회
     * @param taxDto has two search variables.
     *               compCd : 회사코드
     *               taxCd : 세금코드/명
     * */
    @PostMapping("/taxes/list")
    public ResponseEntity<List<TaxDto>> getTaxCodeList(@RequestBody TaxDto taxDto) {
        List<TaxDto> list = taxService.getTaxCodeList(taxDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /**
     * EA-06-03 세금코드관리
     * 저장 event
     * @param taxDtos is tax code informations on grid.
     * */
    @PostMapping("/taxes/save")
    public ResponseEntity<String> saveTaxCodes(@RequestBody List<TaxDto> taxDtos) {
        return taxService.saveTaxCodes(taxDtos);
    }
    /**
     * EA-06-03 세금코드관리
     * 행삭제 event
     * @param
     * */
    @PostMapping("/taxes/delete")
    public ResponseEntity<String> deleteTaxCode(@RequestBody TaxDto taxDto) {
        return taxService.deleteTaxCode(taxDto);
    }

}

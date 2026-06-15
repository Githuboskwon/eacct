package com.iljin.apiServer.ijeas.system.cctr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CostCenterController {
	
    private static final Logger logger = LoggerFactory.getLogger(CostCenterController.class);
	
	private final CostCenterService cctrService;

	@Autowired
    public CostCenterController(CostCenterService cctrService) {
		this.cctrService = cctrService;
	}

    @ExceptionHandler(CostCenterException.class)
    public ResponseEntity<Error> cctrNotFound(CostCenterException e) {
        Error error = new Error("2001  "+e, e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 비용부서 조회
     */
    @GetMapping(value={"/cctr","/cctr/{value}"})
    public ResponseEntity<List<CostCenterDto>> getCctrs(@PathVariable(required=false) String value) {
    	//logger.debug("value:"+value);
    	List<CostCenterDto> cctrs = cctrService.getByCctrCdOrCctrNm(value);
    	return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }

    @PostMapping("/slip/cctr")
    public ResponseEntity<List<CostCenterDto>> getSlipCctrs(@RequestBody CostCenterDto costCenterDto) {

        List<CostCenterDto> cctrs = cctrService.getSlipCctr(costCenterDto);
        return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }


    @PostMapping("/cctr/deptRole")
    public ResponseEntity<List<CostCenterDto>> getCctrsDeptRole(@RequestBody CostCenterDto costCenterDto) {
        List<CostCenterDto> cctrs = cctrService.getCctrsDeptRole(costCenterDto);
        return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }
}

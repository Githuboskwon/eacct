package com.iljin.apiServer.ijeas.system.cctr2;

import com.iljin.apiServer.ijeas.system.cctr.CostCenterDto;
import com.iljin.apiServer.ijeas.system.delegate.DelegateDto;
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
public class CostCenterController2 {
	
    private static final Logger logger = LoggerFactory.getLogger(CostCenterController2.class);
	
	private final CostCenterService2 cctrService;

	@Autowired
    public CostCenterController2(CostCenterService2 cctrService) {
		this.cctrService = cctrService;
	}

    @ExceptionHandler(CostCenterException2.class)
    public ResponseEntity<Error> cctrNotFound(CostCenterException2 e) {
        Error error = new Error("2001  "+e, e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 비용부서 조회
     */
    @GetMapping(value={"/cctr2","/cctr2/{value}"})
    public ResponseEntity<List<CostCenterDto2>> getCctrs(@PathVariable(required=false) String value) {
    	//logger.debug("value:"+value);
    	List<CostCenterDto2> cctrs = cctrService.getByCctrCdOrCctrNm(value);
    	return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }


    @PostMapping("/slip/cctr2")
    public ResponseEntity<List<CostCenterDto2>> getSlipCctrs(@RequestBody CostCenterDto2 costCenterDto) {

        List<CostCenterDto2> cctrs = cctrService.getSlipCctr(costCenterDto);
        return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }

    @PostMapping("/slip/cctr2/admin")
    public ResponseEntity<List<CostCenterDto2>> getSlipCctrAdmin(@RequestBody CostCenterDto2 costCenterDto) {

        List<CostCenterDto2> cctrs = cctrService.getSlipCctrAdmin(costCenterDto);
        return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }

    @PostMapping("/cctr2/delegate")
    public ResponseEntity<List<CostCenterDto2>> getCctrsDelegate(@RequestBody DelegateDto delegateDto) {
        List<CostCenterDto2> cctrs = cctrService.getCctrsDelegate(delegateDto);
        return new ResponseEntity<>(cctrs, HttpStatus.OK);
    }
}

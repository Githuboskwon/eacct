package com.iljin.apiServer.ijeas.slip.header;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/slip")
public class SlipBusinessTripController {
    private final SlipBusinessTripService slipBusinessTripService;


    /**
     * 출장 목록 조회
     * @param slipBusinessTripDto has searchConditions
     * */
    @PostMapping("/business/trip/list")
    public ResponseEntity<List<SlipBusinessTripDto>> getBusinessTripList(@RequestBody SlipBusinessTripDto slipBusinessTripDto) {
        return new ResponseEntity<>(slipBusinessTripService.getBusinessTripList(slipBusinessTripDto), HttpStatus.OK);
    }

}

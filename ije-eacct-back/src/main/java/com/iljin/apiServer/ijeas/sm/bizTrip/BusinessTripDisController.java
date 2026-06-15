package com.iljin.apiServer.ijeas.sm.bizTrip;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessTripDisController {

    private final BusinessTripDisService businessTripDisService;

    @ExceptionHandler(BusinessTripDisException.class)
    public ResponseEntity<Error> businessTripDisNotFound(BusinessTripDisException e) {
        Error error = new Error("2001"+e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 출장거리관리
     * 리스트 조회
     * @param businessTripDisDto has two search variables.
     *                           standardYymm, departureArrivalArea
     */
    @PostMapping("/businessTripDis/list")
    public ResponseEntity<List<BusinessTripDisDto>> getBusinessTripDisList(@RequestBody BusinessTripDisDto businessTripDisDto) {
        List<BusinessTripDisDto> result = businessTripDisService.getBusinessTripDisList(businessTripDisDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/businessTripDis/slip")
    public ResponseEntity<List<BusinessTripDisDto>> getBusinessTripDisSlip(@RequestBody BusinessTripDisDto businessTripDisDto){
        List<BusinessTripDisDto> dto = businessTripDisService.getBusinessTripDisSlip(businessTripDisDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * 출장거리관리
     * 저장
     * @param businessTripDisDtos is business trip distance list on grid.
     * */
    @PostMapping("/businessTripDis/save")
    public ResponseEntity<String> saveBusinessTripDisList(@RequestBody List<BusinessTripDisDto> businessTripDisDtos) {
        return new ResponseEntity<>(businessTripDisService.saveBusinessTripDisList(businessTripDisDtos), HttpStatus.OK);
    }

    /**
     * 출장거리관리
     * 행 삭제
     * */
    @PostMapping("/businessTripDis/delete")
    public ResponseEntity<String> deleteBusinessTripDis(@RequestBody BusinessTripDisDto businessTripDisDto) {
        return new ResponseEntity<>(businessTripDisService.deleteBusinessTripDis(businessTripDisDto), HttpStatus.OK);
    }
}

package com.iljin.apiServer.ijeas.system.dff;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/dff")
public class DffController {
    private final DffService dffService;

    @PostMapping("/list")
    public ResponseEntity<List<DffDto>> getDffFromAccount(@RequestBody DffDto dffDto){
        List<DffDto> result = dffService.getDffFromAccount(dffDto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping(value={"/detail"})
    public ResponseEntity<List<DffDto>> getDffDetail(@RequestBody DffDto dffDto){
        List<DffDto> result = dffService.getDffDetail(dffDto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

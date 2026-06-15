package com.iljin.apiServer.ijeas.system.trx;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/trx")
@RequiredArgsConstructor
public class TrxController {
    private final TrxService trxService;

    @PostMapping("/list")
    public ResponseEntity<List<TrxDto>> getTrxList(@RequestBody TrxDto trxDto){

       return new ResponseEntity<>(trxService.getTrxList(trxDto), HttpStatus.OK);
    }

    @PostMapping("/slip/list")
    public ResponseEntity<List<TrxDto>> getSlipTrxList(@RequestBody TrxDto trxDto){

        return new ResponseEntity<>(trxService.getSlipTrxList(trxDto), HttpStatus.OK);
    }

    @PostMapping("/getAwt")
    public ResponseEntity<List<TrxDto>> getAwtInfo(@RequestBody TrxDto trxDto){
        return new ResponseEntity<>(trxService.getAwtInfo(trxDto), HttpStatus.OK);
    }
}

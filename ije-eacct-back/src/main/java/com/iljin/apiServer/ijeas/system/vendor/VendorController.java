package com.iljin.apiServer.ijeas.system.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @PostMapping("/list")
    public ResponseEntity<List<VendorDto>> getVendorList(@RequestBody VendorDto vendorDto){
        Pageable pageable = PageRequest.of(vendorDto.page, vendorDto.limit);
        List<VendorDto> list = vendorService.getVendorList(vendorDto, pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/count")
    public ResponseEntity<Long> getVendorCount(@RequestBody VendorDto vendorDto){
        return new ResponseEntity<>(vendorService.getVendorCount(vendorDto), HttpStatus.OK);
    }

    @PostMapping("/bankList")
    public ResponseEntity<List<VendorDto>> getBankList(@RequestBody VendorDto vendorDto){
        List<VendorDto> list = vendorService.getBankList(vendorDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/list/search")
    public ResponseEntity<List<VendorDto>> getVendorListSearch(@RequestBody VendorDto vendorDto){
        Pageable pageable = PageRequest.of(vendorDto.page, vendorDto.limit);
        List<VendorDto> list = vendorService.getVendorListSearch(vendorDto, pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/update")
    public Map<String, String> callErpVendorUpdate(){
        return vendorService.callErpVendorUpdate();
    }


    @PostMapping("/getTerms")
    public ResponseEntity<List<VendorDto>> getTermsList(@RequestBody VendorDto vendorDto){
        List<VendorDto> list = vendorService.getTermsList(vendorDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

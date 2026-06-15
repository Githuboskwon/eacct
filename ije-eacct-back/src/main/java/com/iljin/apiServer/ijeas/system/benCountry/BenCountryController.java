package com.iljin.apiServer.ijeas.system.benCountry;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/benCountry")
public class BenCountryController {

    private final BenCountryService benCountryService;

    /**
     * 수익자국가 조회
     * 전체 조회 수익자국가 코드, 국가명으로 조회
     * */
    @GetMapping(value={"/list", "/list/{value}"})
    public ResponseEntity<List<BenCountryDto>> getBenCountryList(@PathVariable(required = false) String value) {
        List<BenCountryDto> result = benCountryService.getBenCountryList(value);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

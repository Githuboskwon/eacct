package com.iljin.apiServer.ijeas.slip.tax;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/tax")
public class TaxDtiNtsMainController {

    private final TaxDtiNtsMainService taxDtiNtsMainService;

    /**
     * 발생전표 작성 법인카드 팝업
     * @param searchDto
     * searchSlipHeaderId : 전표headerId
     * searchFrom : 작성시작일자
     * searchTo : 작성종료일자
     * searchSuId : 사업자번호/명
     * searchIssueId : 승인번호
     * searchEmail : 담당자 이메일
     * searchIpPersname : 공급받는자 담당자
     * searchTaxEvidenceType :
     * searchTaxSmartbillNo :
     * searchLineAttribute6 :
     * 조회조건 진행상태는 AS-IS에서 개발되어 있지 않음. ERP팀과 정의 필요함.
     * */
    @PostMapping("/dtiNtsMain/list")
    public ResponseEntity<List<TaxDtiNtsMainDto>> getTaxDtiNtsMainList(@RequestBody SearchTaxDtiNtsDto searchDto){
        return new ResponseEntity<>(taxDtiNtsMainService.getTaxDtiNtsMainList(searchDto), HttpStatus.OK);
    }

    @PostMapping("/taxRcvList")
    public ResponseEntity<List<TaxDtiNtsMainDto>> getTaxRcvList(@RequestBody SearchTaxDtiNtsDto searchDto){
        return new ResponseEntity<>(taxDtiNtsMainService.getTaxRcvList(searchDto), HttpStatus.OK);
    }
}

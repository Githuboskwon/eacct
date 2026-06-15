package com.iljin.apiServer.ijeas.slip.etax;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/salesTax")
@RequiredArgsConstructor
public class SalesTaxInvoiceController {
    private final SalesTaxInvoiceService salesTaxInvoiceService;

    //매출전표 조회 및 확정

    @PostMapping("/list")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getSalesTaxInvoiceList(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getSalesTaxInvoiceList(salesTaxInvoiceDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/getLine")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getLine(@RequestBody Integer trxId){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getLine(trxId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/accounting/{customerTrxId}")
    public ResponseEntity<Map<String,String>> accounting(@PathVariable Integer customerTrxId){
        Map<String,String> map = salesTaxInvoiceService.accounting(customerTrxId);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Map<String,String>> cancelInvoice(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = salesTaxInvoiceService.cancelInvoice(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/confirm/sum")
    public ResponseEntity<Map<String,String>> confirmSum(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = salesTaxInvoiceService.confirmSum(list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/confirm/one")
    public ResponseEntity<Map<String,String>> confirmOne(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = salesTaxInvoiceService.confirmOne(list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/confirm/not")
    public ResponseEntity<Map<String,String>> confirmNot(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = salesTaxInvoiceService.confirmNot(list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/modifyAmt")
    public ResponseEntity<Map<String,String>> modifyAmount(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String,String> map = salesTaxInvoiceService.modifyAmount(salesTaxInvoiceDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    //매출세금계산서 발행
    @PostMapping("/issueList")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getSalesTaxInvoiceIssueList(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getSalesTaxInvoiceIssueList(salesTaxInvoiceDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PostMapping("/getItem")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getItem(@RequestBody Integer etaxIssueId){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getItem(etaxIssueId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/getSlipNo")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getSlipNo(@RequestBody Integer etaxIssueId){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getSlipNo(etaxIssueId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/issue")
    public ResponseEntity<Map<String,String>> issueTaxInvoice(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.issueTaxInvoice(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reIssue")
    public ResponseEntity<Map<String,String>> reIssueTaxInvoice(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.reIssueTaxInvoice(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("/cancelIssue")
    public ResponseEntity<Map<String,String>> cancelIssueTaxInvoice(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.cancelIssueTaxInvoice(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/emailReSend")
    public ResponseEntity<Map<String,String>> emailReSend(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.emailReSend(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reverseSave")
    public ResponseEntity<Map<String,String>> reverseSave(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.reverseSave(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/reverseCancel")
    public ResponseEntity<Map<String,String>> reverseCancel(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.reverseCancel(list);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/etaxModify")
    public ResponseEntity<Map<String,String>> etaxModify(@RequestBody List<SalesTaxInvoiceDto> list){
        Map<String,String> map = new HashMap<>();

        try {
            map = salesTaxInvoiceService.etaxModify(list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("Message", "Error : " + e.getMessage());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/modifyList")
    public ResponseEntity<List<SalesTaxInvoiceDto>> getModifyList(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        List<SalesTaxInvoiceDto> list = salesTaxInvoiceService.getModifyList(salesTaxInvoiceDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/preview")
    public ResponseEntity<Map<String,String>> preview(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String,String> map = new HashMap<>();

        map = salesTaxInvoiceService.preview(salesTaxInvoiceDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/modifyItem")
    public ResponseEntity<Map<String,String>> modifyItem(@RequestBody SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String,String> map = salesTaxInvoiceService.modifyItem(salesTaxInvoiceDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

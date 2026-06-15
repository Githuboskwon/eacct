package com.iljin.apiServer.ijeas.es;

import com.iljin.apiServer.core.util.Error;
import com.iljin.apiServer.ijeas.es.bulk.ErpBondSlipDto;
import com.iljin.apiServer.ijeas.es.bulk.ErpBulkSlipDto;
import com.iljin.apiServer.ijeas.es.collection.ErpCollectionSlipDto;
import com.iljin.apiServer.ijeas.es.erpViews.*;

import com.iljin.apiServer.ijeas.es.fund.ErpFundSlipDto;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.iljin.apiServer.ijeas.es.item.ErpItemSlipDto;
import java.util.List;

import com.iljin.apiServer.ijeas.es.sale.ErpSalesOverseasDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/erp")
public class ErpSlipController {

    private final ErpSlipService erpSlipService;

    @ExceptionHandler(ErpSlipException.class)
    public ResponseEntity<Error> ErpSlipNotFound(ErpSlipException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * ERP전표 가져오기
     * @param slipType : 21 건별지급 (ITEM)
     *                   : 22 대량지급 (BULK)
     *                   : 23 전자채권만기 (BOND)
     *                   : 24 자금전표 (FUND)
     *                   : 25 집금전표 (CLCT)
     *                   : 27 GL전표 (GL)
     *                   : 28 매출전표 (SALE)
     *                   : 29 해외전표 (FRGN)
     *                   : 30 수출전표 (EXPT)
     * */
    @PostMapping("/slip/{slipType}")
    public ResponseEntity<String> pullErpSlipListFromErp(@PathVariable String slipType, @RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        String returnMsg = "가져오기에 실패했습니다.";

        if(slipType.equals(SlipType.ITEM.getCode())) { // 건별 지급(21)
            returnMsg = erpSlipService.pullErpItemSlipListFromErp(slipType, erpSlipRequestDto);
        } else if (slipType.equals(SlipType.BULK.getCode())) { // 대량 지급(22)
            returnMsg = erpSlipService.pullErpBulkSlipListFromErp(slipType, erpSlipRequestDto);
        }else if (slipType.equals(SlipType.BOND.getCode())) { // 전자 채권(23)
            returnMsg = erpSlipService.pullErpBondSlipListFromErp(slipType, erpSlipRequestDto);
        }else if (slipType.equals(SlipType.FUND.getCode())) { // 자금 전표(24)
            returnMsg = erpSlipService.pullErpFundSlipListFromErp(slipType, erpSlipRequestDto);
        }else if (slipType.equals(SlipType.CLCT.getCode())) { // 집금 전표(25)
            returnMsg = erpSlipService.pullErpClctSlipListFromErp(slipType, erpSlipRequestDto);
        } else if (slipType.equals(SlipType.GL.getCode())) {
            returnMsg = erpSlipService.pullErpGlSlipListFromErp(slipType, erpSlipRequestDto);
        } else if (slipType.equals(SlipType.SALE.getCode())) { // 매출 전표(28)
            returnMsg = erpSlipService.pullErpSalesSlipListFromErp(slipType, erpSlipRequestDto);
        }else if (slipType.equals(SlipType.FRGN.getCode())) { // 해외 전표(29)
            returnMsg = erpSlipService.pullErpForeignSlipListFromErp(slipType, erpSlipRequestDto);
        }else if (slipType.equals(SlipType.EXPT.getCode())) { // 해외 전표(30)
            returnMsg = erpSlipService.pullErpExportSlipListFromErp(slipType, erpSlipRequestDto);
        }
        return new ResponseEntity<>(returnMsg, HttpStatus.OK);
    }

    /**
     * 건별지급 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/item/list")
    public ResponseEntity<List<ErpItemSlipDto>> getErpSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpItemSlipList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 건별지급 상세 조회
     * */
    @PostMapping("/slip/item/detail")
    public ResponseEntity<ErpItemSlipDto> getErpSlipDetail(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpItemSlipDetail(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 대량지급 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/bulk/list")
    public ResponseEntity<List<ErpBulkSlipDto>> getErpBulkSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpBulkSlipList(erpSlipRequestDto), HttpStatus.OK);
    }


    /**
     * 대량지급 / 전자채권 상세 조회
     * */
    @PostMapping("/slip/bulk/detail")
    public ResponseEntity<ErpBulkSlipDto> getErpBulkSlipDetail(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpBulkSlipDetail(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 전자채권 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/bond/list")
    public ResponseEntity<List<ErpBondSlipDto>> getErpBondSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpBondSlipList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 자금전표 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/fund/list")
    public ResponseEntity<List<ErpFundSlipDto>> getErpFundSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpFundSlipList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 자금전표 상세 조회
     * */
    @PostMapping("/slip/fund/detail")
    public ResponseEntity<ErpFundSlipDto> getErpFundSlipDetail(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpFundSlipDetail(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 집금전표 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/collection/list")
    public ResponseEntity<List<ErpCollectionSlipDto>> getErpCollectionSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpCollectionSlipList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * 집금전표 상세 조회
     * */
    @PostMapping("/slip/collection/detail")
    public ResponseEntity<ErpCollectionSlipDto> getErpCollectionSlipDetail(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpCollectionSlipDetail(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * GL전표 목록 조회
     * */
    @PostMapping("/slip/gl/list")
    public ResponseEntity<List<ErpGlSlipDto>> getErpGlSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpGlSlipList(erpSlipRequestDto), HttpStatus.OK);
    }

    /**
     * GL전표 상세 조회
     * */
    @PostMapping("/slip/gl/detail")
    public ResponseEntity<ErpGlSlipDto> getErpGlSlipDetail(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpGlSlipDetail(erpSlipRequestDto), HttpStatus.OK);
    }


    /**
     * 매출/해외/수출 전표 목록 조회
     * @param erpSlipRequestDto has searchConditions
     * */
    @PostMapping("/slip/sales/list")
    public ResponseEntity<List<ErpSalesOverseasDto>> getErpSalesSlipList(@RequestBody ErpSlipRequestDto erpSlipRequestDto) {
        return new ResponseEntity<>(erpSlipService.getErpSalesSlipList(erpSlipRequestDto), HttpStatus.OK);
    }


    /**
     * ERP전표 전송취소
     * */
    @PostMapping("/slip/delete/{slipTypeCd}")
    public ResponseEntity<String> deleteErpSlip(@PathVariable String slipTypeCd, @RequestBody List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String returnMsg = "삭제 실패했습니다.";
        if(slipTypeCd.equals(SlipType.ITEM.getCode())){
            returnMsg = erpSlipService.deleteErpSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.BULK.getCode())){
            returnMsg = erpSlipService.deleteErpBulkSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.GL.getCode())) {
            returnMsg = erpSlipService.deleteErpGlSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.BOND.getCode())) {
            returnMsg = erpSlipService.deleteErpBondSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.FUND.getCode())) {
            returnMsg = erpSlipService.deleteErpFundSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.CLCT.getCode())) {
            returnMsg = erpSlipService.deleteErpClctSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.SALE.getCode()) || slipTypeCd.equals(SlipType.FRGN.getCode())) {
            returnMsg = erpSlipService.deleteErpSaleAndFrgnSlip(erpSlipRequestDtos);
        } else if(slipTypeCd.equals(SlipType.EXPT.getCode())) {
            returnMsg = erpSlipService.deleteErpExportSlip(erpSlipRequestDtos);
        }


        return new ResponseEntity<>(returnMsg, HttpStatus.OK);
    }

    /**
     * 매출 전표 저장
     * */
    @PostMapping("/slip/sales/save")
    public ResponseEntity<String> saveExportSlip(@RequestBody List<ErpSlipRequestDto> erpSlipRequestDtos) {
        return erpSlipService.saveExportSlip(erpSlipRequestDtos);
    }

}

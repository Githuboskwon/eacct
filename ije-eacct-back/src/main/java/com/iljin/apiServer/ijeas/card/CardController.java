package com.iljin.apiServer.ijeas.card;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/card/list")
    public ResponseEntity<List<CardDto>> getBusinessCreditCardList(@RequestBody CardDto cardDto) {
        Pageable pageable = PageRequest.of(cardDto.page, cardDto.limit);
        List<CardDto> cardList = cardService.getBusinessCreditCardList(cardDto,pageable);

        return new ResponseEntity<>(cardList, HttpStatus.OK);
    }

    @PostMapping("/card/pop/list")
    public ResponseEntity<List<CardDto>> getBusinessCreditCardPopList(@RequestBody CardDto cardDto) {
        Pageable pageable = PageRequest.of(cardDto.page, cardDto.limit);
        List<CardDto> cardList = cardService.getBusinessCreditCardPopList(cardDto,pageable);

        return new ResponseEntity<>(cardList, HttpStatus.OK);
    }

    @PostMapping("/card/count")
    public ResponseEntity<Long> getBusinessCreditCardCount(@RequestBody CardDto cardDto){
        return new ResponseEntity<>(cardService.getBusinessCreditCardCount(cardDto), HttpStatus.OK);
    }



    /**
     * EA-04-01 법인카드 정보관리
     * Func : 법인카드 정보(상세)
     * @param crdNo
     * */
    @GetMapping("/card/detail/{crdNo}")
    public ResponseEntity<List<CardDto>> getCardDetail(@PathVariable String crdNo) {
        List<CardDto> card = cardService.getCardDetail(crdNo);

        if(card.size() > 0) {
            return new ResponseEntity<>(card, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * EA-04-01 법인카드 정보관리
     * Func : 삭제
     * @param crdNo
     * */
    @DeleteMapping("/card/delete/{crdNo}")
    public ResponseEntity<String> deleteCard(@PathVariable String crdNo) {
        return cardService.deleteCard(crdNo);
    }


    /**
     * EA-04-01 법인카드 정보관리
     * Func : 저장(신규/수정)
     * @param cardDto
     * */
    @PostMapping("/card/save")
    public ResponseEntity<String> saveBusinessCreditCardDetail(@RequestBody CardDto cardDto) {

        return cardService.saveCard(cardDto);
    }

    @PostMapping("/card/useList")
    public ResponseEntity<List<CardUseListDto>> getCardUseList(@RequestBody CardUseListDto cardUseListDto) {
        List<CardUseListDto> list = cardService.getCardUseList(cardUseListDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/card/useList/change")
    public ResponseEntity<String> changeCardStatus(@RequestBody List<CardUseListDto> cardUseListDto) {
        for(CardUseListDto list : cardUseListDto){
            cardService.changeCardStatus(list);
        }
        return new ResponseEntity<>("변경되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/card/useList/cancel")
    public ResponseEntity<String> cancelCardStatus(@RequestBody List<CardUseListDto> cardUseListDto) {
        for(CardUseListDto list : cardUseListDto){
            cardService.cancelCardStatus(list);
        }
        return new ResponseEntity<>("취소되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/card/useList/emp/update")
    public ResponseEntity<String> updateCardEmpInfo(@RequestBody List<CardUseListDto> cardUseListDto) {
        for(CardUseListDto list : cardUseListDto){
            cardService.updateCardEmpInfo(list);
        }
        return new ResponseEntity<>("변경되었습니다.", HttpStatus.OK);
    }

    /**
     * 발생전표 작성 법인카드 팝업
     * @param cardUseListDto
     * ledgerId : ?? (필수)
     * taxEvidenceType : ?? (필수)
     * compCd : 회사구분코드(필수0
     * slipHeaderId : 전표헤더ID (필수)
     * cardNo : 카드번호(필수)
     * userId : 사용자 (필수)
     * searchDtmFr : 사용시작일자
     * searchDtmTo : 사용종료일자
     * status : 진행상태 (개인용도 : 07, 미처리 : 01)
     * cancelFlag : 취소여부 (취소 : Y, 승인 : N)
     * erpSlipFlag : erp전표에서 법인카드 맵핑하는 플래그
     * */
    @PostMapping("/card/popup/useList")
    public ResponseEntity<List<CardUseListDto>> getCardPopupUseList(@RequestBody CardUseListDto cardUseListDto) {
        List<CardUseListDto> list = cardService.getCardPopupUseList(cardUseListDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 법인카드 사용정보  (as-is 신용카드전표)
     * @param apprNo is 승인번호(법인카드 승인번호)
     * */
    @GetMapping("/card/use/info/{apprNo}")
    public ResponseEntity<List<CardUseListDto>> getCardUsed(@PathVariable String apprNo) {
        return new ResponseEntity<>(cardService.getSimpleCardUsedData(apprNo), HttpStatus.OK);
    }


}

package com.iljin.apiServer.ijeas.card;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CardService {

    @Modifying
    @Transactional
    ResponseEntity<String> saveBcCard(CardDto cardDto);

    List<CardDto> getBusinessCreditCardList(CardDto cardDto, Pageable pageable);

    List<CardDto> getBusinessCreditCardPopList(CardDto cardDto, Pageable pageable);

    Long getBusinessCreditCardCount(CardDto cardDto);

    List<CardDto> getCardDetail(String crdNo);

    ResponseEntity<String> deleteCard(String crdNo);

    ResponseEntity<String> saveCard(CardDto cardDto);

    List<CardUseListDto> getCardUseList(CardUseListDto cardUseListDto);

    List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto);

    @Modifying
    @Transactional
    ResponseEntity<String> changeCardStatus(CardUseListDto cardUseListDto);

    @Modifying
    @Transactional
    ResponseEntity<String> cancelCardStatus(CardUseListDto cardUseListDto);

    @Modifying
    @Transactional
    ResponseEntity<String> updateCardEmpInfo(CardUseListDto cardUseListDto);

    List<CardUseListDto> getSimpleCardUsedData(String apprNo);

}

package com.iljin.apiServer.ijeas.card;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardQdslRepository {

    List<CardDto> getBusinessCreditCardList(CardDto cardDto, Pageable pageable);

    List<CardDto> getBusinessCreditCardPopList(CardDto cardDto, Pageable pageable);

    Long getBusinessCreditCardCount(CardDto cardDto);

    List<CardDto> getCardDetail(String crdNo);

    List<CardUseListDto> getCardUseList(CardUseListDto cardUseListDto);

    List<CardUseListDto> getSimpleCardUsedData(String apprNo);

    // AS-IS와 다른 쿼리임. CUSTOM으로 새로 개발함. 
    //List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto);

}

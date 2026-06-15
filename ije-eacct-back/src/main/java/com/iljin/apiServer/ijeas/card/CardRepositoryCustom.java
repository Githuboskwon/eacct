package com.iljin.apiServer.ijeas.card;

import java.util.List;

public interface CardRepositoryCustom {

    List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto);

}

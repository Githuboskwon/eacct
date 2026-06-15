package com.iljin.apiServer.ijeas.mobile.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoBoardDto {
    //키워드
    String keyWord;

    //검색시작일자
    String searchDtmFr;
    //검색종료일자
    String searchDtmTo;

    public MoBoardDto(String keyWord, String searchDtmFr, String searchDtmTo) {
        this.keyWord = keyWord;
        this.searchDtmFr = searchDtmFr;
        this.searchDtmTo = searchDtmTo;
    }
}

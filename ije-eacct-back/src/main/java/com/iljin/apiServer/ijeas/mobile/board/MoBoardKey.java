package com.iljin.apiServer.ijeas.mobile.board;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MoBoardKey implements Serializable {
    Long id;
    String compCd;
    String loginId;
}

package com.iljin.apiServer.ijeas.card;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CardUseListKey implements Serializable {

    private static final long serialVersionUID = 3973090930745991202L;

    String compCd;

    String usedNo;

}

package com.iljin.apiServer.ijeas.mobile.menu;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MoMenuKey implements Serializable {
    String compCd;
    String loginId;
    String menuNo;
}

package com.iljin.apiServer.ijeas.slip.etax;

import java.io.Serializable;
import java.math.BigDecimal;

public class XxsbDtiItemKey implements Serializable {
    private static final long serialVersionUID = 8110384322599193130L;

    String conversationId;
    String supbuyType;
    String direction;
    BigDecimal dtiLineNum;
}

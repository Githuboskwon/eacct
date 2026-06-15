package com.iljin.apiServer.ijeas.es.erpViews.gl;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpGlHeadersKey implements Serializable {

    private static final long serialVersionUID = 402699726483723166L;

    Integer orgId;

    BigDecimal jeHeaderId;


}

package com.iljin.apiServer.ijeas.es.erpViews.gl;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpGlLinesKey implements Serializable {

    private static final long serialVersionUID = -5269244714593981166L;

    BigDecimal jeHeaderId;

    Integer jeLineNum;
}

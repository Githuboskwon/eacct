package com.iljin.apiServer.ijeas.es.collection;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SpTrCollectionHdKey implements Serializable {
    private static final long serialVersionUID = 3257181501572039740L;

    String compCd;

    String slipNo;

    String slipType;
}

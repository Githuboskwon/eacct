package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class ErpViewPopFundSlipDto implements Serializable {

    String dealType;

    String productType;

}

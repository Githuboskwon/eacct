package com.iljin.apiServer.ijeas.slipCommon.hierarchy;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CoaHierarchyKey implements Serializable {

	String flexValue;

	BigDecimal coaId;

	String segmentNum;

	String parentFlexValue;
}

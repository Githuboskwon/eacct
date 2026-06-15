package com.iljin.apiServer.ijeas.slip.detail;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SlipTrafficDtKey implements Serializable {

	String compCd;

	String slipNo;

	BigDecimal slipHeaderId;

	String slipSeq;
}

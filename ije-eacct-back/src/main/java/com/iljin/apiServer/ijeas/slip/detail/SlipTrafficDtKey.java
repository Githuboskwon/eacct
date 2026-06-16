package com.iljin.apiServer.ijeas.slip.detail;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SlipTrafficDtKey implements Serializable {

	String compCd;

	String slipNo;

	BigDecimal slipHeaderId;

	String slipSeq;
}

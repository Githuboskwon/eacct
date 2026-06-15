package com.iljin.apiServer.ijeas.slip.prepayment;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PrepaymentApplyKey implements Serializable {

	BigDecimal prepaymentApplyId;

	BigDecimal slipHeaderId;

	Integer orgId;
}

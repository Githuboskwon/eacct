package com.iljin.apiServer.ijeas.slip.header;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlipBusinessTripKey implements Serializable {
	String compCd;
	String masterSlipNo;
	BigDecimal masterSlipHeaderId;
	Integer seq;

	public SlipBusinessTripKey(String compCd, String masterSlipNo, BigDecimal masterSlipHeaderId,
		Integer seq) {
		this.compCd = compCd;
		this.masterSlipNo = masterSlipNo;
		this.masterSlipHeaderId = masterSlipHeaderId;
		this.seq = seq;
	}
}

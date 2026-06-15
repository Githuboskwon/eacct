package com.iljin.apiServer.ijeas.slip;

import lombok.Data;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlipHeaderKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;
	String compCd;
	String slipNo;

	public SlipHeaderKey(String compCd, String slipNo) {
		this.compCd = compCd;
		this.slipNo = slipNo;
	}
}

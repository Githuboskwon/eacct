package com.iljin.apiServer.ijeas.slip.header;

import lombok.Data;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SlipTrafficHdKey implements Serializable {
	String compCd;
	String slipNo;

	public SlipTrafficHdKey(String compCd, String slipNo) {
		this.compCd = compCd;
		this.slipNo = slipNo;
	}
}

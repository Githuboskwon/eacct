package com.iljin.apiServer.ijeas.slip;

import lombok.Data;

import java.io.Serializable;

@Data
public class SlipDetailKey implements Serializable {
	String compCd;
	String slipNo;
	String slipSeq;
	String bungaeNo;
}

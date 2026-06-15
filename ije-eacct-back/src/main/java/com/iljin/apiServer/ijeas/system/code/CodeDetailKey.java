package com.iljin.apiServer.ijeas.system.code;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CodeDetailKey implements Serializable {
	String groupCd;
	String detailCd;
	String compCd;
}

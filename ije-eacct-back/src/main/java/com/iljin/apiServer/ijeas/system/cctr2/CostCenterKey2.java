package com.iljin.apiServer.ijeas.system.cctr2;

import lombok.Data;

import java.io.Serializable;

@Data
public class CostCenterKey2 implements Serializable {

	private static final long serialVersionUID = -5540871256152307952L;

	String deptCd;
	String compCd;
	
}

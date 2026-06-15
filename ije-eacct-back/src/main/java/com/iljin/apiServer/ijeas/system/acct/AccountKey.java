package com.iljin.apiServer.ijeas.system.acct;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountKey implements Serializable {

	private static final long serialVersionUID = -5540871256152307952L;

	String compCd;

	String trxTypeCd;

	String acctCd;

	String drCr;
}

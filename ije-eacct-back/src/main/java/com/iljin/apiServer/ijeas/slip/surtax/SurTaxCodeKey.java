package com.iljin.apiServer.ijeas.slip.surtax;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SurTaxCodeKey implements Serializable {

	private static final long serialVersionUID = 2903347160331141789L;

	String taxRateCode;

	Integer taxRateId;

	Integer ledgerId;

	String taxEvidenceType;
}

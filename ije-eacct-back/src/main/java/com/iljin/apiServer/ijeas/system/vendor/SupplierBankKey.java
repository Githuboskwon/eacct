package com.iljin.apiServer.ijeas.system.vendor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SupplierBankKey implements Serializable {

    private static final long serialVersionUID = -8559950571337278200L;
    String compCd;
    String integrationVendorNum;

    @Builder
    public SupplierBankKey(String compCd, String integrationVendorNum) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
    }

    /* Default Constructor */
    public SupplierBankKey() {

    }
}


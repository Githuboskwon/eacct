package com.iljin.apiServer.ijeas.system.vendor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VendorKey implements Serializable {
    private static final long serialVersionUID = -4494001889505998130L;
    String compCd;
    String integrationVendorNum;

    @Builder
    public VendorKey(String compCd, String integrationVendorNum) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
    }

    /* Default Constructor */
    public VendorKey() {

    }
}

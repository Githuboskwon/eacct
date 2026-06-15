package com.iljin.apiServer.ijeas.system.customer;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerKey implements Serializable {

    private static final long serialVersionUID = -5539518063863647192L;

    String compCd;
    String integrationVendorNum;

    public CustomerKey(String compCd, String integrationVendorNum) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
    }
}

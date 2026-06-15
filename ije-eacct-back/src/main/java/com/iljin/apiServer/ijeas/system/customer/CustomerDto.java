package com.iljin.apiServer.ijeas.system.customer;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 4177999854970798563L;

    String compCd;
    String integrationVendorNum;
    Long customerId;
    String customerNum;
    String customerName;
    String vatRegistrationNum;

    @QueryProjection
    public CustomerDto(String compCd, String integrationVendorNum, Long customerId,
        String customerNum,
        String customerName, String vatRegistrationNum) {
        this.compCd = compCd;
        this.integrationVendorNum = integrationVendorNum;
        this.customerId = customerId;
        this.customerNum = customerNum;
        this.customerName = customerName;
        this.vatRegistrationNum = vatRegistrationNum;
    }
}


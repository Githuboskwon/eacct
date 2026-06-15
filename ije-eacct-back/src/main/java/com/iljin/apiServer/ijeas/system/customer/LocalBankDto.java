package com.iljin.apiServer.ijeas.system.customer;

import com.querydsl.core.annotations.QueryProjection;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocalBankDto implements Serializable {

    private static final long serialVersionUID = 9182693156891019479L;

    String integrationVendorNum;
    String integrationVendorName;

    @QueryProjection
    public LocalBankDto(String integrationVendorNum, String integrationVendorName) {
        this.integrationVendorNum = integrationVendorNum;
        this.integrationVendorName = integrationVendorName;
    }
}

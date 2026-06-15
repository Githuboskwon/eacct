package com.iljin.apiServer.ijeas.system.customer;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class CustomerPageResponse implements Serializable {

    private static final long serialVersionUID = -1293350903540184121L;

    List<CustomerDto> customerList;

    int totalPageCount;

    int currentPageCount;

    public CustomerPageResponse(List<CustomerDto> customerList, int totalPageCount,
        int currentPageCount) {
        this.customerList = customerList;
        this.totalPageCount = totalPageCount;
        this.currentPageCount = currentPageCount;
    }

    public static CustomerPageResponse from(Page<CustomerDto> customers) {
        return new CustomerPageResponse(
            customers.getContent(),
            customers.getTotalPages(),
            customers.getNumber()
        );
    }
}

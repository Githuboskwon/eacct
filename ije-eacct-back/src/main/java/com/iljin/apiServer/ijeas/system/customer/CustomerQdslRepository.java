package com.iljin.apiServer.ijeas.system.customer;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerQdslRepository {

    Page<CustomerDto> getCustomerList(String value, Pageable pageable);

    List<LocalBankDto> getLocalBankList(String value);

}

package com.iljin.apiServer.ijeas.system.customer;

import com.iljin.apiServer.core.util.Util;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerQdslRepository customerQdslRepository;

    private final Util util;

    @Override
    public CustomerPageResponse getCustomerList(String value, Pageable pageable) {
        Page<CustomerDto> pages = customerQdslRepository.getCustomerList(value, pageable);
        return CustomerPageResponse.from(pages);

    }

    @Override
    public List<LocalBankDto> getLocalBankList(String value) {
        return customerQdslRepository.getLocalBankList(value);
    }

}

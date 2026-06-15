package com.iljin.apiServer.ijeas.sm.bizTrip;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BusinessTripDisService {

    @Transactional(readOnly = true)
    List<BusinessTripDisDto> getBusinessTripDisList(BusinessTripDisDto businessTripDisDto);

    List<BusinessTripDisDto> getBusinessTripDisSlip(BusinessTripDisDto businessTripDisDto);

    @Modifying
    @Transactional
    String saveBusinessTripDisList(List<BusinessTripDisDto> businessTripDisDtos);

    @Modifying
    @Transactional
    String deleteBusinessTripDis(BusinessTripDisDto businessTripDisDto);
}

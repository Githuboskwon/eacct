package com.iljin.apiServer.ijeas.sm.bizTrip;

import java.util.List;

public interface BusinessTripDisQdslRepository{

    List<BusinessTripDisDto> getBusinessTripDisList(BusinessTripDisDto businessTripDisDto);

    List<BusinessTripDisDto> getBusinessTripDisSlip(BusinessTripDisDto businessTripDisDto);
}

package com.iljin.apiServer.ijeas.slip.header;


import java.util.List;

public interface SlipBusinessTripQdslRepository {
    List<SlipBusinessTripDto> getBusinessTripList(SlipBusinessTripDto slipBusinessTripDto);

}

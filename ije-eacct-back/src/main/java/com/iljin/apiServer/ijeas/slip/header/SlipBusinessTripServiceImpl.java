package com.iljin.apiServer.ijeas.slip.header;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class SlipBusinessTripServiceImpl implements SlipBusinessTripService {

    private final SlipBusinessTripQdslRepository slipBusinessTripQdslRepository;

    @Override
    public List<SlipBusinessTripDto> getBusinessTripList(SlipBusinessTripDto slipBusinessTripDto){
        return slipBusinessTripQdslRepository.getBusinessTripList(slipBusinessTripDto);
    }

}

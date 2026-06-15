package com.iljin.apiServer.ijeas.sm.bizTrip;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BusinessTripDisServiceImpl implements BusinessTripDisService{

    private final BusinessTripDisQdslRepository businessTripDisQdslRepository;
    private final BusinessTripDisRepository businessTripDisRepository;
    private final Util util;

    @Override
    public List<BusinessTripDisDto> getBusinessTripDisList(BusinessTripDisDto businessTripDisDto) {
        return businessTripDisQdslRepository.getBusinessTripDisList(businessTripDisDto);
    }

    @Override
    public List<BusinessTripDisDto> getBusinessTripDisSlip(BusinessTripDisDto businessTripDisDto){
        return businessTripDisQdslRepository.getBusinessTripDisSlip(businessTripDisDto);
    }

    @Override
    public String saveBusinessTripDisList(List<BusinessTripDisDto> businessTripDisDtos) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        for (BusinessTripDisDto businessTripDisDto : businessTripDisDtos) {
            String standardYymm = businessTripDisDto.getStandardYymm();
            if (!standardYymm.isEmpty()) {
                standardYymm = standardYymm.replaceAll("-", "");
            }
            BusinessTripDisKey businessTripDisKey = BusinessTripDisKey.builder()
                    .compCd(businessTripDisDto.getCompCd())
                    .standardYymm(standardYymm)
                    .departureArrivalArea(businessTripDisDto.getDepartureArrivalArea())
                    .build();

            Optional<BusinessTripDis> result = businessTripDisRepository.findById(businessTripDisKey);
            if (result.isPresent()) {
                result.ifPresent(b -> {
                    b.update(businessTripDisDto);
                });
            } else {
                Optional<BusinessTripDis> duplicated = businessTripDisRepository.findByStandardYymmAndDepartureArrivalArea(businessTripDisDto.getStandardYymm(), businessTripDisDto.getDepartureArrivalArea());
                if(duplicated.isPresent()) {
                    throw new RuntimeException("기준년월(" + businessTripDisDto.getStandardYymm() + ")에 출발도착지(" + businessTripDisDto.getDepartureArrivalArea() + ")가 있어 추가할 수 없습니다.");
                }

                BusinessTripDis businessTripDis = BusinessTripDis.builder()
                        .compCd(compCd)
                        .standardYymm(businessTripDisDto.getStandardYymm())
                        .departureArrivalArea(businessTripDisDto.getDepartureArrivalArea())
                        .distance(businessTripDisDto.getDistance())
                        .remark(businessTripDisDto.getRemark())
                        .useYn(businessTripDisDto.getUseYn())
                        .build();
                businessTripDisRepository.save(businessTripDis);
            }
        }
        return "저장되었습니다.";
    }

    @Override
    public String deleteBusinessTripDis(BusinessTripDisDto businessTripDisDto) {
        String compCd = businessTripDisDto.getCompCd();
        String standardYymm = businessTripDisDto.getStandardYymm();
        String depatureArrivalArea = businessTripDisDto.getDepartureArrivalArea();

        if(!standardYymm.isEmpty()) {
            standardYymm = standardYymm.replaceAll("-", "");
        }

        BusinessTripDisKey businessTripDisKey = BusinessTripDisKey.builder()
                .compCd(compCd)
                .standardYymm(standardYymm)
                .departureArrivalArea(depatureArrivalArea)
                .build();
        if(!businessTripDisRepository.existsById(businessTripDisKey)) {
            throw new RuntimeException("삭제할 출장거리가 존재하지 않습니다.");
        }

        businessTripDisRepository.deleteById(businessTripDisKey);
        return "삭제되었습니다.";
    }

}

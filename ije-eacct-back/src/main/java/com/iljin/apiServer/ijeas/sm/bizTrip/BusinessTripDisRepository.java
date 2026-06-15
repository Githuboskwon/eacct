package com.iljin.apiServer.ijeas.sm.bizTrip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTripDisRepository extends JpaRepository<BusinessTripDis, BusinessTripDisKey> {
    Optional<BusinessTripDis> findByStandardYymmAndDepartureArrivalArea(String standardYymm, String departureArrivalArea);

}

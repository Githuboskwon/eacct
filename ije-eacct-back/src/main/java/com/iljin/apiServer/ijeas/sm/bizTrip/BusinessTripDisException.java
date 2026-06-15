package com.iljin.apiServer.ijeas.sm.bizTrip;

class BusinessTripDisException extends RuntimeException{

    private static final long serialVersionUID = 2301875561143637891L;

    BusinessTripDisException() {
        super();
    }

    BusinessTripDisException(String msg) {
        super(msg);
    }
}

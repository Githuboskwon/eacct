package com.iljin.apiServer.ijeas.system.payBank;

public class PayBankException extends RuntimeException{

    private static final long serialVersionUID = -329875062412311795L;

    PayBankException() {
        super();
    }

    PayBankException(String msg) {
        super(msg);
    }
}

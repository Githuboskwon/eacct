package com.iljin.apiServer.ijeas.approval.dlgt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalDelegateKey implements Serializable {
    private static final long serialVersionUID = 2083029313556201717L;

    String adlgId;
    String actId;
    Short adlgSeq;
}

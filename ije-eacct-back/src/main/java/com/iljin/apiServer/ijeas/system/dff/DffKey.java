package com.iljin.apiServer.ijeas.system.dff;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class DffKey implements Serializable {
    private static final long serialVersionUID = -3600636194820614989L;

    String applicationShortCd;
    String acctCd;
    String mngItemCd;
}

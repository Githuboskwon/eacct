package com.iljin.apiServer.ijeas.system.expend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpendKey implements Serializable {
    private static final long serialVersionUID = 5596999969800268464L;

    String compCd;
    String expendCd;
    String startDate;
}

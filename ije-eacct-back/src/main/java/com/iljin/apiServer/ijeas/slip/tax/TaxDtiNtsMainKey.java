package com.iljin.apiServer.ijeas.slip.tax;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class TaxDtiNtsMainKey implements Serializable {

    private static final long serialVersionUID = -3844487304083246719L;

    String issueId;

    String supbuyType;

    @Builder
    public TaxDtiNtsMainKey(String issueId, String supbuyType) {
        this.issueId = issueId;
        this.supbuyType = supbuyType;
    }
}

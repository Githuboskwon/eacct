package com.iljin.apiServer.ijeas.approval.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRuleKey implements Serializable {
    private static final long serialVersionUID = 9075902817484123172L;

    String compCd;
    String docTypeCd;
    String dtlTypeCd;
    String curCd;
    Short ruleSeq;

}

package com.iljin.apiServer.ijeas.sm.apprLine;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApprLineDtKey implements Serializable{
        private static final long serialVersionUID = 504633445397083443L;
        String compCd;
        String userId;
        Integer apprSeq;
        Integer subApprSeq;

        @Builder
        public ApprLineDtKey(String compCd, String userId, Integer apprSeq, Integer subApprSeq) {
                this.compCd = compCd;
                this.userId = userId;
                this.apprSeq = apprSeq;
                this.subApprSeq = subApprSeq;
        }
}

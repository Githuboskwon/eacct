package com.iljin.apiServer.ijeas.sm.apprLine;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApprLineKey implements Serializable{
        private static final long serialVersionUID = -7148441688774210050L;
        String compCd;
        String userId;
        Integer apprSeq;

        @Builder
        public ApprLineKey(String compCd, String userId, Integer apprSeq) {
                this.compCd = compCd;
                this.userId = userId;
                this.apprSeq = apprSeq;
        }
}

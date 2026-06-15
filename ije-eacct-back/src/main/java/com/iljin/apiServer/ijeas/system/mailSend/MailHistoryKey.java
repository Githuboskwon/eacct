package com.iljin.apiServer.ijeas.system.mailSend;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailHistoryKey implements Serializable {
    private static final long serialVersionUID = 2154213335360320929L;

    String mailSendDt;
    BigDecimal mailSeq;
    String mailTypeCd;

    public MailHistoryKey(String mailSendDt, BigDecimal mailSeq, String mailTypeCd) {
        this.mailSendDt = mailSendDt;
        this.mailSeq = mailSeq;
        this.mailTypeCd = mailTypeCd;
    }
}

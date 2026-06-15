package com.iljin.apiServer.ijeas.system.mailSend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MailHistoryDto implements Serializable {
    private static final long serialVersionUID = 7682776652974378656L;

    String searchDt;
    String statusCd;
    String compCd;
    String mailSendDt;
    String mailSeq;
    String mailTypeCd;
    String empNo;
    String empNm;
    String deptCd;
    String deptNm;
    String jobCd;
    String jobNm;
    BigDecimal count;
    String sendUserNm;
    List<MailHistoryDto> sendList;
    String subject;
    String text;


    // 미상신 전표 사원 리스트
    public MailHistoryDto(String compCd, String empNo, BigDecimal count, String empNm, String deptCd,
        String deptNm, String jobCd, String jobNm, String sendUserNm, String mailSendDt) {
        this.compCd = compCd;
        this.empNo = empNo;
        this.empNm = empNm;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.jobCd = jobCd;
        this.jobNm = jobNm;
        this.count = count;
        this.sendUserNm = sendUserNm;
        this.mailSendDt = mailSendDt;
    }
}

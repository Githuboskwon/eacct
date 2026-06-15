package com.iljin.apiServer.core.cert;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@AllArgsConstructor
@Data
public class CertFileDto implements Serializable {
    Integer fileId;

    String effectStDt;

    String effectEndDt;

    String pw;

    String pwConfirm;

    String certName;

    String remark;

    BigInteger fileCnt;

    String regId;

    String regDtm;

    String chgId;

    String chgDtm;

    String searchDate;

    String pwFake;

    String pwConfirmFake;

    public CertFileDto(Integer fileId,
                       String effectStDt,
                       String effectEndDt,
                       String pwFake,
                       String pwConfirmFake,
                       String certName,
                       String remark,
                       BigInteger fileCnt,
                       String regId,
                       String regDtm,
                       String chgId,
                       String chgDtm) {
        this.fileId = fileId;
        this.effectStDt = effectStDt;
        this.effectEndDt = effectEndDt;
        this.pwFake = pwFake;
        this.pwConfirmFake = pwConfirmFake;
        this.certName = certName;
        this.remark = remark;
        this.fileCnt = fileCnt;
        this.regId = regId;
        this.regDtm = regDtm;
        this.chgId = chgId;
        this.chgDtm = chgDtm;
    }

    //기본 생성자 추가
    public CertFileDto() {

    }
}

package com.iljin.apiServer.ijeas.system.acct;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AccountDto implements Serializable {
    private static final long serialVersionUID = -5099137579326444329L;

    String compCd;
    String trxTypeCd;
    String acctCd;
    String acctNm;
    String acctType;
    String enabledFlag;
    String drCr;
    String drcrType;
    String interfaceSlipType;
    String interfaceSlipTypeNm;
    String acctModule;
    String interfaceModule;
    String checkFlag;
    String acctAttribute1;
    String acctAttribute2;
    String assetsTrackingFlag;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    String attribute6;
    String attribute7;
    String attribute8;
    String attribute9;
    String attribute10;
    String attribute11;
    String attribute12;
    String attribute13;
    String attribute14;
    String attribute15;

    String deptCd;

    @QueryProjection
    public AccountDto(String compCd, String trxTypeCd, String acctCd, String acctNm, String acctType, String enabledFlag, String drCr, String drcrType,
                      String interfaceSlipType, String interfaceSlipTypeNm, String acctModule, String interfaceModule, String checkFlag, String acctAttribute1,
                      String acctAttribute2, String assetsTrackingFlag, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6,
                      String attribute7, String attribute8, String attribute9, String attribute10, String attribute11, String attribute12, String attribute13,
                      String attribute14, String attribute15) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.acctType = acctType;
        this.enabledFlag = enabledFlag;
        this.drCr = drCr;
        this.drcrType = drcrType;
        this.interfaceSlipType = interfaceSlipType;
        this.interfaceSlipTypeNm = interfaceSlipTypeNm;
        this.acctModule = acctModule;
        this.interfaceModule = interfaceModule;
        this.checkFlag = checkFlag;
        this.acctAttribute1 = acctAttribute1;
        this.acctAttribute2 = acctAttribute2;
        this.assetsTrackingFlag = assetsTrackingFlag;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.attribute11 = attribute11;
        this.attribute12 = attribute12;
        this.attribute13 = attribute13;
        this.attribute14 = attribute14;
        this.attribute15 = attribute15;
    }

    Long dffCnt;
    Long requiredFlagCnt;
    String deptType;

    @QueryProjection
    public AccountDto(String compCd, String trxTypeCd, String acctCd, String acctNm, String acctType, String enabledFlag, String drCr, String drcrType, String acctModule,
                      String acctAttribute1, String assetsTrackingFlag,  Long dffCnt, Long requiredFlagCnt, String deptType, String interfaceSlipType) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.acctType = acctType;
        this.enabledFlag = enabledFlag;
        this.drCr = drCr;
        this.drcrType = drcrType;
        this.acctModule = acctModule;
        this.acctAttribute1 = acctAttribute1;
        this.assetsTrackingFlag = assetsTrackingFlag;
        this.dffCnt = dffCnt;
        this.requiredFlagCnt = requiredFlagCnt;
        this.deptType = deptType;
        this.interfaceSlipType = interfaceSlipType;
    }

    @QueryProjection
    public AccountDto(String compCd, String acctCd, String acctNm, String assetsTrackingFlag) {
        this.compCd = compCd;
        this.acctCd = acctCd;
        this.acctNm = acctNm;
        this.assetsTrackingFlag = assetsTrackingFlag;
    }

    String childTrxTypeCd;
    String childTrxTypeNm;

    @QueryProjection
    public AccountDto(String compCd, String trxTypeCd, String childTrxTypeCd, String childTrxTypeNm, String attribute1, String attribute2, String attribute3,
                      String attribute4, String attribute5) {
        this.compCd = compCd;
        this.trxTypeCd = trxTypeCd;
        this.childTrxTypeCd = childTrxTypeCd;
        this.childTrxTypeNm = childTrxTypeNm;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public AccountDto(String acctCd, String acctNm) {
        this.acctCd = acctCd;
        this.acctNm = acctNm;
    }
}

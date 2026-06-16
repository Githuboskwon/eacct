package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Table(name = "RA_CUSTOMER_TRX_ALL")
@IdClass(RaCustomerTrxAllKey.class)
@Entity
public class RaCustomerTrxAll{
    @Id
    @Column(name = "CUSTOMER_TRX_ID")
    BigDecimal customerTrxId;

    @Column(name = "TRX_NUMBER")
    String trxNumber;

    @Column(name = "COMPLETE_FLAG")
    String completeFlag;

    @Column(name = "GLOBAL_ATTRIBUTE4")
    String globalAttribute4;

    @Column(name = "GLOBAL_ATTRIBUTE9")
    String globalAttribute9;

    @Column(name = "GLOBAL_ATTRIBUTE10")
    String globalAttribute10;

    @Column(name = "GLOBAL_ATTRIBUTE21")
    String globalAttribute21;

    @Column(name = "GLOBAL_ATTRIBUTE22")
    String globalAttribute22;

    @Column(name = "GLOBAL_ATTRIBUTE23")
    String globalAttribute23;

    @Column(name = "LAST_UPDATE_DATE")
    String lastUpdateDate;

    @Column(name = "LAST_UPDATED_BY")
    String lastUpdatedBy;
}

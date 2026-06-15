package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "CBO_AR_TRX_MERGE")
@IdClass(CboArTrxMergeKey.class)
@Entity
public class CboArTrxMerge{
    @Id
    @Column(name = "ETAX_ISSUE_ID")
    BigDecimal etaxIssueId;

    @Id
    @Column(name = "CUSTOMER_TRX_ID")
    BigDecimal customerTrxId;

    @Column(name = "DELETE_FLAG")
    String deleteFlag;

    @Column(name = "CREATED_PERSON_ID")
    BigDecimal createdPersonId;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_PERSON_ID")
    BigDecimal lastUpdatedPersonId;

    @Column(name = "LAST_UPDATE_DATE")
    LocalDateTime lastUpdateDate;

    @Column(name = "ETAX_EXCLUDE_FLAG")
    String etaxExcludeFlag;
}

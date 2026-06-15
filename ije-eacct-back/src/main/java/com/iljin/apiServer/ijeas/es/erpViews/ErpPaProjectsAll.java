package com.iljin.apiServer.ijeas.es.erpViews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Immutable
@Table(name = "PA_PROJECTS_ALL")
@Entity
public class ErpPaProjectsAll {

    @Id
    @Column(name = "PROJECT_ID")
    BigDecimal projectId;

    @Column(name = "NAME")
    String name;

    @Column(name = "SEGMENT1")
    String segment1;

    @Column(name = "LAST_UPDATE_DATE")
    String lastUpdateDate;

    @Column(name = "LAST_UPDATED_BY")
    String lastUpdatedBy;

    @Column(name = "CREATION_DATE")
    String creationDate;

    @Column(name = "CREATED_BY")
    String createdBy;

    @Column(name = "LAST_UPDATE_LOGIN")
    String lastUpdateLogin;

    @Column(name = "PROJECT_TYPE")
    String projectType;

    @Column(name = "CARRYING_OUT_ORGANIZATION_ID")
    String carryingOutOrganizationId;

    @Column(name = "PUBLIC_SECTOR_FLAG")
    String publicSectorFlag;

    @Column(name = "PROJECT_STATUS_CODE")
    String projectStatusCode;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "START_DATE")
    String startDate;

    @Column(name = "COMPLETION_DATE")
    String completionDate;

    @Column(name = "CLOSED_DATE")
    String closedDate;

    @Column(name = "DISTRIBUTION_RULE")
    String distributionRule;

    @Column(name = "LABOR_INVOICE_FORMAT_ID")
    String laborInvoiceFormatId;

    @Column(name = "NON_LABOR_INVOICE_FORMAT_ID")
    String nonLaborInvoiceFormatId;

    @Column(name = "RETENTION_INVOICE_FORMAT_ID")
    String retentionInvoiceFormatId;

    @Column(name = "RETENTION_PERCENTAGE")
    String retentionPercentage;

    @Column(name = "BILLING_OFFSET")
    String billingOffset;

    @Column(name = "BILLING_CYCLE")
    String billingCycle;

    @Column(name = "ORG_ID")
    Integer orgId;

    /* 컬럼이 총 226개인데, 실제 사용하는 컬럼은 한개임 (비용예산 프로젝트 팝업 서브 쿼리,PROJECT_ID), 필요시 추가 할것.*/

}

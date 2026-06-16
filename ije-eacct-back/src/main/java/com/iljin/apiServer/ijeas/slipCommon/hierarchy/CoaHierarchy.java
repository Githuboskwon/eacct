package com.iljin.apiServer.ijeas.slipCommon.hierarchy;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "CBO_GL_COA_HIERARCHY_V")
@IdClass(CoaHierarchyKey.class)
public class CoaHierarchy{

	@Id
	@Column(name = "FLEX_VALUE" , nullable=false)
	String flexValue;

	@Column(name = "FLEX_VALUE_DESC")
	String flexValueDesc;

	@Column(name = "ACCT_TYPE")
	String acctType;

	@Id
	@Column(name = "COA_ID" , nullable=false)
	BigDecimal coaId;

	@Id
	@Column(name = "SEGMENT_NUM" , nullable=false)
	String segmentNum;

	@Column(name = "SEGMENT_NAME" , nullable=false)
	String segmentName;

	@Column(name = "APPLICATION_COLUMN_NAME" , nullable=false)
	String applicationColumnName;

	@Column(name = "START_DATE_ACTIVE")
	LocalDateTime startDateActive;

	@Column(name = "END_DATE_ACTIVE")
	LocalDateTime endDateActive;

	@Column(name = "ENABLED_FLAG" , nullable=false)
	String enabledFlag;

	@Column(name = "HIERARCHY_CODE" , nullable=false)
	String hierarchyCode;

	@Id
	@Column(name = "PARENT_FLEX_VALUE" , nullable=false)
	String parentFlexValue;

	@Column(name = "HIERARCHY_LEVEL")
	Integer hierarchyLevel;


	@Builder
	public CoaHierarchy(String flexValue,String flexValueDesc,String acctType,BigDecimal coaId,
						String segmentNum,String segmentName,String applicationColumnName,
						LocalDateTime startDateActive,LocalDateTime endDateActive,String enabledFlag,
						String hierarchyCode,String parentFlexValue,Integer hierarchyLevel) {
		this.flexValue = flexValue;
		this.flexValueDesc = flexValueDesc;
		this.acctType = acctType;
		this.coaId = coaId;
		this.segmentNum = segmentNum;
		this.segmentName = segmentName;
		this.applicationColumnName = applicationColumnName;
		this.startDateActive = startDateActive;
		this.endDateActive = endDateActive;
		this.enabledFlag = enabledFlag;
		this.hierarchyCode = hierarchyCode;
		this.parentFlexValue = parentFlexValue;
		this.hierarchyLevel = hierarchyLevel;
	}
}

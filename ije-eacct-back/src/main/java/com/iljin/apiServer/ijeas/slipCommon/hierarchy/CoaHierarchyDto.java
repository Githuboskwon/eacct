package com.iljin.apiServer.ijeas.slipCommon.hierarchy;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class CoaHierarchyDto {

	String flexValue;

	String flexValueDesc;

	String acctType;

	BigDecimal coaId;

	String segmentNum;

	String segmentName;

	String applicationColumnName;

	LocalDateTime startDateActive;

	LocalDateTime endDateActive;

	String enabledFlag;

	String hierarchyCode;

	String parentFlexValue;

	Integer hierarchyLevel;


	@QueryProjection
	public CoaHierarchyDto(String flexValue, String flexValueDesc, String acctType, BigDecimal coaId,
                           String segmentNum, String segmentName, String applicationColumnName,
                           LocalDateTime startDateActive, LocalDateTime endDateActive, String enabledFlag,
                           String hierarchyCode, String parentFlexValue, Integer hierarchyLevel) {
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

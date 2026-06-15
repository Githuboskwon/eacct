package com.iljin.apiServer.ijeas.slip.hr;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
public class SlipHrDto implements Serializable {
    private static final long serialVersionUID = -1087546522619086804L;

    Integer orgId;
    Integer ledgerId;
    Integer payrollBatchId;
    String batchPeriod;
    String payrollBatchName;
    String uploadEmpNo;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    String createdEmpNo;
    String creationDate;
    String lastUpdatedEmpNo;
    String lastUpdatedDate;

    //채번용 ID
    BigDecimal tempPayrollBatchId;


    String slipDate;
    String payrollTypeCode;
    String payrollTypeName;
    String uploadTitle;
    String retireEmpNo;
    String coaSegment3;
    String coaSegment4;
    String coaSegment5;
    BigDecimal payrollAmount;
    String lineDescription;
    String slipIfFlag;
    Integer slipHeaderId;
    LocalDateTime slipIfDate;
    String errorMsg;
    Integer payrollTypeId;
    String payrollType;
    String payrollItem;
    String deductionType;
    String intVendorNum;
    Integer payTermId;
    String accountCr;
    String attribute6;
    String attribute7;
    String attribute8;
    String attribute9;
    String attribute10;
    String termName;
    String bankAccountName;
    String dueDate;


    @QueryProjection
    public SlipHrDto(Integer orgId, Integer ledgerId, Integer payrollBatchId, String batchPeriod, String payrollBatchName, String uploadEmpNo, String attribute1,
                     String attribute2, String attribute3, String attribute4, String attribute5, String createdEmpNo, String creationDate, String lastUpdatedEmpNo,
                     String lastUpdatedDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.batchPeriod = batchPeriod;
        this.payrollBatchName = payrollBatchName;
        this.uploadEmpNo = uploadEmpNo;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    String validationFlag;

    @QueryProjection
    public SlipHrDto(Integer orgId, Integer ledgerId, Integer payrollBatchId, String uploadEmpNo, String validationFlag) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.uploadEmpNo = uploadEmpNo;
        this.validationFlag = validationFlag;
    }

    @QueryProjection
    public SlipHrDto(Integer orgId, Integer payrollBatchId, String uploadEmpNo) {
        this.orgId = orgId;
        this.payrollBatchId = payrollBatchId;
        this.uploadEmpNo = uploadEmpNo;
    }

    @QueryProjection
    public SlipHrDto(BigDecimal tempPayrollBatchId){
        this.tempPayrollBatchId = tempPayrollBatchId;
    }

    @QueryProjection
    public SlipHrDto(Integer orgId, Integer ledgerId, Integer payrollBatchId, String batchPeriod, String payrollBatchName, String uploadEmpNo, String createdEmpNo,
                     String creationDate, String lastUpdatedEmpNo, String lastUpdatedDate, String slipDate, String payrollTypeCode, String payrollTypeName,
                     String uploadTitle, String retireEmpNo, String coaSegment3, String coaSegment4, String coaSegment5, BigDecimal payrollAmount, String lineDescription,
                     String termName, String bankAccountName, String dueDate) {
        this.orgId = orgId;
        this.ledgerId = ledgerId;
        this.payrollBatchId = payrollBatchId;
        this.batchPeriod = batchPeriod;
        this.payrollBatchName = payrollBatchName;
        this.uploadEmpNo = uploadEmpNo;
        this.createdEmpNo = createdEmpNo;
        this.creationDate = creationDate;
        this.lastUpdatedEmpNo = lastUpdatedEmpNo;
        this.lastUpdatedDate = lastUpdatedDate;
        this.slipDate = slipDate;
        this.payrollTypeCode = payrollTypeCode;
        this.payrollTypeName = payrollTypeName;
        this.uploadTitle = uploadTitle;
        this.retireEmpNo = retireEmpNo;
        this.coaSegment3 = coaSegment3;
        this.coaSegment4 = coaSegment4;
        this.coaSegment5 = coaSegment5;
        this.payrollAmount = payrollAmount;
        this.lineDescription = lineDescription;
        this.termName = termName;
        this.bankAccountName = bankAccountName;
        this.dueDate = dueDate;
    }

}
